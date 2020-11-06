/*
 * FILE NAME: MyJavaFXBrowser.java
 * AUTHOR: Zaid Sweidan, 040887956
 * COURSE: CST884 – OOP 
 * Assignment: 2
 * DATE: January 12, 2018
 * PROFESSOR: Raymond Peterkin
 * PURPOSE: This file contains a class that runs a JavaFX application which is responsible for launching
 * 	and loading various components of the web browser.
 *
 * REFERENCES:
 * https://stackoverflow.com/questions/42598097/using-javafx-application-stop-method-over-shutdownhook
 */

package assignment2;

import java.io.File;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 * This class is the JavaFX application that displays the browser and all of its menus.
 * Execution of the browser is done here and its various components are loaded onto the scene.
 * @author David B. Houtman, Zaid Swedian
 * @version 2
 * @see assignment2
 * @since JavaSE-1.8
 */
public class MyJavaFXBrowser extends Application {
	
	/**
	 * Overrides the start method and loads a Stage object on to it
	 * @param primaryStage primary Stage instance
	 */
	@Override
	public void start(Stage primaryStage) {
		
	    WebPage currentPage = new WebPage();
		WebView webView = currentPage.getWebView();
		WebEngine webEngine = currentPage.createWebEngine(primaryStage);
		
		BorderPane root = new BorderPane();
		VBox vBox = new VBox();
		
		if (FileUtils.fileExists("default.web"))
			webEngine.load(FileUtils.getFileContentsAsArrayList(new File("default.web")).get(0));
		else {
			FileUtils.saveFile("default.web", "http://www.google.ca");
			webEngine.load("http://www.google.ca");
		}
		
		MenuBar menus = Menus.getMenuBar(webEngine, vBox, root);
		vBox.getChildren().add(menus);
		
		root.setCenter(webView);
		root.setTop(vBox);
		
		
		Scene scene = new Scene(root, 800, 500);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	/**
	 * Main method that launches the application
	 * @param args String[]
	 */
	public static void main(String[] args) {
		Application.launch(args);
	}
	
	/**
	 * Overrides the stop method and makes a backup of the bookmarks file
	 */
	@Override
    public void stop() {
		FileUtils.saveFile("bookmarks.web", "");
	}

}
