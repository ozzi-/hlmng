package testing;

import static org.junit.Assert.assertTrue;
import hlmng.dao.GenDao;
import hlmng.dao.GenDaoLoader;
import hlmng.model.User;

import org.junit.Test;

public class DaoTest {
	@Test
	 public void addUser(){
		GenDao userDao = GenDaoLoader.instance.getUserDao();
		
		int userCountBeforeAdd=userDao.listElements().size();
		
		User user = new User("AddUserTest", "DeviceID", "RegID");
		int userID=userDao.addElement(user);
		
		int userCountAfterAdd=userDao.listElements().size();
		userDao.deleteElement(Integer.toString(userID));
		
		int userCountAfterRemove=userDao.listElements().size();
		assertTrue(userCountAfterAdd>userCountBeforeAdd && userCountBeforeAdd==userCountAfterRemove);
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
