package hlmng.resource.pub;

import hlmng.dao.GenDao;
import hlmng.dao.GenDaoLoader;
import hlmng.model.Presentation;
import hlmng.resource.Resource;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import settings.HLMNGSettings;



@Path(HLMNGSettings.pubURL+"/pub/presentation")
public class PresentationResource extends Resource {
	private GenDao presentationDao =GenDaoLoader.instance.getPresentationDao();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object> getPresentation() throws IOException {
		return listResource(presentationDao, false);
	}

	@GET
	@Path("/lastupdate")
	@Produces(MediaType.TEXT_PLAIN)
	public long getLastUpdateTime() throws IOException {
		return presentationDao.getLastUpdateTime();
	}

	@GET
	@Path("{id}")
	public Presentation getPresentation(@PathParam("id") int id) throws IOException{
		return (Presentation) getResource(presentationDao, id);	
	}
}

