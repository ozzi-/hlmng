package hlmng.auth;

/**
 * 
 * AuthCredential is a container for a username and the according secret
 *
 */
public class AuthCredential{
	private String username;
	private String secret;

	public AuthCredential(String username, String secret) {
		this.username = username;
		this.secret = secret;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}
	
	/** 
	 * Compare to accounts if username and secret is the same
	 * @param the other AuthCredential object you wish to compare
	 * @return true if same or false if something is different
	 */
	public boolean equals(AuthCredential other){
		if(this.getUsername().equals(other.getUsername()) && this.getSecret().equals(other.getSecret())){
			return true;
		}
		return false;
	}
}
