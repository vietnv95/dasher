package com.phamkhanh.object;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import com.phamkhanh.exception.MapErrorException;
import com.phamkhanh.image.ImageLoader;
import com.phamkhanh.mapengine.Direction;
import com.phamkhanh.mapengine.MapEngine;

public class Controller extends Cell{
	private ArrayList<Direction> directions; // Mang da sap xep huong theo thu
												// tu
	private int index; // Chi so cua cua huong hien tai trong mang
	private transient ArrayList<ActionListener> listeners = new ArrayList<>();

	/**
	 * true neu co box da chiem controller nay false neu nguoc lai
	 */
	private boolean lock;

	public Controller(Point ptMap, int index, ArrayList<Direction> directions) {
		super(ptMap);
		this.index = index;
		this.directions = directions;
		lock = false;
	}

	public Direction getDirection(){
		return directions.get(index);
	}
	
	public ArrayList<Direction> getDirections() {
		return directions;
	}
	
	public void setDirections(ArrayList<Direction> directions) {
		this.directions = directions;
	}


	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}



	public boolean isLock() {
		return lock;
	}

	public void setLock(boolean lock) {
		this.lock = lock;
	}

	public void nextDirection() {
		this.index = (this.index + 1) % directions.size();
	}

	@Override
	public void draw(Graphics g) {
		Point ptTile = MapEngine.tilePlotter(getPtMap());
		if (directions.get(index) != null) {
			g.drawImage(getImage(directions.get(index)), ptTile.x - 8,
					ptTile.y - 4, null);
		} else {
			g.drawImage(getImage(directions.get(0)), ptTile.x - 8,
					ptTile.y - 4, null);
		}
	}

	public BufferedImage getImage(Direction direction) {
		String imgName = "";
		switch (direction) {
		case SOUTHEAST:
			imgName = "se";
			break;
		case SOUTHWEST:
			imgName = "sw";
			break;
		case NORTHEAST:
			imgName = "ne";
			break;
		case NORTHWEST:
			imgName = "nw";
			break;
		default:
			break;
		}
		if (directions.size() == 1) {
			imgName += "1.png";
		} else {
			imgName += ".png";
		}
		return ImageLoader.getImage(imgName);
	}

	public synchronized void addActionListener(ActionListener listener) {
		listeners.add(listener);
	}

	public synchronized void removeActionListener(ActionListener listener) {
		listeners.remove(listener);
	}

	public synchronized void fireActionEvent() {
		ActionEvent actionEvent = new ActionEvent(this, 1, "click");
		for (ActionListener listener : listeners) {
			listener.actionPerformed(actionEvent);
		}
	}

	@Override
	public String getProperty() {
		int sumOfDirect = 0;
		for (Direction direct : directions) {
			sumOfDirect += direct.getValue();
		}
		return "3." + index + "-" + sumOfDirect;
	}

	public static Cell getInstance(String property, Point ptMap)
			throws MapErrorException {
		try {
			StringTokenizer tokens = new StringTokenizer(property, "-");
			int index = Integer.parseInt(tokens.nextToken());
			int sumOfDirect = Integer.parseInt(tokens.nextToken());

			ArrayList<Direction> directions = new ArrayList<>();
			for (Direction direct : Direction.getDirections()) {
				if ((sumOfDirect & direct.getValue()) == direct.getValue()) {
					directions.add(direct);
				}
			}
			return new Controller(ptMap, index, directions);
		} catch (NoSuchElementException | NumberFormatException e) {
			throw new MapErrorException(
					"Thuộc tính của Controller không đúng định dạng");
		}
	}

	@Override
	public String toString() {
		return "Controller [directions=" + directions + ", " + super.toString()
				+ "]";
	}

}
