package hlmng.resource;

import hlmng.dao.GenDao;
import hlmng.dao.GenDaoLoader;
import hlmng.model.Social;

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



@Path("/social")
public class SocialResource extends Resource  {
	private GenDao socialDao = GenDaoLoader.instance.getSocialDao();

		
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object> getSocial() {
		return GenDaoLoader.instance.getSocialDao().listElements();
	}

	
	@GET
	@Path("{id}")
	public Social getSocial(@PathParam("id") int id) throws IOException{
		return (Social) getResource(socialDao, id);
	}

	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putSocial(Social element,@PathParam("id") int id) throws IOException {
		return putResource(socialDao, element, id);
	}
	

	@DELETE
	@Path("{id}")
	public Response deleteSocial(@PathParam("id") int id) throws IOException {
		return deleteResource(socialDao, id);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postSocial(Social element) throws IOException {
		return postResource(socialDao, element, false);
	}

}

