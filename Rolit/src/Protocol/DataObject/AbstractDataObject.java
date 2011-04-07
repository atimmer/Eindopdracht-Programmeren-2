package Protocol.DataObject;

import Protocol.DataObjectHandler;

/**
 * This is a abstract representation of protocol messages
 * 
 * @author Marcel Boersma & Anton Timmermans
 *
 */
public class AbstractDataObject{
	
	/**
	 * The is the object who sets the data in the AbstractDataObject
	 */
	public DataObjectHandler From = null;
	
	/**
	 * This is the string identifier to which class the object needs to go
	 */
	public String To	= null;
	
	/**
	 * This is the data which is send to the class, this can be anything.
	 * It is necessary that the sender and the receiver knows what kind of data is in the object.
	 */
	public Object What = null;
	
	/**
	 * This is the method to which the data needs to be send or the method which sends the data.
	 * 
	 */
	public String FromMethod = null;
}
