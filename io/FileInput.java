package io;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import exception.ReadingQueueDataException;
import exception.ReadingSLAWDataException;
import exception.ReadingThemeParkDataException;

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
public class FileInput {
	private String mobilityModel;
	private int numberOfVisitors = 500; 
	private double trajectoryTime ;
	private double trajectorySamplingTime;  
	private int trajectoryListSize;

	
	public FileInput(String mobilityModelInput, double simTime, double samplingTime) {
		mobilityModel = mobilityModelInput; // "SLAW" or "Theme Park"
		trajectoryTime = simTime;
		trajectorySamplingTime = samplingTime;
		trajectoryListSize= (int) (trajectoryTime/trajectorySamplingTime);

	}

	public List<Trajectory> readTrajectoryFile() {
		List<Trajectory> trajectoryList = null;
		
		if(mobilityModel.equalsIgnoreCase("SLAW")){
			 trajectoryList = readSlawMobilityData();
		}
		else if(mobilityModel.equalsIgnoreCase("Theme Park")){
			 trajectoryList = readThemeParkMobilityData();
		}
		
		return trajectoryList;
	}


	private List<Trajectory> readThemeParkMobilityData() {

		String curDir = System.getProperty("user.dir");
		
		File f = new File(curDir+  "\\input\\" + "Trajectory" + ".txt");
		Scanner s;
		List<Trajectory> themeParkTrajectoryList= new ArrayList<Trajectory>();
		
		try {
			s = new Scanner(f);
			// initial points
			
			/*	NumberOfVisitors: 500
			SimulationTime: 36000.0
			SamplingTime: 10.0
			Current Simulation Time: 0.0
			Index: 0 Coordinates: 29.98 52.71 */
			s.nextLine(); s.nextLine(); s.nextLine();
			boolean curFlag = true;
			for(int i=0;i<trajectoryListSize; i++){
				if(!s.hasNextLine()){
					break;
				}
				if(curFlag){
					s.nextLine(); // for currentSimulation Time ( it is equal to i*trajectorySamplingTime )
					curFlag = false;
				}
				else{
					s.nextLine();
					s.nextLine();
				}
	
				Trajectory t = new Trajectory(i*trajectorySamplingTime);
				List<WayPoint> tmpTrajectoryPointList = new ArrayList<WayPoint>();
				
				for(int j=0; j<numberOfVisitors; j++){
					double x=-1;
					double y=-1;
				
					if(!s.hasNext()){
						break;
					}
					String tmp = s.next();
					if(tmp.equalsIgnoreCase("Current")){
						curFlag = true;
						break;
					}
					else{ // trajectories for this time has not finished
						int tmpIndex = s.nextInt();
						s.next();
						x= s.nextDouble();
						y = s.nextDouble();
					    WayPoint wp = new WayPoint(x,y, tmpIndex);
					    tmpTrajectoryPointList.add(wp);
					}
					
					if(x==-1 || y==-1){
						System.out.println("Error: Cannot read Theme Park mobility data!");
						throw new ReadingThemeParkDataException();
					}
			
				}
				
				t.setTrajectory(tmpTrajectoryPointList);
				themeParkTrajectoryList.add(t);
			}
			
		} 
		catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Error: Cannot read SLAW data!");
		}
		catch (ReadingThemeParkDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error: Unexpected error while reading input from SLAW data file!");
	}
		return themeParkTrajectoryList;
		
	}

	private List<Trajectory> readSlawMobilityData() {
	
		String curDir = System.getProperty("user.dir");

		File f = new File(curDir+  "\\input\\" + mobilityModel + ".mob");
		Scanner s;
		List<Trajectory> slawTrajectoryList= new ArrayList<Trajectory>();

		try {
			s = new Scanner(f);
			// initial points
			
			for(int i=0;i<trajectoryListSize; i++){
				// for each mobility simulation time ... 
				
				//$node_(1) set X_ 665.3
				//$node_(1) set Y_ 506.5
				//$node_(1) set Z_  0.0
				
				Trajectory t = new Trajectory(i*trajectorySamplingTime);
				List<WayPoint> tmpTrajectoryPointList = new ArrayList<WayPoint>();
			
				for(int j=0; j<numberOfVisitors; j++){
					double x=-1;
					double y=-1;
					if(i==0){ // inputs of simulation time=0 have different format
						// add the points for all visitors at this time
						s.next();  s.next(); s.next();
					    x = s.nextDouble();
						s.next();  s.next(); s.next();
					    y = s.nextDouble();
						s.next();  s.next(); s.next();
					    s.nextDouble(); // this value is not used (z coordinate)
					}
					else{
						s.next();  s.next(); s.next(); s.next(); s.next();
						x= s.nextDouble();
						y= s.nextDouble();
						s.next(); // this value is not used (z coordinate)
					}
					if(x==-1 || y==-1){
						System.out.println("Error: Cannot read SLAW data!");
						throw new ReadingSLAWDataException();
					}
				    WayPoint wp = new WayPoint(x,y, j);
				    tmpTrajectoryPointList.add(wp);
				}
				
				t.setTrajectory(tmpTrajectoryPointList);
				slawTrajectoryList.add(t);
			}
			
		} 
		catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Error: Cannot read SLAW data!");
		}
		catch (ReadingSLAWDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error: Unexpected error while reading input from SLAW data file!");
	}
		return slawTrajectoryList;
	
	}

	public List<QueueNode> readQueueFile() {
		

		String curDir = System.getProperty("user.dir");

		File f = new File(curDir+  "\\input\\" + "Queues" + ".txt");
		Scanner s;
		List<QueueNode> queueList= new ArrayList<QueueNode>();


			try {
				s = new Scanner(f);
				/*	NumberOfQueues: 15
				*	Index: 0 QType: Restaurant
				*	CenterCoordinates: 61.45934210526318 27.185000000000006
				*	Capacity: 76
				*/	
				s.next();
				int queueListSize = s.nextInt();
				for(int i=0; i<queueListSize;i++){
					s.next();
					int index = s.nextInt();
					if(index != i){
						throw new ReadingQueueDataException();
					}
					s.next();
					String queueType = s.next();
					s.next();
					double x = s.nextDouble(); 
					double y = s.nextDouble();
					s.next();
					int capacity = s.nextInt();
					QueueNode qn = new QueueNode(capacity, index, x,y, queueType);
					queueList.add(qn);
				}
			
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ReadingQueueDataException e) {
				// TODO Auto-generated catch block
				System.out.println("Error: Unexpected error while reading input from Queue data file!");
				e.printStackTrace();
			}
		
		return queueList;
	}

}
