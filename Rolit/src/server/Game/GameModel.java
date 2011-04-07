package server.Game;

import java.util.concurrent.CopyOnWriteArrayList;

import server.Game.Board.*;
import server.Player.*;
import server.Chat.*;

/**
 * This is the model site of the game
 * @author Marcel Boersma & Anton Timmermans
 *
 */
public class GameModel {

	private BoardController board = null;
	private ChatController ChatController = null;
	private int turn = 0;
	
	private int capacity = 0;
	private int playersInGame = 0;
	
	private CopyOnWriteArrayList<PlayerController> players = null;
	
	/**
	 * Setup a game model
	 */
	public GameModel()
	{
		
		this.setPlayers(new CopyOnWriteArrayList<PlayerController>());
		this.ChatController 			= new ChatController();
		
	}
	
	/**
	 * Get the chat controller for the game
	 * 
	 * @ensure result != null
	 * @return Chat controller
	 */
	public ChatController getChatController()
	{
		return this.ChatController;
	}
	
	/**
	 * Set the chat controller
	 * 
	 * @require c != null
	 * @param c Chat controller
	 */
	public void setChatController(ChatController c)
	{
		this.ChatController = c;
	}

	/**
	 * Set the board controller
	 * 
	 * @require baord != null
	 * @param board Board controller
	 */
	public void setBoard(BoardController board) {
		this.board = board;
	}

	/**
	 * Get the board
	 * 
	 * @return null or the board controller
	 */
	public BoardController getBoard() {
		return board;
	}

	/**
	 * Set the turn index
	 * 
	 * @require 0 <= turn < getCapacity()
	 * @param turn
	 */
	public void setTurn(int turn) {
		this.turn = turn;
	}

	/**
	 * Get the turn index
	 * @ensure IF getCapacity > 0 THEN 0 <= result < getCapacity() ELSE 0 <= result <= getCapacity()
	 * @return the current index
	 */
	public int getTurn() {
		return turn;
	}

	/**
	 * Set the player capacity of the game
	 * 
	 * @require capacity > 0
	 * @param capacity amount of players
	 */
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
	/**
	 * Increase the capacity by i
	 * 
	 * @ensure old.getCapacity() + i = new.getCapacity()
	 * @param i amount to increase
	 */
	public void increaseCapacityBy(int i)
	{
		this.capacity += i;
	}

	/**
	 * Get the game player capacity
	 * 
	 * @ensure result >= 0
	 * @return the player capacity of the game
	 */
	public int getCapacity() {
		return capacity;
	}

	/**
	 * Set the amount of players all ready in the game
	 * 
	 * @require 0 <= playersInGame  <= getCapacity();
	 * @param playersInGame amount of players in the game
	 */
	public void setPlayersInGame(int playersInGame) {
		this.playersInGame = playersInGame;
	}
	
	/**
	 * Increase the amount of players in the game by one
	 * 
	 * @ensure old.getPlayersInGame() + 1 = new.getPlayersInGame();
	 */
	public void increasePlayersInGame()
	{
		this.playersInGame += 1;
	}

	/**
	 * Get the amount of players in the game
	 * 
	 * @ensure result >= 0
	 * @return the amount of players in the game
	 */
	public int getPlayersInGame() {
		return playersInGame;
	}

	/**
	 * Set the players
	 * 
	 * @require players != null
	 * @param copyOnWriteArrayList array list with player controllers
	 */
	public void setPlayers(CopyOnWriteArrayList<PlayerController> copyOnWriteArrayList) {
		this.players = copyOnWriteArrayList;
	}
	
	/**
	 * Get the player at index i
	 * 
	 * @param i index
	 * @require 0 <= i < getPlayers().size()
	 * @return the player object at index i
	 */
	public PlayerController getPlayer(int i)
	{
		return this.players.get(i);
	}
	
	/**
	 * Add a player to the game
	 * 
	 * @ensure old.getPlayers().size() + 1 = new.getPlayers().size()
	 * @require p != null
	 * @param p Player
	 */
	public void addPlayer(PlayerController p)
	{
		this.players.add(p);
	}

	/**
	 * Remove player form the game
	 * 
	 * @require getPlayers().contains(p)
	 * @ensure old.getPlayers().size() -1 = new.getPlayers().size()
	 * @param p Player
	 */
	public void removePlayer(PlayerController p)
	{
		this.players.remove(p);
	}
	
	/**
	 * Get the list of players controllers
	 * 
	 * @ensure result != null
	 * @return list of player controllers
	 */
	public CopyOnWriteArrayList<PlayerController> getPlayers() {
		return players;
	}
	
	
	
}
