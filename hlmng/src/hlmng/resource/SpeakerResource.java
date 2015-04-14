package hlmng.resource;

import hlmng.dao.GenDao;
import hlmng.model.Speaker;

import java.io.IOException;
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



@Path("/speaker")
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

	private void enrichSpeakerWithMedia(List<Object> speakerObjects) {
		for (Object object : speakerObjects) {
			Speaker speaker = (Speaker) object;
			String media = MediaResource.getMediaURL(uri, speaker.getMediaIDFK());
			speaker.setMedia(media);
		}
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
	

	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putSpeaker(Speaker element,@PathParam("id") int id) throws IOException {
		return putResource(speakerDao, element, id);
	}
	
	@DELETE
	@Path("{id}")
	public Response deleteSpeaker(@PathParam("id") int id) throws IOException {
		return deleteResource(speakerDao, id);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Object postSpeaker(Speaker element) throws IOException {
		return postResource(speakerDao, element, true);
	}
}

