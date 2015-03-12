package hlmng.resource;

import hlmng.auth.AuthChecker;
import hlmng.dao.UserDao;
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
public class UserResource  {
	
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
		return UserDao.instance.listElements();
	}

	
	@GET
	@Path("count")
	@Produces(MediaType.TEXT_PLAIN)
	public int getCount() {
		return UserDao.instance.listElements().size();
	}

	
	@GET
	@Path("{id}")
	public User getUser(@PathParam("id") String id,
			@Context HttpHeaders headers,
			@Context HttpServletResponse servletResponse) throws IOException{
		AuthChecker.check(headers, servletResponse);
		Object obj=UserDao.instance.getElement(id);
		if(obj==null){
		    response.sendError(Response.Status.NOT_FOUND.getStatusCode());
		}
		return (User)obj;
	}
	
	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putUser(User element,@PathParam("id") String id) {
		Response res;
		if (UserDao.instance.getElement(id)!=null) {
			UserDao.instance.updateElement(element,id);
			res = Response.accepted().build();
		}else{
			UserDao.instance.addElement(element);
			res = Response.created(uriInfo.getAbsolutePath()).build();	
		}
		return res;	
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response newUser(User element) throws IOException {
		UserDao.instance.addElement(element);
		return Response.ok().build();
	}
	
	// FORMS
	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response newUser(@FormParam("name") String name,
			@FormParam("deviceID") String deviceID, @FormParam("regID") String regID,
			@Context HttpServletResponse servletResponse) throws IOException {
		User addUser = new User(name, deviceID,regID);
		UserDao.instance.addElement(addUser);
		return Response.ok().build();
	}


}
