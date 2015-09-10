package processor;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.QueueNode;

/**
 * @author Gurkan Solmaz 
 *    
 *    Department of Electrical Engineering and Computer Science
 *    University of Central Florida
 *
 */

public class EdgeCreator {
	
	private List<QueueNode> queueNodeList;
//	private List<MobileSink> mobileSinkNodeList;
//	private List<Event> eventNodeList;
	private int degreeOfGraph; // for graphs created according to closest attractions
	private String edgeType;
	private double graphDensity; // for random graphs

	public EdgeCreator(int degreeOfGraph, String type, double density) {
		super();
		this.degreeOfGraph = degreeOfGraph;
		this.edgeType = type;
		this.graphDensity  = density;
	}
	
	public double[][] createInitialEdges(List<QueueNode> queueList){
		
		this.queueNodeList  = queueList;
	
		double[][] graphMatrix=null;
		if(edgeType.equalsIgnoreCase("Closest Attraction")){
			graphMatrix= createEdgesByClosestAttraction();
		}
		else if(edgeType.equalsIgnoreCase("Random")){
			graphMatrix= createEdgesRandomly();

		}
		
		
		
		return graphMatrix;
	}

	private double[][] createEdgesRandomly() {
		int numberOfVertices = queueNodeList.size();
		double density = graphDensity;
		
		// calculate numberOfEdges by the density formula and getting floor value
		int numberOfEdges = (int) Math.floor(density * numberOfVertices * (numberOfVertices-1) /2 ) ; 
		
		// allocate an empty adjacency matrix for graph of order n ( # of vertices)
		double[][] matrix = new double[numberOfVertices][numberOfVertices];
		
		int edgeCounter=0;
	
		// allocate arrays for storing random numbers below
		int[] randomXNumbers = new int[numberOfEdges];
		int[] randomYNumbers = new int[numberOfEdges];
		
	    while(randomYNumbers[numberOfEdges-1]==0){
	    	
	    	// generate random x and random y values from 0,1,...(numberOfVertices-1)
			Random random = new Random();
	    	int randomX = random.nextInt(numberOfVertices);
	    	int randomY = random.nextInt(numberOfVertices);
	    	
	    	// finding upper left points for matrix is enough (matrix is symmetrical)
	    	if(randomY>randomX){
	    		// check if the selected numbers are already selected before
	    		int flag=0;
	    		for(int i=0; i<edgeCounter; i++){
	    			if(randomXNumbers[i] == randomX && randomYNumbers[i] == randomY){
	    				flag =1; // selected numbers are already selected
	    			}
	    		}
	    		if(flag!=1){ //selected numbers can be used
		    		randomXNumbers[edgeCounter]=randomX;
		    		randomYNumbers[edgeCounter]=randomY;
		    		// edge count increased by 1
		    		edgeCounter++;
	    		}
	    	}
	    }
		
	    // generate adjacency matrix for graph of order n and edge count m
	    // using selected random numbers above
	    for(int i=0; i<numberOfEdges; i++){
	    	QueueNode tmpFirstQueue = queueNodeList.get(randomXNumbers[i]);
	    	QueueNode tmpSecondQueue = queueNodeList.get(randomYNumbers[i]);

	    	double distance = getDistanceBetweenNodePair(tmpFirstQueue.getCenterX(), tmpFirstQueue.getCenterY(),
	    											   tmpSecondQueue.getCenterY(), tmpSecondQueue.getCenterY());
	    	
	    	matrix[randomXNumbers[i]][randomYNumbers[i]]=distance;
			// matrix is symmetrical for x,y values
	    	matrix[randomYNumbers[i]][randomXNumbers[i]]=distance;
	    }
	    
	    
	    return matrix;
		
	}

	private double[][] createEdgesByClosestAttraction() {
		
		// works for symmetric and asymmetric directed graphs
		
		int numberOfVertices = queueNodeList.size();
		int degree = degreeOfGraph;
		
		// calculate numberOfEdges by the degree and number of vertices
		int numberOfEdges = degree * numberOfVertices; 
		
		// allocate an empty adjacency matrix for graph of order n ( # of vertices)
		double[][] matrix = new double[numberOfVertices][numberOfVertices];
		
		// keep the lowest weights in an array 
    	double[] closestDistances = new double[numberOfEdges];
    	int edgeCounter=0;
    	
    	// keep the edges that are found to be the lowest ones in a list
    	List<Point> edgeList = new ArrayList<Point>();
	    while(closestDistances[numberOfEdges-1]==0){
	    	// set the base case
	    	double tmpClosestDistance = 50000; 	    	
	    	Point tmpClosestEdge = new Point();
	    	for(int i=0;i<numberOfVertices; i++){
	    		QueueNode tmpQFrom = queueNodeList.get(i);
	    		for(int j=0;j<numberOfVertices; j++){
	    			if(i==j){ // no self loop
	    				continue;
	    			}
		    		QueueNode tmpQTo = queueNodeList.get(j);
		    		double tmpDist = getDistanceBetweenNodePair(tmpQFrom.getCenterX(), tmpQFrom.getCenterY(), 
							  tmpQTo.getCenterX(), tmpQTo.getCenterY());
	    			if( tmpDist< tmpClosestDistance){
	    				Point tmpEdge = new Point();
	    				tmpEdge.setLocation(i,j);
	    				if(!edgeList.contains(tmpEdge)){
		    				tmpClosestDistance = tmpDist;
		    				tmpClosestEdge.setLocation(i, j);
	    				}
	    			}
	    			
	    		}
	    	}
	    	closestDistances[edgeCounter] = tmpClosestDistance;
	    	edgeCounter++;
	    	edgeList.add(tmpClosestEdge);
	    	
	    }
	    // generate adjacency matrix for graph of order n and edge count m
	    // using selected numbers above
	    for(int i=0; i<numberOfEdges; i++){
	    	// fill the matrix with the already found values, starting from the lowest weight
	    	Point tmpPoint = edgeList.get(i); 
	    	
	    	matrix[tmpPoint.getLocation().x][tmpPoint.getLocation().y]=closestDistances[i];
	    }
	    
	    
	    return matrix;
	}
	
	public double getDistanceBetweenNodePair(double x1, double y1, double x2, double y2){
		
		return Math.sqrt((x1-x2)*(x1-x2) + (y1-y2) * (y1-y2));
	}
	
}
