package com.phamkhanh.object;

import java.awt.Graphics;
import java.awt.Point;

import com.phamkhanh.exception.MapErrorException;
import com.phamkhanh.mapengine.MapEngine;

public class Cell {
	private Point ptMap;

	public Cell() {

	}

	public Cell(Point ptMap) {
		super();
		this.ptMap = ptMap;
	}

	public Point getPtMap() {
		return ptMap;
	}

	public void setPtMap(Point ptMap) {
		this.ptMap = ptMap;
	}

	public void draw(Graphics g) {
		Point ptTile = MapEngine.tilePlotter(ptMap);
		int x = ptTile.x;
		int y = ptTile.y;
		int w = MapEngine.TILEWIDTH;
		int h = MapEngine.TILEHEIGHT;
		g.drawPolygon(new int[] { x + w / 2, x + w, x + w / 2, x }, new int[] {
				y, y + h / 2, y + h, y + h / 2 }, 4);
	}

	@Override
	public String toString() {
		return "Cell [ptMap=" + ptMap + "]";
	}

	public String getProperty() {
		return "0";
	}

	public static Object getInstance(String property, Point ptMap)
			throws MapErrorException {
		return new Cell(ptMap);
	}

}
