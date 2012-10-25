package com.phamkhanh.object;

import java.awt.*;

import com.phamkhanh.mapengine.Direction;
import com.phamkhanh.mapengine.MapEngine;

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
public abstract class Sprite 
{
	private Map map;
	
	private Point ptMap;  // Toa do tile hien tai cua Sprite
	private Point ptTile;  // Toa do pixel hien tai cua Sprite
	private int speed;  // Toc do,don vi tile/s
	private Direction direction;  // Huong di chuyen cua Sprite
	private boolean active = true;
	
	private SpritePlayer player;

	
	public Sprite(Map map, Point ptMap, Direction direction, int speed) {
		super();
		this.map = map;
		this.ptMap = ptMap;
		this.direction = direction;
		this.speed = speed;
		this.ptTile = MapEngine.tilePlotter(ptMap);
		
		int seqDuration = (int)(1000/speed);
		player = new SpritePlayer(seqDuration);
	}
	
	public Point getPtMap() {
		return ptMap;
	}

	public void setPtMap(Point ptMap) {
		this.ptMap = ptMap;
	}
	
	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
		int seqDuration = (int)(1000/speed);
		player.setSeqDuration(seqDuration);
	}
	
	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	public abstract void draw(Graphics g);
	
	
	/** 
	 * Ham update toa do ptMap va ptTile, duoc goi trong moi vong lap GameUpdate()
	 * ptMap
	 *     Chi update theo tung o mot,nhay tung o,dung phuong thuc tileWalker
	 * ptTile 
	 *     Update chi tiet hon,ung voi moi lan thay doi ptMap se co nhieu lan thay doi ptTile trung gian
	 */
	public void update(){
		player.updateStick();
	}
	
	
	
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
				// Khi bat dau vao mot o, kiem tra o ke tiep co bi lock khong
				if(animTotalTime == 0){
					Cell cell = map.getCell(MapEngine.tileWalker(ptMap, direction));
					if(cell != null && cell.getClass() == Controller.class){
						Controller controller = (Controller)cell;
						if(controller.isLock()) return;
					}
				}
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
				
				// Kiem tra dieu kien trang thai cua Sprite
				if((animTotalTime + animPeriod) >= seqDuration){
					// Den vi tri cuoi cung, cap nhat toa do va huong
					ptMap = MapEngine.tileWalker(ptMap, direction);
					updateDirection();
					animTotalTime = 0;
				}
					
			}	
		}

		public void setSeqDuration(int seqDuration) {
			this.seqDuration = seqDuration;
		}
		
	}





}  
