package hlmng.resource.adm;

import hlmng.dao.GenDao;
import hlmng.dao.GenDaoLoader;
import hlmng.model.Vote;
import hlmng.resource.Resource;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import settings.HLMNGSettings;



@Path(HLMNGSettings.admURL+"/vote")
public class VoteResource extends Resource  {
		
	private GenDao voteDao = GenDaoLoader.instance.getVoteDao();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object> getVote() throws IOException {
		return listResource(voteDao, false);			
	}

	@GET
	@Path("/lastupdate")
	@Produces(MediaType.TEXT_PLAIN)
	public long getLastUpdateTime() throws IOException {
		return voteDao.getLastUpdateTime();
	}
	
	@GET
	@Path("{id}")
	public Vote getVote(@PathParam("id") int id) throws IOException{
			return (Vote) getResource(voteDao, id);			
	}
		
	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putVote(Vote element,@PathParam("id") int id) throws IOException {
		return putResource(voteDao, element, id);
	}

}

