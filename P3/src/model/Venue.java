package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Venue 
{
	static Integer id;
	static String name;
	static String city;
	static String type;
	static String description;
	static String weblink;
	static String email;
	static String address;
	
	public void setVenue(int x) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
//		System.out.println("... fetching venue ...");
		try {
//   		System.out.println("Registering Driver");
            DriverManager.registerDriver ( new org.postgresql.Driver() ) ;
//            System.out.println("Successful Registration");
        } 
            catch (Exception cnfe){
            System.out.println("Class not found");
            }
		Connection conn = DriverManager.getConnection("jdbc:postgresql://comp421.cs.mcgill.ca:5432/cs421","cs421g36", "LookVibrant");
		
		id = x; 
		Statement stmnt = conn.createStatement();
		ResultSet venueattributes = stmnt.executeQuery("SELECT * FROM Venue WHERE venueid = "+ id);
		venueattributes.next();
		name = venueattributes.getString("venuename");
		city = venueattributes.getString("city");
		address = venueattributes.getString("address");
		type = venueattributes.getString("type");
		description = venueattributes.getString("description");
		weblink = venueattributes.getString("weblink"); 
		email = venueattributes.getString("email"); 
//		System.out.println("... fetched ...");
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
	public String getAddress() {
		return address;
	}
	public String getType() {
		return type;
	}
	public String getDescription() {
		return description;
	}
	public String getWeblink() {
		return weblink;
	}
	public String getEmail() {
		return email;
	}
}
