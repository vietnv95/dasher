package com.phamkhanh.object;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import com.phamkhanh.exception.MapErrorException;
import com.phamkhanh.image.ImageLoader;
import com.phamkhanh.mapengine.Direction;
import com.phamkhanh.mapengine.MapEngine;

public class Producer extends Conveyer {


	public Producer() {
			
	}

	public Producer(Point ptMap, Direction direction) {
		super(ptMap, direction);
		

	}
	
	/*
	 * return a Box with ptMap, direction speed, color
	 */
	
	public Box genBox(Map map,Point ptMap, Direction direction, int speed, Color color){
	
		return new Box(map,ptMap,direction,speed,color);
	}

	@Override
	public void draw(Graphics g) {
		Point ptTile = MapEngine.tilePlotter(getPtMap());
		g.drawImage(ImageLoader.getImage("producer.png"), ptTile.x, ptTile.y,
				null);
	}

	@Override
	public String toString() {
		return super.toString();
	}

	@Override
	public String getProperty() {
		return "4." + getDirection().getValue();
	}

	public static Cell getInstance(String property, Point ptMap)
			throws MapErrorException {
		try {
			return new Producer(ptMap, Direction.getDirection(Integer
					.parseInt(property)));
		} catch (NumberFormatException e) {
			throw new MapErrorException(
					"Thuộc tính Producer không đúng định dạng");
		}
	}
}
