package hlmng.model;

public class Media {

	private int mediaID;
	private String type;
	private String link;
	
	public Media(){
		
	}
	public Media(int mediaID, String type, String link){
		this.setMediaID(mediaID);
		this.setType(type);
		this.setLink(link);
	}
	public Media(String type, String link){
		this.setType(type);
		this.setLink(link);
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public int getMediaID() {
		return mediaID;
	}
	public void setMediaID(int mediaID) {
		this.mediaID = mediaID;
	}
	
}
