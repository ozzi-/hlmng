package hlmng.resource.adm;

import java.net.MalformedURLException;
import java.net.URL;

import facebook4j.Account;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.PostUpdate;
import facebook4j.ResponseList;
import facebook4j.auth.AccessToken;
import facebook4j.auth.OAuthAuthorization;
import facebook4j.conf.Configuration;
import hlmng.model.Social;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONObject;

import settings.HLMNGSettings;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

@Path(HLMNGSettings.admURL+"/publish")
public class PublishExtResource {

	
	@SuppressWarnings("unchecked")
	@POST
	@Path("/twitter")
	@Consumes(MediaType.APPLICATION_JSON)
	public JSONObject postToTwitter(Social social) throws TwitterException {
	    Twitter twitter = TwitterFactory.getSingleton();
	    
		String strng = (social.getMedia()==null)?social.getAuthorName()+": "+social.getText():social.getAuthorName()+": "+social.getText()+"\n"+social.getMedia();
		if(strng.length()>140){
			return null; 
	    }
		Status status = twitter.updateStatus(strng);
		String url= "https://twitter.com/" + status.getUser().getScreenName()+ "/status/" + status.getId();
		JSONObject ret = new JSONObject();
		ret.put("publishedlink",url);
		ret.put("publisher","Twitter");
		return ret;
	}
	
	
	@SuppressWarnings("unchecked")
	@POST
	@Path("/facebook")
	@Consumes(MediaType.APPLICATION_JSON)
	public JSONObject postToFacebook(Social social) throws FacebookException, MalformedURLException {

		Facebook facebook = new FacebookFactory().getInstance();
		facebook.setOAuthAccessToken(facebook.getOAuthAppAccessToken());
//		AccessToken oauthToken = facebook.getOAuthAppAccessToken();
//		String pageAccessToken="";
//		ResponseList<Account> accounts = facebook.getAccounts();
//		for (Account account : accounts) {
//			System.out.println(account.getName());
//			if(account.getName().equals(HLMNGSettings.facebookAppName)){
//				pageAccessToken = account.getAccessToken();
//			}
//		}
//		System.out.println(pageAccessToken);
//		if(pageAccessToken.equals("")){
//			return null;
//		}
//		AccessToken p = new AccessToken(pageAccessToken);
//		facebook.setOAuthAccessToken(p);
		
		PostUpdate post = new PostUpdate(new URL("http://priklad.sk"))
        .picture(new URL("http://priklad.sk/obrazcok/testik.png"))
        .name("priklad")
        .caption("priklad")
        .message("priklad")
        .description("priklad");
		 facebook.postFeed(post);
	
		 return null;
		
		
	}
	

}
