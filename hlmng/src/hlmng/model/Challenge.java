package hlmng.model;

public class Challenge {
	private int challengeID;
	private String name;
	private String difficulty;
	private String category;
	private int eventIDFK;
	private int mediaIDFK;
	public Challenge(){
		
	}
	public Challenge(int challengeID, String name, String difficulty, String category, int eventIDFK, int mediaIDFK){
		this.challengeID=challengeID;
		this.name=name;
		this.difficulty=difficulty;
		this.category=category;
		this.eventIDFK=eventIDFK;
		this.mediaIDFK=mediaIDFK;
	}
	public Challenge(String name, String difficulty, String category, int eventIDFK, int mediaIDFK){
		this.name=name;
		this.difficulty=difficulty;
		this.category=category;
		this.eventIDFK=eventIDFK;
		this.mediaIDFK=mediaIDFK;
	}

	public int getChallengeID() {
		return challengeID;
	}
	public void setChallengeID(int challengeID) {
		this.challengeID = challengeID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDifficulty() {
		return difficulty;
	}
	public void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public int getEventIDFK() {
		return eventIDFK;
	}
	public void setEventIDFK(int eventIDFK) {
		this.eventIDFK = eventIDFK;
	}
	public int getMediaIDFK() {
		return mediaIDFK;
	}
	public void setMediaIDFK(int mediaIDFK) {
		this.mediaIDFK = mediaIDFK;
	}

}
