package tests;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Protocol.ProtocolINF2Rolit;

import server.Challenge.Challenge;
import server.Player.PlayerController;


public class ChallengeModelTester {

	private Challenge challenge;
	private PlayerController player1, player2;
	
	ServerSocket s;
	Socket s1, s2;
	
	/**
	 * Creates the initial situation before each test
	 */
	@Before public void setUp()
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
			
				this.challenge = new Challenge(player1, player2);
			}
		
			
		}
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
	
	/**
	 * Check if the challenger is the challenger and the challenged player is the challenged player
	 */
	@Test public void initializeChallengeTest()
	{
		//check if player 1 is the challenger
		Assert.assertTrue(player1.equals(challenge.getChallenger()));
		//check if player 2 is the challenger
		Assert.assertTrue(player2.equals(challenge.getChallenged()));
	}
	
	
}
