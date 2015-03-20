package hlmng.model;

public class News {

	private int newsID;
	private String text;
	private String author;
	private int mediaIDFK;
	private int eventIDFK;
	
	public News(){
	}
	public News(int newsID, String text, String author, int mediaIDFK, int eventIDFK){
		this.newsID=newsID;
		this.text=text;
		this.author=author;
		this.mediaIDFK=mediaIDFK;
		this.eventIDFK=eventIDFK;
	}
	public News(String text, String author, int mediaIDFK, int eventIDFK){
		this.text=text;
		this.author=author;
		this.mediaIDFK=mediaIDFK;
		this.eventIDFK=eventIDFK;
	}
	
	public int getNewsID() {
		return newsID;
	}
	public void setNewsID(int newsID) {
		this.newsID = newsID;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
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
}
