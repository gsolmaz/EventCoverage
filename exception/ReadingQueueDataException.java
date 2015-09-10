package exception;

public class ReadingQueueDataException extends Exception {

	/**
	 * @author Gurkan Solmaz 
	 *    
	 *    Department of Electrical Engineering and Computer Science
	 *    University of Central Florida
	 *
	 */
	private static final long serialVersionUID = 1384258095425527457L;

	/**
	 * 
	 */

	public ReadingQueueDataException() {
		super("Error: Unexpected input while reading Queue data file.");
	}

}
