package hlmng.resource;


import hlmng.dao.GenDaoLoader;
import hlmng.model.EventRoom;

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



@Path("/eventroom")
public class EventRoomResource  {
	
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
	public List<Object> getEventRoom() {
		return GenDaoLoader.instance.getEventRoomDao().listElements();
	}

	
	@GET
	@Path("count")
	@Produces(MediaType.TEXT_PLAIN)
	public int getCount() {
		return GenDaoLoader.instance.getEventRoomDao().listElements().size();
	}
	
	@GET
	@Path("{id}")
	public EventRoom getEventRoom(@PathParam("id") String id,
			@Context HttpHeaders headers,
			@Context HttpServletResponse servletResponse) throws IOException{
		Object obj =  GenDaoLoader.instance.getEventRoomDao().getElement(id);
		ResourceHelper.sendErrorIfNull(obj,response);
		EventRoom evt=(EventRoom) obj;
		return evt;	
	}

	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putEventRoom(EventRoom element,@PathParam("id") String id) {
		Response res;
		if (GenDaoLoader.instance.getEventRoomDao().getElement(id)!=null){
			GenDaoLoader.instance.getEventRoomDao().updateElement(element, id);
			res = Response.accepted().build();
		}else{
			int insertedID = GenDaoLoader.instance.getEventRoomDao().addElement(element);
			res= ResourceHelper.returnOkOrErrorResponse(!(insertedID==-1));
		}
		return res;	
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response newEventRoom(EventRoom element) throws IOException {
		int insertedID = GenDaoLoader.instance.getEventRoomDao().addElement(element);
		return ResourceHelper.returnOkOrErrorResponse(!(insertedID==-1));
	}

}

