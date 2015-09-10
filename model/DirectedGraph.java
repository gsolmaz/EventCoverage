package model;

/**
 * @author Gurkan Solmaz 
 *    
 *    Department of Electrical Engineering and Computer Science
 *    University of Central Florida
 *
 */
public class DirectedGraph {
	
	// vertices = queue nodes +  mobile sinks + events 
	private int numberOfVertices;
	
	/* graph matrix holds the values of the edges between vertices of the graph
	* Note: (-1) for no edge.
	* Index x,y starts with queue nodes, continues with mobile sinks and ends with the events
	*/
	private double[][] graphMatrix;
	
	
	public DirectedGraph(int numberOfQueueNodes, int numberOfMobileSinks,int maxNumberOfConcurrentEvents,
			double[][] initialMatrix ) {

		this.numberOfVertices = numberOfQueueNodes+ numberOfMobileSinks+ maxNumberOfConcurrentEvents;
		this.graphMatrix = initialMatrix;
	}

	public void updateGraphMatrixValue( int x, int y, double newValue){
		// x -> from , y-> to
		this.graphMatrix[x][y] = newValue;
	}
	
	public int getNumberOfVertices() {
		return numberOfVertices;
	}

	public double[][] getGraphMatrix() {
		return graphMatrix;
	}

	public void setNumberOfVertices(int numberOfVertices) {
		this.numberOfVertices = numberOfVertices;
	}


}
