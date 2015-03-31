package hlmng.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import log.Log;

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
			dbConnection = getDBConnection();
			ps = dbConnection.prepareStatement("SELECT * FROM vote WHERE userIDFK = ? AND sliderIDFK = ?");
			ps.setInt(1,userID);
			ps.setInt(2,sliderID);
	        rs = ps.executeQuery();
	        hasVoted=(rs.next());
	        ps.close();
		} catch (Exception e) {
			Log.addEntry(Level.WARNING, "Slider couldn't be returned (by user id). "+e.getMessage());
			e.printStackTrace();
		}
		finally{
			tryToClose(rs, ps, dbConnection);
		}
		Log.addEntry(Level.INFO,"User has voted for slider ("+sliderID+") before?  ("+hasVoted+")");
		return hasVoted;
	}

}
