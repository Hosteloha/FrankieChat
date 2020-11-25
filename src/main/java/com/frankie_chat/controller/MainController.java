package com.frankie_chat.controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.frankie_chat.utils.SystemInfo;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class MainController implements Initializable {

	@FXML
	private ScrollPane scrlPane_AppLog;

	@FXML
	private TextFlow txtFlow_AppLog;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		recordAppLog("MainController :: initialized", Level.INFO);
		String javaVersion = SystemInfo.javaVersion();
		String javafxVersion = SystemInfo.javafxVersion();
		String app_info = ("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
		recordAppLog(app_info, Level.INFO);
	}

	public void recordAppLog(String appLog, Level logLevel) {
		Logger.getLogger(MainController.class.getSimpleName()).log(logLevel, appLog);
		Platform.runLater(() -> {
			Text text = new Text("> " + appLog + "\n");
			text.setStyle("-fx-font: 13 consolas;");
			text.setFontSmoothingType(FontSmoothingType.LCD);
			if (logLevel == Level.INFO) {
				text.setFill(Color.LIGHTSTEELBLUE);
			} else if (logLevel == Level.SEVERE || logLevel == Level.WARNING) {
				text.setFill(Color.PALEVIOLETRED);
			} else {
				text.setFill(Color.LIGHTGRAY);
			}

			txtFlow_AppLog.getChildren().addListener((ListChangeListener<Node>) ((change) -> {
				txtFlow_AppLog.requestLayout();
				scrlPane_AppLog.setVvalue(1.0);
			}));
			txtFlow_AppLog.getChildren().add(text);
		});
	}

}
