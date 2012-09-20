package com.phamkhanh.mapdesign;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import com.phamkhanh.image.ImageLoader;
import com.phamkhanh.mapengine.MapEngine;
import com.phamkhanh.object.Map;
import com.phamkhanh.object.Tile;

public class DesignPanel extends JPanel implements Runnable {
	
	
	private static final int PWIDTH = 976; // size of panel
	private static final int PHEIGHT = 488;

	private Thread animator; // for animation
	private volatile boolean running = false; // stops animation
	private volatile boolean designEnd = false; // for game termination
	private volatile boolean isPaused = false;

	
	private int period = 1000000000 / MapEngine.FPS; // ns A iteration time as 100 FPS.Khoang 12.5ms mot vong lap game

	// Global variables for off-screen rendering
	private Graphics dbg;
	private Image dbImage = null;

	// Background image
	private BufferedImage bgImage = null;
	// Map element
	private Map map;
	// 
	private Tile tileSelected;
	public volatile int x;
	public volatile int y;
	
	private boolean isDragged;
	private Point ptHead;
	private Point ptTail;
	
	
	// More variables, explained later
	//
	
	public Tile getTileSelected() {
		return tileSelected;
	}

	public void setTileSelected(Tile tileSelected) {
		this.tileSelected = tileSelected;
	}

	public DesignPanel() {
		
		
		setDoubleBuffered(false);
	    setBackground(Color.black);
		setPreferredSize(new Dimension(PWIDTH, PHEIGHT));

		setFocusable(true);
		requestFocus(); // Jpanel now receives key events
		readyForTermination();

		// create game components
		//
		
		// Load background image
		bgImage = ImageLoader.loadImage("background.jpg");
		
		map = new Map();
		
		tileSelected = null;

		// Listen for mouse event
		MouseHandler listener = new MouseHandler(this);
		addMouseListener(listener);
		addMouseMotionListener(listener);
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
		if (!isPaused && !designEnd) {
			// do something
		}
	}

	/*
	 * Wait for the JPanel to be added to the JFrame/JApplet before starting
	 */
	public void addNotify() {
		super.addNotify();
		startDesign();
	}

	/* Initially and start the Thread. */
	private void startDesign() {
		if (animator == null || !running) {
			animator = new Thread(this);
			animator.start();
		}
	}

	/* Called by the user to stop execution. */
	public void stopDesign() {
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
			designUpdate(); // game state is updated
			designRender(); // render to a buffer
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
				designUpdate();
				skips++;
			}
		}
		System.exit(0);
	}

	// Update game state
	private void designUpdate() {
		if (!isPaused && !designEnd) {
			
		}
	}

	// draw the current frame to an image buffer (secondary image) use graphics of image
	// size of image buffer == size of screen
	private Point ptMouse = new Point();
	private void designRender() {
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

		// Draw game elements
		// ..
		
		// draw background image
		dbg.drawImage(bgImage, 0, 0, null);
		
		// Draw map and its element
		map.draw(dbg);
		
		// Draw tileSelected at toa do chuot,sao cho chuot nam o tam cua tileSelected
		if(tileSelected != null){
			ptMouse.setLocation(x, y);
			tileSelected.setPtMap(MapEngine.mouseMap(ptMouse));
			tileSelected.draw(dbg);
		}
		
		if (designEnd) {
			
		}
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

	public void pauseDesign() {
		isPaused = true;
	}

	public void resumeDesign() {
		isPaused = false;
	}

	// More methods,explained later..

}
