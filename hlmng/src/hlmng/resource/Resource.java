package hlmng.resource;

import hlmng.dao.GenDao;
import hlmng.model.News;
import hlmng.model.Social;
import hlmng.model.Speaker;
import hlmng.resource.adm.MediaResource;
import hlmng.resource.adm.UserResource;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

public class Resource {
	
	/**
	 * Inject context variables
	 */
	@Context
	private UriInfo uriInfo;
	@Context
	protected Request request;
	@Context
	protected HttpServletResponse response;
	@Context
	protected UriInfo uri;
	@Context
	protected HttpHeaders headers;
	@Context
	protected HttpServletResponse servletResponse;
	
	/**
	 * Tries to delete a object with the provided ID.
	 * Backend Login required
	 * @param dao
	 * @param id
	 * @return 202 or 404 if not found
	 */
	protected Response deleteResource(GenDao dao, int id) throws IOException{
			boolean deleted = dao.deleteElement(id);
			return ResourceHelper.returnOkOrNotFoundResponse(deleted);			
	}
	
	/**
	 * List all resources.
	 * @param dao
	 * @param limit Limits the output to the newest entries.
	 * @return
	 * @throws IOException
	 */
	protected List<Object> listResource(GenDao dao,boolean limit) throws IOException{
		return dao.listElements(limit);	
	}
	
	/**
	 * Create a new resource. 
	 * @param dao
	 * @param element
	 * @param backend
	 * @return 202, 401
	 */
	protected Object postResource(GenDao dao, Object element){
		int insertedID = dao.addElement(element); // todo auth check for users. . . . 
		return dao.getElement(insertedID);			
	}

	
	/**
	 * Gets a resource with the ID provided
	 * @param dao
	 * @param id
	 * @return The Object or 404
	 */
	protected Object getResource(GenDao dao, int id) throws IOException{
		Object obj =  dao.getElement(id);
		if(ResourceHelper.sendErrorIfNull(obj,response)==false){
			return obj;			
		}
		return null;
	}
	

	/**
	 * Tries to put (update) a given element for the ID provided.
	 * Backend Login required.
	 * @param dao
	 * @param element
	 * @param id
	 * @return a response, either 202 (accepted) or 404 if the provided ID doesn't exist.
	 */
	protected Response putResource(GenDao dao ,Object element, int id) throws IOException{
		Response res;
		if (dao.getElement(id)!=null){
			dao.updateElement(element, id);
			res = Response.accepted().build();
		}else{
			res = Response.status(404).build();
		}
		return res;				
	}
	
	protected void enrichSpeakerWithMedia(List<Object> speakerObjects) {
		for (Object object : speakerObjects) {
			Speaker speaker = (Speaker) object;
			String media = MediaResource.getMediaURL(uri, speaker.getMediaIDFK());
			speaker.setMedia(media);
		}
	}
	protected void enrichSocialWithUsernameAndMedia(List<Object> socialObjects) {
		for (Object object : socialObjects) {
			Social social = (Social) object;
			String authorName = UserResource.getUsername(social.getUserIDFK());
			social.setAuthorName(authorName);
			String media = MediaResource.getMediaURL(uri, social.getMediaIDFK());
			social.setMedia(media);

		}
	}	
	protected void enrichNewsWithMedia(List<Object> newsObjects) {
		for (Object object : newsObjects) {
			News news = (News) object;
			String media = MediaResource.getMediaURL(uri, news.getMediaIDFK());
			news.setMedia(media);
		}
	}
}

