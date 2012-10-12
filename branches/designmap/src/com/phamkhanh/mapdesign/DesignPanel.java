package com.phamkhanh.mapdesign;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

import com.phamkhanh.exception.SaveNotSuccessException;
import com.phamkhanh.image.ImageLoader;
import com.phamkhanh.mapdesign.command.HistoryCommand;
import com.phamkhanh.mapengine.Direction;
import com.phamkhanh.mapengine.MapEngine;
import com.phamkhanh.object.Cell;
import com.phamkhanh.object.Conveyer;
import com.phamkhanh.object.ObjectPlayer;
import com.phamkhanh.object.Map;

public class DesignPanel extends JPanel implements Runnable {

	private static final int PWIDTH = 976; // size of panel
	private static final int PHEIGHT = 488;

	private Thread animator; // for animation
	private volatile boolean running = false; // stops animation
	private volatile boolean designEnd = false; // for game termination
	private volatile boolean isPaused = false;

	// Time in ns to Run a Iteration Game
	private int period = 1000000000 / MapEngine.FPS;

	// Global variables for off-screen rendering
	private Graphics dbg;
	private Image dbImage = null;

	// History List
	private HistoryCommand history;

	// Background Image
	private BufferedImage bgImage = null;

	// Design Element
	private Map map;
	private Cell tileSelected;

	// Design State
	public volatile boolean isPressed;
	public volatile boolean isDragged;
	public Point ptMouse; // Currently Mouse Coordinate in Pixel (Always Update
							// by mouseMoved Event)
	public Point ptHeadPixel; // Mouse Coordinate in Pixel When Begin Pressing
								// and Dragging Mouse
	public Point ptTailPixel; // Mouse Coordinate in Pixel When End Dragging and
		                      // begin Releasing Mouse

	
	public Map getMap() {
		return map;
	}

	public HistoryCommand getHistory() {
		return history;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public Cell getTileSelected() {
		return tileSelected;
	}

	public void setTileSelected(Cell tileSelected) {
		this.tileSelected = tileSelected;
	}

	public DesignPanel() {

		setDoubleBuffered(false);
		setBackground(Color.black);
		setPreferredSize(new Dimension(PWIDTH, PHEIGHT));

		setFocusable(true);
		requestFocus(); // Jpanel now receives key events

		history = new HistoryCommand();

		// Initialize Background Image
		bgImage = ImageLoader.loadImage("background.jpg");

		// Initialize Design Elements (Changeable Through Commands)
		map = new Map();
		tileSelected = null;

		// Initialize Design State
		isPressed = false;
		isDragged = false;
		ptMouse = new Point(-100, -100);
		ptHeadPixel = new Point(-100, -100);
		ptTailPixel = new Point(-100, -100);

		// Listen Mouse Event
		MouseHandler mouseHandler = new MouseHandler(this);
		addMouseListener(mouseHandler);
		addMouseMotionListener(mouseHandler);
		
		// Listen Key Event
		KeyHandler keyHandler = new KeyHandler(this);
		addKeyListener(keyHandler);
	}


	// Wait for the JPanel to be added to the JFrame/JApplet before starting
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
		long beforeTime, afterTime, timeDiff, sleepTime; // ns
		long overSleepTime = 0L; // ns
		int noDelays = 0;
		long excess = 0L; // ns

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

	// Update Design state
	private void designUpdate() {
		if (!isPaused && !designEnd) {
			ObjectPlayer.getInstance().updateStick();
		}
	}

	// draw the current frame to an image buffer (secondary image) use graphics
	// of image
	// size of image buffer == size of screen
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

		// Draw background image
		dbg.drawImage(bgImage, 0, 0, null);

		// Draw map and its element
		map.draw(dbg);

		// Draw tileSelected at toa do chuot,sao cho chuot nam o tam cua
		// tileSelected
		if (tileSelected != null) {
			tileSelected.setPtMap(MapEngine.mouseMap(ptMouse));
			tileSelected.draw(dbg);
		}

		// Draw doan conveyer khi nguoi dung drag chuot dung huong
		if (tileSelected != null && tileSelected.getClass() == Conveyer.class) {
			if (isDragged) {
				Point ptMapHead = MapEngine.mouseMap(ptHeadPixel);
				Point ptMapTail = MapEngine.mouseMap(ptTailPixel);
				Direction direction = MapEngine.tileDirecter(ptMapHead,
						ptMapTail);
				if (direction == Direction.SOUTHEAST
						|| direction == Direction.SOUTHWEST) {
					do {
						tileSelected.setPtMap(ptMapHead);
						tileSelected.draw(dbg);
						if (ptMapHead.x == ptMapTail.x
								&& ptMapHead.y == ptMapTail.y)
							break;
						ptMapHead = MapEngine.tileWalker(ptMapHead, direction);
					} while (true);
				}else if(direction == Direction.NORTHEAST || direction == Direction.NORTHWEST){
					do {
						tileSelected.setPtMap(ptMapTail);
						tileSelected.draw(dbg);
						if (ptMapHead.x == ptMapTail.x
								&& ptMapHead.y == ptMapTail.y)
							break;
						ptMapTail = MapEngine.tileWalker(ptMapTail, MapEngine.getDirection(direction, MapEngine.BACK));
					} while (true);
				}
			}
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
	
	public void saveMap(){
		pauseDesign();
		try {
			this.map.save("resources/data/maps/vidu.txt");
			System.out.println("Thanh cong");
		} catch (SaveNotSuccessException e) {
			e.printStackTrace();
			System.out.println("Khong thanh cong, khong luu duoc map, thu lai");
			resumeDesign();
		}
	}

	@Override
	public String toString() {
		return "DesignPanel [map=" + map + ", tileSelected=" + tileSelected
				+ ", isPressed=" + isPressed + ", isDragged=" + isDragged
				+ ", ptMouse=" + ptMouse + ", ptHead=" + ptHeadPixel
				+ ", ptTail=" + ptTailPixel + "]";
	}
}
