package hlmng.dao;

import hlmng.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import db.DB;

/**
 * Extends the generic dao with a few specific vote functions
 */
public class VoteDao extends GenDao {

	public <T> VoteDao(Class<T> classTypeP) {
		super(classTypeP);
	}
	
	
	public boolean userVotedSliderBefore(int userID, int sliderID) {
		PreparedStatement ps = null;
        ResultSet rs = null;
        boolean hasVoted = true ;
        Connection dbConnection =null;
		try {
			dbConnection = DB.getConnection();
			ps = dbConnection.prepareStatement("SELECT * FROM vote WHERE userIDFK = ? AND sliderIDFK = ?");
			ps.setInt(1,userID);
			ps.setInt(2,sliderID);
	        rs = ps.executeQuery();
	        hasVoted=(rs.next());
	        ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			tryToClose(rs, ps, dbConnection);
		}
		Log.addEntry(Level.INFO,"User has voted for slider ("+sliderID+") before?  ("+hasVoted+")");
		return hasVoted;
	}

}
