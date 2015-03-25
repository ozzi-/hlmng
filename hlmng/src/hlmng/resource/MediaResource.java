package hlmng.resource;

import hlmng.Log;
import hlmng.auth.AuthChecker;
import hlmng.dao.GenDaoLoader;
import hlmng.model.Media;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.logging.Level;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
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

import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;


@Path("/media")
public class MediaResource {

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
	private static String fileRootDir = "/home/ozzi/media/";
	//private static String fileRootDir = "/home/student/media/";
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object> listMedia() {
		List<Object> listMedia = GenDaoLoader.instance.getMediaDao()
				.listElements();
		setURLPathList(listMedia);
		return listMedia;
	}

	@GET
	@Path("{id}")
	public Response getMedia(@PathParam("id") String id,
			@Context HttpHeaders headers,
			@Context HttpServletResponse servletResponse) throws IOException {
		return getMediaAsResponse(id, uri, request);
	}
	
	public static Response getMediaStatic(String idS, UriInfo uriS, Request requestS) throws IOException {
		return getMediaAsResponse(idS, uriS, requestS);
	}

	private static Response getMediaAsResponse(String idS, UriInfo uriS,
			Request requestS) {
		ResponseBuilder builder;
		Object obj = GenDaoLoader.instance.getMediaDao().getElement(idS);
		if(obj==null){
			builder = Response.status(404);
		}else{
			Media media = (Media) obj;
			ResourceHelper.setMediaURLPath(uriS,media);
			builder = ResourceHelper.cacheControl(media,requestS);			
		}
        return builder.build();
	}
	

	@GET
	@Path("jpg/{name}")
	@Produces("image/jpeg")
	public Response getJPG(@PathParam("name") String fileName, @Context HttpHeaders headers)
			throws IOException {
			return mediaResponse(fileName, "jpg");
	}

	@GET
	@Path("png/{name}")
	@Produces("image/png")
	public Response getPNG(@PathParam("name") String fileName)
			throws IOException {
		return mediaResponse(fileName, "png");
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
			String filePath = fileRootDir + contentDispositionHeader.getFileName();
			boolean savedOK=saveFile(fileInputStream, filePath);
			if(savedOK){
				Log.addEntry(Level.INFO, "File uploaded to:"+filePath+" with mime type: "+mimeType );
				response=Response.status(200).build();				
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

	private Response mediaResponse(String fileName, String fileType) {
		ResponseBuilder response;
		File file = new File(fileRootDir + fileName);
		if (file.canRead()) {
			response = ResourceHelper.cacheControl((File) file,request);
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