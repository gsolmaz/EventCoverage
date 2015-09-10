package model;


/**
 * @author Gurkan Solmaz 
 *    
 *    Department of Electrical Engineering and Computer Science
 *    University of Central Florida
 *
 */
public class Event {
	
	private int index;
	
	// type may be location update for a mobile sink or an occuring event
	private String type;
	
	// Priority: From 1 to 5,   5 -> the most prior event
	private int priority;
	private int eventQueueIndex;
	
	private double startTime; // 
	private double timeout;		  // 
	
	private double eventLocationX;
	private double eventLocationY;
	public Event(String type, int priority, double startTime,
			double timeout, double eventLocationX, double eventLocationY, int eventQueueIndex) {
		super();
		this.type = type;
		this.priority = priority;
		this.startTime = startTime;
		this.timeout = timeout;
		this.eventLocationX = eventLocationX;
		this.eventLocationY = eventLocationY;
		this.eventQueueIndex = eventQueueIndex;
	}
	public String getType() {
		return type;
	}
	public int getPriority() {
		return priority;
	}

	
	public double getStartTime() {
		return startTime;
	}
	public double getTimeout() {
		return timeout;
	}
	public double getEventLocationX() {
		return eventLocationX;
	}
	public double getEventLocationY() {
		return eventLocationY;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public int getEventQueueIndex() {
		return eventQueueIndex;
	}
	
}
