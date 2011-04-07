package server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

import Protocol.*;
import Protocol.DataObject.AbstractDataObject;
import server.Lobby.LobbyController;
import server.Game.GameController;
import server.Player.PlayerController;
import server.Challenge.*;
 

/**
 * This class is the starts the server, it listens on a defined port for incoming connections and
 * handles the flow of players between the lobby and a game.
 * 
 * @author Marcel Boersma & Anton Timmersmans
 *
 */
public class ServerController implements DataObjectHandler{
	
	/**
	 * This is the static var that contains an instance for the ServerController
	 * 
	 */
	private static ServerController SADInstance = null;
	
	
	/**####################### All the socket vars ########################**/
	
	/**
	 * This is the thread var for all the socket connections
	 */
	public SocketThread sThread = null;
	
	/**
	 *This is the variable that holds the server socket connection on the preferred port
	 */
	private ServerSocket ssock = null;
	
	/**
	 * This is the var with the port number of the server socket connection
	 */
	private int port;
	
	/**
	 * This is the var that contains the protocol 
	 */
	private Protocol serverProtocol = null;
	
	
	/**#################### all the game vars #############################**/
	
	/**
	 * This is the var that contains all the games
	 */
	private CopyOnWriteArrayList<GameController> games = null;
	
	/**
	 * This holds the lobby controller instanc
	 */
	public LobbyController lobbyChat = null;
	
	
	/**################## All the DataObjectHandler vars ##################**/
	
	/**
	 * This object contains all the objects to send DataObjectHandler to
	 */
	private CopyOnWriteArrayList<DataObjectHandler> objectReaders = null;
	
	
	
	
	
	
	
	/**
	 * This is the main function of the server application
	 * 
	 * You can start a server by the following commands <b>ServerController</b> <i>port</i>
	 * 
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		ServerController SAD = ServerController.sharedApplication();
		
		//start listening 
		(new Thread(new SystemCommands())).start();
		
		if(args.length == 1)
		{
			 SAD.setPort(Integer.parseInt(args[0]));
			 SAD.setServerProtocol(new ProtocolINF2Rolit());

			 SAD.start();
		}
		else{
			ServerGUI.printMessage("RTFM, more arguments please....");
			System.exit(0);
		}
		
	
		
	}
	
	/**
	 * Get the ServerController instance for shared usage of main functions
	 * 
	 * @ensure result != null
	 * @return ServerController
	 */
	public static ServerController sharedApplication()
	{
		if(SADInstance == null ){
			SADInstance = new ServerController();
		}
		
		return SADInstance;
		
	}
	
	/**############################### Server part #################################**/
	
	
	/**
	 * Set protocol used by the server
	 * 
	 * @require p != null
	 * @param p Protocol
	 */
	public void setServerProtocol(Protocol p)
	{
		this.serverProtocol = p;
	}
	
	/**
	 * 
	 * This function sets the port on which the server will
	 * listen for incoming connections
	 * 
	 * @require p!=null && 0 < p <= 65536
	 * @param p port number
	 */
	public void setPort(int p)
	{
		this.port = p;
	}
	
	/**
	 * This function creates a server socket that listens on the
	 * preferred port.
	 * For every new incoming connection it will create a newClientConnection();
	 * 
	 */
	public void start()
	{
		
		ServerGUI.printMessage("####################################################");
		ServerGUI.printMessage("#                                                  #");
		ServerGUI.printMessage("#                                                  #");
		ServerGUI.printMessage("#               Rolit Server 1.0                   #");
		ServerGUI.printMessage("#                    by                            #");
		ServerGUI.printMessage("#                Anton & Marcel                    #");
		ServerGUI.printMessage("#                                                  #");
		ServerGUI.printMessage("####################################################");	
		
		ServerGUI.printMessage(" ");

		
		this.objectReaders 	= new CopyOnWriteArrayList<DataObjectHandler>();
		
		//setup arrays
		this.games			= new CopyOnWriteArrayList<GameController>();
		this.lobbyChat		= new LobbyController();
		
		//add lobby chat as listener
		this.addDataObjectReader(this.lobbyChat);
		
		this.sThread		= new SocketThread();
		
		
		//start the thread
		new Thread(this.sThread).start();
		
		ssock = null;
			
		try {
			 ssock = new ServerSocket(this.port);
			
			 ServerGUI.printMessage("Let's wait for incoming connections on port : " + this.port);
			 
		} catch (IOException e) {
			ServerGUI.printMessage("Couldn't listen on port : " + this.port + " try running a different one");
			System.exit(-1);
		}
		
		Socket clientSocket = null;
		
		
		while(true)
		{
			try {
				clientSocket = ssock.accept();
				
				ServerGUI.printMessage("Hi I'm " + clientSocket.getInetAddress() + " and I really want to play Rolit");
				ServerGUI.printMessage("Woohoo, a new client want's to play Rolit, let's help him!");
				
			} catch (IOException e) {
				ServerGUI.printMessage("Client accept failed on port " + this.port + " please try to reboot the server");
				System.exit(-1);
			}
			
			if(clientSocket != null)
				this.newClientConnection(clientSocket);	
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {

			}
		}
		
		
		
		
	}



	
	/**
	 * This function creates a new player object which will be 
	 * added into the socket thread for incoming messages listening.
	 * 
	 * @require client != null
	 * @param client
	 */
	private void newClientConnection(Socket client){
		
		
		PlayerController newPlayer = new PlayerController(client, this.serverProtocol);
		newPlayer.addDataObjectReader(this);
		
		this.lobbyChat.addPlayerControllerToChatController(newPlayer);
		
		this.sThread.add(newPlayer);
		
	}
	
	/**
	 * Get the game collection
	 * 
	 * @ensure result != null
	 * @return Collection with games
	 */
	public CopyOnWriteArrayList<GameController> getGames()
	{
		return this.games;
	}
	
	
	/**
	 * Checks whether a player name is unique
	 * 
	 * This function passes the player name to the socket thread class which holds all
	 * the players and there names. 
	 * 
	 * @ensure result = true | false
	 * @param name name of the player
	 * @return boolean true if unique, false if not unique
	 */
	public boolean uniquePlayerName(String name)
	{
		//socket has all the players
		return this.sThread.uniquePlayerName(name);
	}
	
	/**	 
	 * This function searches for a game with the requested amount of players.
	 * If it finds a game it will return the game, if it doesn't it creates a new game 
	 * where others can connect with.
	 * 
	 * @require 1 < players < 5
	 * @param players game capacity
	 * @return GameController
	 */
	private GameController findGameWithPlayers(int players)
	{
		
		GameController matchingGame = null;
		
		Iterator<GameController> g = this.games.iterator();
		
		while(g.hasNext())
		{
			GameController tmp = (GameController) g.next();
			
			if((tmp.getCapacity() == players) & (tmp.getAvailable() >= 1))
			{
				//there is a game found :) 
				matchingGame = tmp;
			}
			
		}
		
		if(matchingGame == null)
		{
			//no matching game found.... let's create one
			matchingGame = new GameController();
			matchingGame.setPlayers(players);
			
			this.games.add(matchingGame);
			this.addDataObjectReader(matchingGame);
		}
		
		return matchingGame;
		
		
	}
	
	
	
	/**
	 * Ends the game and removes it from the list
	 * 
	 * @require g != null
	 * @param g Game
	 */
	public void endGame(GameController g)
	{
		Collection<PlayerController> p = g.getPlayers();
		
		Iterator<PlayerController> i = p.iterator();
		
		//move players to lobby
		while(i.hasNext())
		{
			PlayerController aPlayer = i.next();
			this.lobbyChat.addPlayerControllerToChatController(aPlayer);
		}
		
		//remove game
		this.games.remove(g);
		this.objectReaders.remove(g);
		
	}
	
	
	/**
	 * This function is invoked to start a challenge, the player will be removed from the lobbyChat
	 * and will be added to a game, which will start directly.
	 * 
	 * @require c != null
	 * @param c Challenge
	 */
	public void startChallenge(Challenge c)
	{
		this.lobbyChat.removePlayerControllerFromLobby(c.getChallenger());
		this.lobbyChat.removePlayerControllerFromLobby(c.getChallenged());
		
		GameController g = new GameController();
		g.setPlayers(2);
		g.addPlayer(c.getChallenger());
		g.addPlayer(c.getChallenged());
		this.addDataObjectReader(g);
		
	}
	
	
	
	
	
	/**################################# DataObjectHandler ##############################**/
	
	
	
	


	@Override
	public void exec(AbstractDataObject obj) {
		if(obj.To.equals(this.getIdentifier()))
		{

		   //let's see if there is a game with ADO.What players
			int players = Integer.parseInt(obj.What.toString());
			GameController game = this.findGameWithPlayers(players);
			
			//let's add the player to the game
			if(obj.From.getIdentifier().equals("Player")){
				if(!((PlayerController) obj.From).isPlayerInGame()){
					ServerGUI.printMessage("Add player " + ((PlayerController) obj.From).getPlayerName() + " to the game");
					this.lobbyChat.removePlayerControllerFromLobby((PlayerController ) obj.From);
					game.addPlayer((PlayerController) obj.From);
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
		return "ServerController";
	}
	
	
	
	/**###################################### Destructor ##############################**/
	
	
	/**
	 * This function terminates the program, first it will terminate the server socket and after that it will disconnect all the clients
	 * 
	 */
	public void terminate()
	{
		//terminate server socket
		try {
			ServerGUI.printMessage("Closing the serversocket on port : " + this.port);
			this.ssock.close();
		} catch (IOException e) {
		
		}
		
		
		//start terminating all sockets
		this.sThread.terminate();
		
		
	}

}
