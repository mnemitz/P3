package view;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.*;


import javax.xml.crypto.Data;
import java.net.URL;
import java.time.LocalDate;
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

    @FXML
    private SplitMenuButton showHourPicker;

    @FXML
    private TextField addShowMin;

    @FXML
    private SplitMenuButton showAMPM;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            DatabaseConnector.getInstance();
            welcomeLabelOne.setText( "Welcome, " + DatabaseConnector.getInstance().getCurrentUserName());
            showMyBands();
            showMyVenues();
        } catch (DatabaseConnectionException e) {
           System.out.println("error connecting to database");
        }
        setNameBand();
        setNameVenue();


        // initialize show picking event listeners

        for(MenuItem m : showHourPicker.getItems())
        {
            m.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    showHourPicker.setText(m.getText());
                }
            });
        }
        for(MenuItem m : showAMPM.getItems())
        {
            m.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    showAMPM.setText(m.getText());
                }
            });
        }
    }

    @FXML
    private TextField bandSearchBar;

    public void setNameBand()
    {
        bandParam = DatabaseConnector.BandCol.B_NAME_LIKE;
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
        venParam = DatabaseConnector.VenueCol.V_NAME_LIKE;
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


    public void searchVenues()
    {
        String searchkey = venueSearchBar.getCharacters().toString();
        TreeItem<String> venuesToDisplay = new TreeItem<String>("Venue search:");
        venuesToDisplay.setExpanded(true);

        try {
            ArrayList<Venue> venues = DatabaseConnector.getInstance().getVenuesBy(venParam, searchkey);
            // System.out.println(bands.get(0).getName());
            for(Venue v : venues)
            {
                TreeItem<String> currVenueItem = new TreeItem<>(v.name);
                currVenueItem.getChildren().add(new TreeItem<>("Street address:\t" + v.address));
                currVenueItem.getChildren().add(new TreeItem<>("City:\t" + v.city));
                currVenueItem.getChildren().add(new TreeItem<>("Email:\t" + v.email));
                currVenueItem.getChildren().add(new TreeItem<>("Genre:\t" + v.type));
                currVenueItem.getChildren().add(new TreeItem<>("Weblink:\t" + v.weblink));

                TreeItem<String> bioItem = new TreeItem<>("Bio");
                bioItem.getChildren().add(new TreeItem<>(v.description));
                bioItem.setExpanded(false);
                currVenueItem.getChildren().add(bioItem);
                currVenueItem.setExpanded(false);
                venuesToDisplay.getChildren().add(currVenueItem);
            }
            consoleTree.setRoot(venuesToDisplay);
        }
        catch(DatabaseConnectionException d)
        {
            System.out.println("Error connecting to database");
        }
        catch(NoResultException nre)
        {
            venuesToDisplay = new TreeItem<String>("No results found for this search input");
        }
        consoleTree.setRoot(venuesToDisplay);
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
                return;
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
                currBandItem.getChildren().add(new TreeItem<>("Email:\t" + b.email));
                currBandItem.getChildren().add(new TreeItem<>("Genre:\t" + b.genre));
                currBandItem.getChildren().add(new TreeItem<>("Weblink:\t" + b.weblink));

                TreeItem<String> bioItem = new TreeItem<>("Bio");
                bioItem.getChildren().add(new TreeItem<>(b.bio));
                bioItem.setExpanded(false);
                currBandItem.getChildren().add(bioItem);
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
    @FXML
    private TreeView<String> myVenuesView;

    public void showMyVenues()
    {
        ArrayList<TreeItem<String>> venuesToDisplay = new ArrayList<>();
        ArrayList<Venue> venuesFromDB;
        try
        {
            venuesFromDB = DatabaseConnector.getInstance().getCurrUsrVenues();
            for(Venue v : venuesFromDB)
            {
                TreeItem<String> currVenueItem = new TreeItem<>(v.name);
                currVenueItem.getChildren().add(new TreeItem<>("Street address:\t" + v.address));
                currVenueItem.getChildren().add(new TreeItem<>("City:\t" + v.city));
                currVenueItem.getChildren().add(new TreeItem<>("Email:\t" + v.email));
                currVenueItem.getChildren().add(new TreeItem<>("Genre:\t" + v.type));
                currVenueItem.getChildren().add(new TreeItem<>("Weblink:\t" + v.weblink));

                TreeItem<String> bioItem = new TreeItem<>("Bio");
                bioItem.getChildren().add(new TreeItem<>(v.description));
                bioItem.setExpanded(false);
                currVenueItem.getChildren().add(bioItem);
                currVenueItem.setExpanded(false);
                venuesToDisplay.add(currVenueItem);
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

        TreeItem<String> root = new TreeItem<>("My Venues");
        for(TreeItem<String> item : venuesToDisplay)
        {
            root.getChildren().add(item);
        }
        root.setExpanded(true);
        myVenuesView.setRoot(root);
    }


    @FXML
    private TextField addShowName;

    @FXML
    private DatePicker addShowDate;

    @FXML
    private TextField addShowBands;

    @FXML
    private TextField addShowVenue;

    @FXML
    private CheckBox isPublicBox;

    @FXML
    private Button addShowButton;

    @FXML
    private Label showTabConsole;

    public void createShow() {
        // first check the date to avoid doing extra work
        showTabConsole.setText("");
        LocalDate date = addShowDate.getValue();
        if (date == null || date.isBefore(LocalDate.now())) {
            showTabConsole.setText("Please select a valid future date");
            return;
        }
        String showName = addShowName.getText();
        String showHour = showHourPicker.getText();
        String showMin = addShowMin.getText();
        boolean pm = showAMPM.getText().equals("PM");
        if(!showMin.matches("^[0-9]{2}") || showHour == "00")
        {
            showTabConsole.setText("please enter a valid time");
            return;
        }
        else
        {
            String stringtime;
            if(pm){
                stringtime = ((Integer)(Integer.parseInt(showHour.replace(":", "")) + 12)).toString() + ":";
            }
            else{
                stringtime = showHour;
            }
            stringtime += showMin + ":00";
            String bandsInput = addShowBands.getText();
            String[] bands = bandsInput.split(", ");
            String venueName = addShowVenue.getText();
            try{
                if(bandsAllExist(bands)){
                    showTabConsole.setText("bands given all exist");
                }
                else{
                    showTabConsole.setText("at least one of the given bands does not exist in the database");
                    return;
                }
                if(!venueExists(venueName))
                {
                    showTabConsole.setText("Given venue does not exist in the database");
                }
                else{
                   try{
                       DatabaseConnector.getInstance().createShow(new Show(
                               showName, date.toString(), stringtime, venueName, isPublicBox.isSelected(), bands
                       ));
                   }
                   catch(NoResultException n)
                   {
                       showTabConsole.setText("something's wrong...");
                   }
                   catch(ShowExistsException s)
                   {
                       showTabConsole.setText("You have already created a show by this name");
                   }
                }

            }
            catch(DatabaseConnectionException d){
                showTabConsole.setText("error connecting to database");
                return;
            }
            showTabConsole.setText("Show created, please note show confirmation functionality is not yet available");
        }
    }

    private boolean bandsAllExist(String[] pBands) throws DatabaseConnectionException
    {
        try{

            for(String band : pBands)
            {
                DatabaseConnector.getInstance().getBandsBy(DatabaseConnector.BandCol.B_NAME_EQ, band);
            }

        }
        catch(NoResultException n)
        {
            return false;
        }
        return true;
    }

    private boolean venueExists(String venueName) throws DatabaseConnectionException
    {
        try
        {
            DatabaseConnector.getInstance().getVenuesBy(DatabaseConnector.VenueCol.V_NAME_EQ, venueName);
        }
        catch(NoResultException n)
        {
            return false;
        }
        return true;
    }

    @FXML
    private TextField venueSearchBar;

    public void searchForShows()
    {
        TreeItem<String> showsToDisplay = new TreeItem<>("Show search:");
        ArrayList<Show> showsFromDB = new ArrayList<>();
        String bandName = bandSearchBar.getText();
        String venueName = venueSearchBar.getText();
        try{
            showsFromDB = DatabaseConnector.getInstance().searchShows(bandName, venueName);
        }
        catch(NoResultException n)
        {
            // no results
        }
        catch(DatabaseConnectionException d)
        {
            System.out.println("Error connecting to database");
        }
        for(Show currshow : showsFromDB)
        {
            TreeItem<String> curr = new TreeItem<>(currshow.name);
            curr.getChildren().add(new TreeItem<>("Venue:\t"+ currshow.venueName));
            curr.getChildren().add(new TreeItem<>("Time:\t" + currshow.time));
            curr.getChildren().add(new TreeItem<>("Is public:\t" + ((Boolean)currshow.isPublic).toString()));
            curr.setExpanded(false);
            showsToDisplay.getChildren().add(curr);
        }
        showsToDisplay.setExpanded(true);
        consoleTree.setRoot(showsToDisplay);
    }
}
