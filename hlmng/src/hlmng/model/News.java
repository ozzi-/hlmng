package hlmng.model;

public class News {

	private int newsID;
	private String title;
	private String text;
	private String author;
	private String media;
	private int mediaIDFK;
	private int eventIDFK;
	
	public News(){
	}
	public News(int newsID,String title, String text, String author, int mediaIDFK, int eventIDFK){
		this.newsID=newsID;
		this.setTitle(title);
		this.text=text;
		this.author=author;
		this.mediaIDFK=mediaIDFK;
		this.eventIDFK=eventIDFK;
	}
	public News(String text, String title, String author, int mediaIDFK, int eventIDFK){
		this.setTitle(title);
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMedia() {
		return media;
	}
	public void setMedia(String media) {
		this.media = media;
	}
}
