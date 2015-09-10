package io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import model.ResultBackup;

import controller.InputCtrl;
import controller.OutputCtrl;

/**
 * @author Gurkan Solmaz 
 *    
 *    Department of Electrical Engineering and Computer Science
 *    University of Central Florida
 *
 */
public class FileOutput {

	InputCtrl inputCtrl;
	OutputCtrl outputCtrl;
	ResultBackup updatedResultBackup;
	

	public FileOutput(OutputCtrl outputCtrl, InputCtrl inputCtrl, ResultBackup rs) {
		this.outputCtrl = outputCtrl;
		this.inputCtrl = inputCtrl;
		this.updatedResultBackup = rs;

	}


	public void writeSimulationResults(int expNumber) {
		String curDir = System.getProperty("user.dir");
		FileWriter fstream;
		try {
			
		
			fstream = new FileWriter(curDir + "\\output\\" +"Output-" +  
					inputCtrl.getMobileSinkDecisionType()+  "-" + 
					inputCtrl.getEventHandlingStrategy()+  "-" +
					inputCtrl.getMobilityModel()+  "-" +
					inputCtrl.getEventType() + "-" +
					inputCtrl.getNumberOfSpecificQueues() +
					".txt", true);
		
			BufferedWriter out = new BufferedWriter(fstream);
		
			// write outputs
		//	out.write("OUTPUTS:\n");
		//	out.write("AverageHandlingTime: " + outputCtrl.getAverageEventHandlingTime() + "\n" );
		//	out.write("AverageTravelDistance: " + outputCtrl.getAverageTravelDistance() + "\n" );
	//		out.write("HitRatio: " + outputCtrl.getHitRatio() + "\n" );
			
			if(expNumber==0){
				out.write("Experiment information:" + inputCtrl.getMobileSinkDecisionType() + "," + inputCtrl.getNumberOfMobileSinks() + ","
					+inputCtrl.getEventHandlingStrategy() + "," + inputCtrl.getMobilityModel()+ "," + 
					inputCtrl.getEventType() + "," + inputCtrl.getNumberOfSpecificQueues() +  "\t");
				out.write("Number of experiments: " + inputCtrl.getNumberOfExperiments() +  "\n");
			}
			
		
			out.write(outputCtrl.getAverageEventHandlingTime() + "\t" );
			out.write(outputCtrl.getAverageTravelDistance() + "\t" );
			out.write(outputCtrl.getHitRatio() + "\n" );
			
			updatedResultBackup.addLocalBuffers(outputCtrl.getAverageEventHandlingTime(), outputCtrl.getAverageTravelDistance(), outputCtrl.getHitRatio());
	
			if(expNumber==inputCtrl.getNumberOfExperiments()-1){
				// find and output the average
				int numExp = inputCtrl.getNumberOfExperiments();
				double totalAvgTime =0, totalRatio=0, totalDist =0;
				for(int i=0;i<inputCtrl.getNumberOfExperiments();i++){
					totalAvgTime+=updatedResultBackup.getAvgHandlingTimeResultBuffer().get(i);
					totalRatio+=updatedResultBackup.getHitRatioResultBuffer().get(i);
					totalDist += updatedResultBackup.getAvgDistanceResultBuffer().get(i);
				}
				
				updatedResultBackup.addGlobalBuffers(totalAvgTime/numExp, totalDist/numExp,totalRatio/numExp);
				updatedResultBackup.clearLocalBuffers();
				
				double meanAvgTime = totalAvgTime/inputCtrl.getNumberOfExperiments() ;
				double meanDist = totalDist/inputCtrl.getNumberOfExperiments() ;
				double meanRatio = totalRatio/inputCtrl.getNumberOfExperiments();
				out.write("Mean results:\n");
				out.write(meanAvgTime + "\t" );
				out.write(meanDist + "\t" );
				out.write(meanRatio + "\n" );
			
				// for journal 
			/*	double varianceAvgTime =0;
				double varianceDist =0;
				double varianceRatio=0;
		
				for(int i=0;i<inputCtrl.getNumberOfExperiments();i++){
					double diff1= meanAvgTime- updatedResultBackup.getAvgHandlingTimeResultBuffer().get(i);
					varianceAvgTime += diff1*diff1;
					double diff2= meanDist- updatedResultBackup.getAvgDistanceResultBuffer().get(i);
					varianceDist += diff2*diff2;
					double diff3= meanRatio- updatedResultBackup.getHitRatioResultBuffer().get(i);
					varianceRatio += diff3*diff3;
				}
				varianceAvgTime = varianceAvgTime / inputCtrl.getNumberOfExperiments();
				varianceDist = varianceDist / inputCtrl.getNumberOfExperiments();
				varianceRatio = varianceRatio / inputCtrl.getNumberOfExperiments();

				
				double standartDeviationAvgTime= Math.sqrt(varianceAvgTime);
				double standartDeviationDist=Math.sqrt(varianceDist);
				double standartDeviationRatio=Math.sqrt(varianceRatio);
				out.write("Journal Addition: Standart Deviations:\n");
				out.write(standartDeviationAvgTime + "\t" );
				out.write(standartDeviationDist + "\t" );
				out.write(standartDeviationRatio + "\n" );
				*/
			}
			
			
	/*		out.write("TotalTravelDistance: " + outputCtrl.getTotalTravelDistance() + "\t" );
	
	
			// write inputs
			out.write("INPUTS:\n");
			out.write("EdgeCreationType: " + inputCtrl.getEdgeCreationType() + "\n" );
			out.write("AdaptiveChangeRate: " + inputCtrl.getAdaptiveWeightChangeRate() + "\n" );
			out.write("EventHandlingStrategy: " + inputCtrl.getEventHandlingStrategy() + "\n" );
			out.write("EventHappeningTime: " + inputCtrl.getEventHappeningTime() + "\n" );
			out.write("EventType: " + inputCtrl.getEventType() + "\n" );

			out.write("MobileSinkDecisionType: " + inputCtrl.getMobileSinkDecisionType() + "\n" );
			out.write("MobileSinkLocationUpdateTime: " + inputCtrl.getMobileSinkLocationUpdateTime() + "\n" );
			out.write("MobilityModel: " + inputCtrl.getMobilityModel() + "\n" );
			out.write("NumberOfMobileSinks: " + inputCtrl.getNumberOfMobileSinks() + "\n" );
			out.write("SimulationTime: " + inputCtrl.getSimulationTime() + "\n" );
			out.write("SamplingTime: " + inputCtrl.getSamplingTime() + "\n" );
			out.write("NumberOfQueues: " + inputCtrl.getQueueList().size());
*/
			
			out.close();
			fstream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public void writeResultBackupInOrder(ResultBackup rba, int sinkNumberCount) {
		String curDir = System.getProperty("user.dir");
		FileWriter fstream;
		try {
			
			fstream = new FileWriter(curDir + "\\output\\" +"ExcelResults-" +  
					inputCtrl.getMobileSinkDecisionType()+  "-" + 
					inputCtrl.getEventHandlingStrategy()+  "-" +
					inputCtrl.getMobilityModel()+  "-" +
					inputCtrl.getEventType() + "-" +
					inputCtrl.getNumberOfSpecificQueues() + 
					".txt", true);
		
			BufferedWriter out = new BufferedWriter(fstream);
		

			out.write("Experiment information:" + inputCtrl.getMobileSinkDecisionType() + "," 
			+inputCtrl.getEventHandlingStrategy() + "," + inputCtrl.getMobilityModel()+ "," + 
				inputCtrl.getEventType() + "," + inputCtrl.getNumberOfSpecificQueues() +  ",");
			out.write("Number of experiments: " + inputCtrl.getNumberOfExperiments() + ","
					+ "Mob sink count-> from 1 to " + sinkNumberCount + "\n");
		
			for(int i=0;i<sinkNumberCount;i++){
				out.write(rba.getMeanTimes().get(i)+ "\t");
				out.write(rba.getMeanDistances().get(i)+ "\t");
				out.write(rba.getMeanHitRatios().get(i)+ "\n");
			}

			
			
			out.close();
			fstream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public ResultBackup getUpdatedResultBackup() {
		return updatedResultBackup;
	}
	
	
}
