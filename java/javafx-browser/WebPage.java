/*
 * FILE NAME: WebPage.java
 * AUTHOR: Zaid Sweidan, 040887956
 * COURSE: CST884 – OOP 
 * Assignment: 2
 * DATE: January 12, 2018
 * PROFESSOR: Raymond Peterkin
 * PURPOSE: This file contains a class that is responsible for handling web page generation.
 */

package assignment2;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 * This class is responsible for generating and handling instances of the web page using
 * tools imported from the JavaFX library.
 * @author David B. Houtman
 * @version 1
 * @see assignment2
 * @since JavaSE-1.8
 */
public class WebPage {
	
	/** WebView instance */
	private WebView webview = new WebView();
	/** WebEngine instance */
	private WebEngine engine;
	
	/**
	 * Returns a WebEngine instance with an added listener
	 * @param stage Stage Object
	 * @return fully loaded WebEngine instance
	 */
	public WebEngine createWebEngine(Stage stage) {
		
		WebView wv = getWebView();
		engine = wv.getEngine();
		
		engine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
			@Override
			public void changed(ObservableValue<? extends State> ov, State oldState, State newState) {
				if (newState == Worker.State.RUNNING) {
					stage.setTitle(engine.getLocation());
				}
			}
		});
		return engine;
	}
	
	/**
	 * Getter than returns a WebView instance
	 * @return WebView instance
	 */
	public WebView getWebView() {
		return webview;
	}
	
	/**
	 * Getter that returns a WebEngine instance
	 * @return WebEngine instance
	 */
	public WebEngine getWebEngine() {
		return engine;
	}
}
