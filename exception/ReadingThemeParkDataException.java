package exception;


public class ReadingThemeParkDataException extends Exception {
	/**
	 * @author Gurkan Solmaz 
	 *    
	 *    Department of Electrical Engineering and Computer Science
	 *    University of Central Florida
	 *
	 */
	private static final long serialVersionUID = 8803563067273147724L;

	public ReadingThemeParkDataException() {
		super("Error: Unexpected input while reading Theme Park mobility data file.");
	}
	
}
