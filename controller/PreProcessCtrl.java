package controller;

import java.util.ArrayList;
import java.util.List;

import model.MobileSink;
import model.QueueNode;

/**
 * @author Gurkan Solmaz 
 *    
 *    Department of Electrical Engineering and Computer Science
 *    University of Central Florida
 *
 */
public class PreProcessCtrl {
	/*
	 * 
	 * PreProcessCtrl class is responsible for initial creation of directed graph
	 * and initial creation of mobile sinks 
	 * (Mobile sinks are not placed at this time, they will be placed in the area when 
	 * the actual discrete time simulation starts)
	 * 
	 */
	private GraphCtrl graphCtrl;
	private List<QueueNode> queueNodeList;
	private int numberOfMobileSinks;
	private int maxNumberOfConcurrentEvents;
	private double mobileSinkRegularSpeed;
	private double graphDensity;
	private int graphDegree;
	private String edgeCreationType;
	private double adaptiveWeightChangeRate;
		
	public PreProcessCtrl(List<QueueNode> queueList, int numberOfMobileSinks,/* int maxNumOfConcEvents, */
			double mobileSinkRegularSpeed, double graphDensity, int graphDegree, String edgeCreationType, double adaptiveWeightChangeRate) {
		this.queueNodeList = queueList;
		this.numberOfMobileSinks = numberOfMobileSinks;
	//	this.maxNumberOfConcurrentEvents = maxNumOfConcEvents;
		this.mobileSinkRegularSpeed = mobileSinkRegularSpeed;
		this.graphDensity = graphDensity;
		this.graphDegree = graphDegree;
		this.edgeCreationType = edgeCreationType;
		this.adaptiveWeightChangeRate = adaptiveWeightChangeRate;
		createDirectedGraph();
	}


	private void createDirectedGraph() {
		
		List<MobileSink> mobileSinkList = createMobileSinks();
		
		graphCtrl = new GraphCtrl(queueNodeList, mobileSinkList, maxNumberOfConcurrentEvents, graphDensity, graphDegree, edgeCreationType, adaptiveWeightChangeRate);
		graphCtrl.createInitialGraph();

		return;
	}
	
	private List<MobileSink> createMobileSinks() {
		// create initial mobile sink objects
		
		List<MobileSink> mobileSinkList = new ArrayList<MobileSink>();
		for(int i=0; i<numberOfMobileSinks; i++){
			MobileSink m = new MobileSink(mobileSinkRegularSpeed, i);
			mobileSinkList.add(m);
		}
		return mobileSinkList;
	}


	public GraphCtrl getGraphCtrl() {
		return graphCtrl;
	}

	

}
