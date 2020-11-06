import java.io.File;
import java.io.*;
import java.util.Scanner;
/************************************************************************************************
 * 
 * Original Author: Linda Crane - CST8130 - Winter 2018
 * Modified By: Zaid Sweidan (2018-04-15)
 * 
 * Purpose:  This class drives the menu for Assignment 4 - Inventory system
 * ****updated to add a search for item by code option
 *
 ***********************************************************************************************/

public class Assign4Main {
	
	public static void main (String [] args) {
	
		String menuChoice = "0";
		Scanner keyboard = new Scanner (System.in);
		Inventory company = new Inventory();
		
		while (menuChoice.charAt(0) !=  '8') {
			System.out.println ("\nEnter 1 to add an item to inventory \n2 to display current inventory");
			System.out.println ("3 buy some of an item \n4 sell some of an item ");
			System.out.println ("5 to load data from a file \n6 to save data to a file");
			System.out.println ("7 to search for an item by code  \n8 to quit");
			menuChoice = keyboard.next();
			
			switch (menuChoice.charAt(0)) {
				case '1':   if (!company.addItem(keyboard))
								System.out.println ("Error...could not add item");
							break;
				case '2':	System.out.println (company);
							break;
				case '3':   if (!company.updateItem(keyboard, true))
								System.out.println ("Error...could not buy item");
							break;
				case '4':	if (!company.updateItem(keyboard, false))
								System.out.println ("Error... could not sell item");
							break;
				case '5':	Scanner inFile = openInputFile();
							if (inFile!= null)
								while (company.addItemFromFile(inFile)) {  // loop processing Items in file until done
									
								}
							break;
				case '6':	FileWriter outFile = openOutputFile();
							if (outFile != null) {
								company.writeToFile(outFile);
							}
							break;
				case '7':	company.findItem(keyboard);
							break;
				case '8':	System.out.println ("goodbye");
							break;
				default: System.out.println ("Invalid entry...");
			} // end switch
		} // end while
	} // end main
	
	public static Scanner openInputFile() {

		Scanner keyboard = new Scanner(System.in);
		String fileName = new String();
		Scanner inFile = null;

		System.out.print("\n\nEnter name of file to process: ");
		fileName = keyboard.next();

		File file = new File(fileName);
		try {
			if (file.exists()) {
				inFile = new Scanner(file);
			}
			return inFile;
		} catch (IOException e) {
			System.out
					.println("Could not open file...." + fileName + "exiting");
			return null;
		}
		
	}// end openOutputFile method
	public static FileWriter openOutputFile() {

		Scanner keyboard = new Scanner(System.in);
		String fileName = new String();
		FileWriter outFile = null;

		System.out.print("\n\nEnter name of file to write to: ");
		fileName = keyboard.next();

		try {
			outFile = new FileWriter(fileName);
		}
		catch (IOException e) {
			System.out .println("Could not open file...." + fileName + "exiting");
		}
		return outFile;
		
	}// end openFile method

}// end class	
	

