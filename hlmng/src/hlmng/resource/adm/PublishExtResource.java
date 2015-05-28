package hlmng.resource.adm;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.Media;
import facebook4j.PhotoUpdate;
import facebook4j.auth.AccessToken;
import hlmng.dao.GenDao;
import hlmng.dao.GenDaoLoader;
import hlmng.model.AccessTokenFB;
import hlmng.model.Social;
import hlmng.model.SocialPublish;
import hlmng.resource.Resource;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import log.Log;
import settings.HLMNGSettings;
import settings.HTTPCodes;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

@Path(HLMNGSettings.admURL+"/publish")
public class PublishExtResource  extends Resource{

	public static AccessToken accessTokenFB=null;
	private static GenDao mediaDao =GenDaoLoader.instance.getMediaDao();
	private static GenDao socialPublishDao = GenDaoLoader.instance.getSocialPublishDao();
	
	@POST
	@Path("/twitter")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postToTwitter(Social social) throws TwitterException, IOException {
	    Twitter twitter = TwitterFactory.getSingleton();
	    
		String strng = social.getAuthorName()+": "+social.getText();
		if(strng.length()>140){
		    response.sendError(HTTPCodes.requestEntityTooLarge);
	    }
		
        StatusUpdate status = new StatusUpdate(strng);

		if(!social.getMedia().equals("")){
			hlmng.model.Media mediaFile = (hlmng.model.Media) mediaDao.getElement(social.getMediaIDFK());
			status.setMedia(new File(HLMNGSettings.mediaFileRootDir+mediaFile.getLink()));
		}
		Status statusRespone = twitter.updateStatus(status);
		String twitterLink= "https://twitter.com/" + statusRespone.getUser().getScreenName()+ "/status/" + statusRespone.getId();
		
		socialPublishDao.addElement(new SocialPublish("Twitter", twitterLink, social.getSocialID()));
		
		return Response.accepted().build();
	}
	
	@POST
	@Path("/facebook/updatetoken")
	@Consumes(MediaType.APPLICATION_JSON)
	public void updateAccessToken(AccessTokenFB acToFb) throws FacebookException, MalformedURLException {
		accessTokenFB= acToFb.createToken();
		Log.addEntry(Level.INFO,"Updating Facebook Access Token"); 
	}
	
	@POST
	@Path("/facebook")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postToFacebook(Social social) throws FacebookException, IOException {

		Facebook facebook = new FacebookFactory().getInstance();
		if(accessTokenFB==null){
		    response.sendError(HTTPCodes.failedDependency);
			Log.addEntry(Level.WARNING,"Couldn't post to Facebook due to a missing access token, make sure the frontend loads the social list page first"); 
		    return null;
		}
		facebook.setOAuthAccessToken(accessTokenFB);
		String postID;
		if(social.getMedia().equals("")){
			postID = facebook.postStatusMessage(social.getAuthorName()+": "+social.getText());    
			postID= postID.substring(postID.lastIndexOf("_") + 1);
		}else{
			hlmng.model.Media mediaFile = (hlmng.model.Media) mediaDao.getElement(social.getMediaIDFK());
			Media mediaFB = new Media(new File(HLMNGSettings.mediaFileRootDir+mediaFile.getLink()));
			
			PhotoUpdate update = new PhotoUpdate(mediaFB);
			update.message(social.getAuthorName()+": "+social.getText());
			postID = facebook.postPhoto(update);    			
		}

		String fbLink= "https://facebook.com/permalink.php?story_fbid="+postID+"&id="+HLMNGSettings.facebookPageId;
		
		socialPublishDao.addElement(new SocialPublish("Facebook", fbLink, social.getSocialID()));
		return Response.accepted().build();
		
	}
	

}
