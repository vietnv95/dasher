package com.phamkhanh.object;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import com.phamkhanh.exception.MapErrorException;
import com.phamkhanh.image.ImageLoader;
import com.phamkhanh.mapengine.MapEngine;

public class Consumer extends Cell {
	private Color color;

	public Consumer() {
	}

	public Consumer(Point ptMap, Color color) {
		super(ptMap);
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public boolean check(Point pt){
		return (pt.x == this.getPtMap().x) && (pt.y == this.getPtMap().y);
	}
	@Override
	public void draw(Graphics g) {
		Point ptTile = MapEngine.tilePlotter(getPtMap());
		g.drawImage(ImageLoader.getImage("consumer.png"), ptTile.x, ptTile.y,
				null);
	}

	@Override
	public String toString() {
		return "Consumer [color=" + color + "," + super.toString() + "]";
	}

	@Override
	public String getProperty() {
		StringBuilder result = new StringBuilder();
		result.append("5");
		return result.toString();
	}

	public static Cell getInstance(String property, Point ptMap)
			throws MapErrorException {
		return new Consumer(ptMap,null);	
	}

}
