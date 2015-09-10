package ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author Gurkan Solmaz 
 *    
 *    Department of Electrical Engineering and Computer Science
 *    University of Central Florida
 *    
 */
public class InputUI implements ActionListener {

		/* 
		 * 
		 * Inputs from the GUI: 
		 * 			- Mobile Sink Decision Type (adaptive, weighted or random)
		 * 			- Number of mobile sinks
		 * 			- Number of specific queues 
		 * 		//	- Max number of concurrent events
		 * 			- Event Handling Strategy (shortest path or. closest sink or random)
		 * 			- Mobility Model
		 * 			- Event happening type
		 * 		
		 * 			- Visualizer enabled or disabled
		 * 			- Key input enabled (?)
		 * 
		 */
	 	private JFrame guiFrame;
	 	private String inputArray[];
	 	private int numberOfInputs;
	 	private JPanel inputPanel;
	 	private JPanel visualizerPanel;
	 	public boolean buttonPressed;
		
  	    public InputUI(int numOfUserInputs)
	    {
  	    	numberOfInputs = numOfUserInputs;
	        guiFrame = configureGUIFrame();
	        addPanels();
	  
	    }    
	    
	    private void addPanels() {	
	    	
	    	inputPanel = createInputPanel();
	    
	    	visualizerPanel  = createVisualizerOptionsPanel();

	        JButton startButton = new JButton( "Start Simulation");	        
	        
	        guiFrame = putPanelsAndButtons(startButton );
	  
	  
	        //make sure the JFrame is visible
	        guiFrame.setVisible(true);
	
	    
	        //The ActionListener class is used to handle the      
	        //event that happens when the user clicks the button.

	        startButton.addActionListener(this);
	            
	    }
	
		private JPanel createVisualizerOptionsPanel() {
			// Visulizer options
	        //The first JPanel contains a JLabel and JCombobox
	        final JPanel visualizerPanel = new JPanel();
	        
	        //Options for the JComboBox 
	        String[] choices = {"Disabled", "Enabled"};
	        
	        JLabel comboLabel = new JLabel("                               Visualizer:");
	        JComboBox comboBox = new JComboBox(choices);
	        //JButton 
	        visualizerPanel.add(comboLabel);
	        visualizerPanel.add(comboBox);

	        comboLabel = new JLabel("                     Keyboard Input:");
	        comboBox = new JComboBox(choices);
	        //JButton 
	        visualizerPanel.add(comboLabel);
	        visualizerPanel.add(comboBox);
	        
	        
	        
	        
	        return visualizerPanel;
		
		}

		private JPanel createInputPanel() {
			// Mobile Sink Decision Type
	        final JPanel inputPanel = new JPanel();
	        
	        //Options for the JComboBox 
	        String[] choices = {"Adaptive", "Weighted" , "Random"};
	        
	        JLabel comboLabel = new JLabel("  Mobile Sink Decision Type:");
	        JComboBox comboBox = new JComboBox(choices);
	        //JButton 
	        inputPanel.add(comboLabel);
	        inputPanel.add(comboBox);
	       	        
	        JTextField jText = new JTextField(5);
			jText.setText("1");
	        jText.setVisible(true);
	        JLabel textLabel = new JLabel("Number of Mobile Sinks:");
	         
	        inputPanel.add(textLabel);
	        inputPanel.add(jText);

	        
	        jText = new JTextField(5);
			jText.setText("1");
	        jText.setVisible(true);
	        textLabel = new JLabel("             Specific Queues:");
		         
	        inputPanel.add(textLabel);
	        inputPanel.add(jText);
	        
	        jText = new JTextField(5);
   			jText.setText("10");
   	        jText.setVisible(true);
   	        textLabel = new JLabel("Number of Experiments:");
   		         
   	        inputPanel.add(textLabel);
   	        inputPanel.add(jText);

	        
 			// Event Handling Strategy Panel
 	        //Options for the JComboBox 
 	        String[] eventStrategy = {"Shortest Path", "Closest Sink" , "Random Sink"};
 	       
 	        comboLabel = new JLabel("                Event Handling Method:");
 	        comboBox = new JComboBox(eventStrategy);
 	        //JButton 
 	        inputPanel.add(comboLabel);
 	        inputPanel.add(comboBox);
 	        
	        //Options for the JList
	        String[] mobilityModels = {"Theme Park", "SLAW" };
	        
	        comboLabel = new JLabel("               Human Mobility Model:");
	        comboBox = new JComboBox(mobilityModels);
	        	
	        inputPanel.add(comboLabel);
	        inputPanel.add(comboBox);
       
			// Event Type Panel
	        //Options for the JComboBox 
	        String[] eventTypes = {"Random","Biased", "Specific" };
	       
	        comboLabel = new JLabel("                            Event Type:");
	        comboBox = new JComboBox(eventTypes);
	       
	        //JButton 
	        inputPanel.add(comboLabel);
	        inputPanel.add(comboBox);
	        
	   	   	        
	        
	        inputPanel.setVisible(true);
	 
	        return inputPanel;
		}

		private JFrame putPanelsAndButtons(JButton graphicsOptionButton) {
			
			JPanel container = new JPanel();
			container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

			//panel1.set[Preferred/Maximum/Minimum]Size()

			container.add(inputPanel);
			container.add(visualizerPanel);	
			
	        //Put the JPanels and JButton in different areas.
		    guiFrame.add(container);
	        guiFrame.add(graphicsOptionButton, BorderLayout.SOUTH);
	        return guiFrame;
		}
		

		public String[] getInputArray() {
			return inputArray;
		}
		
		private void setInputArray(JPanel inputPanel, JPanel visualizerPanel) {
			
			inputArray = new String[numberOfInputs];
			
			// get mobile sink decision type
			JComboBox combo = (JComboBox) inputPanel.getComponent(1);
			inputArray[0] = (String) combo.getSelectedItem();	
			// get number of mobile sinks
			JTextField textField = (JTextField) inputPanel.getComponent(3);
			inputArray[1] = (String) textField.getText();	
			
			textField = (JTextField) inputPanel.getComponent(5);
			inputArray[2] = (String) textField.getText();	
			
			textField = (JTextField) inputPanel.getComponent(7);
			inputArray[8] = (String) textField.getText();	
			
			
			// get the other inputs 
			for(int i=0; i<3; i++){
				// get input from combo box
				combo = (JComboBox) inputPanel.getComponent(9+i*2);
				inputArray[3+i] = (String) combo.getSelectedItem();	
			}

			// get visualizer inputs
			
			// get input from combo box
			combo = (JComboBox) visualizerPanel.getComponent(1);
			inputArray[6] = (String) combo.getSelectedItem();	
			
			combo = (JComboBox) visualizerPanel.getComponent(3);
			inputArray[7] = (String) combo.getSelectedItem();	
		
	
		
		
		}

		JFrame configureGUIFrame() {
	    	
			JFrame guiFrame = new JFrame();
	    	
	    	//make sure the program exits when the frame closes	
	    	guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            guiFrame.setTitle("Theme Park Simulation Options");
	        guiFrame.setSize(330,450);
	        //This will center the JFrame in the middle of the screen
	        guiFrame.setLocationRelativeTo(null);
	        return guiFrame;
		}

		@Override
        public void actionPerformed(ActionEvent e) {
			
			guiFrame.setVisible(false);	
		  	setInputArray(inputPanel, visualizerPanel);
		  	buttonPressed=true;
		  	
		}
	    
}

