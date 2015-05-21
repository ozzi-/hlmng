package hlmng.resource.pub;


import hlmng.dao.GenDao;
import hlmng.dao.GenDaoLoader;
import hlmng.model.EventItem;
import hlmng.resource.Resource;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import settings.HLMNGSettings;



@Path(HLMNGSettings.pubURL+"/eventitem")
public class EventItemResource extends Resource{
	
	private GenDao eventItemDao = GenDaoLoader.instance.getEventItemDao();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object> getEventItems() throws IOException {
		return listResource(eventItemDao, false);
	}
	
	@GET
	@Path("/lastupdate")
	@Produces(MediaType.TEXT_PLAIN)
	public long getLastUpdateTime() throws IOException {
		return eventItemDao.getLastUpdateTime();
	}
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public EventItem getEventItem(@PathParam("id") int id) throws IOException{
		return (EventItem) getResource(eventItemDao, id);	
	}

}

