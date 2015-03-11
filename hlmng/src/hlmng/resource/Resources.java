package hlmng.resource;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

public interface Resources{

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object> getElements();

	@GET
	@Path("count")
	@Produces(MediaType.TEXT_PLAIN)
	public int getCount();

	@Path("{resources}")
	public Resource getElement(@PathParam("resources") String id,
			@Context HttpHeaders headers,
			@Context HttpServletResponse servletResponse);

} 