package core;

import java.time.Duration;
import java.util.*;

public class Game {
	public Game(int startWindowWidth, int startWindowHeight, int startDifficulty, boolean changeSpeed){
		int initialVelocity = 150;
		windowWidth = startWindowWidth;
		windowHeight = startWindowHeight;
		areas = new ArrayList<Area>();
		areas.add(new Area (windowWidth/2, windowHeight, startDifficulty, changeSpeed, initialVelocity));
		areas.add(new Area (windowWidth/2, windowHeight, startDifficulty, changeSpeed, initialVelocity));
		ai = new Ai(0.8); //1 = perfect, 0 = basic bitch
		layerHeight = Block.getHeight() + getBuildingArea(0).getGapBetweenLayers();
		numBlocksFitOnScreen = (int) (startWindowHeight/layerHeight);
	}
	
	public boolean step(Duration step, Duration clickCorrection){
		if (getRunning(0)) {
			setRunning(0, getBuildingArea(0).step(step, clickCorrection));
		} 
		else {
			endGame(0);
			youWin(1);
		}
		if (getRunning(1)) {
			setRunning(1, ai.controlBuilding(getBuildingArea(1), step));
		}
		else {
			endGame(1);
			youWin(0);
		}
		compareTowerHeights();
		return (getRunning(0) || getRunning(1));
 	}
	
	private void setRunning(int i, boolean j){
		areas.get(i).running = j;
	}
	private boolean getRunning(int i){
		return areas.get(i).running;
	}
	
	private void youWin(int i){
		getBuildingArea(i).youWin();
	}
	
	public BuildingArea getBuildingArea(int i) {
		return areas.get(i).buildingArea;
	}
	
	public void endGame(int i){
		areas.get(i).buildingArea.endGame();
	}
	
	public int getNumBuildingAreas() {
		return areas.size();
	}
	
	private void compareTowerHeights() {
		Area highestScoreArea = Collections.max(areas, Comparator.comparing(Area::getScore));
		for (Area b : areas) {
			if (highestScoreArea.getScore() - b.getScore() >= numBlocksFitOnScreen){
				b.running = false;
			}
		}
	}
	
	public final int layerHeight;
	public final int numBlocksFitOnScreen;
	private Ai ai;
	private ArrayList<Area> areas;
	private final int windowWidth;
	private final int windowHeight;
	
	private class Area {
		public Area(int startAreaWidth, int startAreaHeight, int startDifficulty, boolean startChangeSpeed, int initVelocity) {
			buildingArea = new BuildingArea(startAreaWidth, startAreaHeight, startDifficulty, startChangeSpeed, initVelocity);
			running = true;
		}
		
		public int getScore(){
			return buildingArea.getScore();
		}
		
		public boolean running;
		public BuildingArea buildingArea;
	}

	
}
