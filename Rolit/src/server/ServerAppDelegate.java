package server;
import java.util.Collection;

import Protocol.*;

/**
 * 
 */

/**
 * @author marcelboersma
 *
 */
public class ServerAppDelegate {
	
	private static ServerAppDelegate SADInstance = null;
	
	private ProtocolRolit serverProtocol = null;
	private Collection<Player> players = null;
	private Collection<Game> games = null;

	/**
	 * This is the main function of the server application
	 * 
	 * You can start a server by the following commands <b>server<b> <i>port</i> <i>optional: protocol (default is protocol...)</i> 
	 * The 
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		//TODO create ServerApplicationInstance
		ServerAppDelegate SAD = ServerAppDelegate.sharedApplication();
		
		//TODO Setup protocol (depends on argument)
		
		//TODO Create a serverSocket with port
		
	
		

	}
	
	public static ServerAppDelegate sharedApplication()
	{
		if(SADInstance == null ){
			SADInstance = new ServerAppDelegate();
		}
		
		return SADInstance;
		
	}
	
	public boolean game(Player p, int players){
		//TODO add player to game
		
		//TODO if game isn't there, create one
		
		return true;
	}
	
	/**
	 * A new connection is requested, so setup the connection
	 */
	private void newClientConnection(){
		//TODO create player object
		Player newPlayer = new Player(this.serverProtocol);
		
		//TODO pass protocol to player object (always the ServerAppDelegate protocol, unless the special change protocol command is invoked
		
		//TODO add player to player list
	}

}
