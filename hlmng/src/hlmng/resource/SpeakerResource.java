package hlmng.resource;

import hlmng.dao.GenDao;
import hlmng.dao.GenDaoLoader;
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
	public List<Object> getSpeaker() {
		return GenDaoLoader.instance.getSpeakerDao().listElements();
	}

	
	@GET
	@Path("{id}")
	public Speaker getSpeaker(@PathParam("id") String id) throws IOException{
		return (Speaker) getResource(speakerDao, id);
	}
	
	// TODO return this anyways as media
	@GET
	@Path("{id}/media")
	public Response getSpeakerMedia(@PathParam("id") String id) throws IOException{
		Speaker speaker =  (Speaker)GenDaoLoader.instance.getSpeakerDao().getElement(id);
		return MediaResource.getMediaStatic(Integer.toString(speaker.getMediaIDFK()), uri,request);
	}

	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putSpeaker(Speaker element,@PathParam("id") String id) {
		return putResource(speakerDao, element, id);
	}
	
	@DELETE
	@Path("{id}")
	public Response deleteSpeaker(@PathParam("id") String id) {
		return deleteResource(speakerDao, id);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response newSpeaker(Speaker element) throws IOException {
		return newResource(speakerDao, element, true);
	}
}

