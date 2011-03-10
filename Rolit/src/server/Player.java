package server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Observable;
import java.util.Observer;

import Protocol.*;
import Protocol.DataObject.AbstractDataObject;

public class Player implements Observer, DataObjectHandler{
	
	private Object parent 	= null;
	private Socket sock 		= null;
	private Protocol prot 	= null;
	
	private Collection<DataObjectHandler> objectReaders = null;
	public BufferedReader input = null;

	
	public Player(Socket s, Protocol p, Object pa)
	{
		this.objectReaders = new ArrayList<DataObjectHandler>();
		
		this.parent = pa;
		this.sock 	= s;
		this.prot	= p;
		
		try {
			this.input = new BufferedReader(new InputStreamReader(s.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		
	}
	
	public BufferedReader getInput()
	{
		
		return this.input;
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
	public String getIdentifier() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addDataObjectReader(DataObjectHandler obj) {
		
		this.objectReaders.add(obj);
		
		
	}

	public void messageFromServer(String message) {
		// TODO Auto-generated method stub
		System.out.println("Message from client: "+message);
	}
	
	
}
