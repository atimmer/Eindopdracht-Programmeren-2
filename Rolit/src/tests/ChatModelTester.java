package tests;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import Protocol.ProtocolINF2Rolit;

import server.Chat.ChatModel;
import server.Player.PlayerController;


public class ChatModelTester {

	private ChatModel model = null;
	ServerSocket s;
	Socket s1;
	private PlayerController player1;
	
	@Before public void setUp()
	{
		this.model = new ChatModel();
		
	}
	
	@Test public void initialChatModelTest()
	{
		Assert.assertTrue(this.model.getPlayers() != null);
		
	}
	
	@Test public void addPlayerTest()
	{
		s = null;
		
		try {
			s = new ServerSocket(1337);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(s != null){
		
			s1 = null;
			
			try {
				s1 = new Socket(s.getInetAddress(), s.getLocalPort());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if(s1 != null){
				player1 = new PlayerController(s1, new ProtocolINF2Rolit());
			
				int oldPlayers = this.model.getPlayers().size();
				this.model.addPlayer(player1);
				int newPlayers = this.model.getPlayers().size();
				
				Assert.assertTrue(oldPlayers + 1 == newPlayers);
			}
		
			
		}
		
		
		
		try {
			this.s1.close();
			this.s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	@Test public void removePlayer()
	{
		s = null;
		
		try {
			s = new ServerSocket(1337);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(s != null){
		
			s1 = null;
			
			try {
				s1 = new Socket(s.getInetAddress(), s.getLocalPort());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if(s1 != null){
				player1 = new PlayerController(s1, new ProtocolINF2Rolit());
			
				this.model.addPlayer(player1);
				int oldPlayers = this.model.getPlayers().size();
				this.model.removePlayer(player1);
				int newPlayers = this.model.getPlayers().size();
				
				Assert.assertTrue(oldPlayers - 1 == newPlayers);
			}
		
			
		}
		
		
		
		try {
			this.s1.close();
			this.s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
}
