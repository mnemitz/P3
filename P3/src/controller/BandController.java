package controller;

import model.Band;

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
