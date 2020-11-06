/*
 * FILE NAME: FileUtils.java
 * AUTHOR: Zaid Sweidan, 040887956
 * COURSE: CST8284 – OOP 
 * Assignment: 2
 * DATE: January 12, 2018
 * PROFESSOR: Raymond Peterkin
 * PURPOSE: this file contains a class that features several static utility methods
 *  designed to save, write and edit files used by the browser.
 */

package assignment2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class contains several static utility methods designed to save, write and
 * edit files used by the browser.
 * @author Zaid Swedian
 * @version 2
 * @see assignment2
 * @since JavaSE-1.8
 */
public class FileUtils {
	
	/** String containing the name of the file*/
	private String fileName;
	/** String containing the path of the file*/
	private String path;
	
	/**
	 * Saves the contents of a declared file using an ArrayList of Strings
	 * @param f declared File
	 * @param ar ArrayList of Strings containing the desired contents
	 */
	public static void saveFileContents(File f, ArrayList<String> ar) {
		try {
			PrintWriter save = new PrintWriter (f);
			for (String a: ar) {
				save.println(a);
			}
			save.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Extracts contents of a file as an ArrayList of Strings
	 * @param f declared File
	 * @return ArrayList of Strings that contains the files contents
	 */
	public static ArrayList<String> getFileContentsAsArrayList(File f){
		ArrayList<String> a = new ArrayList<>();
		try {
			Scanner read = new Scanner(f);
			while (read.hasNextLine()) {
				a.add(read.nextLine());
			}
			read.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return a;
	}
	
	/**
	 * Checks if a file exists using a declared File
	 * @param f declared File
	 * @return boolean value of expression
	 */
	public static boolean fileExists (File f) {
		return (f.exists() && f.isFile());
	}
	
	/**
	 * Checks if a file exists using a a String containing the file name
	 * @param s String value of a declared File name
	 * @return boolean value of expression
	 */
	public static boolean fileExists (String s) {
		File f = new File(s);
		return (fileExists(f));
	}
	
	/**
	 * Creates and saves a file using String values of the file name and its contents
	 * @param fn String value of file name
	 * @param contents String value of file contents
	 */
	public static void saveFile (String fn, String contents) {
		File f = new File(fn);
		if (FileUtils.fileExists(f)) {
			ArrayList<String> as = FileUtils.getFileContentsAsArrayList(f);
			FileUtils.saveFileContents(f, as);
		}else {
			try {
				PrintWriter save = new PrintWriter (fn);
				save.close();
				ArrayList<String> as = FileUtils.getFileContentsAsArrayList(f);
				if (contents != "")
					as.add(contents);
				FileUtils.saveFileContents(f, as);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}	
		}
	}
	
	/**
	 * Creates and appends to a file using String values of the file name and a new line
	 * @param fn String value of the file name
	 * @param line String value of a line to be appended to the file
	 */
	public static void appendFile (String fn, String line) {
		File f = new File(fn);
		if (FileUtils.fileExists(f)) {
		}else {
			try {
				PrintWriter save = new PrintWriter (fn);
				save.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}	
		}
		ArrayList<String> as = FileUtils.getFileContentsAsArrayList(f);
		as.add(line);
		FileUtils.saveFileContents(f, as);
	}
	
	/**
	 * Creates a file and removes a line from a it using a String value of the file name and an int value of the line number
	 * @param fn String value of file name
	 * @param ln int value of line number
	 */
	public static void removeLine (String fn, int ln ) {
		File f = new File(fn);
		if (FileUtils.fileExists(f)) {
		}else {
			try {
				PrintWriter save = new PrintWriter (fn);
				save.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}	
		}
		ArrayList<String> as = FileUtils.getFileContentsAsArrayList(f);
		as.remove(ln);
		FileUtils.saveFileContents(f, as);
	}
	
	/**
	 * Getter than returns a String value of the path name
	 * @return String value of path name
	 */
	public String getPath() {
		return path;
	}
	/**
	 * Getter than returns a String value of the file name
	 * @return String value of file name
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * Setter than sets a String value of the path name
	 * @param s String value of path name
	 */
	public void setPath(String s) {
		path = s;
	}
	/**
	 * Setter than sets a String value of the file name
	 * @param s String value of file name
	 */
	public void setFileName(String s) {
		fileName = s;
	}
	
}
