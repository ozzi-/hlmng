package hlmng.dao;

import hlmng.model.Event;
import hlmng.model.User;

public enum GenDaoLoader {
	instance;
	
	private GenDao userDao;
	private GenDao eventDao;

	private GenDaoLoader() {
		System.out.println("GenDaoLoader starting!");
		userDao = new GenDao(User.class);
		eventDao = new GenDao(Event.class);
	}

	public GenDao getUserDao() {
		return userDao;
	}

	public GenDao getEventDao() {
		return eventDao;
	}

}
