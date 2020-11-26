package com.frankie_chat.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.frankie_chat.controller.MainController;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;

public class UserX implements Runnable {

	BufferedWriter writer = null;
	BufferedReader reader = null;

	public UserX(String host, int port) {
		try {
			Socket socketClient = new Socket(host, port);
			if (socketClient != null) {
				writer = new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream()));
				reader = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR, "Error while creating the user socket", ButtonType.OK);
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.show();
		}

	}

	public void sendData(String message) {
		System.out.println("Sending data to server");
		String str = message;
		try {
			if (writer != null) {
				writer.write(str);
				writer.write("\r\n");
				writer.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR, "Error while sending data to host", ButtonType.OK);
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.show();
		}
	}

	public void run() {
		try {
			String msg = "";
			while ((msg = reader.readLine()) != null) {
				MainController.getmController().updateChatArea(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR, "Error while running the user thread", ButtonType.OK);
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.show();
		}
	}

	public static void main(String[] args) {
		String host = "localhost";
		int port = 2003;
		UserX one = new UserX(host, port);
		Thread t1 = new Thread(one);
		t1.start();
	}

}
