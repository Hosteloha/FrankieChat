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

import com.frankie_chat.utils.Define;

/**
 * JavaFX App 
 */
public class App extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
			URL url_fxml = new File(Define.path_fxml).toURI().toURL();
			VBox root = (VBox) FXMLLoader.load(url_fxml);
			Scene scene = new Scene(root, 1000, 700);
			URL url_css = new File(Define.path_css).toURI().toURL();
			scene.getStylesheets().add(url_css.toExternalForm());
			setApplicationIcon(primaryStage);
			primaryStage.setTitle(Define.str_title);
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
			URL url_icon = new File(Define.path_app_icn).toURI().toURL();
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