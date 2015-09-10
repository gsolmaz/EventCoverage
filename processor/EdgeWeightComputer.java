package processor;

import java.util.ArrayList;
import java.util.List;

import exception.NullWayPointException;

import model.Trajectory;
import model.WayPoint;

/**
 * @author Gurkan Solmaz 
 *    
 *    Department of Electrical Engineering and Computer Science
 *    University of Central Florida
 *
 */
public class EdgeWeightComputer {

	
	
	public EdgeWeightComputer() {
		super();
	}

	public double computeEdgeWeightBetweenTwoNodes(WayPoint from, WayPoint to, Trajectory first, Trajectory second, double mobileSinkRegularSpeed){
		// find the distance between the two nodes
		double edgeWeight = 0;

		double areaOfHuman = 0.5 * 0.5; // contact area of a visitor
		double width = 50; // 10 meters (width of the way)
		double distance = findDistanceBetweenNodes(from, to);	
		double maxSpeed= mobileSinkRegularSpeed;
		double speedOfVisitor = 1.0; // 1 meters/second
		double littleDelta = 700;
		// find the direction of the sink (angle m)
		Double mobileSinkDirection = (to.getY() - from.getY())/(to.getX() - from.getX());
		if(mobileSinkDirection.isNaN()){
			mobileSinkDirection = Math.PI / 2;
		}
		else{
			mobileSinkDirection = Math.atan(mobileSinkDirection);
		}
		// find number of people walking in different directions
		List<Double> visitorsDirectionList = getVisitorsDirectionList(from, to, first, second, width);	
		
		// USING THE FORMULA FOR EDGE WEIGHTS !!!
		
		double speedOfMobileSink = maxSpeed;
		
		double c = areaOfHuman / (distance*width);
		
		//System.out.println("# of Visitors " + visitorsDirectionList.size() );


		for(int i=0; i<visitorsDirectionList.size(); i++){
			if(i<10){
		//	System.out.println("Directions of Visitor " + i + ": " +  visitorsDirectionList.get(i) );
			}
			double directionDiff = mobileSinkDirection -  visitorsDirectionList.get(i);
			speedOfMobileSink = speedOfMobileSink - littleDelta*  c*(speedOfMobileSink - speedOfVisitor*Math.cos(directionDiff));

		}
		if(speedOfMobileSink <0.05){
			speedOfMobileSink = 0.05;
		}
	//	System.out.println("Speed of the sink " + speedOfMobileSink );

		edgeWeight = distance / speedOfMobileSink;
		
		return edgeWeight;
	}
	
	private List<Double> getVisitorsDirectionList(WayPoint from, WayPoint to,Trajectory first, Trajectory second, double width) {
		List<Double> returnList = new ArrayList<Double>();
		try {
			for(int i=0; i<first.getTrajectory().size(); i++){
				WayPoint wp = first.getTrajectory().get(i);
				double dist = pointToLineDistance(from, to, wp);
				if(dist < (width/2) && checkIfInTheRegion(from, to, wp)){
					// the visitor is on the way
					WayPoint newWP= null;
					if(second.getTrajectory().size()>i){
						newWP = second.getTrajectory().get(i);
						if(newWP.getVisitorIndex() != wp.getVisitorIndex()){
							boolean visitorFound = false;
							for(int k=0;k<second.getTrajectory().size(); k++){
								if(second.getTrajectory().get(k).getVisitorIndex() == wp.getVisitorIndex()){
									newWP = second.getTrajectory().get(k);
									visitorFound= true;
								}
							}
							if(!visitorFound){ // visitor does not exist any more, go ahead with the next visitor
								continue; 
							}
						}
					}
					else{
						boolean visitorFound = false;
						for(int k=0;k<second.getTrajectory().size(); k++){
							if(second.getTrajectory().get(k).getVisitorIndex() == wp.getVisitorIndex()){
								newWP = second.getTrajectory().get(k);
								visitorFound= true;
							}
						}
						if(!visitorFound){ // visitor does not exist any more, go ahead with the next visitor
							continue; 
						}
					}
					if(newWP == null){
					
						throw new NullWayPointException();
					} 
				
					
					// find and add the movement direction(angle m) of the visitor
					Double direction =  (newWP.getY() - wp.getY())/(newWP.getX() - wp.getX());
					if(direction.isNaN()){
						direction = Math.PI /2;
					}
					else{
						direction = Math.atan(direction);
					}
					
					returnList.add(direction);
				}
			}
		}
		catch (NullWayPointException e) {
			e.printStackTrace();
			System.out.println("Error: Error in finding directions of visitors!");
		}
		
		return returnList;
	}

	private boolean checkIfInTheRegion(WayPoint from, WayPoint to, WayPoint wp) { // by triangle equations check the angle
		boolean returnValue= false;
		double d1 = findDistanceBetweenNodes(from, wp);
		double d2= findDistanceBetweenNodes(to, wp);
		double d3 = findDistanceBetweenNodes(from, to);
		if(d1<d2){ // wp is close to from
			if(Math.sqrt((d1*d1) + (d3*d3)) >= Math.sqrt(d2*d2) ){
				returnValue = true;
			}
			
		}
		else{
			if(Math.sqrt((d2*d2) + (d3*d3)) >= Math.sqrt(d1*d1) ){
				returnValue = true;
			}
			
		}
		return returnValue;
	}

	public double pointToLineDistance(WayPoint A, WayPoint B, WayPoint P)
	{
		double normalLength = Math.hypot(B.getX() - A.getX(), B.getY() - A.getY());
		return Math.abs((P.getX() - A.getX()) * (B.getY() - A.getY()) - (P.getY() - A.getY()) * (B.getX() - A.getX())) / normalLength;
	}


	public double findDistanceBetweenNodes(WayPoint p1, WayPoint p2){
		
		return Math.sqrt((p1.getX()-p2.getX())*(p1.getX()-p2.getX()) + (p1.getY()-p2.getY()) * (p1.getY()-p2.getY()));
	}
	
	
}
