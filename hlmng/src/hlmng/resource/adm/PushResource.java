package hlmng.resource.adm;

import gcm.GCM;
import hlmng.dao.GenDao;
import hlmng.dao.GenDaoLoader;
import hlmng.model.ModelHelper;
import hlmng.model.Push;
import hlmng.model.User;
import hlmng.model.UserActionLimiter;
import hlmng.resource.Resource;
import hlmng.resource.TimeHelper;

import java.io.IOException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

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

import settings.HLMNGSettings;



@Path(HLMNGSettings.admURL+"/push")
public class PushResource  extends Resource{
	private GenDao pushDao = GenDaoLoader.instance.getPushDao();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object> getPush() throws IOException {
		return listResource(pushDao, false);
	}
	
	
	@GET
	@Path("/lastupdate")
	@Produces(MediaType.TEXT_PLAIN)
	public long getLastUpdateTime() throws IOException {
		return pushDao.getLastUpdateTime();
	}
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
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
	@Produces(MediaType.APPLICATION_JSON)
	public Response deletePush(@PathParam("id") int id) throws IOException {
		return deleteResource(pushDao, id);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Object postPush(Push element) throws IOException, ParseException {
		if(!UserActionLimiter.actionsExceeded("pushResource")){
			List<Object> users = listResource(GenDaoLoader.instance.getUserDao(), false);
			doGCMSend(element, users);
			ModelHelper.valuestoString(element);
			return postResource(pushDao, element);
		}else{
			return null;
		}
	}


	protected static void doGCMSend(Push element, List<Object> users)
			throws ProtocolException, IOException, ParseException {
		
		if(!settings.HLMNGSettings.sendGCM){
			log.Log.addEntry(Level.INFO, "GCM push is disabled by config.properties");
			return;
		}
		
		List<String> regIds = new ArrayList<String>();
		for (Object userobject : users) {
			regIds.add(((User) userobject).getRegID());
		}
		
		String gcmResponse = GCM.sendGcm(element.getTitle(), element.getText(), regIds);			
		setPushMetaData(element, gcmResponse);
	}



	private static void setPushMetaData(Push element, String gcmResponse) throws ParseException {
		JSONParser parser = new JSONParser();
		JSONObject response  = (JSONObject) parser.parse(gcmResponse);

		element.setReceivedCounter(Integer.parseInt(response.get("success").toString()));
		element.setFailedCounter(Integer.parseInt(response.get("failure").toString()));
		
		element.setDate(TimeHelper.getCurrentDate());
		element.setTime(TimeHelper.getCurrentTime());
	}

}

