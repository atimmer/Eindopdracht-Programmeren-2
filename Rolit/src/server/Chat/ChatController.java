package server.Chat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import Protocol.DataObjectHandler;
import Protocol.DataObject.AbstractDataObject;

import server.*;
import server.Player.*;

/**
 * The class is the chat controller, it handles everything with the chat.
 * 
 * @author Marcel Boersma & Anton Timmermans
 *
 */
public class ChatController implements DataObjectHandler{

	private Collection<DataObjectHandler> objectReaders = null;
	private ChatModel model;
	
	/**
	 * Get the ChatController instance
	 */
	public ChatController()
	{
		this.model = new ChatModel();
		this.objectReaders = new ArrayList<DataObjectHandler>();

	}
	
	/**
	 * Adds a player to the chat, so it can receive message from other players in the chat.
	 * 
	 * @requires p != null
	 * @param p PlayerController
	 */
	public void addPlayerToChat(PlayerController p)
	{
		this.model.addPlayer(p);

	}
	
	/**
	 * Removes a player from the chat, so it doesn't receive any message from this chat.
	 * 
	 * @require p != null
	 * @param p PlayerController
	 */
	public void removePlayerFromChat(PlayerController p)
	{
		this.model.removePlayer(p);
	}
	
	/**
	 * Broadcast a message to all players in this chat.
	 * 
	 * @require message != null
	 * @param message
	 */
	private void broadcastMessage(String message)
	{
		Iterator<PlayerController> p = this.model.getPlayers().iterator();
		
		
		while(p.hasNext())
		{
			PlayerController tmp = p.next();
			
			AbstractDataObject ADO = new AbstractDataObject();
			ADO.From = this;
			ADO.To	 = "Player";
			ADO.What =  message;
			
			tmp.exec(ADO);
			
		}
	}
	
	/**
	 * Return a boolean if the Player is in this chat.
	 * 
	 * @require pl != null
	 * @param pl PlayerController
	 * @return result = true | false
	 */
	private boolean isPlayerInChat(PlayerController pl)
	{
		boolean result = false;
		Iterator<PlayerController> p = this.model.getPlayers().iterator();
		
		while(p.hasNext())
		{
			if(p.next().getPlayerName().equals(pl.getPlayerName()))
			{
				result = true;
			}
		}
		
		return result;
	}
	
	
	@Override
	public void exec(AbstractDataObject obj) {

		if(obj.To.equals(this.getIdentifier()))
		{
			
			if(this.isPlayerInChat((PlayerController) obj.From)){
				ServerGUI.printMessage(((PlayerController) obj.From).getPlayerName()+" blaats : " + obj.What.toString());
				this.broadcastMessage(((PlayerController) obj.From).getPlayerName() + " "+ obj.What.toString());
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
		return "Chat";
	}

	
	
}
