package com.frankie_chat.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.frankie_chat.network.Message;
import com.github.javafaker.Faker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public class AppUtils {

	private static String USERNAME = null;
	private static boolean isEnterKeyEnabled = Define.defaultEnterKeyEnable;

	public static boolean isEnterKeyEnabled() {
		return isEnterKeyEnabled;
	}

	public static void setEnterKeyEnabled(boolean isEnterKeyEnabled) {
		AppUtils.isEnterKeyEnabled = isEnterKeyEnabled;
	}

	public static String getUSERNAME() {
		return USERNAME;
	}

	public static void setUSERNAME(String uSERNAME) {
		USERNAME = uSERNAME;
	}

	public static String javaVersion() {
		return System.getProperty("java.version");
	}

	public static String javafxVersion() {
		return System.getProperty("javafx.version");
	}

	public static String createJsonMessage(Message message) {
		Gson gson = new GsonBuilder().create();
		return gson.toJson(message);
	}

	public static Message getJsonObject(String jsonString) {
		System.out.println(" JSON string :: " + jsonString);
		Gson gson = new GsonBuilder().create();
		Message message = gson.fromJson(jsonString, Message.class);
		return message;
	}

	public static String initializeDefaultUserName() {
		Faker faker = new Faker();
		String firstName = faker.name().firstName();
		setUSERNAME(firstName);
		return getUSERNAME();
	}

	public static void setClipBoardContent(String content) {
		final ClipboardContent clipboardContent = new ClipboardContent();
		clipboardContent.putString(content);
		Clipboard.getSystemClipboard().setContent(clipboardContent);
	}

	public static String getCurrentTimeStamp() {
		SimpleDateFormat formatter = new SimpleDateFormat(
				"dd/MM/yyyy HH:mm:ss");
		Date date = new Date();
		return formatter.format(date);
	}
}