package hlmng.resource;

import gcm.GCM;
import hlmng.auth.AuthChecker;
import hlmng.dao.GenDao;
import hlmng.dao.GenDaoLoader;
import hlmng.model.Push;
import hlmng.model.User;
import hlmng.model.UserActionLimiter;

import java.io.IOException;
import java.util.ArrayList;
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

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;



@Path("/push")
public class PushResource  extends Resource{
	private GenDao pushDao = GenDaoLoader.instance.getPushDao();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object> getPush() {
		return GenDaoLoader.instance.getPushDao().listElements();
	}
	
	@GET
	@Path("{id}")
	public Push getPush(@PathParam("id") int id) throws IOException{
		return (Push)getResource(pushDao, id);
	}

	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putPush(Push element,@PathParam("id") int id) throws IOException {
		return putResource(pushDao, element, id);
	}
	
	@DELETE
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deletePush(@PathParam("id") int id) throws IOException {
		return deleteResource(pushDao, id);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postPush(Push element) throws IOException, ParseException {
		if(AuthChecker.check(headers, servletResponse, true) && !UserActionLimiter.actionsExceeded("pushResource")){
			List<Object> users = GenDaoLoader.instance.getUserDao().listElements();
			List<String> regIds = new ArrayList<String>();
			for (Object userobject : users) {
				regIds.add(((User) userobject).getRegID());
			}
			
			String gcmResponse = GCM.postGCM(element.getTitle(), element.getText(), regIds);
			
		    JSONParser parser = new JSONParser();
		    JSONObject response  = (JSONObject) parser.parse(gcmResponse);

			element.setReceivedCounter(Integer.parseInt(response.get("success").toString()));
			element.setFailedCounter(Integer.parseInt(response.get("failure").toString()));
			
			element.setDate(ResourceHelper.getCurrentDate());
			element.setTime(ResourceHelper.getCurrentTime());

			Response ret = postResource(pushDao, element, true);
			return ret;
		}else{
			return null;
		}
	}

}

