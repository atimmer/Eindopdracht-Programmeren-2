package tests;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Protocol.ProtocolINF2Rolit;

import server.Game.GameController;
import server.Game.GameModel;
import server.Game.Board.BoardController;
import server.Player.PlayerController;


public class GameModelTester {

	private GameModel model = null;
	
	private PlayerController player1, player2;
	
	ServerSocket s;
	Socket s1, s2;
	
	@Before public void setUp()
	{
		this.model = new GameModel();

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
			
				this.model.addPlayer(player1);
				this.model.addPlayer(player2);
			}
		
			
		}
	}
	
	@Test public void initialGameModelTest()
	{
		//test if the initial model has a chatcontroller
		Assert.assertTrue(this.model.getChatController() != null);
		//test if the initial model doesn't have a board
		Assert.assertTrue(this.model.getBoard() == null);
		
		//Test if the initial capacity is greater than 0
		Assert.assertTrue(this.model.getCapacity() >= 0);
		
		//Test if the initial amount of players in the game is 0
		Assert.assertTrue(this.model.getPlayersInGame() == 0);
		
		Assert.assertTrue(this.model.getPlayers() != null);
	
		//Test if the turns is greater than 0 and smaller than capacity
		Assert.assertTrue(this.model.getTurn() <= 0);
		Assert.assertTrue(this.model.getTurn() <= this.model.getCapacity());
			
	}
	
	@Test public void setGameModelTest()
	{
		BoardController b = new BoardController(new GameController());
		this.model.setBoard(b);
		
		//test if the board is correctly set
		Assert.assertTrue(this.model.getBoard().equals(b));
		
		int oldCapacity = this.model.getCapacity();
		this.model.increaseCapacityBy(2);
		int newCapacity = this.model.getCapacity();
		
		//test if the increase by two worked correctly
		Assert.assertTrue(oldCapacity + 2 == newCapacity);
		
		int oldPlayersInGame = this.model.getPlayersInGame();
		this.model.increasePlayersInGame();
		int newPlayersInGame = this.model.getPlayersInGame();

		//test if the increase by one worked correctly
		Assert.assertTrue((oldPlayersInGame + 1) == newPlayersInGame);
		
		this.model.setCapacity(3);
		Assert.assertTrue(this.model.getCapacity() == 3);
		
		this.model.setTurn(2);
		Assert.assertTrue(this.model.getTurn() == 2);
		
		this.model.setPlayersInGame(2);
		Assert.assertTrue(this.model.getPlayersInGame() == 2);
		
	
		oldCapacity = this.model.getPlayers().size();
		this.model.removePlayer(player1);
		newCapacity = this.model.getPlayers().size();
		
		Assert.assertTrue(oldCapacity - 1 == newCapacity);
		
		oldCapacity = this.model.getPlayers().size();
		this.model.addPlayer(player1);
		newCapacity = this.model.getPlayers().size();
		
	}
	
	/**
	 * Close the socket connection used for testing purpose
	 */
	@After public void closeSockets()
	{
		
		try {
			this.s1.close();
			this.s2.close();
			this.s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
