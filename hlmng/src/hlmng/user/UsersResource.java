package hlmng.user;

import hlmng.auth.AuthChecker;
import hlmng.dao.UserDao;
import hlmng.model.User;
import hlmng.resource.Resource;
import hlmng.resource.Resources;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/user")
public class UsersResource implements Resources {

	// Allows to insert contextual objects into the class,
	// e.g. ServletContext, Request, Response, UriInfo
	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	@Override
	public List<Object> getElements() {
		return UserDao.instance.listElements();
	}

	@Override
	public int getCount() {
		return UserDao.instance.listElements().size();
	}

	@Override
	public Resource getElement(String id, HttpHeaders headers,
			HttpServletResponse servletResponse) {
		AuthChecker.check(headers, servletResponse);
		return new Resource(uriInfo, request, id, UserDao.instance);
	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response newUser(@FormParam("name") String name,
			@FormParam("deviceID") String deviceID,
			@Context HttpServletResponse servletResponse) throws IOException {
		User addUser = new User(name, deviceID);
		UserDao.instance.addElement(addUser);
		return Response.ok().build();
	}


}
