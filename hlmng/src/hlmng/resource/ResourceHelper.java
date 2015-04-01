package hlmng.resource;

import hlmng.model.Media;
import hlmng.model.ModelHelper;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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

	private static final DateFormat formatterDate = new SimpleDateFormat("yyyy-MM-dd");
	private static final DateFormat formatterDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final DateFormat formatterTime = new SimpleDateFormat("HH:mm:ss");
	
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
                Log.addEntry(Level.FINE,"Cache control needs to create new entry"); 
                builder.tag(etag);
        }else{
            Log.addEntry(Level.FINE,"Cache control can use cached entry"); 
        }
        builder.cacheControl(cc);
		return builder;
	}
	
	/**
	 * @return yyyy-MM-dd
	 */
	public static String getCurrentDate(){
		Date dateTime = new Date(System.currentTimeMillis()); 
		return formatterDate.format(dateTime);
	}
	/**
	 * @return yyyy-MM-dd HH-mm-ss
	 */
	public static String getCurrentDateTime(){
		Date dateTime = new Date(System.currentTimeMillis()); 
		return formatterDateTime.format(dateTime);
	}
	/**
	 * @return HH-mm-ss
	 */
	public static String getCurrentTime(){
		Date dateTime = new Date(System.currentTimeMillis()); 
		return formatterTime.format(dateTime);
	}
	
	static void setMediaURLPath(UriInfo uri, Media media) {
		media.setLink(uri.getBaseUri().toString() + "media/"
				+ media.getType() + "/" + media.getLink());
	}
}
