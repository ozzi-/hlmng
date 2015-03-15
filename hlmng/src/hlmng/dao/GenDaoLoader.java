package hlmng.dao;

import hlmng.model.Event;
import hlmng.model.EventItem;
import hlmng.model.User;

public enum GenDaoLoader {
	instance;
	
	private GenDao userDao;
	private GenDao eventDao;
	private GenDao eventItemDao;

	private GenDaoLoader() {
		System.out.println("GenDaoLoader starting!");
		userDao = new GenDao(User.class);
		eventDao = new GenDao(Event.class);
		eventItemDao = new GenDao(EventItem.class);
	}

	public GenDao getUserDao() {
		return userDao;
	}

	public GenDao getEventDao() {
		return eventDao;
	}
	

	public GenDao getEventItemDao() {
		return eventItemDao;
	}

}
