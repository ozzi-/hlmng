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

import org.owasp.esapi.ESAPI;

import settings.HLMNGSettings;
import settings.HTTPCodes;



@Path(HLMNGSettings.pubURL+"/social")
public class SocialResource extends Resource  {
	private GenDao socialDao = GenDaoLoader.instance.getSocialDao();
	private GenDao socialPublishDao = GenDaoLoader.instance.getSocialPublishDao();

	
	@GET
	@Path("/lastupdate")
	@Produces(MediaType.TEXT_PLAIN)
	public long getLastUpdateTime() throws IOException {
		return socialDao.getLastUpdateTime();
	}
	
	@GET
	@Path("{id}/publications")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object> getPublications(@PathParam("id") int id){
		return socialPublishDao.listByFK("socialIDFK", id);
	}
		
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object> getSocials() throws IOException {
		List<Object> socialObjects = listResource(socialDao, false);
		ResourceHelper.enrichSocialListWithUsernameAndMedia(uri,socialObjects);
		return socialObjects;
	}
	
	@GET
	@Path("{id}")
	public Social getSocial(@PathParam("id") int id) throws IOException{
		Social social = (Social) getResource(socialDao, id);
		ResourceHelper.enrichSocialWithUsernameAndMedia(uri,social);
		return social;
	}


	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Object postSocial(Social element) throws IOException {
		Social social = null;
		if(AuthChecker.checkAuthorization(headers, servletResponse)){
			Log.addEntry(Level.INFO, "CHEKED AUTH POST SOCIAL");
			if(!(ESAPI.encoder().encodeForHTML(element.getText()).equals(element.getText()))){
				Log. addEntry(Level.WARNING,"Somebody possibly tried to insert XSS while posting a social entry");
			}
			element.setText(ESAPI.encoder().encodeForHTML(element.getText())); 
		
			String authorizationHeader = headers.getHeaderString("Authorization");
			if(authorizationHeader==null){
				return Response.status(HTTPCodes.unauthorized).build();
			} 	
			
			AuthCredential authCredential = AuthChecker.decodeBasicAuthB64(authorizationHeader);
			User user = GenDaoLoader.instance.getUserDao().getUserByNameAndDeviceID(authCredential.getUsername(), authCredential.getSecret());
			if( user.getUserID()!=element.getUserIDFK()){
				Log.addEntry(Level.WARNING, "User tried to be author as somebody else . UserID: "+user.getUserID()+" as "
											+element.getUserIDFK()
											+". "+ModelHelper.valuestoString(element));
				return Response.status(HTTPCodes.forbidden).build();
			}
			
			boolean isAuthor=false;
			String qrHeader = headers.getHeaderString("X-QRCode"); 
		
			if(qrHeader!=null){							
				AuthResult qrAuthRes = AuthChecker.checkQRCodeAuthorization(qrHeader,element.getEventIDFK(),user,"jury",TimeHelper.getCurrentDateTime());
				if(qrAuthRes.isAuthorized()){
					isAuthor=true;
				}else{
					return Response.status(qrAuthRes.getResponseCode()).build();
				}
			}
			
			setStatus(element, isAuthor);
				
			social = (Social) postResource(socialDao, element);
			if(social!=null){
				ResourceHelper.enrichSocialWithUsernameAndMedia(uri,social);		
			}			
		}
		return social;
	}


	private void setStatus(Social element, boolean isAuthor) {
		if(isAuthor){
			element.setStatus(Social.statusEnum.accepted.toString());
		}else{				
			element.setStatus(Social.statusEnum.pending.toString());
		}
	}
}

