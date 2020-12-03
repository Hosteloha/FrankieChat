package com.frankie_chat.controller;

import java.io.IOException;

import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.frankie_chat.bubble.Bubble;
import com.frankie_chat.bubble.Bubble.POS;
import com.frankie_chat.network.Message;
import com.frankie_chat.network.Server;
import com.frankie_chat.network.UserX;
import com.frankie_chat.utils.AppUtils;
import com.frankie_chat.utils.Define;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
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
	private MenuItem mitem_FileSettings, mitem_FileClose, mitem_connect,
			mitem_host, mitem_shortcuts, mitem_about, mitem_end,
			mitem_clear_chat, mitem_copy_chat, mitem_clear_chat_quick,
			mitem_copy_chat_quick, mitem_view_compact, mitem_view_maximize,
			mitem_view_showlog, mitem_view_shownotes;

	@FXML
	private TabPane tabpane_main;

	@FXML
	private Tab tab_meeting, tab_chat, tab_notes;

	@FXML
	private Button btn_endconn, btn_sendmsg, btn_copynotes, btn_hostmeeting,
			btn_connect;

	@FXML
	private TextArea txtarea_clientmessageinput;

	@FXML
	private TextField txtFld_task0, txtFld_task1, txtFld_task2, txtFld_task3,
			txtFld_task4, txtFld_task5, txtFld_task6, txtFld_task9,
			txtip_ipaddr, txtip_port, mitem_txFld_usrname,
			mitem_txFld_usrname_quick;

	@FXML
	private ScrollPane scrlPane_AppLog;

	@FXML
	private TextFlow txtFlow_AppLog;

	@FXML
	private RadioMenuItem mitem_enterkey_send, mitem_enterkey_send_quick;

	@FXML
	private ListView listview_chatitems;

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

		// Creating default name //TODO Make it editable
		String userName = AppUtils.initializeDefaultUserName();
		mitem_txFld_usrname.setText(userName);
		mitem_txFld_usrname.setDisable(true);
		mitem_txFld_usrname_quick.setText(userName);
		mitem_txFld_usrname_quick.setDisable(true);
		recordAppLog("Deafult user name created as : " + userName, Level.INFO);
	}

	public void recordAppLog(String appLog, Level logLevel) {
		Logger.getLogger(MainController.class.getSimpleName()).log(logLevel,
				appLog);
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

			txtFlow_AppLog.getChildren()
					.addListener((ListChangeListener<Node>) ((change) -> {
						txtFlow_AppLog.requestLayout();
						scrlPane_AppLog.setVvalue(1.0);
					}));
			txtFlow_AppLog.getChildren().add(text);
		});
	}

	public void initListeners() {
		// Buttons
		btn_endconn.setOnAction(mEventHandler);
		btn_sendmsg.setOnAction(mEventHandler);
		btn_copynotes.setOnAction(mEventHandler);
		btn_hostmeeting.setOnAction(mEventHandler);
		btn_connect.setOnAction(mEventHandler);
		// Menu items
		mitem_host.setOnAction(mEventHandler);
		mitem_connect.setOnAction(mEventHandler);
		mitem_end.setOnAction(mEventHandler);
		txtarea_clientmessageinput
				.setOnKeyPressed(new EventHandler<KeyEvent>() {
					@Override
					public void handle(KeyEvent event) {
						if (event.getCode().equals(KeyCode.ENTER)
								&& event.isShiftDown()) {
							btn_sendmsg.fire();
						}
					}
				});
		// listview_chatitems.setMouseTransparent(true);
		// listview_chatitems.setFocusTraversable(false);
		listview_chatitems.getItems()
				.addListener((ListChangeListener<Node>) ((change) -> {
					int items_size = listview_chatitems.getItems().size() - 1;
					listview_chatitems.scrollTo(items_size);
				}));
	}

	private UserX mUser = null;
	private Server mServer = null;
	private Thread mClientThread = null;
	private Thread mServerThread = null;
	ServerSocket serSocket = null;
	private boolean isServerRunning = false;

	EventHandler<ActionEvent> mEventHandler = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent event) {
			System.out.println("Clicked :: " + event.getSource());
			/**
			 * BTN_CLEAR MESSAGE : to clear the messages off the chat window
			 */
			if (event.getSource() == mitem_clear_chat
					|| event.getSource() == mitem_clear_chat_quick) {
				System.out.println("btn_clearmsgs clicked");
				// TODO to remove messages from scroll pane
			}
			/**
			 * BTN_END CONNECTION : to end the chat communication
			 */
			if (event.getSource() == btn_endconn
					|| event.getSource() == mitem_end) {
				System.out.println("btn_endconn clicked");
				closeResource();
				disableViews(false);
			}
			/**
			 * BTN_SEND : to broadcast the message to the server
			 */
			if (event.getSource() == btn_sendmsg) {
				System.out.println("btn_sendmsg clicked");
				String userMessage = txtarea_clientmessageinput.getText()
						.trim();
				if (userMessage.length() > 0) {
					userMessage = userMessage.trim();
					// Remove empty lines if present any
					userMessage = userMessage.replaceAll("(?m)^[ \t]*\r?\n",
							"");
					// Remove new lines with spaces and dots
					userMessage = userMessage.replaceAll("[\\t\\n\\r]+", " | ");
					// Create JSON Message object
					Message message = new Message();
					message.setContent(userMessage);
					message.setOwner(AppUtils.getUSERNAME());

					if (mUser != null) {
						message.setServer(false);
						// Convert to GSON
						String jsonMessage = AppUtils
								.createJsonMessage(message);
						mUser.sendData(jsonMessage);
					}
					if (mServer != null) {
						message.setServer(true);
						// Convert to GSON
						String jsonMessage = AppUtils
								.createJsonMessage(message);
						mServer.sendServerData(jsonMessage);
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
			if (event.getSource() == mitem_host
					|| event.getSource() == btn_hostmeeting) {
				disableViews(true);
				System.out.println("mitem_host clicked");
				txtip_ipaddr.setText("localhost");
				int port = 2003;
				String portText = txtip_port.getText();
				if (portText != null && portText.length() == 4) {
					port = Integer.parseInt(txtip_port.getText());
				} else {
					txtip_port.setText("2003");
				}
				// Enabling server
				isServerRunning = true;
				try {
					final Integer finalPort = new Integer(port);
					mServerThread = new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								serSocket = new ServerSocket(finalPort);
							} catch (BindException e) {
								e.printStackTrace();
								Alert alert = new Alert(AlertType.ERROR,
										"Already port in use, \nPlease ABORT/END and try some other port",
										ButtonType.OK);
								alert.getDialogPane()
										.setMinHeight(Region.USE_PREF_SIZE);
								alert.show();
							} catch (IOException e) {
								e.printStackTrace();
							}
							while (isServerRunning) {
								System.out.println("Server running...");
								Socket socket = null;
								try {
									socket = serSocket.accept();
								} catch (IOException e) {
									e.printStackTrace();
								}
								mServer = new Server(socket);
								Thread thread = new Thread(mServer);
								thread.start();
							}
						}
					});
					mServerThread.start();
					recordAppLog("Host running at port " + finalPort
							+ " Address : " + InetAddress.getLocalHost(),
							Level.SEVERE);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			/**
			 * MENU ITEM : CONNECT CLIENT TO SERVER ; Connect user to Server
			 * host
			 */
			if (event.getSource() == mitem_connect
					|| event.getSource() == btn_connect) {
				disableViews(true);
				System.out.println("mitem_host clicked");
				String ip_address = txtip_ipaddr.getText();
				int port = 2003;
				String portText = txtip_port.getText();
				if (portText != null && portText.length() == 4) {
					port = Integer.parseInt(txtip_port.getText());
				} else {
					txtip_port.setText("2003");
				}
				try {
					final Integer finalPort = new Integer(port);
					final String finalIpAddr = new String(ip_address);

					mClientThread = new Thread(new Runnable() {
						@Override
						public void run() {
							mUser = new UserX(finalIpAddr, finalPort);
							Thread t1 = new Thread(mUser);
							t1.start();
						}
					});
					mClientThread.start();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	};

	public void testBubbleFeature(String messageContent, String userName,
			Bubble.POS position) {
		Bubble bl = null;
		if (userName == null) {
			bl = new Bubble(messageContent);
		} else {
			bl = new Bubble(messageContent, userName);
		}
		HBox hbox = new HBox();
		hbox.getChildren().addAll(bl);
		switch (position) {
			case LEFT :
				hbox.setAlignment(Pos.BASELINE_LEFT);
				break;
			case RIGHT :
				hbox.setAlignment(Pos.BASELINE_RIGHT);
				break;
			case CENTER :
				hbox.setAlignment(Pos.BASELINE_CENTER);
			default :
				break;
		}
		// To avoid illegal state exception
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				listview_chatitems.getItems().add(hbox);
			}
		});
	}

	public void updateChatArea(String userMessage, boolean isJson) {
		if (isJson) {
			Message message = AppUtils.getJsonObject(userMessage);
			String messageOwner = message.getOwner();
			String currentUser = AppUtils.getUSERNAME();
			POS position = null;

			if (messageOwner.contentEquals(currentUser)) {
				// then some other owner; not your message
				position = Bubble.POS.RIGHT;
			} else {
				position = Bubble.POS.LEFT;
			}

			testBubbleFeature(message.getContent(), message.getOwner(),
					position);
		} else {
			testBubbleFeature(userMessage, null, Bubble.POS.CENTER);
		}
	}

	public void closeResource() {
		// Close client connection, else we will get socket connection close
		if (mUser != null) {
			mUser.closeResources();
		}
		// End host socket
		isServerRunning = false;
		if (mServer != null) {
			mServer.closeResources();

		}
		if (mClientThread != null) {
			mClientThread.stop();
		}
		if (serSocket != null) {
			try {
				serSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (mServerThread != null) {
			mServerThread.stop();
		}

		recordAppLog("Connection END, Resources closed", Level.SEVERE);
	}

	public void disableViews(boolean isDisable) {
		txtip_port.setDisable(isDisable);
		txtip_ipaddr.setDisable(isDisable);
		btn_connect.setDisable(isDisable);
		btn_hostmeeting.setDisable(isDisable);

	}
}
