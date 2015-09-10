package controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import model.Event;
import model.QueueNode;
import exception.EventCreateException;
import exception.EventTypeDoesNotExistException;

/**
 * @author Gurkan Solmaz 
 *    
 *    Department of Electrical Engineering and Computer Science
 *    University of Central Florida
 *
 */
public class EventCtrl {
	private String eventType; // event location type
	//private int maxNumberOfConcurrentEvents; // !!!! forgot to use this, you need to use this while generating random values !!!! 
	private double eventHappeningTime;
	private int eventMaxPriority;
	private double eventMinTimeout;
	private double eventMaxtimeout;
	private int numberOfSpecificQueues;
	
	private List<Event> eventList;
		
	private double totalSimulationTime;
	private double mobileSinksLocationUpdateTime;
	
	
	private List<QueueNode> queueNodeList;

	
	public EventCtrl(String eventType, 
			/*int maxNumberOfConcurrentEvents,*/ double eventHappeningTime, int eventMaxPriority,
			double eventMinTimeout, double eventMaxTimeout, double totalSimTime, double mobSinkUpdTime, 
			List<QueueNode> queueNodeList, int numOfSpecificQueues) {
		super();
		this.eventType = eventType;
//		this.maxNumberOfConcurrentEvents = maxNumberOfConcurrentEvents;
		this.eventHappeningTime = eventHappeningTime;
		this.eventMaxPriority = eventMaxPriority;
		this.eventMinTimeout = eventMinTimeout;
		this.eventMaxtimeout = eventMaxTimeout;
		this.totalSimulationTime = totalSimTime;
		this.mobileSinksLocationUpdateTime =  mobSinkUpdTime;
		this.queueNodeList = queueNodeList;
		this.numberOfSpecificQueues = numOfSpecificQueues;
		
		createEvents();
	}
	
	private void createEvents() {
		List<Event> tmpOccuringEventList = createOccuringEvents();
		List<Event> tmpUpdateEventList= createLocationUpdateEvents();
		eventList = sortCreatedEventsByTime(tmpOccuringEventList, tmpUpdateEventList); 
		
		
	}

	private List<Event> sortCreatedEventsByTime(List<Event> tmpOccuringEventList, List<Event> tmpUpdateEventList) {
			
			List<Event> returnList = new ArrayList<Event>();

			int numberOfEvents = tmpOccuringEventList.size() + tmpUpdateEventList.size();
			int counter =0, i=0, j=0;
			while(counter<numberOfEvents){
				if(j==tmpUpdateEventList.size() || 
				 (i!= tmpOccuringEventList.size() && 
				 tmpOccuringEventList.get(i).getStartTime() < tmpUpdateEventList.get(j).getStartTime())){
					// add occuring event to the list
					returnList.add(tmpOccuringEventList.get(i));
					i++;	
				}
				else{
					// add location update event
					returnList.add(tmpUpdateEventList.get(j));
					j++;
				}
				counter++;
			}
			return returnList;
	}

	private List<Event> createLocationUpdateEvents() {
		List<Event> tmpList = new ArrayList<Event>();
		
		int numberOfLocationUpdates = (int) Math.floor(totalSimulationTime/mobileSinksLocationUpdateTime) +1 ;
		for(int i=0;i<numberOfLocationUpdates;i++){
			String type = "Location Update Event";
			double startTime = i * mobileSinksLocationUpdateTime;
			Event tmpEvent = new Event(type, -1, startTime, -1, -1, -1, -1);			
			tmpList.add(tmpEvent);		
		}
		return tmpList;
	}

	public double[] createSortedEventTimes(){
		
		// events are uniformly distributed in the total simulation time
		
		double numberOfEvents = totalSimulationTime / eventHappeningTime;
		double[] eventTimes = new double[(int)numberOfEvents];
		
		Random r = new Random();
		for(int i=0; i<numberOfEvents; i++){
			eventTimes[i] = r.nextInt((int)totalSimulationTime);
		}
		
		// sort the array
		Arrays.sort(eventTimes);
		
		return eventTimes;
	}
	
	// create the events according to the types 
	// 3 types: Random, Biased (Weighted), Specific (e.g. always from the same queue) 
	private List<Event> createOccuringEvents() {
		
		List<Event> tmpEventList = new ArrayList<Event>();

		try{
			
			Random r = new Random();
			
			double[] eventTimeList = createSortedEventTimes();

			Event tmpEvent;

			int specificQueueIndex[]= new int[numberOfSpecificQueues];
			int totalCapacityOfQueues=0;
			if (eventType.equalsIgnoreCase("Specific")){
				for(int t=0; t<numberOfSpecificQueues; t++){
					specificQueueIndex[t]= r.nextInt(queueNodeList.size());
				}

			}
			else if (eventType.equalsIgnoreCase("Biased")){
				for(int k=0;k<queueNodeList.size();k++){
					totalCapacityOfQueues += queueNodeList.get(k).getCapacity();
				}
			}

			
			
			for(int i=0; i<eventTimeList.length; i++){
				int priority = r.nextInt(eventMaxPriority)+1;  // priority from 1 to 5
				double eventTimeout =  r.nextDouble() * (eventMaxtimeout - eventMinTimeout) + eventMinTimeout;
				double eventStartTime = eventTimeList[i];
				String type = "Occuring Event";
				double eventLocationX ;
				double eventLocationY;
				
				// create randomness for the exact location of the event
				double randomDistanceToCenterPointX = r.nextDouble() * 100 - 50; // between -50 to +50 
				double randomDistanceToCenterPointY = r.nextDouble() * 100 - 50; 
			

				
				int eventQueueIndex=-1;
				// find location of the event according to the event types
				if(eventType.equalsIgnoreCase("Random")){
					eventQueueIndex=  r.nextInt(queueNodeList.size());
				}
				else if(eventType.equalsIgnoreCase("Specific")){
					// select a queue randomly
					eventQueueIndex=  specificQueueIndex[r.nextInt(numberOfSpecificQueues)];				
				}
				else if(eventType.equalsIgnoreCase("Biased")){
					// weighted according to the queue capacities
					int randomValue = r.nextInt(totalCapacityOfQueues);
					for(int j=0;j<queueNodeList.size();j++){
						if(queueNodeList.get(j).getCapacity()>randomValue){
							eventQueueIndex = j;
							break;
						}
						else{
							randomValue -= queueNodeList.get(j).getCapacity(); 
						}
					}	
				}
				else{
					throw new EventTypeDoesNotExistException();
				}
				
				// create event near to the selected queue
				eventLocationX = queueNodeList.get(eventQueueIndex).getCenterX() + randomDistanceToCenterPointX;
				eventLocationY = queueNodeList.get(eventQueueIndex).getCenterY() + randomDistanceToCenterPointY;
				if(eventQueueIndex == -1){
					throw new EventCreateException();
				}
				// create the event
				tmpEvent = new Event(type, priority, eventStartTime, eventTimeout, eventLocationX, eventLocationY, eventQueueIndex);
				tmpEventList.add(tmpEvent);
			}

		}
		catch (EventTypeDoesNotExistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error: Unexpected error while using event types!");
		}
		catch(EventCreateException e){
			e.printStackTrace();
			System.out.println("Error: Could not create event properly!");

		}
		return tmpEventList;
	}
	
	public Event getEventByIndex(int index){
		return eventList.get(index);
	}

	public List<Event> getEventList() {
		return eventList;
	}

	public void setEventList(List<Event> eventList) {
		this.eventList = eventList;
	}
	
		
	
}
