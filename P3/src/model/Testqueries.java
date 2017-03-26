package model;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class Testqueries{
    
    static Connection conn = null;
    static ResultSet rs = null;
    static ArrayList<Integer> bandIds = new ArrayList();
    static ArrayList<Integer> showIds = new ArrayList();
    static ArrayList<Integer> venueIds = new ArrayList();
    static Band crntBand = new Band();
    static Show crntShow = new Show();
    static Venue crntVenue = new Venue();
    //static User crntUser = new User();
    static int i = 1;
    
    static Scanner scanner = new Scanner(System. in); 
   
    

    
    /*public static void main(String args[]) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException
    {
    	openConnection();       
    	System.out.println("1___________________________________________________________");
    	BandListPage("Montreal", "city");
    	System.out.println("2___________________________________________________________");
    	BandPageHelper(1);
    	System.out.println("3___________________________________________________________");
    	ShowPageHelper(1);
    	System.out.println("4___________________________________________________________");
    	BandPageHelper(1);
    	System.out.println("5___________________________________________________________");
    	ShowPageHelper(1);
    	System.out.println("6___________________________________________________________");
    	VenuePageHelper(1);
    	
    	
//    	Statement stmnt = conn.createStatement();
//    	rs = stmnt.executeQuery("SELECT * FROM ");
//      displayShows(rs);
       	//displayShows(rs);
    	closeConnection();
    }

    public static void openConnection()throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException
    {
    	// Register the driver.  
    	try {
    		System.out.println("Registering Driver");
            DriverManager.registerDriver ( new org.postgresql.Driver() ) ;
            System.out.println("Successful Registration");
        } 
            catch (Exception cnfe){
            System.out.println("Class not found");
            }
        
        conn = DriverManager.getConnection("jdbc:postgresql://comp421.cs.mcgill.ca:5432/cs421","cs421g36", "LookVibrant");
    }
    
    public static void closeConnection()throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException
    {
    	conn.close();
    }
    
    // INPUT: argument + 'name', 'city' or 'genre'
    public static void BandListPage(String attrname, String attr ){
        try {
           	Statement stmnt = conn.createStatement();
            rs = stmnt.executeQuery("SELECT bandid, name, city, LEFT(bio, 50) AS shortbio " + 
                    " FROM Band WHERE "+ attr + " LIKE '" + attrname + "%'");
            displayBands(rs);
        }catch(SQLException e) {System.err.println("Error Occured While Listing Bands");}
    }
    
    // INPUT: argument + 'name', 'city' or 'type'
    public static void VenueListPage(String attrname, String attr ){
        try {
           	Statement stmnt = conn.createStatement();
            rs = stmnt.executeQuery("SELECT venueid, name, city, LEFT(description, 20) " + 
                    " FROM Band WHERE "+ attr + " LIKE " + attrname + "%");
            displayVenues(rs);
        }catch(SQLException e) {System.err.println("Error Occured While Listing Venues");}
    }
    
    // INPUT: position of the ID number from the ID array ex. 1 refers to bandIds at position 0
    // I assume this would be analogous to where a user clicks, given a list of options
    public static void BandPageHelper(int IDslot) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
    {
    	try {
    		BandPage(bandIds.get(IDslot-1));
    	} catch(IndexOutOfBoundsException e) { System.err.println("There is no band at that index");}
    //	bandIds.clear();
    }
    
    public static void BandPage(Integer x) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
    {	
    	venueIds.clear();
    	bandIds.clear();
    	showIds.clear();
    	crntBand.setBand(x);
        try {
           	Statement stmnt = conn.createStatement();
            rs = stmnt.executeQuery("SELECT s.showid, s.public, s.showname, s.city, s.showdate, x.venuename"+
           	" FROM show s JOIN (SELECT v.venuename, v.venueid, h.showid FROM hosts h JOIN venue v ON v.venueid=h.venueid) x"+
            " ON x.showid=s.showid WHERE s.showdate >= NOW() AND s.showID IN"+
           	" (SELECT showID FROM plays WHERE bandID = '" + crntBand.id + "' ) ORDER BY showdate;"); //tested in sql

        }catch(SQLException e) {System.err.println("Error Occured While Selecting Shows");}
        System.out.println("Name: " + crntBand.getName());
        System.out.println("City: " + crntBand.getCity());
        System.out.println("Genre: " + crntBand.getGenre());
        System.out.println("Bio: " + crntBand.getBio());
        System.out.println("Website: " + crntBand.getWeblink());
        System.out.println("Email: " + crntBand.getEmail());
        System.out.println();
        System.out.println("Upcoming Shows:");
        System.out.println();
        displayShows(rs);
        //displays shows (only those which are public) and cues up list of shows, to be clicked on according to it's slot 
    }
    
    public static void VenuePageHelper(int IDslot) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
    {
    	VenuePage(venueIds.get(IDslot-1));
    	//venueIds.clear();
    }
    
    public static void VenuePage(Integer x) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
    {	
    	venueIds.clear();
    	bandIds.clear();
    	showIds.clear();
    	crntVenue.setVenue(x);
        try {
           	Statement stmnt = conn.createStatement();
           	//FIX THIS -> include headlining band
            rs = stmnt.executeQuery("SELECT s.showid, s.public, s.showname, s.city, s.showdate, string_agg(x.bandname, ', ') AS bands FROM show s "+
            		"JOIN (SELECT band.name AS bandname, plays.showid FROM plays JOIN band ON band.bandid=plays.bandid) x ON x.showid=s.showid "+
            		"WHERE s.showdate >= NOW() AND s.showID IN (SELECT showID FROM hosts WHERE venueid = "+ crntVenue.getId() + ")"+
            		" GROUP BY s.showid ORDER BY showdate;"); 
        }catch(SQLException e) {System.err.println("Error Occured While Selecting Shows From Venue");}
        System.out.println("Name: " + crntVenue.getName());
        System.out.println("City: " + crntVenue.getCity());
        System.out.println("Adress: " + crntVenue.getAddress());
        System.out.println("Type: " + crntVenue.getType());
        System.out.println("Description: " + crntVenue.getDescription());
        System.out.println("Website: " + crntVenue.getWeblink());
        System.out.println("Email: " + crntVenue.getEmail());
        System.out.println();
        System.out.println("Upcoming Shows:");
        System.out.println();
        displayShowsAtVenue(rs);
        //displays shows (only those which are public) and cues up list of shows, to be clicked on according to it's slot 
    }

    public static void ShowPageHelper(int IDslot) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
    {
    	ShowPage(showIds.get(IDslot-1));
    	//showIds.clear();
    }
    
    public static void ShowPage(Integer x) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
    {	
    	venueIds.clear();
    	bandIds.clear();
    	showIds.clear();
    	crntShow.setShow(x);
        System.out.println("Name: " + crntShow.getName());
        System.out.println("City: " + crntShow.getCity());
        System.out.println("Date: " + crntShow.getDate());
        System.out.println("Time: " + crntShow.getTime());
        System.out.println("Link: " + crntBand.getWeblink());
        try {
           	Statement stmnt = conn.createStatement();
            rs = stmnt.executeQuery("SELECT v.venueid, v.venuename FROM venue v JOIN hosts h ON v.venueid = h.venueid WHERE h.showid = " + crntShow.getId()); 
        }catch(SQLException e) {System.err.println("Error Occured While Selecting Venue");}
        rs.next();
        venueIds.add((Integer) rs.getInt("venueid"));
        System.out.println("Venue: " + rs.getString("venuename"));
        System.out.println();
//        System.out.println("Email: " + crntBand.getUserEmail());
        try {
           	Statement stmnt = conn.createStatement();
            rs = stmnt.executeQuery("SELECT p.bandid, name, timeslot, confirmationcolour FROM band b JOIN plays p ON b.bandid=p.bandid WHERE p.showid= "
           	+ crntShow.getId() +" ORDER BY timeslot"); 

        }catch(SQLException e) {System.err.println("Error Occured While Selecting Bands on Bill");}
        System.out.println("Bill:");
        System.out.println();
        displayBandsOnBill(rs);
//        Boolean correctvalue = false;
//        while(correctvalue == false){
//        	System.out.println("type a band's slot number to view band, type 'v' to view venue, type 'search' to return to search menue: ");
//        	String input = scanner.nextLine();
//        
//        	if (scanner.hasNextInt()) {
//        		Integer number = (Integer) scanner.nextInt();
//        		if (bandIds.get(number-1)!=null) {
//        			correctvalue = true;
//        			BandPageHelper(number-1);
//        		}
//        		else {
//        			System.out.println("There is no band that occupies that slot");
//        		}
//        	}
//        	else if (input == "v"){
//        		if (VenuePage(venueIds.get(0) != null)){
//        			correctvalue = true;
//        			VenuePage(venueIds.get(0));
//        		}
//        		else {
//        			System.out.println("The is no venue associated with this show");
//        		}
//        	}
//        	else if (input == "search") {
//        		correctvalue = true;
//        		SearchPage();
//        	}
//        }    
        	// At any point can type "search" to return to search page
    }
    
    public static void displayBands(ResultSet x) throws SQLException{
        while ( x.next() ) {
        	bandIds.add((Integer) x.getInt("bandid"));
            String name = x.getString("name");
            String city = x.getString("city");
            String description = x.getString("shortbio");
            System.out.println(i + ".  Name: " + name + " - City: " + city);
            System.out.println(description + "...");
            System.out.println();
            i++;
        }
        i = 1;
    }
    
    public static void displayVenues(ResultSet x) throws SQLException{
        while ( x.next() ) {
        	venueIds.add((Integer) x.getInt("venueid"));
            String name = x.getString("venuename");
            String city = x.getString("city");
            String description = x.getString("description");
            System.out.println(i + ".  Name: " + name + "City:" + city);
            System.out.println(description);
            System.out.println();
            i++;
        }
        i = 1;
    }
    
    public static void displayBandsOnBill(ResultSet x) throws SQLException{
        while ( x.next() ) {
        	bandIds.add((Integer) x.getInt("bandid"));
            String name = x.getString("name");
            String timeslot = x.getString("timeslot");
            String colour = x.getString("confirmationcolour");
            System.out.println(i + ". "+ name + " ("+ colour +") @ " + timeslot);
            System.out.println();
            i++;
        }
        i = 1;
    }
    
    public static void displayShows(ResultSet x) throws SQLException{
        while ( x.next() ) {
    //    	System.out.println("STARTED GETTING SHOWS");
        	Boolean p = x.getBoolean("public");
        	if (p) {
        		showIds.add((Integer) x.getInt("showid"));
        		String showname = x.getString("showname");
        		String city = x.getString("city");
        		String showdate = x.getString("showdate");
        		String venuename = x.getString("venuename");
        		System.out.println(i + ". " + showname + " @ " + venuename + ", " + city + " on " + showdate);
        		System.out.println();
        	}
            i++;
        }
        i = 1;
    }

    public static void displayShowsAtVenue(ResultSet x) throws SQLException{
        while ( x.next() ) {

        	Boolean p = x.getBoolean("public");
        	if (p) {
        		showIds.add((Integer) x.getInt("showid"));
        		String showname = x.getString("showname");
        		String city = x.getString("city");
        		String showdate = x.getString("showdate");
        		String bands = x.getString("bands");
        		System.out.println(i + ". " + showname + " with " + bands + ", " + city + " on " + showdate);
        		System.out.println();
        	}
            i++;
        }
        i = 1;
    }

//    public void loginPage(String email, String passwd) throws SQLException {
//        try {
//            Statement stmnt = conn.createStatement();
//            rs = stmnt.executeQuery("SELECT * FROM user WHERE email = " + email);
//        }catch(SQLException e) {System.err.println("Error occured while scanning users");}
//
//            if (!rs.next()){
//                System.out.println("Email does not exist in the system");
//            }
//            else if (passwd == rs.getString("password")){
//                crntUser.setUser(rs.getString("email"));  //NEED TO CREATE USER CLASS
//                System.out.println("You've successfully logged in as " + rs.getString("name"));
//            }
//            else {
//                System.out.println("Password does not match user name");
//            }
//    }
//
//    public void addBandPage(String N, String C, String E, String B, String G, String W) throws SQLException {
//        Statement stmnt = conn.createStatement();
//        rs = stmnt.executeQuery( "SELECT MAX(bandid) FROM band");//get most current band id
//        int newId = rs.getInt("bandid") + 1;
//        if (crntUser.getId() == null){
//            System.out.println("Must log in to add band");
//        }
//        else { // There is a user logged in
//            try {
//                stmnt = conn.createStatement();
//                rs = stmnt.executeQuery("SELECT * FROM bands WHERE name = " + N + " AND city = " + C);
//            }catch(SQLException e) {System.err.println("Error Occured While Scanning through bands");}
//            if (!rs.next()){
//                addBand(newId, N, C, E, B, G , W);
//            }
//            else {
//                System.out.println("Are any of these bands yours?");
//                System.out.println();
//                displayBands(rs);
//                System.out.println();
//                System.out.println("newId, N, C, E, B, G , W, Call registerExistingBand(<index number>, <'y' or 'n'>)");
//                //obviously this prompt will be implemented according to java effects
//                //I figure that calling methods would translate more directly than reading from scanner
//                }
//            }
//        }
//    public void registerExistingBand(int I, String N, String C, String E, String B, String G, String W, int IDslot, String yesno ) throws SQLException {
//        if (yesno = 'n') {
//            addBand(I, N, C, E, B, G , W);
//        }
//        else if (yesno = 'y') {
//            System.out.println("Linking your account");
//            crntBand.setBand(bandIds.get(IDslot-1));
//            Statement stmnt = conn.createStatement();
//            stmnt.executeQuery("INSERT INTO bmanages (bandId, userId, sincedate) VALUES (" + crntBand.getId() + ", " + crntUser.getId() + "NOW())");
//        }
//    }
//    public void addBand(int I, String N, String C, String E, String B, String G, String W){
//        try {// there are no bands that match that name and city
//            Statement stmnt = conn.createStatement();
//            stmnt.executeQuery("INSERT INTO bands (bandid, name, city, email, bio, genre, weblink) VALUES (" + I + ", " +
//                    N + ", " + C + ", " + E + ", " + B + ", " + G + ", " + W + ")"); //insert new band into band table
//            stmnt.executeQuery("INSERT INTO bmanages (bandId, userId, sincedate) VALUES (" + I + ", " + crntUser.getId() + "NOW())");
//        }catch(SQLException e) {System.err.println("Error Occured While Inputing New Band");}
//        System.out.println( N + " has been added to the database");
//    }
}


