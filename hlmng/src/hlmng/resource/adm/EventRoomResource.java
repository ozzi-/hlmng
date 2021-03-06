package hlmng.resource.adm;


import hlmng.dao.GenDao;
import hlmng.dao.GenDaoLoader;
import hlmng.model.EventRoom;
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



@Path(HLMNGSettings.admURL+"/eventroom")
public class EventRoomResource extends Resource  {

	private GenDao eventRoomDao =GenDaoLoader.instance.getEventRoomDao();
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object> getEventRoom() throws IOException {
		return listResource(eventRoomDao, false);
	}

	
	@GET
	@Path("/lastupdate")
	@Produces(MediaType.TEXT_PLAIN)
	public long getLastUpdateTime() throws IOException {
		return eventRoomDao.getLastUpdateTime();
	}
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public EventRoom getEventRoom(@PathParam("id") int id) throws IOException{
		return (EventRoom) getResource(eventRoomDao, id);
	}

	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response putEventRoom(EventRoom element,@PathParam("id") int id) throws IOException {
		return putResource(eventRoomDao, element, id);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Object postEventRoom(EventRoom element) throws IOException {
		return postResource(eventRoomDao, element);
	}
	
	@DELETE
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteEventRoom(EventRoom element,@PathParam("id") int id) throws IOException {
		return deleteResource(eventRoomDao, id);
	}
	

}

