package hlmng.resource;

import hlmng.auth.AuthChecker;
import hlmng.dao.GenDaoLoader;
import hlmng.model.User;

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



@Path("/user")
public class  UserResource  {
	
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
	public List<Object> getUsers() {
		return GenDaoLoader.instance.getUserDao().listElements();
	}

	
	@GET
	@Path("count")
	@Produces(MediaType.TEXT_PLAIN)
	public int getCount() {
		return GenDaoLoader.instance.getUserDao().listElements().size();
	}

	
	@GET
	@Path("{id}")
	public User getUser(@PathParam("id") String id,
			@Context HttpHeaders headers,
			@Context HttpServletResponse servletResponse) throws IOException{
		AuthChecker.check(headers, servletResponse);
		Object obj=GenDaoLoader.instance.getUserDao().getElement(id);
		ResourceHelper.sendErrorIfNull(obj,response);
		return (User)obj;
	}
	
	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putUser(User element,@PathParam("id") String id) {
		Response res;
		if (GenDaoLoader.instance.getUserDao().getElement(id)!=null){
			GenDaoLoader.instance.getUserDao().updateElement(element, id);
			res = Response.accepted().build();
		}else{
			boolean ok = GenDaoLoader.instance.getUserDao().addElement(element);
			res =  ResourceHelper.returnOkOrErrorResponse(ok);	
		}
		return res;	
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response newUser(User element) throws IOException {
		boolean ok = GenDaoLoader.instance.getUserDao().addElement(element);
		return ResourceHelper.returnOkOrErrorResponse(ok);
	}
	
	// FORMS
	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response newUser(@FormParam("name") String name,
			@FormParam("deviceID") String deviceID, @FormParam("regID") String regID,
			@Context HttpServletResponse servletResponse) throws IOException {
		Object addUser = (Object)new User(name, deviceID,regID);
		boolean ok = GenDaoLoader.instance.getUserDao().addElement(addUser);
		return ResourceHelper.returnOkOrErrorResponse(ok);
	}


}
