package hlmng.resource;

import hlmng.dao.GenDao;
import hlmng.dao.GenDaoLoader;
import hlmng.model.Push;

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



@Path("/push")
public class PushResource  extends Resource{
	private GenDao pushDao = GenDaoLoader.instance.getPushDao();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object> getPush() {
		return GenDaoLoader.instance.getPushDao().listElements();
	}
	
	@GET
	@Path("{id}")
	public Push getPush(@PathParam("id") String id) throws IOException{
		return (Push)getResource(pushDao, id);
	}

	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putPush(Push element,@PathParam("id") String id) {
		return putResource(pushDao, element, id);
	}
	
	@DELETE
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deletePush(@PathParam("id") String id) {
		return deleteResource(pushDao, id);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response newPush(Push element) throws IOException {
		return newResource(pushDao, element, true);
	}

}

