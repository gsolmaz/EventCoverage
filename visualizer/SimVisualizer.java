package visualizer;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Calendar;
import java.util.List;

import javax.swing.JFrame;

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
public class SimVisualizer implements KeyListener{
	// GUI Data
	public JFrame frame; 
	public Canvas area2D; 
	private int width; // the width of the 2D area
	private int height; // the height of the 2D area
	private int nodeRadius;
	private int pointRadius;
	private int squareHalfLength;
	public boolean case1;
	private double projectionRatio;
	Graphics g ;



	Calendar keyTimeFlag;
	
	
	// constructor
	public SimVisualizer() {
		this.width=600;
		this.height=600;
		double dimensionLength= 100;
		this.projectionRatio = dimensionLength/width; 
		this.nodeRadius = 2;
		this.pointRadius = 1;
		this.squareHalfLength = 5;
		configureGUI();
	}


	public JFrame getFrame() {
		return frame;
	}


	public void setFrame(JFrame frame) {
		this.frame = frame;
	}


	public Canvas getArea2D() {
		return area2D;
	}


	public void setArea2D(Canvas area2d) {
		area2D = area2d;
	}


	private void configureGUI()
	{
		// Create the window object
		frame = new JFrame("Theme Park Simulation");
		frame.setSize(width, height);
		frame.setResizable(false);
		frame.setBounds(700, 0, width, height);
		
		// The program should end when the window is closed
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Set the window's layout manager
		frame.setLayout(new FlowLayout());
		
		// set area
		createArea();
		
		// Make the frame listen to keystrokes
		frame.addKeyListener(this);
		frame.setVisible(true);
		case1 =true;
		        

	}
	
	public void draw(List<MobileSink> mobileSinkList, Trajectory trajectory, List<QueueNode> queueList, Event e ){

		// draw mobile sink nodes
		g.setColor(Color.BLUE);

		for(int i=0; i<mobileSinkList.size(); i++){
			MobileSink m = mobileSinkList.get(i);
			g.drawOval((int)Math.floor(m.getX()/projectionRatio-nodeRadius),(int)Math.floor((int)m.getY()/projectionRatio - nodeRadius), nodeRadius*2, nodeRadius*2);
	    	g.fillOval((int)Math.floor(m.getX()/projectionRatio-nodeRadius),(int)Math.floor((int)m.getY()/projectionRatio - nodeRadius), nodeRadius*2, nodeRadius*2);

		}
		
		// draw event node
		g.setColor(Color.RED);

		g.drawOval((int)Math.floor(e.getEventLocationX()/projectionRatio-nodeRadius),(int)Math.floor((int)e.getEventLocationY()/projectionRatio - nodeRadius), nodeRadius*2, nodeRadius*2);
    	g.fillOval((int)Math.floor(e.getEventLocationX()/projectionRatio-nodeRadius),(int)Math.floor((int)e.getEventLocationY()/projectionRatio - nodeRadius), nodeRadius*2, nodeRadius*2);
		g.drawString("Event", (int)Math.floor(e.getEventLocationX()/projectionRatio-nodeRadius), (int)Math.floor(e.getEventLocationY()/projectionRatio)+2*nodeRadius);

			
		g.setColor(Color.BLACK);

		// draw trajectory points
		if(trajectory != null){
			for(int i=0; i<trajectory.getTrajectory().size(); i++){
				WayPoint w = trajectory.getTrajectory().get(i);
							g.drawOval((int)Math.floor(w.getX()/projectionRatio-pointRadius),(int)Math.floor((int)w.getY()/projectionRatio - pointRadius), pointRadius*2, pointRadius*2);
				g.fillOval((int)Math.floor(w.getX()/projectionRatio-pointRadius),(int)Math.floor((int)w.getY()/projectionRatio - pointRadius), pointRadius*2, pointRadius*2);
			}
		}
		// draw queues
		for(int i=0; i<queueList.size(); i++){
			QueueNode q = queueList.get(i);
			if(q.getQueueType().equalsIgnoreCase("Restaurant") || q.getQueueType().equalsIgnoreCase("RT")){
				g.setColor(Color.YELLOW);
			}
			else if(q.getQueueType().equalsIgnoreCase("Ride")){
				g.setColor(Color.RED);
			}
			else if(q.getQueueType().equalsIgnoreCase("MediumRide") ||q.getQueueType().equalsIgnoreCase("M-RD") ){
				g.setColor(Color.BLUE);
		
			}
			else if(q.getQueueType().equalsIgnoreCase("LiveShow") ||q.getQueueType().equalsIgnoreCase("LS") ){
				g.setColor(Color.MAGENTA);
			}
			else {  
				g.setColor(Color.YELLOW);
				}
			// Make all the rectangles Gray
		//	g.setColor(Color.GRAY);
			
			g.drawRect((int)Math.floor(q.getCenterX()/projectionRatio-squareHalfLength), (int)Math.floor(q.getCenterY()/projectionRatio-squareHalfLength), 
						squareHalfLength*2, squareHalfLength*2); 		 
			g.fillRect((int)Math.floor(q.getCenterX()/projectionRatio-squareHalfLength), (int)Math.floor(q.getCenterY()/projectionRatio-squareHalfLength), 
						squareHalfLength*2, squareHalfLength*2); 		
			
			g.setColor(Color.BLACK);
			g.drawOval((int)Math.floor(q.getCenterX()/projectionRatio-nodeRadius), (int)Math.floor(q.getCenterY()/projectionRatio-nodeRadius), 
					 nodeRadius*2, nodeRadius*2); 
			g.fillOval((int)Math.floor(q.getCenterX()/projectionRatio-nodeRadius), (int)Math.floor(q.getCenterY()/projectionRatio-nodeRadius), 
					 nodeRadius*2, nodeRadius*2); 	
		//	g.setColor(Color.BLACK);
			if(q.getQueueType().equalsIgnoreCase("Restaurant"))
				g.drawString( " RT", (int)Math.floor( q.getCenterX()/projectionRatio-nodeRadius), (int)Math.floor(q.getCenterY()/projectionRatio)+nodeRadius);
			else if(q.getQueueType().equalsIgnoreCase("Ride"))
				g.drawString( " RD", (int)Math.floor( q.getCenterX()/projectionRatio-nodeRadius), (int)Math.floor(q.getCenterY()/projectionRatio)+nodeRadius);
			else if(q.getQueueType().equalsIgnoreCase("MediumRide"))
				g.drawString( " M-RD", (int)Math.floor(q.getCenterX()/projectionRatio-nodeRadius), (int)Math.floor(q.getCenterY()/projectionRatio)+nodeRadius);
			else if(q.getQueueType().equalsIgnoreCase("LiveShow"))
				g.drawString( " LS", (int)Math.floor(q.getCenterX()/projectionRatio-nodeRadius), (int)Math.floor(q.getCenterY()/projectionRatio)+nodeRadius);
			else
			g.drawString(" "+ q.getQueueType(), (int)Math.floor(q.getCenterX()/projectionRatio-nodeRadius), (int)Math.floor(q.getCenterY()/projectionRatio)+2*nodeRadius);
			

		}
		case1 =false;
		
	}
	
	public void createArea(){
		// Create the play area
		area2D = new Canvas();
		area2D.setSize(width, height);
		area2D.setBackground(Color.WHITE);
		area2D.setFocusable(false);
		frame.add(area2D);
	}
	
	public void clearArea(){
		g = area2D.getGraphics();
		
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);
		g.setColor(Color.BLUE);
		Font font = new Font ("Monospaced", Font.BOLD , 18);
		g.setFont(font);
	}
	@Override
	public void keyPressed(KeyEvent key) {
		// TODO Auto-generated method stub
		if(key.getKeyCode() == KeyEvent.VK_UP){
			this.case1 = true;
		}
	
	}
	@Override
	public void keyReleased(KeyEvent key) {
		// TODO Auto-generated method stub

	}
	

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	public boolean isCase1() {
		return case1;
	}


	public void setCase1(boolean case1) {
		this.case1 = case1;
	}
	
	
}
