package hlmng.model;

public class Presentation {
	private int presentationID;
	private String name;
	private String teamName;
	private int challengeIDFK;
	public Presentation(){

	}
	public Presentation(int presentationID, String name, String teamName, int challengeIDFK){
		this.presentationID=presentationID;
		this.name=name;
		this.teamName=teamName;
		this.challengeIDFK=challengeIDFK;
	}
	public Presentation(String name, String teamName, int challengeIDFK){
		this.name=name;
		this.teamName=teamName;
		this.challengeIDFK=challengeIDFK;
	}
	public int getPresentationID() {
		return presentationID;
	}
	public void setPresentationID(int presentationID) {
		this.presentationID = presentationID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public int getChallengeIDFK() {
		return challengeIDFK;
	}
	public void setChallengeIDFK(int challengeIDFK) {
		this.challengeIDFK = challengeIDFK;
	}
}
