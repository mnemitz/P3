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
		B_NAME_EQ,
		B_NAME_LIKE,
		B_EMAIL,
		B_CITY,
		B_GENRE,
		B_BIO,
		B_WEBLINK,
	}

	public enum VenueCol
	{
		V_ID,
		V_NAME_EQ,
		V_NAME_LIKE,
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



	public static DatabaseConnector getInstance() throws DatabaseConnectionException
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
	private DatabaseConnector() throws DatabaseConnectionException
	{
		openThisConnection();
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
			case B_NAME_EQ: 	query = "SELECT * FROM band WHERE name ='" + searchKey + "';";	break;
			case B_NAME_LIKE: 	query = "SELECT * FROM band WHERE name LIKE "+ "'%" + searchKey + "%';";	break;
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

	public ArrayList<Venue> getVenuesBy(VenueCol col, String searchKey) throws DatabaseConnectionException, NoResultException
	{
		openThisConnection();
		ArrayList<Venue> venuesFound = new ArrayList<Venue>();
		String query = null;
		switch(col)
		{
			case V_ID: 		query = "SELECT * FROM venue WHERE ID = " + searchKey; break;
			case V_NAME_EQ: 	query = "SELECT * FROM venue WHERE venuename = '" + searchKey + "';"; break;
			case V_NAME_LIKE: 	query = "SELECT * FROM venue WHERE venuename LIKE '%" + searchKey + "%';"; break;
			case V_EMAIL:	query = "SELECT * FROM venue WHERE email = " + searchKey; break;
			case V_CITY:	query = "SELECT * FROM venue WHERE city = " + searchKey; break;
			case V_TYPE:	query = "SELECT * FROM venue WHERE type LIKE " + searchKey; break;
			default:		System.out.println("Please select a valid search parameter. Returning null now.");
		}
		if (query == null)
		{
			throw new NoResultException("");
		}
		System.out.println("Now executing:\t" + query);
		ResultSet rs;
		try {
			Statement s = CONNECTION.createStatement();
			rs = s.executeQuery(query);
			while(rs.next())
			{
				int curr_venueID = rs.getInt("venueid");
				String curr_name = rs.getString("venuename");
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
		if(venuesFound.size() == 0)
		{
			System.out.println("No venue found");
			throw new NoResultException("");
		}
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
		System.out.println("About to execute: " + query);
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

	public ArrayList<Band> getCurrUsrBands() throws DatabaseConnectionException, NoResultException
	{
		ArrayList<Band> ret = null;
		openThisConnection();
		try{
			Statement s = CONNECTION.createStatement();
			ResultSet rs = s.executeQuery("SELECT band.* FROM bmanages LEFT JOIN band ON (bmanages.bandid = band.bandid) WHERE bmanages.email = '"
					+ CurrentUserEmail + "';" );
			while(rs.next())
			{
				if(ret == null)
				{
					ret = new ArrayList<>();
				}
				Band curr = new Band(
						rs.getInt("bandid"),
						rs.getString("name"),
						rs.getString("city"),
						rs.getString("genre"),
						rs.getString("bio"),
						rs.getString("weblink"),
						rs.getString("email")
				);
				ret.add(curr);
			}
		}
		catch(SQLException e)
		{
			closeThisConnection();
			throw new DatabaseConnectionException("");
		}
		closeThisConnection();
		if(ret == null)
		{
			throw new NoResultException("");
		}
		return ret;
	}

	public ArrayList<Venue> getCurrUsrVenues() throws DatabaseConnectionException, NoResultException
	{
		ArrayList<Venue> ret = null;
		openThisConnection();
		try{
			Statement s = CONNECTION.createStatement();
			ResultSet rs = s.executeQuery("SELECT venue.* FROM vmanages LEFT JOIN venue ON (vmanages.venueid = venue.venueid) WHERE vmanages.email = '"
					+ CurrentUserEmail + "';" );
			while(rs.next())
			{
				if(ret == null)
				{
					ret = new ArrayList<>();
				}
				Venue curr = new Venue(
						rs.getInt("venueid"),
						rs.getString("venuename"),
						rs.getString("city"),
						rs.getString("type"),
						rs.getString("description"),
						rs.getString("weblink"),
						rs.getString("email"),
						rs.getString("address")
				);
				ret.add(curr);
			}
		}
		catch(SQLException e)
		{
			closeThisConnection();
			throw new DatabaseConnectionException("");
		}
		closeThisConnection();
		if(ret == null)
		{
			throw new NoResultException("");
		}
		return ret;
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


	public void createShow(Show toAdd) throws DatabaseConnectionException, NoResultException, ShowExistsException
	{
		openThisConnection();
		try{
			Statement vid = CONNECTION.createStatement();
			ResultSet vidResult = vid.executeQuery("SELECT venueid FROM venue WHERE venuename = '" + toAdd.venueName + "';");
			Integer venueID;
			if(vidResult.next())
			{
				venueID = (Integer) vidResult.getInt("venueid");
			}
			else{
				closeThisConnection();
				throw new NoResultException("");
			}
			closeThisConnection();
			for(String bandname : toAdd.bands){
				Band currBand = getBandsBy(BandCol.B_NAME_EQ, bandname).get(0);
				toAdd.addBandID(currBand.id);
			}
			// Now all the needed information is ready
			openThisConnection();
			Statement ins = CONNECTION.createStatement();
			ins.executeQuery(toAdd.insertionQuery(venueID));
		}
		catch(SQLException e){
			closeThisConnection();

			if(!e.toString().contains("org.postgresql.util.PSQLException: No results were returned by the query."))
			{
				e.printStackTrace();
				throw new DatabaseConnectionException("");
			}
		}
		closeThisConnection();
	}

	public ArrayList<Show> searchShows(String bandName, String venueName) throws DatabaseConnectionException, NoResultException
	{
		ArrayList<Show> ret = new ArrayList<>();
		openThisConnection();
		try{
			Statement showsearch = CONNECTION.createStatement();
			ResultSet results = showsearch.executeQuery(
					"SELECT show.* , band.name, venue.venuename " +
							"FROM (show LEFT JOIN (hosts LEFT JOIN venue ON hosts.venueid = venue.venueid) ON show.showid = hosts.showid) LEFT JOIN " +
								"(plays LEFT JOIN band ON plays.bandid = band.bandid) " +
							"ON show.showid = plays.showid WHERE band.name LIKE '%" + bandName + "%' AND venue.venuename LIKE '%" + venueName + "%';");
			while(results.next())
			{
				ret.add(new Show(
						results.getInt("showid"),
						results.getString("showname"),
						results.getString("showdate"),
						results.getString("showtime"),
						results.getString("venuename"),
						results.getBoolean("public")
				));
			}
		}
		catch(SQLException e)
		{
			closeThisConnection();
			e.printStackTrace();
			throw new DatabaseConnectionException("");
		}
		if(ret.size() == 0)
		{
			closeThisConnection();
			throw new NoResultException("");
		}
		closeThisConnection();
		return ret;
	}
}