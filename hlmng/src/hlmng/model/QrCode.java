package hlmng.model;

public class QrCode {
	private int qrCodeID;
	private String createdAt;
	private String claimedAt;
	private String payload;
	private String role;
	private int userIDFK;
	private int eventIDFK;
	
	public QrCode(){
		
	}
	public QrCode(int qrCodeID, String createdAt, String claimedAt, String payload, String role, int userIDFK, int eventIDFK){
		this.qrCodeID=qrCodeID;
		this.createdAt=createdAt;
		this.claimedAt=claimedAt;
		this.payload=payload;
		this.role=role;
		this.userIDFK=userIDFK;
		this.eventIDFK=eventIDFK;
	}
	public QrCode(String createdAt, String claimedAt, String payload, String role, int userIDFK, int eventIDFK){
		this.createdAt=createdAt;
		this.claimedAt=claimedAt;
		this.payload=payload;
		this.role=role;
		this.userIDFK=userIDFK;
		this.eventIDFK=eventIDFK;
	}
	public int getQrCodeID() {
		return qrCodeID;
	}
	public void setQrCodeID(int qrCodeID) {
		this.qrCodeID = qrCodeID;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public String getClaimedAt() {
		return claimedAt;
	}
	public void setClaimedAt(String claimedAt) {
		this.claimedAt = claimedAt;
	}
	public String getPayload() {
		return payload;
	}
	public void setPayload(String payload) {
		this.payload = payload;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public int getUserIDFK() {
		return userIDFK;
	}
	public void setUserIDFK(int userIDFK) {
		this.userIDFK = userIDFK;
	}
	public int getEventIDFK() {
		return eventIDFK;
	}
	public void setEventIDFK(int eventIDFK) {
		this.eventIDFK = eventIDFK;
	}	
}
