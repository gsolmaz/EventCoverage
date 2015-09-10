/**
 * 
 */
package processor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import model.DirectedGraph;
import dijkstra.DijkstraAlgorithm;
import dijkstra.Edge;
import dijkstra.Graph;
import dijkstra.Vertex;
import exception.EventHandlingTypeDoesNotExistException;

/**
 * @author Gurkan Solmaz 
 *    
 *    Department of Electrical Engineering and Computer Science
 *    University of Central Florida
 *
 */

public class ShortestPathFinder {
	String pathFinderType;
	double lastTravelDistance;

	public ShortestPathFinder(String pathFinderType) {
		super();
		this.pathFinderType = pathFinderType;
	}
	
	public double handleEvent(DirectedGraph directedGraph, DirectedGraph distanceGraph, int numberOfQueues, int closestSinkIndex){
		double eventHandlingTime = 0;
		try{
			if(pathFinderType.equalsIgnoreCase("Shortest Path")){
				eventHandlingTime = findDijsktraShortestPath(distanceGraph.getGraphMatrix(), directedGraph.getGraphMatrix(), numberOfQueues);
			}
			else if(pathFinderType.equalsIgnoreCase("Closest Sink")){
				eventHandlingTime = findClosestSinkShortestPath(distanceGraph.getGraphMatrix(), directedGraph.getGraphMatrix(), numberOfQueues,closestSinkIndex );

			}
			else if(pathFinderType.equalsIgnoreCase("Random Sink")){
				eventHandlingTime = findRandomShortestPath(distanceGraph.getGraphMatrix(), directedGraph.getGraphMatrix(), numberOfQueues);
			}
			else{
				throw new EventHandlingTypeDoesNotExistException();
			}
		}
		catch(EventHandlingTypeDoesNotExistException e){
			e.printStackTrace();
			System.out.println("Error: Error in event handling type!");
		
		}
		
		
		// if the type is not according to dynamic edge weights, find the time according to exact weights but using the distance path
		return eventHandlingTime;
		
	}

	private double findRandomShortestPath(double[][] distGraphMatrix,
			double[][] dynamicGraphMatrix, int numberOfQueues) {
		Graph g = createDijkstraGraph(distGraphMatrix);
		
		DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(g);
		// find shortest path from a random mobile sink
		double returnValue;
		int numberOfMobileSinks = distGraphMatrix.length - (numberOfQueues + 1);
		
		Random r = new Random();
		int selectedSink = r.nextInt(numberOfMobileSinks);
		selectedSink += numberOfQueues;
		
		dijkstra.execute(g.getVertexes().get(selectedSink)); // execute for source 
		LinkedList<Vertex> path = dijkstra.getPath(g.getVertexes().get(distGraphMatrix.length -1)); // find the path to the target (event)
		returnValue = findValueOfPath(path, dynamicGraphMatrix);
		this.lastTravelDistance = findValueOfPath(path, distGraphMatrix);

		return returnValue;

	}

	


	private double findClosestSinkShortestPath(double[][] distGraphMatrix,
			double[][] dynamicGraphMatrix, int numberOfQueues, int closestSinkIndex) {
		// find shortest path from the closest sink
		Graph g = createDijkstraGraph(distGraphMatrix);
		
		DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(g);

		double returnValue;
		
		int selectedSink = closestSinkIndex + numberOfQueues;
		
		dijkstra.execute(g.getVertexes().get(selectedSink)); // execute for source 
		LinkedList<Vertex> path = dijkstra.getPath(g.getVertexes().get(distGraphMatrix.length -1)); // find the path to the target (event)
		if(path == null){
			System.out.println("Could not find a path to travel");
		}
		returnValue = findValueOfPath(path, dynamicGraphMatrix);
		if(returnValue == 0){
			System.out.println("Error in calculating return value");
		}
		this.lastTravelDistance = findValueOfPath(path, distGraphMatrix);

		return returnValue;
		
		
	}

	private double findDijsktraShortestPath(double[][] distGraphMatrix,
			double[][] dynamicGraphMatrix, int numberOfQueues) {
		Graph g = createDijkstraGraph(dynamicGraphMatrix);
		DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(g);
		// find shortest path among all possible paths and return the min path (time)
		double minValue = 10000;
		for(int i=numberOfQueues; i<dynamicGraphMatrix.length-1; i++){ // for each mobile sink
			dijkstra.execute(g.getVertexes().get(i)); // execute for source 
			LinkedList<Vertex> path = dijkstra.getPath(g.getVertexes().get(dynamicGraphMatrix.length-1)); // find the path to the target (event)
			double tmp = findValueOfPath(path, dynamicGraphMatrix);
			if(tmp!=-1){
				this.lastTravelDistance = findValueOfPath(path, distGraphMatrix);
				if(tmp<minValue){
					minValue = tmp;
				}
			}
		}
		return minValue;
	}
	private double findValueOfPath(LinkedList<Vertex> path, double[][] graphMatrix) {
		double returnValue =0;
		// find the total distance of a path
		if(path == null){
			return -1;
		}
		
		for(int i=0;i<path.size()-1; i++){
			Vertex from = path.get(i);
			Vertex to = path.get(i+1);
			returnValue += graphMatrix[Integer.parseInt(from.getId())][Integer.parseInt(to.getId())];		
		}
		
		return returnValue;
	}

	private Graph createDijkstraGraph(double[][] graphMatrix) {
		
		List<Vertex> vertexes = new ArrayList<Vertex>();
		List<Edge> edges = new ArrayList<Edge>();
		int edgeIndex=0;
		for(int i=0;i<graphMatrix.length; i++){
			Vertex source = new Vertex(i+"", i+"");
			vertexes.add(source);
			for(int j=0;j<graphMatrix.length; j++){
				Vertex dest = new Vertex(j+"", j+"");
				if(i!=j && graphMatrix[i][j]!=0){ // ==> if there exists an edge
					Edge e = new Edge(edgeIndex+"", source, dest, graphMatrix[i][j]);
					edgeIndex++;
					edges.add(e);
				}
			}
		}
		Graph g = new Graph(vertexes, edges);
		return g;
	}

	public double getLastTravelDistance() {
		return lastTravelDistance;
	}

	public void setLastTravelDistance(double lastTravelDistance) {
		this.lastTravelDistance = lastTravelDistance;
	}

}
