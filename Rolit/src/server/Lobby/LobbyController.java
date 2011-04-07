package server.Lobby;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import Protocol.DataObjectHandler;
import Protocol.DataObject.AbstractDataObject;

import server.ServerGUI;
import server.Player.*;

/**
 * This class is responsible for the lobby, it will handle challenges and the main ChatController.
 * @author Marcel Boersma & Anton Timmermans
 *
 */
public class LobbyController implements DataObjectHandler {

	private Collection<DataObjectHandler> objectReaders = null;
	private LobbyModel model;
	
	/**
	 * Get a lobby controller instance
	 */
	public LobbyController()
	{
		this.model = new LobbyModel(this);
		
		this.objectReaders = new ArrayList<DataObjectHandler>();
		this.addDataObjectReader(this.model.getLobbyChatController());
		this.addDataObjectReader(this.model.getChallengeController());
	}
	
	/**
	 * Add a PlayerController to the lobby and main ChatController
	 * @param p PlayerController
	 */
	public void addPlayerControllerToChatController(PlayerController p)
	{
		ServerGUI.printMessage("Welcome in the lobby "+ p.getPlayerName());
		this.model.addPlayerToChatController(p);
		this.model.addPlayerControllers(p);
		this.broadcastLobby();
	}
	
	/**
	 * Broadcast a message through the lobby, this is used to send the player list 
	 * to all players in the lobby.
	 */
	public void broadcastLobby()
	{
		AbstractDataObject ADO = new AbstractDataObject();
		ADO.To = "PlayerController";
		ADO.From = this;
		
		Iterator<PlayerController> i = this.model.getPlayerControllers().iterator();
		
		String allPlayerControllers = "";
		
		while(i.hasNext())
		{
			PlayerController p = i.next();
			if(!p.getPlayerName().equals(null))
				allPlayerControllers +=p.getPlayerName() + " ";
		}
		
		ADO.What = allPlayerControllers;
		
		Iterator<PlayerController> b = this.model.getPlayerControllers().iterator();
		while(b.hasNext())
		{
		
			b.next().exec(ADO);
		}
		
		
		
	
	}
	
	/**
	 * Here you can get a PlayerController object by giving a PlayerController name.
	 * This is necessary for the challenge functionality
	 * 
	 * @ensure result = null | result = PlayerController
	 * @param name the player name
	 * @return the player controller that matches the game or null
	 */
	public PlayerController getPlayerControllerObjectForName(String name)
	{
		Iterator<PlayerController> i = this.model.getPlayerControllers().iterator();
		PlayerController yourPlayerController = null;
		while(i.hasNext())
		{
			PlayerController tmp = i.next();
			if(tmp.getPlayerName().equals(name))
				yourPlayerController = tmp;
		}
		
		return yourPlayerController;
	}
	
	/**
	 * Here you can remove a PlayerController from the lobby, usually done when
	 * a PlayerController enters a game.
	 * 
	 * @param p PlayerController
	 */
	public void removePlayerControllerFromLobby(PlayerController p)
	{	
		if(this.model.getPlayerControllers().contains(p)){
			this.model.removePlayerFromChatController(p);
			this.model.removePlayerController(p);
			this.broadcastLobby();
		}
	}
	

	@Override
	public void exec(AbstractDataObject obj) {
		if(obj.To.equals(this.getIdentifier()))
		{
			
			
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
		return "Lobby";
	}

}
