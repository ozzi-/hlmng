package hlmng.resource.pub;


import hlmng.resource.Resource;
import hlmng.resource.TimeHelper;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import settings.HLMNGSettings;



@Path(HLMNGSettings.pubURL+"/time")
public class TimeResource extends Resource  {	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	/**
	 * We need this to calculate the time delta between clients and server so voting can be more accurate
	 * @return
	 */
	public String getServerTime() {
		return TimeHelper.getCurrentTimeMillisecs();
	}
}

