package hlmng.resource;

import hlmng.FileSettings;
import hlmng.auth.AuthChecker;
import hlmng.dao.GenDao;
import hlmng.dao.GenDaoLoader;
import hlmng.model.Media;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
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
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

import log.Log;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;



@Path("/media")
public class MediaResource extends Resource{

	private static HashMap<String,ResponseBuilder> localJPGResponseCache = new HashMap<String,ResponseBuilder>();
	private static HashMap<String,ResponseBuilder> localPNGResponseCache = new HashMap<String,ResponseBuilder>();
	private GenDao mediaDao =GenDaoLoader.instance.getMediaDao();


	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object> listMedia() throws IOException {
		List<Object> listMedia = listResource(mediaDao, false);
		setURLPathList(listMedia);
		return listMedia;
	}

	@GET
	@Path("{id}")
	public Response getMedia(@PathParam("id") int id) throws IOException {
		return getMediaAsResponse(id, uri, request);
	}
	
	@DELETE
	@Path("{id}")
	public Response deleteMedia(@PathParam("id") int id) throws IOException{
		Media media = (Media)GenDaoLoader.instance.getMediaDao().getElement(id);
		// ONLY deletes on filesystem and local cache,  since path's to that media still have to be there, see documentation
		boolean deleted=false;
		if(media !=null){ 
			deleted = deleteMediaFromFSandCache(media.getLink(),media.getType());						
		}
		return ResourceHelper.returnOkOrNotFoundResponse(deleted);
	}

	private boolean deleteMediaFromFSandCache(String fileName, String type) {

		String mediaPath= FileSettings.mediaFileRootDir+fileName;

		Log.addEntry(Level.INFO, "Trying to del:"+fileName);
		if(type.equals("image/jpg") || type.equals("image/jpeg")){ 
			ResponseBuilder jpgres = localJPGResponseCache.remove(fileName);
			Log.addEntry(Level.INFO, "Removed JPG response from local cache :"+jpgres);
		}else if(type.equals("image/png")){ 
			ResponseBuilder pngres = localPNGResponseCache.remove(fileName);
			Log.addEntry(Level.INFO, "Removed PNG response from local cache :"+pngres);
		}else{
			throw new IllegalArgumentException("implement other types first..");
		}
		File file = new File(mediaPath);
		boolean filedelres = file.delete();
		Log.addEntry(Level.INFO, "Removed media from file system? "+filedelres);
		return filedelres;
	}
	
	public static Response getMediaStatic(int id, UriInfo uriS, Request requestS) throws IOException {
		return getMediaAsResponse(id, uriS, requestS);
	}

	private static Response getMediaAsResponse(int id, UriInfo uriS,Request requestS) {
		ResponseBuilder builder;
		Object obj = GenDaoLoader.instance.getMediaDao().getElement(id);
		if(obj==null){
			builder = Response.status(404);
		}else{
			Media media = (Media) obj;
			ResourceHelper.setMediaURLPath(uriS,media);
			builder = ResourceHelper.cacheControl(media,requestS);			
		}
        return builder.build();
	}
	
	public static String getMediaURL(UriInfo uri, int id){
		Media media = (Media) GenDaoLoader.instance.getMediaDao().getElement(id);
		if(media!=null){
			ResourceHelper.setMediaURLPath(uri,media);
			return media.getLink();			
		}
		return null;
	}
	

	@GET
	@Path("image/jpg/{name}")
	@Produces("image/jpeg")
	public Response getJPG(@PathParam("name") String fileName) throws IOException {
			return mediaResponse(FileSettings.mediaFileRootDir+fileName, "jpg", request,localJPGResponseCache);
	}

	@GET
	@Path("image/png/{name}")
	@Produces("image/png")
	public Response getPNG(@PathParam("name") String fileName)
			throws IOException { 
		return mediaResponse(FileSettings.mediaFileRootDir+fileName, "png", request,localPNGResponseCache);
	}

	@POST
	@Path("/upload")
	@Consumes("multipart/form-data")
	public Response uploadFile(
			@FormDataParam("file") InputStream fileInputStream,
			@FormDataParam("file") FormDataBodyPart body,
			@FormDataParam("file") FormDataContentDisposition contentDispositionHeader,
			@Context HttpHeaders headers, 
			@Context HttpServletResponse servletResponse
			) {

		String mimeType=(body.getMediaType()==null)?"none provided" :body.getMediaType().toString() ;
		
		boolean authenticated = AuthChecker.check(headers, servletResponse, false);
		Response response;
		
		if(authenticated && (mimeType.equals("image/png")||mimeType.equals("image/jpeg"))){
			String filePath = FileSettings.mediaFileRootDir+contentDispositionHeader.getFileName();
			File f = new File(filePath);
			if(f.exists()){
				return  Response.status(422).entity("File name already exists locally. Try again with another one!").build(); 
			}
			boolean savedOK=saveFile(fileInputStream, filePath);
			if(savedOK){
				Log.addEntry(Level.INFO, "File uploaded to:"+filePath+" with mime type: "+mimeType );
				int insertedID = GenDaoLoader.instance.getMediaDao().addElement(new Media(mimeType,contentDispositionHeader.getFileName()));
				return getMediaAsResponse(insertedID, uri, request);
			}else{
				Log.addEntry(Level.WARNING, "File couldn't be saved to:"+filePath+" with mime type: "+mimeType );
				response=Response.status(500).build();				
			}
		}else{
			Log.addEntry(Level.WARNING, "File wasn't uploaded because of wrong mime type: "+mimeType+" or too many requests" );
			response=Response.status(415).build();
		}
		return response;
	}

	// save uploaded file to a defined location on the server
	private boolean saveFile(InputStream uploadedInputStream, String serverLocation) {
		try {
			OutputStream outpuStream = new FileOutputStream(new File(
					serverLocation));
			int read = 0;
			byte[] bytes = new byte[1024];

			outpuStream = new FileOutputStream(new File(serverLocation));
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				outpuStream.write(bytes, 0, read);
			}
			outpuStream.flush();
			outpuStream.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static ResponseBuilder getLocalCacheFile(File file, Request request, HashMap<String, ResponseBuilder> localCache){
		if(localCache.containsKey(file.getName())){
			Log.addEntry(Level.INFO, "Using cached Media Response. Filename:"+file.getName());
			return localCache.get(file.getName());
			
		}else{
			ResponseBuilder responseBuilder = ResourceHelper.cacheControl((File) file,request);
			localCache.put(file.getName(), responseBuilder);
			Log.addEntry(Level.INFO, "Created new Media Response since missing in local cache. Filename:"+file.getName());
			return responseBuilder;
		}
	}

	public static Response mediaResponse(String filePath, String fileType, Request request,HashMap<String,ResponseBuilder> localCache) {
		ResponseBuilder response;
		File file = new File(filePath);
		if (file.canRead()) {
			response = getLocalCacheFile((File) file,request,localCache);
			//response.header("Content-Disposition", "attachment; filename=hlmng." + fileType); 
		} else {
			response = Response.status(Response.Status.NOT_FOUND);
		}
		return response.build();
	}

	private void setURLPathList(List<Object> listMedia) {
		for (Object obj : listMedia) {
			ResourceHelper.setMediaURLPath(uri,(Media) obj);
		}
	}

}