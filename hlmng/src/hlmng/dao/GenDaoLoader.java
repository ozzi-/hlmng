package hlmng.dao;

import hlmng.model.Event;
import hlmng.model.EventItem;
import hlmng.model.EventRoom;
import hlmng.model.Media;
import hlmng.model.News;
import hlmng.model.Push;
import hlmng.model.QrCode;
import hlmng.model.Slider;
import hlmng.model.Social;
import hlmng.model.Speaker;
import hlmng.model.User;
import hlmng.model.Vote;
import hlmng.model.Voting;

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
	private QrCodeDao qrCodeDao;
	private VoteDao voteDao;
	private GenDao votingDao;
	private GenDao sliderDao;
	private GenDao presentationDao;
	private GenDao pushDao;
	private GenDao socialDao;
	
	private GenDaoLoader() {
		eventDao = new GenDao(Event.class);
		eventItemDao = new GenDao(EventItem.class);
		eventRoomDao= new GenDao(EventRoom.class);
		mediaDao= new GenDao(Media.class);
		newsDao=new GenDao(News.class);
		pushDao=new GenDao(Push.class);
		qrCodeDao=new QrCodeDao(QrCode.class);
		sliderDao=new GenDao(Slider.class);
		socialDao=new GenDao(Social.class);
		speakerDao= new GenDao(Speaker.class);
		userDao = new UserDao(User.class);
		voteDao=new VoteDao(Vote.class);
		votingDao=new GenDao(Voting.class);
	}
	public GenDao getSliderDao() {
		return sliderDao;
	}
	public void setSliderDao(GenDao sliderDao) {
		this.sliderDao = sliderDao;
	}
	public GenDao getSocialDao() {
		return socialDao;
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
	public QrCodeDao getQrCodeDao() {
		return qrCodeDao;
	}
	public GenDao getVoteDao() {
		return voteDao;
	}
	public GenDao getVotingDao() {
		return votingDao;
	}
	public GenDao getPushDao() {
		return pushDao;
	}
	public GenDao getPresentationDao() {
		return presentationDao;
	}

}
