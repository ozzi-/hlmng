package hlmng.auth;

import hlmng.dao.GenDaoLoader;
import hlmng.model.ModelHelper;
import hlmng.model.QrCode;
import hlmng.model.User;
import hlmng.model.UserActionLimiter;
import hlmng.resource.adm.QrCodeResource;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.logging.Level;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

import log.Log;

import org.apache.tomcat.util.codec.binary.Base64;

import settings.HTTPCodes;

/**
 * AuthChecker provides a public method "check" which inspects the HTTP headers
 * and makes a lookup if a auth was provided and if it was correct.
 */
public class AuthChecker {

	public static AuthResult checkQRCodeAuthorization(String qrHeader, int checkIDFK, User user, String role,String currentDateTime){
		QrCode qrcode = (QrCode) GenDaoLoader.instance.getQrCodeDao().getQrCodeByPayload(qrHeader);
		if(qrcode!=null){
			if(qrcode.getEventIDFK()!=checkIDFK){
				Log.addEntry(Level.WARNING, "User tried to auth with a qr code issued to another eventIDFK. UserID:"+user.getUserID());
				return new AuthResult(false, HTTPCodes.forbidden);
			}
			if(qrcode.getRole().equals(role)){
				if(qrcode.getUserIDFK()!=0){
					if(user.getUserID()==qrcode.getUserIDFK()){ // already claimed for correct user
						return new AuthResult(true, HTTPCodes.ok);
					}else{
						Log.addEntry(Level.WARNING, "User tried to auth with a qr code claimed by somebody else before. UserID:"+user.getUserID());
						return new AuthResult(false,HTTPCodes.forbidden);
					}
				}else{				
					QrCodeResource.claimQrCode(user, currentDateTime, qrcode);
					return new AuthResult(true, HTTPCodes.ok);
				}
			}else{
				Log.addEntry(Level.WARNING, "User tried to vote with a qr code that has a wrong role. UserID:"+user.getUserID());
				return new AuthResult(false, HTTPCodes.forbidden);
			}
		}else{
			Log.addEntry(Level.WARNING,"User sent a wrong qr code. UserID:"+user.getUserID());
			return new AuthResult(false, HTTPCodes.forbidden);
		}
	}


	/**
	 * Checks if a Authorization header is sent and if it contains an authorized Base64 encoded login
	 * Notice: This method sends according error codes if the user provided no, wrong or ill-formed authentication data!
	 * @param headers
	 * @param servletResponse
	 * @return true if the user credentials are ok, else false
	 */
	public static boolean checkAuthorization(@Context HttpHeaders headers, @Context HttpServletResponse servletResponse) {
		AuthCredential authCredential = null;
		String authorizationHeader = headers.getHeaderString("Authorization");
		if (authorizationHeader == null) {
			// client not sending http auth credentials, tell him to do so
			sendErrorAuthCode(servletResponse,HttpServletResponse.SC_UNAUTHORIZED);
		} else {
			authCredential = decodeBasicAuthB64(authorizationHeader);
			if(authCredential==null){
				sendErrorAuthCode(servletResponse,HttpServletResponse.SC_BAD_REQUEST);
			}else{
				AuthResult authRes = checkLoginInformation(servletResponse, authCredential);
				if(authRes.isAuthorized()){
					return true;
				}else{
					sendErrorAuthCode(servletResponse, authRes.getResponseCode());
					return false;
				}
			}
		}
		Log.addEntry(Level.INFO, "Failed auth");
		return false;
	}

	private static AuthResult checkLoginInformation( HttpServletResponse servletResponse, AuthCredential authCredential) {
		User userDB = GenDaoLoader.instance.getUserDao().getUserByNameAndDeviceID(authCredential.getUsername(),authCredential.getSecret());
		if(credentialMatchesUser(authCredential, userDB)){
			if(UserActionLimiter.actionsExceeded(userDB.getName())){
				Log.addEntry(Level.WARNING, "Successful user auth but action limit exceeded. "+ModelHelper.valuestoString(authCredential));
				return new AuthResult(false, HTTPCodes.toManyRequests);
			}
			Log.addEntry(Level.INFO, "Successful user auth. "+ModelHelper.valuestoString(authCredential));
			return new AuthResult(true, HTTPCodes.ok);	
		}else {
			Log.addEntry(Level.INFO, "Failed auth due to wrong credentials. "+ModelHelper.valuestoString(authCredential));
			sendErrorAuthCode(servletResponse,HttpServletResponse.SC_UNAUTHORIZED);
			return new AuthResult(false, HTTPCodes.unauthorized);
		}		
	}
	
	/**
	 * Send Error code and Authenticate Header
	 */
	private static void sendErrorAuthCode(HttpServletResponse servletResponse, int code) {
		servletResponse.setHeader("WWW-Authenticate", "Basic realm=\"HLMNG\"");
		try {
			servletResponse.sendError(code);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Tries to decode the authorization header, if something isn't conform null will be returned.
	 * Expected input (HTTP Basic Auth): "Basic base64{name:secret}"
	 * @param authorizationHeader
	 * @return auth credential or null
	 */
	public static AuthCredential decodeBasicAuthB64(String authorizationHeader) {
		Base64 b = new Base64();
		AuthCredential authCredential=null;
		String[] authCredentials = null;
		if (authorizationHeader.startsWith("Basic")) { 
			String base64Credentials = authorizationHeader.substring("Basic".length()).trim();
			String credentials = new String(b.decode(base64Credentials), Charset.forName("UTF-8"));
			if(credentials.contains(":")){
				authCredentials = credentials.split(":", 2);
				if(authCredentials!=null && authCredentials[0]!=null && authCredentials[1] != null){
					authCredential = new AuthCredential(authCredentials[0], authCredentials[1]);				
				}				
			}
		}
		return authCredential;
	}

	private static boolean credentialMatchesUser(AuthCredential authCredential, User userDB) {
		if(!(authCredential==null) && !(userDB==null) 
			 && (userDB.getName().equals(authCredential.getUsername()) 
			 && (userDB.getDeviceID().equals(authCredential.getSecret())))){
			return true;
		}
		return false;
	}
	
}
