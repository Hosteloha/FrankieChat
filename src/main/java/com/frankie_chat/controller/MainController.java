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
import javafx.scene.control.skin.VirtualFlow;
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
			mitem_view_showlog, mitem_view_shownotes, mitem_clearlog,
			mitem_copylog, mitem_clearlog_quick, mitem_copylog_quick;

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
			txtFld_task4, txtFld_task5, txtFld_task6, txtFld_task7,
			txtip_ipaddr, txtip_port, mitem_txFld_usrname,
			mitem_txFld_usrname_quick;

	@FXML
	private ScrollPane scrlPane_AppLog;

	@FXML
	private TextFlow txtFlow_AppLog;

	@FXML
	private RadioMenuItem mitem_enterkey_send, mitem_enterkey_send_quick;

	@FXML
	private ListView<HBox> listview_chatitems;

	private TextField[] txtFieldArr_MeetingNotes = null;

	private static MainController mController = null;

	public static MainController getmController() {
		return mController;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		mController = this;
		recordAppLog("MainController :: initialized", Level.INFO);
		recordAppLog(Define.app_info, Level.INFO);
		// Initializing variables
		txtFieldArr_MeetingNotes = new TextField[]{txtFld_task0, txtFld_task1,
				txtFld_task2, txtFld_task3, txtFld_task4, txtFld_task5,
				txtFld_task6, txtFld_task7};

		initListeners();

		// Creating default name //TODO Make it editable
		String userName = AppUtils.initializeDefaultUserName();
		mitem_txFld_usrname.setText(userName);
		mitem_txFld_usrname.setDisable(true);
		mitem_txFld_usrname_quick.setText(userName);
		mitem_txFld_usrname_quick.setDisable(true);
		recordAppLog("Default user name created as : " + userName, Level.INFO);
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
		mitem_enterkey_send.setOnAction(mEventHandler);
		mitem_enterkey_send_quick.setOnAction(mEventHandler);
		// Clear the windows
		mitem_clear_chat.setOnAction(mEventHandler);
		mitem_clear_chat_quick.setOnAction(mEventHandler);
		mitem_clearlog.setOnAction(mEventHandler);
		mitem_clearlog_quick.setOnAction(mEventHandler);
		// Copy content to clipboard
		mitem_copy_chat.setOnAction(mEventHandler);
		mitem_copy_chat_quick.setOnAction(mEventHandler);
		mitem_copylog.setOnAction(mEventHandler);
		mitem_copylog_quick.setOnAction(mEventHandler);
		btn_copynotes.setOnAction(mEventHandler);
		// KeyEvent define
		keyEventListeners();
		// listview_chatitems.setMouseTransparent(true);
		// listview_chatitems.setFocusTraversable(false);
		listview_chatitems.getItems()
				.addListener((ListChangeListener<Node>) ((change) -> {
					int items_size = listview_chatitems.getItems().size() - 1;
					listview_chatitems.scrollTo(items_size);
				}));
	}

	private void setEnterKeyOption(boolean isEnabled) {
		mitem_enterkey_send.setSelected(isEnabled);
		mitem_enterkey_send_quick.setSelected(isEnabled);
		AppUtils.setEnterKeyEnabled(isEnabled);
	}

	private void keyEventListeners() {
		// Checking enter key state.
		setEnterKeyOption(AppUtils.isEnterKeyEnabled());
		// Altering entering, shift+Enter, ALt+enter
		txtarea_clientmessageinput
				.setOnKeyPressed(new EventHandler<KeyEvent>() {
					@Override
					public void handle(KeyEvent event) {
						if ((event.getCode().equals(KeyCode.ENTER)
								&& event.isShiftDown())
								|| (event.getCode().equals(KeyCode.ENTER)
										&& event.isAltDown())) {
							String new_line = System.lineSeparator();
							txtarea_clientmessageinput.appendText(new_line);
							// btn_sendmsg.fire();
						}

						if (event.getCode().equals(KeyCode.ENTER)
								&& !(event.isShiftDown()
										|| event.isAltDown())) {
							if (AppUtils.isEnterKeyEnabled()) {
								btn_sendmsg.fire();
								txtarea_clientmessageinput.setText("");
								event.consume();
							}
						}
					}
				});

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
				listview_chatitems.getItems().clear();
			}
			/**
			 * BTN_CLEAR MESSAGE : to clear the messages off the chat window
			 */
			if (event.getSource() == mitem_clearlog
					|| event.getSource() == mitem_clearlog_quick) {
				System.out.println("btn_clearlogs clicked");
				txtFlow_AppLog.getChildren().clear();
			}
			/**
			 * BTN_COPY NOTES : to copy meeting notes in a point wise manner
			 */
			if (event.getSource() == btn_copynotes) {
				System.out.println("btn_copy notes clicked");
				StringBuilder meetingNotesContent = new StringBuilder();
				int noteIndex = 1;
				meetingNotesContent.append(
						"- Meeting Notes : " + AppUtils.getCurrentTimeStamp()
								+ " -" + System.lineSeparator());
				for (int i = 0; i < txtFieldArr_MeetingNotes.length; i++) {
					if (txtFieldArr_MeetingNotes[i] != null) {
						String meeting_note = txtFieldArr_MeetingNotes[i]
								.getText();

						if (meeting_note != null && meeting_note.length() > 0) {
							meeting_note = meeting_note.trim();
							meetingNotesContent.append(noteIndex + ". "
									+ meeting_note + System.lineSeparator());
							noteIndex = noteIndex + 1;
						}
					}
				}
				AppUtils.setClipBoardContent(meetingNotesContent.toString());
				recordAppLog(meetingNotesContent.toString(), Level.INFO);
				recordAppLog("Meeting notes copied to your clipboard !",
						Level.SEVERE);
			}
			/**
			 * BTN_COPY LOG : to clear the messages off the chat window
			 */
			if (event.getSource() == mitem_copylog
					|| event.getSource() == mitem_copylog_quick) {
				System.out.println("btn_copy logs clicked");
				StringBuilder logContent = new StringBuilder();
				int textFlowSize = txtFlow_AppLog.getChildren().size();
				logContent.append(
						"- Log Content : " + AppUtils.getCurrentTimeStamp()
								+ " -" + System.lineSeparator());
				for (int i = 0; i < textFlowSize; i++) {
					Node node = txtFlow_AppLog.getChildren().get(i);
					if (node instanceof Text) {
						logContent.append(((Text) node).getText());
					}
				}
				AppUtils.setClipBoardContent(logContent.toString());
				recordAppLog("Log copied to your clipboard !", Level.SEVERE);
			}
			/**
			 * BTN_COPY CHAT : to clear the messages off the chat window
			 */
			if (event.getSource() == mitem_copy_chat
					|| event.getSource() == mitem_copy_chat_quick) {
				System.out.println("btn_copy chats clicked");
				StringBuilder chatContent = new StringBuilder();
				int listViewSize = listview_chatitems.getItems().size();
				chatContent.append(
						"- Chat Content : " + AppUtils.getCurrentTimeStamp()
								+ " -" + System.lineSeparator());

				for (int i = 0; i < listViewSize; i++) {
					Node node_hbox = listview_chatitems.getItems().get(i);
					if (node_hbox instanceof HBox) {
						Bubble node_bubble = (Bubble) ((HBox) node_hbox)
								.getChildrenUnmodifiable().get(0);
						chatContent.append(" MSG > " + node_bubble.getMetaText()
								+ " :  " + node_bubble.getMainText()
								+ System.lineSeparator());
					}
				}
				AppUtils.setClipBoardContent(chatContent.toString());
				recordAppLog("Chat copied to your clipboard !", Level.SEVERE);
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
			/**
			 * MENU ITEM : ENTER KEY SEND
			 */
			if (event.getSource() == mitem_enterkey_send
					|| event.getSource() == mitem_enterkey_send_quick) {
				boolean isSelected = ((RadioMenuItem) event.getSource())
						.isSelected();
				setEnterKeyOption(isSelected);
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
