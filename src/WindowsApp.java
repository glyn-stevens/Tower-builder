import java.awt.Dimension;
import java.time.Duration;
import java.util.ArrayList;

import core.Game;
import ui.windows.Renderer;

public class WindowsApp {

	public static void main(String[] args) {
		WindowsApp windowsApp = new WindowsApp();
		windowsApp.runGame();
		
	}	
	
	public WindowsApp() {
		windowWidth = 600;
		windowHeight = 300;
	}
	
	private void runGame(){
		boolean running = true;
		final Duration step = Duration.ofMillis(50); // 50hz
		Game game = new Game(windowWidth, windowHeight, 1, true);
		Renderer renderer = new Renderer(new Dimension (windowWidth, windowHeight), game);
		renderer.render(game);
		Duration clickCorrectDur = null;
		while(running) {
			try {
				long start = System.nanoTime();
				running = game.step(step, clickCorrectDur);
				clickCorrectDur = null;
				renderer.render(game);
				long end = System.nanoTime();
				Duration loopTime = Duration.ofNanos(end-start);
				Duration sleepTime = step.minus(loopTime);
				if (sleepTime.toMillis() > 0)
					Thread.sleep(sleepTime.toMillis());
				if (renderer.isClicked() != null){
					clickCorrectDur = Duration.ofNanos(renderer.isClicked() - start);
				}
				renderer.clearClicked();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private int windowWidth;
	private int windowHeight;
}

