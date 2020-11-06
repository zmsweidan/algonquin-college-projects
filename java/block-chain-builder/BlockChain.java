/*
 * Zaid Sweidan
 * ST# 040887956
 * ASSIGNMENT 3
 * Mar 30 2018
 * 
 * PURPOSE: 
 * 	This class contains methods for a block chain representing an individual course. Each course
 * 	is defined by blocks linked by their hash codes.
 * 
 * MEMBERS:
 * 	(-) blockChain: linked list of blocks used in an instance of the BlockChain class
 * 	(-) courseeName: string value that stores the name of the course
 * 
 * METHODS:
 * 	(+) printBlockChain(): prints the details of a course, showing all of its blocks
 *  (+) getCourseName(): returns a string a value of the course name
 *  (+) verifyChain(): returns a boolean value indicating if the chain is valid(i.e. contains no bad blocks)
 *  (+) addBlock(Scanner): allows the user to add a 'good'(valid) block to the block chain
 *  (+) addBadBlock(Scanner): allows the user to add a 'bad'(invalid) block to the block chain
 *  (+) removeBadBlocks(): removes all bad blocks from the block chain (non consecutive blocks only)
 */
import java.util.*;

public class BlockChain {
	
	private LinkedList<Block> blockChain;
	private String courseName;
	
	public BlockChain ( String courseName) {
		blockChain = new LinkedList<>();
		blockChain.add(new Block());
		this.courseName = new String (courseName);
	}
	
	public void printBlockChain() {
		System.out.println ("For course: " + courseName);
		for (Block b: blockChain)
			System.out.println (b);
	}
	
	public String getCourseName() {
		return courseName;
	}
	
	public boolean verifyChain() {
		
		if(blockChain.size()==1)
			return true;

		for (int j = 1; j <blockChain.size(); j++) {
			if (blockChain.get(j).isEqual(blockChain.get(j-1)));
			else
				return false;
		}
		return true;

	}
		
	public void addBlock(Scanner keyboard) {
		Block newOne = new Block();
		if (newOne.addInfoToBlock(keyboard, blockChain.getLast().getCurrentHash())){
			blockChain.add(newOne);
		}
		
	}
		
	public void addBadBlock(Scanner keyboard) {
		Random random = new Random();
		Block newOne = new Block();
		if (newOne.addInfoToBlock(keyboard, random.nextFloat())){	
			blockChain.add(newOne);
		}
		
	}
		
	public void removeBadBlocks() {
		Block previousBlock = blockChain.getFirst();
		Block currentBlock = blockChain.get(1);
		int i = 1;
		while ( i < blockChain.size()) {
			if (!currentBlock.isEqual(previousBlock)) {
				blockChain.remove(i);
				if (i-1 != blockChain.size()-1) {
					blockChain.get(i).updatePreviousHash(previousBlock.getCurrentHash());
					currentBlock = blockChain.get(i);
				}
			} else {
				previousBlock = currentBlock;
				currentBlock = blockChain.get(i++);
			}
		}
		
	}
		
	
}
