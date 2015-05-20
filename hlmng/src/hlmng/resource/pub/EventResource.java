package hlmng.resource.pub;

import hlmng.dao.GenDao;
import hlmng.dao.GenDaoLoader;
import hlmng.model.Event;
import hlmng.model.News;
import hlmng.model.Social;
import hlmng.resource.Resource;
import hlmng.resource.ResourceHelper;
import hlmng.resource.adm.UserResource;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import settings.HLMNGSettings;



@Path(HLMNGSettings.pubURL+"/event")
public class EventResource extends Resource {
	private GenDao eventDao =GenDaoLoader.instance.getEventDao();
	private GenDao newsDao =GenDaoLoader.instance.getNewsDao();
	private GenDao eventItemDao =GenDaoLoader.instance.getEventItemDao();
	private GenDao eventRoomDao =GenDaoLoader.instance.getEventRoomDao();
	private GenDao pushDao =GenDaoLoader.instance.getPushDao();
	private GenDao socialDao =GenDaoLoader.instance.getSocialDao();
	private GenDao votingDao =GenDaoLoader.instance.getVotingDao();
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object> getEvent() throws IOException {
		return listResource(eventDao, false);
	}
	@GET
	@Path("{id}/votings")
	public List<Object> getVotings(@PathParam("id") int id) throws IOException{
		List<Object> obj=votingDao.listByFK("eventIDFK",id);
		return obj;
	}
	@GET
	@Path("{id}/socials")
	public List<Object> getSocials(@PathParam("id") int id) throws IOException{
		List<Object> obj=socialDao.listByFK("eventIDFK",id);
		enrichSocialListWithUsernameAndMedia(obj);
		return obj;
	}
	@GET
	@Path("{id}/pushes")
	public List<Object> getEventPushes(@PathParam("id") int id) throws IOException{
		List<Object> obj=pushDao.listByFK("eventIDFK",id);
		return obj;
	}
	@GET
	@Path("{id}/eventitems")
	public List<Object> getEventItems(@PathParam("id") int id) throws IOException{
		List<Object> obj=eventItemDao.listByFK("eventIDFK",id);
		return obj;
	}
	@GET
	@Path("{id}/eventrooms")
	public List<Object> getEventRooms(@PathParam("id") int id) throws IOException{
		List<Object> obj=eventRoomDao.listByFK("eventIDFK",id);
		return obj;
	}
	@GET
	@Path("{id}/news")
	public List<Object> getSliders(@PathParam("id") int id) throws IOException{
		List<Object> obj= newsDao.listByFK("eventIDFK", id);
		enrichNewsListWithMedia(obj);
		return  obj;
	}	
	@GET
	@Path("/lastupdate")
	@Produces(MediaType.TEXT_PLAIN)
	public long getLastUpdateTime() throws IOException {
		return eventDao.getLastUpdateTime();
	}
	
	@GET
	@Path("{id}")
	public Event getEvent(@PathParam("id") int id) throws IOException{
		return (Event) getResource(eventDao, id);
	}
	
	protected void enrichSocialListWithUsernameAndMedia(List<Object> socialObjects) {
		for (Object object : socialObjects) {
			Social social = (Social) object;
			enrichSocialWithUsernameAndMedia(social);
		}
	}
	private void enrichSocialWithUsernameAndMedia(Social social) {
		String authorName = UserResource.getUsername(social.getUserIDFK());
		social.setAuthorName(authorName);
		String media = ResourceHelper.getMediaURL(uri, social.getMediaIDFK());
		social.setMedia(media);
	}
	protected void enrichNewsListWithMedia(List<Object> newsObjects) {
		for (Object object : newsObjects) {
			News news = (News) object;
			String media = ResourceHelper.getMediaURL(uri, news.getMediaIDFK());
			news.setMedia(media);
		}
	}

}

