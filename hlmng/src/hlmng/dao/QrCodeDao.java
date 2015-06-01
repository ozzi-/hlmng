package hlmng.dao;

import hlmng.model.QrCode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.owasp.esapi.ESAPI;

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
		QrCode element = null;
		payload = ESAPI.encoder().canonicalize(payload);
		payload=Jsoup.clean(payload, Whitelist.none());
		try (Connection dbConnection = getDBConnection()){
			try(PreparedStatement ps = dbConnection.prepareStatement("SELECT * FROM qrcode WHERE payload = ?")){
				ps.setString(1,payload);
				try(ResultSet rs = ps.executeQuery()){
					if (rs.isBeforeFirst() ) {     
						rs.next();
						element=DB.getObjectFromRS(rs,QrCode.class);
					} 				
				}
			}
		} catch (Exception e) {
			Log.addEntry(Level.WARNING, "QR Code couldn't be returned by payload. "+e.getMessage());
			e.printStackTrace();
		}
		Log.addEntry(Level.INFO,"Qr Code get by payload ("+element+")");
		return element;
	}

}
