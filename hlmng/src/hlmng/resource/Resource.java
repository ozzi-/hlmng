package hlmng.resource;

import hlmng.dao.Dao;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;


public class Resource {

	private UriInfo uriInfo;
	@SuppressWarnings("unused")
	private Request request;
	private String id;
	private Dao dao;

	public Resource(UriInfo uriInfo, Request request, String id, Dao dao) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.id = id;
		this.dao = dao;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Object getElement(@Context HttpServletResponse servletResponse) throws IOException {
		Object element = dao.getInstance().getElement(id);
		if (element == null){
			servletResponse.sendError(HttpServletResponse.SC_NOT_FOUND,"");		
		}
		return element;
	}
	
	@DELETE
	public Response deleteElement() {
		Response res;
		boolean c = dao.getInstance().deleteElement(id);
		if (!c){
			res = Response.notModified("Element not found").build();
		}else{
			res = Response.ok().build();
		}
		return res;
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putElement(Object element) {
		Response res;
		if (dao.getInstance().getElement(id)!=null) {
			dao.getInstance().updateElement(element);
			res = Response.accepted().build();
		}else{
			dao.getInstance().addElement(element);
			res = Response.created(uriInfo.getAbsolutePath()).build();	
		}
		return res;	
	}
	

}