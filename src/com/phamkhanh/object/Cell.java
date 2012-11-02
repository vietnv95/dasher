package com.phamkhanh.object;

import java.awt.Graphics2D;
import java.awt.Point;
import java.io.Serializable;

import com.phamkhanh.exception.MapErrorException;
import com.phamkhanh.mapengine.MapEngine;

public class Cell implements Serializable {
	private Point ptMap;
	
	private static boolean grid = true;

	public Cell() {
	}

	public Cell(Point ptMap) {
		this.ptMap = ptMap;
	}

	public Point getPtMap() {
		return ptMap;
	}

	public void setPtMap(Point ptMap) {
		this.ptMap = ptMap;
	}
	
	public static boolean isGrid() {
		return grid;
	}

	public static void setGrid(boolean grid) {
		Cell.grid = grid;
	}

	public void draw(Graphics2D g) {
		if(grid){
			Point ptTile = MapEngine.tilePlotter(ptMap);
			int x = ptTile.x;
			int y = ptTile.y;
			int w = MapEngine.TILEWIDTH;
			int h = MapEngine.TILEHEIGHT;
			g.drawPolygon(new int[] { x + w / 2, x + w, x + w / 2, x }, new int[] {
					y, y + h / 2, y + h, y + h / 2 }, 4);
		}
	}

	public String getProperty() {
		return "0";
	}

	public static Object getInstance(String property, Point ptMap)
			throws MapErrorException {
		return new Cell(ptMap);
	}
	
	@Override
	public String toString() {
		return "Cell [ptMap=" + ptMap + "]";
	}
}
