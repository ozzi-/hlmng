package hlmng.resource;

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

import log.Log;

public class ResourceHelper {


	static int cacheTime=1800;
	/**
	 * @param ok
	 * @return If @param is true returns a HTTP 200 (OK) Response, else 400 (bad request)
	 */
	static Response returnOkOrBadReqResponse(boolean ok) {
		if(ok){
			return Response.ok().build();			
		}else{
			return Response.status(400).build();
		}
	}
	
	static Response returnOkOrNotFoundResponse(boolean ok) {
		if(ok){
			return Response.ok().build();			
		}else{
			return Response.status(404).build();
		}
	}
	
	static boolean sendErrorIfNull(Object obj,HttpServletResponse response) throws IOException {
		if(obj==null){
		    response.sendError(404);
		    return true;
		}
		return false;
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
                Log.addEntry(Level.INFO,"Cache control needs to create new entry"); 
                builder.tag(etag);
        }else{
            Log.addEntry(Level.INFO,"Cache control can use cached entry"); 
        }
        builder.cacheControl(cc);
		return builder;
	}
	

	
	static void setMediaURLPath(UriInfo uri, Media media) {
		media.setLink(uri.getBaseUri().toString() + "media/"
				+ media.getType() + "/" + media.getLink());
	}
}
