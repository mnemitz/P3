package controller;

import model.Band;
import model.DatabaseConnector;
import model.NoResultException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import static model.DatabaseConnector.BandCol.*;

/**
 * Created by arthur on 3/25/2017.
 */
public class BandController {

    public BandController() {

    }

    public void createBand(String name, String city, String genre, String bio, String weblink, String email) {


    }

    public void deleteBand(int id) {
    }

    public void modifyBand(int id) {
    }

    public Band getBand(Integer id) {
        try {
            return DatabaseConnector.getInstance().getBandsBy(B_ID, id.toString()).get(0);
        }
        catch(NoResultException nre)
        {
            System.out.println("No band by this ID");
            return null;
        }
    }

    public List<Band> searchBandsByName(String query) {
        try {
            return DatabaseConnector.getInstance().getBandsBy(B_NAME, query);
        } catch (NoResultException e) {
            e.printStackTrace();
            return null;
        }
    }

   /* public List<Band> searchBandsByDate(String query) {
        // return a list of bands that correspond to the query based on their dates
        return null;
    }
*/
    public List<Band> searchBandsByCity(String query) {
        // returns a list of bands that correspond to the query based on their cities
        try {
            return DatabaseConnector.getInstance().getBandsBy(B_CITY, query);
        } catch (NoResultException e) {
            e.printStackTrace();
            return null;
        }
    }

}
