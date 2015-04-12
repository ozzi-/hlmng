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
	public List<Object> getPresentation() throws IOException {
		return listResource(presentationDao, false);
	}

	
	@GET
	@Path("/lastupdate")
	@Produces(MediaType.TEXT_PLAIN)
	public long getLastUpdateTime() throws IOException {
		return presentationDao.getLastUpdateTime();
	}

	@GET
	@Path("{id}")
	public Presentation getPresentation(@PathParam("id") int id) throws IOException{
		return (Presentation) getResource(presentationDao, id);	
	}

	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putPresentation(Presentation element,@PathParam("id") int id) throws IOException {
		return putResource(presentationDao, element, id);
	}
	

	@DELETE
	@Path("{id}")
	public Response deletePresentation(@PathParam("id") int id) throws IOException {
		return deleteResource(presentationDao, id);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postPresentation(Presentation element) throws IOException {
		return postResource(presentationDao, element, true);
	}

}

