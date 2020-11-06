/*
 * Zaid Sweidan
 * ST# 040887956
 * ASSIGNMENT 3
 * Mar 30 2018
 * 
 * PURPOSE: 
 * 	This class contains methods that manage a block representing an individual student enrolled in a
 *  a course. This class is managed primarily by the BlockChain class.
 * 
 * MEMBERS:
 * 	(-) date: integer value representing the date in month day year format (eg) 2152018
 * 	(-) studentNumber: integer value representing the student number
 * 	(-) grade: integer value representing the students grade
 * 	(-) previousHash: floating point representing the previous block's hash code
 * 	(-) currentHash: floating point representing the current block's hash code
 * 
 * METHODS:
 * 	(+) calculateHash(): calculates a hash number based on the provided information and returns it as a float
 *  (+) toString(): returns a string value containing the details of a block
 *  (+) getCurrentHash(): returns a float value of the current block's hash number
 *  (+) isEqual(Block): checks if the previous hash of this block is equal to the current hash of the block passed to the method
 *  (+) updatePreviousHash(float): allows the user to update this block's previous hash using a float parameter
 *  (+) addInfoToBlock(Scanner, float): allows the user to add info to a block by passing a scanner object and the previous hash
 */
import java.util.*;

public class Block {
	private int date;
	private int studentNumber;
	private int grade;
	private float previousHash;
	private float currentHash;
	
	public Block() {
		// create the Genesis block
		date = 2152018;
		studentNumber = 0;
		grade = 100;
		previousHash = 0f;
		currentHash = calculateHash();
	}
	
	public float calculateHash() {
		return (date+studentNumber+grade)/88;
	}
	
	public String toString() {
		return "" + studentNumber + " " + grade + " " + date +  " current: " + currentHash + " previous: " + previousHash ;
	}

	public float getCurrentHash() {
		return currentHash;
	}
	
	public boolean isEqual (Block temp) {
		return (previousHash == temp.currentHash);
	}
	
	public void updatePreviousHash(float prevHash) {
		previousHash = prevHash;
	}
	
	public boolean addInfoToBlock(Scanner keyboard, float previousHash) {
		System.out.print ("Enter date: ");
		while (!keyboard.hasNextInt())  {
			System.out.print ("Invalid...enter an int for date: ");
			keyboard.next();
		}
		date = keyboard.nextInt();
		
		
		System.out.print ("Enter student number: ");
		while (!keyboard.hasNextInt())  {
			System.out.print ("Invalid...enter an int for student number: ");
			keyboard.next();
		}
		studentNumber = keyboard.nextInt();
		
		
		System.out.println ("Enter grade: ");
		while (!keyboard.hasNextInt())  {
			System.out.print ("Invalid...enter an int for grade: ");
			keyboard.next();
		}
		grade = keyboard.nextInt();
		
		currentHash = calculateHash();
		this.previousHash = previousHash;
		return true;
	}
	
}
