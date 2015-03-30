package hlmng.dao;

import hlmng.model.User;
import hlmng.resource.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import db.DB;

/**
 * Extends the generic dao with a few specific user functions
 */
public class UserDao extends GenDao {

	public <T> UserDao(Class<T> classTypeP) {
		super(classTypeP);
	}
	
	
	public User getUserByNameAndDeviceID(String userName, String deviceID) {
		PreparedStatement ps = null;
        ResultSet rs = null;
        Object element=null;
        Connection dbConnection =null;
		try {
			dbConnection = getDBConnection();
			ps = dbConnection.prepareStatement("SELECT * FROM user WHERE name = ? AND deviceID = ?");
			ps.setString(1,userName);
			ps.setString(2,deviceID);
	        rs = ps.executeQuery();
	        if (rs.isBeforeFirst() ) {     
	        	rs.next();
				element=DB.getObjectFromRS(rs,User.class);
			} 
			ps.close();
		} catch (Exception e) {
			Log.addEntry(Level.WARNING, "User couldn't be returned (by name and device id). "+e.getMessage());
			e.printStackTrace();
		}
		finally{
			tryToClose(rs, ps, dbConnection);
		}
		Log.addEntry(Level.INFO,"User Element get by name and device id ("+element+")");
		return (User) element;
	}
	
	public User getUserByName(String userName) {
		PreparedStatement ps = null;
        ResultSet rs = null;
        Object element=null;
        Connection dbConnection =null;
		try {
			dbConnection = getDBConnection();
			ps = dbConnection.prepareStatement("SELECT * FROM user WHERE name = ?");
			ps.setString(1,userName);
	        rs = ps.executeQuery();
	        if (rs.isBeforeFirst() ) {     
	        	rs.next();
				element=DB.getObjectFromRS(rs,User.class);
			} 
			ps.close();
		} catch (Exception e) {
			Log.addEntry(Level.WARNING, "User couldn't be returned (by name). "+e.getMessage());
			e.printStackTrace();
		}
		finally{
			tryToClose(rs, ps, dbConnection);
		}
		Log.addEntry(Level.INFO,"User Element get by name ("+element+")");
		return (User) element;
	}

}
