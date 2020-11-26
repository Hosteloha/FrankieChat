package com.frankie_chat.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import com.frankie_chat.controller.MainController;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Region;

public class Server implements Runnable {

	Socket socket;

	public static Vector<BufferedWriter> client = new Vector<BufferedWriter>();

	public Server(Socket socket) {
		try {
			this.socket = socket;
		} catch (Exception e) {
			e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR, "Error while instantiating socket", ButtonType.OK);
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.show();
		}
	}

	public void run() {
		try {
			if (socket != null) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				if (client != null) {
					client.add(writer);
					while (true) {
						String data = reader.readLine();
						if (data != null) {
							data = data.trim();
							System.out.println("Received " + data);
							MainController.getmController().updateChatArea(data);
							for (int i = 0; i < client.size(); i++) {
								try {
									BufferedWriter bw = (BufferedWriter) client.get(i);
									bw.write(data);
									bw.write("\r\n");
									bw.flush();
								} catch (Exception e) {
									e.printStackTrace();
									Alert alert = new Alert(AlertType.ERROR, "Error while flushing server thread data",
											ButtonType.OK);
									alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
									alert.show();
								}
							}
						}
					}
				} else {
					System.out.println("Client is NULL");
				}

			} else {
				System.out.println("Socket is NULL");
			}

		} catch (Exception e) {
			e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR, "Error while running server thread", ButtonType.OK);
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.show();
		}

	}

	public void sendServerData(String message) {
		System.out.println("Sending server message");
		try {
			if (socket != null) {
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				if (client != null) {
					if (message != null) {
						message = message.trim();
						System.out.println("Received " + message);
						MainController.getmController().updateChatArea(message);
						for (int i = 0; i < client.size(); i++) {
							try {
								BufferedWriter bw = (BufferedWriter) client.get(i);
								bw.write(message);
								bw.write("\r\n");
								bw.flush();
							} catch (Exception e) {
								e.printStackTrace();
								Alert alert = new Alert(AlertType.ERROR, "Error while flushing server thread data",
										ButtonType.OK);
								alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
								alert.show();
							}
						}
					}

				} else {
					System.out.println("Client is NULL");
				}

			} else {
				System.out.println("Socket is NULL");
			}

		} catch (Exception e) {
			e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR, "Error while running server thread", ButtonType.OK);
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.show();
		}

	}

	public static void createSocket(int port) throws IOException {
		ServerSocket serSocket = new ServerSocket(port);
		while (true) {
			System.out.println("Server running...");
			Socket socket = serSocket.accept();
			Server server = new Server(socket);
			Thread thread = new Thread(server);
			thread.start();
		}
	}

	public static void main(String[] args) throws Exception {
		ServerSocket s = new ServerSocket(2003);
		while (true) {
			Socket socket = s.accept();
			Server server = new Server(socket);
			Thread thread = new Thread(server);
			thread.start();
		}
	}
}
