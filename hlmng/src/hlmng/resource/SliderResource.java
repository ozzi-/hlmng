package hlmng.resource;

import hlmng.dao.GenDaoLoader;
import hlmng.model.Slider;

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



@Path("/slider")
public class SliderResource  {
	
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
	public List<Object> getSlider() {
		return GenDaoLoader.instance.getSliderDao().listElements();
	}

	
	@GET
	@Path("count")
	@Produces(MediaType.TEXT_PLAIN)
	public int getCount() {
		return GenDaoLoader.instance.getSliderDao().listElements().size();
	}
	
	@GET
	@Path("{id}")
	public Slider getSlider(@PathParam("id") String id,
			@Context HttpHeaders headers,
			@Context HttpServletResponse servletResponse) throws IOException{
		Object obj =  GenDaoLoader.instance.getSliderDao().getElement(id);
		ResourceHelper.sendErrorIfNull(obj,response);
		Slider evt=(Slider) obj;
		return evt;	
	}

	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putSlider(Slider element,@PathParam("id") String id) {
		Response res;
		if (GenDaoLoader.instance.getSliderDao().getElement(id)!=null){
			GenDaoLoader.instance.getSliderDao().updateElement(element, id);
			res = Response.accepted().build();
		}else{
			int insertedID = GenDaoLoader.instance.getSliderDao().addElement(element);
			res= ResourceHelper.returnOkOrErrorResponse(insertedID==-1?false:true);
		}
		return res;	
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response newSlider(Slider element) throws IOException {
		int insertedID = GenDaoLoader.instance.getSliderDao().addElement(element);
		return ResourceHelper.returnOkOrErrorResponse(insertedID==-1?false:true);
	}

}

