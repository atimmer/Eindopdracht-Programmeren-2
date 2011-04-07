package Protocol;
import Protocol.DataObject.*;

/**
 * The interface that needs to be implemented to handle AbstractDataObjects
 * 
 * @author Marcel Boersma & Anton Timmermans
 *
 */
public interface DataObjectHandler {
	/**
	 * This function could be triggered by any one anywhere in the program, it searches in the 
	 * AbstractDataObject for information for this object through the getIdentifierd() String. If it can't find any information it
	 * will pass all the data to the objects registered as DataObjectHandler.
	 * 
	 * 
	 * @require obj != null
	 * @param obj AbstractDataObject
	 */
	public void exec(AbstractDataObject obj);
	
	/**
	 * This function will add the DataObjectHandler to a list in the current object,
	 * the current object will notify all the DataObjectHandlers with the data 
	 * the object received (AbstractDataObject) that wasn't meant for the current
	 * object.
	 * 
	 * @require obj !=null
	 */
	public void addDataObjectReader(DataObjectHandler obj);

	
	/**
	 * Current object identifier
	 * 
	 * When the exec function scans for data it recognizes 
	 * the data through the identifier. 
	 */
	public String getIdentifier();
}
