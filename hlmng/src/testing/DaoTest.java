package testing;

import static org.junit.Assert.assertTrue;

import java.util.Properties;

import hlmng.dao.GenDao;
import hlmng.dao.GenDaoLoader;
import hlmng.model.Event;
import hlmng.model.Media;
import hlmng.model.Presentation;
import hlmng.model.Slider;
import hlmng.model.User;
import hlmng.model.Vote;
import hlmng.model.Voting;

import org.junit.Before;
import org.junit.Test;

public class DaoTest {
	
	GenDao userDao = GenDaoLoader.instance.getUserDao();
	GenDao eventDao = GenDaoLoader.instance.getEventDao();
	GenDao mediaDao = GenDaoLoader.instance.getMediaDao();
	GenDao votingDao = GenDaoLoader.instance.getVotingDao();
	GenDao voteDao = GenDaoLoader.instance.getVoteDao();
	GenDao sliderDao = GenDaoLoader.instance.getSliderDao();
	GenDao presentationDao = GenDaoLoader.instance.getPresentationDao();

	@Before
	public void db(){	
		Properties loginData= new Properties();
	    loginData.put("user","user");
	    loginData.put("password","pw12");
	    userDao.setTest(true,loginData,"jdbc:mysql://127.0.0.1/hlmng");
		eventDao.setTest(true,loginData,"jdbc:mysql://127.0.0.1/hlmng");
		mediaDao.setTest(true,loginData,"jdbc:mysql://127.0.0.1/hlmng");
		votingDao.setTest(true,loginData,"jdbc:mysql://127.0.0.1/hlmng");
		voteDao.setTest(true,loginData,"jdbc:mysql://127.0.0.1/hlmng");
		sliderDao.setTest(true,loginData,"jdbc:mysql://127.0.0.1/hlmng");
		presentationDao.setTest(true,loginData,"jdbc:mysql://127.0.0.1/hlmng");
	}
	@Test
	public void testVoting(){
		
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
	 public void testUser(){
		boolean ok =testDaoGeneric(new User("name","deviceid","regid"), userDao);
		assertTrue(ok);
    }
	@Test
	 public void testEvent(){
		boolean ok =testDaoGeneric(new Event("event","description","2014-01-01","2014-01-01"), eventDao);
		assertTrue(ok);
	}
	@Test
	 public void testMedia(){
		boolean ok =testDaoGeneric(new Media("testtype","testlink"), mediaDao);
		assertTrue(ok);
	}
	
	public boolean testDaoGeneric(Object element, GenDao dao){
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

