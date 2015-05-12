package hlmng.model;

public class Voting {
	private int votingID;
	private String name;
	private int juryCount;
	private String status;
	private int sliderMaxValue;
	private String votingStarted;
	private String votingDuration;
	private String arethmeticMode;
	private int round;
	private int eventIDFK;

	  
	public enum statusEnum {
		pre_presentation,presentation,presentation_end,voting,voting_end
	}
	
	public Voting(){
	
	}
	public Voting(int votingID, String name, int juryCount, String status, int sliderMaxValue, String votingStarted, String votingDuration, String arethmeticMode, int round, int eventIDFK){
		this.votingID=votingID;
		this.name=name;
		this.juryCount=juryCount;
		this.status=status;
		this.sliderMaxValue=sliderMaxValue;
		this.votingStarted=votingStarted;
		this.votingDuration=votingDuration;
		this.arethmeticMode=arethmeticMode;
		this.round=round;
		this.eventIDFK=eventIDFK;
	}
	public Voting(String name, int juryCount, String status, int sliderMaxValue, String votingDuration, String arethmeticMode, int round, int eventIDFK){
		this.name=name;
		this.juryCount=juryCount;
		this.status=status;
		this.sliderMaxValue=sliderMaxValue;
		this.votingDuration=votingDuration;
		this.arethmeticMode=arethmeticMode;
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
	public String getArethmeticMode() {
		return arethmeticMode;
	}
	public void setArethmeticMode(String arethmeticMode) {
		this.arethmeticMode = arethmeticMode;
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

}
