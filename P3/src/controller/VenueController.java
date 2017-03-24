package controller;

import model.Venue;

import java.util.List;

/**
 * Created by arthur on 3/25/2017.
 */
public class VenueController {

    public VenueController() {

    }

    public void createVenue(String name, String city, String type, String description, String weblink, String email, String address) {
        //TODO create a venue and add it to the database
    }

    public void deleteVenue(int id) {
        //TODO delete the venue from the database
    }

    public void modifyVenue(int id) {
        //TODO modify the venue with the corresponding id and update the database
    }

    public List<Venue> searchVenuesByName(String query) {
        //TODO return a list of venues that correspond to the query based on their names
        return null;
    }

    public List<Venue> searchVenuesByDate(String query) {
        //TODO return a list of venues that correspond to the query based on their dates
        return null;
    }

    public List<Venue> searchVenuesByCity(String query) {
        //TODO return a list of venues that correspond to the query based on their cities
        return null;
    }

}