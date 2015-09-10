/**
 * 
 */
package controller;

import java.util.List;

import model.DirectedGraph;
import model.MobileSink;
import model.QueueNode;
import processor.EdgeCreator;

/**
 * @author Gurkan Solmaz 
 *    
 *    Department of Electrical Engineering and Computer Science
 *    University of Central Florida
 *
 */
public class GraphCtrl {
	
	private DirectedGraph directedGraph;
	private List<QueueNode> queueNodeList;
	private List<MobileSink> initialMobileSinkList;
	private DirectedGraph distanceGraph;
//	private List<Event> currentEventList; - not necessary, event is not a node for sink to travel
	private int maxNumberOfConcurrentEvents;
	private double graphDensity;
	private int graphDegree;
	private String edgeCreationType;
	private int numberOfVertices; 
	private double adaptiveWeightChangeRate;
	
	public GraphCtrl(
			List<QueueNode> queueNodeList, List<MobileSink> mobileSinkList, int maxNumberOfConcurrentEvents,
			double graphDensity , int graphDegree, String edgeCreationType, double adaptiveWeightChangeRate) {
		this.queueNodeList = queueNodeList;
		this.initialMobileSinkList = mobileSinkList;
		this.maxNumberOfConcurrentEvents = 1;
		this.graphDensity = graphDensity;
		this.graphDegree = graphDegree;
		this.edgeCreationType = edgeCreationType;
		this.numberOfVertices = queueNodeList.size() + mobileSinkList.size() + this.maxNumberOfConcurrentEvents; // max number of concurrent events
		this.adaptiveWeightChangeRate = adaptiveWeightChangeRate;
	}
	
	
	public void createInitialGraph() {
		// create edge between the attraction nodes
		EdgeCreator edgeCreator = new EdgeCreator(graphDegree, edgeCreationType, graphDensity);
		double[][] initialEdgesMatrix = edgeCreator.createInitialEdges(queueNodeList);
			
		double[][] initialGraphMatrix = createInitialGraphMatrix(initialEdgesMatrix);
		
		//  create the initial directed graph
		// add the mobile sinks without their initial locations (not placed anywhere yet)
		directedGraph = new DirectedGraph(queueNodeList.size(), initialMobileSinkList.size(),maxNumberOfConcurrentEvents, initialGraphMatrix);	
	}

		
	
	private double[][] createInitialGraphMatrix(double[][] initialEdgesMatrix) {
		double[][] initialGraphMatrix = new double[numberOfVertices][numberOfVertices];
		for(int i=0;i<queueNodeList.size();i++){
			for(int j=0; j<queueNodeList.size();j++){
				initialGraphMatrix[i][j] = initialEdgesMatrix[i][j];
			}
		}
		return initialGraphMatrix;
	}


	public List<MobileSink> getInitialMobileSinkList() {
		return initialMobileSinkList;
	}



	public void setInitialAdaptiveWeightsOfQueues() {
		double totalCapacityOfQueues=0;
		for(int k=0;k<queueNodeList.size();k++){
			totalCapacityOfQueues += queueNodeList.get(k).getCapacity();
		}
		for(int i=0;i<queueNodeList.size();i++){
			double capacity = queueNodeList.get(i).getCapacity();
			queueNodeList.get(i).setAdaptiveWeight(capacity/totalCapacityOfQueues);
		}
	}

	public void updateAdaptiveWeightsOfQueuesByEvent(int eventQueueIndex){
		// ADAPTIVE UPDATE formula		
		for(int i=0;i<queueNodeList.size();i++){
			double currentAdaptiveWeight = queueNodeList.get(i).getAdaptiveWeight();
	//		System.out.println("Currrent Weight for queue " + i+  ": " + currentAdaptiveWeight);
			
			if(i==eventQueueIndex){ // increase the weight
				currentAdaptiveWeight += adaptiveWeightChangeRate;
			}
			else{ // decrease the weight
				currentAdaptiveWeight -= adaptiveWeightChangeRate/(queueNodeList.size() -1);
			}
	//		System.out.println("Adapted Weight for queue " + i+  ": " + currentAdaptiveWeight);
			queueNodeList.get(i).setAdaptiveWeight(currentAdaptiveWeight);
		}
			
	}
	
	
	public List<QueueNode> getQueueNodeList() {
		return queueNodeList;
	}


	public void setQueueNodeList(List<QueueNode> queueNodeList) {
		this.queueNodeList = queueNodeList;
	}


	public DirectedGraph getDirectedGraph() {
		return directedGraph;
	}


	public void setDirectedGraph(DirectedGraph directedGraph) {
		this.directedGraph = directedGraph;
	}


	public void setDistanceGraph(DirectedGraph distanceGraph) {
		this.distanceGraph = distanceGraph;
	}


	public DirectedGraph getDistanceGraph() {
		return distanceGraph;
	}


	
}
