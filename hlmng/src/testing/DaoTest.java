package testing;

import static org.junit.Assert.assertTrue;

import java.util.Properties;

import hlmng.dao.GenDao;
import hlmng.dao.GenDaoLoader;
import hlmng.model.Event;
import hlmng.model.Media;
import hlmng.model.User;

import org.junit.Before;
import org.junit.Test;

public class DaoTest {
	
	GenDao userDao = GenDaoLoader.instance.getUserDao();
	GenDao eventDao = GenDaoLoader.instance.getEventDao();
	GenDao mediaDao = GenDaoLoader.instance.getMediaDao();
	
	@Before
	public void db(){	
		Properties loginData= new Properties();
	    loginData.put("user","user");
	    loginData.put("password","pw12");
	    userDao.setTest(true,loginData,"jdbc:mysql://127.0.0.1/hlmng");
		eventDao.setTest(true,loginData,"jdbc:mysql://127.0.0.1/hlmng");
		mediaDao.setTest(true,loginData,"jdbc:mysql://127.0.0.1/hlmng");

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


		int elementCountBeforeAdd=dao.listElements().size();
		System.out.println("BEFORE:"+elementCountBeforeAdd);
		int elementID=dao.addElement(element);
		
		int elementCountAfterAdd=dao.listElements().size();
		System.out.println("AFTER:"+elementCountAfterAdd);
		dao.deleteElement(elementID);
		
		int elementCountAfterRemove=dao.listElements().size();
		System.out.println("AFTER AFTER"+elementCountAfterRemove);
		return (elementCountAfterAdd==elementCountBeforeAdd+1 && elementCountBeforeAdd==elementCountAfterRemove);
	}

	@Test
    public void testGetUsers(){
		boolean isNotEmpty = ! userDao.listElements().isEmpty();
		assertTrue(isNotEmpty);
    }	
	@Test
    public void testGetMedias(){
		boolean isNotEmpty = ! mediaDao.listElements().isEmpty();
		assertTrue(isNotEmpty);
    }
	@Test
    public void testGetEvents(){
		boolean isNotEmpty = ! eventDao.listElements().isEmpty();
		assertTrue(isNotEmpty);
    }

}

