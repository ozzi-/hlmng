package hlmng.dao;

import hlmng.Log;
import hlmng.model.User;

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
			dbConnection = DB.getConnection();
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
			e.printStackTrace();
		}
		finally{
			tryToClose(rs, ps, dbConnection);
		}
		Log.addEntry(Level.INFO,"User Element get by name and device id ("+element+")");
		return (User) element;
	}

}
