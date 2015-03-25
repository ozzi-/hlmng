package hlmng.resource;

import hlmng.Log;
import hlmng.model.Media;
import hlmng.model.ModelHelper;

import java.io.IOException;
import java.util.logging.Level;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

public class ResourceHelper {

	
	static int cacheTime=1800;
	/**
	 * @param ok
	 * @return If @param is true returns a HTTP 200 (OK) Response, else a 500 (server error)
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
	
	/**
	 * Either returns a 200 with the desired object if not in cache, else 304 not modified
	 * @param object
	 * @param request
	 * @return
	 */
	static ResponseBuilder cacheControl(Object object,Request request) {
		CacheControl cc = new CacheControl();
        cc.setMaxAge(cacheTime);
        cc.setPrivate(true);
        EntityTag etag = new EntityTag(Integer.toString(ModelHelper.HashCode(object)));
        ResponseBuilder builder = request.evaluatePreconditions(etag);
        // cached resource did change -> serve updated content
        if(builder == null){
                builder = Response.ok(object);
                Log.addEntry(Level.FINE,"Resource changed"); 
                builder.tag(etag);
        }else{
            Log.addEntry(Level.FINE,"Resource cache"); 
        }
        builder.cacheControl(cc);
		return builder;
	}
	 
	
	static void setMediaURLPath(UriInfo uri, Media media) {
		media.setLink(uri.getBaseUri().toString() + "media/"
				+ media.getType() + "/" + media.getLink());
	}
}
