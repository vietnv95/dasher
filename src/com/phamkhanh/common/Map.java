package com.phamkhanh.common;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

import com.phamkhanh.image.ImageLoader;
import com.phamkhanh.mapengine.Direction;
import com.phamkhanh.mapengine.MapEngine;
import com.phamkhanh.object.Sprite;

public class Map {
	// size of map by tile
	public static final int MAPWIDTH = 30;
	public static final int MAPHEIGHT = 60;
	
	// 2-dimension array tile map
	private int[][] tileMap = new int[MAPWIDTH][MAPHEIGHT];
	
	// there are 2 kind of tiles to create map
	private final int TILE1 = 1;
	private final int TREE = 3;
	
	//
	private Image tile1Image = null;
	private Image conveyer = null;
	private Image controller = null;
	private Sprite sprite;
	
	public Map(){
		for(int i = 0; i < MAPWIDTH; i++){
			for(int j = 0; j < MAPHEIGHT; j++){
				tileMap[i][j] = TILE1;
			}
		}
		
		tile1Image = ImageLoader.loadImage("tile1.png");
		conveyer = ImageLoader.loadImage("conveyer.png");
		controller = ImageLoader.loadImage("se.png");
		sprite = new Sprite(new Point(0,0), Direction.SOUTHEAST, 1, new Point(2,1), ImageLoader.loadImage("box2.png"));
	}
	
	public void draw(Graphics g){
		Point ptTile = new Point(); // tile position by pixel
		Point ptMap = new Point(); //tile position by map size (20*20)
		
		for(int y = 0; y < MAPHEIGHT; y++){
			for(int x = 0; x < MAPWIDTH; x++){
				ptMap.x = x;
				ptMap.y = y;
				ptTile = MapEngine.tilePlotter(ptMap);
				
				g.drawImage(tile1Image, ptTile.x, ptTile.y, null);
				
			}
		}
		drawDasher(g);
		
		sprite.draw(g);
		
		
		
	}
	
	public void drawDasher(Graphics g){
		
		Point ptMap = new Point(0,0); //tile position by map size (20*20)
		Point ptController = new Point();
		do{
			Point ptTile = MapEngine.tilePlotter(ptMap);
			g.drawImage(conveyer, ptTile.x-4, ptTile.y, null);
			ptMap = MapEngine.tileWalker(ptMap, Direction.SOUTHEAST);
			
			if(ptMap.y == 30){
				ptController = (Point) ptTile.clone();
			}
		}while(ptMap.y != 60);
		
		
		g.drawImage(controller, ptController.x-8, ptController.y-4, null);
	}
	
	public void update(){
		sprite.update();
	}
	
}
