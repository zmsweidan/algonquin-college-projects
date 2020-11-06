/*
 * FILE NAME: Menus.java
 * AUTHOR: Zaid Sweidan, 040887956
 * COURSE: CST884 – OOP 
 * Assignment: 2
 * DATE: January 12, 2018
 * PROFESSOR: Raymond Peterkin
 * PURPOSE: This file contains a class with methods that load all the menus and their 
 * respective event handlers onto the browser. 
 * 
 * REFERENCES:
 * http://code.makery.ch/blog/javafx-dialogs-official/
 * http://java-buddy.blogspot.ca/2012/02/javafx-20-set-accelerator.html
 * https://stackoverflow.com/questions/37260118/javafx-menuitem-does-not-react-on-mouseevent-clicked
 * https://stackoverflow.com/questions/18928333/how-to-program-back-and-forward-buttons-in-javafx-with-webview-and-webengine
 * https://stackoverflow.com/questions/13880638/how-do-i-pick-up-the-enter-key-being-pressed-in-javafx2/13881850
 * https://stackoverflow.com/questions/32486758/detect-url-changes-in-javafx-webview
 * http://www.java2s.com/Tutorials/Java/JavaFX/1500__JavaFX_WebEngine.htm
 * https://docs.oracle.com/javafx/2/ui_controls/menu_controls.htm
 */

package assignment2;

import java.io.File;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.input.KeyCombination;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.input.*;
import javafx.concurrent.Worker.State;

/**
 * This class contains all the tools, menus and their respective event handlers that are 
 * loaded onto the browser using the getMenuBar() method.
 * @author Zaid Swedian
 * @version 2
 * @see assignment2
 * @since JavaSE-1.8
 */
public class Menus {
	
	/** MenuBar instance that holds all Menus */
	private static MenuBar menuBar;
	/** Menu instance that holds MenuItem/s */
	private static Menu mnuFile, mnuSettings, mnuBookmarks, mnuHelp;
	/** MenuItem instance which performs a specific action */
	private static MenuItem mnuItmRefresh, mnuItmExit, 
							mnuItmToggleAddressBar, mnuItmChangeStartup, mnuItmHistory, mnuItmDisplayCode,
							mnuItmAddBookmark, mnuItmAbout;
	
	
	//************MENU-ITEMS************
	/**
	 * Returns a MenuItem that refreshes the current page
	 * @param we WebEngine instance
	 * @return refresh MenuItem 
	 */
	public static MenuItem getMnuItmRefresh (WebEngine we) {
		mnuItmRefresh = new MenuItem("Refresh");
		mnuItmRefresh.setOnAction ( e ->  we.reload() );
		mnuItmRefresh.setAccelerator(KeyCombination.keyCombination("Ctrl+R"));
		return mnuItmRefresh;
	}
	/**
	 * Returns a MenuItem that exits the application
	 * @return exit MenuItem
	 */
	public static MenuItem getMnuItmExit () {
		mnuItmExit = new MenuItem("Exit");
		mnuItmExit.setOnAction ( e -> Platform.exit() );
		mnuItmExit.setAccelerator(KeyCombination.keyCombination("Ctrl+E"));
		return mnuItmExit;
	}
	
	/**
	 * Returns a MenuItem which toggles the address bar either ON or OFF
	 * @param we WebEngine instance
	 * @param v Vbox instance
	 * @return address bar MenuItem
	 */
	public static MenuItem getMnuItmToggleAddressBar(WebEngine we, VBox v) {
		mnuItmToggleAddressBar = new MenuItem("Toggle Address Bar");
		mnuItmToggleAddressBar.setOnAction (e -> {
			if (v.getChildren().size() == 1)
				v.getChildren().add(getAddressBar(we));
			else
				v.getChildren().remove(1);
		});
		mnuItmToggleAddressBar.setAccelerator(KeyCombination.keyCombination("Ctrl+T"));
		return mnuItmToggleAddressBar;
	}
	/**
	 * Returns a MenuItem which changes the startup page to the current page
	 * @param we WebEngine instance
	 * @return change startup page MenuItem
	 */
	public static MenuItem getMnuItmChangeStartup (WebEngine we) {
		mnuItmChangeStartup = new MenuItem("Change Start-up Page");
		mnuItmChangeStartup.setOnAction ( e -> {
			File f = new File("default.web");
			ArrayList<String> al = new ArrayList<>();
			if (FileUtils.fileExists(f)) {
				al.add(we.getLocation());
				FileUtils.saveFileContents(f, al);
			}
		});
		mnuItmChangeStartup.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
		return mnuItmChangeStartup;
	}
	/**
	 * Returns a MenuItem that displays the browser history
	 * @param we WebEngine instance
	 * @param bp BorderPane instance
	 * @return display history MenuItem
	 */
	public static MenuItem getMnuItmHistory (WebEngine we, BorderPane bp) {
		mnuItmHistory = new MenuItem("History");
		mnuItmHistory.setAccelerator(KeyCombination.keyCombination("Ctrl+H"));
		VBox h = getHistoryBar(we);
		mnuItmHistory.setOnAction (e -> {
			if ( !bp.getChildren().contains(h))
				bp.setRight(h);
			else
				bp.setRight(null);
		});
		return mnuItmHistory;
	}
	/**
	 * Returns a MenuItem that displays the HTML/CSS code of the current page
	 * @param we WebEngine instance
	 * @param bp BorderPane instance
	 * @return display code MenuItem
	 */
	public static MenuItem getmMuItmDisplayCode (WebEngine we, BorderPane bp) {
		mnuItmDisplayCode = new MenuItem("Display Code");
		mnuItmDisplayCode.setAccelerator(KeyCombination.keyCombination("Ctrl+D"));
		ScrollPane sp = new ScrollPane();
		mnuItmDisplayCode.setOnAction ( e -> {
	        try {
	            TransformerFactory transformerFactory = TransformerFactory
	                .newInstance();
	            Transformer transformer = transformerFactory.newTransformer();
	            StringWriter stringWriter = new StringWriter();
	            transformer.transform(new DOMSource(we.getDocument()),
	                new StreamResult(stringWriter));
	            String xml = stringWriter.getBuffer().toString();
	            
	            FileUtils.saveFile("xml.txt", xml);
	            sp.setContent(new Text (xml));
	            sp.setMaxHeight(200);
	            
				if ( !bp.getChildren().contains(sp))
					bp.setBottom(sp);
				else
					bp.setBottom(null);
	            
	          } catch (Exception ex) {
	            ex.printStackTrace();
	          }
		});
		return mnuItmDisplayCode;
	}
	
	/**
	 * Returns a MenuItem that adds a bookmark of the current web page
	 * @param we WebEngine instance
	 * @return add bookmark MenuItem
	 */
	public static MenuItem getMnuItmAddBookmark (WebEngine we) {
		mnuItmAddBookmark = new MenuItem("Add Bookmark");
		mnuItmAddBookmark.setOnAction ( e1 -> {
			FileUtils.appendFile("bookmarks.web", we.getTitle() +" : "+ we.getLocation());
			loadBookmarks(we);
		});
		mnuItmAddBookmark.setAccelerator(KeyCombination.keyCombination("Ctrl+A"));
		return mnuItmAddBookmark;
	}
	
	/**
	 * Returns a MenuItem that displays the about section
	 * @return about MenuItem
	 */
	public static MenuItem getMnuItmAbout () {
		mnuItmAbout = new MenuItem("About");
		mnuItmAbout.setOnAction ( e -> {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("About");
			alert.setHeaderText("Developed by:");
			alert.setContentText("Zaid Sweidan \n040887956");
			alert.showAndWait();
		});
		mnuItmAbout.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
		return mnuItmAbout;
	}
	
	
	//************MENUS************
	/**
	 * Returns a Menu that contains all "File" MenuItems
	 * @param we WebEngine instance
	 * @return "File" Menu
	 */
	public static Menu getMnuFile(WebEngine we) {
		mnuFile = new Menu("_File");
		mnuFile.getItems().addAll( getMnuItmRefresh(we), getMnuItmExit() );
		mnuFile.setAccelerator(KeyCombination.keyCombination("F"));
		return mnuFile;
	}
	/**
	 * Returns a Menu that contains all "Settings" MenuItems
	 * @param we WebEngine instance
	 * @param v VBox instance
	 * @param bp BorderPane instance
	 * @return "Settings" Menu
	 */
	public static Menu getMnuSettings(WebEngine we, VBox v, BorderPane bp) {
		mnuSettings = new Menu("_Settings");
		mnuSettings.getItems().addAll( getMnuItmToggleAddressBar(we, v), getMnuItmChangeStartup(we), getMnuItmHistory(we, bp), getmMuItmDisplayCode(we, bp) );
		mnuSettings.setAccelerator(KeyCombination.keyCombination("S"));
		return mnuSettings;
	}
	/**
	 * Returns a Menu that contains all "Bookmarks" MenuItems
	 * @param we WebEngine instance
	 * @param bp BorderPane instance
	 * @return "Bookmarks" Menu
	 */
	public static Menu getMnuBookmarks(WebEngine we, BorderPane bp) {
		mnuBookmarks = new Menu("_Bookmarks");
		mnuBookmarks.getItems().add(getMnuItmAddBookmark(we));
		loadBookmarks(we);
		mnuBookmarks.setAccelerator(KeyCombination.keyCombination("B"));
		return mnuBookmarks;
	}
	/**
	 * Returns a Menu that contains all "About" MenuItems
	 * @return "About" Menu
	 */
	public static Menu getMnuHelp() {
		mnuHelp = new Menu("_Help");
		mnuHelp.getItems().add(getMnuItmAbout());
		mnuHelp.setAccelerator(KeyCombination.keyCombination("H"));
		return mnuHelp;
	}
	
	
	//************MENU-BAR************
	/**
	 * The main method used to load all Menu instances into the browser
	 * @param we WebEngine instance
	 * @param v VBox instance
	 * @param bp BorderPane instance
	 * @return MenuBar item
	 */
	public static MenuBar getMenuBar(WebEngine we, VBox v, BorderPane bp) {
		menuBar = new MenuBar();
		menuBar.getMenus().addAll(getMnuFile(we), getMnuSettings(we, v, bp), getMnuBookmarks(we, bp), getMnuHelp());
		return menuBar;
	}
	
	
	//************LOAD-BOOKMARKS************
	/**
	 * Loads all bookmarks from a file: "bookmarks.web" and creates corresponding MenuItems in the "Bookmarks" Menu
	 * @param we WebEngine instance
	 */
	public static void loadBookmarks(WebEngine we) {
		if (FileUtils.fileExists("bookmarks.web")) {
			if (mnuBookmarks.getItems().size() >=1) mnuBookmarks.getItems().remove(1, mnuBookmarks.getItems().size());
			mnuBookmarks.getItems().add(new SeparatorMenuItem());
			ArrayList<String> al = FileUtils.getFileContentsAsArrayList(new File("bookmarks.web"));
			for (String s: al) {
				String[] sep = s.split(" : ");
				Label l = new Label(sep[0]);
				CustomMenuItem m = new CustomMenuItem (l);
				l.setOnMouseClicked( e1 -> {
	                if (e1.getButton() == MouseButton.PRIMARY)
	                	we.load(sep[1]);
	                if (e1.getButton() == MouseButton.SECONDARY) {
	                	mnuBookmarks.show();
	                	ContextMenu cm = new ContextMenu();
	                	cm.getItems().add(new MenuItem("Remove Bookmark?"));
	                	l.setMaxHeight(3);
	                	cm.setMaxHeight(5);
	                	cm.show(l, Side.TOP, 132, 25);
	                	cm.setOnAction(e2 -> {
	                		FileUtils.removeLine("bookmarks.web", (mnuBookmarks.getItems().indexOf(m)-2) );
	                		mnuBookmarks.hide();
	                		loadBookmarks(we);
	                	});
	                }
		        });
				mnuBookmarks.getItems().add(m);
			}
		}
	}

	
	//************ADDRESS-BAR************
	/**
	 * Returns a HBox that contains all components of the address bar
	 * @param we WebEngine instance
	 * @return HBox containing the address bar
	 */
	public static HBox getAddressBar(WebEngine we) {
		
		Label l = new Label(" Enter Address:  ");
		l.setAlignment(Pos.CENTER);
		l.setMinHeight(22);
		
		TextField url = new TextField();
		url.setMaxWidth(Double.MAX_VALUE);
		url.setPromptText("URL Address");
		url.setOnKeyPressed(e -> {
			if (e.getCode().toString().equals("ENTER")){
				loadPage(url.getText(), we);
			}
		});
		we.getLoadWorker().stateProperty().addListener(( ov, oldState, newState)-> {
	           if (newState == State.SUCCEEDED)
	        	   url.setText(we.getLocation());
		});
		Button go = new Button();
		go.setText("Go");
		go.setOnAction( e -> { loadPage(url.getText(), we); });
		
		HBox address = new HBox();
		address.getChildren().addAll(l, url, go);
		HBox.setHgrow(url, Priority.ALWAYS);
		return address;
	}
	
	
	//************HISTORY-BAR************
	/**
	 * Returns a VBox that contains all components of the history bar
	 * @param we WebEngine instance
	 * @return VBox containing the history bar
	 */
	public static VBox getHistoryBar(WebEngine we) {
		
		VBox history = new VBox();
	
		//Top Label
		Label l = new Label("Web History:");
		l.setMinHeight(22);
		HBox label = new HBox();
		label.getChildren().addAll(l);
		label.setAlignment(Pos.CENTER);
		history.getChildren().add(label);
		
		//History List
		ObservableList<WebHistory.Entry> entryList = we.getHistory().getEntries();
		ScrollPane sp = new ScrollPane();
		sp.setMaxWidth(250);
		we.getLoadWorker().stateProperty().addListener(( ov, oldState, newState)-> {
           if (newState == State.SUCCEEDED) {
        	   int list=1;
	   			String st = "";
				for (int i=entryList.size()-1; i>-1; i--) {
					st += list++ +". " + entryList.get(i).toString() + "  \n";
				}
				sp.setContent(new Text (st));
           }
	    });
		history.getChildren().add(sp);
		VBox.setVgrow(sp, Priority.ALWAYS);
		
		//Buttons
		Button back = new Button("\u23F4");
		back.setMinSize(50, 20);
		back.setOnAction( e ->  {
			if (we.getHistory().getCurrentIndex() > 0 )
				we.getHistory().go(-1);
		});
		Button forw = new Button("\u23F5");
		forw.setMinSize(50, 20);
		forw.setOnAction( e -> {
			if (we.getHistory().getCurrentIndex()+1 < entryList.size() )
				we.getHistory().go(1);
		});
		back.setDisable(true); forw.setDisable(true);
		we.setOnStatusChanged( e -> {
			if (we.getHistory().getCurrentIndex() > 0 )
				back.setDisable(false);
			else back.setDisable(true);
			if (we.getHistory().getCurrentIndex()+1 < entryList.size() )
				forw.setDisable(false);	
			else forw.setDisable(true);
		});
		HBox buttons = new HBox();
		buttons.getChildren().addAll(back, forw);
		buttons.setAlignment(Pos.CENTER);
		history.getChildren().add(buttons);
		
		//Return
		return history;
	}
	
	
	//************LOAD-PAGE************
	/**
	 * Loads a web page using a String containing the URL and a WebEngine instance
	 * @param s String value of the URL
	 * @param we WebEngine instance
	 */
	public static void loadPage(String s, WebEngine we) {
		boolean muE = false;
		try {
			@SuppressWarnings("unused")
			URL url = new URL(s);
		} catch (MalformedURLException e1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Error: Malformed URL Exception Caught");
			alert.showAndWait();
			muE = true;
		}
		if (!muE) 
			we.load(s);
	}

}
