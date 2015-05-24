package testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import hlmng.dao.GenDao;
import hlmng.dao.GenDaoLoader;
import hlmng.model.Event;
import hlmng.model.ModelHelper;
import hlmng.model.Slider;
import hlmng.model.User;
import hlmng.model.Vote;
import hlmng.model.Voting;
import hlmng.resource.TimeHelper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.Properties;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.junit.Before;
import org.junit.Test;

import settings.HLMNGSettings;

import com.owlike.genson.Genson;

public class RestTest {

	GenDao qrDao = GenDaoLoader.instance.getQrCodeDao();
	GenDao userDao = GenDaoLoader.instance.getUserDao();
	GenDao eventDao = GenDaoLoader.instance.getEventDao();
	GenDao voteDao = GenDaoLoader.instance.getVoteDao();
	GenDao votingDao = GenDaoLoader.instance.getVotingDao();
	GenDao sliderDao = GenDaoLoader.instance.getSliderDao();
	

	@Before
	public void init() {
		Properties loginData = new Properties();
		loginData.put("user", HLMNGSettings.jdbcUser);
		loginData.put("password", HLMNGSettings.jdbcPassword);
		qrDao.setTest(true, loginData, HLMNGSettings.jdbcPath);
		userDao.setTest(true, loginData, HLMNGSettings.jdbcPath);
		eventDao.setTest(true, loginData, HLMNGSettings.jdbcPath);
		voteDao.setTest(true, loginData, HLMNGSettings.jdbcPath);
		votingDao.setTest(true, loginData, HLMNGSettings.jdbcPath);
		sliderDao.setTest(true, loginData, HLMNGSettings.jdbcPath);
	}

	@Test
	public void testVoteScoring() throws IOException{
		Voting voting = new Voting("Name",10,"voting_end",8,"00:00:40","arithemtic","00:05:00","00:07:00","13:00:00","13:06:00",1,1,1);
		int votingid = votingDao.addElement(voting);

		Slider slider1 = new Slider("slider1",1, votingid);
		int sliderid1 = sliderDao.addElement(slider1);
		Slider slider2 = new Slider("slider2",2, votingid);
		int sliderid2 = sliderDao.addElement(slider2);
		Slider slider4 = new Slider("slider4",4, votingid);
		int sliderid4 = sliderDao.addElement(slider4);
		// public votings
		Vote vote1_1 = new Vote(10,0,sliderid1,1);
		int vote1_1_id = voteDao.addElement(vote1_1);
		
		Vote vote2_1 = new Vote(5,0,sliderid2,1);
		int vote2_1_id = voteDao.addElement(vote2_1);
		
		Vote vote4_1 = new Vote(7,0,sliderid4,1);
		int vote4_1_id = voteDao.addElement(vote4_1);
		// jury votings
		Vote vote1_2 = new Vote(7,1,sliderid1,1);
		int vote1_2_id = voteDao.addElement(vote1_2);
		
		Vote vote2_2 = new Vote(10,1,sliderid2,1);
		int vote2_2_id = voteDao.addElement(vote2_2);

		Vote vote4_2 = new Vote(5,1,sliderid4,1);
		int vote4_2_id = voteDao.addElement(vote4_2);


		try {
			turnOffSslChecking();
		} catch (KeyManagementException e) {
		} catch (NoSuchAlgorithmException e) {
		}

		double response =  Double.parseDouble(doURL(HLMNGSettings.restAppPath+HLMNGSettings.admURL+"/voting/"+votingid+"/totalscoreaudience", "GET",null,null));
		double response2 =  Double.parseDouble(doURL(HLMNGSettings.restAppPath+HLMNGSettings.admURL+"/voting/"+votingid+"/totalscorejury", "GET",null,null));

		assertTrue(voteDao.deleteElement(vote4_2_id));
		assertTrue(voteDao.deleteElement(vote2_2_id));
		assertTrue(voteDao.deleteElement(vote1_2_id));
		assertTrue(voteDao.deleteElement(vote4_1_id));
		assertTrue(voteDao.deleteElement(vote2_1_id));
		assertTrue(voteDao.deleteElement(vote1_1_id));
		assertTrue(sliderDao.deleteElement(sliderid4));
		assertTrue(sliderDao.deleteElement(sliderid2));
		assertTrue(sliderDao.deleteElement(sliderid1));
		assertTrue(votingDao.deleteElement(votingid));
		assertEquals(6.857,response,0.05);
		assertEquals(6.714,response2,0.05);

	}
	
	@Test
	public void checkVoteCorrectAuth(){
		cleanUpTestUsers();
		User user = new User("testusername", "1234test1234", "4321test");
		int userid  = userDao.addElement(user);
			
		Voting voting = new Voting("TEST", 10, "voting", 10,"00:00:50", "median","00:05:30","00:06:30",TimeHelper.getCurrentTime(),"00:07:00", 1,1,1);
		voting.setVotingStarted(TimeHelper.getCurrentTime());
		int votingid = votingDao.addElement(voting);
		
		Slider slider = new Slider("TEST", 1, votingid);
		int sliderid = sliderDao.addElement(slider);

		String postData= "{\"isJury\": false,\"score\": 7,\"sliderIDFK\": "+sliderid+",\"userIDFK\": "+userid+"}";
		String response="";
		try {
			try {
				turnOffSslChecking();
			} catch (KeyManagementException e) {
			} catch (NoSuchAlgorithmException e) {
			}
			response = doURL(HLMNGSettings.restAppPath+HLMNGSettings.pubURL+"/vote/", "POST",postData,"Basic dGVzdHVzZXJuYW1lOjEyMzR0ZXN0MTIzNA==");
		} catch (IOException e){
			e.printStackTrace();
		}
		assertTrue(response.contains("voteID"));
		Genson genson = new Genson();
		Vote vote = genson.deserialize(response,Vote.class);
		assertTrue(voteDao.deleteElement(vote.getVoteID()));
		assertTrue(sliderDao.deleteElement(sliderid));
		assertTrue(votingDao.deleteElement(votingid));
		assertTrue(userDao.deleteElement(userid));
	}

	/**
	 * If the JUnit test should fail, we still have a "testusername" user, clean up before running
	 */
	private void cleanUpTestUsers() {
		List<Object> userList = userDao.listElements(false);
		for (Object object : userList) {
			User user = (User) object;
			if(user.getName().equals("testusername")){
				userDao.deleteElement(user.getUserID());
			}
		}
	}
	
	@Test
	public void checkVoteMissingAuth(){
		
		User user = new User("name", "1234", "4321");
		int userid  = userDao.addElement(user);
			
		Voting voting = new Voting("TEST", 10, "voting", 10,"00:00:50", "testmode","00:03:50","00:33:33","00:07:00","00:07:00",1,1,1);
		int votingid = votingDao.addElement(voting);
		
		Slider slider = new Slider("TEST", 1, votingid);
		int sliderid = sliderDao.addElement(slider);

		String postData= "{\"isJury\": false,\"score\": 7,\"sliderIDFK\": "+sliderid+",\"userIDFK\": "+userid+"},";
		
		try {
			try {
				turnOffSslChecking();
			} catch (KeyManagementException e) {
			} catch (NoSuchAlgorithmException e) {
			}
			doURL(HLMNGSettings.restAppPath+HLMNGSettings.pubURL+"/vote/", "POST",postData,null);
		} catch (IOException e){
			assertTrue(e.getLocalizedMessage().contains("Server returned HTTP response code: 401"));
		}
		
		assertTrue(sliderDao.deleteElement(sliderid));
		assertTrue(votingDao.deleteElement(votingid));
		assertTrue(userDao.deleteElement(userid));
	}

	@Test
	public void testEventRest() throws IOException {
		Event orig = new Event("eventname", "description", "2015-01-01",
				"2015-01-01",true);
		int elementID = eventDao.addElement(orig);
		String response = "";
		try {
			turnOffSslChecking();
			response = doURL(HLMNGSettings.restAppPath+HLMNGSettings.admURL+"/event/"
					+ elementID, "GET",null,null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		orig.setEventID(elementID);
		Genson genson = new Genson();
		Event excpected = genson.deserialize(response, Event.class);
		assertTrue(eventDao.deleteElement(elementID));
		assertTrue(ModelHelper.compare(excpected, orig));
		// ^ Note to self: If this fails, make sure the server is actually started...
	}

	private String doURL(String url, String method,String contentBody,String auth) throws IOException {

		javax.net.ssl.HttpsURLConnection
				.setDefaultHostnameVerifier(new javax.net.ssl.HostnameVerifier() {
					public boolean verify(String hostname,
							javax.net.ssl.SSLSession sslSession) {
						if (hostname.equals("localhost")) {
							return true; // this is ok for a junit test
						}
						return false;
					}
				});

	      
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod(method);
		con.setRequestProperty("User-Agent", "Mozilla/5.0");
		if(contentBody!=null){
			con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			con.setDoOutput(true);
			if(auth!=null){
				con.setRequestProperty("Authorization", auth);
			}
			con.setRequestProperty("Content-Length", "" +  Integer.toString(contentBody.getBytes().length));
			DataOutputStream wr = new DataOutputStream (con.getOutputStream ());
			wr.writeBytes (contentBody);
			wr.flush ();
			wr.close ();			
		}

		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		return response.toString();
	}

	/*
	 * Following code is from: http://stackoverflow.com/questions/23504819/how-to-disable-ssl-certificate-checking-with-spring-resttemplate
	 * NEVER do this in production, this is only used in the junit test!
	 */
	private static final TrustManager[] UNQUESTIONING_TRUST_MANAGER = new TrustManager[] { new X509TrustManager() {
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		@Override
		public void checkClientTrusted(
				java.security.cert.X509Certificate[] arg0, String arg1)
				throws CertificateException {
			
		}

		@Override
		public void checkServerTrusted(
				java.security.cert.X509Certificate[] arg0, String arg1)
				throws CertificateException {
	
		}
	} };

	public static void turnOffSslChecking() throws NoSuchAlgorithmException,
			KeyManagementException {
		// Install the all-trusting trust manager
		final SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, UNQUESTIONING_TRUST_MANAGER, null);
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	}

	public static void turnOnSslChecking() throws KeyManagementException,
			NoSuchAlgorithmException {
		// Return it to the initial state (discovered by reflection, now
		// hardcoded)
		SSLContext.getInstance("SSL").init(null, null, null);
	}

}
