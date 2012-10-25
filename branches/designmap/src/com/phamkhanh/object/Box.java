package com.phamkhanh.object;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

import com.phamkhanh.image.ImageLoader;
import com.phamkhanh.mapengine.Direction;

public class Box extends Sprite {
	private Color color;
	
	public Box(Map map, Point ptMap, Direction direction, int speed, Color color) {
		super(map, ptMap, direction, speed);
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
		int x = getPtMap().x;
		int y = getPtMap().y;
		// update offset box
		
		
		
		g.drawImage(getImage(),x , y, null);
	}
	
	private BufferedImage getImage(){
		if(color == Color.red) return ImageLoader.getImage("box1.png");
		if(color == Color.blue) return ImageLoader.getImage("box2.png");
		if(color == Color.blue) return ImageLoader.getImage("box2.png");
		else return ImageLoader.getImage("box3.png");
	}




	
	
}
