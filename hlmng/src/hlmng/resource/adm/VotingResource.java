
package hlmng.resource.adm;

import hlmng.dao.GenDao;
import hlmng.dao.GenDaoLoader;
import hlmng.model.PresentationPause;
import hlmng.model.Push;
import hlmng.model.Slider;
import hlmng.model.Vote;
import hlmng.model.Voting;
import hlmng.resource.CSVExporter;
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
	@Produces(MediaType.APPLICATION_JSON)
	public Voting getVoting(@PathParam("id") int id) throws IOException{
		return (Voting) getResource(votingDao, id);
	}
	@GET
	@Path("{id}/sliders")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object> getSliders(@PathParam("id") int id) throws IOException{
		List<Object> obj= sliderDao.listByFK("votingIDFK", id);
		return obj;
	}
	@GET
	@Path("{id}/export")
	@Produces({"text/csv"})
	public String doExport(@PathParam("id") int id) throws IOException, java.text.ParseException{
		CSVExporter csvExp = new CSVExporter();
		List<Vote> voteList = getVoteList(id,modes.all);
		if(voteList.size()==0){
			csvExp.addValue("Didn't find any votes or voting id invalid", true);
		}else{
			csvExp.addValue("Voting Settings", true);
			csvExp.addValue("", true);
			Voting voting = (Voting) getResource(votingDao, id);
			csvExp.addHeader(voting);
			csvExp.addLine(voting);
			csvExp.addValue("", true);
			//--
			csvExp.addValue("Total Jury Score", false);
			csvExp.addValue("Total Audience Score", false);
			csvExp.addValue("", true);
			csvExp.addValue(String.valueOf(getTotalScoreJury(id)), false);
			csvExp.addValue(String.valueOf(getTotalScoreAudience(id)), true);
			csvExp.addValue("", true);
			//--
			csvExp.addValue("Votes:", true);
			csvExp.addValue("", true);
			csvExp.addList(new ArrayList<Object>(voteList));
		}
		return csvExp.toString();
	}
	@GET
	@Path("{id}/totalscorejury")
	@Produces(MediaType.TEXT_PLAIN)
	public Double getTotalScoreJury(@PathParam("id") int id) throws IOException, java.text.ParseException{
		List<Object> objSliderList= sliderDao.listByFK("votingIDFK", id);
		boolean inTime = isPresentationInTimeInternal(id);
		Double totalScoreJury = getTotalScore(objSliderList,inTime,id);
		return totalScoreJury;
	}
	@GET
	@Path("{id}/totalscoreaudience")
	@Produces(MediaType.TEXT_PLAIN)
	public Double getTotalScoreAudience(@PathParam("id") int id) throws IOException{
		List<Object> objSliderList= sliderDao.listByFK("votingIDFK", id);
		return getTotalScore(objSliderList);
	}
	@GET
	@Path("{id}/presentationpauses")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object> getPauses(@PathParam("id") int id){
		return presentationpauseDao.listByFK("votingIDFK", id);
	}
	@GET
	@Path("{id}/presentationintime")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean isPresentationInTime(@PathParam("id") int id) throws java.text.ParseException, IOException{
		return isPresentationInTimeInternal(id);
	}
	private boolean isPresentationInTimeInternal(int id) throws IOException,
			java.text.ParseException {
		Voting voting = (Voting) getResource(votingDao, id);
		String presentationDuration = presentationDuration(id, voting);
		String presentationMinTime = voting.getPresentationMinTime();
		String presentationMaxTime = voting.getPresentationMaxTime();
		TimePart tpDur = TimePart.parse(presentationDuration);
		TimePart tpMin = TimePart.parse(presentationMinTime);
		TimePart tpMax = TimePart.parse(presentationMaxTime);
		return(tpDur.compareTo(tpMin)>=0 && (tpDur.compareTo(tpMax)<=0));
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
	@Produces(MediaType.APPLICATION_JSON)
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
	@Produces(MediaType.TEXT_PLAIN)
	public String getDuration(@PathParam("id") int id) throws java.text.ParseException{
		Voting voting = (Voting)votingDao.getElement(id);
		return presentationDuration(id, voting);
	}  
	@GET
	@Path("{id}/votes")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Vote> getVotes(@PathParam("id") int id) throws IOException{
		return getVoteList(id,modes.all);
	}
	@GET
	@Path("{id}/votes/count")
	@Produces(MediaType.TEXT_PLAIN)
	public int getVotesCount(@PathParam("id") int id) throws IOException{
		int sliderCount = getSliders(id).size();
		return getVoteList(id,modes.all).size()/sliderCount;
	}
	@GET
	@Path("{id}/votes/audience")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Vote> getVotesAudience(@PathParam("id") int id) throws IOException{
		return getVoteList(id,modes.audienceOnly);
	}
	@GET
	@Path("{id}/votes/audience/count")
	@Produces(MediaType.APPLICATION_JSON)
	public int getVotesAudienceCount(@PathParam("id") int id) throws IOException{
		int sliderCount = getSliders(id).size();
		if(sliderCount==0){
			return 0;
		}
		return getVoteList(id,modes.audienceOnly).size()/sliderCount;
	}
	@GET
	@Path("{id}/votes/jury")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Vote> getVotesJury(@PathParam("id") int id) throws IOException{
		return getVoteList(id,modes.juryOnly);
	}
	@GET
	@Path("{id}/votes/jury/count")
	@Produces(MediaType.APPLICATION_JSON)
	public int getVotesJuryCount(@PathParam("id") int id) throws IOException{
		int sliderCount = getSliders(id).size();
		if(sliderCount==0){
			return 0;
		}
		return getVoteList(id,modes.juryOnly).size()/sliderCount;
	}
	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response putVoting(Voting element,@PathParam("id") int id) throws IOException, ParseException {
		if(element.getStatus().equals(Voting.statusEnum.presentation_end.toString()) || element.getStatus().equals(Voting.statusEnum.voting.toString())){
			List<Object> users = listResource(userDao, false);
			Push pushNotif = new Push(element.getStatus(),"{ \"votingID\": "+element.getVotingID()+" , \"name\": \""+element.getName()+"\" }", "vote_event" );
			PushResource.doGCMSend(pushNotif, users);
			postResource(pushDao,pushNotif);
		}else{
			log.Log.addEntry(Level.INFO, "No need for GCM push");
		}
		Voting before = (Voting) getResource(votingDao, id);
		
		if(element.getRound() > before.getRound()){
			List<Object> sliders = sliderDao.listByFK("votingIDFK",before.getVotingID());
			for (Object sliderObj : sliders) {
				Slider slider = (Slider) sliderObj;
				List<Object> votesToDelete = voteDao.listByFK("sliderIDFK", slider.getSliderID());
				for (Object voteObj : votesToDelete) {
					Vote vote = (Vote) voteObj;
					voteDao.deleteElement(vote.getVoteID());
				}
			}
		}
		return putResource(votingDao, element, id);
	}
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Object postVoting(Voting element) throws IOException{
		return postResource(votingDao, element);
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
	private String presentationDuration(int id, Voting voting)
			throws java.text.ParseException {
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
	private Double getTotalScore(List<Object> objSliderList,Boolean inTime, int id) {
		double totalScore = 0.0;
		int virtualSliderCount=0;
		for (Object obj : objSliderList) {
			Slider slider = (Slider) obj;
			virtualSliderCount+=slider.getWeight();
			totalScore += getSliderScore(slider.getSliderID(),false)*slider.getWeight();
		}
		Voting voting = (Voting) votingDao.getElement(id);
		double inTimeScore = (inTime)?voting.getSliderMaxValue()*voting.getInTimeScoreWeight():0.0;
		virtualSliderCount+=voting.getInTimeScoreWeight();
		totalScore+=inTimeScore;
		return totalScore / virtualSliderCount;
	}
	private Double getTotalScore(List<Object> objSliderList) {
		double totalScore = 0.0;
		int virtualSliderCount=0;
		for (Object obj : objSliderList) {
			Slider slider = (Slider) obj;
			virtualSliderCount+=slider.getWeight();
			totalScore += getSliderScore(slider.getSliderID(),false)*slider.getWeight();
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
}

