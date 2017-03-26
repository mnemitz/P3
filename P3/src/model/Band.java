package model;


public class Band 
{
	Integer id;
	String name;
	String city;
	String genre;
	String bio;
	String weblink;
	String email;
	Show[] shows;

	public Band(int id, String name, String city, String genre, String bio, String weblink, String email) {
		this.id = id;
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
