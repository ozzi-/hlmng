package hlmng.resource.pub;

import hlmng.dao.GenDao;
import hlmng.model.Speaker;
import hlmng.resource.Resource;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import settings.HLMNGSettings;



@Path(HLMNGSettings.pubURL+"/speaker")
public class SpeakerResource extends Resource  {
	private GenDao speakerDao = new GenDao(Speaker.class);

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object> getSpeakers() throws IOException {
		List<Object> speakerObjects = listResource(speakerDao, false);
		enrichSpeakerWithMedia(speakerObjects);
		return speakerObjects;
	}

	
	@GET
	@Path("/lastupdate")
	@Produces(MediaType.TEXT_PLAIN)
	public long getLastUpdateTime() throws IOException {
		return speakerDao.getLastUpdateTime();
	}

	
	@GET
	@Path("{id}")
	public Speaker getSpeaker(@PathParam("id") int id) throws IOException{
		Speaker speaker = (Speaker) getResource(speakerDao, id);
		if(speaker!=null){
			String media = MediaResource.getMediaURL(uri,speaker.getMediaIDFK());
			speaker.setMedia(media);
		}
		return speaker;
	}
	
}

