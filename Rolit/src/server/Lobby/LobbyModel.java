package server.Lobby;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

import server.Chat.*;
import server.Player.*;
import server.Challenge.*;

/**
 * Contains all the data for the lobby controller
 * 
 * @author Marcel Boersma & Anton Timmermans
 *
 */
public class LobbyModel {

	private ChatController LobbyChatController = null;
	private CopyOnWriteArrayList<PlayerController> PlayerControllers = null;
	private ChallengeController challengeController = null;
	
	/**
	 * Construct the lobby with a lobby controller
	 * 
	 * @require lc != null
	 * @param lc lobby controller
	 */
	public LobbyModel(LobbyController lc)
	{
		this.setLobbyChatController(new ChatController());
		this.setChallengeController(new ChallengeController(lc));
		this.setPlayerControllers(new CopyOnWriteArrayList<PlayerController>());
		
	}

	/**
	 * Set the chat controller for the lobby
	 * 
	 * @require lobbyChatController != null
	 * @ensure getLobbyChatController() != null;
	 * @param lobbyChatController the chat controller for the lobby
	 */
	public void setLobbyChatController(ChatController lobbyChatController) {
		LobbyChatController = lobbyChatController;
	}

	/**
	 * Get the lobby chat controller
	 * 
	 * @ensure result != null
	 * @return the chat controller
	 */
	public ChatController getLobbyChatController() {
		return LobbyChatController;
	}
	
	/**
	 * Adds a player to the chat
	 * 
	 * @require p != null
	 * @param p Player
	 */
	public void addPlayerToChatController(PlayerController p)
	{
		this.LobbyChatController.addPlayerToChat(p);
	}
	
	/**
	 * Remove the player from the chat
	 *
	 * @require p != null
	 * @param p Player
	 */
	public void removePlayerFromChatController(PlayerController p)
	{
		this.LobbyChatController.removePlayerFromChat(p);
	}

	/**
	 * Set the current PlayerControllers
	 * 
	 * @require playerControllers != null
	 * @param playerControllers the player controller collection
	 */
	public void setPlayerControllers(CopyOnWriteArrayList<PlayerController> playerControllers) {
		PlayerControllers = playerControllers;
	}

	/**
	 * Get all the PlayerControllers
	 * 
	 * @ensure result != null
	 * @return the current PlayerControllers collection
	 */
	public Collection<PlayerController> getPlayerControllers() {
		return PlayerControllers;
	}
	
	/**
	 * Remove player from PlayerControllers
	 * 
	 * @require p != null
	 * @ensure getPlayerControllers().contains(p) == false
	 * @param p player
	 */
	public void removePlayerController(PlayerController p)
	{
		this.PlayerControllers.remove(p);
	}
	
	/**
	 * Add the player to the PlayerControllers
	 * 
	 * @require p != null
	 * @ensure getPlayerControllers().contains(p) == true
	 * @param p player
	 */
	public void addPlayerControllers(PlayerController p)
	{
		if(!this.getPlayerControllers().contains(p))
			this.PlayerControllers.add(p);
	}

	/**
	 * Set the challenge controller for the lobby
	 * 
	 * @require challengeController != null
	 * @param challengeController the controller which handles the challenges
	 */
	public void setChallengeController(ChallengeController challengeController) {
		this.challengeController = challengeController;
	}

	/**
	 * Get the challenge controller for the lobby
	 * 
	 * @ensure result != null
	 * @return the challenge controller
	 */
	public ChallengeController getChallengeController() {
		return challengeController;
	}
	
}
