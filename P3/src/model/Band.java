package model;


public class Band 
{
	public Integer id;
	public String name;
	public String city;
	public String genre;
	public String bio;
	public String weblink;
	public String email;


	public Band(int id, String name, String city, String genre, String bio, String weblink, String email) {
		this.id = id;
		this.name = name;
		this.city = city;
		this.genre = genre;
		this.bio = bio;
		this.weblink = weblink;
		this.email = email;
	}
	public Band(String name, String city, String genre, String bio, String weblink, String email) {
		this.id = -1;	// for insertions, when the ID is not set yet
		this.name = name;
		this.city = city;
		this.genre = genre;
		this.bio = bio;
		this.weblink = weblink;
		this.email = email;
	}


	public String getName(){return name;}
	public String getLocation(){return city;}


}
