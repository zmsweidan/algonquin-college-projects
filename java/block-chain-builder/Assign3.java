/*
 * Zaid Sweidan
 * ST# 040887956
 * ASSIGNMENT 3
 * Mar 30 2018
 * 
 * PURPOSE: 
 * 	This class is the starting point of execution for the program. It only contains a main method that 
 *  handles a console-based menu.
 * 
 * METHODS:
 * 	(+) main(String[]): launches a console-based selection menu for various operations on an instance of the College class
 *  
 */

import java.util.*;

public class Assign3 {

	public static void main(String[] args) {
		Scanner keyboard = new Scanner (System.in);
		String menuChoice = "a";
		
		College college = new College("Algonquin");
		
		while (menuChoice.charAt(0) != '6') {
			System.out.println ("\nEnter 1 to display the college courses: ");
			System.out.println ("2 to add a new course ");
			System.out.println ("3 to add a block: ");
			System.out.println ("4 to verify chains: ");
			System.out.println ("5 to fix a chain:");
			System.out.println ("6 to quit: ");
			menuChoice = keyboard.next();
			
			switch (menuChoice.charAt(0)) {
			
			case '1': //display courses
				college.printCourseDetails();
				break;
				
			case '2': //add a course
				college.addCourse(keyboard);
				break;
				
			case '3': //add a block
				if (college.isEmpty()) break;
				college.addBlock(keyboard);
				break;
			  		  
			case '4': //verify chains
				if (college.isEmpty()) break;
				college.verifyChains();
	          	break;
	          		  
			case '5': //fix a chain
				if (college.isEmpty()) break;
				college.fixChain(keyboard);
				break;
					  
			case '6': //quit
				System.out.println ("Goodbye");
				break;
					  
			default:  System.out.println ("Invalid choice...");
			}
		}
	}
	


}
