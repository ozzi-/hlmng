package hlmng.resource.adm;

import hlmng.dao.GenDao;
import hlmng.dao.GenDaoLoader;
import hlmng.model.Event;
import hlmng.resource.Resource;
import hlmng.resource.ResourceHelper;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import settings.HLMNGSettings;



@Path(HLMNGSettings.admURL+"/event")
public class EventResource extends Resource {
	private GenDao eventDao =GenDaoLoader.instance.getEventDao();
	private GenDao newsDao =GenDaoLoader.instance.getNewsDao();
	private GenDao eventItemDao =GenDaoLoader.instance.getEventItemDao();
	private GenDao eventRoomDao =GenDaoLoader.instance.getEventRoomDao();
	private GenDao pushDao =GenDaoLoader.instance.getPushDao();
	private GenDao socialDao =GenDaoLoader.instance.getSocialDao();
	private GenDao votingDao =GenDaoLoader.instance.getVotingDao();
	private GenDao qrCodeDao =GenDaoLoader.instance.getQrCodeDao();
	
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
		List<Object> socialObjects = socialDao.listByFKLimited("eventIDFK", id);
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
	@Path("{id}/eventrooms")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object> getEventRooms(@PathParam("id") int id) throws IOException{
		List<Object> obj=eventRoomDao.listByFK("eventIDFK",id);
		return obj;
	}
	@GET
	@Path("{id}/qrcodes")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object> getQrCodes(@PathParam("id") int id) throws IOException{
		List<Object> obj=qrCodeDao.listByFK("eventIDFK",id);
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
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object> getEvent() throws IOException {
		List<Object> eventObjects = listResource(eventDao, false);
		ResourceHelper.enrichEventListWithMedia(uri, eventObjects);
		return eventObjects;
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
		Event event = (Event) getResource(eventDao, id);
		ResourceHelper.enrichEventWithMedia(uri, event);
		return event;
	}
	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response putEvent(Event element,@PathParam("id") int id) throws IOException {
		return putResource(eventDao,element,id);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Object postEvent(Event element) throws IOException {
		return (Event)postResourceWithID(eventDao, element);
	}
	
	@DELETE
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteEvent(@PathParam("id") int id) throws IOException {
		return deleteResource(eventDao, id);
	}

}

