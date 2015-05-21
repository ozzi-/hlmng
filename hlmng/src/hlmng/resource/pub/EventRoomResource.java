package hlmng.resource.pub;


import hlmng.dao.GenDao;
import hlmng.dao.GenDaoLoader;
import hlmng.model.EventRoom;
import hlmng.resource.Resource;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import settings.HLMNGSettings;



@Path(HLMNGSettings.pubURL+"/eventroom")
public class EventRoomResource extends Resource  {

	private GenDao eventRoomDao =GenDaoLoader.instance.getEventRoomDao();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object> getEventRoom() throws IOException {
		return listResource(eventRoomDao, false);
	}

	@GET
	@Path("/lastupdate")
	@Produces(MediaType.TEXT_PLAIN)
	public long getLastUpdateTime() throws IOException {
		return eventRoomDao.getLastUpdateTime();
	}
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public EventRoom getEventRoom(@PathParam("id") int id) throws IOException{
		return (EventRoom) getResource(eventRoomDao, id);
	}

}

