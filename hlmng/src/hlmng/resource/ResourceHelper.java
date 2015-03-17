package hlmng.resource;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;

public class ResourceHelper {
	/**
	 * @param ok
	 * @return If @param is true retruns a HTTP 200 (OK) Response, else a 500 (server error)
	 */
	static Response returnOkOrErrorResponse(boolean ok) {
		if(ok){
			return Response.ok().build();			
		}else{
			return Response.serverError().build();
		}
	}
	
	static void sendErrorIfNull(Object obj,HttpServletResponse response) throws IOException {
		if(obj==null){
		    response.sendError(Response.Status.NOT_FOUND.getStatusCode());
		}
	}
	 
}
