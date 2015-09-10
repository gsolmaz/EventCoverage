package exception;

public class NullWayPointException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2895071107110292473L;

	/**
	 * @author Gurkan Solmaz 
	 *    
	 *    Department of Electrical Engineering and Computer Science
	 *    University of Central Florida
	 *
	 */

	public NullWayPointException() {
		super("Error: WayPoint error while trying to find the direction of a visitor!");
	}

}