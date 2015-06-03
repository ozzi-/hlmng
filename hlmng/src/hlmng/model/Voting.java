package hlmng.model;

public class Voting {
	private int votingID;
	private String name;
	private int juryCount;
	private String status;
	private int sliderMaxValue;
	private String votingDate;
	private String votingStarted;
	private String votingDuration;
	private String arithmeticMode;
	private String presentationMinTime;
	private String presentationMaxTime;
	private String presentationStarted;
	private String presentationEnded;
	private int inTimeScoreWeight;
	private int round;
	private int eventIDFK;

	  
	public enum statusEnum {
		pre_presentation,presentation,presentation_end,voting,voting_end
	}
	
	public Voting(){
	
	}
	public Voting(int votingID, String name, int juryCount, String status, int sliderMaxValue, String date, String votingStarted, String votingDuration, String arithmeticMode, String presentationMinTime, String presentationMaxTime, String presentationStarted, String presentationEnded,int inTimeScoreWeight, int round, int eventIDFK){
		this.votingID=votingID;
		this.name=name;
		this.juryCount=juryCount;
		this.status=status;
		this.sliderMaxValue=sliderMaxValue;
		this.votingDate=date;
		this.votingStarted=votingStarted;
		this.votingDuration=votingDuration;
		this.arithmeticMode=arithmeticMode;
		this.presentationMinTime=presentationMinTime;
		this.presentationMaxTime=presentationMaxTime;
		this.presentationStarted=presentationStarted;
		this.presentationEnded=presentationEnded;
		this.inTimeScoreWeight=inTimeScoreWeight;
		this.round=round;
		this.eventIDFK=eventIDFK;
	}
	public Voting(String name, int juryCount, String status, int sliderMaxValue, String date, String votingDuration, String arithmeticMode, String presentationMinTime, String presentationMaxTime, String presentationStarted, String presentationEnded, int inTimeScoreWeight, int round, int eventIDFK){
		this.name=name;
		this.juryCount=juryCount;
		this.status=status;
		this.sliderMaxValue=sliderMaxValue;
		this.votingDate=date;
		this.votingDuration=votingDuration;
		this.arithmeticMode=arithmeticMode;
		this.presentationMinTime=presentationMinTime;
		this.presentationMaxTime=presentationMaxTime;
		this.presentationStarted=presentationStarted;
		this.presentationEnded=presentationEnded;
		this.inTimeScoreWeight=inTimeScoreWeight;
		this.round=round;
		this.eventIDFK=eventIDFK;
	}

	public int getVotingID() {
		return votingID;
	}
	public void setVotingID(int votingID) {
		this.votingID = votingID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getJuryCount() {
		return juryCount;
	}
	public void setJuryCount(int juryCount) {
		this.juryCount = juryCount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getSliderMaxValue() {
		return sliderMaxValue;
	}
	public void setSliderMaxValue(int sliderMaxValue) {
		this.sliderMaxValue = sliderMaxValue;
	}
	public int getRound() {
		return round;
	}
	public void setRound(int round) {
		this.round = round;
	}
	public int getEventIDFK() {
		return eventIDFK;
	}
	public void setEventIDFK(int eventIDFK) {
		this.eventIDFK = eventIDFK;
	}
	public String getArithmeticMode() {
		return arithmeticMode;
	}
	public void setArithmeticMode(String arithmeticMode) {
		this.arithmeticMode = arithmeticMode;
	}
	public String getVotingStarted() {
		return votingStarted;
	}
	public void setVotingStarted(String votingStarted) {
		this.votingStarted = votingStarted;
	}
	public String getVotingDuration() {
		return votingDuration;
	}
	public void setVotingDuration(String votingDuration) {
		this.votingDuration = votingDuration;
	}
	public String getPresentationMinTime() {
		return presentationMinTime;
	}
	public void setPresentationMinTime(String presentationMinTime) {
		this.presentationMinTime = presentationMinTime;
	}
	public String getPresentationMaxTime() {
		return presentationMaxTime;
	}
	public void setPresentationMaxTime(String presentationMaxTime) {
		this.presentationMaxTime = presentationMaxTime;
	}
	public String getPresentationEnded() {
		return presentationEnded;
	}
	public void setPresentationEnded(String presentationEnded) {
		this.presentationEnded = presentationEnded;
	}
	public String getPresentationStarted() {
		return presentationStarted;
	}
	public void setPresentationStarted(String presentationStarted) {
		this.presentationStarted = presentationStarted;
	}
	public int getInTimeScoreWeight() {
		return inTimeScoreWeight;
	}
	public void setInTimeScoreWeight(int inTimeScoreWeight) {
		this.inTimeScoreWeight = inTimeScoreWeight;
	}
	public String getVotingDate() {
		return votingDate;
	}
	public void setVotingDate(String date) {
		this.votingDate = date;
	}

}
