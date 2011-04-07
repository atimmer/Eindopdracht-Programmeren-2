package tests;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Protocol.ProtocolINF2Rolit;

import server.Lobby.LobbyController;
import server.Lobby.LobbyModel;
import server.Player.PlayerController;


public class LobbyModelTest {

	private LobbyModel lobbymodel;
	
	private ServerSocket s;
	private Socket s1, s2;
	
	PlayerController player1, player2;
	
	/**
	 * Set the initial situation for the lobby model testing
	 */
	@Before public void setUp()
	{
		LobbyController lc = new LobbyController();
		this.lobbymodel = new LobbyModel(lc);
		
		this.setupServer();
	}
	
	/**
	 * Setup a 2 players with a server connection
	 */
	private void setupServer()
	{

		s = null;
		
		try {
			s = new ServerSocket(1337);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(s != null){
		
			s1 = null;
			s2 = null;
			
			try {
				s1 = new Socket(s.getInetAddress(), s.getLocalPort());
				s2 = new Socket(s.getInetAddress(), s.getLocalPort());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if(s1 != null && s2 != null){
				player1 = new PlayerController(s1, new ProtocolINF2Rolit());
				player2 = new PlayerController(s2, new ProtocolINF2Rolit());
			
			}
		
			
		}
	}
	
	
	/**
	 * Test if the play
	 */
	@Test public void initializeLobbyTest()
	{
		//test if the challenge controller isn't null
		Assert.assertTrue(this.lobbymodel.getChallengeController() != null);
		
		//test if the chat controller isn't null
		Assert.assertTrue(this.lobbymodel.getLobbyChatController() != null);
		
		//test if player1 isn't in game
		Assert.assertTrue(!this.lobbymodel.getPlayerControllers().contains(this.player1));
		
		//test if player 2 isn't in game
		Assert.assertTrue(!this.lobbymodel.getPlayerControllers().contains(this.player2));		
		
	}
	
	@Test public void newPlayersTest()
	{
		this.lobbymodel.addPlayerControllers(player1);
		this.lobbymodel.addPlayerControllers(player2);
		
		//test if player1 isn in game
		Assert.assertTrue(this.lobbymodel.getPlayerControllers().contains(this.player1));
		
		//test if player 2 is in game
		Assert.assertTrue(this.lobbymodel.getPlayerControllers().contains(this.player2));		
	
		//remove player 1
		this.lobbymodel.removePlayerController(player1);
		
		//test if player 1 isn't in the game anymore
		Assert.assertTrue(!this.lobbymodel.getPlayerControllers().contains(this.player1));
		
	}
	
	
	
	@After public void closeSocket()
	{
		try {
			s1.close();
			s2.close();
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
