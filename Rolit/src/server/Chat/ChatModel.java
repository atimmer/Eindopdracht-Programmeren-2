package server.Chat;


import java.util.concurrent.CopyOnWriteArrayList;

import server.Player.*;

/**
 * Keeps track of all the data for the ChatController
 * @author Marcel Boersma & Anton Timmermans
 *
 */
public class ChatModel {

	private CopyOnWriteArrayList<PlayerController> players = null;
	
	/**
	 * Setup a chat model
	 */
	public ChatModel()
	{
		this.setPlayers(new CopyOnWriteArrayList<PlayerController>());
	}

	/**
	 * Set the players
	 * 
	 * @require players != null
	 * @param players
	 */
	public void setPlayers(CopyOnWriteArrayList<PlayerController> players) {
		this.players = players;
	}

	/**
	 * Get the players
	 * 
	 * @ensure result != null
	 * @return the current players in chat
	 */
	public CopyOnWriteArrayList<PlayerController> getPlayers() {
		return players;
	}
	
	/**
	 * Add a player to the chat
	 * 
	 * @require p != null
	 * @ensure old.getPlayers().size() + 1 = new.getPlayers().size()
	 * @param p Player
	 */
	public void addPlayer(PlayerController p)
	{
		this.players.add(p);
	}
	
	/**
	 * Remove a player from chat
	 * 
	 * @ensure old.getPlayers().size() - 1 = new.getPlayers().size()
	 * @require p != null
	 * 			getPlayers().contains(p)
	 * @param p
	 */
	public void removePlayer(PlayerController p)
	{
		this.players.remove(p);
	}
}
