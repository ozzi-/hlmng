package hlmng.model;

public class Speaker {
	private int speakerID;
	private String name;
	private String title;
	private String description;
	private int mediaIDFK;
	
	public Speaker(){
		
	}
	public Speaker(int speakerID, String name, String title, String description, int mediaIDFK){
		this.speakerID=speakerID;
		this.name=name;
		this.title=title;
		this.description=description;
		this.mediaIDFK=mediaIDFK;
	}
	public Speaker(String name, String title, String description, int mediaIDFK){
		this.name=name;
		this.title=title;
		this.description=description;
		this.mediaIDFK=mediaIDFK;
	}
		
	public int getSpeakerID() {
		return speakerID;
	}
	public void setSpeakerID(int speakerID) {
		this.speakerID = speakerID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getMediaIDFK() {
		return mediaIDFK;
	}
	public void setMediaIDFK(int mediaIDFK) {
		this.mediaIDFK = mediaIDFK;
	}

}
