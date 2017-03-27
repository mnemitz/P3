package view;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.*;


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
        catch(DatabaseConnectionException d)
        {
            System.out.println("Error connecting to database");
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

    @FXML
    Tab myBandsTab;

    @FXML
    private TextField addBandName;

    @FXML
    private TextField addBandEmail;

    @FXML
    private TextField addBandGenre;

    @FXML
    private TextField addBandCity;

    @FXML
    private TextField addBandWeblink;

    @FXML
    private TextArea addBandBio;

    @FXML
    private Label bandTabConsole;

    public void addBand()
    {
        String bnameToAdd = addBandName.getText();
        String bemailToAdd = addBandEmail.getText();
        String bgenreToAdd = addBandGenre.getText();
        String bcityToAdd = addBandCity.getText();
        String bweblinkToAdd = addBandWeblink.getText();
        String bbioToAdd = addBandBio.getText();

        if( //check none are empty
                !bnameToAdd.equals("") &&
                !bemailToAdd.equals("") &&
                !bgenreToAdd.equals("") &&
                !bcityToAdd.equals("") &&
                !bweblinkToAdd.equals("") &&
                !bbioToAdd.equals("")
                ){
            try{
                DatabaseConnector.getInstance().addBandForThisUsr(
                        new Band(
                              bnameToAdd,bcityToAdd,bgenreToAdd,bbioToAdd,bweblinkToAdd,bemailToAdd
                        )
                );
            }
            catch(DatabaseConnectionException d)
            {
                bandTabConsole.setText("Error connecting to database");
            }
            catch(BandExistsException b)
            {
                bandTabConsole.setText("You already manange a band by this name");
            }
            bandTabConsole.setText("Successfully created band: " + bnameToAdd);

    }else
        {
            bandTabConsole.setText("Please fill in all the fields");
        }

    }

    @FXML
    TreeView<String> myBandsView;

    public void showMyBands()
    {
        ArrayList<TreeItem<String>> bandsToDisplay = new ArrayList<>();
        ArrayList<Band> bandsFromDB;
        try
        {
            bandsFromDB = DatabaseConnector.getInstance().getCurrUsrBands();
            for(Band b : bandsFromDB)
            {
                TreeItem<String> currBandItem = new TreeItem<>(b.name);
                currBandItem.getChildren().add(new TreeItem<>("City:\t" + b.city));
                currBandItem.getChildren().add(new TreeItem<>("Email:\t" + b.city));
                currBandItem.getChildren().add(new TreeItem<>("Genre:\t" + b.city));
                currBandItem.getChildren().add(new TreeItem<>("Weblink:\t" + b.city));
                currBandItem.getChildren().add(new TreeItem<>("City:\t" + b.city));
                TreeItem<String> bioItem = new TreeItem<>("Bio");
                bioItem.setExpanded(false);
                bioItem.getChildren().add(new TreeItem<>(b.bio));
                currBandItem.setExpanded(false);
                bandsToDisplay.add(currBandItem);
            }
        }
        catch(DatabaseConnectionException d)
        {
            System.out.println("database connection exception");
            return;
        }
        catch(NoResultException n)
        {
            System.out.println("no result");
            return;
        }

        TreeItem<String> root = new TreeItem<>("My Bands");
        for(TreeItem<String> item : bandsToDisplay)
        {
            root.getChildren().add(item);
        }
        root.setExpanded(true);
        myBandsView.setRoot(root);
    }

    @FXML
    private TextField addVenueName;

    @FXML
    private TextField addVenueEmail;

    @FXML
    private TextField addVenueType;

    @FXML
    private TextField addVenueAddress;

    @FXML
    private TextField addVenueCity;

    @FXML
    private TextField addVenueWeblink;

    @FXML
    private TextArea addVenueDescription;

    @FXML
    private Label venueTabConsole;

    public void addVenue()
    {
        String vnameToAdd = addVenueName.getText();
        String vemailToAdd = addVenueEmail.getText();
        String vtypeToAdd = addVenueType.getText();
        String vcityToAdd = addVenueCity.getText();
        String vweblinkToAdd = addVenueWeblink.getText();
        String vdescriptionToAdd = addVenueDescription.getText();
        String vaddressToAdd = addVenueAddress.getText();

        if( //check none are empty
                !vnameToAdd.equals("") &&
                        !vemailToAdd.equals("") &&
                        !vtypeToAdd.equals("") &&
                        !vcityToAdd.equals("") &&
                        !vweblinkToAdd.equals("") &&
                        !vdescriptionToAdd.equals("") &&
                        !vaddressToAdd.equals("")
                ){
            try{
                DatabaseConnector.getInstance().addVenueForThisUsr(new Venue(
                        vnameToAdd, vcityToAdd, vtypeToAdd, vdescriptionToAdd, vweblinkToAdd, vemailToAdd, vaddressToAdd
                ));
            }
            catch(DatabaseConnectionException d)
            {
                venueTabConsole.setText("Error connecting to database");
            }
            catch(VenueExistsException b)
            {
                venueTabConsole.setText("You already manage a venue by this name");
            }
            venueTabConsole.setText("Sucessfully created venue: " + vnameToAdd);
        }else
        {
            venueTabConsole.setText("Please fill in all the fields");
        }

    }
}
