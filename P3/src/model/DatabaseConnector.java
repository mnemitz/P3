package model;
import org.postgresql.util.PSQLException;

import java.sql.*;
import java.math.*;
import java.util.*;



/*Singleton implementation of DatabaseConnector
*
* The idea is this is the one class which executes queries. It has a connection to the database so we don't make many
* */

public class DatabaseConnector 
{
	public enum BandCol
	{
		B_ID,
		B_NAME,
		B_EMAIL,
		B_CITY,
		B_GENRE,
		B_BIO,
		B_WEBLINK,
	}

	public enum VenueCol
	{
		V_ID,
		V_NAME,
		V_EMAIL,
		V_CITY,
		V_TYPE,
		V_WEBLINK,
	}
	private static Connection CONNECTION = null;
	private static String URL = "jdbc:postgresql://comp421.cs.mcgill.ca:5432/cs421";
	private static String dbUsr = "cs421g36";
	private static String dbUsrPwd = "LookVibrant";
	private static DatabaseConnector instance = null;
	private static String CurrentUserEmail = null;
	private static String CurrentUserName = null;



	public static DatabaseConnector getInstance()
	{
		if(instance == null) {
			instance = new DatabaseConnector();
		}
		return instance;
	}

	// set the id of the user for this session
	public void setSessionUserID(String usrID)
	{
		CurrentUserEmail = usrID;
		openThisConnection();

		try{
			Statement s = CONNECTION.createStatement();
			ResultSet rs = s.executeQuery("SELECT name FROM usr WHERE email = '" + CurrentUserEmail + "';");
			while(rs.next())
			{
				CurrentUserName = rs.getString("name");
			}
		}
		catch(SQLException e){}
		closeThisConnection();
	}

	public String getCurrentUserName()
	{
		return CurrentUserName;
	}
	private DatabaseConnector()
	{
		openThisConnection();
		closeThisConnection();
	}

	private static void openThisConnection()
	{
		// Register the driver.
			try {
				System.out.println("Registering Driver");
				DriverManager.registerDriver(new org.postgresql.Driver());
				System.out.println("Successful Registration");
				CONNECTION = DriverManager.getConnection(URL, dbUsr, dbUsrPwd);
			} catch (SQLException e) {
				closeThisConnection();
				System.out.println("entered this catch block");
			}
			System.out.println("connection opened!");
	}

	private static void closeThisConnection()
	{
		try {
			CONNECTION.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("connection closed");
	}
	public ArrayList<Band> getBandsBy(BandCol col, String searchKey) throws NoResultException
	{
		openThisConnection();
		ArrayList<Band> bandsFound = new ArrayList<Band>();
		String query = null;
		switch(col)
		{
			case B_ID: 		query = "SELECT * FROM band WHERE ID = " + searchKey + ";"; break;
			case B_NAME: 	query = "SELECT * FROM band WHERE name LIKE "+ "'%" + searchKey + "%';";	break;
			case B_EMAIL:	query = "SELECT * FROM band WHERE email = " + searchKey; break;
			case B_CITY:	query = "SELECT * FROM band WHERE city = " + "'" + searchKey + "';"; break;
			case B_GENRE:	query = "SELECT * FROM band WHERE genre LIKE " + "'" + searchKey + "';"; break;
			default:		System.out.println("Please select a valid search parameter. Returning null now.");
		}
		if (query == null)
		{
			return null;
		}
		System.out.println("Excecuting query: " + query);
		ResultSet rs;
		try {
			Statement s = CONNECTION.createStatement();
			rs = s.executeQuery(query);
			while(rs.next())
			{
				int curr_bandID = rs.getInt("bandid");
				String curr_name = rs.getString("name");
				String curr_city = rs.getString("city");
				String curr_weblink = rs.getString("weblink");
				String curr_email = rs.getString("email");
				String curr_description = rs.getString("bio");
				String curr_genre = rs.getString("genre");

				//make a new band from the attributes in found this row
				Band currBand = new Band(curr_bandID, curr_name, curr_city, curr_genre, curr_description, curr_weblink, curr_email);
				// ... and add it to the return list
				bandsFound.add(currBand);
			}
		}
		catch(SQLException e){	closeThisConnection(); e.printStackTrace();}
		closeThisConnection();
		if(bandsFound.size() == 0)
		{
			throw new NoResultException("no results");
		}
		return bandsFound;
	}

	public User getUserByEmail(String email) throws NoResultException
	{
		User ret = null;
		openThisConnection();
		ResultSet rs;
		try {
			Statement stmt = CONNECTION.createStatement();
			rs = stmt.executeQuery("SELECT * FROM usr WHERE email = '" + email + "';");
			while(rs.next())
			{
				ret = new User(
				rs.getString("email"), rs.getString("name"), rs.getString("password")
				);
			}
		}
		catch(SQLException sqle)
		{
			System.out.println("Error finding user with email:\t" + email);
			return null;
		}
		closeThisConnection();
		if(ret == null)
		{
			throw new NoResultException("no user found by email:\t" + email);
		}
		else
		{
			return ret;
		}
	}

	public ArrayList<Venue> getVenuesBy(VenueCol col, String searchKey)
	{
		ArrayList<Venue> venuesFound = new ArrayList<Venue>();
		String query = null;
		switch(col)
		{
			case V_ID: 		query = "SELECT * FROM venue WHERE ID = " + searchKey; break;
			case V_NAME: 	query = "SELECT * FROM venue WHERE name LIKE " + searchKey; break;
			case V_EMAIL:	query = "SELECT * FROM venue WHERE email = " + searchKey; break;
			case V_CITY:	query = "SELECT * FROM venue WHERE city = " + searchKey; break;
			case V_TYPE:	query = "SELECT * FROM venue WHERE type LIKE " + searchKey; break;
			default:		System.out.println("Please select a valid search parameter. Returning null now.");
		}
		if (query == null)
		{
			return null;
		}
		ResultSet rs;
		try {
			Statement s = CONNECTION.createStatement();
			rs = s.executeQuery(query);
			while(rs.next())
			{
				int curr_venueID = rs.getInt("venueid");
				String curr_name = rs.getString("name");
				String curr_city = rs.getString("city");
				String curr_weblink = rs.getString("weblink");
				String curr_email = rs.getString("email");
				String curr_description = rs.getString("description");
				String curr_type = rs.getString("type");
				String curr_addr = rs.getString("address");
				//make a new band from the attributes in found this row
				Venue currVenue = new Venue(curr_venueID, curr_name, curr_city, curr_type, curr_description, curr_weblink, curr_email, curr_addr);
				// ... and add it to the return list
				venuesFound.add(currVenue);
			}
		}
		catch(SQLException e){closeThisConnection(); e.printStackTrace();}

		return venuesFound;
	}

	public void addUser(User toAdd, String pwd) throws UserExistsException
	{
		openThisConnection();
		try{
			Statement s = CONNECTION.createStatement();
			s.executeQuery("INSERT INTO usr (email, name, password) VALUES('" + toAdd.getName() + "', '" + toAdd.getEmail() + "', '" + pwd + "');");
		}
		catch(SQLException e)
		{
			closeThisConnection();
			throw new UserExistsException("user already exists");
		}
		closeThisConnection();
	}

	public void addBand(Band toAdd)
	{
		openThisConnection();
		//The following query will insert a new enrty into the band table, and also update the bmanages table
		// TODO make query
		try{
			Statement s = CONNECTION.createStatement();

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			closeThisConnection();
		}
		closeThisConnection();
	}
}