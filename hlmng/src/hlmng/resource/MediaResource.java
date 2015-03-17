package hlmng.resource;

import hlmng.dao.GenDaoLoader;
import hlmng.model.Media;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.StatusType;
import javax.ws.rs.core.UriInfo;

import com.sun.org.apache.xerces.internal.util.Status;

@Path("/media")
public class MediaResource  {
	
    @Context
    private UriInfo uriInfo;
	@Context
	private Request request;
	@Context
	private String id;
	@Context 
	private HttpServletResponse response;
	@Context
	UriInfo uri;
	private static String fileRootDir="media/";

	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object> listMedia() {
		List<Object> listMedia=  GenDaoLoader.instance.getMediaDao().listElements();
		setURLPath(listMedia);
		return listMedia;
	}
	
	@GET
	@Path("jpg/{name}")
	@Produces("image/jpeg")
	public Response getJPG(@PathParam("name") String fileName) throws IOException {
		ResponseBuilder response;
			File file = new File(fileRootDir+fileName);
			if(file.canRead()) {
				response = Response.ok((Object) file);
				response.header("Content-Disposition","attachment; filename=hlmng.jpg");				
			}else{
			    response = Response.status(Response.Status.NOT_FOUND);
			}
		return response.build();
	}
 

	private void setURLPath(List<Object> listMedia) {
		for (Object obj : listMedia) 	{
			Media media = (Media) obj; 
			media.setLink(uri.getBaseUri().toString()+"media/"+media.getType()+"/"+media.getLink());
		}	
	}
}