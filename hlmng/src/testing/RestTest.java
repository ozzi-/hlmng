package testing;

import static org.junit.Assert.*;
import hlmng.dao.GenDao;
import hlmng.dao.GenDaoLoader;
import hlmng.model.Event;
import hlmng.model.ModelHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Test;

import com.owlike.genson.Genson;

public class RestTest {

	@Test
    public void testEventRest() throws IOException{
		GenDao dao = GenDaoLoader.instance.getEventDao();
		Event orig = new Event("eventname","description","2015-01-01","2015-01-01");
		int elementID=dao.addElement(orig);
		String response="";
		try {
			response = sendGet("http://localhost:8080/hlmng/rest/event/"+elementID);

		} catch (Exception e) {
			e.printStackTrace();
		}
		orig.setEventID(elementID);
		Genson genson = new Genson();
		Event excpected = genson.deserialize(response, Event.class);
		dao.deleteElement(Integer.toString(elementID));
		assertTrue(ModelHelper.Compare(excpected,orig));
	}


	private String sendGet(String url) throws Exception {
	
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET"); 
		con.setRequestProperty("User-Agent", "Mozilla/5.0");
 
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		return response.toString();
 
	}
}
