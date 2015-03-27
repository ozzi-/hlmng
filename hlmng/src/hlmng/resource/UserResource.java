package hlmng.resource;

import hlmng.auth.AuthChecker;
import hlmng.dao.GenDao;
import hlmng.dao.GenDaoLoader;
import hlmng.dao.UserDao;
import hlmng.model.User;
import hlmng.model.UserActionLimiter;

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



@Path("/user")
public class  UserResource  extends Resource {
	private GenDao userDao = GenDaoLoader.instance.getUserDao();


	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object> getUsers() {
		AuthChecker.check(headers, servletResponse,true);
		return GenDaoLoader.instance.getUserDao().listElements();
	}
	
	@GET 
	@Path("{id}")
	public User getUser(@PathParam("id") String id) throws IOException{
		return (User) getResource(userDao, id);
	}
	
	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putUser(User element,@PathParam("id") String id) throws IOException {
		return putResource(userDao, element, id);			
	}
	
	@DELETE
	@Path("{id}")
	public Response deleteUser(@PathParam("id") String id) throws IOException {
		return deleteResource(userDao, id);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postUser(User element) throws IOException {	
		User userNameExists = ((UserDao) userDao).getUserByName(element.getName());
		if(userNameExists == null){ 
			if(!UserActionLimiter.actionsExceeded("userCreation")){ // we don't want people spamming the profile creation
				return postResourceDo(userDao, element);			
			}
			return Response.status(429).build();  // Too Many Requests			
		}else{
			return Response.status(422).build();  // Unprocessable Entity (exists)
		}
	}

}
