package hlmng.model;

public class Vote {
	private int voteID;
	private int score;
	private boolean isJury;
	private int sliderIDFK;
	private int userIDFK;
	public Vote(){
		
	}
	public Vote(int voteID, int score, int isJury, int sliderIDFK, int userIDFK){
		this.voteID=voteID;
		this.setIsJury((isJury==1)?true:false);
		this.score=score;
		this.sliderIDFK=sliderIDFK;
		this.userIDFK=userIDFK;
	}
	public Vote(int score, int isJury, int sliderIDFK, int userIDFK){
		this.score=score;
		this.setIsJury((isJury==1)?true:false);
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
	public boolean isIsJury() {
		return isJury;
	}
	public void setIsJury(boolean isJury) {
		this.isJury = isJury;
	}
}
