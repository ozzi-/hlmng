package hlmng.resource.adm;

import hlmng.dao.GenDao;
import hlmng.dao.GenDaoLoader;
import hlmng.model.Media;
import hlmng.resource.Resource;
import hlmng.resource.ResourceHelper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import log.Log;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import settings.HLMNGSettings;



@Path(HLMNGSettings.admURL+"/media")
public class MediaResource extends Resource{

	private static GenDao mediaDao =GenDaoLoader.instance.getMediaDao();


	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object> listMedia() throws IOException {
		List<Object> listMedia = listResource(mediaDao, false);
		ResourceHelper.setURLPathList(listMedia,uri);
		return listMedia;
	}


	@GET
	@Path("{id}")
	public Response getMedia(@PathParam("id") int id) throws IOException {
		return ResourceHelper.getMediaAsResponse(id, uri, request);
	}
	
	@GET
	@Path("/lastupdate")
	@Produces(MediaType.TEXT_PLAIN)
	public long getLastUpdateTime() throws IOException {
		return mediaDao.getLastUpdateTime();
	}
	
	@DELETE
	@Path("{id}")
	public Response deleteMedia(@PathParam("id") int id) throws IOException{
		Media media = (Media)mediaDao.getElement(id);
		// ONLY deletes on filesystem -  since path's to that media still have to be there, see documentation
		boolean deleted=false;
		if(media !=null){ 
			deleted = deleteMediaFromFS(media.getLink(),media.getType());						
		}
		return ResourceHelper.returnOkOrNotFoundResponse(deleted);
	}

	private boolean deleteMediaFromFS(String fileName, String type) {
		String mediaPath= HLMNGSettings.mediaFileRootDir+fileName;
		Log.addEntry(Level.INFO, "Trying to delete:"+fileName);			
		File file = new File(mediaPath);
		boolean filedelres = file.delete();
		Log.addEntry(Level.INFO, "Removed media from file system? "+filedelres);
		return filedelres;
	}

	

	@GET
	@Path("image/jpeg/{name}")
	@Produces("image/jpeg")
	public Response getJPG(@PathParam("name") String fileName) throws IOException {
			return ResourceHelper.mediaResponse(HLMNGSettings.mediaFileRootDir+fileName, "jpg", request);
	}

	@GET
	@Path("image/png/{name}")
	@Produces("image/png")
	public Response getPNG(@PathParam("name") String fileName)
			throws IOException { 
		return ResourceHelper.mediaResponse(HLMNGSettings.mediaFileRootDir+fileName, "png", request);
	}

	@POST
	@Path("/upload")
	@Consumes("multipart/form-data")
	public Response uploadFileBackend(
			@FormDataParam("file") InputStream fileInputStream,
			@FormDataParam("file") FormDataBodyPart body,
			@FormDataParam("file") FormDataContentDisposition contentDispositionHeader,
			@Context HttpHeaders headers, 
			@Context HttpServletResponse servletResponse
			) throws IOException {

		String mimeType=(body.getMediaType()==null)?"none provided" :body.getMediaType().toString() ;
		Response response =  ResourceHelper.doUpload(fileInputStream, contentDispositionHeader, mimeType,uri,request);
		return response;
	}
	

}