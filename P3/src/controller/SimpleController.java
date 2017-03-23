package controller;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TreeView;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;


public class SimpleController implements Initializable
{
	// TODO: put static model driver here
	
	@FXML
	private TabPane rootPane;
	
	// Children tabs of rootPane:
	
	@FXML
	private Tab searchTab;
	
	@FXML
	private Tab modifyTab;
	
	@FXML
	private TextField bandSearchBar;
	
	@FXML
	private TextField venueSearchBar;
	
	@FXML
	private TreeView consoleTreeView;
	
	@FXML
	private SplitMenuButton bandsBy;
	
	@FXML
	private SplitMenuButton venuesBy;
	
	public void initialize(URL location, ResourceBundle resources) 
	{
		
	}	
}