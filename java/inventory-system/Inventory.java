import java.util.*;
import java.io.*;

/************************************************************************************************
 * 
 * Original Author: Linda Crane - CST8130 - Winter 2018
 * Modified By: Zaid Sweidan (2018-04-15)
 * 
 * Purpose:  This class holds the data structure for the Inventory
 * ****updated to handle hash code based insertions, searching and editing
 * Data members:  inventory: ArrayList<Item> - array of Item objects 
 *      
 * Methods: [UPDATED]addItem(Scanner):boolean - fills valid values for the data members in this class from the Scanner object (meant as keyboard - with prompts)
 *          [UPDATED]addItemFromFile(Scanner): boolean - fills valid values for data members from a file
 *          [UPDATED]toString(): String - sends contents of data members to a String
 *          [UPDATED]alreadyExists(Item): int - returns the index of an item where it is found or -1 if not found
 *          [UPDATED]findIndex(Item): int - returns index of where to add an item using its hash code
 *          [UPDATED]updateItem(Scanner): boolean - updates the quantity of the Item object
 *          [UPDATED]writeToFile (FileWriter)": boolean - writes the arraylist to a file    
 *          [NEW]FindItem (Scanner)": prints to check if an input item code exists in the current inventory, if it does it also prints the contents                       
 *
 ***********************************************************************************************/

public class Inventory {
	private ArrayList<Item> inventory = new ArrayList<Item>(100);
	
	public Inventory () {
		for (int i= 0; i < 100; i++)
			inventory.add(i, new Item());
	}
	
	public Inventory (int size) {
		if (size < 0) {
			size = 0;
		}
		inventory = new ArrayList<Item>(size);
	}
	
	public boolean addItem(Scanner keyboard) {
		 
		 Item newItem;
		 String type = "";
		 // which type of item to add?
		 System.out.print ("Do you wish to add a purchased item (enter P/p) or manufactured (enter anything else)? ");
		 if (keyboard.hasNext())
			 type = keyboard.next();
		 else return false;
		 if (type.charAt(0) == 'P' || type.charAt(0) == 'p')
			 newItem = new PurchasedItem();
		 else 
			 newItem = new ManufacturedItem();
		 
		 // fill newItem object with data,  if successful, add to array - if not already there
		 if (newItem.addItem(keyboard)) {
			 if (alreadyExists(newItem)== -1) { 
				 int indexToAdd = findIndex(newItem);
				 if (indexToAdd != -1) {
					 inventory.set(indexToAdd, newItem);
					 return true;
				 } else {
					 System.out.println ("Error: Item could not be added");
					 return false;
				 }
			 } else {
				 System.out.println ("Item already in inventory");
				 return false;
			 }
		 } else 
			 return false;
	}
	
	public boolean addItemFromFile (Scanner inFile) {
		 
		 Item newItem;
		 String type = "";
		 // which type of item to add?
		 if (inFile.hasNext())
			 type = inFile.next();
		 else return false;
		 if (type.charAt(0) == 'P' || type.charAt(0) == 'p')
			 newItem = new PurchasedItem();
		 else 
			 newItem = new ManufacturedItem();
		 
		 // fill newItem object with data,  if successful, add to array - if not already there
		 if (newItem.addItemFromFile(inFile)) {
			 if (alreadyExists(newItem) == -1) { 
				 int indexToAdd = findIndex(newItem);
				 inventory.set(indexToAdd, newItem);
			 	 return true;
			 } else {
				 System.out.println ("Item already in inventory");
				 return true;
			 }
		 } else 
			 return false;
	}
	
	public int alreadyExists (Item item) {
		int index = item.hashCode();
		if (inventory.get(index).isEqual(item))
			return index;
		else {
			while (index < inventory.size()) {
				if (inventory.get(index).isEqual(item))
					return index;
				else
					index++;
			}
		}
		return -1;
	}
	
	public int findIndex(Item item) {
		int index = item.hashCode();
		Item currentItem;
		Boolean found = false;
		do {
			currentItem = inventory.get(index);
			if (currentItem.equals(item)) {
				found = true; break;
			} 
			if (currentItem.isEmpty())
				break;
			index++;
		} while (index < 100);
		//if index could not be added or is found
		if (index == 100 || found)
			index = -1;
		return index;
		
	}
	
	public String toString() {
		String temp = "Inventory: \n";
		for (int i = 0; i < inventory.size(); i++)
			if (!inventory.get(i).isEmpty())
				temp+= inventory.get(i).toString() + "\n";
		return temp;
	}
	
	public boolean updateItem(Scanner keyboard, boolean isBuying) {
		boolean isProcessed = false;
		Item item = new Item();
		int quantity = 0;
		
		if (!item.inputCode(keyboard)) {
			System.out.println ("Invalid code entered - can't continue");
			isProcessed = false;	
		} else {   // get valid quantity to update
			boolean isValid = false;
			// find inventory item for code
			int index = alreadyExists(item);
			if (index == -1) {
				System.out.println ("Code not found in inventory...");
				isProcessed = false;
			} else {
					while (!isValid) {
							System.out.print ("Enter valid quantity : ");
							if (keyboard.hasNextInt()) {
								quantity = keyboard.nextInt();
								isValid = true;
							}
					if (!isBuying && quantity > 0)  // check that if we are selling - the quantity is negative - if not switch it
						quantity = -1 * quantity;
					isProcessed = inventory.get(index).update(quantity);
					} // end while
			}// end else
			
		}// end else
		return isProcessed;
	}
	
	public boolean writeToFile(FileWriter outFile) {
		try {
			for (int i=0; i < inventory.size(); i++) {
				if (!inventory.get(i).isEmpty())
					outFile.append(inventory.get(i).writeItem() + " ");
			}
			outFile.close();
		}
		catch (IOException e) {
			System.out.println ("Error writing to file....");
			return false;
		}
		return true;
	}
	
	public void findItem (Scanner keyboard) {
		Item item = new Item();
		item.inputCode(keyboard);
		int index = alreadyExists(item);
		if (index != -1) 
			System.out.println ("This item is in inventory at index " + index +"\n"
					+ inventory.get(index).toString());
		else 
			System.out.println ("This item is not in the inventory ");
		
	}
	

}
