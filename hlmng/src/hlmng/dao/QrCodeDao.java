package hlmng.dao;

import hlmng.model.QrCode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import log.Log;
import db.DB;

/**
 * Extends the generic dao with a few specific qr code functions
 */
public class QrCodeDao extends GenDao {

	public <T> QrCodeDao(Class<T> classTypeP) {
		super(classTypeP);
	}
	
	
	public QrCode getQrCodeByPayload(String payload) {
		PreparedStatement ps = null;
        ResultSet rs = null;
        Object element=null;
        Connection dbConnection =null;
		try {
			dbConnection = getDBConnection();
			ps = dbConnection.prepareStatement("SELECT * FROM qrcode WHERE payload = ?");
			ps.setString(1,payload);
	        rs = ps.executeQuery();
	        if (rs.isBeforeFirst() ) {     
	        	rs.next();
				element=DB.getObjectFromRS(rs,QrCode.class);
			} 
			ps.close();
		} catch (Exception e) {
			Log.addEntry(Level.WARNING, "QR Code couldn't be returned by payload. "+e.getMessage());
			e.printStackTrace();
		}
		finally{
			tryToClose(rs, ps, dbConnection);
		}
		Log.addEntry(Level.INFO,"Qr Code get by payload ("+element+")");
		return (QrCode) element;
	}

}
