package server;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

import Protocol.*;
import Protocol.DataObject.AbstractDataObject;

public class Player implements Observer, DataObjectHandler{
	
	private Object parent 	= null;
	private Socket sock 	= null;
	private Protocol prot 	= null;

	
	public Player(Socket s, Protocol p, Object pa)
	{
		this.parent = pa;
		this.sock 	= s;
		this.prot	= p;
	
		//TODO create new thread for the player to listen to the socket
		ServerController.sharedApplication().addToSocketThread(this.sock);
		
		
	}
	
	public void setParent(Object p)
	{
		if(p != null)
			this.parent = p;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exec(AbstractDataObject obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addProtocolReader(Object obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getIdentifier() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
