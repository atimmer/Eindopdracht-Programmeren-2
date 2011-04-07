package Protocol;

import Protocol.DataObject.AbstractDataObject;

/**
 * Needs to be implemented by all protocols to handle AbstractDataObjects
 * 
 * @author Marcel Boersma & Anton Timmermans
 *
 */
public interface Protocol {
	
	/**
	 * This function reads the commands and translates it to a AbstractDataObject
	 * which can be read by every object which implements DataObjectHandler
	 * 
	 * @return AbstractDataObject
	 */
	public AbstractDataObject read(String obj);
	
	/**
	 * This function translates a AbstractDataObject into protocol defined commands
	 * and returns a string with all the commands the protocol understands.
	 * 
	 * @return String protocol message
	 */
	public String write(AbstractDataObject obj);

}
