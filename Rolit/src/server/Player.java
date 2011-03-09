package server;
import Protocol.*;

public class Player {

	public Player()
	{
		
	}
	
	public Player(ProtocolRolit p)
	{
		ServerAppDelegate SAD = ServerAppDelegate.sharedApplication();
		SAD.game(this, 3);
		
		
		DataObject d = ProtocolRolit(new BufferedReader(New ConnectionInputReader(socket.in)));
		
		
		
		
	}
	
}
