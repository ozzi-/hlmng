package hlmng.resource.adm;

import hlmng.dao.GenDao;
import hlmng.dao.GenDaoLoader;
import hlmng.model.Slider;
import hlmng.model.Vote;
import hlmng.resource.Resource;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import settings.HLMNGSettings;



@Path(HLMNGSettings.admURL+"/slider")
public class SliderResource extends Resource {
		
	private GenDao sliderDao = GenDaoLoader.instance.getSliderDao();
	private GenDao voteDao = GenDaoLoader.instance.getVoteDao();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object> getSlider() throws IOException {
		return listResource(sliderDao, false);
	}
	
	@SuppressWarnings("unchecked")
	@GET
	@Path("{id}/votes")
	public List<Object> getVotesOfSlider(@PathParam("id") int id) throws IOException{
		Object obj= voteDao.listByFK("sliderIDFK", id);
		return (List<Object>) obj;			
	}
	@GET
	@Path("{id}/votescorejury")
	public double getVoteScoreJury(@PathParam("id") int id) throws IOException{
		List<Object> objList= voteDao.listByFK("sliderIDFK", id);
		int voteSum=0;
		int voteCounter=0;
		for (Object obj : objList) {
			Vote vote = (Vote) obj;
			if(vote.isIsJury()){
				voteSum+=vote.getScore();
				voteCounter++;
			}
		}
		if(voteCounter>0){
			return (double)voteSum/(double)voteCounter;
		}
		return -1;
	}
	
	@GET
	@Path("{id}/votescoreaudience")
	public double getVoteScoreAudience(@PathParam("id") int id) throws IOException{
		List<Object> objList= voteDao.listByFK("sliderIDFK", id);
		int voteSum=0;
		int voteCounter=0;
		for (Object obj : objList) {
			Vote vote = (Vote) obj;
			if(!vote.isIsJury()){
				voteSum+=vote.getScore();
				voteCounter++;
			}
		}
		if(voteCounter>0){
			return (double)voteSum/(double)voteCounter;
		}
		return -1;		
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

	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putSlider(Slider element,@PathParam("id") int id) throws IOException {
		return putResource(sliderDao, element, id);
	}
	
	@DELETE
	@Path("{id}")
	public Response deleteSlider(@PathParam("id") int id) throws IOException {
		return deleteResource(sliderDao, id);
	}


	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Object postSlider(Slider element) throws IOException {
		return postResource(sliderDao, element);
	}

}

