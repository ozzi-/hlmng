package hlmng.resource.adm;

import hlmng.dao.GenDao;
import hlmng.dao.GenDaoLoader;
import hlmng.model.PresentationPause;
import hlmng.resource.Resource;
import hlmng.resource.TimeHelper;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.parser.ParseException;

import settings.HLMNGSettings;

@Path(HLMNGSettings.admURL+"/presentationpause")
public class PresentationpauseResource extends Resource {

	private GenDao presentationpauseDao = GenDaoLoader.instance.getPresentationpauseDao();
	
	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response putPresentationpause(PresentationPause element,@PathParam("id") int id) throws IOException {
		element.setStop(TimeHelper.getCurrentTime());
		return putResource(presentationpauseDao, element, id);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Object postPresentationPause(PresentationPause element) throws IOException, ParseException {
		element.setStart(TimeHelper.getCurrentTime());
		return postResource(presentationpauseDao, element);
	}

}
