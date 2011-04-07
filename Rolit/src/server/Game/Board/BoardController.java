package server.Game.Board;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import Protocol.DataObjectHandler;
import Protocol.DataObject.AbstractDataObject;

import server.Game.*;
import server.*;
import server.Player.*;

/**
 * The BoardController handles everything on the Rolit Board
 * @author Marcel Boersma & Anton Timmermans
 *
 */
public class BoardController implements DataObjectHandler{

	private Collection<DataObjectHandler> objectReaders = null;
	private BoardModel model;
	
	/**
	 * Get a BoardController instance
	 * @param gameController the game where the board is in.
	 */
	public BoardController(GameController gameController)
	{
		this.model = new BoardModel(gameController);
		this.objectReaders = new ArrayList<DataObjectHandler>();

	}
	
	/**
	 * Look if a move is legal
	 * 
	 * @param box the place where you want to put the mark
	 * @param m the mark
	 * @return true if it's legal and false if its illegal
	 */
	public boolean legalMove(int box, Mark m)
	{
		return this.model.legalMove(box, m);
		
	}
	
	/**
	 * Returns the Mark of the box with number boxNumber
	 * 
	 * @ensure result = Mark.GREEN | Mark.BLUE | Mark.YELLOW | Mark.RED | Mark.EMPTY
	 * @return the Mark of the box
	 */
	public Mark getBox(int boxNumber) {
		
		return this.model.getBox(boxNumber);	
			
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
		return this.model.makeMove(box, m);
	}
	
	/**
	 * Calculate the point for the mark
	 * 
	 * @param m the mark
	 * @return amount of points
	 * @ensure 0 <= result <= 63 
	 */
	public int getPointForMark(Mark m)
	{
		return this.model.getPointForMark(m);
	}
	
	/**
	 * Returns the Mark of the winner
	 * @require this.hasWinner()
	 * @ensure result != null
	 * @return the mark who won
	 */
	public Mark getWinner() {
		
		return this.model.getWinner();

	}
	
	/**
	 * Returns if the board has a winner.
	 * @ensure result = this.isFull()
	 * @return if the board has a winner.
	 */
	public boolean hasWinner() {
		return this.model.hasWinner();
	}
	
	/**
	 * Returns whether the board is full or not
	 * @return Whether the board is full
	 */
	public boolean isFull() {		
		return this.model.isFull();
	}
	
	@Override
	public String toString() {
		
		String board = "";
		
		for(int i = 0; i < 8; i++)
		{
			//rows
			String row = "";
			for (int a = 0; a < 8; a++)
			{
				int box = i*8 + a;
				Mark m = this.getBox(box);
				row += "| " + m.toString() + " |";
			}
			
			board += row + "\n";
		}
		
		return board;
		
	}
	
	
	@Override
	public void exec(AbstractDataObject obj) {
		
		if(obj.To.equals(this.getIdentifier())){
		
			PlayerController p = (PlayerController) obj.From;
			
			
			if(this.model.getCurrentGameController().whosTurnIsIt().equals(p.getPlayerName()))
			{
				ServerGUI.printMessage("Moving..." + obj.What);
				int moveTo = Integer.parseInt(obj.What.toString());
				

				if(this.model.makeMove(moveTo, this.model.getCurrentGameController().currentPlayer().getMark()))
				{
				
					ServerGUI.printMessage("Nice move " + p.getPlayerName() + " !;)");
					
					this.model.getCurrentGameController().broadcastMessage(p.getPlayerName() + " " +obj.What.toString(), "movedone"); 
					if(!this.isFull()){
						this.model.getCurrentGameController().nextPlayersTurn();
					}else{
						this.model.getCurrentGameController().endGame(false);
					}
					
					ServerGUI.printMessage(this.toString());
					
				}else{
					
					ServerGUI.printMessage("Woops wrong move " + p.getPlayerName() + " now you need to leave the game! Bye bye...");
					
					this.model.getCurrentGameController().kickPlayer(p);
					
					
				}
				
				
			}else{
				ServerGUI.printMessage("It's not your turn : " + p.getPlayerName() + " so be patient and wait a second");
			}
		}else{
			if(this.objectReaders != null){
				
				Iterator<DataObjectHandler> i = this.objectReaders.iterator();
				
				while(i.hasNext())
				{
					i.next().exec(obj);
				}
			}
		}
	}

	
	@Override
	public void addDataObjectReader(DataObjectHandler obj) {
		this.addDataObjectReader(obj);
	}

	
	@Override
	public String getIdentifier() {
		return "Board";
	}
	
}
