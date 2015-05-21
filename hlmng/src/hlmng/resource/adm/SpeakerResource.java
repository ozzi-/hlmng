package hlmng.resource.adm;

import hlmng.dao.GenDao;
import hlmng.model.Speaker;
import hlmng.resource.Resource;
import hlmng.resource.ResourceHelper;

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

import settings.HLMNGSettings;



@Path(HLMNGSettings.admURL+"/speaker")
public class SpeakerResource extends Resource  {
	private GenDao speakerDao = new GenDao(Speaker.class);

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object> getSpeakers() throws IOException {
		List<Object> speakerObjects = listResource(speakerDao, false);
		ResourceHelper.enrichSpeakerWithMedia(uri,speakerObjects);
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
	@Produces(MediaType.APPLICATION_JSON)
	public Speaker getSpeaker(@PathParam("id") int id) throws IOException{
		Speaker speaker = (Speaker) getResource(speakerDao, id);
		if(speaker!=null){
			String media = ResourceHelper.getMediaURL(uri,speaker.getMediaIDFK());
			speaker.setMedia(media);
		}
		return speaker;
	}
	

	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response putSpeaker(Speaker element,@PathParam("id") int id) throws IOException {
		element.setNationality(element.getNationality().toUpperCase());
		return putResource(speakerDao, element, id);
	}
	
	@DELETE
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteSpeaker(@PathParam("id") int id) throws IOException {
		return deleteResource(speakerDao, id);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Object postSpeaker(Speaker element) throws IOException {
		element.setNationality(element.getNationality().toUpperCase());
		Speaker speaker = (Speaker) postResource(speakerDao, element);
		if(speaker!=null){
			String media = ResourceHelper.getMediaURL(uri, speaker.getMediaIDFK());
			speaker.setMedia(media);
		}
		return speaker;
	}
	
	
}

