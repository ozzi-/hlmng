package hlmng.model;

public class EventItem {
	
	private int eventItemID;
	private String name;
	private String description;
	private String date;
	private String startTime;
	private String endTime;
	private int roomIDFK;
	private int eventIDFK;
	private int speakerIDFK;
	public EventItem(){
	}
	
	public EventItem(int eventItemID, String name, String description, String date, String startTime, String endTime, int roomIDFK, int eventIDFK,int speakerIDFK){
		this.eventItemID=eventItemID;
		this.name=name;
		this.setDescription(description);
		this.date=date;
		this.startTime = startTime;
		this.endTime=endTime;
		this.roomIDFK=roomIDFK;
		this.eventIDFK=eventIDFK;
		this.speakerIDFK=speakerIDFK;
	}
	
	public EventItem(String name, String description, String date, String startTime, String endTime, int roomIDFK, int eventIDFK, int speakerIDFK){
		this.name=name;
		this.setDescription(description);
		this.date=date;
		this.startTime = startTime;
		this.endTime=endTime;
		this.roomIDFK=roomIDFK;
		this.eventIDFK=eventIDFK;
		this.speakerIDFK=speakerIDFK;
	}
	
	
	public int getEventItemID() {
		return eventItemID;
	}

	public void setEventItemID(int eventItemID) {
		this.eventItemID = eventItemID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public int getRoomIDFK() {
		return roomIDFK;
	}

	public void setRoomIDFK(int roomIDFK) {
		this.roomIDFK = roomIDFK;
	}

	public int getEventIDFK() {
		return eventIDFK;
	}

	public void setEventIDFK(int eventIDFK) {
		this.eventIDFK = eventIDFK;
	}
	
	public int getSpeakerIDFK() {
		return speakerIDFK;
	}

	public void setSpeakerIDFK(int speakerIDFK) {
		this.speakerIDFK = speakerIDFK;
	}


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
