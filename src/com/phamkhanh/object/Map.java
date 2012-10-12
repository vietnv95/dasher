package com.phamkhanh.object;

import java.awt.Graphics;
import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.NoSuchElementException;
import java.util.Scanner;
import com.phamkhanh.exception.MapErrorException;
import com.phamkhanh.exception.SaveNotSuccessException;

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
	
	/** 
	 * Luu doi tuong map vao file, neu luu khong thanh cong thi tung Exception
	 * @throws FileNotFoundException 
	 */
	public void save(String fileName) throws SaveNotSuccessException{
		try{
			File file = new File(fileName);
			PrintWriter writer = new PrintWriter(file);  // Khả năng bung FileNotFoundException
			for(int y = 0; y < MAPHEIGHT; y++){
				for(int x = 0; x < MAPWIDTH; x++){
					writer.print(tileMap[x][y].getProperty()+" ");
				}
				writer.println();
			}
			writer.close();
		}catch(FileNotFoundException e){
			throw new SaveNotSuccessException("Lỗi lưu bản đồ, file name không đúng định dạng hoặc không có quyền mở,ghi file");
		}
	}
	
	public void load(String fileName) throws MapErrorException{
		Scanner input = null;
		try{
			File file = new File(fileName);
			input = new Scanner(file);  // Khả năng bung FileNotFoundException
			for(int y = 0; y < MAPHEIGHT; y++){
				for(int x = 0; x < MAPWIDTH; x++){
					String line = input.next(); // Khả năng bung NoSuchElementException | IllegalStateException
					char id = line.charAt(0);
					String property = null;
					if(line.length() > 1){
						property = line.substring(2);
					}
					switch(id){
					case '0' : break;
					case '2' : tileMap[x][y] = Conveyer.getInstance(property, new Point(x,y));break;
					case '3' : tileMap[x][y] = Controller.getInstance(property, new Point(x,y));break;
					case '4' : tileMap[x][y] = Producer.getInstance(property, new Point(x,y));break;
					case '5' : tileMap[x][y] = Consumer.getInstance(property, new Point(x, y));break;
					default : throw new MapErrorException("Bản đồ chứa đối tượng không xác định");
					}
				}
			}
		}catch(FileNotFoundException | NoSuchElementException | IllegalStateException e){
			throw new MapErrorException("Không load được bản đồ : không đọc được file hoặc file bị lỗi");
		} finally {
			if(input != null){
				input.close();
			}
		}
	}
}
