package com.phamkhanh.object;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import com.phamkhanh.mapengine.MapEngine;

public class Tile extends Cell {

	private BufferedImage image;

	public Tile() {
	}

	public Tile(Point ptMap, BufferedImage image) {
		super(ptMap);
		this.image = image;
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public void draw(Graphics g) {
		Point ptTile = MapEngine.tilePlotter(getPtMap());
		g.drawImage(image, ptTile.x, ptTile.y, null);
	}

	@Override
	public String toString() {
		return "Tile [" + super.toString() + "]";
	}

	@Override
	public String getProperty() {
		return "1";
	}

}
