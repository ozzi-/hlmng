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
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.junit.After;
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
	private static final int eventID= 9999;
	private static int userID;
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
		eventDao.addIDElement(new Event(eventID,"test", "test", "2015-05-05", "2015-05-05",true));
		userID=userDao.addElement(new User("test", "2221128", "72727"));
	}
	@After
	public void bye(){
		assertTrue(eventDao.deleteElement(eventID));
	}

	@SuppressWarnings("serial")
	@Test
	/**
	 * In detail calculations of this test can be found as an excel sheet, see /src/doc/testing.xls
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void testVoteScoring() throws IOException, InterruptedException{
		Voting voting = new Voting("Name",10,"voting_end",10,"2015-05-05","00:00:40","arithemtic","00:05:00","00:07:00","13:00:00","13:06:00",1,1,eventID);
		int votingid = votingDao.addElement(voting);

		Slider slider1 = new Slider("slider1",1, votingid);
		int sliderid1 = sliderDao.addElement(slider1);
		Slider slider3 = new Slider("slider3",3, votingid);
		int sliderid3 = sliderDao.addElement(slider3);
		Slider slider2 = new Slider("slider2",2, votingid);
		int sliderid2 = sliderDao.addElement(slider2);
		
		ArrayList<Integer> voteList= new ArrayList<>();
		ArrayList<Vote> voteObjList= new ArrayList<>();
		// Jury
		ArrayList<Integer> voteScoreSlider1Jury = new ArrayList<Integer>(){{
			add(6);
			add(5);
			add(4);
			add(5);
			add(6);
			add(7);
			add(8);
			add(10);
			add(10);
		}};
		ArrayList<Integer> voteScoreSlider3Jury = new ArrayList<Integer>(){{
			add(7);
			add(4);
			add(3);
			add(5);
			add(6);
			add(4);
			add(3);
			add(2);
			add(4);
		}};
		ArrayList<Integer> voteScoreSlider05Jury = new ArrayList<Integer>(){{
			add(5);
			add(6);
			add(7);
			add(8);
			add(6);
			add(5);
			add(4);
			add(3);
			add(3);
		}};
		// Audience
		ArrayList<Integer> voteScoreSlider1Audience = new ArrayList<Integer>(){{
			add(6);
			add(5);
			add(4);
			add(5);
			add(6);
			add(7);
			add(8);
			add(10);
			add(10);
		}};
		ArrayList<Integer> voteScoreSlider3Audience = new ArrayList<Integer>(){{
			add(7);
			add(4);
			add(3);
			add(5);
			add(6);
			add(4);
			add(3);
			add(2);
			add(4);
		}};
		ArrayList<Integer> voteScoreSlider05Audience = new ArrayList<Integer>(){{
			add(5);
			add(6);
			add(7);
			add(8);
			add(6);
			add(5);
			add(4);
			add(3);
			add(3);
		}};
		// Jury
		for (Integer score : voteScoreSlider1Jury) {
			voteObjList.add(new Vote(score,1,sliderid1,userID));
		}
		for (Integer score : voteScoreSlider3Jury) {
			voteObjList.add(new Vote(score,1,sliderid3,userID));
		}
		for (Integer score : voteScoreSlider05Jury) {
			voteObjList.add(new Vote(score,1,sliderid2,userID));
		}
		// Audience
		for (Integer score : voteScoreSlider1Audience) {
			voteObjList.add(new Vote(score,0,sliderid1,userID));
		}
		for (Integer score : voteScoreSlider3Audience) {
			voteObjList.add(new Vote(score,0,sliderid3,userID));
		}
		for (Integer score : voteScoreSlider05Audience) {
			voteObjList.add(new Vote(score,0,sliderid2,userID));
		}
		// INSERT
		for (Vote vote : voteObjList) {
			voteList.add(voteDao.addElement(vote));
		}
		
		try {
			turnOffSslChecking();
		} catch (KeyManagementException e) {
		} catch (NoSuchAlgorithmException e) {
		}

		double totalscoreaudience =  Double.parseDouble(doURL(HLMNGSettings.restAppPath+HLMNGSettings.admURL+"/voting/"+votingid+"/totalscoreaudience", "GET",null,null));
		double totalscorejury =  Double.parseDouble(doURL(HLMNGSettings.restAppPath+HLMNGSettings.admURL+"/voting/"+votingid+"/totalscorejury", "GET",null,null));
		
		Voting votingNotInTime = new Voting("Name",10,"voting_end",10,"2015-05-05","00:00:40","arithemtic","00:05:00","00:07:00","13:00:00","13:08:00",1,1,eventID);
		assertTrue(votingDao.updateElement(votingNotInTime,votingid));
		double totalscorejuryNotInTime =  Double.parseDouble(doURL(HLMNGSettings.restAppPath+HLMNGSettings.admURL+"/voting/"+votingid+"/totalscorejury", "GET",null,null));
		
		// CLEANUP
		for (Integer voteID : voteList) {
			assertTrue(voteDao.deleteElement(voteID));
		}
		assertTrue(sliderDao.deleteElement(sliderid1));
		assertTrue(sliderDao.deleteElement(sliderid3));
		assertTrue(sliderDao.deleteElement(sliderid2));
		Thread.sleep(50); // the DB isn't too fast on my laptop, else FK constraint fails sometimes
		assertTrue(votingDao.deleteElement(votingid));
		assertEquals(4.944,totalscoreaudience,0.05);
		assertEquals(5.698,totalscorejury,0.05);
		assertEquals(4.269,totalscorejuryNotInTime,0.05);

	}
	
	@Test
	public void checkVoteCorrectAuth(){
		cleanUpTestUsers();
		User user = new User("testusername", "1234test1234", "4321test");
		int userid  = userDao.addElement(user);
			
		Voting voting = new Voting("TEST", 10, "voting", 10,"2015-05-05","00:00:50", "median","00:05:30","00:06:30",TimeHelper.getCurrentTime(),"00:07:00", 1,1,eventID);
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
			
		Voting voting = new Voting("TEST", 10, "voting", 10,"2015-05-05","00:00:50", "testmode","00:03:50","00:33:33","00:07:00","00:07:00",1,1,eventID);
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
		System.out.println(votingid+" !!!");
		assertTrue(votingDao.deleteElement(votingid));
		assertTrue(userDao.deleteElement(userid));
	}

	@Test
	public void testEventRest() throws IOException {
		int elementID = 949494;
		Event orig = new Event(elementID,"eventname", "description", "2015-01-01",
				"2015-01-01",true);
		eventDao.addIDElement(orig);
		
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
