package gcm;

import hlmng.FileSettings;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;

import log.Log;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

// parts of the following code from: http://hmkcode.com/send-http-post-request-from-java-application-to-google-messaging-service/
public class GCM {

	public static String postGCM(String title, String message,List<String> recipients) throws ProtocolException, IOException {
		GCMContent content = new GCMContent();
		for (String regId : recipients) {
			content.addRegId(regId);
		}
		content.createData(title,message);
		return sendGCM(FileSettings.apiKey, content);
	}
	
	private static String sendGCM(String apiKey, GCMContent content) throws ProtocolException, IOException {

			Log.addEntry(Level.INFO, "GCM - POST request to URL : " + FileSettings.gcmURL);
			URL url = new URL(FileSettings.gcmURL);
			HttpURLConnection conn = setUpCon(apiKey, url);

			ObjectMapper mapper = new ObjectMapper();
			mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
			mapper.writeValue(wr, content);
			wr.flush();
			wr.close();

			int responseCode = conn.getResponseCode();
			Log.addEntry(Level.INFO, "GCM - Returned response Code : " + responseCode);

			String response = getResult(conn);
			Log.addEntry(Level.INFO, "GCM - response:"+response);
			return response;
	}

	private static String getResult(HttpURLConnection conn)
			throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		return response.toString();
	}

	private static HttpURLConnection setUpCon(String apiKey, URL url)
			throws IOException, ProtocolException {
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Authorization", "key=" + apiKey);
		conn.setDoOutput(true);
		return conn;
	}
}
