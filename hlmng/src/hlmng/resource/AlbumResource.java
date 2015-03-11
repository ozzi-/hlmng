package hlmng.resource;

import hlmng.dao.AlbumDao;
import hlmng.model.Album;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;


public class AlbumResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	String id;

	public AlbumResource(UriInfo uriInfo, Request request, String id) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.id = id;
	}

	// Application integration
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Album getAlbum() {
		Album album = AlbumDao.instance.getAlbum(Integer.parseInt(id));
		if (album == null)
			throw new RuntimeException("Get: Album with " + id + " not found");
		return album;
	}

	// for the browser
	@GET
	@Produces(MediaType.TEXT_XML)
	public Album getAlbumHTML() {
		Album album = AlbumDao.instance.getAlbum(Integer.parseInt(id));
		if (album == null)
			throw new RuntimeException("Get: Album with " + id + " not found");
		return album;
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putAlbum(Album album) {
		return putAndGetResponse(album); 	
	}

	@DELETE
	public Response deleteAlbum() {
		Response res;
		boolean c = AlbumDao.instance.deleteAlbum(Integer.parseInt((id)));
		if (!c){
			res = Response.notModified("ID not found -> couldn't delete it").build();
		}else{
			res = Response.ok().build();
		}
		return res;
	}

	private Response putAndGetResponse(Album album) {
		Response res;
		if (AlbumDao.instance.getAlbum(Integer.parseInt(id))!=null) {
			AlbumDao.instance.updateAlbum(album);
			res = Response.accepted().build();
		}else{
			AlbumDao.instance.addAlbum(album);
			res = Response.created(uriInfo.getAbsolutePath()).build();	
		}
		return res;
	}

}