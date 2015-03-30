package hlmng.model;

public class Social {
	private int socialID;
	private String text;
	private String status;
	private String media;
	private int userIDFK;
	private int mediaIDFK;
	private int eventIDFK;

	public Social() {

	}

	public Social(int socialID, String text, String status, String media, int userIDFK,
			int mediaIDFK, int eventIDFK) {
		this.socialID = socialID;
		this.text = text;
		this.status = status;
		this.setMedia(media);
		this.userIDFK = userIDFK;
		this.mediaIDFK = mediaIDFK;
		this.eventIDFK = eventIDFK;
	}

	public Social(String text, String status, String media, int userIDFK, int mediaIDFK,
			int eventIDFK) {
		this.text = text;
		this.status = status;
		this.setMedia(media);
		this.userIDFK = userIDFK;
		this.mediaIDFK = mediaIDFK;
		this.eventIDFK = eventIDFK;
	}

	public int getSocialID() {
		return socialID;
	}

	public void setSocialID(int socialID) {
		this.socialID = socialID;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getUserIDFK() {
		return userIDFK;
	}

	public void setUserIDFK(int userIDFK) {
		this.userIDFK = userIDFK;
	}

	public int getMediaIDFK() {
		return mediaIDFK;
	}

	public void setMediaIDFK(int mediaIDFK) {
		this.mediaIDFK = mediaIDFK;
	}

	public int getEventIDFK() {
		return eventIDFK;
	}

	public void setEventIDFK(int eventIDFK) {
		this.eventIDFK = eventIDFK;
	}

	public String getMedia() {
		return media;
	}

	public void setMedia(String media) {
		this.media = media;
	}
}