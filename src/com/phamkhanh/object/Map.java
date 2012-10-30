package com.phamkhanh.object;

import java.awt.Graphics;
import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Scanner;

import com.phamkhanh.exception.MapErrorException;
import com.phamkhanh.exception.SaveNotSuccessException;

public class Map implements Serializable {
	
	// size of map by tile
	public static final int MAPWIDTH = 30;
	public static final int MAPHEIGHT = 60;
	
	private boolean saved = false;
	private transient File file = null;
	private Cell[][] tileMap = new Cell[MAPWIDTH][MAPHEIGHT];
	
	// Tao ban do rong
	public Map(){
		for(int i = 0; i < MAPWIDTH; i++){
			for(int j = 0; j < MAPHEIGHT; j++){
				tileMap[i][j] = new Cell(new Point(i, j));
			}
		}
	}
	
	
	
	public boolean isSaved() {
		return saved;
	}
	
	public void setSaved(boolean saved) {
		this.saved = saved;
	}
	
	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public Cell getCell(Point point){
		return getCell(point.x, point.y);
	}
	
	public Cell getCell(int x, int y){
		try{
			return tileMap[x][y];
		}catch(ArrayIndexOutOfBoundsException e){
			return null;
		}
	}
	
	public void setCell(int x, int y, Cell cell){
		try{
			tileMap[x][y] = cell;	
		}catch(ArrayIndexOutOfBoundsException e){
			return;
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
	public void save() throws SaveNotSuccessException{
		try{
			PrintWriter writer = new PrintWriter(file);  //FileNotFoundException
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
	
	
	public void load(String filePath) throws MapErrorException{
		
		Scanner input = null;
		try{
			this.file = new File(filePath);  // NullPoiterException
			input = new Scanner(file);  // FileNotFoundException
			for(int y = 0; y < MAPHEIGHT; y++){
				for(int x = 0; x < MAPWIDTH; x++){
					String line = input.next(); // NoSuchElementException | IllegalStateException
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
		}catch(NullPointerException | FileNotFoundException | NoSuchElementException | IllegalStateException e){
			throw new MapErrorException("Không load được bản đồ : không đọc được file hoặc file bị lỗi");
		} finally {
			if(input != null){
				input.close();
			}
		}
	}
}
