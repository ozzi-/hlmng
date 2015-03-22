package hlmng.resource;

import hlmng.dao.GenDaoLoader;
import hlmng.model.QrCode;

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



@Path("/qrcode")
public class QrCodeResource  {
	
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
	public List<Object> getQrCode() {
		return GenDaoLoader.instance.getQrCodeDao().listElements();
	}

	
	@GET
	@Path("count")
	@Produces(MediaType.TEXT_PLAIN)
	public int getCount() {
		return GenDaoLoader.instance.getQrCodeDao().listElements().size();
	}


	
	@GET
	@Path("{id}")
	public QrCode getQrCode(@PathParam("id") String id,
			@Context HttpHeaders headers,
			@Context HttpServletResponse servletResponse) throws IOException{
		Object obj =  GenDaoLoader.instance.getQrCodeDao().getElement(id);
		ResourceHelper.sendErrorIfNull(obj,response);
		QrCode evt=(QrCode) obj;
		return evt;

	
	}



	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putQrCode(QrCode element,@PathParam("id") String id) {
		Response res;
		if (GenDaoLoader.instance.getQrCodeDao().getElement(id)!=null){
			GenDaoLoader.instance.getQrCodeDao().updateElement(element, id);
			res = Response.accepted().build();
		}else{
			int insertedID = GenDaoLoader.instance.getQrCodeDao().addElement(element);
			res= ResourceHelper.returnOkOrErrorResponse(insertedID==-1?false:true);
		}
		return res;	
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response newQrCode(QrCode element) throws IOException {
		int insertedID = GenDaoLoader.instance.getQrCodeDao().addElement(element);
		return ResourceHelper.returnOkOrErrorResponse(insertedID==-1?false:true);
	}



}

