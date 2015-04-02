package hlmng.auth;

import hlmng.dao.GenDaoLoader;
import hlmng.model.ModelHelper;
import hlmng.model.QrCode;
import hlmng.model.User;
import hlmng.model.UserActionLimiter;
import hlmng.resource.ResourceHelper;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import log.Log;

import org.apache.tomcat.util.codec.binary.Base64;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * 
 * AuthChecker provides a public method "check" which inspects the HTTP headers
 * and makes a lookup if a auth was provided and if it was correct.
 * If the login credentials are wrong a 401 is sent, if the formating / data is weird 400
 *
 */
public class AuthChecker {

	private static List<AuthCredential> logins=new ArrayList<AuthCredential>();
	private static boolean loginsLoaded=false;
	
	/**
	 * Load the logins from the backendlogis json file. This method is called  automatically if required
	 * @return true if were able to load
	 */
	private static boolean loadLogins(){
       JSONParser parser = new JSONParser();
       JSONArray jArray=null;
		try { 
			URL url = AuthChecker.class.getResource("/backendlogins.json");
			File file = new File(url.getPath());
			jArray = (JSONArray) parser.parse(new FileReader(file));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		for (int j = 0; j < jArray.size(); j++) {
		    JSONObject jsonObject = (JSONObject) jArray.get(j);
		    String username = (String) jsonObject.get("username");
			String password = (String) jsonObject.get("password");
			logins.add(new AuthCredential(username, password));
		}
		return true;
	}
	
	public static QRAuthResult checkQRCode(String qrHeader, int checkIDFK, User user, String role){
		QrCode qrcode = (QrCode) GenDaoLoader.instance.getQrCodeDao().getQrCodeByPayload(qrHeader);
		if(qrcode!=null){
			if(qrcode.getEventIDFK()!=checkIDFK){
				Log.addEntry(Level.WARNING, "User tried to auth with a qr code issued to another eventIDFK. UserID:"+user.getUserID());
				return new QRAuthResult(false, Response.status(403).build());
			}
			if(qrcode.getRole().equals(role)){
				if(qrcode.getUserIDFK()!=0){
					if(user.getUserID()==qrcode.getUserIDFK()){
						// Claimed and same -> ok
						return new QRAuthResult(true, null);
					}else{
						Log.addEntry(Level.WARNING, "User tried to auth with a qr code claimed by somebody else before. UserID:"+user.getUserID());
						return new QRAuthResult(false, Response.status(403).build());
					}
				}else{
					// Unclaimed -> ok but claim it									
					qrcode.setClaimedAt(ResourceHelper.getCurrentDateTime());
					qrcode.setUserIDFK(user.getUserID()); 
					GenDaoLoader.instance.getQrCodeDao().updateElement(qrcode, qrcode.getQrcodeID());
					return new QRAuthResult(true, null);
				}
			}else{
				Log.addEntry(Level.WARNING, "User tried to vote with a qr code that has a wrong role. UserID:"+user.getUserID());
				return new QRAuthResult(false, Response.status(403).build());
			}
		}else{
			Log.addEntry(Level.WARNING,"User sent a wrong qr code. UserID:"+user.getUserID());
			return new QRAuthResult(false, Response.status(403).build());
		}
	}
	
	/**
	 * Checks if a Authorization header is sent and if it contains an authorized Base64 encoded login
	 * Notice: This method sends according error codes if the user provided no, wrong or ill-formed authentication data!
	 * @param headers
	 * @param servletResponse
	 * @param backEnd if set True the local json file is checked for logins, if false the HLMNG Database Table User
	 * @return true if the user credentials are ok, else false
	 */
	public static boolean check(@Context HttpHeaders headers, @Context HttpServletResponse servletResponse,boolean backEnd) {
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
				return checkLoginInformation(servletResponse, backEnd, authCredential);
			}
		}
		Log.addEntry(Level.INFO, "Failed auth (backend="+backEnd+")");
		return false;
	}

	private static boolean checkLoginInformation(
			HttpServletResponse servletResponse, boolean backEnd,
			AuthCredential authCredential) {
		if(backEnd){
			if(!checkLoginInformationBackend((authCredential))){
				Log.addEntry(Level.INFO, "Failed auth (backend="+backEnd+") due to wrong credentials."+ModelHelper.valuestoString(authCredential));
				sendErrorAuthCode(servletResponse,HttpServletResponse.SC_UNAUTHORIZED);
				return false;
			}else{
				Log.addEntry(Level.INFO, "Successful backend auth. "+ModelHelper.valuestoString(authCredential));
				return true;
			}
		}else{
			int code = checkLoginInformation(authCredential);
			if(code==HttpServletResponse.SC_OK){
				Log.addEntry(Level.INFO, "Successful user auth. "+ModelHelper.valuestoString(authCredential));
				return true;	
			}else{
				Log.addEntry(Level.INFO, "Failed auth (backend="+backEnd+") due to wrong credentials. "+ModelHelper.valuestoString(authCredential));
				sendErrorAuthCode(servletResponse,code); 	
				return false;
			}
		}
	}
	
	/**
	 * Sends a response with the provided error code
	 * @param servletResponse
	 * @param code
	 */
	private static void sendErrorAuthCode(HttpServletResponse servletResponse, int code) {
		servletResponse.setHeader("WWW-Authenticate", "Basic realm=\"HLMNG\"");
		try {
			servletResponse.sendError(code,"");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Tries to decode the authorization header, if something isn't conform null will be returned.
	 * Expected input: "Basic base64{name:secret}"
	 * @param authorizationHeader
	 * @return auth credential or null
	 */
	public static AuthCredential decodeBasicAuthB64( String authorizationHeader) {
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
	
	/**
	 * Checks the Database if that user exists, also checks for Action limit!
	 * @param authCredential
	 * @return 
	 */
	private static int checkLoginInformation(AuthCredential authCredential) {
		User userDB = GenDaoLoader.instance.getUserDao().getUserByNameAndDeviceID(authCredential.getUsername(),authCredential.getSecret());
		if(!(authCredential==null) && !(userDB==null) && (userDB.getName().equals(authCredential.getUsername()) && (userDB.getDeviceID().equals(authCredential.getSecret())))){
			if(UserActionLimiter.actionsExceeded(userDB.getName())){
				return 429;
			}
			return HttpServletResponse.SC_OK;
		}else {
			return HttpServletResponse.SC_UNAUTHORIZED;
		}
	}
	
	/**
	 * Checks the local json file for backend logins
	 * @param requestingCredential
	 * @return true if there is such a user, else false
	 */
	private static boolean checkLoginInformationBackend(AuthCredential requestingCredential) {
		if(!loginsLoaded){
			loginsLoaded=loadLogins();
			if(loginsLoaded){
				log.Log.addEntry(Level.INFO, "Loaded backend logins");				
			}else{
				log.Log.addEntry(Level.SEVERE, "Couldn't load backend logins");				
			}
		}
		for (AuthCredential authorizedCredentials  : logins) {
			if(authorizedCredentials.equals(requestingCredential)){
				return true;
			}
		}
		return false;
	}
}
