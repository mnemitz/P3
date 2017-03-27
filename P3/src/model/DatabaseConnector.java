package model;
import org.postgresql.util.PSQLException;

import javax.xml.crypto.Data;
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
	public void setSessionUserID(String usrID) throws DatabaseConnectionException
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
		try{
			openThisConnection();
		}
		catch(DatabaseConnectionException d)
		{
			System.out.println("Error connecting to database");
		}
		closeThisConnection();
	}

	private static void openThisConnection() throws DatabaseConnectionException
	{
		// Register the driver.
			try {
				System.out.println("Registering Driver");
				DriverManager.registerDriver(new org.postgresql.Driver());
				System.out.println("Successful Registration");
				CONNECTION = DriverManager.getConnection(URL, dbUsr, dbUsrPwd);
			} catch (SQLException e) {
				closeThisConnection();
				throw new DatabaseConnectionException("Could not connect to database");
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
	public ArrayList<Band> getBandsBy(BandCol col, String searchKey) throws NoResultException, DatabaseConnectionException
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

	public User getUserByEmail(String email) throws NoResultException, DatabaseConnectionException
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

	public void addUser(User toAdd, String pwd) throws UserExistsException, DatabaseConnectionException
	{
		openThisConnection();
		try{
			Statement s = CONNECTION.createStatement();
			s.executeQuery("INSERT INTO usr (email, name, password) VALUES('" + toAdd.getName() + "', '" + toAdd.getEmail() + "', '" + pwd + "');");
		}
		catch(SQLException e) {
			closeThisConnection();
			System.out.println(e.toString());
			if (e.toString().contains("org.postgresql.util.PSQLException: ERROR: duplicate key value violates unique constraint \"usr_pkey\"")) {
				throw new UserExistsException("user already exists");
			}
		}
		closeThisConnection();
	}

	public void addBandForThisUsr(Band toAdd) throws DatabaseConnectionException, BandExistsException
	{
		openThisConnection();

		// First, check if this user already has a band by the given name
		ResultSet checkRs;
		try
		{
			Statement checkStatement = CONNECTION.createStatement();
			checkRs = checkStatement.executeQuery(
					"SELECT name FROM bmanages LEFT JOIN band " +
							"ON (bmanages.bandid = band.bandid) " +
							"WHERE bmanages.email = '" + CurrentUserEmail + "'" + " AND band.name = '" + toAdd.getName() + "';");
			if(checkRs.next())
			{
				closeThisConnection();
				throw new BandExistsException("Band by this name already exists");
			}
		}
		catch(SQLException e)
		{
			closeThisConnection();
			System.out.println("problem with check");
			if(!e.toString().contains("org.postgresql.util.PSQLException: No results were returned by the query."))
			{
				throw new DatabaseConnectionException("");
			}
		}

		// From here we can just assume the band by that name does not exist

		// this query inserts into the band table, and updates the manages table based on the current user
		String query = "INSERT INTO band(city, genre, bio, weblink, name, email) VALUES" +
				"('" + toAdd.city + "', '" + toAdd.genre + "', '" + toAdd.bio + "', '" + toAdd.weblink + "', '" + toAdd.name + "', '" + toAdd.email + "');" +
				"INSERT INTO bmanages(email, bandid, sincedate) VALUES('" + CurrentUserEmail + "', " + "currval('band_bandid_seq'::regclass), now());";
		try{
			Statement s = CONNECTION.createStatement();
			s.executeQuery(query);
		}
		catch(SQLException e)
		{
			closeThisConnection();
			e.printStackTrace();
		//	System.out.println("problem with insertion");
			if(!e.toString().contains("org.postgresql.util.PSQLException: No results were returned by the query."))
			{
				throw new DatabaseConnectionException("");
			}
		}
		closeThisConnection();
	}

	public ArrayList<Band> getCurrUsrBands() throws DatabaseConnectionException
	{
		openThisConnection();

		closeThisConnection();
		return null;
	}

	public void addVenueForThisUsr(Venue toAdd) throws DatabaseConnectionException, VenueExistsException
	{
		openThisConnection();

		// First, check if this user already has a band by the given name
		ResultSet checkRs;
		try
		{
			Statement checkStatement = CONNECTION.createStatement();
			checkRs = checkStatement.executeQuery(
					"SELECT venuename FROM vmanages LEFT JOIN venue " +
							"ON (vmanages.venueid = venue.venueid) " +
							"WHERE vmanages.email = '" + CurrentUserEmail + "'" + " AND venue.venuename = '" + toAdd.name + "';");
			if(checkRs.next())
			{
				closeThisConnection();
				throw new VenueExistsException("Venue by this name already exists");
			}
		}
		catch(SQLException e)
		{
			closeThisConnection();
			System.out.println("problem with check");

			// below is just to ignore no-result errors, but propogate the others
			if(!e.toString().contains("org.postgresql.util.PSQLException: No results were returned by the query."))
			{
				throw new DatabaseConnectionException("");
			}
		}

		// From here we can just assume the band by that name does not exist

		// this query inserts into the band table, and updates the manages table based on the current user
		String query = "INSERT INTO venue(city, type, description, weblink, venuename, email, address) VALUES" +
				"('" + toAdd.city + "', '" + toAdd.type + "', '" + toAdd.description + "', '" + toAdd.weblink + "', '" + toAdd.name + "', '" + toAdd.email + "', '" + toAdd.address + "');" +
				"INSERT INTO vmanages(email, venueid, sincedate) VALUES('" + CurrentUserEmail + "', " + "currval('venue_venueid_seq'::regclass), now());";
		try{
			Statement s = CONNECTION.createStatement();
			s.executeQuery(query);
		}
		catch(SQLException e)
		{
			closeThisConnection();
			e.printStackTrace();
			//	System.out.println("problem with insertion");

			// below is just to ignore no-result errors, but propogate the others
			if(!e.toString().contains("org.postgresql.util.PSQLException: No results were returned by the query."))
			{
				throw new DatabaseConnectionException("");
			}
		}
		closeThisConnection();
	}
}