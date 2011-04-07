package server.Game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import Protocol.DataObjectHandler;
import Protocol.DataObject.AbstractDataObject;

import server.Player.*;
import server.Game.Board.*;
import server.*;

/**
 * 
 * The GameController handles everything which has to do with a game.
 * 
 * @author Marcel Boersma & Anton Timmermans
 *
 */
public class GameController implements DataObjectHandler {

	private GameModel model;
	
	private Collection<DataObjectHandler> objectReaders = null;
	
	/**
	 * Get the GameController instance
	 */
	public GameController()
	{
		this.model = new GameModel();
		this.objectReaders 	= new ArrayList<DataObjectHandler>();
		
		this.addDataObjectReader(this.model.getChatController());
	}
	
	/**
	 * Returns the current player
	 * 
	 * @return returns the player whose turn it is or null if there aren't any player in the game
	 */
	public PlayerController currentPlayer()
	{
		if(this.model.getPlayersInGame() != 0)
			return this.model.getPlayer(this.model.getTurn());
		else
			return null;
	}
	
	/**
	 * Will return the player name of the player who's turn it is.
	 * 
	 * @ensure result != null
	 * @return the player name whose turn it is
	 */
	public String whosTurnIsIt()
	{
		if(this.model.getPlayersInGame() != 0)
			return this.model.getPlayer(this.model.getTurn()).getPlayerName();
		else
			return "";
	}
	
	/**
	 * Gives the next player the rights to make a move
	 */
	public void nextPlayersTurn()
	{
		
		if((this.model.getTurn() + 1) < this.model.getCapacity())
		{
			this.model.setTurn(this.model.getTurn() + 1);
		}else{
			this.model.setTurn(0);
		}
		
		
		this.broadcastMessage(this.whosTurnIsIt(), "turn");
		
	}
	
	
	/**
	 * Gives the maximum amount of players for this game.
	 * 
	 * @return integer
	 */
	public int getCapacity()
	{
		return this.model.getCapacity();
	}
	
	/**
	 * Gives the amount of free seats in the game.
	 * 
	 * @return integer 
	 */
	public int getAvailable()
	{
		return (this.model.getCapacity() - this.model.getPlayersInGame());
		
	}
	

	/**
	 * Sets the maximum amount of players for this game.
	 * 
	 * @require 1 < players < 5
	 * @param players
	 */
	public void setPlayers(int players)
	{
		if(players > 1 && players < 5){
			this.model.setCapacity(players);
		}
	}
	
	/**
	 * Broadcast a message to all the players in the game
	 * 
	 * @param message to broadcast
	 * @param fromMethod method which initiates the broadcast 
	 */
	public void broadcastMessage(String message, String fromMethod)
	{
		Iterator<PlayerController> i = this.model.getPlayers().iterator();
		
		while(i.hasNext())
		{
			PlayerController currentPlayer = i.next();
						
			AbstractDataObject ADO = new AbstractDataObject();
			ADO.To = "Player";
			ADO.From = this;
			ADO.FromMethod = fromMethod;
			ADO.What = message;
			
			currentPlayer.exec(ADO);
			
		}
	}
	
	/**
	 * Gives the players a mark
	 */
	private void setMarksForPlayers()
	{
		//randomize the list
		
		CopyOnWriteArrayList<PlayerController> randomList = new CopyOnWriteArrayList<PlayerController>();
		
		for (int i = 0; i < this.model.getCapacity(); i++)
		{
			Random generator = new Random();
			int rPlayer = generator.nextInt(this.model.getPlayers().size());
			
			randomList.add(this.model.getPlayers().get(rPlayer));
			this.model.getPlayers().remove(rPlayer);
		}
		
		this.model.setPlayers(randomList);
		
		
		
		//set the marks
		
		for(int i = 0 ; i < this.model.getCapacity(); i++)
		{
			if(this.model.getCapacity() == 2){
				
				switch(i){
				case 0 :
						this.model.getPlayer(0).setMark(Mark.RED);
				break;
				case 1 :
						this.model.getPlayer(1).setMark(Mark.GREEN);
				break;
				}
				
			}else{
				switch(i)
				{
				
				case 0 :
					this.model.getPlayer(0).setMark(Mark.RED);
				break;
				case 1 :
					this.model.getPlayer(1).setMark(Mark.YELLOW);
				break;
				case 2 :
					this.model.getPlayer(2).setMark(Mark.GREEN);
				break;
				case 3 :
					this.model.getPlayer(3).setMark(Mark.BLUE);
				break;
				}
				
			}
			
			
			
		}
	}
	
	/**
	 * This function will add a player to the game, if a game is full it will start the game.
	 * 
	 * @param p PlayerController
	 */
	public void addPlayer(PlayerController p) {
		
		
		//check if there are seats available before adding the player to the game
		if(this.getAvailable() > 0){
			this.model.addPlayer(p);
			this.model.increasePlayersInGame();
			this.model.getChatController().addPlayerToChat(p);
		
			//change player state
			p.setPlayerInGameTo(true);
				
			if(this.model.getCapacity() == this.model.getPlayersInGame())
				this.startGame();
		
		}
		
	}
	
	/**
	 * Starts the game and will create the board object
	 * 
	 * @require getAvailable() == 0
	 */
	private void startGame()
	{
		//create a board
		this.model.setBoard(new BoardController(this));
		this.addDataObjectReader(this.model.getBoard());
		//the first turn is for the first player who entered the game
		
		ServerGUI.printMessage("The game started, good luck everybody!...");
		ServerGUI.printMessage("");
		
		//set marks for all players
		this.setMarksForPlayers();
		
		for(int i = 0 ; i < this.getCapacity() ; i++){
			ServerGUI.printMessage("Player "+ i +" : " +this.model.getPlayer(i).getPlayerName() + " with mark " + this.model.getPlayer(i).getMark().toString());
		}
		
		
		//notify all players
		Iterator<PlayerController> p = this.model.getPlayers().iterator();
		
		int counter = 0;
		
		while(p.hasNext())
		{
			PlayerController currentPlayer 	= p.next();
			
			AbstractDataObject startGame = new AbstractDataObject();
			startGame.From 			= this;
			startGame.To			= "Player";
			startGame.FromMethod 	= "startGame";
			startGame.What			= this.model.getPlayers();

			//acknowledge start command
			currentPlayer.exec(startGame);
			
			AbstractDataObject turn = new AbstractDataObject();
			turn.From 				= this;
			turn.To					= "Player";
			turn.FromMethod 		= "turn";
			turn.What				= this.model.getPlayer(0).getPlayerName();

			//acknowledge turn command
			currentPlayer.exec(turn);
			
			counter++;
		}
		
		ServerGUI.printMessage(this.model.getBoard().toString());
		
	}
	
	/**
	 * Ends the game
	 * 
	 * @param force is true if the game is forced to end in case of kicked player
	 */
	public void endGame(boolean force)
	{
		//notify all players that the game ended!
		ServerGUI.printMessage("Another game has ended, see you in the lobby!;)");
		Iterator<PlayerController> i = this.model.getPlayers().iterator();
		
		String scores = "";
		
		if(!force)
		{
			while(i.hasNext())
			{
				PlayerController p = i.next();
				scores += " " + this.model.getBoard().getPointForMark(p.getMark());
			}
		}else{
			scores += " 0";//player has been removed before score could be printed, add the score
			//forced end game, reset all the scores to zero
			while(i.hasNext())
			{
				i.next();
				scores += " 0";
			}
		}
		
		//reset iterator
		i = this.model.getPlayers().iterator();
		
		while(i.hasNext())
		{
			PlayerController p = i.next();
			p.setPlayerInGameTo(false);
			
			//remove player from the game chat
			this.model.getChatController().removePlayerFromChat(p);
			
			AbstractDataObject ADO = new AbstractDataObject();
			ADO.To = "Player";
			ADO.From = this;
			ADO.FromMethod = "endGame";
			
			ADO.What = scores;
			
			p.exec(ADO);
		}
		
		//notify the game controller that the game ended
		ServerController.sharedApplication().endGame(this);
	}
	
	/**
	 * Get the list of current players in the game
	 * @ensure result != null
	 * @return Collection with playerControllers
	 */
	public Collection<PlayerController> getPlayers()
	{
		return this.model.getPlayers();
	}
		
	
	/**
	 * Kicks a player from the game and ends it
	 * 
	 * @param p PlayerController
	 */
	public void kickPlayer(PlayerController p)
	{
		//removes the kicked player from the game
		this.model.removePlayer(p); 
		this.model.getChatController().removePlayerFromChat(p);
		
		//notify everybody that player p will be kicked
		this.broadcastMessage(p.getPlayerName(), "kick");
		
		//remove player from thread
		ServerController.sharedApplication().sThread.remove(p);

		this.endGame(true);//ends the game
		
	}
	
	
	
	
	@Override
	public void exec(AbstractDataObject obj) {
		if(obj.To.equals(this.getIdentifier()))
		{
			//there is some data for this object...
		 
			
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
		
		this.objectReaders.add(obj);
		
	}

	
	@Override
	public String getIdentifier() {
		return "Game";
	}

	
	
}
