package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.Event;
import model.MobileSink;
import model.QueueNode;
import exception.MobileSinkDecisionTypeDoesNotExistException;

/**
 * @author Gurkan Solmaz 
 *    
 *    Department of Electrical Engineering and Computer Science
 *    University of Central Florida
 *
 */
public class MobileSinkCtrl {
	private String mobileSinkDecisionType;
	private List<MobileSink> mobileSinkList;
	
	
	public MobileSinkCtrl(String mobileSinkDecisionType,
			List<MobileSink> mobileSinkList) {
		super();
		this.mobileSinkDecisionType = mobileSinkDecisionType;
		this.mobileSinkList = mobileSinkList;
	}	
	
	// use adaptive decider to decide if some of the mobile sinks need to move
	// or not, after happening of an event or a location update 
	// Three Types for placement of mobile sinks: Adaptive, Weighed or Random 
	public void locateMobileSinksAdaptively(List<QueueNode> queueNodeList) {
		System.out.println("Location Updates");

		// change the locations according to the current adaptive weights of the queues and the directed graph
		List<Integer> occupiedIndexList = new ArrayList<Integer>();		
		Random r = new Random();
		for(int i=0;i<mobileSinkList.size();i++){			
			int tmpBestQueueIndexAvailable=-1;
			double tmpBestAdaptiveWeight=-10000;
			
			// move from the old queue to the new queue, do the necessary and add the queue to occupiedIndexList
			for(int j=0; j<queueNodeList.size();j++){	
				// check if it is occupied or if is there a way or not
				if(occupiedIndexList.contains(j) 	/*|| 
					  (directedGraph.getGraphMatrix()[mobileSinkList.get(i).getCurrentQueueIndex()][j]==0 
				   && mobileSinkList.get(i).getCurrentQueueIndex() != j ) */){
					continue;
				}
				
				if(tmpBestAdaptiveWeight < queueNodeList.get(j).getAdaptiveWeight()){
					tmpBestQueueIndexAvailable = j;
					tmpBestAdaptiveWeight = queueNodeList.get(j).getAdaptiveWeight();
				}
			}
			int sinkQueueIndex = tmpBestQueueIndexAvailable;
			
			if(sinkQueueIndex==-1){
				System.out.println("Could not find a queue to locate");
			}
			
			if(sinkQueueIndex == mobileSinkList.get(i).getCurrentQueueIndex()|| sinkQueueIndex ==-1){ // already in the best queue
				occupiedIndexList.add(sinkQueueIndex);
				System.out.println("Mobile sink" + i + " stayed in the same location: " + sinkQueueIndex);
				continue;
			}
			// create randomness for the exact location of the event
			double randomDistanceToCenterPointX = r.nextDouble() * 100 - 50; // between -50 to +50 
			double randomDistanceToCenterPointY = r.nextDouble() * 100 - 50; 

			double sinkLocationX, sinkLocationY;
			// create event near to the selected queue
			sinkLocationX = queueNodeList.get(sinkQueueIndex).getCenterX() + randomDistanceToCenterPointX;
			sinkLocationY = queueNodeList.get(sinkQueueIndex).getCenterY() + randomDistanceToCenterPointY;

			mobileSinkList.get(i).setX(sinkLocationX); 
			mobileSinkList.get(i).setY(sinkLocationY);
		
			mobileSinkList.get(i).setCurrentQueueIndex(sinkQueueIndex);
			occupiedIndexList.add(sinkQueueIndex);
			System.out.println("Mobile sink" + i + " new location: " + sinkQueueIndex);
		}
		
	
	}
	
	// function to decide and set the locations of mobile sinks according to current weights (initial positions)
	public void locateMobileSinksInitially(List<QueueNode> queueNodeList){
		try{// 3 types of posititioning
			if(mobileSinkDecisionType.equalsIgnoreCase("Adaptive")){
				locateMobileSinksByCapacitiesOfQueues(queueNodeList);
			}	
			else if(mobileSinkDecisionType.equalsIgnoreCase("Weighted")){
				locateMobileSinksByCapacitiesOfQueues(queueNodeList);
			}
			else if(mobileSinkDecisionType.equalsIgnoreCase("Random")){
				locateMobileSinksRandomly(queueNodeList);
			}
			else{
				throw new MobileSinkDecisionTypeDoesNotExistException();
			}
			
			}
			catch(MobileSinkDecisionTypeDoesNotExistException e){
				e.printStackTrace();
			}
	}


	private void locateMobileSinksRandomly(List<QueueNode> queueNodeList) {
		Random r = new Random();
		for(int i=0;i<mobileSinkList.size();i++){
			//locate the current sink randomly according to capacities of queues
			int sinkQueueIndex=-1;
			
			// weighted according to the queue capacities
			int randomValue = r.nextInt(queueNodeList.size());
			sinkQueueIndex = randomValue;
			
			// create randomness for the exact location of the event
			double randomDistanceToCenterPointX = r.nextDouble() * 100 - 50; // between -50 to +50 
			double randomDistanceToCenterPointY = r.nextDouble() * 100 - 50; 

			double sinkLocationX, sinkLocationY;
			// create event near to the selected queue
			sinkLocationX = queueNodeList.get(sinkQueueIndex).getCenterX() + randomDistanceToCenterPointX;
			sinkLocationY = queueNodeList.get(sinkQueueIndex).getCenterY() + randomDistanceToCenterPointY;

			mobileSinkList.get(i).setX(sinkLocationX); 
			mobileSinkList.get(i).setY(sinkLocationY);
			mobileSinkList.get(i).setCurrentQueueIndex(sinkQueueIndex);
		}
	}

	private void locateMobileSinksByCapacitiesOfQueues(
			List<QueueNode> queueNodeList) {
		Random r = new Random();
		for(int i=0;i<mobileSinkList.size();i++){
			//locate the current sink randomly according to capacities of queues
			int sinkQueueIndex=-1;
			int totalCapacityOfQueues=0;
			for(int k=0;k<queueNodeList.size();k++){
				totalCapacityOfQueues += queueNodeList.get(k).getCapacity();
			}
			
			// weighted according to the queue capacities
			int randomValue = r.nextInt(totalCapacityOfQueues);
			for(int j=0;j<queueNodeList.size();j++){
				if(queueNodeList.get(j).getCapacity()>randomValue){
					sinkQueueIndex = j;
					break;
				}
				else{
					randomValue -= queueNodeList.get(j).getCapacity(); 
				}
			}	
			
			// create randomness for the exact location of the event
			double randomDistanceToCenterPointX = r.nextDouble() * 100 - 50; // between -50 to +50 
			double randomDistanceToCenterPointY = r.nextDouble() * 100 - 50; 

			double sinkLocationX, sinkLocationY;
			// create event near to the selected queue
			sinkLocationX = queueNodeList.get(sinkQueueIndex).getCenterX() + randomDistanceToCenterPointX;
			sinkLocationY = queueNodeList.get(sinkQueueIndex).getCenterY() + randomDistanceToCenterPointY;

			mobileSinkList.get(i).setX(sinkLocationX); 
			mobileSinkList.get(i).setY(sinkLocationY);
			mobileSinkList.get(i).setCurrentQueueIndex(sinkQueueIndex);

		}
		
	}

	public List<MobileSink> getMobileSinkList() {
		return mobileSinkList;
	}

	public int getIndexOfClosestSinkToEvent(Event e) {
		int closestSinkIndex = -1;
		double closestDistance = -1;
		// get the mobile sink that is closest to the event by distance
		for(int i=0;i<mobileSinkList.size();i++){
			MobileSink ms= mobileSinkList.get(i);
			double tmpDistance =getDistanceBetweenNodePair( ms.getX() , ms.getY() , e.getEventLocationX() , e.getEventLocationY());
			if(closestDistance== -1 || tmpDistance<closestDistance){
				closestDistance = tmpDistance;
				closestSinkIndex = i;
			}
		}
		return closestSinkIndex;
	}


	
	public double getDistanceBetweenNodePair(double x1, double y1, double x2, double y2){
		
		return Math.sqrt((x1-x2)*(x1-x2) + (y1-y2) * (y1-y2));
	}
		
	
}
