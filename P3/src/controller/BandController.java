package controller;

import model.Band;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

/**
 * Created by arthur on 3/25/2017.
 */
public class BandController {

    static Integer id;
    static String name;
    static String city;
    static String genre;
    static String bio;
    static String weblink;
    static String email;

    public BandController() {

    }

    public void createBand(String name, String city, String genre, String bio, String weblink, String email) {
        //TODO create a band and add it to the database
    }

    public void deleteBand(int id) {
        //TODO delete the band with the corresponding id from the database
    }

    public void modifyBand(int id) {
        //TODO modify the band with the corresponding id and update the database
    }

    public Band getBand(int id) throws Exception {
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

        return new Band(id, name, city, genre, bio, weblink, email);
    }

    public List<Band> searchBandsByName(String query) {
        //TODO return a list of bands that correspond to the query based on their names
        return null;
    }

    public List<Band> searchBandsByDate(String query) {
        //TODO return a list of bands that correspond to the query based on their dates
        return null;
    }

    public List<Band> searchBandsByCity(String query) {
        //TODO return a list of bands that correspond to the query based on their cities
        return null;
    }

}
