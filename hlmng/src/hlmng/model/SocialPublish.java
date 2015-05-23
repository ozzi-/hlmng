package hlmng.model;

public class SocialPublish {
	private int socialPublishID;
	private String publisher;
	private String publishedLink;
	private int socialIDFK;
	
	public SocialPublish(){
	}
	public SocialPublish(int socialPublishID, String publisher, String publishedLink, int socialIDFK){
		this.socialPublishID=socialPublishID;
		this.publisher=publisher;
		this.publishedLink=publishedLink;
		this.socialIDFK=socialIDFK;
	}
	
	public SocialPublish(String publisher, String publishedLink, int socialIDFK){
		this.publisher=publisher;
		this.publishedLink=publishedLink;
		this.socialIDFK=socialIDFK;
	}	
	
	public int getSocialPublishID() {
		return socialPublishID;
	}
	public void setSocialPublishID(int socialPublishID) {
		this.socialPublishID = socialPublishID;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public String getPublishedLink() {
		return publishedLink;
	}
	public void setPublishedLink(String publishedLink) {
		this.publishedLink = publishedLink;
	}
	public int getSocialIDFK() {
		return socialIDFK;
	}
	public void setSocialIDFK(int socialIDFK) {
		this.socialIDFK = socialIDFK;
	}
	
}
