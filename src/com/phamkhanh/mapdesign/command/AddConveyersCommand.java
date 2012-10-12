package com.phamkhanh.mapdesign.command;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import com.phamkhanh.mapdesign.DesignPanel;
import com.phamkhanh.mapengine.Direction;
import com.phamkhanh.mapengine.MapEngine;
import com.phamkhanh.object.Cell;
import com.phamkhanh.object.Controller;
import com.phamkhanh.object.Conveyer;
import com.phamkhanh.object.Map;

public class AddConveyersCommand implements Command {

	private Map map;
	private List<Cell> before = new ArrayList<Cell>();
	private List<Cell> after = new ArrayList<Cell>();
	private Direction direct;
	private Point ptHead;
	private Point ptTail;

	public AddConveyersCommand(DesignPanel designPanel) {
		map = designPanel.getMap();
		ptHead = MapEngine.mouseMap(designPanel.ptHeadPixel);
		ptTail = MapEngine.mouseMap(designPanel.ptTailPixel);
		direct = MapEngine.tileDirecter(ptHead, ptTail);
		
		Point ptNext = (Point) ptHead.clone();  // Biáº¿n cháº¡y dÃ¹ng Ä‘á»ƒ duyá»‡t tá»« ptHead -> ptTail
		if (direct != null ) {
			// ThÃªm cell trÆ°á»›c ptHead náº¿u nÃ³ lÃ  instanceof Conveyer
			Cell cellBeforeHead = getBeforeHead();
			if(cellBeforeHead != null) before.add(cellBeforeHead);
			
			// ThÃªm cÃ¡c cell tá»« ptHead -> ptTail 
			//ứ ự ử ừ
			do {
				before.add(map.getCell(ptNext));
				if (ptNext.equals(ptTail))
					break;
				ptNext = MapEngine.tileWalker(ptNext, direct);
			} while (true);
			
			// ThÃªm cell sau ptTail náº¿u nÃ³ lÃ  instanceof Conveyer
			Cell cellAfterTail = getAfterTail();
			if(cellAfterTail != null) before.add(cellAfterTail);
		}
	}
	
	/** Láº¥y vá»� Ã´ Ä‘áº±ng trÆ°á»›c ptHead ngÆ°á»£c hÆ°á»›ng direction. */
	private Cell getBeforeHead(){
		Point ptBeforeHead = MapEngine.tileWalker(ptHead, MapEngine.getDirection(direct, MapEngine.BACK));
		Cell cellBeforeHead = map.getCell(ptBeforeHead);
		if(cellBeforeHead != null && cellBeforeHead instanceof Conveyer) return cellBeforeHead;
		else return null;
	}
	
	/** Láº¥y vá»� Ã´ ngay sau ptTail theo hÆ°á»›ng direction. */
	private Cell getAfterTail(){
		Point ptAfterTail = MapEngine.tileWalker(ptTail, direct);
		Cell cellAfterTail = map.getCell(ptAfterTail);
		if(cellAfterTail != null && cellAfterTail instanceof Conveyer) return cellAfterTail;
		else return null;
	}

	@Override
	public void execute() {
		// Duyá»‡t tá»«ng Ã´ trong máº£ng before
		for (Cell cell : before) {
			int x = cell.getPtMap().x;
			int y = cell.getPtMap().y;
			map.getTileMap()[x][y] = new Conveyer(new Point(x, y), direct);
		}

		// Conveyers cháº¡y tá»« Ä‘áº§u Ä‘áº¿n cuá»‘i máº£ng before, Ä‘Æ°á»£c update láº¡i
		for(int i = 0; i < before.size(); i++){
			if(i == 0){
				changeHead(before.get(i), direct);
			}else if(i == before.size()-1){
				changeTail(before.get(i), direct);
			}else{
				changeMiddle(before.get(i), direct);
			}	
		}

		// LÆ°u trá»¯ láº¡i thÃ´ng tin sau khi thay Ä‘á»•i, Ä‘á»ƒ táº¡o lá»‡nh undo,redo
		for (Cell cell : before) {
			after.add(map.getCell(cell.getPtMap()));
		}
		
		// Log láº¡i thÃ´ng tin Ä‘á»ƒ test
		System.out.println("before:"+before);
		System.out.println("after:"+after);
	}

	private void changeHead(Cell cell, Direction direction) {
		TreeSet<Direction> directions = new TreeSet<Direction>();
		directions.add(direction);
		
		Direction left = MapEngine.getDirection(direction, MapEngine.LEFT);
		Direction right = MapEngine.getDirection(direction, MapEngine.RIGHT);
		Direction back = MapEngine.getDirection(direction, MapEngine.BACK);
		
		int isFront = 1;
		int isLeft = testDirection(cell, left);
		int isRight = testDirection(cell, right);
		int isBack = testDirection(cell, back);
		
		if (isLeft == 1)
			directions.add(left);
		if (isRight == 1)
			directions.add(right);
		if(isBack == 1)
			directions.add(back);

		
		int nGates = 0; // sá»‘ cá»•ng cÃ³ thá»ƒ vÃ o hoáº·c ra Ã´ cell
		if(isFront != 0) nGates++;
		if(isLeft != 0) nGates++;
		if(isRight != 0) nGates++;
		if(isBack != 0) nGates++;
		
		int x = cell.getPtMap().x;
		int y = cell.getPtMap().y;
		if (nGates >= 3) {	
			map.getTileMap()[x][y] = new Controller(new Point(x, y),0, new ArrayList<>(directions));
		}else if(nGates == 2){ // Cháº¯c cháº¯n cÃ³ isFront = 1
			// CÃ³ 4 trÆ°á»�ng há»£p
			// TH1. isBack == -1 : Conveyer bÃ¬nh thÆ°á»�ng
			// TH2. isLeft == -1 : Conveyer ngÃ£ ráº½ trÃ¡i
			// TH3. isRight == -1 : Conveyer ngÃ£ ráº½ pháº£i
			// Th4. CÃ²n láº¡i, cell cÃ³ 2 Ä‘áº§u ra nÃªn trá»Ÿ thÃ nh Controller
			if(isBack == -1){
				map.getTileMap()[x][y] = new Conveyer(new Point(x,y), direction);
			}else if(isLeft == -1){
				map.getTileMap()[x][y] = new Conveyer(new Point(x,y), direction, Conveyer.TURN_LEFT);
			}else if(isRight == -1){
				map.getTileMap()[x][y] = new Conveyer(new Point(x,y), direction, Conveyer.TURN_RIGHT);
			}else{
				map.getTileMap()[x][y] = new Controller(new Point(x, y),0, new ArrayList<>(directions));
			}
			
		}else{  // nGates == 1
			// Do nothing
		}
	}

	private void changeTail(Cell cell, Direction direction) {
		TreeSet<Direction> directions = new TreeSet<Direction>();
		
		Direction left = MapEngine.getDirection(direction, MapEngine.LEFT);
		Direction right = MapEngine.getDirection(direction, MapEngine.RIGHT);
		Direction front = direction;
		Direction back = MapEngine.getDirection(direction, MapEngine.BACK);
		
		int isLeft = testDirection(cell, left);
		int isRight = testDirection(cell, right);
		int isFront = testDirection(cell, front);
		int isBack = -1;
		
		if (isLeft == 1)
			directions.add(left);
		if (isRight == 1)
			directions.add(right);
		if(isFront == 1)
			directions.add(front);
		if(isBack == 1)
			directions.add(back);

		
		int nGates = 0; // sá»‘ cá»•ng cÃ³ thá»ƒ vÃ o hoáº·c ra Ã´ cell
		if(isLeft != 0) nGates++;
		if(isRight != 0) nGates++;
		if(isFront != 0) nGates++;
		if(isBack != 0) nGates++;
		
		int x = cell.getPtMap().x;
		int y = cell.getPtMap().y;
		if (nGates >= 3) {
			map.getTileMap()[x][y] = new Controller(new Point(x, y),0, new ArrayList<>(directions));
		}else if(nGates == 2){  // chac chan co isBack = -1 (con 1 trong 4 huong kia la 1 hoac -1)
			// CÃ³ 2 trÆ°á»�ng há»£p
			// TH1. isFront != 0 : Conveyer bÃ¬nh thÆ°á»�ng
			// TH2. isLeft != 0 || isRight != 0 : Conveyer ngÃ£ ráº½
			if(isFront == 1){
				map.getTileMap()[x][y] = new Conveyer(new Point(x,y), direction);
			}else if(isLeft == 1){
				map.getTileMap()[x][y] = new Conveyer(new Point(x,y), direction, Conveyer.TURN_LEFT);
			}else if(isRight == 1){
				map.getTileMap()[x][y] = new Conveyer(new Point(x,y), direction, Conveyer.TURN_RIGHT);
			}else{
				map.getTileMap()[x][y] = new Controller(new Point(x, y),0, new ArrayList<>(directions));
			}
		}else{  // nGates == 1
			// Do nothing
		}
	}

	private void changeMiddle(Cell cell, Direction direction) {
		TreeSet<Direction> directions = new TreeSet<Direction>();
		directions.add(direction);
		
		Direction left = MapEngine.getDirection(direction, MapEngine.LEFT);
		Direction right = MapEngine.getDirection(direction, MapEngine.RIGHT);
		
		int isLeft = testDirection(cell, left);
		int isRight = testDirection(cell, right);
		
		if (isLeft == 1)
			directions.add(left);
		if (isRight == 1)
			directions.add(right);

		if (isLeft != 0 || isRight != 0) {
			int x = cell.getPtMap().x;
			int y = cell.getPtMap().y;
			map.getTileMap()[x][y] = new Controller(new Point(x, y),0, new ArrayList<>(directions));
		}
	}

	/** Kiá»ƒm tra hÆ°á»›ng cá»§a Ã´ náº±m cáº¡nh Ã´ cell (vá»� hÆ°á»›ng direct)
	 * @param cell
	 * @param direct
	 * @return 1 náº¿u hÆ°á»›ng Ä‘Ã³ hÆ°á»›ng ra khá»�i cell (tá»©c cÃ¹ng hÆ°á»›ng vá»›i direct)
	 *         -1 náº¿u hÆ°á»›ng Ä‘Ã³ hÆ°á»›ng Ä‘i vÃ o cell (tá»©c ngÆ°á»£c hÆ°á»›ng vá»›i direct)
	 *         0 náº¿u hÆ°á»›ng Ä‘Ã³ khÃ´ng cÃ¹ng phÆ°Æ¡ng vá»›i direct (khÃ´ng hÆ°á»›ng ra cÅ©ng cháº³ng hÆ°á»›ng vÃ o cell)
	 */        
	private int testDirection(Cell cell, Direction direct) {
		Point ptMap = MapEngine.tileWalker(cell.getPtMap(), direct);
		Cell nearCell = map.getCell(ptMap);

		if (nearCell == null || nearCell.getClass() == Cell.class) {
			return 0;
		} else if (nearCell.getClass() == Conveyer.class) {
			Direction d = ((Conveyer) nearCell).getDirection();
			if (d.equals(direct))
				return 1;
			else if(d.equals(MapEngine.getDirection(direct, MapEngine.BACK))) return -1;
			else return 0;
				
		} else if (nearCell.getClass() == Controller.class) {
			ArrayList<Direction> directions = ((Controller) nearCell)
					.getDirections();
			if (directions.contains(direct))
				return 1;
			else if(directions.contains(MapEngine.getDirection(direct, MapEngine.BACK))) return -1;
			else return 0;
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
