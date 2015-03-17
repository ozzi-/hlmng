package testing;

import static org.junit.Assert.assertTrue;
import hlmng.dao.GenDao;
import hlmng.dao.GenDaoLoader;
import hlmng.model.Event;
import hlmng.model.Media;
import hlmng.model.User;

import org.junit.Test;

public class DaoTest {
	@Test
	 public void testUser(){
		boolean ok =testDaoGeneric(new User("name","deviceid","regid"), GenDaoLoader.instance.getUserDao());
		assertTrue(ok);
    }
	@Test
	 public void testEvent(){
		boolean ok =testDaoGeneric(new Event("event","description","2014-01-01","2014-01-01"), GenDaoLoader.instance.getEventDao());
		assertTrue(ok);
	}
	@Test
	 public void testMedia(){
		boolean ok =testDaoGeneric(new Media("testtype","testlink"), GenDaoLoader.instance.getMediaDao());
		assertTrue(ok);
	}
	
	public boolean testDaoGeneric(Object element, GenDao dao){

		int elementCountBeforeAdd=dao.listElements().size();
		
		int elementID=dao.addElement(element);
		
		int elementCountAfterAdd=dao.listElements().size();
		dao.deleteElement(Integer.toString(elementID));
		
		int elementCountAfterRemove=dao.listElements().size();
		return (elementCountAfterAdd==elementCountBeforeAdd+1 && elementCountBeforeAdd==elementCountAfterRemove);
	}

	@Test
    public void testGetUsers(){
		boolean isNotEmpty = ! GenDaoLoader.instance.getUserDao().listElements().isEmpty();
		assertTrue(isNotEmpty);
    }	
	@Test
    public void testGetMedias(){
		boolean isNotEmpty = ! GenDaoLoader.instance.getMediaDao().listElements().isEmpty();
		assertTrue(isNotEmpty);
    }
	@Test
    public void testGetEvents(){
		boolean isNotEmpty = ! GenDaoLoader.instance.getEventDao().listElements().isEmpty();
		assertTrue(isNotEmpty);
    }
	@Test
    public void testGetSpeakers(){
		boolean isNotEmpty = ! GenDaoLoader.instance.getSpeakerDao().listElements().isEmpty();
		assertTrue(isNotEmpty);
    }
}
