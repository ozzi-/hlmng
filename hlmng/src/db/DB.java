package db;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.sun.rowset.CachedRowSetImpl;

/**
 * This class handles the creation of a DB connection, allows building dynamic prepared statements.
 * This only works if you are using the same class name and field names as declared in the SQL Database!
 * @author ozgheb
 *
 */
public class DB {
	private static final String dbClassName = "com.mysql.jdbc.Driver";
	private static final String dbRoot = "jdbc:mysql://127.0.0.1/";
	private static final Properties loginData= new Properties();
	private Connection connection;
	
	/**
	 * 
	 * Opens a Database connection where queries can be sent to
	 * @param Database name which you want to open
	 * 
	 */
	public DB(String dbName){
	    try {
			Class.forName(dbClassName);
		} catch (ClassNotFoundException e1) {
			System.err.println("Failed to  load JDBC Driver");
			e1.printStackTrace();
		}
	    loginData.put("user","user");
	    loginData.put("password","pw12");
	    System.out.println("Creating DB Handler for:"+dbName);
	    try {
			connection = DriverManager.getConnection(dbRoot+dbName,loginData);
		} catch (SQLException e) {
			System.err.println("Failed top get connection");
			e.printStackTrace();
		}
	}
	
	
	public void closeDB(){
		try {
			connection.close();
		} catch (SQLException e) {
			//we tried...
			e.printStackTrace();
		}
	}

	
	/**
	 * Use this to build a prepared statement where you only have to set an ID.	
	 * Example: Select Element or Delete Element
	 * @param PreparedStatement which you want to set the fields
	 * @param The ID value which will be set as first field in the prepared statement
	 * @return The prepared Statment
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
	 * Returns (the first) object from the result set
	 * @param resultSet of the query returning (hopefully only one) object
	 * @param Add the type class of the desired document so it can be dynamically built
	 * @return
	 */
	public static <T> T getObjectFromRS(ResultSet resultSet, Class<T> type) {
		T instance=null;
		try {
			instance = type.newInstance();
			for (Field field : type.getDeclaredFields()) {
				Object value = resultSet.getObject(field.getName());
				PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), type);
				Method method = propertyDescriptor.getWriteMethod();
				method.invoke(instance, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}

	

	/** 
	 * Use this to build a prepared statement where you have to set all fields.
	 * @param The prepared statement which you want to set the values
	 * @param The class type of the provided object
	 * @param The object which will provide the values, all fields will be extracted and set
	 * @param If you are making an insert statment you won't have to add a ID so pass NULL! Else the ID should be the one of the object in the DB which you want to change (... where ID={x} ) 
	 * @return The prepared statement 
	 */
	public static <T> PreparedStatement setAllFieldsOfPS(PreparedStatement ps, Class<?> classType,T tModel,Integer idLastParam) {
		int i=0;
		Object value;
		PropertyDescriptor propertyDescriptor;

		String className= classType.getSimpleName();
		String classID = className.toLowerCase()+"ID";		
		for (Field field : classType.getDeclaredFields()) {
			if(!classID.equals(field.getName())){
				try {
					propertyDescriptor = new PropertyDescriptor(field.getName(), classType);
					Method method = propertyDescriptor.getReadMethod();
					value = method.invoke(tModel);
					ps.setObject(++i, value);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if(idLastParam!=null){
			try {
				ps.setObject(++i,idLastParam);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ps;
	}

	
	/**
	 * @deprecated
	 * @param updateStr
	 * @return
	 */
	public int doUpdateGetResult(String updateStr) {
		Statement stmt = null;
	    ResultSet resultSet = null;
	    int resultVal =0;
	    try {
	        stmt = connection.createStatement();
	    	resultVal = stmt.executeUpdate(updateStr);	        	
	    } catch (SQLException e) {
	        throw new IllegalStateException("Unable to execute query: " + updateStr, e);
	    }finally {
	        try {
	            if (resultSet != null) {
	                resultSet.close();
	            }
	            if (stmt != null) {
	                stmt.close();
	            }
	        } catch (SQLException e) {
	            System.err.println("SQL EXCEPTION HANDLE & LOG ME");
	        }
	    }
	    return resultVal;
	}
	
	
	/**
	 * @deprecated
	 * @param queryStr
	 * @return
	 */
	public ResultSet doQueryGetResult(String queryStr) {
	    Statement stmt = null;
	    ResultSet resultSet = null;
	    CachedRowSetImpl crs = null;
	    try {
	        stmt = connection.createStatement();
	        resultSet = stmt.executeQuery(queryStr);	        		        	
	        crs = new CachedRowSetImpl();
	        crs.populate(resultSet);
	    } catch (SQLException e) {
	        throw new IllegalStateException("Unable to execute query: " + queryStr, e);
	    }finally {
	        try {
	            if (resultSet != null) {
	                resultSet.close();
	            }
	            if (stmt != null) {
	                stmt.close();
	            }
	        } catch (SQLException e) {
	            System.err.println("SQL EXCEPTION HANDLE & LOG ME");
	        }
	    }
	    return crs;
	}
	
	public Connection getConnection() {
		return connection;
	}


}
