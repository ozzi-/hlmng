package hlmng.resource.pub;

import hlmng.auth.AuthChecker;
import hlmng.auth.AuthCredential;
import hlmng.dao.GenDao;
import hlmng.dao.GenDaoLoader;
import hlmng.dao.UserDao;
import hlmng.model.ModelHelper;
import hlmng.model.User;
import hlmng.model.UserActionLimiter;
import hlmng.resource.Resource;

import java.io.IOException;
import java.util.logging.Level;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.owasp.esapi.ESAPI;

import log.Log;
import settings.HLMNGSettings;
import settings.HTTPCodes;



@Path(HLMNGSettings.pubURL+"/user")
public class  UserResource  extends Resource {
	private static GenDao userDao = GenDaoLoader.instance.getUserDao();

	
	@GET
	@Path("/lastupdate")
	@Produces(MediaType.TEXT_PLAIN)
	public long getLastUpdateTime() throws IOException {
		return userDao.getLastUpdateTime();
	}
	
	
	public static String getUsername(int id){
		System.out.println();
		User user = (User) userDao.getElement(id);
		if(user!=null){
			return user.getName();
		}
		return "unknown";
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Object postUser(User element) throws IOException {	
		String escapedName  = ESAPI.encoder().encodeForHTML(element.getName());
		String escapedRegId  = ESAPI.encoder().encodeForHTML(element.getRegID());
		String escapedDevId  = ESAPI.encoder().encodeForHTML(element.getDeviceID());

		if(!(escapedName.equals(element.getName())) || !(escapedRegId.equals(element.getRegID())) || !(escapedDevId.equals(element.getDeviceID()))){
			return Response.status(HTTPCodes.badRequest).build();
		}
		User userNameExists = ((UserDao) userDao).getUserByName(element.getName());
		if(userNameExists == null){ 
			if(!UserActionLimiter.actionsExceeded("userCreation")){ // we don't want people spamming the profile creation
				return postResource(userDao, element);			
			}
			return Response.status(HTTPCodes.tooManyRequests).build();  			
		}else{
			return Response.status(HTTPCodes.unprocessableEntity).build();  // Unprocessable Entity (exists)
		}
	}
	
	@PUT
	@Path("{id}/changeregid")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putUserChangeRegID(User element,@PathParam("id") int id) throws IOException {
		Response res;
		
		String escapedRegId  = ESAPI.encoder().encodeForHTML(element.getRegID());

		if(!(escapedRegId.equals(element.getRegID()))){
			return Response.status(HTTPCodes.badRequest).build();
		}
		
		String authorizationHeader = headers.getHeaderString("Authorization");
		if(authorizationHeader==null){
			return Response.status(HTTPCodes.unauthorized).build();
		}
		if(element==null || element.getName()==null){
			return Response.status(HTTPCodes.badRequest).build();
		} 
				
		AuthCredential authCredential = AuthChecker.decodeBasicAuthB64(authorizationHeader);
		
		// only allow the user to change his own object
		if( authCredential.getUsername().equals(element.getName()) &&  (AuthChecker.checkAuthorization(headers, servletResponse))){ 
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

}
