package server.Player;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import Protocol.DataObjectHandler;
import Protocol.Protocol;
import Protocol.DataObject.AbstractDataObject;

import server.Game.Board.*;
import server.*;

/**
 * The PlayerController manages everything that has to do with the player
 * 
 * @author Marcel Boersma & Anton Timmermans
 *
 */
public class PlayerController implements DataObjectHandler {

	
	private Collection<DataObjectHandler> objectReaders = null;
	private PlayerModel model;
	
	/**
	 * Get a playerController instance
	 * @param s the socket connection of the player
	 * @param p the protocol the player uses.
	 */
	public PlayerController(Socket s, Protocol p)
	{
		this.objectReaders = new ArrayList<DataObjectHandler>();
		this.model = new PlayerModel();
		
		this.model.setSocket(s);
		this.model.setProtocol(p);
		
		try {
			this.model.setInput(new BufferedReader(new InputStreamReader(s.getInputStream())));
			this.model.setOutput(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Get the output stream
	 * @return null or the output stream
	 */
	public BufferedWriter getOutput()
	{
		return this.model.getOutput();
	}
	
	
	/**
	 * Sets the mark for the player
	 * 
	 * @param m Mark
	 */
	public void setMark(Mark m)
	{
		this.model.setMark(m);
	}
	
	/**
	 * Get the players mark
	 * 
	 * @ensure result = Mark.Blue | Mark.RED | Mark.GREEN | Mark.YELLOW | Mark.EMPTY
	 * @return the mark
	 */
	public Mark getMark()
	{
	
		return this.model.getMark();
	}
	
	/**
	 * Will return the state of the player, if in game returns true else false
	 * 
	 * @return true if in game otherwise false
	 */
	public boolean isPlayerInGame()
	{
		return this.model.isPlayerIsInGame();
	}
	
	/**
	 * Sets the player state
	 * 
	 * @param f true if in game, false if not
	 */
	public void setPlayerInGameTo(boolean f)
	{
		this.model.setPlayerIsInGame(f);
	}
	
	/**
	 * Returns the input stream of the player
	 * 
	 * @return the buffered reader or null
	 */
	public BufferedReader getInput()
	{
		
		return this.model.getInput();
	}
	
	
	
	/**
	 * Get player name
	 * 
	 * @ensure result != null
	 * @return player name
	 */
	public String getPlayerName()
	{
		return this.model.getName();
	}
	
	/**
	 * This function is invoked by the socket thread for incoming commands.
	 * The function will create a AbstractDataObject and pass it to the receiver object.
	 * 
	 * @require message != null
	 * @param message
	 */
	public void messageFromServer(String message) {
		
		AbstractDataObject db =	this.model.getProtocol().read(message);
		db.From = (DataObjectHandler)this; //cast as dataObjectHandler for response
		
		
		if(db.To.equals(this.getIdentifier()))
		{
			
			String playerName = db.What.toString();
			
			//ah, got something in it for me, that must be the players name because that's the only thing you can set
			if(playerName != null ){
				String uniquePlayerName = playerName;
				int count = 1;
				while(!ServerController.sharedApplication().uniquePlayerName(uniquePlayerName)){
						uniquePlayerName = playerName + count;
						count++;
				}
				
				this.model.setName(uniquePlayerName);
				
				ServerGUI.printMessage("Hi " + this.model.getName() +" hope you have fun playing Rolit");
				ServerGUI.printMessage("");
				
				//let's reply with the reply answer
				AbstractDataObject conAck = new AbstractDataObject();
				conAck.From 	= this;
				conAck.To		= "Player";
				conAck.What		= this.model.getName();
				
				db.From.exec(conAck);
				
				//so my name is okay, let everybody know i'm there!
				ServerController.sharedApplication().lobbyChat.broadcastLobby();
				
				
				
			}
			
		}else{
			//Don't broadCast anything into the system without knowing for who it is....
			if(!db.To.equals("classNotFound")){
				
				Iterator<DataObjectHandler> i = this.objectReaders.iterator();
	
				if(!this.getPlayerName().equals("")){
					while(i.hasNext())
					{
						i.next().exec(db);
					
					}
			
				}else{
						//you must have a unique name before we're gonna talk to you
						ServerGUI.printMessage("Somebody is talking without introducing himself, who are you?....");
					
				}
			}
		}
		
	}
	
	/**
	 * Terminates the socket
	 */
	public void terminate()
	{
		try {
			System.out.println("Closing socket connection for player : " + this.model.getName());
			this.model.getSocket().close();
		} catch (IOException e) {
			
		}
	}
	
	


	@Override
	public void exec(AbstractDataObject obj) {
		//Player is invoked, so a object sends some data to the form object of AbstractDataObject
		
		//This must mean that I must sends some data to the player because the player requested it
		
		//I don't know how but the protocol does, so lets send it to the protocol and I will send it to the output stream
		try {
						
			this.model.getOutput().write(this.model.getProtocol().write(obj));
			this.model.getOutput().flush();

		} catch (IOException e) {
			ServerController.sharedApplication().sThread.remove(this);
		}
		
	}


	
	@Override
	public String getIdentifier() {
		
		return "Player";
	}

	
	@Override
	public void addDataObjectReader(DataObjectHandler obj) {
		
		this.objectReaders.add(obj);
		
		
	}
	
	
}
