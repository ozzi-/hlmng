package testing;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import hlmng.auth.AuthChecker;
import hlmng.auth.QRAuthResult;
import hlmng.dao.GenDao;
import hlmng.dao.GenDaoLoader;
import hlmng.model.Event;
import hlmng.model.ModelHelper;
import hlmng.model.QrCode;
import hlmng.model.User;
import hlmng.resource.TimeHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import com.owlike.genson.Genson;


public class RestTest {
	
	GenDao qrDao = GenDaoLoader.instance.getQrCodeDao();
	GenDao userDao = GenDaoLoader.instance.getUserDao();
	GenDao eventDao = GenDaoLoader.instance.getEventDao();
	
	@Before
	public void init(){
		Properties loginData= new Properties();
		loginData.put("user","user");
		loginData.put("password","pw12");
		qrDao.setTest(true,loginData,"jdbc:mysql://127.0.0.1/hlmng");
		userDao.setTest(true,loginData,"jdbc:mysql://127.0.0.1/hlmng");
		eventDao.setTest(true,loginData,"jdbc:mysql://127.0.0.1/hlmng");
	}

	@Test
    public void testQrRestAllOK() throws IOException{
		int eventIDFK=1;
		QrCode qrcode = new QrCode("TESTPAYLOAD","jury", eventIDFK);
		qrcode.setCreatedAt(TimeHelper.getCurrentDateTime());
		User user = new User("TEST", "112233","112233");
		int userid= userDao.addElement(user);
		int qrcodeid= qrDao.addElement(qrcode);
		user.setUserID(userid);
		QRAuthResult authQrCheck = AuthChecker.checkQRCode(qrcode.getPayload(),eventIDFK,user,"jury",TimeHelper.getCurrentDateTime());
		qrDao.deleteElement(qrcodeid);
		userDao.deleteElement(userid);
		assertTrue(authQrCheck.isAuthorized());
	}
	
	@Test
    public void testQrRestWrongUnkownQrCode() throws IOException{
		int eventIDFK=1;
		QrCode qrcode = new QrCode("TESTPAYLOAD","jury", eventIDFK);
		qrcode.setCreatedAt(TimeHelper.getCurrentDateTime());
		User user = new User("TEST", "112233","112233");
		int userid= userDao.addElement(user);
		user.setUserID(userid);
		QRAuthResult authQrCheck = AuthChecker.checkQRCode(qrcode.getPayload(),eventIDFK,user,"jury",TimeHelper.getCurrentDateTime());
		userDao.deleteElement(userid);
		assertFalse(authQrCheck.isAuthorized());
		assertTrue(authQrCheck.getResponse().getStatus()==403);
	}
	
	@Test
    public void testQrRestWrongUser() throws IOException{
		int eventIDFK=1;
		QrCode qrcode = new QrCode("TESTPAYLOAD","jury", eventIDFK);
		qrcode.setUserIDFK(1); // <--
		qrcode.setCreatedAt(TimeHelper.getCurrentDateTime());
		User user = new User("TEST", "112233","112233");
		int userid= userDao.addElement(user);
		int qrcodeid= qrDao.addElement(qrcode);
		user.setUserID(userid);
		QRAuthResult authQrCheck = AuthChecker.checkQRCode(qrcode.getPayload(),eventIDFK,user,"jury",TimeHelper.getCurrentDateTime());
		qrDao.deleteElement(qrcodeid);
		userDao.deleteElement(userid);
		assertFalse(authQrCheck.isAuthorized());
		assertTrue(authQrCheck.getResponse().getStatus()==403);
	}
	

	@Test
    public void testQrRestWrongRole() throws IOException{
		int eventIDFK=1;
		QrCode qrcode = new QrCode("TESTPAYLOAD","moderator", eventIDFK);
		qrcode.setCreatedAt(TimeHelper.getCurrentDateTime());
		User user = new User("TEST", "112233","112233");
		int userid= userDao.addElement(user);
		int qrcodeid= qrDao.addElement(qrcode);
		user.setUserID(userid);
		QRAuthResult authQrCheck = AuthChecker.checkQRCode(qrcode.getPayload(),eventIDFK,user,"jury",TimeHelper.getCurrentDateTime());
		qrDao.deleteElement(qrcodeid);
		userDao.deleteElement(userid);
		assertFalse(authQrCheck.isAuthorized());
		assertTrue(authQrCheck.getResponse().getStatus()==403);
	}
	
	
	
	
	
	@Test
    public void testEventRest() throws IOException{
		Event orig = new Event("eventname","description","2015-01-01","2015-01-01");
		int elementID=eventDao.addElement(orig);
		String response="";
		try {
			response = doURL("http://localhost:8080/hlmng/rest/event/"+elementID,"GET");
		} catch (Exception e) {
			e.printStackTrace();
		}
		orig.setEventID(elementID);
		Genson genson = new Genson();
		Event excpected = genson.deserialize(response, Event.class);
		assertTrue(eventDao.deleteElement(elementID));
		assertTrue(ModelHelper.Compare(excpected,orig));
	}


	private String doURL(String url, String method) throws Exception {
	
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod(method); 
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
