package ui.windows;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import core.BuildingArea;
import core.Game;

public class Renderer implements MouseListener{
	public Renderer(Dimension windowDim, Game state) {
        //Create and set up the window.
        frame = new JFrame("Tappy Tower");
        frame.setLayout(new FlowLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvases = new Vector<GameCanvas>();
        windowDim.setSize(windowDim.getWidth()/state.getNumBuildingAreas(), windowDim.getHeight());
        for (int i = 0; i < state.getNumBuildingAreas(); i++){
        	canvases.add(new GameCanvas(state.getBuildingArea(i)));
        	canvases.get(i).setPreferredSize(windowDim);
        	frame.getContentPane().add(canvases.get(i));
        }
        maxScrollVel = 5;
        canvases.get(0).addMouseListener(this);
        
        //Display the window.
        frame.pack();
        frame.setVisible(true);
        lowestBlockToDisplay = 0;
	}

	public void render(Game state){
		int score0 = state.getBuildingArea(0).getScore();
		int score1 = state.getBuildingArea(1).getScore();
		int newLowestBlockToDisplay = (int)((score1 + score0 - state.numBlocksFitOnScreen)/2);
		int correction = 0;
		if(newLowestBlockToDisplay > 0) {
			smoothScrolling(newLowestBlockToDisplay, lowestBlockToDisplay, state.layerHeight);
			lowestBlockToDisplay = newLowestBlockToDisplay;
			correction = -(int) Math.ceil(yScrollAdjustement / state.layerHeight); 
		}
        for (int i = 0; i < canvases.size(); i++){
			canvases.get(i).update(state.getBuildingArea(i), lowestBlockToDisplay - correction, yScrollAdjustement);
			canvases.get(i).repaint();
        }
	}

	public void displayError(Exception ex) {
		ex.printStackTrace();
		
	}
	
	private void smoothScrolling(int newTargetLowestBlock, int oldTargetLowestBlock, int layerHeight){
		int oldYTarget = oldTargetLowestBlock * layerHeight;
		int newYTarget = newTargetLowestBlock * layerHeight;
		int oldYpos = oldYTarget + yScrollAdjustement;
		int difference = newYTarget - oldYpos;
		if (difference > maxScrollVel) {
			yScrollAdjustement = Math.abs(difference) - maxScrollVel;
		}
		else {
			yScrollAdjustement = difference;
		}
		if (difference > 0) {
			yScrollAdjustement = -yScrollAdjustement;
		}
	}
	
	private int yScrollAdjustement;
	private int lowestBlockToDisplay;
	private final int maxScrollVel;
	private JFrame frame;
	private Long clicked;
	private Vector<GameCanvas> canvases;
	private int xScrollAdjustment;
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		clicked = System.nanoTime();
	}

	public Long isClicked() {
		return clicked;
	}

	public void clearClicked() {
		clicked = null;
	}
}
