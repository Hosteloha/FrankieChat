package com.frankie_chat.utils;

public class Define {
	/**
	 * Application Strings
	 */
	public static final String str_title = "Frankie_Chat : Let's Talk";

	/**
	 * Resource paths
	 */
	public static final String path_fxml = "src/main/java/com/frankie_chat/MainUI.fxml";
	public static final String path_css = "src/main/java/com/frankie_chat/application.css";
	public static final String path_app_icn = "src/main/java/com/frankie_chat/app_icon.png";

	/**
	 * Logical strings
	 */
	public static final String javaVersion = SystemInfo.javaVersion();
	public static final String javafxVersion = SystemInfo.javafxVersion();
	public static final String app_info = ("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
}
