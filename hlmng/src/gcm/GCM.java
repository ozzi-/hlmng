package gcm;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;

import settings.HLMNGSettings;
import log.Log;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * GCM stands for Google Cloud Messaging. We are using GCM to inform the Mobile Apps about new changes.
 *  * @see <a href="https://developer.android.com/google/gcm/index.html">https://developer.android.com/google/gcm/index.html</a>
 *  Disclaimer: Parts of the following class (in edited form) is from the following url: http://hmkcode.com/send-http-post-request-from-java-application-to-google-messaging-service/
 */
public class GCM {

	public static String sendGcm(String title, String message,List<String> recipients) throws ProtocolException, IOException {
		GCMContent content = new GCMContent();
		content.createData(title,message);
		addRecipients(recipients, content);
		return sendGcmContent(HLMNGSettings.apiKey, content);
	}

	private static void addRecipients(List<String> recipients, GCMContent content) {
		for (String regId : recipients) {
			content.addRegId(regId);
		}
	}
	
	private static String sendGcmContent(String apiKey, GCMContent content) throws ProtocolException, IOException {

			Log.addEntry(Level.INFO, "GCM - POST request to URL : " + HLMNGSettings.gcmURL);
			URL url = new URL(HLMNGSettings.gcmURL);
			HttpURLConnection conn = setUpCon(apiKey, url);

			ObjectMapper mapper = new ObjectMapper();
			mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
			mapper.writeValue(wr, content);
			wr.flush();
			wr.close();

			int responseCode = conn.getResponseCode();
			Log.addEntry(Level.INFO, "GCM - Returned response Code : " + responseCode);

			String response = getHttpConResult(conn);
			Log.addEntry(Level.INFO, "GCM - response:"+response);
			return response;
	}

	private static String getHttpConResult(HttpURLConnection conn) throws IOException {
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
