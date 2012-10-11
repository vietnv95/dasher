package com.phamkhanh.object;

import java.awt.Graphics;
import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

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
	
	public Cell getCell(Point point){
		try{
			return tileMap[point.x][point.y];
		}catch(Exception e){
			return null;
		}
	}
	
	public Cell getCell(int x, int y){
		try{
			return tileMap[x][y];
		}catch(Exception e){
			return null;
		}
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
	
	/** Luu doi tuong map vao file, neu luu khong thanh cong thi tung Exception
	 * @throws FileNotFoundException */
	public void save(String fileName) throws FileNotFoundException{
		File file = new File(fileName);
		PrintWriter writer = new PrintWriter(file);
		for(int y = 0; y < MAPHEIGHT; y++){
			for(int x = 0; x < MAPWIDTH; x++){
				writer.print(tileMap[x][y].getProperty()+" ");
			}
			writer.println();
		}
		writer.close();
	}
	
	public static Map load(String fileName){
		return null;
	}
}
