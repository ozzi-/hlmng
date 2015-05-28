package hlmng.resource;

import hlmng.dao.GenDao;
import hlmng.dao.GenDaoLoader;
import hlmng.model.Media;
import hlmng.model.ModelHelper;
import hlmng.model.News;
import hlmng.model.Social;
import hlmng.model.Speaker;
import hlmng.model.User;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;
import java.util.logging.Level;

import javax.imageio.ImageIO;
import javax.naming.SizeLimitExceededException;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

import log.Log;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.imgscalr.Scalr;

import settings.HLMNGSettings;
import settings.HTTPCodes;

public class ResourceHelper {
	
	private static GenDao mediaDao =GenDaoLoader.instance.getMediaDao();
	private static SecureRandom randomGen = new SecureRandom();
	private static final int bytesPerMB = 1048576;

	
	public static String getSecret(){
	    return new BigInteger(HLMNGSettings.qrcodeSecretStrengthInBit, randomGen).toString(32);
	}
	
	public static void enrichSpeakerWithMedia(UriInfo uri, List<Object> speakerObjects) {
		for (Object object : speakerObjects) {
			Speaker speaker = (Speaker) object;
			String media = ResourceHelper.getMediaURL(uri, speaker.getMediaIDFK());
			speaker.setMedia(media);
		}
	}

	public static void enrichNewsListWithMedia(UriInfo uri, List<Object> newsObjects) {
		for (Object object : newsObjects) {
			News news = (News) object;
			String media = ResourceHelper.getMediaURL(uri, news.getMediaIDFK());
			news.setMedia(media);
		}
	}
	
	public static void enrichNewsWithMedia(UriInfo uri, List<Object> newsObjects) {
		for (Object object : newsObjects) {
			News news = (News) object;
			String media = ResourceHelper.getMediaURL(uri, news.getMediaIDFK());
			news.setMedia(media);
		}
	}
	
	public static void enrichSocialListWithUsernameAndMedia(UriInfo uri, List<Object> socialObjects) {
		for (Object object : socialObjects) {
			Social social = (Social) object;
			enrichSocialWithUsernameAndMedia(uri,social);
		}
	}
	private static String getUsername(int id){
		User user = (User) GenDaoLoader.instance.getUserDao().getElement(id);
		if(user!=null){
			return user.getName();
		}
		return "unknown";
	}
	public static void enrichSocialWithUsernameAndMedia(UriInfo uri, Social social) {
		String authorName = getUsername(social.getUserIDFK());
		social.setAuthorName(authorName);
		String media = ResourceHelper.getMediaURL(uri, social.getMediaIDFK());
		social.setMedia(media);
	}	
	
	/**
	 * @param ok
	 * @return If @param is true returns a HTTP 200 (OK) Response, else 400 (bad request)
	 */
	static Response returnOkOrBadReqResponse(boolean ok) {
		if(ok){
			return Response.ok().build();			
		}else{
			return Response.status(HTTPCodes.badRequest).build();
		}
	}
	
	public static Response returnOkOrNotFoundResponse(boolean ok) {
		if(ok){
			return Response.ok().build();			
		}else{
			return Response.status(HTTPCodes.notFound).build();
		}
	}
	
	static boolean sendErrorIfNull(Object obj,HttpServletResponse response) throws IOException {
		if(obj==null){
		    response.sendError(HTTPCodes.notFound);
		    return true;
		}
		return false;
	}	

	/**
	 * Either returns a 200 with the desired object if not in cache, else 304 not modified
	 * @param object
	 * @param request
	 * @return
	 */
	public static ResponseBuilder cacheControl(Object object,Request request) {
		CacheControl cc = new CacheControl();
        cc.setMaxAge(HLMNGSettings.cacheTime);
        cc.setPrivate(true);
        EntityTag etag = new EntityTag(Integer.toString(ModelHelper.hashCode(object)));
        ResponseBuilder builder = request.evaluatePreconditions(etag);
        if(builder == null){
        	Log.addEntry(Level.INFO,"Cache control needs to create new entry"); 
        	builder = Response.ok(object); 
        	builder.cacheControl(cc);
        	builder.tag(etag);
        }else{
        	builder.cacheControl(cc);
            Log.addEntry(Level.INFO,"Cache control can use cached entry"); 
        }
		return builder;
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
	public static long checkIfUploadToBig(String serverLocation, double maxMediaSize,
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
	
	public static Response doUpload(InputStream fileInputStream,
			FormDataContentDisposition contentDispositionHeader, String mimeType, UriInfo uri, Request request) throws IOException {
		Response response;
		if(mimeType.equals("image/png")||mimeType.equals("image/jpeg")){
			String filePath = HLMNGSettings.mediaFileRootDir+contentDispositionHeader.getFileName();
			File f = new File(filePath);
			if(f.exists()){
				response=  Response.status(HTTPCodes.unprocessableEntity).entity("File name already exists locally. Try again with another one!").build(); 
			}else{
				response = saveImage(fileInputStream, contentDispositionHeader, mimeType, filePath,uri,request);
			}
		}else{
			Log.addEntry(Level.WARNING, "File wasn't uploaded because of wrong mime type: "+mimeType );
			response=Response.status(HTTPCodes.unsupportedMediaType).build();
		}
		return response;
	}
	
	private static Response saveImage(InputStream fileInputStream,
			FormDataContentDisposition contentDispositionHeader,
			String mimeType, String filePath, UriInfo uri, Request request) throws IOException {
		Response response;
		boolean savedOK=false;
		try {
			savedOK = ResourceHelper.saveInputStreamToFile(fileInputStream, filePath,HLMNGSettings.maxMediaImageSizeMB);
			if(savedOK){
				boolean thumbnailWorked = saveThumbnail(filePath);
				Log.addEntry(Level.INFO, "File uploaded to:"+filePath+" with mime type: "+mimeType );
				if(!thumbnailWorked){
					Log.addEntry(Level.WARNING, "Thumbnail creation failed!");
				}
				int insertedID = mediaDao.addElement(new Media(mimeType,contentDispositionHeader.getFileName()));
				response= getMediaAsResponse(insertedID, uri, request);
			}else{
				Log.addEntry(Level.WARNING, "File couldn't be saved to:"+filePath+" with mime type: "+mimeType );
				response=Response.status(HTTPCodes.internalServerError).build();				
			}	
		} catch (SizeLimitExceededException e) {
			Log.addEntry(Level.WARNING, "Somebody tried to upload a media resource bigger than "+HLMNGSettings.maxMediaImageSizeMB+" MB");
			response=Response.status(HTTPCodes.requestEntityTooLarge).build();
		}
		return response;
	}

	private static boolean saveThumbnail(String filePath) throws IOException {
		File fileToScale = new File(filePath);
		File fileScaled = new File(filePath+"_thumb");
		String fileExtension="";
		int i = filePath.lastIndexOf('.');
		if (i > 0) {
		    fileExtension= filePath.substring(i+1);
		}
		fileScaled.createNewFile();
		BufferedImage img = ImageIO.read(fileToScale);
		BufferedImage scaledImg = Scalr.resize(img, HLMNGSettings.mediaUploadThumbnailPixel);
		return ImageIO.write(scaledImg, fileExtension, fileScaled);
	}


	public static void setURLPathList(List<Object> listMedia,UriInfo uri) {
		for (Object obj : listMedia) {
			ResourceHelper.setMediaURLPath(uri,(Media) obj);
		}
	}
	
	public static String getMediaURL(UriInfo uri, int id){
		Media media = (Media) mediaDao.getElement(id);
		if(media!=null){
			ResourceHelper.setMediaURLPath(uri,media);
			return media.getLink();			
		}
		return null;
	}
	
	public static Response getMediaAsResponse(int id, UriInfo uriS,Request requestS) {
		ResponseBuilder builder;
		Object obj = mediaDao.getElement(id);
		if(obj==null){
			builder = Response.status(HTTPCodes.notFound);
		}else{
			Media media = (Media) obj;
			ResourceHelper.setMediaURLPath(uriS,media);
			builder = ResourceHelper.cacheControl(media,requestS);			
		}
        return builder.build();
	}

	public static boolean saveInputStreamToFile(InputStream uploadedInputStream, String serverLocation, double maxMediaImageSize) throws SizeLimitExceededException {
		try {
			OutputStream outputStream = new FileOutputStream(new File(serverLocation));
			int read = 0;
			byte[] bytes = new byte[1024];
			long bytesWritten=0;
			outputStream = new FileOutputStream(new File(serverLocation));
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				bytesWritten = ResourceHelper.checkIfUploadToBig(serverLocation, maxMediaImageSize, outputStream, bytes, bytesWritten);
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

	public static Response mediaResponse(String filePath, String fileType, Request request) {
		ResponseBuilder response;
		File file = new File(filePath);
		if (file.canRead()) {
			response = ResourceHelper.cacheControl((File) file,request);
		} else {
			response = Response.status(HTTPCodes.notFound);
		}
		return response.build();
	}
	
	public static void setMediaURLPath(UriInfo uri, Media media) {
		media.setLink(HLMNGSettings.restAppPath+"/"+HLMNGSettings.pubURL.substring(1) +"/media/"
				+ media.getType() + "/" + media.getLink());
	}
}
