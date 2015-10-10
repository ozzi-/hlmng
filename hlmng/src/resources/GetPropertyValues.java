package resources;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class GetPropertyValues {
	String result = "";
	InputStream inputStream;

	public String getAndSetPropValues() throws IOException {

		try {
			Properties prop = new Properties();
			String propFileName = "config.properties";

			inputStream = this.getClass().getResourceAsStream(propFileName);
			
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}
			settings.HLMNGSettings.sendGCM=(prop.getProperty("sendgcm").equals("no")?false:true);
			System.out.println("GCM :"+settings.HLMNGSettings.sendGCM);

		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
			inputStream.close();
		}
		return result;
	}
}