package hlmng.resource;

import hlmng.auth.AuthChecker;
import hlmng.dao.GenDao;
import hlmng.dao.GenDaoLoader;

import java.io.IOException;

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
	UriInfo uri;
	@Context
	HttpHeaders headers;
	@Context 
	HttpServletResponse servletResponse;
	
	/**
	 * Tries to delete a object with the provided ID.
	 * Backend Login required
	 * @param dao
	 * @param id
	 * @return 202 or 404 if not found
	 */
	protected Response deleteResource(GenDao dao, String id){
		if(AuthChecker.check(headers, servletResponse, true)){
			boolean deleted = GenDaoLoader.instance.getEventDao().deleteElement(id);
			return ResourceHelper.returnOkOrNotFoundResponse(deleted);			
		} 
		return null;
	}
	
	/**
	 * Create a new resource. If its a social entry set backend to false. 
	 * @param dao
	 * @param element
	 * @param backend
	 * @return 202, 401
	 */
	protected Response newResource(GenDao dao, Object element, boolean backend){
		if(AuthChecker.check(headers, servletResponse, backend)){
			newResourceDo(dao, element);
		} 
		return null;		
	}
	
	protected Response newResourceDo(GenDao dao, Object element){
			int insertedID = GenDaoLoader.instance.getEventDao().addElement(element);
			return ResourceHelper.returnOkOrErrorResponse(!(insertedID==-1));			
	}
	
	/**
	 * Gets a resource with the ID provided
	 * @param dao
	 * @param id
	 * @return The Object or 404
	 * @throws IOException
	 */
	protected Object getResource(GenDao dao, String id) throws IOException{
		Object obj =  GenDaoLoader.instance.getEventDao().getElement(id);
		ResourceHelper.sendErrorIfNull(obj,response);
		return obj;
	}
	
	/**
	 * Tries to put (update) a given element for the ID provided.
	 * Backend Login required.
	 * @param dao
	 * @param element
	 * @param id
	 * @return a response, either 202 (accepted) or 404 if the provided ID doesn't exist.
	 */
	protected Response putResource(GenDao dao ,Object element, String id){
		Response res;
		if(AuthChecker. check(headers, servletResponse, true)){
			if (dao.getElement(id)!=null){
				dao.updateElement(element, id);
				res = Response.accepted().build();
			}else{
				res = Response.status(404).build();
			}
			return res;				
		}
		return null;
	}
}
