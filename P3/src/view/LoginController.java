package view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.DatabaseConnector;
import model.NoResultException;
import model.User;
import model.UserExistsException;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Matt Nemitz on 3/26/2017.
 */
public class LoginController implements Initializable{

    @FXML
    private Label loginFailLabel;

    @FXML
    private TextField emailIn;

    @FXML
    private PasswordField pwdf;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private Button loginButton;

    public void attemptLogIn()
    {
        boolean isValid = false;
        loginFailLabel.setText(""); //refresh
        String loginEmail = emailIn.getText();
        String givenPwd = pwdf.getText();
        User thisUsr = null;
        if(loginEmail.equals(""))
        {
            loginFailLabel.setText("Please provide a username and password");
            return;
        }
        try{
            thisUsr = DatabaseConnector.getInstance().getUserByEmail(loginEmail);
            isValid = thisUsr.isPassword(givenPwd);
        }
        catch(NoResultException nre)
        {
            System.out.println("No result exception thrown");
        }


        if(thisUsr != null & isValid)
        {
            logIn(thisUsr.getEmail());
            return;
        }
        else
        {
            loginFailLabel.setText("Sorry, could not find user/password");
            return;
        }
    }

    private void logIn(String usrEmail)
    {
        Stage ourStage = (Stage) loginFailLabel.getScene().getWindow();
        DatabaseConnector.getInstance().setSessionUserID(usrEmail);
        try {
            TabPane mainTabPane = (TabPane) FXMLLoader.load(Main.class.getResource("CHMainView.fxml"));
            Scene mainScene = new Scene(mainTabPane);
            ourStage.setScene(mainScene);
            ourStage.setTitle("Concert Hog Prototype");
            ourStage.show();
        } catch (Exception e) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
            System.exit(1);
        }
    }

    @FXML
    private TextField createUserName;

    @FXML
    private TextField createUserEmail;

    @FXML
    private PasswordField createUserPassword;

    @FXML
    private Label createAcctFailLabel;

    public void createUser() {
        String emailToCreate = createUserEmail.getText();
        String nameToCreate = createUserName.getText();
        String passwdToCreate = createUserPassword.getText();
        if((!emailToCreate.equals(""))&&(!nameToCreate.equals(""))&&(!passwdToCreate.equals("")))
        {
            try{
                DatabaseConnector.getInstance().addUser(new User(
                        createUserName.getText(),
                        createUserEmail.getText(),
                        createUserPassword.getText()
                ), createUserPassword.getText());
                createAcctFailLabel.setText("User created, log in on other side");
            }
            catch(UserExistsException usre)
            {
                createAcctFailLabel.setText("There is already a user with that email");
            }
        }
        else{
            createAcctFailLabel.setText("Please fill in all the fields");
            return;
        }

    }
}
