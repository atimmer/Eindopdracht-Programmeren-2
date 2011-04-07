package Protocol;

import java.util.Collection;
import server.Player.PlayerController;
import java.util.Iterator;

import Protocol.Protocol;
import Protocol.DataObject.AbstractDataObject;

/**
 * This class implements the Protocol class, so it can handle protocol data and will create a AbstractDataObject
 * 
 * @author Marcel Boersma & Anton Timmermans
 *
 */
public class ProtocolINF2Rolit implements Protocol {
	


	@Override
	public AbstractDataObject read(String obj) {
		//This is all data from client to server
		
		
		AbstractDataObject DOB = new AbstractDataObject();
		//default values
		DOB.To		= "classNotFound";
		DOB.What	= "classNotFound";
		
		String[] args = obj.split(" ");
		if(args.length > 0){
			if(args[0].equals("connect"))
			{
				if(args.length == 2){
					DOB.To = "Player";
					if(args[1] != null)
						DOB.What = args[1];
					else
						DOB.What = "NoName";
				}
				
			}else if(args[0].equals("domove"))
			{
				
				try{
					
					if( Integer.parseInt(args[1]) >=0 &&  Integer.parseInt(args[1] ) < 64){
						DOB.What 	= Integer.parseInt(args[1]);
						DOB.To 		= "Board";
					}
					
				}catch(NumberFormatException e)
				{
					
				}

			}
			else if(args[0].equals("join"))
			{
				if(args.length == 2){
					// joint command is invoked, so let's add the player to a game with args[1] players
					
					try{
						
						DOB.What 	= Integer.parseInt(args[1]); 
						DOB.To 		= "ServerController";
					}catch(NumberFormatException e){
						
					}
					
				}
			}else if(args[0].equals("chat"))
			{
				if(args.length >= 2)
				{
					DOB.To 		= "Chat";
					
					//Gluing things together
					String totalMessage = "";
					for(int i = 1; i < args.length; i++)
					{
						totalMessage += args[i] + " ";
					}
					
					DOB.What	= totalMessage;
				}
			}else if(args[0].equals("challenge"))
			{
				if(args.length == 2)
				{
					DOB.To = "ChallengeController";
					DOB.FromMethod = "challenge";
					DOB.What = args[1];
				}
				
			}else if(args[0].equals("challengeresponse"))
			{
				if(args.length == 3)
				{
					DOB.To = "ChallengeController";
					DOB.FromMethod = "challengeresponse";
					
					
					AbstractDataObject DOBSub = new AbstractDataObject();
					DOBSub.To = args[1];
					DOBSub.What = args[2];
					
					DOB.What = DOBSub;
				}
			}
			
		}
		
		
	
		
		return DOB;
	}

	
	@Override
	public String write(AbstractDataObject obj) {
		
		String protocolStringMessage = null;
		
		if(obj.From.getIdentifier().equals("Player"))
		{
			
			protocolStringMessage = "ackconnect " + obj.What.toString() +"\n";
			
		}else if(obj.From.getIdentifier().equals("Server")){
			
		}else if(obj.From.getIdentifier().equals("Game"))
		{
			if(obj.FromMethod.equals("startGame"))
			{
				Collection<PlayerController> playerCollection =  (Collection<PlayerController>) obj.What;
				Iterator<PlayerController> p = playerCollection.iterator();
				
				String allPlayerNames = "";
				
				while(p.hasNext())
				{
					PlayerController tmpP = p.next();
					allPlayerNames = allPlayerNames + " " + tmpP.getPlayerName();
					
				}
				
				protocolStringMessage = "startgame " +allPlayerNames +"\n";
				
				
			}else if(obj.FromMethod.equals("turn"))
			{
				protocolStringMessage = "turn " + obj.What.toString() + "\n";
			}else if(obj.FromMethod.equals("kick"))
			{
				protocolStringMessage = "kick " + obj.What.toString() + "\n";
			}else if(obj.FromMethod.equals("movedone"))
			{
				protocolStringMessage = "movedone " + obj.What.toString() + "\n";
			}else if(obj.FromMethod.equals("endGame"))
			{
				protocolStringMessage = "endgame"+obj.What.toString() + "\n";
			}
			
			
		}else if(obj.From.getIdentifier().equals("Chat"))
		{
			protocolStringMessage = "message " + obj.What.toString() + "\n";
		}else if (obj.From.getIdentifier().equals("Board")){
			protocolStringMessage = obj.What.toString();
		}else if(obj.From.getIdentifier().equals("ChallengeController"))
		{
			protocolStringMessage = "challenged " + ((PlayerController) obj.What).getPlayerName() + "\n";
		}else if(obj.From.getIdentifier().equals("Lobby")){
			protocolStringMessage = "lobby "+obj.What + "\n";
		}
		
		
		return protocolStringMessage;
	}

}
