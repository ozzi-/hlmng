package hlmng.album;


import hlmng.Log;
import hlmng.auth.AuthChecker;
import hlmng.dao.AlbumDao;
import hlmng.model.Album;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;


// Will map the resource to the URL albums
@Path("/albums")
public class AlbumsResource {

  // Allows to insert contextual objects into the class,
  // e.g. ServletContext, Request, Response, UriInfo
  @Context
  UriInfo uriInfo;
  @Context
  Request request;

  // Return the list of albums to the user in the browser
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<Album> getAlbumsBrowser() {
    List<Album> albums = new ArrayList<Album>();
    albums.addAll(AlbumDao.instance.listAlbums());
    return albums;
  }

  // Return the list of albums for applications
//  @GET
//  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
//  public List<Album> getAlbums() {
//    List<Album> albums = new ArrayList<Album>();
//    albums.addAll(AlbumDao.instance.listAlbums());
//    return albums;
//  }

  // returns the number of albums
  // Use http://localhost:8080/de.vogella.jersey.todo/rest/albums/count
  // to get the total number of records
  @GET
  @Path("count")
  @Produces(MediaType.TEXT_PLAIN)
  public int getCount() {
    return AlbumDao.instance.listAlbums().size();
  }

  @POST
  @Produces(MediaType.TEXT_HTML)
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public void newAlbum(@FormParam("id") int id,
      @FormParam("album") String album,
      @FormParam("artist") String artist,
      @FormParam("year") int year,
      @Context HttpServletResponse servletResponse) throws IOException {
    Album addAlbum = new Album(id, album,artist,year);
    AlbumDao.instance.addAlbum(addAlbum);
    servletResponse.sendRedirect("../createalbum.html");
  }

  // Defines that the next path parameter after albums is
  // treated as a parameter and passed to the AlbumResources
  // Allows to type http://localhost:8080/de.vogella.jersey.todo/rest/albums/1
  // 1 will be treaded as parameter todo and passed to AlbumResource
  @Path("{album}")
  public AlbumResource getAlbum(@PathParam("album") String id, @Context HttpHeaders headers, @Context HttpServletResponse servletResponse) {
	  AuthChecker.check(headers, servletResponse);
	  Log.addEntry(Level.INFO, "ALBUM RESSOURCE "+id+" GET");
    return new AlbumResource(uriInfo, request, id);
  }

} 