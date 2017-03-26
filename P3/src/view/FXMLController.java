package view;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Band;
import model.DatabaseConnector;
import model.NoResultException;


import javax.xml.crypto.Data;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Matt Nemitz on 3/25/2017.
 */
public class FXMLController implements Initializable {

    @FXML
    private SplitMenuButton bandsBy;

    @FXML
    private SplitMenuButton venuesBy;

    @FXML
    private Label welcomeLabelOne;

    @FXML
    private Label welcomeLabelTwo;

    private DatabaseConnector.BandCol bandParam;
    private DatabaseConnector.VenueCol venParam;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DatabaseConnector.getInstance();
        setNameBand();
        setNameVenue();
        welcomeLabelOne.setText( "Welcome, " + DatabaseConnector.getInstance().getCurrentUserName());
    }

    @FXML
    private TextField bandSearchBar;

    public void setNameBand()
    {
        bandParam = DatabaseConnector.BandCol.B_NAME;
        bandsBy.setText("by name");
    }

    public void setLocationBand()
    {
        bandParam = DatabaseConnector.BandCol.B_CITY;
        bandsBy.setText("by city");
    }

    public void setGenreBand()
    {
        bandParam = DatabaseConnector.BandCol.B_GENRE;
        bandsBy.setText("by genre");
    }


    public void setNameVenue()
    {
        venParam = DatabaseConnector.VenueCol.V_NAME;
        venuesBy.setText("by name");
    }

    public void setLocationVenue()
    {
        venParam = DatabaseConnector.VenueCol.V_CITY;
        venuesBy.setText("by location");
    }

    public void setTypeVenue()
    {
        venParam = DatabaseConnector.VenueCol.V_TYPE;
        venuesBy.setText("by type");
    }

    @FXML
    TreeView<String> consoleTree;

    public void searchBands()
    {
        String searchkey = bandSearchBar.getCharacters().toString();
        TreeItem<String> treeRoot = new TreeItem<String>("Band search:");
        treeRoot.setExpanded(true);

        try {
            ArrayList<Band> bands = DatabaseConnector.getInstance().getBandsBy(bandParam, searchkey);
           // System.out.println(bands.get(0).getName());
            for(Band b : bands)
            {
                TreeItem<String> bandTreeItem = new TreeItem<String>(b.getName() + ",\t" + b.getLocation());
                treeRoot.getChildren().add(bandTreeItem);
            }
        }
        catch(NoResultException nre)
        {
            TreeItem<String> noResultMessage = new TreeItem<String>("No results found for this search input");
            treeRoot.getChildren().add(noResultMessage);
            System.out.println("could not find band based on that input...");
        }
        consoleTree.setRoot(treeRoot);
    }

    @FXML
    private Button logOutButton;

    public void logOut()
    {
        Stage ourStage = (Stage) welcomeLabelOne.getScene().getWindow();
        try {
            SplitPane loginSplitPane = (SplitPane) FXMLLoader.load(Main.class.getResource("Login.fxml"));
            Scene loginScene = new Scene(loginSplitPane);
            ourStage.setScene(loginScene);
            ourStage.setTitle("Concert Hog Prototype");
            DatabaseConnector.getInstance().setSessionUserID(null);
            ourStage.show();
        } catch (Exception e) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
            System.exit(1);
        }
    }
}
