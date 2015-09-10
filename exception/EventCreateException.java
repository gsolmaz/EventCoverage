package exception;

public class EventCreateException extends Exception {

	/**
	 * @author Gurkan Solmaz 
	 *    
	 *    Department of Electrical Engineering and Computer Science
	 *    University of Central Florida
	 *
	 */

	private static final long serialVersionUID = -4243565542156729321L;



	public EventCreateException() {
		super("Error: Could not create event properly.");
	}

}
