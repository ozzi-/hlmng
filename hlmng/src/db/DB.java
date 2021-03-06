package db;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import log.Log;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.owasp.esapi.ESAPI;

/**
 * This class handles the creation of a DB connection and allows building 'dynamic' prepared statements.
 */
public class DB {

	/**
	 * Here we list field names, which we don't want to be used in the queries, those
	 * fields are injected by the code and don't really belong to the model.
	 * Example: Instead of only sending the client a userIDFK, we send the name of said IDFK too.
	 * That way the client has to perform one call less!
	 */
	@SuppressWarnings("serial")
	private static ArrayList<String> dontMapFields = new ArrayList<String>() {{
	    add("media");
	    add("authorName");
	}};
	/**
	 * Here are entries we don't wan't to be escaped by the ESAPI.
	 * These entries are checked by SafeValues.isSafeString.
	 * If the check fails they will be escaped.
	 */
	@SuppressWarnings("serial")
	private static ArrayList<SafeValues> safeValuesMap = new ArrayList<SafeValues>() {	{
		add(new SafeValues(8, "(([0-1][0-9])|([2][0-3])):([0-5][0-9]):([0-5][0-9])")); // HH:MM:ss
		add(new SafeValues(5, "(([0-1][0-9])|([2][0-3])):([0-5][0-9])")); // HH:MM
		add(new SafeValues(10, "[0-9]{4}-(0[1-9]|1[012])-(0[1-9]|1[0-9]|2[0-9]|3[01])")); //  YYYY-MM-DD
		add(new SafeValues(19, "[0-9]{4}-(0[1-9]|1[012])-(0[1-9]|1[0-9]|2[0-9]|3[01]) (([0-1][0-9])|([2][0-3])):([0-5][0-9]):([0-5][0-9])")); //  YYYY-MM-DD HH:MM:ss
		add(new SafeValues(9, "image/png"));
		add(new SafeValues(10, "image/jpeg")); 	
	}};

		
		
	/**
	 * See WEB-INF/web.xml and WEB-INF/context.xml for DB context settings
	 * DO NOT use this directly, use the class dao, since it contains the logic for testing and more.
	 * @return A connection from the connection pool, the caller has to handle the closure of it properly!
	 * @throws SQLException
	 * @throws NamingException
	 */
	public static Connection getConnection() throws SQLException, NamingException{
		Context initContext;
		initContext = new InitialContext();
		Context envContext = (Context) initContext.lookup("java:comp/env");
		DataSource ds = (DataSource) envContext.lookup("jdbc/hlmng");
		return ds.getConnection();
	}
	
	/**
	 * Use this to build a prepared statement where you only have to set an ID (= parameter)
	 * Example: Select Element or Delete Element
	 * @param PreparedStatement which you want to set the fields
	 * @param The ID value which will be set as first field in the prepared statement
	 * @return The prepared Statement
	 */
	public static <T> PreparedStatement setIdFieldOfPS(PreparedStatement ps, int id) {
		try {
			ps.setObject(1,id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ps;
	}


	/**
	 * @param Result Set of the query returning (hopefully only one) object
	 * @param Add the type class (Name.class) of the desired object so it can be dynamically built
	 * @return The first object from the result set
	 */
	public static <T> T getObjectFromRS(ResultSet resultSet, Class<T> type) {
		T instance=null;
		try {
			instance = type.newInstance();
			for (Field field : type.getDeclaredFields()) {
				if(!(dontMapFields.contains(field.getName()))){ // will be injected later
					Object value = resultSet.getObject(field.getName());
					PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), type);
					Method method = propertyDescriptor.getWriteMethod(); 
					if(value!=null){ // invoking with null throws exception, default will be null anyways
						method.invoke(instance, value);											
					}
				}
			}
		} catch (Exception e) {
			Log.addEntry(Level.WARNING,"Object couldn't be extracted from result set. Class="+type.getSimpleName()+".\r\n"+e.getMessage());
			e.printStackTrace();
		}
		return instance;
	}

	

	/** 
	 * Use this to build a prepared statement where you have to set all fields.
	 * @param The prepared statement which you want to set the values
	 * @param The class type of the provided object
	 * @param The object which will provide the values, all fields will be extracted and set
	 * @param If you are making an insert statement you won't have to add a ID so pass NULL! Else the ID should be the one of the object in the DB which you want to change (... where ID={x} ) 
	 * @return The prepared statement 
	 */
	public static <T> PreparedStatement setAllFieldsOfPS(PreparedStatement ps, Class<?> classType,T tModel,Integer idLastParam,boolean idset) {
		int i=0;
		Object value;
		PropertyDescriptor propertyDescriptor;

		String className= classType.getSimpleName();
		String classID = className.toLowerCase()+"ID";	
		for (Field field : classType.getDeclaredFields()) {
			// ID is set by DB, others injected if idset = false
			if((!classID.toLowerCase().equals(field.getName().toLowerCase())||idset)&& !(dontMapFields.contains(field.getName()))){ 
				try {
					propertyDescriptor = new PropertyDescriptor(field.getName(), classType);
					Method method = propertyDescriptor.getReadMethod();
					value = method.invoke(tModel);
					 // ID's that are 0 have to be turned into null, since we can't use Integer in the Models. There are fields such as deviceID which are string, we don't want those ..
					if(field.getName().contains("ID") && !( value instanceof String)){
						int v = (int) value;
						if (v==0){
							value=null;
						}
					}
					if(value instanceof String){
						boolean isSafe=false;
						for (SafeValues safeValue : safeValuesMap) {
							if(safeValue.isSafeString(value)){
								isSafe=true;
							}
						}
						if(!isSafe){
							value = escapeString(value);
						}
					}
					ps.setObject(++i, value);
				} catch (Exception e) {
					Log.addEntry(Level.SEVERE, "Couldn't build prepared statment! \r\n"+e.getMessage());
					e.printStackTrace();
				}
			}
		}
		if(idLastParam!=null){
			try {
				ps.setObject(++i,idLastParam);
			} catch (Exception e) {
				Log.addEntry(Level.SEVERE, "Couldn't build prepared statment (on last parameter)! \r\n"+e.getMessage());
				e.printStackTrace();
			}
		}
		return ps;
	}

	private static Object escapeString(Object valueP) {
		String value = (String)valueP;
		String valueOld = value;
		value = ESAPI.encoder().canonicalize(value);
		value = value.replaceAll("\0", "");
		value = value.replaceAll("\n", "-newline-");
		value=Jsoup.clean(value, Whitelist.none());
		value = value.replaceAll("-newline-","\n");
		if(!valueOld.equals(value)){
			Log.addEntry(Level.WARNING,"Escaping the string returned different value, possible XSS attack prevented!");
		}
		return value;
	}
	
	public static ArrayList<String> getDontMapFields() {
		return dontMapFields;
	}

}