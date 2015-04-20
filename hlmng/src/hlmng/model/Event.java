package hlmng.model;

public class Event {

	private int eventID;
	private String name;
	private String description;
	private String startDate;
	private String endDate;
	private boolean active;

	public Event(){
		
	}
	public Event(int eventID, String name, String description, String startDate, String endDate,boolean active){
		this.eventID=eventID;
		this.name=name;
		this.description=description;
		this.startDate=startDate;
		this.endDate=endDate;
		this.active=active;
	}
	
	public Event(String name, String description, String startDate, String endDate,boolean active){
		this.name=name;
		this.description=description;
		this.startDate=startDate;
		this.endDate=endDate;
		this.active=active;
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
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}

}
