package com.phamkhanh.object;

import java.awt.*;

import com.phamkhanh.mapengine.Direction;
import com.phamkhanh.mapengine.MapEngine;
import java.awt.image.*;

/**
 *  Class Sprite bieu dien mot doi tuong chuyen dong tren ban do,lop superclass box
 *  Mot so thuoc tinh rat quan trong:
 *    - ptMap : Vi tri tile hien tai cua sprite theo toa do tile map 
 *    - direction : Huong chuyen dong hien tai cua sprite
 *    - speed : Toc do chuyen dong cua sprite (don vi tile/s)
 *    - ptOffset : Buoc di chuyen nho cua sprite, Cang nho thi sprite chuyen dong cang muot 
 *    - image : Anh cua sprite
 *    - active : Trang thai cua sprite,vi co the sprite se khong chuyen dong khi game pause,gap vat can,di toi dich
 * @author Khanh
 *
 */
public class Sprite 
{
	private Point ptMap;  // Toa do tile hien tai cua Sprite
	private Point ptTile;  // Toa do pixel hien tai cua Sprite
	private int speed;  // Toc do,don vi tile/s
	private Direction direction;  // Huong di chuyen cua Sprite
	private BufferedImage image;
	private boolean active = true;
	private SpritePlayer player;

	public Sprite(Point ptMap, Direction direction, int speed, BufferedImage image) {
		super();
		this.ptMap = ptMap;
		this.direction = direction;
		this.speed = speed;
		this.ptTile = MapEngine.tilePlotter(ptMap);
		this.image = image;
		
		int seqDuration = (int)(1000/speed);
		player = new SpritePlayer(seqDuration);
	}
	
	
	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}


	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
		int seqDuration = (int)(1000/speed);
		player.setSeqDuration(seqDuration);
	}


	// Ham ve sprite theo toa do ptTile
	public void draw(Graphics g){
		g.drawImage(image,ptTile.x, ptTile.y-16, null);
	}
	
	/** 
	 * Ham update toa do ptMap va ptTile, duoc goi trong moi vong lap GameUpdate()
	 *  ptMap
	 *     Chi update theo tung o mot,nhay tung o,dung phuong thuc tileWalker
	 *  ptTile 
	 *     Update chi tiet hon,ung voi moi lan thay doi ptMap se co nhieu lan thay doi ptTile trung gian
	 */
	public void update(){
		player.updateStick();
	}
	
	
	/**
	 *  Kiem tra o hien tai co phai la mot chot chuyen huong khong?
	 *  Neu dung la chot chuyen huong,tien hanh update lai huong di chuyen cho sprite
	 */
	public void updateDirection(){
		
	}
	
	
	
	/**
	 * Lớp SpritePlayer có nhiệm vụ tạo chuyển động cho đối tượng Sprite, tức thay đổi tọa độ của Sprite
	 * Tọa độ Sprite sẽ được thay đổi (offset) sau một khoảng thời gian cố định nào đó,phụ thuộc tốc độ Sprite
	 * Mặc dù,Sprite gọi update() sau mỗi vòng lặp game,nhưng không phải lần gọi nào cũng update tọa độ của nó
	 * mà chỉ sau những khoảng thời gian nhất định (một vài lần gọi update() ) thì sự thay đổi tọa độ mới
	 * xảy ra.
	 * @author Khanh
	 *
	 */
	private class SpritePlayer {
		private int seqDuration; // ms Thoi gian de di chuyen mot o toa do tile
		private int showPeriod;  // ms Thoi gian show cua moi buoc di chuyen nho
		private int totalStep;  // So lan update toa do sprite trong thoi gian seqDuration
		private int step;
		
		private int animPeriod;  // ms Chu ki cua mot vong lap game
		private long animTotalTime; // ms Thoi gian tinh tu luc sprite bat dau chuyen dong mot chu ki seqDuration
		private Point ptOffset;  // Offset de thay doi toa do ptTile trong moi buoc nho,ptOffset cang nho thi sprite chuyen dong cang muot
		
		public SpritePlayer(int seqDuration){
			this.seqDuration = seqDuration;
			this.totalStep = (int)(MapEngine.TILEWIDTH/(2*ptOffset.x));  // Can totalStep buoc nho moi di het TILEWIDTH,moi buoc nho di chuyen ptOffset.x
			showPeriod = (int)(seqDuration/this.totalStep);
			
			this.animPeriod = (int)(1000/MapEngine.FPS);
			this.animTotalTime = 0L;
			
			this.step = 0;
		}
		
		public void updateStick(){
			if(active){
				// Xac dinh tong thoi gian tinh tu chu ki moi
				animTotalTime = (animTotalTime + animPeriod)% seqDuration; // Trong khoang 0 -> seqDuration-1 
				step = (int)animTotalTime/showPeriod;  // Trong khoang 0 -> totalStep-1
				
				Point ptTileBefore = MapEngine.tilePlotter(ptMap);
				if(direction == Direction.SOUTHEAST){
					ptTile.x = ptTileBefore.x + step*ptOffset.x;
					ptTile.y = ptTileBefore.y + step*ptOffset.y;
				}else if(direction == Direction.SOUTHWEST){
					ptTile.x = ptTileBefore.x - step*ptOffset.x;
					ptTile.y = ptTileBefore.y + step*ptOffset.y;
				}else if(direction == Direction.NORTHEAST){
					ptTile.x = ptTileBefore.x - step*ptOffset.x;
					ptTile.y = ptTileBefore.y + step*ptOffset.y;
				}else if(direction == Direction.NORTHWEST){
					ptTile.x = ptTileBefore.x - step*ptOffset.x;
					ptTile.y = ptTileBefore.y + step*ptOffset.y;
				}
				
				// Kiem tra dieu kien update ptMap
				if((animTotalTime + animPeriod) >= seqDuration){
					ptMap = MapEngine.tileWalker(ptMap, direction);
					updateDirection();
				}
			}	
		}

		public void setSeqDuration(int seqDuration) {
			this.seqDuration = seqDuration;
		}
		
	}

}  
