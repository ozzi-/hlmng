package hlmng.resource;

import hlmng.dao.GenDaoLoader;
import hlmng.model.Vote;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;



@Path("/vote")
public class VoteResource  {
	
    @Context
    private UriInfo uriInfo;
	@Context
	private Request request;
	@Context
	private String id;
	@Context 
	private HttpServletResponse response;

	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object> getVote() {
		return GenDaoLoader.instance.getVoteDao().listElements();
	}

	
	@GET
	@Path("count")
	@Produces(MediaType.TEXT_PLAIN)
	public int getCount() {
		return GenDaoLoader.instance.getVoteDao().listElements().size();
	}
	
	@GET
	@Path("{id}")
	public Vote getVote(@PathParam("id") String id,
			@Context HttpHeaders headers,
			@Context HttpServletResponse servletResponse) throws IOException{
		Object obj =  GenDaoLoader.instance.getVoteDao().getElement(id);
		ResourceHelper.sendErrorIfNull(obj,response);
		Vote evt=(Vote) obj;
		return evt;	
	}

	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putVote(Vote element,@PathParam("id") String id) {
		Response res;
		if (GenDaoLoader.instance.getVoteDao().getElement(id)!=null){
			GenDaoLoader.instance.getVoteDao().updateElement(element, id);
			res = Response.accepted().build();
		}else{
			int insertedID = GenDaoLoader.instance.getVoteDao().addElement(element);
			res= ResourceHelper.returnOkOrErrorResponse(!(insertedID==-1));
		}
		return res;	
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response newVote(Vote element) throws IOException {
		int insertedID = GenDaoLoader.instance.getVoteDao().addElement(element);
		return ResourceHelper.returnOkOrErrorResponse(!(insertedID==-1));
	}

}

