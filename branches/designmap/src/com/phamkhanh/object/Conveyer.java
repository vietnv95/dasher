package com.phamkhanh.object;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import com.phamkhanh.image.ImageLoader;
import com.phamkhanh.mapengine.Direction;
import com.phamkhanh.mapengine.MapEngine;

public class Conveyer extends Tile {

	private Direction direction;
	
	private int type;
	
	public static final int CONVEYER = 0;
	public static final int TURN_LEFT = 1;
	public static final int TURN_RIGHT = 2;

	private ObjectPlayer player;

	public Conveyer() {

	}

	public Conveyer(Point ptMap, BufferedImage image, Direction direction) {
		super(ptMap, image);
		this.direction = direction;
		this.player = ObjectPlayer.getInstance();
		this.type = CONVEYER;
	}
	
	public Conveyer(Point ptMap, BufferedImage image, Direction direction, int type){
		this(ptMap, image, direction);
		this.type = type;
	}
	

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	@Override
	public void draw(Graphics g) {
		if(type == CONVEYER) drawConveyer(g);
		else drawBranch(g);
	}
	
	public void drawConveyer(Graphics g){
		Point ptTile = MapEngine.tilePlotter(getPtMap());
		g.drawImage(getImage(), ptTile.x - 4, ptTile.y - 2, null);

		// direction == null trong trÆ°á»�ng há»£p Ä‘á»‘i tÆ°á»£ng Conveyer lÃ  tileSelected
		if (direction == Direction.NORTHWEST) {
			g.drawImage(ImageLoader.getImage("linenw.png"),
					ptTile.x + player.getOffset(direction).x,
					ptTile.y + player.getOffset(direction).y, null);
		}
		if (direction == Direction.NORTHEAST) {
			g.drawImage(ImageLoader.getImage("linene.png"),
					ptTile.x + player.getOffset(direction).x,
					ptTile.y + player.getOffset(direction).y, null);
		}
		if (direction == Direction.SOUTHEAST) {
			g.drawImage(ImageLoader.getImage("linese.png"),
					ptTile.x + player.getOffset(direction).x,
					ptTile.y + player.getOffset(direction).y, null);
		}
		if (direction == Direction.SOUTHWEST) {
			g.drawImage(ImageLoader.getImage("linesw.png"),
					ptTile.x + player.getOffset(direction).x,
					ptTile.y + player.getOffset(direction).y, null);
		}
	}
	
	public void drawBranch(Graphics g){
		
	}

	@Override
	public String toString() {
		return "Conveyer [direction=" + direction+","+ super.toString() + "]";
	}

	@Override
	public String getProperty() {
		return "2."+direction.getValue()+"-"+type;
	}

}
