/**
 * 
 */
package controller;

import io.FileInput;

import java.util.Calendar;
import java.util.List;

import model.QueueNode;
import model.Trajectory;
import ui.InputUI;


/**
 * @author Gurkan Solmaz 
 *    
 *    Department of Electrical Engineering and Computer Science
 *    University of Central Florida
 *
 */
public class InputCtrl {

	/* 
	 * 
	 * Inputs from the GUI: 
	 * 			- Mobile Sink Decision Type (adaptive, weighted or random)
	 * 			- Number of mobile sinks
	 * 	 		- Number of specific queues for events
	 * 			- Max number of concurrent events
	 * 			- Event Handling Strategy (shortest path or. closest sink or random)
	 * 			- Mobility Model
	 * 			- Event happening type
	 * 			- Visualizer enabled or disabled
	 * 			- Key input enabled (?)
	 * 			- Number of experiments
	 * 
	 *
	 *  Inputs from the files:
	 * 			- Trajectory file
	 * 			- SLAW points
	 * 			- Queues file
	 *   
	 *  Inputs hardcoded inside this InputCtrl class:
	 *  	    - Dimension Length of the Theme Park area
	 *  		- Total simulation time
	 *  		- Sampling Time
	 *  		- Regular mobile sink speed (without any constraint)
	 *  		- Graph density (edge density)
	 *  		- Degree of the directed graph
	 *  		- Edge creation type of the directed graph (random or closest possible attraction)
	 *  		- Mobile sink location update time (e.g. every 1 hour)
	 *  		- Event happening time (roughly) (e.g. 1 in 3600 seconds)
	 *  		- Event priority
	 *  		- Event min timeout
	 *  		- Event max timeout
	 *  		- Adaptive weight change rate
	 *  		
	 * 
	 */
	private int numberOfUserInputs;
	private List<QueueNode> queueList;
	private List<Trajectory> trajectoryList;

	// input parameters 
	private String mobileSinkDecisionType;
	private int numberOfMobileSinks;
	private int numberOfSpecificQueues;
	private int maxNumberOfConcurrentEvents;
	private String eventHandlingStrategy;
	private String mobilityModel;
	private String eventType;
	private int numberOfExperiments;
	
	private boolean isVisualizerEnabled;
	private boolean isKeyboardInputEnabled;
	
	
	// hard coded input parameters
	private double dimensionLength;
	private double simulationTime;
	private double samplingTime;
	private double mobileSinkRegularSpeed;
	private double mobileSinkLocationUpdateTime;
	private double graphDensity;
	private int graphDegree; 
	private String edgeCreationType;
	private double eventHappeningTime;
	private int eventMaxPriority;
	private double eventMinTimeout;
	private double eventMaxtimeout;
	private double adaptiveWeightChangeRate;
	
	
	public InputCtrl() {
		numberOfUserInputs=9;
		mainInputCtrl();

	}

	private void mainInputCtrl() {
		setHardCodedInputs();
		setUserInputs();
		setFileInputs();
		return;
		
	}

	private void setHardCodedInputs() {
		dimensionLength = 1000;
		simulationTime= 36000; // 10 hours
		samplingTime = 10; // 10 seconds
		mobileSinkRegularSpeed = 5.58; //12.5 mph = 5.58 meters/second
		graphDensity = 0.7; 
		graphDegree = 4;
		edgeCreationType= "Closest Attraction"; // or "Closest Attraction"
		mobileSinkLocationUpdateTime = 1800; // update in 10 minutes
		eventHappeningTime = 120; // one event occurs in 2 minutes
		eventMaxPriority = 5; // from 1 to 5
		eventMinTimeout = 60; // 1 minute
	    eventMaxtimeout = 300; // 5 minutes
	    adaptiveWeightChangeRate= 0.1; // lambda constant used in changing the weights of queues by past events
		
	}

	private void setFileInputs() {
		FileInput fileInput = new FileInput(mobilityModel,simulationTime, samplingTime ); //passing mobility model argument
		trajectoryList = fileInput.readTrajectoryFile();
		queueList = fileInput.readQueueFile();		
		return;
	}



	private void setUserInputs() {
		
		InputUI inputUI = new InputUI(numberOfUserInputs);
		String[] userInputArray = null;
		
		// Wait for the button to be pressed
		waitButtonPress(inputUI);
		
		
		userInputArray = inputUI.getInputArray();
		// read files according to the input[3] (SLAW or Theme Park) 
		

	//	System.out.println("Simulation Configurations:");
		
		displayUserInputs(userInputArray);
				
		setUserInputsUsingArray(userInputArray);
		return;

	}
	
	


	private void displayUserInputs(String[] userInputArray) {
		for (int i=0;i<8;i++){
			if(!(i==2 || i==6 || i==7)){
				System.out.print(userInputArray[i] + ",");
			}
		}
		
	}

	private void setUserInputsUsingArray(String[] userInputArray) {
		// set the user inputs one by one
		
		mobileSinkDecisionType = userInputArray[0];
		numberOfMobileSinks = Integer.parseInt(userInputArray[1]);
		numberOfSpecificQueues = Integer.parseInt(userInputArray[2]);
		eventHandlingStrategy= userInputArray[3];
		mobilityModel = userInputArray[4];
		eventType = userInputArray[5];
		
		if(userInputArray[6].equalsIgnoreCase("Enabled")){
			isVisualizerEnabled= true;
		} else{
			isVisualizerEnabled= false;
		}
		if(userInputArray[7].equalsIgnoreCase("Enabled")){
			isKeyboardInputEnabled= true;
		} else{
			isKeyboardInputEnabled= false;
		}
		numberOfExperiments =Integer.parseInt(userInputArray[8]);

		return;
	}

	private void waitButtonPress(InputUI inputUI) {
		Calendar c =  Calendar.getInstance();
		int counter= 0;
		while(inputUI.buttonPressed != true){
				Calendar tmpC = Calendar.getInstance();
				if((c.getTimeInMillis() - tmpC.getTimeInMillis())%2000000000 == 0){
					counter++;
				}
				if(counter>10000){
					System.out.println("Waiting . . .");
					counter= 0;
				}
		
		}
		return;
	}


	public String getMobileSinkDecisionType() {
		return mobileSinkDecisionType;
	}



	public int getNumberOfMobileSinks() {
		return numberOfMobileSinks;
	}



	public int getMaxNumberOfConcurrentEvents() {
		return maxNumberOfConcurrentEvents;
	}



	public String getEventHandlingStrategy() {
		return eventHandlingStrategy;
	}



	public String getMobilityModel() {
		return mobilityModel;
	}



	public String getEventType() {
		return eventType;
	}



	public int getEventMaxPriority() {
		return eventMaxPriority;
	}

	public double getEventMinTimeout() {
		return eventMinTimeout;
	}

	public double getEventMaxtimeout() {
		return eventMaxtimeout;
	}

	public boolean isVisualizerEnabled() {
		return isVisualizerEnabled;
	}



	public boolean isKeyboardInputEnabled() {
		return isKeyboardInputEnabled;
	}



	public int getNumberOfUserInputs() {
		return numberOfUserInputs;
	}



	public int getNumberOfExperiments() {
		return numberOfExperiments;
	}

	public void setNumberOfExperiments(int numberOfExperiments) {
		this.numberOfExperiments = numberOfExperiments;
	}

	public List<QueueNode> getQueueList() {
		return queueList;
	}


	public List<Trajectory> getTrajectoryList() {
		return trajectoryList;
	}

	public double getDimensionLength() {
		return dimensionLength;
	}

	public double getSimulationTime() {
		return simulationTime;
	}

	public double getSamplingTime() {
		return samplingTime;
	}

	public double getMobileSinkRegularSpeed() {
		return mobileSinkRegularSpeed;
	}

	public double getGraphDensity() {
		return graphDensity;
	}

	public int getGraphDegree() {
		return graphDegree;
	}

	public String getEdgeCreationType() {
		return edgeCreationType;
	}

	public double getMobileSinkLocationUpdateTime() {
		return mobileSinkLocationUpdateTime;
	}

	public double getEventHappeningTime() {
		return eventHappeningTime;
	}

	public double getAdaptiveWeightChangeRate() {
		return adaptiveWeightChangeRate;
	}

	public int getNumberOfSpecificQueues() {
		return numberOfSpecificQueues;
	}

	public void setNumberOfSpecificQueues(int numberOfSpecificQueues) {
		this.numberOfSpecificQueues = numberOfSpecificQueues;
	}

	public void setNumberOfMobileSinks(int numberOfMobileSinks) {
		this.numberOfMobileSinks = numberOfMobileSinks;
	}

	
}





