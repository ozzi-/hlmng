package hlmng.auth;

/**
 * 
 * AuthCredential is a container for a username and the according password
 *
 */
public class AuthCredential{
	private String username;
	private String password;

	public AuthCredential(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	/** 
	 * Compare to accounts if username and pw is the same
	 * @param the other AuthCredential object you wish to compare
	 * @return true if same or false if something is different
	 */
	public boolean equals(AuthCredential other){
		if(this.getUsername().equals(other.getUsername()) && this.getPassword().equals(other.getPassword())){
			return true;
		}
		return false;
	}
}