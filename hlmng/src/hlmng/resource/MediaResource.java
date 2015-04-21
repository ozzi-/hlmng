package hlmng.resource;

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

import javax.naming.SizeLimitExceededException;
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

import settings.HLMNGSettings;



@Path("/media")
public class MediaResource extends Resource{

	private static final int bytesPerMB = 1048576;
	private static HashMap<String,ResponseBuilder> localJPGResponseCache = new HashMap<String,ResponseBuilder>();
	private static HashMap<String,ResponseBuilder> localPNGResponseCache = new HashMap<String,ResponseBuilder>();
	private static GenDao mediaDao =GenDaoLoader.instance.getMediaDao();


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
		// ONLY deletes on filesystem and local cache,  since path's to that media still have to be there, see documentation
		boolean deleted=false;
		if(media !=null){ 
			deleted = deleteMediaFromFSandCache(media.getLink(),media.getType());						
		}
		return ResourceHelper.returnOkOrNotFoundResponse(deleted);
	}

	private boolean deleteMediaFromFSandCache(String fileName, String type) {

		String mediaPath= HLMNGSettings.mediaFileRootDir+fileName;

		Log.addEntry(Level.INFO, "Trying to delete:"+fileName);
		
		if(type.equals("image/jpg") || type.equals("image/jpeg")){ 
			ResponseBuilder jpgres = localJPGResponseCache.remove(fileName);
			Log.addEntry(Level.INFO, "Removed JPG response from local cache :"+jpgres);
		}else if(type.equals("image/png")){ 
			ResponseBuilder pngres = localPNGResponseCache.remove(fileName);
			Log.addEntry(Level.INFO, "Removed PNG response from local cache :"+pngres);
		}else{
			throw new IllegalArgumentException("implement other media types first..");
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
		Object obj = mediaDao.getElement(id);
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
		Media media = (Media) mediaDao.getElement(id);
		if(media!=null){
			ResourceHelper.setMediaURLPath(uri,media);
			return media.getLink();			
		}
		return null;
	}
	

	@GET
	@Path("image/jpeg/{name}")
	@Produces("image/jpeg")
	public Response getJPG(@PathParam("name") String fileName) throws IOException {
			return mediaResponse(HLMNGSettings.mediaFileRootDir+fileName, "jpg", request,localJPGResponseCache);
	}

	@GET
	@Path("image/png/{name}")
	@Produces("image/png")
	public Response getPNG(@PathParam("name") String fileName)
			throws IOException { 
		return mediaResponse(HLMNGSettings.mediaFileRootDir+fileName, "png", request,localPNGResponseCache);
	}

	@POST
	@Path("/uploadbackend")
	@Consumes("multipart/form-data")
	public Response uploadFileBackend(
			@FormDataParam("file") InputStream fileInputStream,
			@FormDataParam("file") FormDataBodyPart body,
			@FormDataParam("file") FormDataContentDisposition contentDispositionHeader,
			@Context HttpHeaders headers, 
			@Context HttpServletResponse servletResponse
			) {

		return upload(fileInputStream, body, contentDispositionHeader, headers,
				servletResponse,true);
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

		return upload(fileInputStream, body, contentDispositionHeader, headers,
				servletResponse,false);
	}


	private Response upload(InputStream fileInputStream, FormDataBodyPart body,
			FormDataContentDisposition contentDispositionHeader,
			HttpHeaders headers, HttpServletResponse servletResponse, boolean backend) {
		String mimeType=(body.getMediaType()==null)?"none provided" :body.getMediaType().toString() ;

		boolean authenticated = AuthChecker.check(headers, servletResponse, backend);
		Response response;
		
		if(authenticated){
			if(mimeType.equals("image/png")||mimeType.equals("image/jpeg")){
				String filePath = HLMNGSettings.mediaFileRootDir+contentDispositionHeader.getFileName();
				File f = new File(filePath);
				if(f.exists()){
					response=  Response.status(422).entity("File name already exists locally. Try again with another one!").build(); 
				}else{
					response = saveImage(fileInputStream, contentDispositionHeader, mimeType, filePath);
				}
			}else{
				Log.addEntry(Level.WARNING, "File wasn't uploaded because of wrong mime type: "+mimeType );
				response=Response.status(415).build();
			}
		}else{
			response=Response.status(401).build();
		}
		return response;
	}

	private Response saveImage(InputStream fileInputStream,
			FormDataContentDisposition contentDispositionHeader,
			String mimeType, String filePath) {
		Response response;
		boolean savedOK=false;
		try {
			savedOK = saveInputStreamToFile(fileInputStream, filePath,HLMNGSettings.maxMediaImageSizeMB);
			if(savedOK){
				Log.addEntry(Level.INFO, "File uploaded to:"+filePath+" with mime type: "+mimeType );
				int insertedID = mediaDao.addElement(new Media(mimeType,contentDispositionHeader.getFileName()));
				response= getMediaAsResponse(insertedID, uri, request);
			}else{
				Log.addEntry(Level.WARNING, "File couldn't be saved to:"+filePath+" with mime type: "+mimeType );
				response=Response.status(500).build();				
			}	
		} catch (SizeLimitExceededException e) {
			Log.addEntry(Level.WARNING, "Somebody tried to upload a media resource bigger than "+HLMNGSettings.maxMediaImageSizeMB+" MB");
			response=Response.status(413).build();
		}
		return response;
	}


	private boolean saveInputStreamToFile(InputStream uploadedInputStream, String serverLocation, double maxMediaImageSize) throws SizeLimitExceededException {
		try {
			OutputStream outputStream = new FileOutputStream(new File(serverLocation));
			int read = 0;
			byte[] bytes = new byte[1024];
			long bytesWritten=0;
			outputStream = new FileOutputStream(new File(serverLocation));
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				bytesWritten = checkIfUploadToBig(serverLocation, maxMediaImageSize, outputStream, bytes, bytesWritten);
				outputStream.write(bytes, 0, read);
			}
			outputStream.flush();
			outputStream.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Stop downloading if file is bigger than @param maxMediaSize, cleanup and throw exception
	 * @param serverLocation
	 * @param maxMediaSize
	 * @param outputStream
	 * @param bytes
	 * @param bytesWritten
	 * @return
	 * @throws IOException
	 * @throws SizeLimitExceededException
	 */
	private long checkIfUploadToBig(String serverLocation, double maxMediaSize,
			OutputStream outputStream, byte[] bytes, long bytesWritten)
			throws IOException, SizeLimitExceededException {
		
		double mbWritten;
		
		bytesWritten+=bytes.length;
		mbWritten=bytesWritten/bytesPerMB;
		
		if(Double.compare(mbWritten,maxMediaSize)>0){
			outputStream.flush();
			outputStream.close();
			File removeFile = new File(serverLocation);
			removeFile.delete();
			throw new SizeLimitExceededException();
		}
		return bytesWritten;
	}
	
	public static ResponseBuilder getLocalCacheFile(File file, Request request, HashMap<String, ResponseBuilder> localCache){
		ResponseBuilder response;
		if(localCache.containsKey(file.getName())){
			Log.addEntry(Level.INFO, "Using cached Media Response. Filename:"+file.getName());
			response = localCache.get(file.getName());		
		}else{
			Log.addEntry(Level.INFO, "Created new Media Response since missing in local cache. Filename:"+file.getName());
			response = ResourceHelper.cacheControl((File) file,request);
			localCache.put(file.getName(), response);
		}
		return response;
	}

	public static Response mediaResponse(String filePath, String fileType, Request request,HashMap<String,ResponseBuilder> localCache) {
		ResponseBuilder response;
		File file = new File(filePath);
		if (file.canRead()) {
			response = getLocalCacheFile((File) file,request,localCache);
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