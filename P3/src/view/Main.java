package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;


public class Main extends Application {
	private Stage theStage;
	private TabPane rootPane;

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			theStage = primaryStage;
			rootPane = (TabPane) FXMLLoader.load(Main.class.getResource("CHMainView.fxml"));
			Scene scene = new Scene(rootPane);
			theStage.setScene(scene);
			theStage.setTitle("Concert Hog Prototype");
			theStage.show();
		} catch (Exception e) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
			System.exit(1);
		}

	}

}
	
/*	public static void main(String[] args)
	{
		// launch the application, give it casted args to make the angry java monster happy
		Application.launch(Main.class, (java.lang.String[])null);
		DatabaseConnector connector = new DatabaseConnector();
	}
}*/
