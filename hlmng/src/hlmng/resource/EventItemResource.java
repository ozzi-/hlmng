package hlmng.resource;


import hlmng.dao.GenDaoLoader;
import hlmng.model.EventItem;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
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



@Path("/eventitem")
public class EventItemResource  {
	
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
		return GenDaoLoader.instance.getEventItemDao().listElements();
	}

	
	@GET
	@Path("count")
	@Produces(MediaType.TEXT_PLAIN)
	public int getCount() {
		return GenDaoLoader.instance.getEventItemDao().listElements().size();
	}



	
	@GET
	@Path("{id}")
	public EventItem getEvent(@PathParam("id") String id,
			@Context HttpHeaders headers,
			@Context HttpServletResponse servletResponse) throws IOException{
		Object obj =  GenDaoLoader.instance.getEventItemDao().getElement(id);
		ResourceHelper.sendErrorIfNull(obj,response);
		EventItem evt=(EventItem) obj;
		return evt;	
	}





	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putEvent(EventItem element,@PathParam("id") String id) {
		Response res;
		if (GenDaoLoader.instance.getEventItemDao().getElement(id)!=null){
			GenDaoLoader.instance.getEventItemDao().updateElement(element, id);
			res = Response.accepted().build();
		}else{
			int insertedID = GenDaoLoader.instance.getEventItemDao().addElement(element);
			res= ResourceHelper.returnOkOrErrorResponse(insertedID==-1?false:true);
		}
		return res;	
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response newEvent(EventItem element) throws IOException {
		int insertedID = GenDaoLoader.instance.getEventItemDao().addElement(element);
		return ResourceHelper.returnOkOrErrorResponse(insertedID==-1?false:true);
	}


	
	// FORMS
//	@POST
//	@Produces(MediaType.TEXT_HTML)
//	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//	public Response newEvent(@FormParam("name") String name,
//			@FormParam("description") String description, 
//			@FormParam("start") String start,
//			@FormParam("end") String end,
//			@Context HttpServletResponse servletResponse) throws IOException {
//		Event addEvent = new Event(name,description,start,end);
//		boolean ok = GenDaoLoader.instance.getEventItemDao().addElement(addEvent);
//		return ResourceHelper.returnOkOrErrorResponse(ok);
//	}


}

