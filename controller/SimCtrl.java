package controller;

import java.util.List;

import model.DirectedGraph;
import model.Event;
import model.Trajectory;
import processor.DirectedGraphUpdater;
import processor.ShortestPathFinder;
import visualizer.SimVisualizer;


/**
 * @author Gurkan Solmaz 
 *    
 *    Department of Electrical Engineering and Computer Science
 *    University of Central Florida
 *
 */
public class SimCtrl {

	// controllers
	private GraphCtrl graphCtrl; // includes mobile sinks, queue node list
	private List<Event> eventList;
	private MobileSinkCtrl mobileSinkCtrl;
	private OutputCtrl outputCtrl;
	private SimVisualizer simVisualizer;
	
	// processors
	private DirectedGraphUpdater dgUpdater;
	private ShortestPathFinder shortestPathFinder;

	
	private List<Trajectory> trajectoryList;

	// simulation parameters
	private double samplingTime;
	private String mobileSinkDecisionType;
	private boolean isVisualizerOn;
	private boolean isKeyboardOn;
	
	// for visualizer
	private Trajectory visualTrajectory;


	
	public SimCtrl(GraphCtrl graphCtrl, EventCtrl eventCtrl, String eventHandlingStrategy,  double sampTime,
			String mobileSinkDecisionType, double adaptiveWeightChangeRate, List<Trajectory> trajectList, boolean isVisualizerEnabled,
			boolean isKeyboardEnabled) {
	
		this.graphCtrl = graphCtrl;
		this.samplingTime = sampTime;
		this.eventList = eventCtrl.getEventList();	
		this.shortestPathFinder = new ShortestPathFinder(eventHandlingStrategy);
		this.mobileSinkCtrl = new MobileSinkCtrl(mobileSinkDecisionType, graphCtrl.getInitialMobileSinkList());
		this.trajectoryList = trajectList;
		this.mobileSinkDecisionType = mobileSinkDecisionType;
		if(isVisualizerEnabled){
			this.simVisualizer = new SimVisualizer();
		}
		this.isVisualizerOn = isVisualizerEnabled;
		this.isKeyboardOn = isKeyboardEnabled;
		adaptiveInitialConfiguration();
		initialize();
	}

	private void initialize() {
		 dgUpdater = new DirectedGraphUpdater(graphCtrl.getDirectedGraph(), graphCtrl.getQueueNodeList() );
		 outputCtrl = new OutputCtrl();
	}

	private void adaptiveInitialConfiguration() {
		graphCtrl.setInitialAdaptiveWeightsOfQueues();
		mobileSinkCtrl.locateMobileSinksInitially(graphCtrl.getQueueNodeList());	
		graphCtrl.setDistanceGraph(graphCtrl.getDirectedGraph());


	}

	// Discrete Event Simulation 
	public void simulate(){
		
		for(int i=0;i<eventList.size();i++){
			Event e = eventList.get(i);
			
			if(e.getType().equalsIgnoreCase("Location Update Event")){
					// update the locations of the mobile sinks (MobileSinkCtrl)
					if(mobileSinkDecisionType.equalsIgnoreCase("Adaptive")){
						mobileSinkCtrl.locateMobileSinksAdaptively(graphCtrl.getQueueNodeList());
					}
				
				// visualize if it is enabled
				/*if(isVisualizerOn){			
					simVisualizer.clearArea();
					while(true){
						simVisualizer.draw(mobileSinkCtrl.getMobileSinkList(),visualTrajectory, graphCtrl.getQueueNodeList(),e);
						if(!isKeyboardOn|| simVisualizer.case1){
							break;
						}
						System.out.println("Waiting");
					}
				}*/
				
				
			}
			else if(e.getType().equalsIgnoreCase("Occuring Event")){
			
							
				// update the directed graph: find the current weights of all ways, create the new edges for mobile sink and the event

				updateDirectedGraphByDirectedGraphUpdater(e); // dynamic graph
				updateDistanceGraphByDirectedGraphUpdater(e); // distance graph
				
				//handle the event (Path Finder)
				// use dijkstra`s shortest path or find the time of closest sink or find a time of a random one
				double eventHandlingTime = shortestPathFinder.handleEvent(graphCtrl.getDirectedGraph(), graphCtrl.getDistanceGraph(), graphCtrl.getQueueNodeList().size(),
						mobileSinkCtrl.getIndexOfClosestSinkToEvent(e));
				if(eventHandlingTime == 10000){
					outputCtrl.addNumberOfMisses(1);
					continue;
				}
				
				// change the adaptive weights according to the event (do not change the locations of adaptive sinks)
				if(mobileSinkDecisionType.equalsIgnoreCase("Adaptive")){
					graphCtrl.updateAdaptiveWeightsOfQueuesByEvent(e.getEventQueueIndex());
				}
				
				// store the necessary information about this event ( hit or miss, time of handling the event,
				outputCtrl.addEventHandlingTime(eventHandlingTime);
				if(e.getTimeout() < eventHandlingTime){
					// could not handle the event in time
					outputCtrl.addNumberOfMisses(1);
				}
				else{ // was able to handle the event in time
					outputCtrl.addNumberOfHits(1);
				}
				// total distance traveled-for energy purposes)
				outputCtrl.addTravelDistance(shortestPathFinder.getLastTravelDistance());
				// DATA stored
				
				
			/*	// visualize if it is enabled
				if(isVisualizerOn){		
					simVisualizer.clearArea();
					while(true){
						simVisualizer.draw(mobileSinkCtrl.getMobileSinkList(),visualTrajectory, graphCtrl.getQueueNodeList(),e);

						if(!isKeyboardOn|| simVisualizer.case1){
							break;
						}
				//		System.out.println("Waiting");
					}
				} */
			}		
		}
		// do the necessary computations for generating results
		outputCtrl.computeResults();	
		
}

	private void updateDistanceGraphByDirectedGraphUpdater(Event e) {
		DirectedGraph updatedDistanceGraph = dgUpdater.updateDistanceGraph(graphCtrl.getQueueNodeList(), graphCtrl.getDistanceGraph(),  mobileSinkCtrl.getMobileSinkList(), e);
		graphCtrl.setDistanceGraph(updatedDistanceGraph);
		
	}

	private void updateDirectedGraphByDirectedGraphUpdater(Event e) {
		
		int trajectoryIndex =(int) (( e.getStartTime() - (e.getStartTime() % samplingTime) ) / samplingTime);
		
		// select the trajectory right before the event happened
		if(trajectoryIndex == 0){ return;}
		Trajectory firstTrajectory  = trajectoryList.get(trajectoryIndex-1);
		visualTrajectory= firstTrajectory;
	
		// select the trajectory when event is happening
		Trajectory secondTrajectory  = trajectoryList.get(trajectoryIndex);
	
		DirectedGraph updatedDirectedGraph = dgUpdater.updateDirectedGraph(graphCtrl.getQueueNodeList(), graphCtrl.getDirectedGraph(), e, mobileSinkCtrl.getMobileSinkList(), firstTrajectory, secondTrajectory);
		graphCtrl.setDirectedGraph(updatedDirectedGraph);
	}

	public OutputCtrl getOutputCtrl() {
		return outputCtrl;
	}


}
