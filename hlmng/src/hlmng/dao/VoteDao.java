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
        boolean hasVoted = true ;
		try (Connection dbConnection = getDBConnection()){
			try(PreparedStatement ps = dbConnection.prepareStatement("SELECT * FROM vote WHERE userIDFK = ? AND sliderIDFK = ?")){
				ps.setInt(1,userID);
				ps.setInt(2,sliderID);
				try(ResultSet rs = ps.executeQuery()){
					hasVoted=(rs.next());
				}
			}
		} catch (Exception e) {
			Log.addEntry(Level.WARNING, "Slider couldn't be returned (by user id). "+e.getMessage());
			e.printStackTrace();
		}
		Log.addEntry(Level.INFO,"User has voted for slider ("+sliderID+") before?  ("+hasVoted+")");
		return hasVoted;
	}

}
