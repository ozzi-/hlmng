package hlmng.model;

import facebook4j.auth.AccessToken;

public class AccessTokenFB {
	private String access_token;
	private String id;
	
	public AccessTokenFB(String access_token, String id){
		this.setAccess_token(access_token);
		this.setId(id) ;
	}
	
	public AccessToken createToken(){
		return new AccessToken(access_token);
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	

}
