/**
 * 
 */
package model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Gurkan
 *
 */
public class ResultBackup {
	
	List<Double> avgHandlingTimeResultBuffer, hitRatioResultBuffer, avgDistanceResultBuffer;
	List<Double>  meanTimes, meanHitRatios, meanDistances;
	public ResultBackup() {
		avgHandlingTimeResultBuffer = new ArrayList<Double>();
		hitRatioResultBuffer = new ArrayList<Double>();
		avgDistanceResultBuffer = new ArrayList<Double>();
		meanTimes = new ArrayList<Double>();
		meanDistances= new ArrayList<Double>();
		meanHitRatios = new ArrayList<Double>();
	}

	public void clearLocalBuffers(){
		avgHandlingTimeResultBuffer.clear();
		avgHandlingTimeResultBuffer = new ArrayList<Double>();
		hitRatioResultBuffer.clear(); hitRatioResultBuffer = new ArrayList<Double>();
		avgDistanceResultBuffer.clear(); avgDistanceResultBuffer = new ArrayList<Double>();
	}
	
	public void addLocalBuffers(double time, double distance, double ratio){
		avgHandlingTimeResultBuffer.add(time);
		avgDistanceResultBuffer.add(distance);
		hitRatioResultBuffer.add(ratio);

	}
	
	public void addGlobalBuffers(double meanTime, double meanDistance, double meanRatio){
		meanTimes.add(meanTime);
		meanHitRatios.add(meanRatio);
		meanDistances.add(meanDistance);
	}

	public List<Double> getAvgHandlingTimeResultBuffer() {
		return avgHandlingTimeResultBuffer;
	}

	public List<Double> getHitRatioResultBuffer() {
		return hitRatioResultBuffer;
	}

	public List<Double> getAvgDistanceResultBuffer() {
		return avgDistanceResultBuffer;
	}

	public List<Double> getMeanTimes() {
		return meanTimes;
	}

	public List<Double> getMeanHitRatios() {
		return meanHitRatios;
	}

	public List<Double> getMeanDistances() {
		return meanDistances;
	}
	
}
