package hlmng.model;

public class EventRoom {
	
	private int eventRoomID;
	private int eventIDFK;
	private String name;
	private String location;
	
	public EventRoom(){
		
	}
	
	public EventRoom(int eventRoomID, int eventIDFK, String name, String location){
		this.eventRoomID=eventRoomID;
		this.eventIDFK=eventIDFK;
		this.name=name;
		this.location=location;
	}
	
	public EventRoom(int eventIDFK, String name, String location){
		this.eventIDFK=eventIDFK;
		this.name=name;
		this.location=location;
	}
	
	public int getEventRoomID() {
		return eventRoomID;
	}

	public void setEventRoomID(int eventRoomID) {
		this.eventRoomID = eventRoomID;
	}

	public int getEventIDFK() {
		return eventIDFK;
	}

	public void setEventIDFK(int eventIDFK) {
		this.eventIDFK = eventIDFK;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
}
