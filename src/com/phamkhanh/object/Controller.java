package com.phamkhanh.object;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.phamkhanh.mapengine.Direction;
import com.phamkhanh.mapengine.MapEngine;

public class Controller extends Conveyer {
	private ArrayList<Direction> directions;

	public Controller() {

	}

	public Controller(Point ptMap, BufferedImage image, Direction direction,
			ArrayList<Direction> directions) {
		super(ptMap, image, direction);
		this.directions = directions;
	}

	public ArrayList<Direction> getDirections() {
		return directions;
	}

	public void setDirections(ArrayList<Direction> directions) {
		this.directions = directions;
	}

	@Override
	public void draw(Graphics g) {
		Point ptTile = MapEngine.tilePlotter(getPtMap());
		g.drawImage(getImage(), ptTile.x - 8, ptTile.y - 4, null);
	}

	@Override
	public String toString() {
		return "Controller [directions=" + directions + ", toString()="
				+ super.toString() + "]";
	}
	
	

}
