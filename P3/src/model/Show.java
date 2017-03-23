package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Show 
{
	static Integer id;
	static String name;
	static String city;
	static String date;
	static String time;
	static String weblink;
//	static String bookeremail;

	// add weblink and email to show attributes?
	public void setShow(int x) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
	//	System.out.println("... fetching show ...");
		try {
    //		System.out.println("Registering Driver");
            DriverManager.registerDriver ( new org.postgresql.Driver() ) ;
    //        System.out.println("Successful Registration");
        } 
            catch (Exception cnfe){
            System.out.println("Class not found");
            }
		Connection conn = DriverManager.getConnection("jdbc:postgresql://comp421.cs.mcgill.ca:5432/cs421","cs421g36", "LookVibrant");
		
		id = x;
		Statement stmnt = conn.createStatement();
		ResultSet showattributes = stmnt.executeQuery("SELECT * FROM Show WHERE showid = " + id);
		showattributes.next();
		name = showattributes.getString("showname");
		city = showattributes.getString("city");
		date = showattributes.getString("showdate");
		time = showattributes.getString("showtime");
		// HOW TO INVOLVE TOUR ID?
		weblink = showattributes.getString("weblink"); 
		// bookeremail = ... new query with user accounting for nulls
		
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
	public String getDate() {
		return date;
	}
	public String getTime() {
		return time;
	}
	public String getWeblink() {
		return weblink;
	}
//	public String getEmail() {
//		return email;
//	}
}