package server.Player;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;

import Protocol.Protocol;
import server.Game.Board.*;

/**
 * The PlayerModel holds all the data for the PlayerController
 * @author Marcel Boersma & Anton Timmermans
 *
 */
public class PlayerModel {

	private Socket sock 	= null;
	private Protocol prot 	= null;
	
	private String name = "";
	private boolean playerIsInGame = false;
	private Mark mark = Mark.EMPTY;
	
	private BufferedReader input = null;
	private BufferedWriter output = null;
	
	


	/**
	 * Set the socket connection
	 * 
	 * @require sock != null
	 * @param sock the socket connection
	 */
	public void setSocket(Socket sock) {
		this.sock = sock;
	}


	/**
	 * Get the socket connection
	 * @return the socket connection
	 */
	public Socket getSocket() {
		return sock;
	}


	/**
	 * Set the protocol for the player
	 * 
	 * @require prot != null
	 * @param prot the protocol the player uses
	 */
	public void setProtocol(Protocol prot) {
		this.prot = prot;
	}


	/**
	 * Get the players protocol
	 * @return the protocol the player uses
	 */
	public Protocol getProtocol() {
		return prot;
	}


	/**
	 * Set the name of the player
	 * 
	 * @require name != null
	 * @param name the name of the player
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * Get the name of the player
	 * 
	 * @return the name of the player
	 */
	public String getName() {
		return name;
	}


	/**
	 * Sets the players game state
	 * 
	 * @require playerIsInGame = true | playersIsInGame = false
	 * @param playerIsInGame true if in game, false if not
	 */
	public void setPlayerIsInGame(boolean playerIsInGame) {
		this.playerIsInGame = playerIsInGame;
	}


	/**
	 * Check if the player is playing a game
	 * 
	 * @return if the player is in the game then it returns true otherwise false
	 */
	public boolean isPlayerIsInGame() {
		return playerIsInGame;
	}


	/**
	 * Sets the current mark for the player
	 * 
	 * @require mark != null
	 * @param mark players current mark
	 */
	public void setMark(Mark mark) {
		this.mark = mark;
	}


	/**
	 * Get the current mark of the player
	 * @ensure result = Mark.BLUE | Mark.YELLOW | Mark.RED | Mark.GREEN | Mark.empty
	 * @return this returns the players' mark 
	 */
	public Mark getMark() {
		return mark;
	}


	/**
	 * Sets the buffered reader of the player object
	 * 
	 * @require input != null
	 * @param input set the buffered reader of the player object
	 */
	public void setInput(BufferedReader input) {
		this.input = input;
	}


	/**
	 * Returns player BufferedReader
	 * 
	 * @return this returns the input reader if it's set, otherwise returns null
	 */
	public BufferedReader getInput() {
		return input;
	}


	/**
	 * Set the writer for the player object
	 * 
	 * @require ouput != null
	 * @param output the buffered writer of the player object
	 */
	public void setOutput(BufferedWriter output) {
		this.output = output;
	}


	/**
	 * Gives the BufferedWriter of the player
	 * 
	 * @return this method returns the BufferedWriter if it's set, otherwise it returns null
	 */
	public BufferedWriter getOutput() {
		return output;
	}

}
