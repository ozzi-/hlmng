package testing;

import static org.junit.Assert.*;

import java.util.Properties;

import hlmng.dao.GenDao;
import hlmng.dao.GenDaoLoader;
import hlmng.model.Event;
import hlmng.model.EventItem;
import hlmng.model.EventRoom;
import hlmng.model.Media;
import hlmng.model.Presentation;
import hlmng.model.Slider;
import hlmng.model.User;
import hlmng.model.Vote;
import hlmng.model.Voting;

import org.junit.Before;
import org.junit.Test;

import settings.HLMNGSettings;

public class DaoTest {
	
	GenDao userDao = GenDaoLoader.instance.getUserDao();
	GenDao eventDao = GenDaoLoader.instance.getEventDao();
	GenDao eventRoomDao = GenDaoLoader.instance.getEventRoomDao();
	GenDao eventItemDao = GenDaoLoader.instance.getEventItemDao();
	GenDao mediaDao = GenDaoLoader.instance.getMediaDao();
	GenDao votingDao = GenDaoLoader.instance.getVotingDao();
	GenDao voteDao = GenDaoLoader.instance.getVoteDao();
	GenDao sliderDao = GenDaoLoader.instance.getSliderDao();
	GenDao presentationDao = GenDaoLoader.instance.getPresentationDao();

	@Before
	public void db(){	
		Properties loginData= new Properties();
	    loginData.put("user",HLMNGSettings.jdbcUser);
	    loginData.put("password",HLMNGSettings.jdbcPassword);
	    userDao.setTest(true,loginData,HLMNGSettings.jdbcPath);
		eventDao.setTest(true,loginData,HLMNGSettings.jdbcPath);
		eventRoomDao.setTest(true,loginData,HLMNGSettings.jdbcPath);
		eventItemDao.setTest(true,loginData,HLMNGSettings.jdbcPath);
		mediaDao.setTest(true,loginData,HLMNGSettings.jdbcPath);
		votingDao.setTest(true,loginData,HLMNGSettings.jdbcPath);
		voteDao.setTest(true,loginData,HLMNGSettings.jdbcPath);
		sliderDao.setTest(true,loginData,HLMNGSettings.jdbcPath);
		presentationDao.setTest(true,loginData,HLMNGSettings.jdbcPath);
	}
	
	@Test
	public void testLastUpdateTimeNoChange(){
		long t1= presentationDao.getLastUpdateTime();
		
		assertTrue(System.currentTimeMillis()-t1<10000);
		presentationDao.listElements(true);
		
		long t2= presentationDao.getLastUpdateTime();
		
		assertTrue(t1==t2);
	}
	
	@Test
	public void testLastUpdateTimeChange(){
		long t1= presentationDao.getLastUpdateTime();
		Presentation presentation = new Presentation("TEST","TEST","TEST","00:10:10");
		int presentationid = presentationDao.addElement(presentation);
		presentationDao.deleteElement(presentationid);
		
		long t2= presentationDao.getLastUpdateTime();
		assertFalse(t1==t2);
	}
	
	@Test
	public void testVotingRelations(){
		
		Presentation presentation = new Presentation("TEST","TEST","TEST","00:10:10");
		int presentationid = presentationDao.addElement(presentation);
		
		Voting voting = new Voting("TEST", 10, "voting", 10,"00:00:50", "testmode", 1, presentationid, 1);
		int votingid = votingDao.addElement(voting);
		
		Slider slider = new Slider("TEST", 1, votingid);
		int sliderid = sliderDao.addElement(slider);

		Vote vote = new Vote(10, 1, sliderid, 1);
		int voteid = voteDao.addElement(vote);
		
		assertTrue(voteDao.listByFK("sliderIDFK", sliderid).size()==1);	
		assertTrue(voteDao.deleteElement(voteid));
		assertTrue(sliderDao.deleteElement(sliderid));
		assertTrue(votingDao.deleteElement(votingid));
		assertTrue(presentationDao.deleteElement(presentationid));
	}
	
	@Test
	 public void testNewUser(){
		boolean userCreate =testDaoGenericAdd(new User("name","deviceid","regid"), userDao);
		assertTrue(userCreate);
    }
	@Test
	 public void testNewEvent(){
		boolean eventCreate =testDaoGenericAdd(new Event("event","description","2014-01-01","2014-01-01",true), eventDao);
		assertTrue(eventCreate);
	}
	@Test
	 public void testNewMedia(){
		boolean mediaCreate =testDaoGenericAdd(new Media("testtype","testlink"), mediaDao);
		assertTrue(mediaCreate);
	}
	@Test
	 public void testNewEventRoom(){
		boolean eventRoomCreate =testDaoGenericAdd(new EventRoom(1, "name", "loc"), eventRoomDao);
		assertTrue(eventRoomCreate);
	}
	@Test
	 public void testNewEventItem(){
		boolean eventItemCreate =testDaoGenericAdd(new EventItem("name", "desc", "2015-05-05", "13:00", "14:00", 1, 1), eventItemDao);
		assertTrue(eventItemCreate);
	}
	@Test
	 public void testNewVote(){
		boolean voteCreate =testDaoGenericAdd(new Vote(10, 1, 1, 1), voteDao);
		assertTrue(voteCreate);
	}	
	@Test
	 public void testNewVoting(){
		boolean votingCreate =testDaoGenericAdd(new Voting("name", 10, "undef", 10, "00:05:00", "undef", 1, 1, 1), votingDao);
		assertTrue(votingCreate);
	}	
	@Test
	 public void testNewSlider(){
		boolean sliderCreate =testDaoGenericAdd(new Slider("name", 10, 1), sliderDao);
		assertTrue(sliderCreate);
	}	
	@Test
	 public void testNewPresentation(){
		boolean presentationCreate =testDaoGenericAdd(new Presentation("name", "name", "2015-05-05", "00:05:03"), presentationDao);
		assertTrue(presentationCreate);
	}	
	
	public boolean testDaoGenericAdd(Object element, GenDao dao){
		int elementCountBeforeAdd=dao.listElements(false).size();
		int elementID=dao.addElement(element);
		int elementCountAfterAdd=dao.listElements(false).size();
		dao.deleteElement(elementID);
		int elementCountAfterRemove=dao.listElements(false).size();
		return (elementCountAfterAdd==elementCountBeforeAdd+1 && elementCountBeforeAdd==elementCountAfterRemove);
	}
	@Test 
	public void testPut(){
		User user = new User("TESTUSER", "12345", "12345");
		int userid = userDao.addElement(user);
		user.setName("TESTUSERUPDATE");
		assertTrue(userDao.updateElement(user, userid));
		assertTrue(((User)userDao.getElement(userid)).getName().equals("TESTUSERUPDATE"));
		assertTrue(userDao.deleteElement(userid));
	}
	
	@Test
    public void testGetUsers(){
		boolean isNotEmpty = ! userDao.listElements(false).isEmpty();
		assertTrue(isNotEmpty);
    }	
	@Test
    public void testGetMedias(){
		boolean isNotEmpty = ! mediaDao.listElements(false).isEmpty();
		assertTrue(isNotEmpty);
    }
	@Test
    public void testGetEvents(){
		boolean isNotEmpty = ! eventDao.listElements(false).isEmpty();
		assertTrue(isNotEmpty);
    }

}

