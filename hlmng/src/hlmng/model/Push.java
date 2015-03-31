package hlmng.model;

public class Push {
	private int pushID;
	private String title;
	private String author;
	private String date;
	private String time;
	private int receivedCounter;
	private int failedCounter;
	private String text;
	public Push(){
		
	}
	public Push(int pushID, String title, String text, String author ){
		this.pushID=pushID;
		this.title=title;
		this.text=text;
		this.author=author;
	}
	public Push(String title, String text, String author ){
		this.title=title;
		this.text=text;
		this.author=author;
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

	public int getReceivedCounter() {
		return receivedCounter;
	}
	public void setReceivedCounter(int receivedCounter) {
		this.receivedCounter = receivedCounter;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getFailedCounter() {
		return failedCounter;
	}
	public void setFailedCounter(int failedCounter) {
		this.failedCounter = failedCounter;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
}
