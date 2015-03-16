package hlmng.resource;

import hlmng.dao.GenDaoLoader;
import hlmng.model.Media;
import hlmng.model.Speaker;

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



@Path("/speaker")
public class SpeakerResource  {
	
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
	public List<Object> getSpeaker() {
		return GenDaoLoader.instance.getSpeakerDao().listElements();
	}

	
	@GET
	@Path("count")
	@Produces(MediaType.TEXT_PLAIN)
	public int getCount() {
		return GenDaoLoader.instance.getSpeakerDao().listElements().size();
	}
	
	@GET
	@Path("{id}")
	public Speaker getSpeaker(@PathParam("id") String id,
			@Context HttpHeaders headers,
			@Context HttpServletResponse servletResponse) throws IOException{
		Object obj =  GenDaoLoader.instance.getSpeakerDao().getElement(id);
		ResourceHelper.sendErrorIfNull(obj,response);
		Speaker evt=(Speaker) obj;
		return evt;
	}
	
	@GET
	@Path("{id}/media")
	public List<Object> getSpeakerMedia(@PathParam("id") String id,
			@Context HttpHeaders headers,
			@Context HttpServletResponse servletResponse) throws IOException{
		return  GenDaoLoader.instance.getMediaDao().getElements(id);
	}

	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putSpeaker(Speaker element,@PathParam("id") String id) {
		Response res;
		if (GenDaoLoader.instance.getSpeakerDao().getElement(id)!=null){
			GenDaoLoader.instance.getSpeakerDao().updateElement(element, id);
			res = Response.accepted().build();
		}else{
			boolean ok = GenDaoLoader.instance.getSpeakerDao().addElement(element);
			res= ResourceHelper.returnOkOrErrorResponse(ok);
		}
		return res;	
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response newSpeaker(Speaker element) throws IOException {
		boolean ok = GenDaoLoader.instance.getSpeakerDao().addElement(element);
		return ResourceHelper.returnOkOrErrorResponse(ok);
	}
}

