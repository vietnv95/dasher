package com.phamkhanh.object;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

import com.phamkhanh.mapengine.Direction;
import com.phamkhanh.mapengine.MapEngine;

public class Consumer extends Conveyer {
	private Color color;

	public Consumer() {

	}

	public Consumer(Point ptMap, BufferedImage image, Direction direction,
			Color color) {
		super(ptMap, image, direction);
		this.color = color;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public void draw(Graphics g) {
		Point ptTile = MapEngine.tilePlotter(getPtMap());
		g.drawImage(getImage(), ptTile.x, ptTile.y, null);
	}

	@Override
	public String toString() {
		return "Consumer [color=" + color + "," + super.toString()
				+ "]";
	}

}
