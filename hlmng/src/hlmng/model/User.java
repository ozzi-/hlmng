package hlmng.model;

public class User{
	private String name;
	private String deviceID;
	private String regID;
	private int userID;
	
	// CTOR for form
	public User(String name, String deviceID, String regID){
		this.name=name;
		this.deviceID=deviceID;
		this.regID=regID;
	}
	
	public User(int userID, String name, String deviceID,String regID){
		this.userID=userID;
		this.name=name;
		this.deviceID=deviceID;
		this.regID=regID;
	}

	public String getRegID() {
		return regID;
	}

	public void setRegID(String regID) {
		this.regID = regID;
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
