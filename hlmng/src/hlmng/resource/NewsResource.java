package hlmng.resource;

import hlmng.dao.GenDao;
import hlmng.dao.GenDaoLoader;
import hlmng.model.News;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/news")
public class NewsResource extends Resource  {
	private GenDao newsDao =GenDaoLoader.instance.getNewsDao();


	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object> getNews() {
		List<Object> newsObjects = GenDaoLoader.instance.getNewsDao().listElements();
		for (Object object : newsObjects) {
			News news = (News) object;
			String media = MediaResource.getMediaURL(uri, news.getMediaIDFK());
			news.setMedia(media);
		}
		return newsObjects;
	}
	
	@GET
	@Path("{id}")
	public News getNews(@PathParam("id") int id) throws IOException{
		News news = (News)getResource(newsDao, id);
		if(news!=null){
			String media = MediaResource.getMediaURL(uri,id);
			news.setMedia(media);
		}
		return news;
	}

	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putNews(News element,@PathParam("id") int id) throws IOException {
		return putResource(newsDao, element, id);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postNews(News element) throws IOException {
		return postResource(newsDao, element, true);
	}
	

}

