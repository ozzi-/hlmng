package hlmng.dao;


import hlmng.model.Album;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DB;

public enum AlbumDao {
  instance;
  
  private DB dbAlbum;
  private String tablename;
  
  private AlbumDao() {
	dbAlbum = new DB("albums");
	tablename = "the_classics";
  }
  
  public void addAlbum(Album album){
	  dbAlbum.doUpdateGetResult("insert into "+tablename+" (id,name,artist,year) values ("+album.getId()+", '"+album.getAlbum()+"' , '"+album.getArtist()+"' ,"+album.getYear()+")");
  }
  
  public void updateAlbum(Album album){
	  dbAlbum.doUpdateGetResult("update "+tablename+" set id="+album.getId()+", name='"+album.getAlbum()+"' , artist='"+album.getArtist()+"' , year="+album.getYear()+" where id="+album.getId());
  }
  
  // RETURNS NULL IF NOT FOUND
  public Album getAlbum(int id){
	  ResultSet rs = dbAlbum.doQueryGetResult("select * from "+tablename+" where id="+id);
	  String album="<error>"; String artist="<error>"; int year=0;
	  try {
		  if (!rs.isBeforeFirst() ) {    
			  return null;
		  } 
		  rs.next();
		  album = rs.getString("name");
		  artist = rs.getString("artist");
		  year = rs.getInt("year");
	  } catch (SQLException e) {
		  System.err.println("Error when trying to build album from SQL Resultset");
		  e.printStackTrace();
	  }
	  return new Album(id,album,artist,year);
  }
  
  public boolean deleteAlbum(int id){
	  int delRet = dbAlbum.doUpdateGetResult("delete from "+tablename+" where id="+id);
	  return ((delRet==1?true:false));
  }
  

  
  public List<Album> listAlbums(){
	  ResultSet rs = dbAlbum.doQueryGetResult("select * from "+tablename);
	  int id=0;String album="<error>"; String artist="<error>"; int year=0;
	  List<Album> albumList= new ArrayList<Album>(); 
	  try {
		  while(rs.next()){
			  id = rs.getInt("id");
			  album = rs.getString("name");
			  artist = rs.getString("artist");
			  year = rs.getInt("year");
			  albumList.add(new Album(id,album,artist,year));
		  }
	  } catch (SQLException e) {
		  System.err.println("Error when trying to build album from SQL Resultset");
		  e.printStackTrace();
	  } 


	  return albumList;
  }
    
  
} 

