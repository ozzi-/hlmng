package hlmng.model;

public class Voting {
	private int votingID;
	private String name;
	private int juryCount;
	private String status;
	private int sliderMaxValue;
	private int round;
	private String presentationIDFK;

	public Voting(){
	
	}
	public Voting(int votingID, String name, int juryCount, String status, int sliderMaxValue, String arethmeticMode, int round, String presentationIDFK){
		this.votingID=votingID;
		this.name=name;
		this.juryCount=juryCount;
		this.status=status;
		this.sliderMaxValue=sliderMaxValue;
		this.round=round;
		this.presentationIDFK=presentationIDFK;
	}
	public Voting(String name, int juryCount, String status, int sliderMaxValue, String arethmeticMode, int round, String presentationIDFK){
		this.name=name;
		this.juryCount=juryCount;
		this.status=status;
		this.sliderMaxValue=sliderMaxValue;
		this.round=round;
		this.presentationIDFK=presentationIDFK;
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
	public String getPresentationIDFK() {
		return presentationIDFK;
	}
	public void setPresentationIDFK(String presentationIDFK) {
		this.presentationIDFK = presentationIDFK;
	}

}
