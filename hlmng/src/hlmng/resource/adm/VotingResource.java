
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
import java.util.logging.Level;

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
	@Path("{id}/totalscorejury")
	public Double getTotalScoreJury(@PathParam("id") int id) throws IOException{
		List<Object> objSliderList= sliderDao.listByFK("votingIDFK", id);
		return getTotalScore(objSliderList,true);
	}
	@GET
	@Path("{id}/totalscoreaudience")
	public Double getTotalScoreAudience(@PathParam("id") int id) throws IOException{
		List<Object> objSliderList= sliderDao.listByFK("votingIDFK", id);
		return getTotalScore(objSliderList,false);
	}

	private Double getTotalScore(List<Object> objSliderList,boolean jury) {
		double totalScore = 0.0;
		int virtualSliderCount=0;
		for (Object obj : objSliderList) {
			Slider slider = (Slider) obj;
			virtualSliderCount+=slider.getWeight();
			totalScore += getSliderScore(slider.getSliderID(),jury)*slider.getWeight();
		}
		return totalScore / virtualSliderCount;
	}
	private double getSliderScore(int id,boolean jury) {
		List<Object> objVoteList= voteDao.listByFK("sliderIDFK", id);
		int voteSum=0;
		int voteCounter=0;
		for (Object objVote : objVoteList) {
			Vote vote = (Vote) objVote;
			if(jury){
				if(vote.isIsJury()){
					voteSum+=vote.getScore();
					voteCounter++;
				}
			}else{
				if(!vote.isIsJury()){
					voteSum+=vote.getScore();
					voteCounter++;
				}
			}
		
		}
		if(voteCounter>0){
			return (double)voteSum/(double)voteCounter;
		}
		return -1;
	}

	
	@GET
	@Path("{id}/presentationpauses")
	public List<Object> getPauses(@PathParam("id") int id){
		return presentationpauseDao.listByFK("votingIDFK", id);
	}
	
	@GET
	@Path("{id}/ispaused")
	public boolean isPaused(@PathParam("id") int id){
		List<Object> pauseObjList = presentationpauseDao.listByFK("votingIDFK", id);
		boolean isPaused = false;
		for (Object pPobj : pauseObjList) {
			PresentationPause pP = (PresentationPause) pPobj;
			if(pP.getStop()==null){
				isPaused=true;
			}
		}
		return isPaused;
	}
	@GET
	@Path("{id}/getpause")
	public PresentationPause getPauseElem(@PathParam("id") int id){
		List<Object> pauseObjList = presentationpauseDao.listByFK("votingIDFK", id);
		for (Object pPobj : pauseObjList) {
			PresentationPause pP = (PresentationPause) pPobj;
			if(pP.getStop()==null){
				return pP;
			}
		}
		return null;
	}
	@GET
	@Path("{id}/audiencevotingover")
	public boolean checkAudienceVotingOver(@PathParam("id") int id) throws IOException{
		Voting voting = (Voting) getResource(votingDao, id);
		if(voting==null || voting.getVotingDuration()==null || voting.getVotingStarted()==null){
			return false;
		}
		TimePart tpShould = new TimeHelper.TimePart();
		tpShould.add(TimePart.parse(voting.getVotingStarted()));
		tpShould.add(TimePart.parse(voting.getVotingDuration()));
		TimePart tpIs = new TimeHelper.TimePart();
		tpIs = TimePart.parse(TimeHelper.getCurrentTime());
		return (tpShould.compareTo(tpIs)==-1);
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
		return getVoteList(id,modes.all);
	}
	@GET
	@Path("{id}/votes/count")
	public int getVotesCount(@PathParam("id") int id) throws IOException{
		int sliderCount = getSliders(id).size();
		return getVoteList(id,modes.all).size()/sliderCount;
	}
	@GET
	@Path("{id}/votes/audience")
	public List<Vote> getVotesAudience(@PathParam("id") int id) throws IOException{
		return getVoteList(id,modes.audienceOnly);
	}
	@GET
	@Path("{id}/votes/audience/count")
	public int getVotesAudienceCount(@PathParam("id") int id) throws IOException{
		int sliderCount = getSliders(id).size();
		return getVoteList(id,modes.audienceOnly).size()/sliderCount;
	}
	@GET
	@Path("{id}/votes/jury")
	public List<Vote> getVotesJury(@PathParam("id") int id) throws IOException{
		return getVoteList(id,modes.juryOnly);
	}
	@GET
	@Path("{id}/votes/jury/count")
	public int getVotesJuryCount(@PathParam("id") int id) throws IOException{
		int sliderCount = getSliders(id).size();
		return getVoteList(id,modes.juryOnly).size()/sliderCount;
	}
	

	private enum modes{
		juryOnly, audienceOnly, all
	}
	private List<Vote> getVoteList(int id, modes selectionJuryMode) {
		List<Object> sliders= sliderDao.listByFK("votingIDFK", id);
		List<Vote> voteList = new ArrayList<Vote>(); 
		for (Object sliderObj : sliders) {
			List<Object> toAddVotes = voteDao.listByFK("sliderIDFK", ((Slider) sliderObj).getSliderID());
			for (Object toAddVoteObj : toAddVotes) {
				Vote vote = (Vote)toAddVoteObj;
				if(selectionJuryMode==modes.all){
					voteList.add(vote);					
				}else{
					if(selectionJuryMode==modes.juryOnly){
						if(vote.isIsJury()){
							voteList.add(vote);					
						}
					}else{
						if(!vote.isIsJury()){
							voteList.add(vote);					
						}
					}
				}
			}
		}
		return voteList;
	}
	
	
	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putVoting(Voting element,@PathParam("id") int id) throws IOException, ParseException {
		if(element.getStatus().equals(Voting.statusEnum.presentation_end.toString()) || element.getStatus().equals(Voting.statusEnum.voting.toString())){
			List<Object> users = listResource(userDao, false);
			Push pushNotif = new Push(element.getStatus(),"{ \"votingID\": "+element.getVotingID()+" , \"name\": \""+element.getName()+"\" }", "vote_event" );
			PushResource.doGCMSend(pushNotif, users);
			postResource(pushDao,pushNotif);
		}else{
			log.Log.addEntry(Level.INFO, "No need for GCM push");
		}
		return putResource(votingDao, element, id);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Object postVoting(Voting element) throws IOException{
		return postResource(votingDao, element);
	}

}

