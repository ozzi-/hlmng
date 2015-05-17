package hlmng.model;

public class PresentationPause {
	private int presentationpauseID;
	private String start;
	private String stop;
	private int votingIDFK;
	
	public PresentationPause() {
	}
	public PresentationPause(int presentationpauseID, String start, String stop, int votingIDFK){
		this.setPresentationpauseID(presentationpauseID);
		this.setStart(start);
		this.setStop(stop);
		this.setVotingIDFK(votingIDFK);
	}
	public PresentationPause(String start, String stop, int votingIDFK){
		this.setStart(start);
		this.setStop(stop);
		this.setVotingIDFK(votingIDFK);
	}
	public int getPresentationpauseID() {
		return presentationpauseID;
	}
	public void setPresentationpauseID(int presentationpauseID) {
		this.presentationpauseID = presentationpauseID;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getStop() {
		return stop;
	}
	public void setStop(String stop) {
		this.stop = stop;
	}
	public int getVotingIDFK() {
		return votingIDFK;
	}
	public void setVotingIDFK(int votingIDFK) {
		this.votingIDFK = votingIDFK;
	}	
}
