package core;

import java.time.Duration;
import java.util.Random;

public class Ai {
	public Ai(double howPerfectInput) {
		howPerfect = 1 - howPerfectInput;
		randomGen = new Random();
		blockDirection = null;
		numSteps = 0;
	}
	
	
	public boolean controlBuilding(BuildingArea area, Duration step){
		Duration clickCorrection = calculateAction(area, step);
		return area.step(step, clickCorrection);
	}
	
	private Duration calculateAction(BuildingArea area,  Duration step){
		numSteps++;
		if (numSteps > (int)(20)) {
			int blockXPosAfterStep = calculateXAfterStep(area, step);
			if (checkBounce(area)){
					dropPoint = setDropPoint(area);
					if ((blockXPosAfterStep - dropPoint)*correctForDirec(area) >= 0 ) {
						dropPoint = null;
					}
			}
			if (dropPoint != null && (blockXPosAfterStep - dropPoint)*correctForDirec(area) >= 0 ) {
				if (Math.random() > howPerfect) {
					int currentX = currentXPos(area);
					blockDirection = null;
					numSteps = 0;
					return Duration.ofNanos(step.toNanos() * (dropPoint - currentX) / (blockXPosAfterStep - currentX));
				}
			}
		}
		return null;
	}
	
	private boolean checkBounce(BuildingArea area){
		if (blockDirection == null) {
			blockDirection = (area.getXVel() > 0);
			return true;
		}
		boolean newDirection = (area.getXVel() > 0);
		boolean change = newDirection != blockDirection;
		blockDirection = newDirection;
		return change;
	}

	private int correctForDirec(BuildingArea area) {
		return area.getBlockDirection();
	}

	private int currentXPos(BuildingArea area) {
		return area.getXMovingBlock();
	}

	private int setDropPoint(BuildingArea area) {
		int xPosOfTopStationaryBlock = (int)(area.getXTopStationary() + randomGen.nextGaussian() * howPerfect * 50);
		return xPosOfTopStationaryBlock;
	}

	private int calculateXAfterStep(BuildingArea area, Duration step) {
		return area.pretendStep(step);
	}
	
	private int numSteps;
	private Boolean blockDirection;
	private Integer dropPoint;
	private final double howPerfect;
	private Random randomGen;
}
