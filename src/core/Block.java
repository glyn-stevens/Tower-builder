package core;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

public class Block {
	
	public Block(int x, int y) {
		xVel = 0;
		numSmallBlocks = 1;
		gapBetweenSmallBlocks = 2;
		smallBlockWidth = 90;
		height = 30;
		xPos = x;
		yPosBtm = y;
		gapBetweenLayers = 1;
	}
	
	
	
	public void stepBlock(Duration step){
		xPos = calculateStep(step);
	}
	
	public int calculateStep(Duration step){
		return (int)(xPos + xVel* step.toMillis()/1000);
	}
	
	public int getXPos(){
		return xPos;
	}
	
	public int getYPos(){
		return yPosBtm - height;
	}
	
	public int getYPosBtm(){
		return yPosBtm;
	}
	
	public int getXVel(){
		return xVel;
	}
	
	public int getWidth(){
		return smallBlockWidth * numSmallBlocks + gapBetweenSmallBlocks *(numSmallBlocks - 1);
	}
	
	public int getSmallBlockWidth(){
		return smallBlockWidth;
	}
	
	public static int getHeight(){
		return height;
	}
	
	public int getNumSmallBlocks(){
		return numSmallBlocks;
	}
	
	public int getGapBetweenSmallBlocks() {
		return gapBetweenSmallBlocks;
	}
	
	public void setXVel(int newVel){
		xVel = newVel;
	}
	
	public void setBackward() {
		xVel = -Math.abs(xVel);
	}
	
	public void setForward() {
		xVel = Math.abs(xVel);
	}
	
	public void setYPos(int newY){
		yPosBtm = newY;
	}
	
	public void setXPos(int newX) {
		xPos = newX;
	}

	public void incrementXPos(int inc) {
		xPos += inc;
	}

	public void raiseBlock() {
		yPosBtm -= height + gapBetweenLayers;
	}

	public void randomXPos(int windowWidth) {
		xPos = ThreadLocalRandom.current().nextInt(0, windowWidth - getWidth());
	}
	
	public void changeDirection() {
		xVel = - xVel;
	}
	
	public void randomDirection(){
		if(ThreadLocalRandom.current().nextInt(0,2) == 1) changeDirection();
	}

	public void speedUp(int increase) {
		if (xVel < 0) xVel -= increase;
		else xVel += increase; 
	}
	
	public void reduceWidth(int reduction) {
		smallBlockWidth -= Math.round(reduction / numSmallBlocks);

	}
	public void setWidth(int newWidth) {
		smallBlockWidth = Math.round(newWidth / numSmallBlocks);
		
	}
	
	public final int gapBetweenLayers;
	private int xPos;
	private int yPosBtm;
	private int xVel;
	static private int height;
	private int numSmallBlocks;
	static private int gapBetweenSmallBlocks;
	private int smallBlockWidth;
}
