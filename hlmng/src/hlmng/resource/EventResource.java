package hlmng.resource;

import hlmng.dao.GenDaoLoader;
import hlmng.model.Event;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;



@Path("/event")
public class EventResource  {
	
    @Context
    private UriInfo uriInfo;
	@Context
	private Request request;
	@Context
	private String id;
	@Context 
	private HttpServletResponse response;

	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object> getEvent() {
		return GenDaoLoader.instance.getEventDao().listElements();
	}

	
	@GET
	@Path("count")
	@Produces(MediaType.TEXT_PLAIN)
	public int getCount() {
		return GenDaoLoader.instance.getEventDao().listElements().size();
	}



	
	@GET
	@Path("{id}")
	public Event getEvent(@PathParam("id") String id,
			@Context HttpHeaders headers,
			@Context HttpServletResponse servletResponse) throws IOException{
		Object obj =  GenDaoLoader.instance.getEventDao().getElement(id);
		ResourceHelper.sendErrorIfNull(obj,response);
		Event evt=(Event) obj;
		return evt;

	
	}

	@SuppressWarnings("unchecked")
	@GET
	@Path("{id}/eventitems")
	public List<Object> getEventItems(@PathParam("id") String id,
			@Context HttpHeaders headers,
			@Context HttpServletResponse servletResponse) throws IOException{
		Object obj=GenDaoLoader.instance.getEventItemDao().listByFK("eventIDFK",id);
		ResourceHelper.sendErrorIfNull(obj,response);
		return (List<Object>) obj;
	}
	
	@SuppressWarnings("unchecked")
	@GET
	@Path("{id}/eventrooms")
	public List<Object> getEventRooms(@PathParam("id") String id,
			@Context HttpHeaders headers,
			@Context HttpServletResponse servletResponse) throws IOException{
		Object obj=GenDaoLoader.instance.getEventRoomDao().listByFK("eventIDFK",id);
		ResourceHelper.sendErrorIfNull(obj,response);
		return (List<Object>) obj;
	}


	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putEvent(Event element,@PathParam("id") String id) {
		Response res;
		if (GenDaoLoader.instance.getEventDao().getElement(id)!=null){
			GenDaoLoader.instance.getEventDao().updateElement(element, id);
			res = Response.accepted().build();
		}else{
			boolean ok = GenDaoLoader.instance.getEventDao().addElement(element);
			res= ResourceHelper.returnOkOrErrorResponse(ok);
		}
		return res;	
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response newEvent(Event element) throws IOException {
		boolean ok = GenDaoLoader.instance.getEventDao().addElement(element);
		return ResourceHelper.returnOkOrErrorResponse(ok);
	}


	
	// FORMS
	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response newEvent(@FormParam("name") String name,
			@FormParam("description") String description, 
			@FormParam("start") String start,
			@FormParam("end") String end,
			@Context HttpServletResponse servletResponse) throws IOException {
		Event addEvent = new Event(name,description,start,end);
		boolean ok = GenDaoLoader.instance.getEventDao().addElement(addEvent);
		return ResourceHelper.returnOkOrErrorResponse(ok);
	}


}

