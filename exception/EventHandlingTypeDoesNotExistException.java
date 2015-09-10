package exception;

public class EventHandlingTypeDoesNotExistException  extends Exception {

	/**
	 * @author Gurkan Solmaz 
	 *    
	 *    Department of Electrical Engineering and Computer Science
	 *    University of Central Florida
	 *
	 */
	private static final long serialVersionUID = -3090855778904044203L;

	public EventHandlingTypeDoesNotExistException() {
		super("Error: Event handling type does not exist!");
	}

}
