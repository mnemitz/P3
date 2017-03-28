package controller;

import model.DatabaseConnector;
import model.Show;

import java.util.List;

/**
 * Created by arthur on 3/25/2017.
 */
public class ShowController {

    public ShowController() {

    }

    public void createShow(String name, String city, String date, String time, String weblink) {

    }

    public void deleteShow(int id) {
        //TODO remove a show from database
    }

    public void modifyShow(int id) {
        //TODO modify the show's information in the database
    }

    public List<Show> searchShowsByName(String query) {
        //TODO return a list of shows that correspond to the query based on their names
        return null;
    }

    public List<Show> searchShowsByDate(String query) {
        //TODO return a list of shows that correspond to the query based on their dates
        return null;
    }

    public List<Show> searchShowsByCity(String query) {
        //TODO return a list of shows that correspond to the query based on their cities
        return null;
    }

}
