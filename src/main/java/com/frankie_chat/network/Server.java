package com.frankie_chat.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.Vector;

import com.frankie_chat.controller.MainController;

public class Server implements Runnable {

	Socket socket = null;

	public static Vector<BufferedWriter> client = new Vector<BufferedWriter>();

	public Server(Socket socket) {
		try {
			this.socket = socket;
		} catch (Exception e) {
			e.printStackTrace();
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
							MainController.getmController().updateChatArea(data);
							for (int i = 0; i < client.size(); i++) {
								try {
									BufferedWriter bw = (BufferedWriter) client.get(i);
									bw.write(data);
									bw.write("\r\n");
									bw.flush();
								} catch (SocketException e) {
								} catch (Exception e) {
									e.printStackTrace();
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

		} catch (SocketException e) {
		} catch (Exception e) {
			e.printStackTrace();
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
							} catch (SocketException e) {
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}

				} else {
					System.out.println("Client is NULL");
				}

			} else {
				System.out.println("Socket is NULL");
			}

		} catch (SocketException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void closeResources() {
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
