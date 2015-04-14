package hlmng.resource;


import hlmng.dao.GenDao;
import hlmng.dao.GenDaoLoader;
import hlmng.model.EventItem;

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



@Path("/eventitem")
public class EventItemResource extends Resource{
	
	private GenDao eventItemDao =GenDaoLoader.instance.getEventItemDao();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object> getEventItems() throws IOException {
		return listResource(eventItemDao, false);
	}
	
	
	@GET
	@Path("/lastupdate")
	@Produces(MediaType.TEXT_PLAIN)
	public long getLastUpdateTime() throws IOException {
		return eventItemDao.getLastUpdateTime();
	}
	
	@GET
	@Path("{id}")
	public EventItem getEventItem(@PathParam("id") int id) throws IOException{
		return (EventItem) getResource(eventItemDao, id);	
	}

	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putEventItem(EventItem element,@PathParam("id") int id) throws IOException {
		return putResource(eventItemDao, element, id);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Object postEventItem(EventItem element) throws IOException {
		return postResource(eventItemDao, element,true);
	}
	
	@DELETE
	@Path("{id}")
	public Response deleteEventItem(@PathParam("id") int id) throws IOException {
		return deleteResource(eventItemDao, id);
	}
	
}

