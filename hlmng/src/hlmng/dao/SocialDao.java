package hlmng.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import log.Log;
import db.DB;
import settings.HLMNGSettings;
import hlmng.model.Social;

/**
 * Extends the generic dao with a few specific user functions
 */
public class SocialDao extends GenDao {

	String prepNewestStatement = "SELECT * FROM social WHERE eventIDFK = ? AND  (status = 'published' OR status = 'accepted')  ORDER BY socialID DESC LIMIT "+HLMNGSettings.selectLimit+";";
	
	public <T> SocialDao(Class<T> classTypeP) {
		super(classTypeP);
	}

	public <T> List<Object> getNewestAcceptedOrPublished(int idFK){
		List<Object> elemList = new ArrayList<Object>();
		try (Connection dbConnection = getDBConnection()){
			try(PreparedStatement ps = dbConnection.prepareStatement(prepNewestStatement)){
				PreparedStatement psF= DB.setIdFieldOfPS(ps,idFK);				
				try(ResultSet rs = psF.executeQuery()){
					while (rs.next()) {
						elemList.add(DB.getObjectFromRS(rs,Social.class));
					}					
				}
			}
		} catch (Exception e) {
			Log.addEntry(Level.WARNING,"Elements couldn't be listed by FK. "+e.getMessage());
			e.printStackTrace();
		}
		Log.addEntry(Level.FINEST,"Social newest element list ("+elemList.hashCode()+"["+elemList.size()+"])");
		return elemList;
	}
	
	
}