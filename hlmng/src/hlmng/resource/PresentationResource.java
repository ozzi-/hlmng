package hlmng.resource;

import hlmng.dao.GenDao;
import hlmng.dao.GenDaoLoader;
import hlmng.model.Presentation;

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



@Path("/presentation")
public class PresentationResource extends Resource {
	private GenDao presentationDao =GenDaoLoader.instance.getPresentationDao();

	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object> getPresentation() {
		return GenDaoLoader.instance.getPresentationDao().listElements();
	}

	
	@GET
	@Path("count")
	@Produces(MediaType.TEXT_PLAIN)
	public int getCount() {
		return GenDaoLoader.instance.getPresentationDao().listElements().size();
	}
	
	@GET
	@Path("{id}")
	public Presentation getPresentation(@PathParam("id") String id) throws IOException{
		return (Presentation) getResource(presentationDao, id);	
	}

	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putPresentation(Presentation element,@PathParam("id") String id) {
		return putResource(presentationDao, element, id);
	}
	

	@DELETE
	@Path("{id}")
	public Response deletePresentation(@PathParam("id") String id) {
		return deleteResource(presentationDao, id);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response newPresentation(Presentation element) throws IOException {
		return newResource(presentationDao, element, true);
	}

}

