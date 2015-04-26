package hlmng.resource.adm;

import hlmng.dao.GenDao;
import hlmng.dao.GenDaoLoader;
import hlmng.model.Social;
import hlmng.resource.Resource;

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

import settings.HLMNGSettings;



@Path(HLMNGSettings.admURL+"/social")
public class SocialResource extends Resource  {
	private GenDao socialDao = GenDaoLoader.instance.getSocialDao();

	@GET
	@Path("newest")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object> getNewestSocials() throws IOException {
		List<Object> socialObjects = listResource(socialDao, true);
		enrichSocialWithUsernameAndMedia(socialObjects);
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
		enrichSocialWithUsernameAndMedia(socialObjects);
		return socialObjects;
	}


	@GET
	@Path("{id}")
	public Social getSocial(@PathParam("id") int id) throws IOException{
		Social social = (Social) getResource(socialDao, id);
		String media = MediaResource.getMediaURL(uri, social.getMediaIDFK());
		social.setMedia(media);
		return social;
	}

	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putSocial(Social element,@PathParam("id") int id) throws IOException {
		return putResource(socialDao, element, id);
	}
	

	@DELETE
	@Path("{id}")
	public Response deleteSocial(@PathParam("id") int id) throws IOException {
		return deleteResource(socialDao, id);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Object postSocial(Social element) throws IOException {
		Social social = (Social) postResource(socialDao, element);
		if(social!=null){
			String authorName = UserResource.getUsername(social.getUserIDFK());
			social.setAuthorName(authorName);
			String media = MediaResource.getMediaURL(uri, social.getMediaIDFK());
			social.setMedia(media);			
		}
		return social;
	}

}

