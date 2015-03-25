package hlmng.resource;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

public class Resource {
	@Context
	private UriInfo uriInfo;
	@Context
	private Request request;
	@Context
	private String id;
	@Context
	protected HttpServletResponse response;
	@Context
	UriInfo uri;
}
