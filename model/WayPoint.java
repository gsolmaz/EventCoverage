package model;
/**
 * @author Gurkan Solmaz 
 *    
 *    Department of Electrical Engineering and Computer Science
 *    University of Central Florida
 *
 */
public class WayPoint {
	// a waypoint of a visitor
	
	private double x;
	private double y;
	private int visitorIndex;

	public WayPoint(double x, double y, int visitorIndex) {
		super();
		this.x = x;
		this.y = y;
		this.visitorIndex = visitorIndex;
	}

	
	public WayPoint(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}


	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public int getVisitorIndex() {
		return visitorIndex;
	}

}
