/**
 * 
 */
package controller;

import model.ResultBackup;
import io.FileOutput;


/**
 * @author Gurkan Solmaz 
 *    
 *    Department of Electrical Engineering and Computer Science
 *    University of Central Florida
 * 
 */

public class BaseCtrl {

	public static void main(String[] args) {
		
		// get all simulation parameters
		InputCtrl inputCtrl = new InputCtrl();
		ResultBackup rs = new ResultBackup();
		int sinkNumberCount = 20;
		for(int mobSinkCount=1;mobSinkCount<= sinkNumberCount; mobSinkCount++){
			inputCtrl.setNumberOfMobileSinks(mobSinkCount);
			for(int i=0;i<inputCtrl.getNumberOfExperiments();i++){
				// create initial setup (attraction-graph, mobile sinks)
				PreProcessCtrl preProcessCtrl = new PreProcessCtrl(inputCtrl.getQueueList(),
																   inputCtrl.getNumberOfMobileSinks(),
																//   inputCtrl.getMaxNumberOfConcurrentEvents(), 
																   inputCtrl.getMobileSinkRegularSpeed(),
																   inputCtrl.getGraphDensity(),
																   inputCtrl.getGraphDegree(),
																   inputCtrl.getEdgeCreationType(),
																   inputCtrl.getAdaptiveWeightChangeRate());
				
				// create events using the EventCtrl
				EventCtrl eventCtrl = new EventCtrl(inputCtrl.getEventType(),
												   // inputCtrl.getMaxNumberOfConcurrentEvents(), 
												    inputCtrl.getEventHappeningTime(),
												    inputCtrl.getEventMaxPriority(),
													inputCtrl.getEventMinTimeout(),
													inputCtrl.getEventMaxtimeout(),
													inputCtrl.getSimulationTime(),
													inputCtrl.getMobileSinkLocationUpdateTime(),
													inputCtrl.getQueueList(), 
													inputCtrl.getNumberOfSpecificQueues());
				
				
				
				// start simulation using SimCtrl
				SimCtrl simCtrl = new SimCtrl(preProcessCtrl.getGraphCtrl(), 
											  eventCtrl,
											  inputCtrl.getEventHandlingStrategy(),
										      inputCtrl.getSamplingTime(), 
										      inputCtrl.getMobileSinkDecisionType(),
										      inputCtrl.getAdaptiveWeightChangeRate(),
										      inputCtrl.getTrajectoryList() ,
										      inputCtrl.isVisualizerEnabled(), 
										      inputCtrl.isKeyboardInputEnabled());
				simCtrl.simulate();
				
				FileOutput fileOutput = new FileOutput(simCtrl.getOutputCtrl(), inputCtrl, rs);
				rs = null;
				rs = fileOutput.getUpdatedResultBackup();
				fileOutput.writeSimulationResults(i);
			}
		}
		FileOutput fo = new FileOutput(null, inputCtrl, rs);
		fo.writeResultBackupInOrder(rs, sinkNumberCount);
		System.exit(0);
	}

}
