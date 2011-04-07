package server.Challenge;

import server.Player.*;

/**
 * This is a challenge between 2 players.
 * @author Marcel Boersma & Anton Timmermans
 *
 */
public class Challenge {

	private PlayerController challenger = null;
	private PlayerController challenged = null;
	
	/**
	 * Creates a challenge between two players
	 * 
	 * @require challenger != null
	 * 			challenged != null
	 * 
	 * @param challenger the player who initiate the challenge
	 * @param challenged the player that is been challenged
	 */
	public Challenge(PlayerController challenger, PlayerController challenged)
	{
		this.challenger = challenger;
		this.challenged = challenged;
	}
	
	/**
	 * Get the player who challenged the other player.
	 * 
	 * @ensure result != null
	 * @return the player who challenged the other player
	 */
	public PlayerController getChallenger()
	{
		return this.challenger;
	}
	
	/**
	 * Get the player who is challenged by the challenger.
	 * 
	 * @ensure result != null
	 * @return the player who is challenged
	 */
	public PlayerController getChallenged()
	{
		return this.challenged;
	}
	
	
}
