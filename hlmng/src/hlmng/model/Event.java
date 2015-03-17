package hlmng.model;

public class Event {

	private int eventID;
	private String name;
	private String description;
	private String start;
	private String end;

	public Event(){
		
	}
	public Event(int eventID, String name, String description, String start, String end){
		this.eventID=eventID;
		this.name=name;
		this.description=description;
		this.start=start;
		this.end=end;
	}
	
	public Event(String name, String description, String start, String end){
		this.name=name;
		this.description=description;
		this.start=start;
		this.end=end;
	}
	
	
	public int getEventID() {
		return eventID;
	}
	public void setEventID(int eventID) {
		this.eventID = eventID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}

}
