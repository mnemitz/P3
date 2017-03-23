package model;
import java.sql.*;
import java.math.*;
import java.util.*;

import javafx.scene.*;

/*
 * @TODO:
 * Figure out how the result set of a query can be put into the list view as cleanly as possible.
 * 
 * If searching for Bands, the list view should have a few descriptive columns for the band etc
 * 
 * It seems like we wil need to instantiate a Band object of some kind to keep track of the ID.
 * 
 * That way once the bands appear in the list view, we can assign an event handler to each list item which triggers the stacking of a new pane
 * This new pane needs the specific ID to know what methods to call to DBConnector before it shows itself
 * 
 * So bandSelect should return a Band[] which can subsequently be iterated over to populate the list items
 * 
 * Somehow the ID needs to be passed downward as well. 
 * 
 * OOO!
 * 
 * When iterating through the returned bands:
 * 	1:	Set the ListItem text to match the attributes
 * 	2:	Define the onClick() or whatever handler as an anonymous function based on thebandID of the current band
 * 		i.e. a function that says "open a new bandpage on the stack pane whose ID this this one", and let that pane talk to DBConnector to get what it needs
 * 
 * */



public class DatabaseConnector 
{
	
	public enum BandCols
	{
		BAND_ID, 
		BAND_NAME,
		BAND_EMAIL,
		BAND_CITY,
		BAND_GENRE,
		BAND_BIO,
		BAND_WEBLINK
	}
	
	public enum VenueCols
	{
		VENUE_ID,
		VENUE_NAME,
		VENUE_EMAIL,
		VENUE_CITY,
		VENUE_TYPE,
		VENUE_DESCRIPTION,
		VENUE_WEBLINK
	}
	
	public enum ShowCols
	{
		// TODO columns for show
	}
	

	
	private static Connection CONNECTION = null;
	private String URL = "jdbc:postgresql://comp421.cs.mcgill.ca";
	
	public DatabaseConnector()
	{
		try
		{
			Class.forName("org.postgresql.Driver");
		}
		catch(ClassNotFoundException e)
		{
			System.out.println("Could not locate driver class, make sure a driver is in the build path");
		}
		// From here we can assume the driver was successfully instantiated...
		Properties props = new Properties();
		props.put("user", "cs421g36");
		props.put("password", "LookVibrant");
		
		try
		{
			CONNECTION = DriverManager.getConnection(URL, props);
		}
		catch(SQLException sqle)
		{
			System.out.println(sqle);
		}
		try {
			CONNECTION.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
}