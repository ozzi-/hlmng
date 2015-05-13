package hlmng.resource.adm;

import hlmng.dao.GenDao;
import hlmng.dao.GenDaoLoader;
import hlmng.model.News;
import hlmng.resource.Resource;
import hlmng.resource.ResourceHelper;

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

import settings.HLMNGSettings;

@Path(HLMNGSettings.admURL+"/news")
public class NewsResource extends Resource  {
	private GenDao newsDao =GenDaoLoader.instance.getNewsDao();


	@GET
	@Path("newest")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object> getNewestNews() throws IOException {
		List<Object> newsObjects = listResource(newsDao, true);
		enrichNewsWithMedia(newsObjects);
		return newsObjects;
	}

	
	@GET
	@Path("/lastupdate")
	@Produces(MediaType.TEXT_PLAIN)
	public long getLastUpdateTime() throws IOException {
		return newsDao.getLastUpdateTime();
	}



	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object> getNews() throws IOException {
		List<Object> newsObjects = listResource(newsDao, false);
		enrichNewsWithMedia(newsObjects);
		return newsObjects;
	}
	
	@GET
	@Path("{id}")
	public News getNews(@PathParam("id") int id) throws IOException{
		News news = (News)getResource(newsDao, id);
		if(news!=null){
			String media = ResourceHelper.getMediaURL(uri,news.getMediaIDFK());
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
	public Object postNews(News element) throws IOException {
		News news = (News) postResource(newsDao, element);
		if(news!=null){
			String media = ResourceHelper.getMediaURL(uri, news.getMediaIDFK());
			news.setMedia(media);			
		}
		return news;
	}
	

	protected void enrichNewsWithMedia(List<Object> newsObjects) {
		for (Object object : newsObjects) {
			News news = (News) object;
			String media = ResourceHelper.getMediaURL(uri, news.getMediaIDFK());
			news.setMedia(media);
		}
	}

}

