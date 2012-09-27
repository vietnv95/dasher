package com.phamkhanh.object;

import java.awt.Graphics;
import java.awt.Point;
import java.io.File;

public class Map {
	// size of map by tile
	public static final int MAPWIDTH = 30;
	public static final int MAPHEIGHT = 60;
		
	private Cell[][] tileMap = new Cell[MAPWIDTH][MAPHEIGHT];
	
	// Tao ban do rong
	public Map(){
		for(int i = 0; i < MAPWIDTH; i++){
			for(int j = 0; j < MAPHEIGHT; j++){
				tileMap[i][j] = new Cell(new Point(i, j));
			}
		}
	}
	
	// Doc thong tin ban do tu file
	public Map(File file){
		
	}
	
	public Cell[][] getTileMap() {
		return tileMap;
	}

	public void setTileMap(Cell[][] tileMap) {
		this.tileMap = tileMap;
	}

	public void draw(Graphics g){
		
		for(int y = 0; y < MAPHEIGHT; y++){
			for(int x = 0; x < MAPWIDTH; x++){
				if(tileMap[x][y].getClass() == Cell.class){
					tileMap[x][y].draw(g);
				}
			}
		}
		
		for(int y = 0; y < MAPHEIGHT; y++){
			for(int x = 0; x < MAPWIDTH; x++){
				if(tileMap[x][y].getClass() == Conveyer.class){
					tileMap[x][y].draw(g);
				}
			}
		}
		
		for(int y = 0; y < MAPHEIGHT; y++){
			for(int x = 0; x < MAPWIDTH; x++){
				if(tileMap[x][y].getClass() == Controller.class || 
						tileMap[x][y].getClass() == Producer.class ||
						tileMap[x][y].getClass() == Consumer.class ){
					tileMap[x][y].draw(g);
				}
			}
		}
		
	}
	
}
