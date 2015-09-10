package model;

import java.util.ArrayList;
import java.util.List;
/**
 * @author Gurkan Solmaz 
 *    
 *    Department of Electrical Engineering and Computer Science
 *    University of Central Florida
 *
 */
public class Trajectory {
	
	// trajectory points of all visitors for a specific trajectory time
	private List<WayPoint> trajectory;
	private double currentTimeOfTrajectory;
	
	
	
	public Trajectory(double currentTimeOfTrajectory) {
		super();
		this.trajectory = new ArrayList<WayPoint>();
		this.currentTimeOfTrajectory = currentTimeOfTrajectory;
	}

	
	
	public List<WayPoint> getTrajectory() {
		return trajectory;
	}



	public void setTrajectory(List<WayPoint> trajectory) {
		this.trajectory = trajectory;
	}



	public double getTrajectoryTime() {
		return currentTimeOfTrajectory;
	}

}
