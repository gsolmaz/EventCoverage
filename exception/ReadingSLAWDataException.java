package exception;

public class ReadingSLAWDataException extends Exception {

	/**
	 * @author Gurkan Solmaz 
	 *    
	 *    Department of Electrical Engineering and Computer Science
	 *    University of Central Florida
	 *
	 */
	private static final long serialVersionUID = -8785303835613604429L;

	public ReadingSLAWDataException() {
		super("Error: Unexpected input while reading SLAW data file.");
	}
	
	
	
}
