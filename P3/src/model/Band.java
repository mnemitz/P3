package model;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Band 
{
	static Integer id;
	static String name;
	static String city;
	static String genre;
	static String bio;
	static String weblink;
	static String email;

	public Band() {

	}

	public Band(int id, String name, String city, String genre, String bio, String weblink, String email) {
		this.id = id;
		this.name = name;
		this.city = city;
		this.genre = genre;
		this.bio = bio;
		this.weblink = weblink;
		this.email = email;
	}

	public void setBand(int x) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		//System.out.println("... fetching band ...");
		try {
    	//	System.out.println("Registering Driver");
            DriverManager.registerDriver ( new org.postgresql.Driver() ) ;
        //    System.out.println("Successful Registration");
        } 
            catch (Exception cnfe){
            System.out.println("Class not found");
            }
		Connection conn = DriverManager.getConnection("jdbc:postgresql://comp421.cs.mcgill.ca:5432/cs421","cs421g36", "LookVibrant");
		
		id = x; 
		Statement stmnt = conn.createStatement();
		ResultSet bandattributes = stmnt.executeQuery("SELECT * FROM Band WHERE bandid = "+ id);
		bandattributes.next();
		name = bandattributes.getString("name");
		city = bandattributes.getString("city");
		genre = bandattributes.getString("genre");
		bio = bandattributes.getString("bio");
		weblink = bandattributes.getString("weblink"); 
		email = bandattributes.getString("email"); 
	
		
		conn.close();
	}
	public Integer getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getCity() {
		return city;
	}
	public String getGenre() {
		return genre;
	}
	public String getBio() {
		return bio;
	}
	public String getWeblink() {
		return weblink;
	}
	public String getEmail() {
		return email;
	}
}
