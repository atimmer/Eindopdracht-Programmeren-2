package server.Challenge;

import java.util.ArrayList;

import java.util.Collection;
import java.util.Iterator;

import Protocol.DataObjectHandler;
import Protocol.DataObject.AbstractDataObject;

import server.Lobby.*;
import server.Player.*;
import server.*;

/**
 * This class is responsible for keeping track of all the challenges between players.
 * 
 * @author Marcel Boersma & Anton Timmermans
 *
 */
public class ChallengeController implements DataObjectHandler {

	private Collection<DataObjectHandler> objectReaders = null;
	private ArrayList<Challenge> challenges = null;
	private LobbyController lobbyController = null;
	
	/**
	 * Challenge controller
	 * 
	 * @require lc != null
	 * @param lc LobbyController
	 */
	public ChallengeController(LobbyController lc)
	{
		this.lobbyController = lc;
		this.objectReaders = new ArrayList<DataObjectHandler>();
		this.challenges    = new ArrayList<Challenge>();
	}
	
	/**
	 * This function removes open challenges for a player.
	 * 
	 * @require p != null
	 * @param p PlayerController
	 */
	private void cleanChallenges(PlayerController p)
	{
		Iterator<Challenge> i = this.challenges.iterator();
		
		while(i.hasNext())
		{
			Challenge c = i.next();
			//player has already a challenge, clean the challenge before creating a new one
			if(c.getChallenger().equals(p))
			{
				i.remove();
			}
		}
		
	}
	
	/**
	 * This function creates a new challenge for the player (challenger) who
	 * challenges the player (Challenged).
	 * 
	 * @require challenger != null && challenge != null
	 * 
	 * @param challenger the player who challenged the other
	 * @param challenged the player who is challenged
	 */
	private void createNewChallenge(PlayerController challenger, PlayerController challenged)
	{
			//remove all outstanding challenges
			this.cleanChallenges(challenger);
			this.challenges.add(new Challenge(challenger, challenged));
			
			AbstractDataObject ADO = new AbstractDataObject();
			ADO.To = "Player";
			ADO.From = this;
			ADO.What = challenger;
			
			challenged.exec(ADO);
			
			ServerGUI.printMessage("Oeee... " + challenger.getPlayerName() + " challenged " + challenged.getPlayerName() + " let's wait for a response");
			
	}
	
	/**
	 * This function is invoked when a player accepts a challenge,
	 * here it will search the challenges where the player (p) is the
	 * challenged player in a challenge and it will start the challenge.
	 * 
	 * 
	 * @param p PlayerController
	 */
	private void challengedAccepted(PlayerController p)
	{
		Iterator<Challenge> i = this.challenges.iterator();
		
		while(i.hasNext())
		{
			Challenge c = i.next();
			
			if(c.getChallenger().equals(p)){
				//your challenge is found, so start the game and notify all players
				ServerController SAD = ServerController.sharedApplication();
				
				ServerGUI.printMessage("Yeah, " + p.getPlayerName() + " accepted the challenge, let's rock and roll!");
				
				SAD.startChallenge(c);
				
			}
		}
	}
	
	/**
	 * This function handles a declined challenge, it removes the challenge and notifies the other player.
	 * @param p the player controller
	 */
	private void challengedDeclined(PlayerController p)
	{
		Iterator<Challenge> i = this.challenges.iterator();
		
		while(i.hasNext())
		{
			Challenge c = i.next();
			
			if(c.getChallenger().equals(p)){
				
				ServerGUI.printMessage("Woops, " + p.getPlayerName() + " declined the challenge, to bad better luck next time.");
				i.remove();
			}
		}
	}
	
	

	@Override
	public void exec(AbstractDataObject obj) {
		if(obj.To.equals(this.getIdentifier()))
		{
			if(obj.FromMethod.equals("challenge")){
				
				//create a new challenge
				PlayerController challenged = this.lobbyController.getPlayerControllerObjectForName(obj.What.toString());
				
				
				
				if(challenged != null)	
					this.createNewChallenge((PlayerController) obj.From, challenged );
				
			}else if(obj.FromMethod.equals("challengeresponse")){
				AbstractDataObject ADO = (AbstractDataObject) obj.What;
								
				if( ADO.What.equals("true"))
				{	
					//challenge accepted
					PlayerController challenger = this.lobbyController.getPlayerControllerObjectForName(ADO.To);
					if(challenger != null)
						this.challengedAccepted(challenger);
				}else{
					//challenge accepted
					PlayerController challenger = this.lobbyController.getPlayerControllerObjectForName(ADO.To);
					if(challenger != null)
						this.challengedDeclined(challenger);
				}
				
				
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
		this.objectReaders.add(obj);
	}

	
	@Override
	public String getIdentifier() {
		return "ChallengeController";
	}

}
