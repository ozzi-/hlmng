package hlmng.resource;

import hlmng.dao.GenDao;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import settings.HTTPCodes;

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
		int insertedID = dao.addElement(element); 
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
			res = Response.status(HTTPCodes.notFound).build();
		}
		return res;				
	}
}

