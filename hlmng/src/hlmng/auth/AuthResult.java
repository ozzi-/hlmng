package hlmng.auth;

/**
 * POD: AuthResult consists of a boolean stating if the user is 
 * authorized and a http error code according to what went wrong.
 */
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
