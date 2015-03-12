package hlmng.resource;

import hlmng.dao.EventDao;
import hlmng.model.Event;

import java.io.IOException;
import java.sql.Date;
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
		return EventDao.instance.listElements();
	}

	
	@GET
	@Path("count")
	@Produces(MediaType.TEXT_PLAIN)
	public int getCount() {
		return EventDao.instance.listElements().size();
	}



	
	@GET
	@Path("{id}")
	public Event getEvent(@PathParam("id") String id,
			@Context HttpHeaders headers,
			@Context HttpServletResponse servletResponse) throws IOException{
		Object obj=EventDao.instance.getElement(id);
		if(obj==null){
		    response.sendError(Response.Status.NOT_FOUND.getStatusCode());
		}
		Event evt=(Event) obj;
		return evt;

	
	}
	 
	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putEvent(Event element,@PathParam("id") String id) {
		Response res;
		if (EventDao.instance.getElement(id)!=null) {
			EventDao.instance.updateElement(element,id);
			res = Response.accepted().build();
		}else{
			EventDao.instance.addElement(element);
			res = Response.created(uriInfo.getAbsolutePath()).build();	
		}
		return res;	
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response newEvent(Event element) throws IOException {
		EventDao.instance.addElement(element);
		return Response.ok().build();
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
		EventDao.instance.addElement(addEvent);
		return Response.ok().build();
	}


}

