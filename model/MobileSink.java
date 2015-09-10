/**
 * 
 */
package model;


/**
 * @author Gurkan Solmaz 
 *    
 *    Department of Electrical Engineering and Computer Science
 *    University of Central Florida
 *
 */
public class MobileSink {
	 
	// regular speed is the speed when the movement of the sink is all free
	private double regularSpeed;
  	private int index;

  	// current location of the sink
  	private double x;
  	private double y;
  	private int currentQueueIndex;

	  
	public MobileSink(double mobileSinkRegularSpeed, int index) {
		super();
		this.regularSpeed = mobileSinkRegularSpeed;
		this.index = index;
	}

	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public void setX(double x) {
		this.x = x;
	}
	public void setY(double y) {
		this.y = y;
	}

	public double getRegularSpeed() {
		return regularSpeed;
	}

	public int getIndex() {
		return index;
	}

	public int getCurrentQueueIndex() {
		return currentQueueIndex;
	}

	public void setCurrentQueueIndex(int currentQueueIndex) {
		this.currentQueueIndex = currentQueueIndex;
	}
	  
	 
	  
}
