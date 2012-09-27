package com.phamkhanh.mapdesign.command;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.phamkhanh.image.ImageLoader;
import com.phamkhanh.mapdesign.DesignPanel;
import com.phamkhanh.mapengine.Direction;
import com.phamkhanh.mapengine.MapEngine;
import com.phamkhanh.object.Cell;
import com.phamkhanh.object.Controller;
import com.phamkhanh.object.Conveyer;
import com.phamkhanh.object.Map;
import com.phamkhanh.object.Tile;

public class AddConveyersCommand implements Command {

	private Map map;
	private List<Cell> before = new ArrayList<Cell>();
	private List<Cell> after = new ArrayList<Cell>();
	private Direction direction;
	private Point ptHead;
	private Point ptTail;

	public AddConveyersCommand(DesignPanel designPanel) {
		map = designPanel.getMap();
		ptHead = MapEngine.mouseMap(designPanel.ptHeadPixel);
		ptTail = MapEngine.mouseMap(designPanel.ptTailPixel);
		direction = MapEngine.tileDirecter(ptHead, ptTail);
		if (direction != null) {
			do {
				before.add(map.getTileMap()[ptHead.x][ptHead.y]);
				if (ptHead.equals(ptTail))
					break;
				ptHead = MapEngine.tileWalker(ptHead, direction);
			} while (true);
		}
	}

	@Override
	public void execute() {
		// Duyet tung o trong ArrayList before
		for (Cell cell : before) {
			int x = cell.getPtMap().x;
			int y = cell.getPtMap().y;
			map.getTileMap()[x][y] = new Conveyer(new Point(x, y),
					ImageLoader.getImage("conveyer.png"), direction);
		}

		// Update Cell
		for (Cell cell : before) {
			int x = cell.getPtMap().x;
			int y = cell.getPtMap().y;
			map.getTileMap()[x][y] = changeCell(map.getTileMap()[x][y]);
			after.add(map.getTileMap()[x][y]);
		}
		
		System.out.println("Before:"+before);
		System.out.println("After:"+after);
	}

	private Cell changeCell(Cell cell) {
		int x = cell.getPtMap().x;
		int y = cell.getPtMap().y;

		int nw, ne, se, sw;
		nw = getDirection(cell, Direction.NORTHWEST);
		ne = getDirection(cell, Direction.NORTHEAST);
		se = getDirection(cell, Direction.SOUTHEAST);
		sw = getDirection(cell, Direction.SOUTHWEST);

		ArrayList<Direction> directions = new ArrayList<Direction>();
		if (nw == 1)
			directions.add(Direction.NORTHWEST);
		if (ne == 1)
			directions.add(Direction.SOUTHEAST);
		if (se == 1)
			directions.add(Direction.SOUTHEAST);
		if (sw == 1)
			directions.add(Direction.SOUTHWEST);

		if (directions.size() > 1)
			return new Controller(new Point(x, y),
					ImageLoader.getImage("ne.png"), directions.get(0),
					directions);
		else return cell;
	}

	// Xet kha nang di toi o canh no cua cell
	private int getDirection(Cell cell, Direction direct) {
		int x = cell.getPtMap().x;
		int y = cell.getPtMap().y;
		Point ptMap = MapEngine.tileWalker(new Point(x, y), direct);
		Cell nearCell = map.getTileMap()[ptMap.x][ptMap.y];

		if (nearCell.getClass() == Cell.class || nearCell.getClass() == Tile.class) {
			return 0;
		} else if (nearCell.getClass() == Conveyer.class) {
			if (((Conveyer) nearCell).getDirection() == MapEngine.reverseDirection(direct))
				return -1;
			else
				return 1;
		} else if (nearCell.getClass() == Controller.class) {
			ArrayList<Direction> directions = ((Controller) nearCell)
					.getDirections();
			if (directions.size() == 1
					&& directions.get(0) == MapEngine.reverseDirection(direct))
				return -1;
			else
				return 1;
		} else
			return 0;
	}

	

	@Override
	public void undo() {
		for (Cell cell : before) {
			int x = cell.getPtMap().x;
			int y = cell.getPtMap().y;
			map.getTileMap()[x][y] = cell;
		}
	}

	@Override
	public void redo() {
		for (Cell cell : after) {
			int x = cell.getPtMap().x;
			int y = cell.getPtMap().y;
			map.getTileMap()[x][y] = cell;
		}
	}

}
