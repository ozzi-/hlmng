package db;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * This class handles the creation of a DB connection, allows building dynamic prepared statements.
 * This only works if you are using the same class name and field names as declared in the SQL Database!
 *
 */
public class DB {
	

	public static Connection getConnection() throws SQLException, NamingException{
		Context initContext;
		initContext = new InitialContext();
		Context envContext = (Context) initContext.lookup("java:comp/env");
		DataSource ds = (DataSource) envContext.lookup("jdbc/hlmng");
		return ds.getConnection();
	}
	
	public static void closeConnection(Connection connection){
		try { 
			connection.close();
		} catch (SQLException e) {
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
				value=(value==null&&field.getName().contains("ID")?0:value);
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

	
//	/**
//	 * @deprecated
//	 * @param updateStr
//	 * @return
//	 */
//	public int doUpdateGetResult(String updateStr) {
//		Statement stmt = null;
//	    ResultSet resultSet = null;
//	    int resultVal =0;
//	    try {
//	        stmt = connection.createStatement();
//	    	resultVal = stmt.executeUpdate(updateStr);	        	
//	    } catch (SQLException e) {
//	        throw new IllegalStateException("Unable to execute query: " + updateStr, e);
//	    }finally {
//	        try {
//	            if (resultSet != null) {
//	                resultSet.close();
//	            }
//	            if (stmt != null) {
//	                stmt.close();
//	            }
//	        } catch (SQLException e) {
//	            System.err.println("SQL EXCEPTION HANDLE & LOG ME");
//	        }
//	    }
//	    return resultVal;
//	}
//	
//	
//	/**
//	 * @deprecated
//	 * @param queryStr
//	 * @return
//	 */
//	public ResultSet doQueryGetResult(String queryStr) {
//	    Statement stmt = null;
//	    ResultSet resultSet = null;
//	    CachedRowSetImpl crs = null;
//	    try {
//	        stmt = connection.createStatement();
//	        resultSet = stmt.executeQuery(queryStr);	        		        	
//	        crs = new CachedRowSetImpl();
//	        crs.populate(resultSet);
//	    } catch (SQLException e) {
//	        throw new IllegalStateException("Unable to execute query: " + queryStr, e);
//	    }finally {
//	        try {
//	            if (resultSet != null) {
//	                resultSet.close();
//	            }
//	            if (stmt != null) {
//	                stmt.close();
//	            }
//	        } catch (SQLException e) {
//	            System.err.println("SQL EXCEPTION HANDLE & LOG ME");
//	        }
//	    }
//	    return crs;
//	}
//	
//	public Connection getConnection() {
//		return connection;
//	}
//
}
