package hlmng.dao;

import hlmng.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import db.DB;

/**
 * Extends the generic dao with a few specific user functions
 */
public class UserDao extends GenDao {

	public <T> UserDao(Class<T> classTypeP) {
		super(classTypeP);
	}
	public User getUserByNameAndDeviceID(String userName, String deviceID) {
		PreparedStatement ps;
        ResultSet rs;
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
			DB.closeConnection(dbConnection);
		}
		// TODO Log.addEntry(Level.INFO,className+" Element get ("+element+")");
		return (User) element;
	}

}
