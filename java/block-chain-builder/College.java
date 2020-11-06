/*
 * Zaid Sweidan
 * ST# 040887956
 * ASSIGNMENT 3
 * Mar 30 2018
 * 
 * PURPOSE: 
 * 	This class allows the creation of a college instance which manages an array list containing the
 * 	courses in a college. It is primarily responsible for managing an instance of the BlockChain class.
 * 
 * MEMBERS:
 * 	(-) college: array list of block chains used in an instance of the College class
 * 	(-) collegeName: string value that stores the name of the college
 * 
 * METHODS:
 *  (+) isEmpty(): returns a boolean value indicating if the array list is empty or not
 *  (+) printCourseDetails(): prints a block containing the detailed blocks in each course
 *  (+) printCourseList(): prints a block containing a summary of the courses available and their indexes
 *  (+) addCourse(Scanner): allows the user to add a course via scanner input
 *  (+) addBlock(Scanner): allows the user to add either a good or bad block to a course via scanner input
 *  (+) verifyChains(): checks all chains and alerts the user if a bad block is found
 *  (+) fixChains(Scanner): allows the user to fix a chain of their choosing via scanner input
 *  
 */
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class College {
	
	private ArrayList<BlockChain> college;
	private String collegeName;
	
	public College(String collegeName) {
		this.collegeName = collegeName;
		college =  new ArrayList<>();
	}
	
	public boolean isEmpty() {
		if (college.isEmpty()) {
			System.out.println("Error: no courses added yet");
			return true;
		}
		return false;
	}
	
	public void printCourseDetails() {
		System.out.println("For college: " + collegeName + "\n");
		for (BlockChain bc: college) {
			bc.printBlockChain();
			System.out.println();
		}
	}
	
	public void printCourseList() {
		int i = 0;
		for (BlockChain bc: college)
			System.out.println(i++ +" "+ bc.getCourseName());
	}
	
	public void addCourse(Scanner keyboard) {
		System.out.println("Enter Name of course to add: ");
		String temp = keyboard.next();
		BlockChain newCourse = new BlockChain(temp);
		college.add(newCourse);
	}
	
	public void addBlock(Scanner keyboard) {
		System.out.println("Enter which course to add: \n");
		printCourseList();
		
		try {
			int id = keyboard.nextInt();
			if (id > -1 && id < college.size() ) {
				System.out.println("Add good block or bad?  (g for good, anything else for bad): ");
				if (keyboard.next().charAt(0) == 'g')
					college.get(id).addBlock(keyboard);
				else
					college.get(id).addBadBlock(keyboard);
			} else
				System.out.println("Error: selection out of range");
		} catch (InputMismatchException ime) {
			System.out.println("Error: invalid course selection");
		}
	}
	
	public void verifyChains() {
		for (int i=0; i < college.size(); i++) {
			String check = college.get(i).verifyChain()==true ? " is verified":" is not verified";
			System.out.println("Chain for " + college.get(i).getCourseName() + check);
		}
	}
	
	public void fixChain(Scanner keyboard) {
		System.out.println("Enter which course to fix : \n");
		printCourseList();
		try {
			int id = keyboard.nextInt();
			if (id > -1 && id < college.size() ) {
				college.get(id).removeBadBlocks();
				System.out.println("Chain is Fixed");
			} else
				System.out.println("Error: selection out of range");
		} catch (InputMismatchException ime) {
			System.out.println("Error: invalid course selection");
		}
	}

	

}
