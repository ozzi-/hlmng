package hlmng.resource;

import hlmng.auth.AuthChecker;
import hlmng.auth.AuthCredential;
import hlmng.dao.GenDao;
import hlmng.dao.GenDaoLoader;
import hlmng.dao.UserDao;
import hlmng.model.ModelHelper;
import hlmng.model.User;
import hlmng.model.UserActionLimiter;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

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

import log.Log;



@Path("/user")
public class  UserResource  extends Resource {
	private GenDao userDao = GenDaoLoader.instance.getUserDao();


	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object> getUsers() throws IOException {
		if(AuthChecker.check(headers, servletResponse,true)){ 
			return listResource(userDao, false);
		}
		return null;
	}
	
	@GET 
	@Path("{id}")
	public User getUser(@PathParam("id") int id) throws IOException{
		return (User) getResource(userDao, id);
	}
	
	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putUser(User element,@PathParam("id") int id) throws IOException {
		return putResource(userDao, element, id);
	}
	
	@PUT
	@Path("{id}/changeregid")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putUserChangeRegID(User element,@PathParam("id") int id) throws IOException {
		Response res;
		String authorizationHeader = headers.getHeaderString("Authorization");
		if(authorizationHeader==null){
			return Response.status(401).build();
		}
		if(element==null || element.getName()==null){
			return Response.status(400).build();
		} 
				
		AuthCredential authCredential = AuthChecker.decodeBasicAuthB64(authorizationHeader);
		
		// only allow the user to change his own object
		if( authCredential.getUsername().equals(element.getName()) &&  (AuthChecker. check(headers, servletResponse, false))){
			User dbUser = (User)userDao.getElement(id);
			if (dbUser!=null){
				setDefaultUserFieldValues(element, id, dbUser);
				res = Response.accepted().build();
				Log.addEntry(Level.INFO, "User ("+authCredential.getUsername()+") successfully changed his registration id");
			}else{
				Log.addEntry(Level.WARNING, "User ("+authCredential.getUsername()+") tried to edit a non existant user. "+ModelHelper.valuestoString(element));
				res = Response.status(404).build();
			}
		}else{
			Log.addEntry(Level.WARNING, "User ("+authCredential.getUsername()+") tried to PUT user with wrong credentials or wrong name. "+ModelHelper.valuestoString(element));
			res = Response.status(401).build();
		}
		return res;		
	}

	private void setDefaultUserFieldValues(User element, int id, User dbUser) {
		// only allow changes to regID since this is the user himself wanting to change stuff, don't trust him
		element.setName(dbUser.getName());
		element.setDeviceID(dbUser.getDeviceID());
		element.setUserID(dbUser.getUserID());
		userDao.updateElement(element, id);
	}
	
	@DELETE
	@Path("{id}")
	public Response deleteUser(@PathParam("id") int id) throws IOException {
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
