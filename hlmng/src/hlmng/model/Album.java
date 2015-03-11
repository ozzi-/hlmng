package hlmng.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Album{
	private int id;
	private String album;
	private String artist;
	private int year;

	public Album() {

	}

	public Album(int id, String album, String artist, int year) {
		this.id = id;
		this.album=album;
		this.artist=artist;
		this.year=year;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

}