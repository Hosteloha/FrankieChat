package com.frankie_chat;

import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.TextFlow;

/**
 * JavaFX App 
 */
public class App extends Application {

	@FXML
	private Menu menu_File, menu_Themes, menu_Help;

	@FXML
	private RadioMenuItem mitem_ThemeLight, mitem_ThemeHacker;

	@FXML
	private ToggleGroup theme_group;

	@FXML
	private MenuItem mitem_FileSettings, mitem_FileClose, mitem_connect, mitem_host, mitem_shortcuts, mitem_about;

	@FXML
	private TabPane tabpane_main;

	@FXML
	private Tab tab_meeting, tab_chat, tab_notes;

	@FXML
	private Button btn_copymsgs, btn_clearmsgs, btn_endconn, btn_sendmsg, btn_copynotes;

	@FXML
	private TextArea txtarea_LogSub, txtarea_clientmessageinput;

	@FXML
	private TextField txtFld_task0, txtFld_task1, txtFld_task2, txtFld_task3, txtFld_task4, txtFld_task5, txtFld_task6,
			txtFld_task9;

	@FXML
	private ScrollPane scrlPane_AppLog;

	@FXML
	private TextFlow txtFlow_AppLog;

	@Override
	public void start(Stage primaryStage) {
		try {
			VBox root = (VBox) FXMLLoader.load(getClass().getResource("MainUI.fxml"));
			Scene scene = new Scene(root, 1000, 700);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			setApplicationIcon(primaryStage);
			primaryStage.setTitle(" Frankie_Chat : Anonymous chat communicator");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	public void setApplicationIcon(Stage primaryStage) {
		try {
			// image attribution "Icon made by Pixel perfect from www.flaticon.com"
			String appIconPath = "app_icon.png";
			Image image = new Image(getClass().getResource(appIconPath).toExternalForm());
			primaryStage.getIcons().add(image);
		} catch (Exception e) {
			Logger.getLogger(App.class.getSimpleName()).log(Level.SEVERE, " setApplicationIcon ::" + e.getMessage());
			e.printStackTrace();
		}
	}

//    @Override
//    public void start(Stage stage) {
//        String javaVersion = SystemInfo.javaVersion();
//        String javafxVersion = SystemInfo.javafxVersion();
//
//        Label label = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
//        Scene scene = new Scene(new StackPane(label), 640, 480);
//        stage.setScene(scene);
//        stage.show();
//    }
//

}