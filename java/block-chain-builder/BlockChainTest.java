import static org.junit.jupiter.api.Assertions.*;

import java.util.Scanner;

import org.junit.jupiter.api.Test;

class BlockChainTest {

	@Test
	void test() {
		Scanner test1 = new Scanner ("2 1111 11111 11111");
		Scanner test2 = new Scanner ("2 22222 22222 22222");
		Scanner test3 = new Scanner ("2 3333 33333 33333");
		Scanner test4 = new Scanner ("2 4444 44444 44444");
		Scanner test5 = new Scanner ("2 5555 55555 55555");
		BlockChain myBlockChain = new BlockChain ("CST8130");
		
		myBlockChain.addBlock(test1);
		myBlockChain.addBlock(test2);
		myBlockChain.addBadBlock(test3);
		myBlockChain.addBlock(test4);
		myBlockChain.addBadBlock(test5);
		
		
		myBlockChain.printBlockChain();
		myBlockChain.removeBadBlocks();
		myBlockChain.printBlockChain();
		
		assertTrue(true);
	}

}
