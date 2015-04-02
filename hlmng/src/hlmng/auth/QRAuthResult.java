package hlmng.auth;

import javax.ws.rs.core.Response;

public class QRAuthResult {
	private boolean authorized;
	private Response response;

	public QRAuthResult(boolean authorized, Response response){
		this.authorized=authorized;
		this.response=response;
	}
	public boolean isAuthorized() {
		return authorized;
	}
	public Response getResponse() {
		return response;
	}
}
