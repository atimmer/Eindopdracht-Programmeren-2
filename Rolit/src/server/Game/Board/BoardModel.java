package server.Game.Board;

import java.util.ArrayList;
import java.util.Iterator;

import server.Game.*;

/**
 * The board model holds all the data for the boardControlelr
 * @author Marcel Boersma & Anton Timmermans
 *
 */
public class BoardModel {
	
	private GameController currentGame;

	private ArrayList<Mark> boxes;
	private ArrayList<Integer> emptyBoxes;
	private ArrayList<Integer> adjacentBoxes;
	
	/**
	 * Construct the board model with game controller
	 * 
	 * @require g != null
	 * @param g Game controller
	 */
	public BoardModel(GameController g)
	{
		this.currentGame = g;
		this.boxes = new ArrayList<Mark>();
		this.emptyBoxes = new ArrayList<Integer>();
		this.adjacentBoxes = new ArrayList<Integer>();

		Mark add;
		for(int i = 0; i < 64; i++)
		{
			switch (i) {
			case 27:
				add = Mark.RED;
				break;
			case 28:
				add = Mark.YELLOW;
				break;
			case 35:
				add = Mark.BLUE;
				break;
			case 36:
				add = Mark.GREEN;
				break;
			default:
				add = Mark.EMPTY;
				this.emptyBoxes.add(i);
				break;
			}
			this.boxes.add(add);
		}

		this.adjacentBoxes.add(18);
		this.adjacentBoxes.add(19);
		this.adjacentBoxes.add(20);
		this.adjacentBoxes.add(21);
		this.adjacentBoxes.add(26);
		this.adjacentBoxes.add(29);
		this.adjacentBoxes.add(34);
		this.adjacentBoxes.add(37);
		this.adjacentBoxes.add(42);
		this.adjacentBoxes.add(43);
		this.adjacentBoxes.add(44);
		this.adjacentBoxes.add(45);

	}

	/**
	 * Set the current game controller
	 * 
	 * @require currentGame != null
	 * @param currentGame the game controller
	 */
	public void setCurrentGameController(GameController currentGame) {
		this.currentGame = currentGame;
	}

	/**
	 * Get the current game controller
	 * 
	 * @ensure result != null
	 * @return the game controller
	 */
	public GameController getCurrentGameController() {
		return currentGame;
	}

	/**
	 * Add a mark to the box array
	 * 
	 * @require m = Mark.GREEN | Mark.BLUE | Mark.YELLOW | Mark.RED | Mark.EMPTY
	 * @param m Mark
	 */
	public void addBox(Mark m) {
		this.boxes.add(m);		
	}


	/**
	 * Looks if the move you want to make is legal
	 *
	 * @param box the place where you want to put the mark
	 * @param m the mark you want to place
	 * @return true if legal, false if illegal
	 */
	public boolean legalMove(int box, Mark m)
	{
		return (!this.isFull() && this.getBox(box) == Mark.EMPTY && this.getPossibleMoves(m).contains(box));
	}


	/**
	 * Get the adjacent empty boxes
	 * 
	 * @param box the middle box
	 * @return the array with empty boxes
	 */
	private ArrayList<Integer> getAdjacendEmptyBoxes(int box) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		int rowPosition = box % 8;
		int[] adjacendBoxes;
		switch(rowPosition) {
		case 0:
			int[] adjacendBoxesLeft = {box - 8, box - 7, box + 1, box + 8, box + 9};
			adjacendBoxes = adjacendBoxesLeft;
			break;
		case 7:
			int[] adjacendBoxesRight = {box - 9, box - 8, box - 1, box + 7, box + 8};
			adjacendBoxes = adjacendBoxesRight;
			break;
		default:
			int[] adjacendBoxesFull = {box - 9, box - 8, box - 7, box - 1, box + 1, box + 7, box + 8, box + 9};
			adjacendBoxes = adjacendBoxesFull;
			break;
		}
		for(int i = 0; i < adjacendBoxes.length; i++) {
			int current = adjacendBoxes[i];
			if(current >= 0 && current <= 63 && this.getBox(current).equals(Mark.EMPTY)) {
				result.add(adjacendBoxes[i]);
			}
		}
		
		return result;
	}

	/**
	 * Get all the boxes who aren't empty or have the same mark as the box you want to check the neighbors from
	 * 
	 * @param box the position you want to check
	 * @return Array list with neighbors
	 */
	public ArrayList<Integer> getNeighbours(int box) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		int rowPosition = box % 8;
		int[] adjacendBoxes;
		switch(rowPosition) {
		case 0:
			int[] adjacendBoxesLeft = {box - 8, box - 7, box + 1, box + 8, box + 9};
			adjacendBoxes = adjacendBoxesLeft;
			break;
		case 7:
			int[] adjacendBoxesRight = {box - 9, box - 8, box - 1, box + 7, box + 8};
			adjacendBoxes = adjacendBoxesRight;
			break;
		default:
			int[] adjacendBoxesFull = {box - 9, box - 8, box - 7, box - 1, box + 1, box + 7, box + 8, box + 9};
			adjacendBoxes = adjacendBoxesFull;
			break;
		}
		// // // System.out.println(rowPosition);
		for (int i = 0; i<adjacendBoxes.length; i++) {
			int current = adjacendBoxes[i];
			if (current >= 0 && current <= 63 && this.getBox(current) != Mark.EMPTY && this.getBox(current) != this.getBox(box)) {
				result.add(current);
			}
		}
		
		return result;
	}

	/**
	 * Pivots all the balls for the current move
	 * 
	 * @param move position where the mark is placed
	 * @param m the mark
	 */
	private void goodPivotBallsAfterMove(int move, Mark m) {
		if (moveIsConquering(move, m)) {
			ArrayList<Integer> neighbours = this.getNeighbours(move);
			
			for (int i=0; i < neighbours.size(); i++) {
				int difference = neighbours.get(i) - move;
				int otherBox = this.otherBallOnLine(move, m, difference);
				if (otherBox != -1) {
					// // // System.out.println(neighbours.get(i)+ ":" + difference + ":" + otherBox);
					int currentBox;
					for (currentBox = move + difference; currentBox != otherBox; currentBox += difference) {
						// // System.out.println("    " + currentBox);
						this.setBox(currentBox, m);
					}
					// // System.out.println("    " + currentBox);
				}
			}
		}
	}

	/**
	 * Returns the other ball (if there is one) on a line, starting from move and going into direction (ea, move +- direction)
	 * 
	 * @param move The box to start from
	 * @param m the mark that does the move
	 * @param direction the direction to go in (-9, -8, -7, -1, 1, 7, 8, 9)
	 * @return the other ball on the line if there is one
	 */
	public int otherBallOnLine(int move, Mark m, int direction) {
		int result = -1;
		int leftBound = -1;
		int rightBound = 64;
		int posOnRow = move % 8;
		if (Math.abs(direction) == 1) {
			leftBound = move - (move % 8) - 1;
			rightBound = move - (move % 8) + 8;
		} else if (Math.abs(direction) == 8){
			
		} else if (direction == -9) {
			leftBound = move + (posOnRow + 1) * direction;
		} else if (direction == -7) {
			leftBound = move + (8 - posOnRow) * direction;
		} else if (direction == 7) {
			rightBound = move + (posOnRow + 1) * direction;
		} else if (direction == 9) {
			rightBound = move + (8 - posOnRow) * direction;
		}
		
		leftBound = (leftBound > -1)? leftBound: -1;
		rightBound = (rightBound < 64)? rightBound: 64;
		
		
		// System.out.println(leftBound);
		// System.out.println(rightBound);
		// // System.out.println("\t\t\t\t" + leftBound + ":" + rightBound + ":" + direction);
		
		for (int currentBox = move + direction; currentBox < rightBound && currentBox > leftBound && result == -1; currentBox += direction) {
			// // // System.out.println("current: "+ currentBox);
			if ((currentBox == move + direction) && this.getBox(currentBox) == m) {
				break;
			} else if (this.getBox(currentBox) == m) {
				result = currentBox;
			} else if (this.getBox(currentBox) == Mark.EMPTY) {
				break;
			} 
		}
		
		return result;
	}
	
	/**
	 * Looks if the line is conquering for the direction
	 * 
	 * @param move position where the mark will be placed
	 * @param m the mark
	 * @param direction the direction to check (e.g. horizontal etc..)
	 * @return true if conquering, false if not
	 */
	public boolean lineIsConquering(int move, Mark m, int direction) {
		return (this.otherBallOnLine(move, m, direction) != -1);
	}
	
	/**
	 * Looks if the move is conquering
	 * 
	 * @param move the position where the mark will be placed
	 * @param m the mark
	 * @return true if conquering, false if not
	 */
	public boolean moveIsConquering(int move, Mark m) {

		boolean result = false;
		ArrayList<Integer> neighbours = this.getNeighbours(move);
		// // System.out.println("\t\t"+move+":"+neighbours);
		
		for (int i=0; i < neighbours.size() && result == false; i++) {
			int difference = neighbours.get(i) - move;
			// // System.out.println("\t\t\t"+neighbours.get(i)+":"+difference);
			result = lineIsConquering(move, m, difference);
		}
		
		return result;
	}
	
	
	/**
	 * Get all the possible moves for the mark
	 * 
	 * @ensure result != null
	 * @param m the mark
	 * @return ArrayList with positions
	 */
	public ArrayList<Integer> getPossibleMoves(Mark m)
	{
		ArrayList<Integer> result = new ArrayList<Integer>();
	
			
			ArrayList<Integer> conqueringMoves = new ArrayList<Integer>();
			for (int i = 0; i < this.adjacentBoxes.size(); i++) {
				if (this.moveIsConquering(adjacentBoxes.get(i), m)) {
					conqueringMoves.add(this.adjacentBoxes.get(i));
				}
			}
			
			
			if (conqueringMoves.size() == 0) {
				result = this.adjacentBoxes;
			} else {
				result = conqueringMoves;
			}
			
		
		return result;


	}
	
	/**
	 * Do the move
	 * 
	 * @ensure if (this.legalMove(box))
	 * 				this.getBox(box) == m
	 * 			else 
	 * 				this.getBox(box) != m
	 * @param box the position to place the mark
	 * @param m the mark to place on the position
	 * @return true if done, false if not
	 */
	public boolean makeMove(int box, Mark m)
	{
		boolean moveDone = false;
		
		
		if(this.legalMove(box, m))
		{
			this.setBox(box, m);
			this.goodPivotBallsAfterMove(box, m);

			this.emptyBoxes.remove(new Integer(box));
			moveDone = true;
			
			this.adjacentBoxes.remove(new Integer(box));
			ArrayList<Integer> adjacendEmptyBoxes = this.getAdjacendEmptyBoxes(box);
			for (int i = 0; i < adjacendEmptyBoxes.size(); i++) {
				if (!this.adjacentBoxes.contains(adjacendEmptyBoxes.get(i))) {
					this.adjacentBoxes.add(adjacendEmptyBoxes.get(i));
				}
			}
			
			moveDone = true;
		}
		
		
		return moveDone;
	}

	/**
	 * Set the mark on the position
	 * 
	 * @ensure this.getBox(boxNumber) == m
	 * @param boxNumber position where the mark will be placed
	 * @param m the mark
	 */
	public void setBox(int boxNumber, Mark m) {
		if (m.equals(Mark.EMPTY) && !this.emptyBoxes.contains(boxNumber)) {
			this.emptyBoxes.add(boxNumber);
		} else {
			this.emptyBoxes.remove(new Integer(boxNumber));
		}
		
		this.boxes.set(boxNumber, m);
	}
	
	/**
	 * Returns the Mark of the box with number boxNumber
	 * 
	 * @require 0 =< boxNumber < 64 
	 * @return the Mark of the box
	 */
	public Mark getBox(int boxNumber) {
		return this.boxes.get(boxNumber);
	}
	
	/**
	 * Get the score for the mark
	 * 
	 * @require m != Mark.EMPTY
	 * 			m != null
	 * @ensure 0 <= result <= 64 
	 * @param m the mark
	 * @return the amount of balls
	 */
	public int getPointForMark(Mark m)
	{
		Iterator<Mark> i = this.boxes.iterator();
		
		int score = 0;
		
		while(i.hasNext())
		{
			if(m.equals(i.next()))
				score++;
		}
		
		return score;
	}
	
	/**
	 * Returns the Mark of the winner
	 * 
	 * @require this.hasWinner()
	 * @ensure result != null
	 * @return the mark who wins the game
	 */
	public Mark getWinner() {
		
		int scores[] = new int[4];

			
		Iterator<Mark> i = this.boxes.iterator();
			
		while(i.hasNext())
		{
			
			Mark m = i.next();
			
			if (m.equals(Mark.RED))
			{
				scores[0]++;
				
			}else if(m.equals(Mark.BLUE))
			{
				scores[1]++;
			}else if(m.equals(Mark.YELLOW))
			{
				scores[2]++;
			}else if(m.equals(Mark.GREEN))
			{
				scores[3]++;
			}
			
		}
		
		int max = -1;
		int maxScore = -1;
		int a = 0;
		
		while(a < 4)
		{
			if(maxScore < scores[a])
			{
				maxScore = scores[a];
				max = a;
			}
			
			a++;
		}
		
		Mark m = Mark.EMPTY;
		
		switch(max)
		{
		case 0:
			m = Mark.RED;
			break;
		case 1 :
			m = Mark.BLUE;
			break;
		case 2 :
			m = Mark.YELLOW;
			break;
		case 3 :
			m = Mark.GREEN;
			break;
		}
			
		
		return m;
	
		
	}
	
	/**
	 * Returns if the board has a winner.
	 * 
	 * @ensure result = this.isFull()
	 * @return if the board has a winner.
	 */
	public boolean hasWinner() {
		int scores[] = new int[4];

		
		Iterator<Mark> i = this.boxes.iterator();
			
		while(i.hasNext())
		{
			
			Mark m = i.next();
			
			if (m.equals(Mark.RED))
			{
				scores[0]++;
				
			}else if(m.equals(Mark.BLUE))
			{
				scores[1]++;
			}else if(m.equals(Mark.YELLOW))
			{
				scores[2]++;
			}else if(m.equals(Mark.GREEN))
			{
				scores[3]++;
			}
			
		}
		
		return (!(scores[0] ==  scores[1] && scores[1] == scores[2] && scores[2] == scores[3]));
		
	}
	
	/**
	 * Returns whether the board is full or not
	 * 
	 * @return Whether the board is full
	 */
	public boolean isFull() {
		return (this.emptyBoxes.size() == 0);
	}
	
	@Override
	public boolean equals(Object obj) {
		return false;
	}
	
	

	
	
}
