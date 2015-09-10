/**
 * 
 */
package processor;

import java.util.List;

import exception.DirectedGraphException;

import model.DirectedGraph;
import model.Event;
import model.MobileSink;
import model.QueueNode;
import model.Trajectory;
import model.WayPoint;


/**
 * @author Gurkan Solmaz 
 *    
 *    Department of Electrical Engineering and Computer Science
 *    University of Central Florida
 *
 */
public class DirectedGraphUpdater {
	EdgeWeightComputer edgeWeightComputer;
	List<QueueNode> queueNodeList;
	
	public DirectedGraphUpdater(DirectedGraph directedGraph, List<QueueNode> qNodeList) {
		super();
		edgeWeightComputer = new EdgeWeightComputer();
		queueNodeList = qNodeList;
	}

	public DirectedGraph updateDirectedGraph(List<QueueNode> queueNodeList, DirectedGraph directedGraph, Event e,	
							        List<MobileSink> mobileSinkList, Trajectory firstTrajectory, Trajectory secondTrajectory) {
		EdgeWeightComputer edgeWeightComputer = new EdgeWeightComputer();
		for(int i=0;i<directedGraph.getNumberOfVertices();i++){
			int numberOfMobileSinks= mobileSinkList.size();
			for(int j=0;j<directedGraph.getNumberOfVertices();j++){
				if(i==j){ // no self loop
					continue;
				}
				// TYPES: 0 - queue node, 1-mobile sink node, 2- event node
				int typeNodeFrom = findNodeType(i,numberOfMobileSinks );
				int typeNodeTo = findNodeType(j, numberOfMobileSinks);
				// check if there is a way or not
				if(typeNodeFrom==0 && typeNodeTo ==0){// both queue nodes
					
					if(directedGraph.getGraphMatrix()[i][j] ==0 ){// if there is no way between the two queue nodes
						continue; // do not change the value of the edge (leave it as 0)
					}	
				}	
				if((typeNodeFrom == 1 && (typeNodeTo==1 || typeNodeTo == 2)) || (typeNodeFrom == 2 && (typeNodeTo==1 || typeNodeTo == 2)) ){
					directedGraph.updateGraphMatrixValue(i, j, 0);
					continue; // no edge between sinks to  sinks , events to sinks , sinks to events, events to events
				}
				
				if(typeNodeFrom==2 || typeNodeTo ==1){ // no edge from an event or to a mobile sink
					directedGraph.updateGraphMatrixValue(i, j, 0);
					continue;
				}
				if(typeNodeFrom==1){ // from a mobile sink, there is an edge to the nearest neighbor
					int sinkIndex = i - queueNodeList.size();
					if(mobileSinkList.get(sinkIndex).getCurrentQueueIndex() != j){ // not the nearest neighbor, do not produce an edge
						directedGraph.updateGraphMatrixValue(i, j, 0); // remove the edge if it exists before
						continue;
					}
				}
				if(typeNodeTo==2){ // to an event node, there is an edge from the nearest neighbor
					if(e.getEventQueueIndex()!= i){ // not the nearest neighbor, do not produce an edge
						directedGraph.updateGraphMatrixValue(i, j, 0); // remove the edge if it exists before
						continue;
					}
				}
				
				WayPoint p1,p2;
				p1= findCenterPointOfNode(typeNodeFrom, i, mobileSinkList, e);
				p2 = findCenterPointOfNode(typeNodeTo, j, mobileSinkList, e);

				double newEdgeWeight = edgeWeightComputer.computeEdgeWeightBetweenTwoNodes(p1, p2, firstTrajectory, secondTrajectory, mobileSinkList.get(0).getRegularSpeed());
				directedGraph.updateGraphMatrixValue(i, j, newEdgeWeight);
			}
		}
		return directedGraph;
	}
	
	public DirectedGraph updateDistanceGraph(List<QueueNode> queueNodeList, DirectedGraph distanceGraph, List<MobileSink> mobileSinkList, Event e) {
	
		// directedGraph is an argument by purpose, below we do not change the value of edges between queue nodes
		for(int i=0;i<distanceGraph.getNumberOfVertices();i++){
			int numberOfMobileSinks= mobileSinkList.size();
			for(int j=0;j<distanceGraph.getNumberOfVertices();j++){
				if(i==j){ // no self loop
					continue;
				}
				// TYPES: 0 - queue node, 1-mobile sink node, 2- event node

				int typeNodeFrom = findNodeType(i,numberOfMobileSinks );
				int typeNodeTo = findNodeType(j, numberOfMobileSinks);
				// check if there is a way or not
				if(typeNodeFrom==0 && typeNodeTo ==0){// both queue nodes	
					if(distanceGraph.getGraphMatrix()[i][j] ==0 ){// if there is no way between the two queue nodes
						continue; // do not change the value of the edge (leave it as 0)
					}	
				}	
				if((typeNodeFrom == 1 && (typeNodeTo==1 || typeNodeTo == 2)) || (typeNodeFrom == 2 && (typeNodeTo==1 || typeNodeTo == 2)) ){
					distanceGraph.updateGraphMatrixValue(i, j, 0);
					continue; // no edge between sinks to  sinks , events to sinks , sinks to events, events to events
				}
				if(typeNodeFrom==2 || typeNodeTo ==1){ // no edge from an event or to a mobile sink
					distanceGraph.updateGraphMatrixValue(i, j, 0);
					continue;
				}
				if(typeNodeFrom==1){ // from a mobile sink, there is an edge to the nearest neighbor
					int sinkIndex = i - queueNodeList.size();
					if(mobileSinkList.get(sinkIndex).getCurrentQueueIndex() != j){ // not the nearest neighbor, do not produce an edge
						distanceGraph.updateGraphMatrixValue(i, j, 0); // remove the edge if it exists before
						continue;
					}
				}
				if(typeNodeTo==2){ // from an event node, there is an edge to the nearest neighbor
					if(e.getEventQueueIndex()!= i){ // not the nearest neighbor, do not produce an edge
						distanceGraph.updateGraphMatrixValue(i, j, 0); // remove the edge if it exists before
						continue;
					}
				}
				
				WayPoint p1,p2;
				p1= findCenterPointOfNode(typeNodeFrom, i, mobileSinkList, e);
				p2 = findCenterPointOfNode(typeNodeTo, j, mobileSinkList, e);

				double newEdgeWeight = findDistanceBetweenNodes(p1, p2);
				distanceGraph.updateGraphMatrixValue(i, j, newEdgeWeight);
			}
		}
		return distanceGraph;
		
	}

	public double findDistanceBetweenNodes(WayPoint p1, WayPoint p2){
		
		return Math.sqrt((p1.getX()-p2.getX())*(p1.getX()-p2.getX()) + (p1.getY()-p2.getY()) * (p1.getY()-p2.getY()));
	}
	
	private WayPoint findCenterPointOfNode(int type, int index, List<MobileSink> mobileSinkList, Event e) {
		   	
			WayPoint p;
			double x=0, y=0;
			if(type==0){ // a queue node
		    	 x = queueNodeList.get(index).getCenterX();
				 y =  queueNodeList.get(index).getCenterY();
		    }
		    else if(type==1){ // a mobile sink node
		    	 x = mobileSinkList.get(index-queueNodeList.size()).getX();
				 y = mobileSinkList.get(index-queueNodeList.size()).getY();
		    }
		    else if(type==2){ // an event node
		    	 x = e.getEventLocationX();
				 y = e.getEventLocationY();
		    }	
			p = new WayPoint(x,y);
		   return p; 
	}

	public int findNodeType(int i, int numberOfMobileSinks){
		int returnType = -1;
		// TYPES: 0 - queue node, 1-mobile sink node, 2- event node
		try{
			if(i<queueNodeList.size()){
				// vertex "i" is a queue node
				returnType =0;
			}
			else if(i<queueNodeList.size() + numberOfMobileSinks){
				//vertex "i" is a mobile sink node
				returnType=1;
			}
			else if(i == queueNodeList.size() + numberOfMobileSinks)
			{	// vertex "i" is an event
				returnType=2;
			}
			else{
				throw new DirectedGraphException();
			}
		}
		catch(DirectedGraphException e){
			e.printStackTrace();
			System.out.println("Error: There is a problem for the number of queue nodes, mobile sinks and events");
		}
		return returnType;
	}
	
}
