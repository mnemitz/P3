package view;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;


public class Main extends Application {
	private Stage theStage;
	private SplitPane rootPane;
	private static TabPane mainTabPane;


	@Override
	public void start(Stage primaryStage) throws Exception {

		try {
			theStage = primaryStage;
			rootPane = (SplitPane) FXMLLoader.load(Main.class.getResource("Login.fxml"));
			Scene loginScene = new Scene(rootPane);
			theStage.setScene(loginScene);
			theStage.setTitle("Concert Hog Prototype");
			theStage.show();
		} catch (Exception e) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
			System.exit(1);
		}

	}


	public static void main(String[] args)
	{
		Application.launch(Main.class, (java.lang.String[])null);
	}
}
