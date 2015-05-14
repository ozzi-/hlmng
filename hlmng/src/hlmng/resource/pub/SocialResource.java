package hlmng.resource.pub;

import hlmng.auth.AuthChecker;
import hlmng.auth.AuthCredential;
import hlmng.auth.AuthResult;
import hlmng.dao.GenDao;
import hlmng.dao.GenDaoLoader;
import hlmng.model.ModelHelper;
import hlmng.model.Social;
import hlmng.model.User;
import hlmng.resource.Resource;
import hlmng.resource.ResourceHelper;
import hlmng.resource.TimeHelper;
import hlmng.resource.adm.UserResource;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import log.Log;
import settings.HLMNGSettings;
import settings.HTTPCodes;



@Path(HLMNGSettings.pubURL+"/social")
public class SocialResource extends Resource  {
	private GenDao socialDao = GenDaoLoader.instance.getSocialDao();

	@GET
	@Path("newest")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object> getNewestSocials() throws IOException {
		List<Object> socialObjects = listResource(socialDao, true);
		enrichSocialListWithUsernameAndMedia(socialObjects);
		return socialObjects;
	}
	
	
	@GET
	@Path("/lastupdate")
	@Produces(MediaType.TEXT_PLAIN)
	public long getLastUpdateTime() throws IOException {
		return socialDao.getLastUpdateTime();
	}
	
		
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object> getSocials() throws IOException {
		List<Object> socialObjects = listResource(socialDao, false);
		enrichSocialListWithUsernameAndMedia(socialObjects);
		return socialObjects;
	}
	
	@GET
	@Path("{id}")
	public Social getSocial(@PathParam("id") int id) throws IOException{
		Social social = (Social) getResource(socialDao, id);
		enrichSocialWithUsernameAndMedia(social);
		return social;
	}


	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Object postSocial(Social element) throws IOException {
		Social social = null;
		if(AuthChecker.checkAuthorization(headers, servletResponse)){
						
			boolean isAuthor=false;
			String qrHeader = headers.getHeaderString("X-QRCode"); 
			if(qrHeader!=null){
				String authorizationHeader = headers.getHeaderString("Authorization");
				if(authorizationHeader==null){
					return Response.status(401).build();
				} 	
				AuthCredential authCredential = AuthChecker.decodeBasicAuthB64(authorizationHeader);
				User user = GenDaoLoader.instance.getUserDao().getUserByNameAndDeviceID(authCredential.getUsername(), authCredential.getSecret());
				if(user==null){
					Log.addEntry(Level.WARNING, "User tried to be author with wrong credentials. "+ModelHelper.valuestoString(element));
					return Response.status(HTTPCodes.forbidden).build();
				}
				if( user.getUserID()!=element.getUserIDFK()){
					Log.addEntry(Level.WARNING, "User tried to be author as somebody else . UserID: "+user.getUserID()+" as "
												+element.getUserIDFK()
												+". "+ModelHelper.valuestoString(element));
					return Response.status(HTTPCodes.forbidden).build();
				}
				AuthResult qrAuthRes = AuthChecker.checkQRCodeAuthorization(qrHeader,element.getEventIDFK(),user,"jury",TimeHelper.getCurrentDateTime());
				if(qrAuthRes.isAuthorized()){
					isAuthor=true;
				}else{
					return Response.status(qrAuthRes.getResponseCode()).build();
				}
			} // TODO refactor this into authchecker , same for vote resource ..
			
			if(isAuthor){
				element.setStatus(Social.statusEnum.accepted.toString());
			}else{				
				element.setStatus(Social.statusEnum.pending.toString());
			}
				
			social = (Social) postResource(socialDao, element);
			if(social!=null){
				enrichSocialWithUsernameAndMedia(social);		
			}			
		}
		return social;
	}
	
	protected void enrichSocialListWithUsernameAndMedia(List<Object> socialObjects) {
		for (Object object : socialObjects) {
			Social social = (Social) object;
			enrichSocialWithUsernameAndMedia(social);
		}
	}


	private void enrichSocialWithUsernameAndMedia(Social social) {
		String authorName = UserResource.getUsername(social.getUserIDFK());
		social.setAuthorName(authorName);
		String media = ResourceHelper.getMediaURL(uri, social.getMediaIDFK());
		social.setMedia(media);
	}	

}

