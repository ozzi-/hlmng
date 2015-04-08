package hlmng.dao;

import hlmng.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import log.Log;
import db.DB;

/**
 * Extends the generic dao with a few specific user functions
 */
public class UserDao extends GenDao {

	public <T> UserDao(Class<T> classTypeP) {
		super(classTypeP);
	}
	
	
	public User getUserByNameAndDeviceID(String userName, String deviceID) {
		User element= null;
		try (Connection dbConnection = getDBConnection()){
			try(PreparedStatement ps = dbConnection.prepareStatement("SELECT * FROM user WHERE name = ? AND deviceID = ?")){
				ps.setString(1,userName);
				ps.setString(2,deviceID);
				try(ResultSet rs = ps.executeQuery()){
					if (rs.isBeforeFirst() ) {     
						rs.next();
						element=DB.getObjectFromRS(rs,User.class);
					} 					
				}
			}
		} catch (Exception e) {
			Log.addEntry(Level.WARNING, "User couldn't be returned (by name and device id). "+e.getMessage());
			e.printStackTrace();
		}
		Log.addEntry(Level.INFO,"User Element get by name and device id ("+element+")");
		return  element;
	}
	
	public User getUserByName(String userName) {
		User element= null;
		try (Connection dbConnection = getDBConnection()){
			try(PreparedStatement ps = dbConnection.prepareStatement("SELECT * FROM user WHERE name = ?")){
				ps.setString(1,userName);
				try(ResultSet rs = ps.executeQuery()){
					if (rs.isBeforeFirst() ) {     
						rs.next();
						element=DB.getObjectFromRS(rs,User.class);
					} 					
				}
			}
		} catch (Exception e) {
			Log.addEntry(Level.WARNING, "User couldn't be returned (by name). "+e.getMessage());
			e.printStackTrace();
		}
		Log.addEntry(Level.INFO,"User Element get by name ("+element+")");
		return (User) element;
	}

}
