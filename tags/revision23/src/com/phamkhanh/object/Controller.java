package com.phamkhanh.object;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.TreeSet;

import com.phamkhanh.image.ImageLoader;
import com.phamkhanh.mapengine.Direction;
import com.phamkhanh.mapengine.MapEngine;

public class Controller extends Conveyer {
	private TreeSet<Direction> directions;

	public Controller() {

	}

	public Controller(Point ptMap, BufferedImage image, Direction direction,
			TreeSet<Direction> directions) {
		super(ptMap, image, direction);
		this.directions = directions;
	}

	public TreeSet<Direction> getDirections() {
		return directions;
	}

	@Override
	public void draw(Graphics g) {
		Point ptTile = MapEngine.tilePlotter(getPtMap());
		if(getDirection() != null){
			g.drawImage(getImage(getDirection()), ptTile.x - 8, ptTile.y - 4, null);
		}else{
			g.drawImage(getImage(directions.first()), ptTile.x - 8, ptTile.y - 4, null);
		}
	}
	
	public BufferedImage getImage(Direction direction){
		String imgName = "";
		switch(direction){
		case SOUTHEAST : imgName = "se"; break;
		case SOUTHWEST : imgName = "sw"; break;
		case NORTHEAST : imgName = "ne"; break;
		case NORTHWEST : imgName = "nw"; break;
		default:
			break;
		}
		if(directions.size() == 1){
			imgName += "1.png";
		}else {
			imgName += ".png";
		}
		return ImageLoader.getImage(imgName);
	}

	@Override
	public String toString() {
		return "Controller [directions=" + directions + ", "+ super.toString() + "]";
	}
	
	

}
