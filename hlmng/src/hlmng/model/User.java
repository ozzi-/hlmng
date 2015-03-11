package hlmng.model;

public class User{
	private String name;
	private String deviceID;
	private int userID;
	
	public User(String name, String deviceID){
		this.name=name;
		this.deviceID=deviceID;
	}
	
	public User(int userID, String name, String deviceID){
		this.userID=userID;
		this.name=name;
		this.deviceID=deviceID;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

}
