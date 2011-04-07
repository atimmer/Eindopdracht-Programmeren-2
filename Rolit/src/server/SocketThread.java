package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

import server.Game.GameController;
import server.Player.PlayerController;

/**
 * This is the socket thread, continuously listening for incoming messages from all the socket connections.
 * 
 * @author Marcel Boersma & Anton Timmermans
 *
 */
public class SocketThread implements Runnable {

	private CopyOnWriteArrayList<PlayerController> players = null;
	
	/**
	 * Get the SocketThread instance
	 */
	public SocketThread()
	{
		this.players = new CopyOnWriteArrayList<PlayerController>();
	}
	
	
	/**
	 * This function checks if a player name is unique
	 * 
	 * @require name != ""
	 * @param name player name
	 * @return boolean true if the name is unique otherwise false
	 */
	public boolean uniquePlayerName(String name)
	{
		boolean unique = true;
		Iterator<PlayerController> i = this.players.iterator();
		
		while(i.hasNext())
		{
			if(i.next().getPlayerName().equals(name))
				unique = false;
		}
		
		return unique;
	}
	
	/**
	 * This is the run function in the thread which will check for incoming commands on the socket connection.
	 * 
	 */
	@Override
	public void run() {
			while(true)
			{
				
					
 				Iterator<PlayerController> i = players.iterator();

				while (i.hasNext())
				{		

						PlayerController p = i.next();
										
					
						BufferedReader reader = null;
						reader = p.getInput();
								
						String line = null;
						
						
						
							
						try {
							if(reader.ready())
							{
								line = reader.readLine();
								p.messageFromServer(line);
							}
							
							
						} catch (IOException e) {
							System.out.println("Error while reading nothing!");
							e.printStackTrace();
						}
					
						
						

				}
				
				
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {

				}
				
			}
			
			
	}

	/**
	 * Adds a player to the socket thread
	 * 
	 * @require p != null
	 * @param p PlayerController
	 */
	public void add(PlayerController p)
	{

		this.players.add(p);
		
	}
	
	/**
	 * Removes the player from the socket thread
	 * 
	 * @require p != null
	 * @param p PlayerController
	 */ 
	public void remove(PlayerController p)
	{
		
		//remove player from thread
		this.players.remove(p);
		p.terminate();//terminate player
		
		//try to remove player from lobby
		ServerController.sharedApplication().lobbyChat.removePlayerControllerFromLobby(p);
		
		//try to remove from games
		CopyOnWriteArrayList<GameController> games = ServerController.sharedApplication().getGames();
		
		Iterator<GameController> g = games.iterator();

		while(g.hasNext())
		{
			GameController game = g.next();
			
			Collection<PlayerController> players = game.getPlayers();
			
			Iterator<PlayerController> playerIt = players.iterator();
			
			while(playerIt.hasNext())
			{
				if(playerIt.next().equals(p)){
					//found the game where the player is playing, delete it!
					game.kickPlayer(p);
				}
				
			}
			
			
		}
		
		
	}
	
	/**
	 * Terminate all the player objects
	 */
	public void terminate()
	{
		Iterator<PlayerController> p = this.players.iterator();
		
		while(p.hasNext())
		{
			PlayerController tmp = p.next();
			this.remove(tmp);
		}
	}
	
}
