package com.frankie_chat;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

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
			URL url_fxml = new File("src/main/java/com/frankie_chat/MainUI.fxml").toURI().toURL();
			VBox root = (VBox) FXMLLoader.load(url_fxml);
			Scene scene = new Scene(root, 1000, 700);
			URL url_css = new File("src/main/java/com/frankie_chat/application.css").toURI().toURL();
			scene.getStylesheets().add(url_css.toExternalForm());
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
			URL url_icon = new File("src/main/java/com/frankie_chat/app_icon.png").toURI().toURL();
			Image image = new Image(url_icon.toExternalForm());
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