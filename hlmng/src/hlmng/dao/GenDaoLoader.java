package hlmng.dao;

import hlmng.model.Event;
import hlmng.model.EventItem;
import hlmng.model.EventRoom;
import hlmng.model.News;
import hlmng.model.Speaker;
import hlmng.model.User;
import hlmng.model.Media;

/**
 * Helper method which builds all specific Dao's of our generic Dao.
 */
public enum GenDaoLoader {
	instance;
	
	private UserDao userDao;
	private GenDao eventDao;
	private GenDao eventItemDao;
	private GenDao eventRoomDao;
	private GenDao speakerDao;
	private GenDao mediaDao;
	private GenDao newsDao;

	private GenDaoLoader() {
		System.out.println("GenDaoLoader starting");
		userDao = new UserDao(User.class);
		eventDao = new GenDao(Event.class);
		eventItemDao = new GenDao(EventItem.class);
		eventRoomDao= new GenDao(EventRoom.class);
		speakerDao= new GenDao(Speaker.class);
		mediaDao= new GenDao(Media.class);
		newsDao=new GenDao(News.class);
	}

	public UserDao getUserDao() {
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

	public GenDao getSpeakerDao() {
		return speakerDao;
	}

	public GenDao getMediaDao() {
		return mediaDao;
	}

	public GenDao getNewsDao() {
		return newsDao;
	}


}
