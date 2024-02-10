package ui.windows;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import core.Block;
import core.Game;
import core.MessageE;
import core.BuildingArea;

public class GameCanvas extends JPanel {
	public GameCanvas(BuildingArea state) {
		scoreX = state.getAreaWidth() - 60;
		scoreY = 20;
		messageX = state.getAreaWidth()/2 - 20;
		messageY = (int) (state.getWindowHeight()*0.3);
		messageCount = 0;
		/*	
		try {                
			image = resize(ImageIO.read(new File("sam.jpg")), 50,50);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		*/
		 
	}
	public void update(BuildingArea state, int lowestBlockToDisplay, int xScrollAdjustement){
		//transfer blocks positions etc to GameCanvas
		allBlocks = state.getBlocks();
		lowestDisplayedBlock = lowestBlockToDisplay;
		scrollAdjustment = xScrollAdjustement;
		if (state.getMessageCode() != null){
			messageCount = 6;
			messageCode = state.getMessageCode();
			state.setMessageCodeNull();
		}
	}
	
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		int numBlocks = allBlocks.size();
		displayBlocks(numBlocks, g);
		displayScore(numBlocks, g);
		displayMessages(g);
	}
	
	private void displayBlocks(int numBlocks, Graphics g){
		int x;
		int y;
		int wSml;
		int h = allBlocks.get(0).getHeight();
		int vertGap = allBlocks.get(0).getYPos() - allBlocks.get(1).getYPos();
		int start = lowestDisplayedBlock;
		for(int i = start; i < numBlocks; i++){
			x = allBlocks.get(i).getXPos();
			y = allBlocks.get(i).getYPos() + vertGap * start + scrollAdjustment;
			wSml = allBlocks.get(i).getSmallBlockWidth();
			//g.drawImage(image, x, y, this);
			g.fillRect(x, y, wSml, h);			
			
		}
		g.fillRect(0, 295, 10, 10);
	}
	
	private void displayScore(int numBlocks, Graphics g){
		score = Integer.toString(numBlocks - 2);
		g.drawString("Score: " + score, scoreX, scoreY);
		
	}
	
	private void displayMessages(Graphics g){
		if (messageCount > 0){
			if (messageCode != null) showMessage(g);
			messageCount--;
		}
	}
	
	public static BufferedImage resize(BufferedImage img, int newW, int newH) { 
	    Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
	    BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2d = dimg.createGraphics();
	    g2d.drawImage(tmp, 0, 0, null);
	    g2d.dispose();
	    return dimg;
	}  
	
	public void showMessage(Graphics g) {
        switch (messageCode) {
            case PERFECT:
            	g.drawString("PERFECT", messageX, messageY);
                break;
                
            case GAMEOVER:
            	g.drawString("LOSERRR", messageX, messageY);
                break;
                
            case YOUWIN:
            	g.drawString("YOU WIN", messageX, messageY);
                break;
                
            default:
            	g.drawString("WTF ERROR", messageX, messageY);
            	break;
             
        }
    }
	
	private int scrollAdjustment;
	private int messageCount;
	private int messageX;
	private int messageY;
	private int scoreX;
	private int scoreY;
	private String score;
	private BufferedImage image;
	private ArrayList<Block> allBlocks;
	private int lowestDisplayedBlock;
	private MessageE messageCode;

}
