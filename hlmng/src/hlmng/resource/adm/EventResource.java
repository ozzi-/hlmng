package hlmng.resource.adm;

import hlmng.dao.GenDao;
import hlmng.dao.GenDaoLoader;
import hlmng.model.Event;
import hlmng.resource.Resource;

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
	 
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object> getEvent() throws IOException {
		return listResource(eventDao, false);
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

	@SuppressWarnings("unchecked")
	@GET
	@Path("{id}/eventitems")
	public List<Object> getEventItems(@PathParam("id") int id) throws IOException{
		Object obj=GenDaoLoader.instance.getEventItemDao().listByFK("eventIDFK",id);
		return (List<Object>) obj;
	}
	
	@SuppressWarnings("unchecked")
	@GET
	@Path("{id}/eventrooms")
	public List<Object> getEventRooms(@PathParam("id") int id) throws IOException{
		Object obj=GenDaoLoader.instance.getEventRoomDao().listByFK("eventIDFK",id);
		return (List<Object>) obj;
	}


	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putEvent(Event element,@PathParam("id") int id) throws IOException {
		return putResource(eventDao,element,id);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Object postEvent(Event element) throws IOException {
		return postResource(eventDao, element);
	}
	
	@DELETE
	@Path("{id}")
	public Response deleteEvent(@PathParam("id") int id) throws IOException {
		return deleteResource(eventDao, id);
	}
}

