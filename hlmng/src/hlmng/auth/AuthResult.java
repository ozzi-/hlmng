package hlmng.auth;


public class AuthResult {
	private boolean authorized;
	private int responseCode;

	public AuthResult(boolean authorized, int responseCode){
		this.authorized=authorized;
		this.responseCode=responseCode;
	}
	public boolean isAuthorized() {
		return authorized;
	}
	public int getResponseCode() {
		return responseCode;
	}
}
