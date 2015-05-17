
package hlmng.resource.adm;

import hlmng.dao.GenDao;
import hlmng.dao.GenDaoLoader;
import hlmng.model.PresentationPause;
import hlmng.model.Push;
import hlmng.model.Slider;
import hlmng.model.Vote;
import hlmng.model.Voting;
import hlmng.resource.Resource;
import hlmng.resource.TimeHelper;
import hlmng.resource.TimeHelper.TimePart;

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

import org.json.simple.parser.ParseException;

import settings.HLMNGSettings;



@Path(HLMNGSettings.admURL+"/voting")
public class VotingResource extends Resource {

	private GenDao votingDao = GenDaoLoader.instance.getVotingDao();
	private GenDao sliderDao = GenDaoLoader.instance.getSliderDao();
	private GenDao voteDao = GenDaoLoader.instance.getVoteDao();
	private GenDao pushDao = GenDaoLoader.instance.getPushDao();
	private GenDao userDao = GenDaoLoader.instance.getUserDao();
	private GenDao presentationpauseDao = GenDaoLoader.instance.getPresentationpauseDao();
	
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
	@Path("{id}/presentationpauses")
	public List<Object> getPauses(@PathParam("id") int id){
		return presentationpauseDao.listByFK("votingIDFK", id);
	}
	
	@GET
	@Path("{id}/duration")
	public String getDuration(@PathParam("id") int id) throws java.text.ParseException{
		Voting voting = (Voting)votingDao.getElement(id);
		if(voting.getPresentationStarted()==null || voting.getPresentationEnded()==null){
			return "00:00:00";
		}
		String presentationDiff = calcDifference(voting.getPresentationStarted(), voting.getPresentationEnded());
        String pauseTotal="00:00:00"; 
        String pausePart ="00:00:00";
        
        List<Object> pauses = presentationpauseDao.listByFK("votingIDFK", id);
        for (Object pauseObj : pauses) {
			PresentationPause pause = (PresentationPause) pauseObj;
			pausePart = calcDifference(pause.getStart(),pause.getStop());
			pauseTotal = calcSum(pausePart,pauseTotal);
		}
        return calcDifference(pauseTotal,presentationDiff);
	}  
 
	private String calcSum(String timeOneP, String timeTwoP) throws java.text.ParseException{
		TimePart tp = new TimeHelper.TimePart();
		TimePart one = TimeHelper.TimePart.parse(timeOneP);
		TimePart two = TimeHelper.TimePart.parse(timeTwoP);		
		tp.add(one);
		tp.add(two);
		return(tp.toString());
	}
	private String calcDifference(String startedP, String endedP)
			throws java.text.ParseException {
		if(startedP==null || endedP==null){
			return "00:00:00";
		}
		TimePart tp = new TimeHelper.TimePart();
		TimePart started = TimeHelper.TimePart.parse(startedP);
		TimePart ended = TimeHelper.TimePart.parse(endedP);
		tp.add(ended);
		tp.sub(started);
		return (tp.toString());
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
	public Response putVoting(Voting element,@PathParam("id") int id) throws IOException, ParseException {
		if(element.getStatus().equals(Voting.statusEnum.presentation_end) || element.getStatus().equals(Voting.statusEnum.voting)){
			List<Object> users = listResource(userDao, false);
			Push pushNotif = new Push(element.getStatus(),"{ \"votingID\": "+element.getVotingID()+" , \"name\": \""+element.getName()+"\" }", "vote_event" );
			PushResource.doGCMSend(pushNotif, users);
			postResource(pushDao,pushNotif);
		}
		return putResource(votingDao, element, id);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Object postVoting(Voting element) throws IOException{
		return postResource(votingDao, element);
	}

}

