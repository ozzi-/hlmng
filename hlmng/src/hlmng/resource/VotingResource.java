
package hlmng.resource;

import hlmng.dao.GenDao;
import hlmng.dao.GenDaoLoader;
import hlmng.model.Voting;

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



@Path("/voting")
public class VotingResource extends Resource {

	private GenDao votingDao = GenDaoLoader.instance.getVotingDao();

	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object> getVoting() throws IOException {
		return listResource(votingDao, false);
	}
	
	@GET
	@Path("/lastupdate")
	@Produces(MediaType.TEXT_PLAIN)
	public long getLastUpdateTime() throws IOException {
		return votingDao.getLastUpdateTime();
	}
		
	@GET
	@Path("{id}")
	public Voting getVoting(@PathParam("id") int id) throws IOException{
		return (Voting) getResource(votingDao, id);
	}

	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putVoting(Voting element,@PathParam("id") int id) throws IOException {
		return putResource(votingDao, element, id);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Object postVoting(Voting element) throws IOException {
		return postResource(votingDao, element,true);
	}

}

