package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

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
