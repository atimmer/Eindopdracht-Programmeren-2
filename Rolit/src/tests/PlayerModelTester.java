package tests;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import Protocol.ProtocolINF2Rolit;

import server.Game.Board.Mark;
import server.Player.PlayerModel;


public class PlayerModelTester {

	PlayerModel model;
	
	/**
	 * Run method before each test
	 */
	@Before public void setUp()
	{
		this.model = new PlayerModel();
	}
	
	/**
	 * Test if the initial values are as expected 
	 */
	@Test public void initialPlayerTest()
	{
		//player name empty
		Assert.assertTrue(this.model.getName().equals(""));
		
		//socket null
		Assert.assertTrue(this.model.getSocket() == null);
		
		//protocol null
		Assert.assertTrue(this.model.getProtocol() == null);
		
		//player not in game
		Assert.assertTrue(this.model.isPlayerIsInGame() == false);
		
		//mark is empty
		Assert.assertTrue(this.model.getMark().equals(Mark.EMPTY));
		
		//empty input reader
		Assert.assertTrue(this.model.getInput() == null);
		
		//empty output
		Assert.assertTrue(this.model.getOutput() == null);
		
	}
	
	/**
	 * Test if the set function work correctly
	 */
	@Test public void setFunctionTest()
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
		ProtocolINF2Rolit p = new ProtocolINF2Rolit();
		
		this.model.setInput(br);
		this.model.setMark(Mark.BLUE);
		this.model.setOutput(bw);
		this.model.setPlayerIsInGame(true);
		this.model.setProtocol(p);
		
		//check if it's our input stream
		Assert.assertTrue(this.model.getInput().equals(br));
		
		//test if it's our output stream
		Assert.assertTrue(this.model.getOutput().equals(bw));
		
		//test if it's our mark
		Assert.assertTrue(this.model.getMark().equals(Mark.BLUE));
		
		//test if the player is in game
		Assert.assertTrue(this.model.isPlayerIsInGame() == true);
		
		//test if it's our protocol
		Assert.assertTrue(this.model.getProtocol().equals(p));
		
		
		
		
		
	}
	
}
