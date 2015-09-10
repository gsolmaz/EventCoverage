package model;


public class QueueNode {


	/**
	 * @author Gurkan Solmaz 
	 *    
	 *    Department of Electrical Engineering and Computer Science
	 *    University of Central Florida
	 *
	 */
	
  private int capacity;
  private int index;
  private double adaptiveWeight;
  
  private double centerX;
  private double centerY; 
  private String queueType;
  
  
  
  public QueueNode(int capacity, int index, double x, double y, String queueType) {
	super();
	this.capacity = capacity;
	this.index = index;
	this.centerX = x;
	this.centerY = y;
	this.queueType = queueType;
  }


	public int getCapacity() {
		return capacity;
	}
	
	
	public int getIndex() {
		return index;
	}
	
	
	public double getCenterX() {
		return centerX;
	}
	
	
	public double getCenterY() {
		return centerY;
	}
	
	
	public String getQueueType() {
		return queueType;
	}


	public double getAdaptiveWeight() {
		return adaptiveWeight;
	}


	public void setAdaptiveWeight(double adaptiveWeight) {
		this.adaptiveWeight = adaptiveWeight;
	}
	
  
}
