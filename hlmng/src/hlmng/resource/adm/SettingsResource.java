package hlmng.resource.adm;

import hlmng.resource.Resource;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONObject;

import settings.HLMNGSettings;


@Path(HLMNGSettings.admURL+"/settings")
public class SettingsResource extends Resource{
	
	@SuppressWarnings("unchecked")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public  String getSettings() throws IOException {
		JSONObject settingsJSON = new JSONObject();
		settingsJSON.put("pageId",HLMNGSettings.facebookPageId);
		settingsJSON.put("appId",HLMNGSettings.facebookAppId);
		settingsJSON.put("maxMediaImageSizeMB",HLMNGSettings.maxMediaImageSizeMB);
		settingsJSON.put("mediaUploadThumbnailPixel", HLMNGSettings.mediaUploadThumbnailPixel);
		settingsJSON.put("newestSelectLimit", HLMNGSettings.selectLimit);
		return settingsJSON.toJSONString();
	}
	
	
}
