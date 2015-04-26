package hlmng.resource.pub;

import hlmng.dao.GenDao;
import hlmng.dao.GenDaoLoader;
import hlmng.model.Slider;
import hlmng.resource.Resource;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import settings.HLMNGSettings;



@Path(HLMNGSettings.pubURL+"/slider")
public class SliderResource extends Resource {
	private GenDao sliderDao = GenDaoLoader.instance.getSliderDao();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object> getSlider() throws IOException {
		return listResource(sliderDao, false);
	}
	
	@GET
	@Path("/lastupdate")
	@Produces(MediaType.TEXT_PLAIN)
	public long getLastUpdateTime() throws IOException {
		return sliderDao.getLastUpdateTime();
	}
	
	@GET
	@Path("{id}")
	public Slider getSlider(@PathParam("id") int id) throws IOException{
		return (Slider) getResource(sliderDao, id);
	}

}

