package hlmng.auth;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

import org.apache.tomcat.util.codec.binary.Base64;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * 
 * AuthChecker provides a public method "check" which inspects the HTTP headers
 * and makes a lookup if a auth was provided and if it was correct.
 * If anything is wrong then a 401 message is sent to the client.
 *
 */
public class AuthChecker {

	private static List<AuthCredential> logins=new ArrayList<AuthCredential>();
	private static boolean loginsLoaded=false;
	
	/**
	 * Load the logins from the backendlogins json file. This method is called  automatically if required
	 * @return true if were able to load
	 */
	private static boolean loadLogins(){
       JSONParser parser = new JSONParser();
       JSONArray jArray=null;
		try { 
			jArray = (JSONArray) parser.parse(new FileReader("backendlogins.json"));
		} catch (IOException | ParseException e) {
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
	
	/**
	 * Checks if a Authorization header is sent and if it contains an authorized Base64 encoded login
	 * If something isn't ok a unauthorized will be sent, if everything is ok -> do nothing and continue
	 * @param headers
	 * @param servletResponse
	 */
	public static void check(@Context HttpHeaders headers, @Context HttpServletResponse servletResponse) {
		AuthCredential authCredentials = null;

		String authorizationHeader = headers.getHeaderString("Authorization");
		// client not sending http auth credentials, tell him to do so
		if(authorizationHeader==null){ 	
			sendUnauthorized(servletResponse);
		}else{ 
			// client is sending credentials, read them
			authCredentials = decodeBasicAuthB64(authorizationHeader);
			// if auth credentials somehow not decodeable 
			if (authCredentials == null || !checkLoginInformation(authCredentials)) {
				sendUnauthorized(servletResponse);
			}				
		}
	}
	
	private static void sendUnauthorized(HttpServletResponse servletResponse) {
		servletResponse.setHeader("WWW-Authenticate", "Basic realm=\"HLMNG\"");
		try {
			servletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,"");
		} catch (IOException e) {
			// TODO  what do?
			e.printStackTrace();
		}
	}

	
	private static AuthCredential decodeBasicAuthB64( String authorizationHeader) {
		Base64 b = new Base64();
		String[] authCredentials = null;
		if (authorizationHeader.startsWith("Basic")) {
			String base64Credentials = authorizationHeader.substring("Basic".length()).trim();
			String credentials = new String(b.decode(base64Credentials), Charset.forName("UTF-8"));
			authCredentials = credentials.split(":", 2);
			return new AuthCredential(authCredentials[0], authCredentials[1]);
		}
		return null;
	}

	private static boolean checkLoginInformation(AuthCredential requestingCredential) {
		if(!loginsLoaded){
			loadLogins();
			loginsLoaded=true;
			System.out.println("Loaded logins");
		}		

		for (AuthCredential authorizedCredentials  : logins) {
			if(authorizedCredentials.equals(requestingCredential)){
				return true;
			}
		}
		return false;
	}
}
