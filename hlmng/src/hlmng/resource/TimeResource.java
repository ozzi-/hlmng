package hlmng.resource;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;



@Path("/time")
public class TimeResource extends Resource  {	
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	/**
	 * We need this to calculate the time delta between clients and server so voting can be more accurate
	 * @return
	 */
	public String getServerTime() {
		return ResourceHelper.getCurrentTimeMillisecs();
	}
}

