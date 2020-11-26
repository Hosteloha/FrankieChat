package com.frankie_chat.controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.frankie_chat.network.Server;
import com.frankie_chat.network.UserX;
import com.frankie_chat.utils.Define;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class MainController implements Initializable {

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
	private Label lbl_connection, lbl_port, lb_users;

	@FXML
	private TextArea txtarea_LogSub, txtarea_clientmessageinput;

	@FXML
	private TextField txtFld_task0, txtFld_task1, txtFld_task2, txtFld_task3, txtFld_task4, txtFld_task5, txtFld_task6,
			txtFld_task9;

	@FXML
	private ScrollPane scrlPane_AppLog;

	@FXML
	private TextFlow txtFlow_AppLog;

	private static MainController mController = null;

	public static MainController getmController() {
		return mController;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		mController = this;
		recordAppLog("MainController :: initialized", Level.INFO);
		recordAppLog(Define.app_info, Level.INFO);
		initListeners();
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

	public void initListeners() {
		// Buttons
		btn_copymsgs.setOnAction(mEventHandler);
		btn_clearmsgs.setOnAction(mEventHandler);
		btn_endconn.setOnAction(mEventHandler);
		btn_sendmsg.setOnAction(mEventHandler);
		btn_copynotes.setOnAction(mEventHandler);
		// Menu items
		mitem_host.setOnAction(mEventHandler);
		mitem_connect.setOnAction(mEventHandler);
	}

	private UserX mUser = null;
	private Server mServer = null;
	EventHandler<ActionEvent> mEventHandler = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent event) {
			System.out.println("Clicked :: " + event.getSource());
			/**
			 * BTN_COPY MESSAGE : to copy the content to clipboard
			 */
			if (event.getSource() == btn_copymsgs) {
				System.out.println("btn_copymsgs clicked");
			}
			/**
			 * BTN_CLEAR MESSAGE : to clear the messages off the chat window
			 */
			if (event.getSource() == btn_clearmsgs) {
				System.out.println("btn_clearmsgs clicked");
				txtarea_LogSub.setText("");
			}
			/**
			 * BTN_END CONNECTION : to end the chat communication
			 */
			if (event.getSource() == btn_endconn) {
				System.out.println("btn_endconn clicked");
			}
			/**
			 * BTN_SEND : to broadcast the message to the server
			 */
			if (event.getSource() == btn_sendmsg) {
				System.out.println("btn_sendmsg clicked");
				String userMessage = txtarea_clientmessageinput.getText().trim();
				if (userMessage.length() > 0) {
					if (mUser != null) {
						mUser.sendData(userMessage);
					}
					if (mServer != null) {
						mServer.sendServerData(userMessage);
					}
				}
				txtarea_clientmessageinput.setText("");
			}
			/**
			 * BTN_COPY NOTES : to copy the notes to clipboard
			 */
			if (event.getSource() == btn_copynotes) {
				System.out.println("btn_copynotes clicked");
			}
			/**
			 * MENU ITEM : HOST SERVER ; to host server
			 */
			if (event.getSource() == mitem_host) {
				System.out.println("mitem_host clicked");
				try {
					new Thread(() -> {
						ServerSocket serSocket = null;
						try {
							serSocket = new ServerSocket(2003);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						while (true) {
							System.out.println("Server running...");
							Socket socket = null;
							try {
								socket = serSocket.accept();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							mServer = new Server(socket);
							Thread thread = new Thread(mServer);
							thread.start();
						}
					}).start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			/**
			 * MENU ITEM : CONNECT SERVER ; Connect user to Server host
			 */
			if (event.getSource() == mitem_connect) {
				System.out.println("mitem_host clicked");
				try {
					new Thread(() -> {
						String host = "localhost";
						int port = 2003;
						mUser = new UserX(host, port);
						Thread t1 = new Thread(mUser);
						t1.start();
					}).start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	};

	public void updateChatArea(String userMessage) {
		userMessage = userMessage.trim();
		System.out.println("User entered : " + userMessage);
		txtarea_LogSub.appendText("> " + userMessage + "\n");
	}

}
