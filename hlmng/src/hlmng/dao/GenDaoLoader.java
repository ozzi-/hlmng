package hlmng.dao;

import hlmng.model.Event;
import hlmng.model.EventItem;
import hlmng.model.EventRoom;
import hlmng.model.User;

public enum GenDaoLoader {
	instance;
	
	private GenDao userDao;
	private GenDao eventDao;
	private GenDao eventItemDao;
	private GenDao eventRoomDao;

	private GenDaoLoader() {
		System.out.println("GenDaoLoader starting!");
		userDao = new GenDao(User.class);
		eventDao = new GenDao(Event.class);
		eventItemDao = new GenDao(EventItem.class);
		eventRoomDao= new GenDao(EventRoom.class);
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

	public GenDao getEventRoomDao() {
		return eventRoomDao;
	}

	public void setEventRoomDao(GenDao eventRoomDao) {
		this.eventRoomDao = eventRoomDao;
	}

}
