import java.util.Scanner;
/************************************************************************************************
 * 
 * Linda Crane - CST8130 - Winter 2018
 * Purpose:  This class is a subclass of the Item class
 * **** updated to include methods for reading/writing to/from file
 * Data members:  numItems: int
 *                itemsUsed: int[] - to hold itemCode values used
 * Methods: addItem(Scanner):boolean - fills valid values for the data members in this class from the Scanner object (with prompts)
 * 			addItemFromFile(Scanner): boolean - valid values for data members from Scanner object (no prompts)
 *          toString(): String - sends contents of data members to a String
 *          writeItem():String - String format of output of data to file
 *
 ***********************************************************************************************/

public class ManufacturedItem extends Item{
	private int [] itemsUsed = new int[10];
	private int numItemsUsed = 0;
	
	public boolean addItem(Scanner keyboard) {
		boolean isOk = super.addItem(keyboard);
		
		if (isOk) {
			boolean isValid = false;
			int temp = 0;
			System.out.println ("Enter up to 10 codes used in this item (-1 to quit): ");
			for (numItemsUsed = 0; numItemsUsed < 10; numItemsUsed++ ) {
				isValid = false;
				temp = 0;
				while (!isValid && temp!= -1) {
					if (keyboard.hasNextInt()) { 
						temp = keyboard.nextInt();
					    isValid = true;
					} else {
						System.out.println ("Invalid value...reenter: ");
						keyboard.next();
					}	
				}
			   if (temp == -1)
				   break;   // break out of for loop
			   else 
				   itemsUsed[numItemsUsed] = temp;
			   
			}
		}
		return isOk;
	}
	
	public boolean addItemFromFile(Scanner inFile) {
		boolean isOk = super.addItemFromFile(inFile);
		
		if (isOk) {
			int temp = 0;
			for (numItemsUsed = 0; numItemsUsed < 10; numItemsUsed++ ) {
				temp = 0;
				if (inFile.hasNextInt()) { 
					temp = inFile.nextInt();
				} else {
					return false;	
				}
			    if (temp == -1)
				   break;   // break out of for loop
			    else 
				   itemsUsed[numItemsUsed] = temp;
			   
			}
		}
		return isOk;
	}

	public String toString() {
		String temp = "";
		for (int i=0; i < numItemsUsed; i++) 
			temp += itemsUsed[i] + ", ";
		return super.toString() + " Codes used: " + temp;
	}
	
	public String writeItem() {
		String s =  "m " + super.writeItem() + " ";
		for (int i=0; i < numItemsUsed; i++) {
			s+= itemsUsed[i] + " ";
		}
		return s + "-1";
	}
	

}
