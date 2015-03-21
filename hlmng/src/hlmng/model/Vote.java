package hlmng.model;

public class Vote {
	private int voteID;
	private int score;
	private int sliderIDFK;
	private int userIDFK;
	public Vote(){
		
	}
	public Vote(int voteID, int score, int sliderIDFK, int userIDFK){
		this.voteID=voteID;
		this.score=score;
		this.sliderIDFK=sliderIDFK;
		this.userIDFK=userIDFK;
	}
	public Vote(int score, int sliderIDFK, int userIDFK){
		this.score=score;
		this.sliderIDFK=sliderIDFK;
		this.userIDFK=userIDFK;
	}
	public int getVoteID() {
		return voteID;
	}
	public void setVoteID(int voteID) {
		this.voteID = voteID;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int getSliderIDFK() {
		return sliderIDFK;
	}
	public void setSliderIDFK(int sliderIDFK) {
		this.sliderIDFK = sliderIDFK;
	}
	public int getUserIDFK() {
		return userIDFK;
	}
	public void setUserIDFK(int userIDFK) {
		this.userIDFK = userIDFK;
	}
}
