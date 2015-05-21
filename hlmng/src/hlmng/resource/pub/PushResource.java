package hlmng.resource.pub;

import hlmng.dao.GenDao;
import hlmng.dao.GenDaoLoader;
import hlmng.model.Push;
import hlmng.resource.Resource;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import settings.HLMNGSettings;



@Path(HLMNGSettings.pubURL+"/pub/push")
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
}