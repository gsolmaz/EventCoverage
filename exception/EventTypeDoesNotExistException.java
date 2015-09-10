package exception;

public class EventTypeDoesNotExistException extends Exception {

	/**
	 * @author Gurkan Solmaz 
	 *    
	 *    Department of Electrical Engineering and Computer Science
	 *    University of Central Florida
	 *
	 */

	/**
	 * 
	 */
	private static final long serialVersionUID = 4973874291240503384L;

	/**
	 * 
	 */

	public EventTypeDoesNotExistException() {
		super("Error: Event type does not exist, events cannot be created without type information.");
	}

}
