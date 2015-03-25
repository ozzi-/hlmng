package hlmng.resource;

import hlmng.dao.GenDaoLoader;
import hlmng.model.News;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/news")
public class NewsResource extends Resource  {

	@GET
	@Path("count")
	@Produces(MediaType.TEXT_PLAIN)
	public int getCount() {
		return GenDaoLoader.instance.getNewsDao().listElements().size();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object> getNews() {
		List<Object> newsObjects = GenDaoLoader.instance.getNewsDao().listElements();
		for (Object object : newsObjects) {
			News news = (News) object;
			String media = MediaResource.getMediaURL(uri, Integer.toString(news.getMediaIDFK()));
			news.setMedia(media);
		}
		return newsObjects;
	}
	
	@GET
	@Path("{id}")
	public News getNews(@PathParam("id") String id,
			@Context HttpHeaders headers,
			@Context HttpServletResponse servletResponse) throws IOException{
		Object obj =  GenDaoLoader.instance.getNewsDao().getElement(id);
		News evt=null;
		if(!ResourceHelper.sendErrorIfNull(obj,response)){
			evt=(News) obj;
			String media = MediaResource.getMediaURL(uri, id);
			evt.setMedia(media); 			
		}
		return evt;	
	}

	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putNews(News element,@PathParam("id") String id) {
		Response res;
		if (GenDaoLoader.instance.getNewsDao().getElement(id)!=null){
			GenDaoLoader.instance.getNewsDao().updateElement(element, id);
			res = Response.accepted().build();
		}else{
			int insertedID = GenDaoLoader.instance.getNewsDao().addElement(element);
			res= ResourceHelper.returnOkOrErrorResponse(!(insertedID==-1));
		}
		return res;	
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response newNews(News element) throws IOException {
		int insertedID = GenDaoLoader.instance.getNewsDao().addElement(element);
		return ResourceHelper.returnOkOrErrorResponse(!(insertedID==-1));
	}
	
	// FORMS
	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response newNews(@FormParam("title") String title, @FormParam("text") String text,
			@FormParam("author") String author, @FormParam("mediaIDFK") int mediaIDFK, @FormParam("eventIDFK") int eventIDFK,
			@Context HttpServletResponse servletResponse) throws IOException {
		Object addNews = (Object)new News(title,text, author, mediaIDFK, eventIDFK);
		int insertedID = GenDaoLoader.instance.getNewsDao().addElement(addNews);
		return ResourceHelper.returnOkOrErrorResponse(!(insertedID==-1));
	}

}

