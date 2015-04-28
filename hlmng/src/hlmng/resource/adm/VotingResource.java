
package hlmng.resource.adm;

import hlmng.dao.GenDao;
import hlmng.dao.GenDaoLoader;
import hlmng.model.Slider;
import hlmng.model.Vote;
import hlmng.model.Voting;
import hlmng.resource.Resource;

import java.io.IOException;
import java.util.ArrayList;
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



@Path(HLMNGSettings.admURL+"/voting")
public class VotingResource extends Resource {

	private GenDao votingDao = GenDaoLoader.instance.getVotingDao();
	private GenDao sliderDao = GenDaoLoader.instance.getSliderDao();
	private GenDao voteDao = GenDaoLoader.instance.getVoteDao();
	
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

	@GET
	@Path("{id}/votes")
	public List<Vote> getVotes(@PathParam("id") int id) throws IOException{
		List<Object> sliders= sliderDao.listByFK("votingIDFK", id);
		List<Vote> voteList = new ArrayList<Vote>(); 
		for (Object sliderObj : sliders) {
			List<Object> toAddVotes = voteDao.listByFK("sliderIDFK", ((Slider) sliderObj).getSliderID());
			for (Object toAddVoteObj : toAddVotes) {
				voteList.add((Vote)toAddVoteObj);
			}
		}
		return voteList;
	}

	
	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putVoting(Voting element,@PathParam("id") int id) throws IOException {
		return putResource(votingDao, element, id);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Object postVoting(Voting element) throws IOException {
		return postResource(votingDao, element);
	}

}

