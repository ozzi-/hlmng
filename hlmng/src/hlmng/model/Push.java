package hlmng.model;

public class Push {
	private int pushID;
	private String title;
	private String author;
	private String sent;
	private int receivedCounter;
	public Push(){
		
	}
	public Push(int pushID, String title, String text, String author, String sent, int receivedCounter ){
		this.pushID=pushID;
		this.title=title;
		this.author=author;
		this.sent=sent;
		this.receivedCounter=receivedCounter;
	}
	public Push(String title, String text, String author, String sent, int receivedCounter ){
		this.title=title;
		this.author=author;
		this.sent=sent;
		this.receivedCounter=receivedCounter;
	}
	public int getPushID() {
		return pushID;
	}
	public void setPushID(int pushID) {
		this.pushID = pushID;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getSent() {
		return sent;
	}
	public void setSent(String sent) {
		this.sent = sent;
	}
	public int getReceivedCounter() {
		return receivedCounter;
	}
	public void setReceivedCounter(int receivedCounter) {
		this.receivedCounter = receivedCounter;
	}
}
