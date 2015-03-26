package hlmng.resource;

import hlmng.dao.GenDao;
import hlmng.dao.GenDaoLoader;
import hlmng.model.Vote;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;



@Path("/vote")
public class VoteResource extends Resource  {
		
	private GenDao voteDao = GenDaoLoader.instance.getVoteDao();

	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object> getVote() {
		return GenDaoLoader.instance.getVoteDao().listElements();
	}

	
	@GET
	@Path("{id}")
	public Vote getVote(@PathParam("id") String id) throws IOException{
		return (Vote) getResource(voteDao, id);
	}

	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putVote(Vote element,@PathParam("id") String id) {
		return putResource(voteDao, element, id);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response newVote(Vote element) throws IOException {
		return newResource(voteDao, element, false);
	}

}

