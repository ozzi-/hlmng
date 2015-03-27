package hlmng.resource;

import hlmng.auth.AuthChecker;
import hlmng.dao.GenDao;

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
	protected Response deleteResource(GenDao dao, String id) throws IOException{
		if(!isNumeric(id)){
		    response.sendError(400);
		}else if(AuthChecker.check(headers, servletResponse, true)){
			boolean deleted = dao.deleteElement(id);
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
	protected Response postResource(GenDao dao, Object element, boolean backend){
		if(AuthChecker.check(headers, servletResponse, backend)){
			postResourceDo(dao, element);
		} 
		return null;		
	}
	
	protected Response postResourceDo(GenDao dao, Object element){
			int insertedID = dao.addElement(element);
			return ResourceHelper.returnOkOrBadReqResponse(!(insertedID==-1));			
	}
	
	/**
	 * Gets a resource with the ID provided
	 * @param dao
	 * @param id
	 * @return The Object or 404
	 */
	protected Object getResource(GenDao dao, String id) throws IOException{
		if(!isNumeric(id)){
		    response.sendError(400);
		}else{
			Object obj =  dao.getElement(id);
			ResourceHelper.sendErrorIfNull(obj,response);
			return obj;			
		}
		return null;
	}
	
	private static boolean isNumeric(String str){
		if(str.length()>=10) // if the id is that long, don't even bother, this is wasting our cpu cycles 
			return false; 
	    for (char c : str.toCharArray()) {
	        if (!Character.isDigit(c)) return false;
	    }
	    return true;
	}
	
	/**
	 * Tries to put (update) a given element for the ID provided.
	 * Backend Login required.
	 * @param dao
	 * @param element
	 * @param id
	 * @return a response, either 202 (accepted) or 404 if the provided ID doesn't exist.
	 */
	protected Response putResource(GenDao dao ,Object element, String id) throws IOException{
		Response res;
		if(!isNumeric(id)){
		    response.sendError(400);
		}else if(AuthChecker. check(headers, servletResponse, true)){
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

