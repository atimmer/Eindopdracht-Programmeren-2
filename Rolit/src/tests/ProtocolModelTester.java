package tests;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import Protocol.ProtocolINF2Rolit;
import Protocol.DataObject.AbstractDataObject;


public class ProtocolModelTester {

	
	private ProtocolINF2Rolit prot = null;
	
	@Before public void setUp()
	{
		this.prot = new ProtocolINF2Rolit();
	}
	
	@Test public void getDataObject()
	{
		
		/************ Test hoofdprotocol ************/
		
		//Test with everything all right
		AbstractDataObject ADO = this.prot.read("connect Anton");
		Assert.assertTrue(ADO.To.equals("Player"));
		Assert.assertTrue(ADO.What.equals("Anton"));
		Assert.assertTrue(ADO.FromMethod == null);
		Assert.assertTrue(ADO.From == null);
		
		ADO = this.prot.read("domove 53");
		Assert.assertTrue(ADO.To.equals("Board"));
		Assert.assertTrue(ADO.What.equals(53));
		Assert.assertTrue(ADO.FromMethod == null);
		Assert.assertTrue(ADO.From == null);
		
		ADO = this.prot.read("join 3");
		Assert.assertTrue(ADO.To.equals("ServerController"));
		Assert.assertTrue(ADO.What.equals(3));
		Assert.assertTrue(ADO.FromMethod == null);
		Assert.assertTrue(ADO.From == null);
		
		//test very wrong stuff
		ADO = this.prot.read("join a");	
		Assert.assertTrue(ADO.To.equals("classNotFound"));
		Assert.assertTrue(ADO.What.equals("classNotFound"));
		Assert.assertTrue(ADO.FromMethod == null);
		Assert.assertTrue(ADO.From == null);
		
		ADO = this.prot.read("domove a");
		Assert.assertTrue(ADO.To.equals("classNotFound"));
		Assert.assertTrue(ADO.What.equals("classNotFound"));
		Assert.assertTrue(ADO.FromMethod == null);
		Assert.assertTrue(ADO.From == null);	
		
		/************** Test chat protocol ****************/
		ADO = this.prot.read("chat a chat message");
		Assert.assertTrue(ADO.To.equals("Chat"));
		Assert.assertTrue(ADO.What.equals("a chat message "));
		Assert.assertTrue(ADO.FromMethod == null);
		Assert.assertTrue(ADO.From == null);	
		
		
		/************** Test challenge protocol ***********/
		ADO = this.prot.read("challenge Anton");
		Assert.assertTrue(ADO.To.equals("ChallengeController"));
		Assert.assertTrue(ADO.What.equals("Anton"));
		Assert.assertTrue(ADO.FromMethod.equals("challenge"));
		Assert.assertTrue(ADO.From == null);	
		
		ADO = this.prot.read("challengeresponse Anton true");
		Assert.assertTrue(ADO.To.equals("ChallengeController"));
		
		AbstractDataObject ADO2 = (AbstractDataObject) ADO.What;
		
		Assert.assertTrue(ADO2.To.equals("Anton"));
		Assert.assertTrue(ADO2.What.equals("true"));
		
		Assert.assertTrue(ADO.FromMethod.equals("challengeresponse"));
		Assert.assertTrue(ADO.From == null);	
		
		ADO = this.prot.read("challengeresponse Anton false");
		Assert.assertTrue(ADO.To.equals("ChallengeController"));
		
		ADO2 = (AbstractDataObject) ADO.What;
		
		Assert.assertTrue(ADO2.To.equals("Anton"));
		Assert.assertTrue(ADO2.What.equals("false"));
		
		Assert.assertTrue(ADO.FromMethod.equals("challengeresponse"));
		Assert.assertTrue(ADO.From == null);	
	}
	

}
