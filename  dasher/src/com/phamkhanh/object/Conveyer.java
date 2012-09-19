package com.phamkhanh.object;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

import com.phamkhanh.mapengine.Direction;
import com.phamkhanh.mapengine.MapEngine;

public class Conveyer extends Tile {

	private Direction direction;

	public Conveyer() {

	}

	public Conveyer(Point ptMap, BufferedImage image, Direction direction) {
		super(ptMap, image);
		this.direction = direction;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	@Override
	public void draw(Graphics g) {
		Point ptTile = MapEngine.tilePlotter(getPtMap());
		g.drawImage(getImage(), ptTile.x - 4, ptTile.y, null);
	}

	@Override
	public String toString() {
		return "Conveyer [direction=" + direction + ", toString()="
				+ super.toString() + "]";
	}

}
