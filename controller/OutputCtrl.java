package controller;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Gurkan Solmaz 
 *    
 *    Department of Electrical Engineering and Computer Science
 *    University of Central Florida
 *
 */
public class OutputCtrl {
	private List<Double> eventHandlingTimes;
	private int numberOfHits;
	private int numberOfMisses;
	private List<Double> travelDistances;
	
	
	//results 
	private double averageEventHandlingTime;
	private double hitRatio;
	private double totalTravelDistance;
	private double averageTravelDistance;  
	
	public OutputCtrl() {
		super();
		this.eventHandlingTimes = new ArrayList<Double>();
		this.numberOfHits = 0;
		this.numberOfMisses = 0;
		this.travelDistances = new ArrayList<Double>();
	}
	


	public void addNumberOfHits(int n){
		this.numberOfHits += n;
	}
	
	public void addNumberOfMisses(int n){
		this.numberOfMisses += n;
	} 
	public void addEventHandlingTime(double time){
		this.eventHandlingTimes.add(time);
	} 
	public void addTravelDistance(double travelDistance){
		this.travelDistances.add(travelDistance);
	}

	public void computeResults() {
		this.averageEventHandlingTime= findAverageEventHandlingTime();
		if(numberOfMisses ==0){
			this.hitRatio = 1;
		}
		else if(numberOfHits == 0){
			this.hitRatio = 0;
		}
		else{
			this.hitRatio = (double)numberOfHits / (double)(numberOfHits+numberOfMisses);
		}
		this.totalTravelDistance = findTotalTravelDistance();
		this.averageTravelDistance  = findAverageTravelDistance();
	}

	private double findAverageTravelDistance() {
		double totalDistance=0; 
		for(int i=0;i<travelDistances.size();i++){
			totalDistance += travelDistances.get(i);
		}
		return totalDistance/(double)travelDistances.size();
	}
	private double findTotalTravelDistance() {
		double totalDistance=0; 
		for(int i=0;i<travelDistances.size();i++){
			totalDistance += travelDistances.get(i);
		}
		return totalDistance;
	}

	private double findAverageEventHandlingTime() {
		double totalTime=0; 
		for(int i=0;i<eventHandlingTimes.size();i++){
			totalTime += eventHandlingTimes.get(i);
		}
		return totalTime/(double)eventHandlingTimes.size();
	}



	public double getAverageEventHandlingTime() {
		return averageEventHandlingTime;
	}



	public double getHitRatio() {
		return hitRatio;
	}



	public double getTotalTravelDistance() {
		return totalTravelDistance;
	}



	public double getAverageTravelDistance() {
		return averageTravelDistance;
	}



	public void setAverageEventHandlingTime(double averageEventHandlingTime) {
		this.averageEventHandlingTime = averageEventHandlingTime;
	}



	public void setHitRatio(double hitRatio) {
		this.hitRatio = hitRatio;
	}



	public void setTotalTravelDistance(double totalTravelDistance) {
		this.totalTravelDistance = totalTravelDistance;
	}



	public void setAverageTravelDistance(double averageTravelDistance) {
		this.averageTravelDistance = averageTravelDistance;
	} 
	
	
}
