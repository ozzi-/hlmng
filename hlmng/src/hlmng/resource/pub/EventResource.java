package hlmng.resource.pub;

import hlmng.dao.GenDao;
import hlmng.dao.GenDaoLoader;
import hlmng.dao.SocialDao;
import hlmng.model.Event;
import hlmng.model.EventItem;
import hlmng.resource.Resource;
import hlmng.resource.ResourceHelper;

import java.io.IOException;
import java.util.ArrayList;
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
	private SocialDao socialDao =GenDaoLoader.instance.getSocialDao();
	private GenDao votingDao =GenDaoLoader.instance.getVotingDao();
	private GenDao speakerDao =GenDaoLoader.instance.getSpeakerDao();	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object> getEvent() throws IOException {
		return listResource(eventDao, false);
	}
	@GET
	@Path("{id}/votings")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object> getVotings(@PathParam("id") int id) throws IOException{
		List<Object> obj=votingDao.listByFK("eventIDFK",id);
		return obj;
	}
	@GET
	@Path("{id}/socials")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object> getSocials(@PathParam("id") int id) throws IOException{
		List<Object> obj=socialDao.listByFK("eventIDFK",id);
		ResourceHelper.enrichSocialListWithUsernameAndMedia(uri,obj);
		return obj;
	}
	@GET
	@Path("{id}/socials/newest")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object> getNewestSocials(@PathParam("id") int id) throws IOException {
		List<Object> socialObjects =socialDao.getNewestAcceptedOrPublished(id);
		ResourceHelper.enrichSocialListWithUsernameAndMedia(uri,socialObjects);
		return socialObjects;
	}
	@GET
	@Path("{id}/pushes")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object> getEventPushes(@PathParam("id") int id) throws IOException{
		List<Object> obj=pushDao.listByFK("eventIDFK",id);
		return obj;
	}
	@GET
	@Path("{id}/eventitems")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object> getEventItems(@PathParam("id") int id) throws IOException{
		List<Object> obj=eventItemDao.listByFK("eventIDFK",id);
		return obj;
	}
	@GET
	@Path("{id}/speakers")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object> getEventSpeakers(@PathParam("id") int id) throws IOException{
		List<Object> eventItemObjList=eventItemDao.listByFK("eventIDFK",id);
		List<Object> speakersAtEvent= new ArrayList<Object>();
		List<Integer> speakerIDsAtEvent = new ArrayList<Integer>();
		for (Object eventItemObj : eventItemObjList) {
			EventItem eventItem = (EventItem) eventItemObj;
			if(!speakerIDsAtEvent.contains(eventItem.getSpeakerIDFK())){
				speakerIDsAtEvent.add(eventItem.getSpeakerIDFK());				
			}
		}
		for (Integer speakerID : speakerIDsAtEvent) {
			speakersAtEvent.add(speakerDao.getElement(speakerID));
		}
		return speakersAtEvent;
	}
	@GET
	@Path("{id}/eventrooms")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object> getEventRooms(@PathParam("id") int id) throws IOException{
		List<Object> obj=eventRoomDao.listByFK("eventIDFK",id);
		return obj;
	}
	@GET
	@Path("{id}/news")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object> getSliders(@PathParam("id") int id) throws IOException{
		List<Object> obj= newsDao.listByFK("eventIDFK", id);
		ResourceHelper.enrichNewsListWithMedia(uri,obj);
		return  obj;
	}	
	@GET
	@Path("{id}/news/newest")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object> getNewestNews(@PathParam("id") int id) throws IOException {
		List<Object> newsObjects = newsDao.listByFKLimited("eventIDFK", id);
		ResourceHelper.enrichNewsWithMedia(uri,newsObjects);
		return newsObjects;
	}
	@GET
	@Path("/lastupdate")
	@Produces(MediaType.TEXT_PLAIN)
	public long getLastUpdateTime() throws IOException {
		return eventDao.getLastUpdateTime();
	}
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Event getEvent(@PathParam("id") int id) throws IOException{
		return (Event) getResource(eventDao, id);
	}


}

