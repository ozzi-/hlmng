package testing;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import hlmng.auth.AuthChecker;
import hlmng.auth.AuthCredential;
import hlmng.auth.AuthResult;
import hlmng.dao.GenDao;
import hlmng.dao.GenDaoLoader;
import hlmng.dao.UserDao;
import hlmng.model.Event;
import hlmng.model.QrCode;
import hlmng.model.User;
import hlmng.resource.TimeHelper;

import java.io.IOException;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import settings.HLMNGSettings;

public class AuthTest {

	private int insertedID;
	private UserDao userDao;
	GenDao qrDao = GenDaoLoader.instance.getQrCodeDao();
	GenDao eventDao = GenDaoLoader.instance.getEventDao();
	private static final int eventIDFK= 9999;

	@Before
	public void init(){
		userDao = GenDaoLoader.instance.getUserDao();
		Properties loginData= new Properties();
		loginData.put("user",HLMNGSettings.jdbcUser);
		loginData.put("password",HLMNGSettings.jdbcPassword); 
		userDao.setTest(true,loginData,HLMNGSettings.jdbcPath);
		qrDao.setTest(true,loginData,HLMNGSettings.jdbcPath);
		eventDao.setTest(true, loginData, HLMNGSettings.jdbcPath);
		insertedID=userDao.addElement(new User("user", "1234", "4321"));
		assertTrue(insertedID>0);
		eventDao.addIDElement(new Event(eventIDFK,"test", "test", "2015-05-05", "2015-05-05",true));
	}
	
	

	@Test
	public void testDecode(){
		AuthCredential authCredential = AuthChecker.decodeBasicAuthB64("Basic dXNlcjE6cGFzc3dvcmQx");
		assertTrue(authCredential.getUsername().equals("user1") && authCredential.getSecret().equals("password1"));
	}
	@Test
	public void testInvalidInputDecode(){
		AuthCredential authCredential = AuthChecker.decodeBasicAuthB64("12345");
		assertTrue(authCredential==null);
		authCredential = AuthChecker.decodeBasicAuthB64("Basic ");
		assertTrue(authCredential==null);
		authCredential = AuthChecker.decodeBasicAuthB64("Basic dXNlcjE6cGFczc==()3dvcmQx==");
		assertFalse(authCredential.getSecret().equals("password1"));
	}
	
	@After
	public void deleteUser(){
		boolean deleted = userDao.deleteElement(insertedID);
		assertTrue(deleted);
	}
	
	@Test
	public void testCorrectLogin(){
		boolean authorized = AuthChecker.checkAuthorization(HeaderServletCreator.returnHTTPHeaders("Basic dXNlcjoxMjM0"),HeaderServletCreator.returnServletResponse());
		assertTrue(authorized);
	}
	
	@Test
	public void testIncorrectLogin(){
		boolean authorized = AuthChecker.checkAuthorization(HeaderServletCreator.returnHTTPHeaders("Basic aG9kb3I6aG9kb3I="),HeaderServletCreator.returnServletResponse());
		assertFalse(authorized);
	}
	

	@Test
	public void testQrRestAllOK() throws IOException {
		QrCode qrcode = new QrCode("jury-2-5bucqk9rsndjs8gjgmpqqmrsep", "jury", eventIDFK);
		qrcode.setCreatedAt(TimeHelper.getCurrentDateTime());
		User user = new User("TEST", "112233", "112233");
		int userid = userDao.addElement(user);
		int qrcodeid = qrDao.addElement(qrcode);
		user.setUserID(userid);
		AuthResult authQrCheck = AuthChecker.checkQRCodeAuthorization(qrcode.getPayload(),
				eventIDFK, user, "jury", TimeHelper.getCurrentDateTime());
		qrDao.deleteElement(qrcodeid);
		userDao.deleteElement(userid);
		assertTrue(authQrCheck.isAuthorized());
	}
	
	@Test
	public void testQrRestWrongUnkownQrCode() throws IOException {
		QrCode qrcode = new QrCode("TESTPAYLOAD", "jury", eventIDFK);
		qrcode.setCreatedAt(TimeHelper.getCurrentDateTime());
		User user = new User("TEST", "112233", "112233");
		int userid = userDao.addElement(user);
		user.setUserID(userid);
		AuthResult authQrCheck = AuthChecker.checkQRCodeAuthorization(qrcode.getPayload(),
				eventIDFK, user, "jury", TimeHelper.getCurrentDateTime());
		userDao.deleteElement(userid);
		assertFalse(authQrCheck.isAuthorized());
		assertTrue(authQrCheck.getResponseCode() == 403);
	}

	@Test
	public void testQrRestWrongUser() throws IOException {
		QrCode qrcode = new QrCode("TESTPAYLOAD", "jury", eventIDFK);
		qrcode.setUserIDFK(1); // <--
		qrcode.setCreatedAt(TimeHelper.getCurrentDateTime());
		User user = new User("TEST", "112233", "112233");
		int userid = userDao.addElement(user);
		int qrcodeid = qrDao.addElement(qrcode);
		user.setUserID(userid);
		AuthResult authQrCheck = AuthChecker.checkQRCodeAuthorization(qrcode.getPayload(),
				eventIDFK, user, "jury", TimeHelper.getCurrentDateTime());
		qrDao.deleteElement(qrcodeid);
		userDao.deleteElement(userid);
		assertFalse(authQrCheck.isAuthorized());
		assertTrue(authQrCheck.getResponseCode() == 403);
	}

	@Test
	public void testQrRestWrongRole() throws IOException {
		QrCode qrcode = new QrCode("TESTPAYLOAD", "moderator", eventIDFK);
		qrcode.setCreatedAt(TimeHelper.getCurrentDateTime());
		User user = new User("TEST", "112233", "112233");
		int userid = userDao.addElement(user);
		int qrcodeid = qrDao.addElement(qrcode);
		user.setUserID(userid);
		AuthResult authQrCheck = AuthChecker.checkQRCodeAuthorization(qrcode.getPayload(),
				eventIDFK, user, "jury", TimeHelper.getCurrentDateTime());
		qrDao.deleteElement(qrcodeid);
		userDao.deleteElement(userid);
		assertFalse(authQrCheck.isAuthorized());
		assertTrue(authQrCheck.getResponseCode() == 403);
	}

}
