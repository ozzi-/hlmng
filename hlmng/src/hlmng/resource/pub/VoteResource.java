package hlmng.resource.pub;

import hlmng.auth.AuthChecker;
import hlmng.auth.AuthCredential;
import hlmng.auth.AuthResult;
import hlmng.dao.GenDao;
import hlmng.dao.GenDaoLoader;
import hlmng.dao.VoteDao;
import hlmng.model.ModelHelper;
import hlmng.model.Slider;
import hlmng.model.User;
import hlmng.model.Vote;
import hlmng.model.Voting;
import hlmng.resource.Resource;
import hlmng.resource.TimeHelper;

import java.io.IOException;
import java.util.logging.Level;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import log.Log;
import settings.HLMNGSettings;
import settings.HTTPCodes;



@Path(HLMNGSettings.pubURL+"/vote")
public class VoteResource extends Resource  {
		
	private GenDao voteDao = GenDaoLoader.instance.getVoteDao();


	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Object postVote(Vote element) throws IOException {
		
		String authorizationHeader = headers.getHeaderString("Authorization");
		if(authorizationHeader==null){
			return Response.status(401).build();
		} 	

		AuthCredential authCredential = AuthChecker.decodeBasicAuthB64(authorizationHeader);
		
		User user = GenDaoLoader.instance.getUserDao().getUserByNameAndDeviceID(authCredential.getUsername(), authCredential.getSecret());
		if(user==null){
			Log.addEntry(Level.WARNING, "User tried to vote with wrong credentials. "+ModelHelper.valuestoString(element));
			return Response.status(HTTPCodes.forbidden).build();
		}
		if( user.getUserID()!=element.getUserIDFK()){
			
			Log.addEntry(Level.WARNING, "User tried to vote as somebody else . UserID: "+user.getUserID()+" as "
										+element.getUserIDFK()
										+". "+ModelHelper.valuestoString(element));
			return Response.status(HTTPCodes.forbidden).build();
		}
		
		Slider slider = (Slider) GenDaoLoader.instance.getSliderDao().getElement(element.getSliderIDFK());
		if(slider==null){
			return Response.status(HTTPCodes.badRequest).build();
		}
		Voting voting = (Voting) GenDaoLoader.instance.getVotingDao().getElement(slider.getVotingIDFK());
		if(voting==null){
			return Response.status(HTTPCodes.badRequest).build();
		}
		
		boolean isJury = false;
		
		String qrHeader = headers.getHeaderString("X-QRCode"); 
		if(qrHeader!=null){
			AuthResult qrAuthRes = AuthChecker.checkQRCodeAuthorization(qrHeader,voting.getEventIDFK(),user,"jury",TimeHelper.getCurrentDateTime());
			if(qrAuthRes.isAuthorized()){
				isJury=true;
			}else{
				return Response.status(qrAuthRes.getResponseCode()).build();
			}
		}
		
		if(!isJury && element.isIsJury()){
			Log.addEntry(Level.WARNING, "User tried to vote as jury but has no qr code or a invalid qr code. UserID:"+user.getUserID());
			return Response.status(HTTPCodes.forbidden).build();
		}
		element.setIsJury(isJury);
		
		if(voting.getStatus().equals("voting")){
			boolean userHasVoted=((VoteDao)voteDao).userVotedSliderBefore(element.getUserIDFK(), element.getSliderIDFK());
			if(!userHasVoted){
				Log.addEntry(Level.INFO, "User voted successfully. UserID:"+user.getUserID()+" is jury = "+isJury);
				return postResource(voteDao, element); 			
			}else{			
				Log.addEntry(Level.WARNING, "User tried to vote twice. UserID:"+user.getUserID()+". "+ModelHelper.valuestoString(element));
				return Response.status(HTTPCodes.locked).build();
			}			
		}else{
			Log.addEntry(Level.INFO, "User treid to vote while voting status wasn't 'voting'. "+ModelHelper.valuestoString(element));
			return Response.status(HTTPCodes.failedDependency).build();
		}
	}
}

