package com.phamkhanh.object;

import java.awt.Graphics;
import java.awt.Point;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import com.phamkhanh.exception.MapErrorException;
import com.phamkhanh.image.ImageLoader;
import com.phamkhanh.mapengine.Direction;
import com.phamkhanh.mapengine.MapEngine;

public class Conveyer extends Cell {

	private Direction direction;

	private int type;

	public static final int CONVEYER = 0;
	public static final int TURN_LEFT = 1;
	public static final int TURN_RIGHT = 2;

	private ObjectPlayer player = ObjectPlayer.getInstance();

	public Conveyer() {

	}

	public Conveyer(Point ptMap, Direction direction) {
		super(ptMap);
		this.direction = direction;
		this.type = CONVEYER;
	}

	public Conveyer(Point ptMap, Direction direction, int type) {
		this(ptMap, direction);
		this.type = type;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public void draw(Graphics g) {
		if (type == CONVEYER)
			drawConveyer(g);
		else
			drawBranch(g);
	}

	public void drawConveyer(Graphics g) {
		Point ptTile = MapEngine.tilePlotter(getPtMap());
		g.drawImage(ImageLoader.getImage("conveyer.png"), ptTile.x - 4,
				ptTile.y - 2, null);

		// direction == null trong trÆ°á»�ng há»£p Ä‘á»‘i tÆ°á»£ng Conveyer lÃ 
		// tileSelected
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

	public void drawBranch(Graphics g) {
		Point ptTile = MapEngine.tilePlotter(getPtMap());
		g.drawImage(ImageLoader.getImage("conveyer.png"), ptTile.x - 4,
				ptTile.y - 2, null);
		// Ve anh cho nga re trai
		if (type == TURN_LEFT) {

		}// Ve anh cho nga re phai
		else if (type == TURN_RIGHT) {

		}
	}

	@Override
	public String toString() {
		return "Conveyer [direction=" + direction + "," + super.toString()
				+ "]";
	}

	@Override
	public String getProperty() {
		return "2." + direction.getValue() + "-" + type;
	}

	public static Cell getInstance(String property, Point ptMap)
			throws MapErrorException {
		try {
			StringTokenizer tokens = new StringTokenizer(property, "-");
			Direction direction = Direction.getDirection(Integer
					.parseInt(tokens.nextToken()));
			int type = Integer.parseInt(tokens.nextToken());
			return new Conveyer(ptMap, direction, type);
		} catch (NoSuchElementException | NumberFormatException e) {
			throw new MapErrorException(
					"Thuộc tính của Conveyer không đúng định dạng");
		}
	}

}
