
package hlmng.resource.pub;

import hlmng.dao.GenDao;
import hlmng.dao.GenDaoLoader;
import hlmng.model.Voting;
import hlmng.resource.Resource;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import settings.HLMNGSettings;



@Path(HLMNGSettings.pubURL+"/voting")
public class VotingResource extends Resource {

	private GenDao votingDao = GenDaoLoader.instance.getVotingDao();
	private GenDao sliderDao = GenDaoLoader.instance.getSliderDao();

	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object> getVoting() throws IOException {
		return listResource(votingDao, false);
	}
	
	@GET
	@Path("/lastupdate")
	@Produces(MediaType.TEXT_PLAIN)
	public long getLastUpdateTime() throws IOException {
		return votingDao.getLastUpdateTime();
	}
	
		
	@GET
	@Path("{id}")
	public Voting getVoting(@PathParam("id") int id) throws IOException{
		return (Voting) getResource(votingDao, id);
	}
	

	@SuppressWarnings("unchecked")
	@GET
	@Path("{id}/sliders")
	public List<Object> getSliders(@PathParam("id") int id) throws IOException{
		Object obj= sliderDao.listByFK("votingIDFK", id);
		return (List<Object>) obj;
	}

}

