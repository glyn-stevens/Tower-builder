package core;
import java.time.Duration;
import java.util.ArrayList;

public class BuildingArea {
	public BuildingArea(int startAreaWidth, int startAreaHeight, int startDifficulty, boolean startChangeSpeed, int initVelocity){
		initialVelocity = initVelocity;
		areaWidth = startAreaWidth;
		areaHeight = startAreaHeight;
		allBlocks = new ArrayList<Block>();
		//add moving block, set speed and move one position up
		allBlocks.add(new Block(0,areaHeight));
		movingBlock().setXVel(initialVelocity);
		allBlocks.get(0).raiseBlock();
		//add bottom stationary block
		allBlocks.add(0, new Block(areaWidth/2 - movingBlock().getWidth()/2, areaHeight));
		difficulty = startDifficulty;
		perfectTolerance = 10;
		displayedBlocks = calcDisplayedBlocks(areaHeight, movingBlock().getHeight());
		messageCode = null;
		if (startChangeSpeed) changeSpeed = 1;
		else changeSpeed = 0;
	}
	
	public boolean step(Duration step, Duration clickCorrection){
		boolean running = true;
		if (clickCorrection != null) {
			advanceBlock(clickCorrection);
			running = this.hasClicked();
			step = step.minus(clickCorrection);
		}
		else advanceBlock(step);
		return running;
 	}
	

	private void advanceBlock (Duration step){
		//change direction if hit side wall
		if (movingBlock().getXPos() + movingBlock().getWidth() >= areaWidth) {
			movingBlock().setBackward();
		}
		else if (movingBlock().getXPos() < 0) {
			movingBlock().setForward();
		}
		
		//step moving block onwards
		movingBlock().stepBlock(step);
		if (changeSpeed > 0 && movingBlock().getXVel() > 100) {
			if (changeSpeed > 1) {
				movingBlock().speedUp(-2);
				changeSpeed = 1;
			}
			else changeSpeed ++;
		}
	}
	
	private boolean hasClicked() {
		boolean running = checkPositionAllowed();
		if (!running){
			//set velocity of moving block to 0 and add no more blocks
			movingBlock().setXVel(0);
			return running;
		}
		
		//add new block to be the moving block and make it stationary
		allBlocks.add(new Block(movingBlock().getXPos(), movingBlock().getYPosBtm()));
		initialVelocity += 10*difficulty;
		movingBlock().setXVel(initialVelocity);
		allBlocks.get(allBlocks.size()-2).setXVel(0);
		movingBlock().setWidth(allBlocks.get(allBlocks.size()-2).getWidth());
		
		movingBlock().raiseBlock();
		movingBlock().randomXPos(areaWidth);
		movingBlock().randomDirection();
		return running;
	}
	
	private boolean checkPositionAllowed(){
		//calculate offset between moving block and the block beneath it
		int diff = allBlocks.get(allBlocks.size()-2).getXPos() - movingBlock().getXPos();
		int absDiff = Math.abs(diff);
		if (absDiff < movingBlock().getWidth()){
			if (absDiff > perfectTolerance) {
				movingBlock().reduceWidth(absDiff);
				if (diff > 0) movingBlock().incrementXPos(diff);
			}
			else {
				int xBelow = allBlocks.get(allBlocks.size()-2).getXPos();
				movingBlock().setXPos(xBelow);
				messageCode = MessageE.PERFECT;
			}
			return true;
		}
		else return false;
	}
		
	public void youWin(){
		messageCode = MessageE.YOUWIN;
	}
	
	public void endGame(){
		messageCode = MessageE.GAMEOVER;
	}
	
	private Block movingBlock(){
		return allBlocks.get(allBlocks.size()-1);
	}
	
	public int getXMovingBlock(){
		return movingBlock().getXPos();
	}
	
	public int getYMovingBlock(){
		return movingBlock().getYPos();
	}
	
	public void switchDirection(Block block) {
		block.setXVel(-block.getXVel());
	}
	
	public int getNumBlocks() {
		return allBlocks.size();
	}
	
	public ArrayList<Block> getBlocks() {
		return allBlocks;
	}

	
	//Display functions
	public int getDisplayedBlocksNum() {
		return displayedBlocks;
	}
	
	public int getScoreX(){
		return areaWidth - 60;
	}
	
	public int getScoreY(){
		return 20;
	}
	
	private int calcDisplayedBlocks(int windowHeight, int height) {
		return (int) Math.round(0.6*windowHeight/height);
	}
	
	public MessageE getMessageCode() {
		return messageCode;
	}
	
	public void setMessageCodeNull(){
		messageCode = null;
	}
	
	public int getAreaWidth(){
		return areaWidth;
	}
	
	public int getWindowHeight(){
		return areaHeight;
	}
	
	public int getXTopStationary() {
		return allBlocks.get(allBlocks.size() - 2).getXPos();
	}

	public int pretendStep(Duration step) {
		return movingBlock().calculateStep(step);
	}

	public int getBlockDirection() {
		return movingBlock().getXVel()/Math.abs(movingBlock().getXVel());
	}
	
	public int getXVel() {
		return movingBlock().getXVel();
	}
	
	public int getGapBetweenLayers() {
		return movingBlock().gapBetweenLayers;
	}
	
	public int getScore(){
		return allBlocks.size();
	}
	
	
	private int initialVelocity;
	private int changeSpeed;
	private ArrayList<Block> allBlocks;
	private final int areaWidth;
	private final int areaHeight;
	private int difficulty;
	private int displayedBlocks;
	private int perfectTolerance;
	private MessageE messageCode;
}
