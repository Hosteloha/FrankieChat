package com.frankie_chat.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;

import com.frankie_chat.controller.MainController;

public class UserX implements Runnable {

	BufferedWriter writer = null;
	BufferedReader reader = null;

	private Socket socketClient = null;

	public UserX(String host, int port) {
		try {
			socketClient = new Socket(host, port);
			if (socketClient != null) {
				writer = new BufferedWriter(
						new OutputStreamWriter(socketClient.getOutputStream()));
				reader = new BufferedReader(
						new InputStreamReader(socketClient.getInputStream()));
			}
		} catch (SocketException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendData(String message) {
		try {
			// Write to OP_stream
			if (writer != null) {
				writer.write(message);
				writer.write("\r\n");
				writer.flush();
			} else {
				MainController.getmController()
						.recordAppLog("Could not send to server", Level.SEVERE);
			}
		} catch (SocketException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			String msg = "";
			MainController.getmController().updateChatArea("-- Connected --",
					false);
			while ((msg = reader.readLine()) != null) {
				MainController.getmController().updateChatArea(msg, true);
			}
			MainController.getmController()
					.updateChatArea("-- Connection End --", false);
		} catch (SocketException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void closeResources() {
		System.out.println("Client ended");
		try {
			if (writer != null) {
				writer.close();
			}
			if (reader != null) {
				reader.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
