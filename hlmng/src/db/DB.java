package db;

import hlmng.dao.QueryBuilder;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.owlike.genson.convert.DefaultConverters.PrimitiveConverterFactory.booleanConverter;
import com.sun.rowset.CachedRowSetImpl;




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
	

	public static <T> PreparedStatement setIdFieldOfPS(PreparedStatement ps, int id) {
		try {
			ps.setObject(1,id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ps;
	}


	public static <T> PreparedStatement setAllFieldsOfPS(PreparedStatement ps, Class<T> classType,T instance,Integer idLastParam) {
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
					value = method.invoke(instance);
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
