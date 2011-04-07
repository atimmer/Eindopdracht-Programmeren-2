package tests;

import java.util.ArrayList;

import server.Game.GameController;
import server.Game.Board.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;



public class BoardTest {
	BoardModel board;
	BoardModel redBoard;
	
	@Before public void setUp() {
		GameController g = new GameController();
		GameController g2 = new GameController();
		board = new BoardModel(g);
		
		redBoard = new BoardModel(g2);
		for (int i = 0; i < 64; i++) {
			redBoard.setBox(i, Mark.RED);
		}
	}
	
	@Test public void initialStatus() {
		// 27, 28, 35, 36
		/*
		 * 27: rood
		 * 28: geel
		 * 35: blauw
		 * 36: groen 
		 */
		Assert.assertTrue(board.getBox(27).equals(Mark.RED));
		Assert.assertTrue(board.getBox(28).equals(Mark.YELLOW));
		Assert.assertTrue(board.getBox(35).equals(Mark.BLUE));
		Assert.assertTrue(board.getBox(36).equals(Mark.GREEN));
	}
	
	public void afterAMove() {
		board.makeMove(29, Mark.RED);

		Assert.assertTrue(board.getBox(27).equals(Mark.RED));
		Assert.assertTrue(board.getBox(28).equals(Mark.RED));
		Assert.assertTrue(board.getBox(29).equals(Mark.RED));
		Assert.assertTrue(board.getBox(35).equals(Mark.BLUE));
		Assert.assertTrue(board.getBox(36).equals(Mark.GREEN));
	}
	
	@Test public void testGetNeighbours() {
		ArrayList<Integer> expected;
		
		redBoard.setBox(0, Mark.BLUE);
		expected = new ArrayList<Integer>(); expected.add(1); expected.add(8); expected.add(9);
		Assert.assertTrue(redBoard.getNeighbours(0).equals(expected));
		redBoard.setBox(0, Mark.RED);
		
		redBoard.setBox(8, Mark.BLUE);
		expected = new ArrayList<Integer>(); expected.add(0); expected.add(1); expected.add(9); expected.add(16); expected.add(17);
		Assert.assertTrue(redBoard.getNeighbours(8).equals(expected));
		redBoard.setBox(8, Mark.RED);
		
		redBoard.setBox(18, Mark.BLUE);
		expected = new ArrayList<Integer>(); expected.add(9); expected.add(10); expected.add(11); expected.add(17); expected.add(19); expected.add(25); expected.add(26); expected.add(27);
		Assert.assertTrue(redBoard.getNeighbours(18).equals(expected));
		redBoard.setBox(18, Mark.RED);
		
		redBoard.setBox(31, Mark.BLUE);
		expected = new ArrayList<Integer>(); expected.add(22); expected.add(23); expected.add(30); expected.add(38); expected.add(39);
		Assert.assertTrue(redBoard.getNeighbours(31).equals(expected));
		redBoard.setBox(31, Mark.RED);
		
		redBoard.setBox(63, Mark.BLUE);
		expected = new ArrayList<Integer>(); expected.add(54); expected.add(55); expected.add(62);
		Assert.assertTrue(redBoard.getNeighbours(63).equals(expected));
		redBoard.setBox(63, Mark.RED);
	}

	@Test public void testLineIsConquering() {
		Assert.assertTrue(!board.lineIsConquering(37, Mark.RED, -1));

		board.setBox(16, Mark.GREEN);
		board.setBox(24, Mark.GREEN);
		board.setBox(32, Mark.GREEN);
		board.setBox(25, Mark.RED);

		Assert.assertTrue(!board.lineIsConquering(25, Mark.RED, -1));
		Assert.assertTrue(!board.lineIsConquering(25, Mark.RED, -9));
		Assert.assertTrue(!board.lineIsConquering(25, Mark.RED, 7));
		
		for (int i=1; i<=8; i++) {
			board.setBox(i * 8 - 1, Mark.GREEN);
		}

		Assert.assertTrue(!board.lineIsConquering(25, Mark.RED, -1));
		Assert.assertTrue(!board.lineIsConquering(25, Mark.RED, -9));
		Assert.assertTrue(!board.lineIsConquering(25, Mark.RED, 7));
		
		for (int i=1; i<=8; i++) {
			board.setBox(i * 8 - 1, Mark.GREEN);
		}
		
		for (int i=0; i<8; i++) {
			board.setBox(i * 8, Mark.GREEN);
		}
		
		for (int i=0; i<8; i++) {
			board.setBox(i * 8 + 1, Mark.RED);


			Assert.assertTrue(!board.lineIsConquering(i*8+1, Mark.RED, -1));
			Assert.assertTrue(!board.lineIsConquering(i*8+1, Mark.RED, -9));
			Assert.assertTrue(!board.lineIsConquering(i*8+1, Mark.RED, 7));
		}
		
		
	}
	
	@Test public void testGetAndSetBox() {
		board.setBox(54, Mark.RED);
		Assert.assertTrue(board.getBox(54).equals(Mark.RED));
		
		board.setBox(0, Mark.RED);
		Assert.assertTrue(board.getBox(0).equals(Mark.RED));
		board.setBox(63, Mark.RED);
		Assert.assertTrue(board.getBox(63).equals(Mark.RED));
		
		
	}
	
	@Test public void testIsFull() {
		Assert.assertTrue(redBoard.isFull());
		Assert.assertTrue(!board.isFull());
	}
	
	public void playThrough() {
		
		board.makeMove(20, Mark.GREEN);
		Assert.assertTrue(board.getBox(20).equals(Mark.GREEN));
		Assert.assertTrue(board.getBox(27).equals(Mark.RED));
		Assert.assertTrue(board.getBox(28).equals(Mark.GREEN));
		Assert.assertTrue(board.getBox(35).equals(Mark.BLUE));
		Assert.assertTrue(board.getBox(36).equals(Mark.GREEN));
		
		board.makeMove(45, Mark.RED);
		Assert.assertTrue(board.getBox(20).equals(Mark.GREEN));
		Assert.assertTrue(board.getBox(27).equals(Mark.RED));
		Assert.assertTrue(board.getBox(28).equals(Mark.GREEN));
		Assert.assertTrue(board.getBox(35).equals(Mark.BLUE));
		Assert.assertTrue(board.getBox(36).equals(Mark.RED));
		Assert.assertTrue(board.getBox(45).equals(Mark.RED));
		
		board.makeMove(44, Mark.GREEN);
		Assert.assertTrue(board.getBox(20).equals(Mark.GREEN));
		Assert.assertTrue(board.getBox(27).equals(Mark.RED));
		Assert.assertTrue(board.getBox(28).equals(Mark.GREEN));
		Assert.assertTrue(board.getBox(35).equals(Mark.BLUE));
		Assert.assertTrue(board.getBox(36).equals(Mark.GREEN));
		Assert.assertTrue(board.getBox(44).equals(Mark.GREEN));
		Assert.assertTrue(board.getBox(45).equals(Mark.RED));
		
		board.makeMove(53, Mark.BLUE);
		Assert.assertTrue(board.getBox(20).equals(Mark.GREEN));
		Assert.assertTrue(board.getBox(27).equals(Mark.RED));
		Assert.assertTrue(board.getBox(28).equals(Mark.GREEN));
		Assert.assertTrue(board.getBox(35).equals(Mark.BLUE));
		Assert.assertTrue(board.getBox(36).equals(Mark.GREEN));
		Assert.assertTrue(board.getBox(44).equals(Mark.BLUE));
		Assert.assertTrue(board.getBox(45).equals(Mark.RED));
		Assert.assertTrue(board.getBox(53).equals(Mark.BLUE));
		
		board.makeMove(52, Mark.GREEN);
		Assert.assertTrue(board.getBox(20).equals(Mark.GREEN));
		Assert.assertTrue(board.getBox(27).equals(Mark.RED));
		Assert.assertTrue(board.getBox(28).equals(Mark.GREEN));
		Assert.assertTrue(board.getBox(35).equals(Mark.BLUE));
		Assert.assertTrue(board.getBox(36).equals(Mark.GREEN));
		Assert.assertTrue(board.getBox(44).equals(Mark.GREEN));
		Assert.assertTrue(board.getBox(45).equals(Mark.RED));
		Assert.assertTrue(board.getBox(52).equals(Mark.GREEN));
		Assert.assertTrue(board.getBox(53).equals(Mark.BLUE));
		
		board.makeMove(59, Mark.RED);
		Assert.assertTrue(board.getBox(20).equals(Mark.GREEN));
		Assert.assertTrue(board.getBox(27).equals(Mark.RED));
		Assert.assertTrue(board.getBox(28).equals(Mark.GREEN));
		Assert.assertTrue(board.getBox(35).equals(Mark.BLUE));
		Assert.assertTrue(board.getBox(36).equals(Mark.GREEN));
		Assert.assertTrue(board.getBox(44).equals(Mark.GREEN));
		Assert.assertTrue(board.getBox(45).equals(Mark.RED));
		Assert.assertTrue(board.getBox(52).equals(Mark.RED));
		Assert.assertTrue(board.getBox(53).equals(Mark.BLUE));
		Assert.assertTrue(board.getBox(59).equals(Mark.RED));
		
		board.makeMove(60, Mark.GREEN);
		Assert.assertTrue(board.getBox(20).equals(Mark.GREEN));
		Assert.assertTrue(board.getBox(27).equals(Mark.RED));
		Assert.assertTrue(board.getBox(28).equals(Mark.GREEN));
		Assert.assertTrue(board.getBox(35).equals(Mark.BLUE));
		Assert.assertTrue(board.getBox(36).equals(Mark.GREEN));
		Assert.assertTrue(board.getBox(44).equals(Mark.GREEN));
		Assert.assertTrue(board.getBox(45).equals(Mark.RED));
		Assert.assertTrue(board.getBox(52).equals(Mark.GREEN));
		Assert.assertTrue(board.getBox(53).equals(Mark.BLUE));
		Assert.assertTrue(board.getBox(59).equals(Mark.RED));
		Assert.assertTrue(board.getBox(60).equals(Mark.GREEN));
		
		board.makeMove(61, Mark.RED);
		Assert.assertTrue(board.getBox(20).equals(Mark.GREEN));
		Assert.assertTrue(board.getBox(27).equals(Mark.RED));
		Assert.assertTrue(board.getBox(28).equals(Mark.GREEN));
		Assert.assertTrue(board.getBox(35).equals(Mark.BLUE));
		Assert.assertTrue(board.getBox(36).equals(Mark.GREEN));
		Assert.assertTrue(board.getBox(44).equals(Mark.GREEN));
		Assert.assertTrue(board.getBox(45).equals(Mark.RED));
		Assert.assertTrue(board.getBox(52).equals(Mark.GREEN));
		Assert.assertTrue(board.getBox(53).equals(Mark.RED));
		Assert.assertTrue(board.getBox(59).equals(Mark.RED));
		Assert.assertTrue(board.getBox(60).equals(Mark.RED));
		Assert.assertTrue(board.getBox(61).equals(Mark.RED));
		
		
		
	}
}