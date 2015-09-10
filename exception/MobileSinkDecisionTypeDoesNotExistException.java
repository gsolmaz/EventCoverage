package exception;

public class MobileSinkDecisionTypeDoesNotExistException extends Exception {
	/**
	 * @author Gurkan Solmaz 
	 *    
	 *    Department of Electrical Engineering and Computer Science
	 *    University of Central Florida
	 *
	 */




	private static final long serialVersionUID = 6190067057589668366L;



	public MobileSinkDecisionTypeDoesNotExistException() {
		super("Error: Mobile sink decision type does not exist!");
	}

}