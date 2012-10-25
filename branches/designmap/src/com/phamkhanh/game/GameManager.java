package com.phamkhanh.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Queue;

import javax.swing.JPanel;

import com.phamkhanh.exception.MapErrorException;
import com.phamkhanh.image.ImageLoader;
import com.phamkhanh.mapengine.MapEngine;
import com.phamkhanh.object.Box;
import com.phamkhanh.object.Cell;
import com.phamkhanh.object.Consumer;
import com.phamkhanh.object.Controller;
import com.phamkhanh.object.Map;
import com.phamkhanh.object.ObjectPlayer;
import com.phamkhanh.object.Producer;

public class GameManager extends JPanel implements Runnable {
	
	private static final int PWIDTH = 976; // size of panel
	private static final int PHEIGHT = 488;

	private Thread animator; // for animation
	private volatile boolean running = false; // stops animation
	private volatile boolean gameOver = false; // for game termination
	private volatile boolean isPaused = false;

	
	private int period = 1000000000 / MapEngine.FPS; // ns A iteration time as 100 FPS.Khoang 12.5ms mot vong lap game

	// Global variables for off-screen rendering
	private Graphics dbg;
	private Image dbImage = null;

	// Background image
	private BufferedImage bgImage = null;
	// Map element
	private Map map;
	private GameState gameState;
	
	private Producer producer;
	private ArrayList<Consumer> consumers;
	private ArrayList<Box> boxes;
	private Queue<Box> queueBox;
	
	public GameManager() {
		setDoubleBuffered(false);
	    setBackground(Color.black);
		setPreferredSize(new Dimension(PWIDTH, PHEIGHT));

		setFocusable(true);
		requestFocus(); // Jpanel now receives key events
		readyForTermination();

		// Load background image
		bgImage = ImageLoader.loadImage("background.jpg");
		map = new Map();
		
		try {
			map.load("resources/data/maps/level1/vidu.txt");
		} catch (MapErrorException e) {
			e.printStackTrace();
		}
		
		// Them su kien cho cac Controller
		for(int y = 0; y < Map.MAPHEIGHT; y++){
			for(int x = 0; x < Map.MAPWIDTH; x++){
				Cell cell = map.getCell(x, y);
				if(cell != null && cell.getClass() == Controller.class){
					Controller controller = ((Controller)cell);
					controller.addActionListener(new ActionListener() {		
						@Override
						public void actionPerformed(ActionEvent e) {
							((Controller)e.getSource()).nextDirection();
						}
					});
				}
			}
		}

		// listen for mouse presses
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				testPressed(e.getX(), e.getY());
			}

			@Override
			public void mouseClicked(MouseEvent e) {	
				Point ptMap = MapEngine.mouseMap(e.getPoint());
				Cell cell = map.getCell(ptMap);
				if(cell != null && cell.getClass() == Controller.class){
					((Controller)cell).fireActionEvent();
					System.out.println(cell);
				}
			}
			
		});
	}

	// Listen for ESC, Q, End, Ctrl-C
	private void readyForTermination() {
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int keyCode = e.getKeyCode();
				if (keyCode == KeyEvent.VK_ESCAPE || keyCode == KeyEvent.VK_END
						|| keyCode == KeyEvent.VK_Q
						|| (keyCode == KeyEvent.VK_C && e.isControlDown())) {
					running = false;
				}
			}
		});
	}

	protected void testPressed(int x, int y) {
		if (!isPaused && !gameOver) {
			
		}
	}

	/*
	 * Wait for the JPanel to be added to the JFrame/JApplet before starting
	 */
	public void addNotify() {
		super.addNotify();
		startGame();
	}

	/* Initially and start the Thread. */
	private void startGame() {
		if (animator == null || !running) {
			animator = new Thread(this);
			animator.start();
		}
	}

	/* Called by the user to stop execution. */
	public void stopGame() {
		running = false;
	}

	// Number of frames with a delay of 0ms before
	// the animation thread yields to other running threads
	private static final int NO_DELAYS_PER_YIELD = 16;

	// no. of frames that can be skipped in any one animation loop
	// game updated but not rendered
	private static int MAX_FRAME_SKIPS = 5;

	@Override
	/* Repeatedly update,render,sleep. */
	public void run() {
		long beforeTime, afterTime, timeDiff, sleepTime;   // ns
		long overSleepTime = 0L;   // ns
		int noDelays = 0;
		long excess = 0L;  // ns

		beforeTime = System.nanoTime();

		running = true;
		while (running) {
			gameUpdate(); // game state is updated
			gameRender(); // render to a buffer
			paintScreen(); // draw buffer to screen
			
			afterTime = System.nanoTime();
			timeDiff = afterTime - beforeTime;
			sleepTime = (period - timeDiff) - overSleepTime;

			// some time left in this period
			if (sleepTime > 0) {
				try {
					Thread.sleep(sleepTime / 1000000L); // sleep a bit
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				// the actually sleep < sleepTime,=> exist overSleepTime
				overSleepTime = (System.nanoTime() - afterTime) - sleepTime;
			} else { // sleepTime <= 0, frame took longer than the period
				excess -= sleepTime; // store excess time value
				overSleepTime = 0L;
				noDelays++;
				if (noDelays >= NO_DELAYS_PER_YIELD) {
					Thread.yield(); // give another thread a chance to run
					noDelays = 0;
				}
			}

			beforeTime = System.nanoTime();

			/*
			 * If frame animation is taking too long, update the game state
			 * without rendering to get the updates/sec nearer to the required
			 * FPS
			 */
			
			int skips = 0;
			while ((excess > period) && (skips < MAX_FRAME_SKIPS)) {
				excess -= period;
				gameUpdate();
				skips++;
			}
		}
		System.exit(0);
	}

	// Update game state
	private void gameUpdate() {
		if (!isPaused && !gameOver) {
			ObjectPlayer.getInstance().updateStick();
		}
	}

	// draw the current frame to an image buffer (secondary image) use graphics of image
	// size of image buffer == size of screen
	private void gameRender() {
		if (dbImage == null) {
			dbImage = createImage(PWIDTH, PHEIGHT);
			if (dbImage == null) {
				System.out.println("dbImage is null");
				return;
			} else {
				dbg = dbImage.getGraphics();
			}
		}

		// Clear the background
		dbg.setColor(Color.white);
		dbg.fillRect(0, 0, PWIDTH, PHEIGHT);

		// draw background image
		dbg.drawImage(bgImage, 0, 0, null);
		map.draw(dbg);
		
		if (gameOver) {
			gameOverMessage(dbg);
		}
	}

	// Display center gameOver-message
	private void gameOverMessage(Graphics dbg) {
		// Code to calculate x and y ...
		dbg.drawString("Game Over", PWIDTH / 2, PHEIGHT / 2);
	}

	// actively render the buffer image to the screen size (PWITH, PHEIGHT)
	private void paintScreen() {
		Graphics g;
		try {
			g = this.getGraphics();
			if (g != null && dbImage != null) {
				g.drawImage(dbImage, 0, 0, null);
			}
			Toolkit.getDefaultToolkit().sync();
			g.dispose();
		} catch (Exception e) {
			System.out.println("Graphics context error: " + e);
		}
	}

	public void pauseGame() {
		isPaused = true;
	}

	public void resumeGame() {
		isPaused = false;
	}

	// More methods,explained later..

}
