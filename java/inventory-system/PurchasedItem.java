import java.util.Scanner;
/************************************************************************************************
 * 
 * Linda Crane - CST8130 - Winter 2018
 * Purpose:  This class is a subclass of the Item class
 * ****updated to handle reading/writing from/to file
 * Data members:  supplierName: String
 * Methods: addItem(Scanner):boolean - fills valid values for the data members in this class from the Scanner object (with prompts)
 * 			addItemFromFile(Scanner): boolean - valid values for data members from Scanner object (no prompts)
 *          toString(): String - sends contents of data members to a String
 *          writeItem():String - String format of output of data to file
 *
 ***********************************************************************************************/

public class PurchasedItem extends Item {
	private String supplierName = new String();
	
	public boolean addItem(Scanner keyboard) {
		boolean isOk = super.addItem(keyboard);
		
		if (isOk) {
			System.out.println ("Enter the name of the supplier: ");
			supplierName = keyboard.next();
		}
		return isOk;
	}
	
	public boolean addItemFromFile(Scanner inFile) {
		boolean isOk = super.addItemFromFile(inFile);
		
		if (isOk) {
			supplierName = inFile.next();
		}
		return isOk;
	}

	public String toString() {
		return super.toString() + " Supplier: " + supplierName;
	}
	
	public String writeItem() {
		return "p " + super.writeItem() + " " + supplierName;
	}
	

}
