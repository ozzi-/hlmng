package hlmng.resource.pub;

import hlmng.dao.GenDao;
import hlmng.dao.GenDaoLoader;
import hlmng.model.News;
import hlmng.resource.Resource;
import hlmng.resource.ResourceHelper;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import settings.HLMNGSettings;

@Path(HLMNGSettings.pubURL+"/news")
public class NewsResource extends Resource  {
	private GenDao newsDao =GenDaoLoader.instance.getNewsDao();
	
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
		ResourceHelper.enrichNewsWithMedia(uri,newsObjects);
		return newsObjects;
	}
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public News getNews(@PathParam("id") int id) throws IOException{
		News news = (News)getResource(newsDao, id);
		if(news!=null){
			String media = ResourceHelper.getMediaURL(uri,news.getMediaIDFK());
			news.setMedia(media);
		}
		return news;
	}
	
}

