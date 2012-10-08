package com.phamkhanh.mapdesign.command;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

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
	private Direction direct;
	private Point ptHead;
	private Point ptTail;

	public AddConveyersCommand(DesignPanel designPanel) {
		map = designPanel.getMap();
		ptHead = MapEngine.mouseMap(designPanel.ptHeadPixel);
		ptTail = MapEngine.mouseMap(designPanel.ptTailPixel);
		direct = MapEngine.tileDirecter(ptHead, ptTail);
		
		Point ptNext = (Point) ptHead.clone();  // Biến chạy dùng để duyệt từ ptHead -> ptTail
		if (direct != null) {
			// Thêm cell trước ptHead nếu nó là instanceof Conveyer
			Cell cellBeforeHead = getBeforeHead();
			if(cellBeforeHead != null) before.add(cellBeforeHead);
			
			// Thêm các cell từ ptHead -> ptTail 
			do {
				before.add(map.getCell(ptNext));
				if (ptNext.equals(ptTail))
					break;
				ptNext = MapEngine.tileWalker(ptNext, direct);
			} while (true);
			
			// Thêm cell sau ptTail nếu nó là instanceof Conveyer
			Cell cellAfterTail = getAfterTail();
			if(cellAfterTail != null) before.add(cellAfterTail);
		}
	}
	
	/** Lấy về ô đằng trước ptHead ngược hướng direction. */
	private Cell getBeforeHead(){
		Point ptBeforeHead = MapEngine.tileWalker(ptHead, MapEngine.getDirection(direct, MapEngine.BACK));
		Cell cellBeforeHead = map.getCell(ptBeforeHead);
		if(cellBeforeHead instanceof Conveyer) return cellBeforeHead;
		else return null;
	}
	
	/** Lấy về ô ngay sau ptTail theo hướng direction. */
	private Cell getAfterTail(){
		Point ptAfterTail = MapEngine.tileWalker(ptTail, direct);
		Cell cellAfterTail = map.getCell(ptAfterTail);
		if(cellAfterTail instanceof Conveyer) return cellAfterTail;
		else return null;
	}

	@Override
	public void execute() {
		// Duyệt từng ô trong mảng before
		for (Cell cell : before) {
			int x = cell.getPtMap().x;
			int y = cell.getPtMap().y;
			map.getTileMap()[x][y] = new Conveyer(new Point(x, y),
					ImageLoader.getImage("conveyer.png"), direct);
		}

		// Conveyers chạy từ đầu đến cuối mảng before, được update lại
		for(int i = 0; i < before.size(); i++){
			if(i == 0){
				changeHead(before.get(i), direct);
			}else if(i == before.size()-1){
				changeTail(before.get(i), direct);
			}else{
				changeMiddle(before.get(i), direct);
			}	
		}

		// Lưu trữ lại thông tin sau khi thay đổi, để tạo lệnh undo,redo
		for (Cell cell : before) {
			after.add(map.getCell(cell.getPtMap()));
		}
		
		// Log lại thông tin để test
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

		
		int nGates = 0; // số cổng có thể vào hoặc ra ô cell
		if(isFront != 0) nGates++;
		if(isLeft != 0) nGates++;
		if(isRight != 0) nGates++;
		if(isBack != 0) nGates++;
		
		int x = cell.getPtMap().x;
		int y = cell.getPtMap().y;
		if (nGates >= 3) {	
			map.getTileMap()[x][y] = new Controller(new Point(x, y),
					null, direction, directions);
		}else if(nGates == 2){ // Chắc chắn có isFront = 1
			// Có 4 trường hợp
			// TH1. isBack == -1 : Conveyer bình thường
			// TH2. isLeft == -1 : Conveyer ngã rẽ trái
			// TH3. isRight == -1 : Conveyer ngã rẽ phải
			// Th4. Còn lại, cell có 2 đầu ra nên trở thành Controller
			if(isBack == -1){
				map.getTileMap()[x][y] = new Conveyer(new Point(x,y), null, direction);
			}else if(isLeft == -1){
				map.getTileMap()[x][y] = new Conveyer(new Point(x,y), null, direction, Conveyer.TURN_LEFT);
			}else if(isRight == -1){
				map.getTileMap()[x][y] = new Conveyer(new Point(x,y), null, direction, Conveyer.TURN_RIGHT);
			}else{
				map.getTileMap()[x][y] = new Controller(new Point(x, y),
						null, direction, directions);
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

		
		int nGates = 0; // số cổng có thể vào hoặc ra ô cell
		if(isLeft != 0) nGates++;
		if(isRight != 0) nGates++;
		if(isFront != 0) nGates++;
		if(isBack != 0) nGates++;
		
		if (nGates >= 3) {
			int x = cell.getPtMap().x;
			int y = cell.getPtMap().y;
			map.getTileMap()[x][y] = new Controller(new Point(x, y),
					null, directions.first(), directions);
		}else if(nGates == 2){
			// Có 2 trường hợp
			// TH1. isFront != 0 : Conveyer bình thường
			// TH2. isLeft != 0 || isRight != 0 : Conveyer ngã rẽ
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
			map.getTileMap()[x][y] = new Controller(new Point(x, y),
					null, direction, directions);
		}
	}

	/** Kiểm tra hướng của ô nằm cạnh ô cell (về hướng direct)
	 * @param cell
	 * @param direct
	 * @return 1 nếu hướng đó hướng ra khỏi cell (tức cùng hướng với direct)
	 *         -1 nếu hướng đó hướng đi vào cell (tức ngược hướng với direct)
	 *         0 nếu hướng đó không cùng phương với direct (không hướng ra cũng chẳng hướng vào cell)
	 */        
	private int testDirection(Cell cell, Direction direct) {
		Point ptMap = MapEngine.tileWalker(cell.getPtMap(), direct);
		Cell nearCell = map.getCell(ptMap);

		if (nearCell.getClass() == Cell.class
				|| nearCell.getClass() == Tile.class) {
			return 0;
		} else if (nearCell.getClass() == Conveyer.class) {
			Direction d = ((Conveyer) nearCell).getDirection();
			if (d.equals(direct))
				return 1;
			else if(d.equals(MapEngine.getDirection(direct, MapEngine.BACK))) return -1;
			else return 0;
				
		} else if (nearCell.getClass() == Controller.class) {
			TreeSet<Direction> directions = ((Controller) nearCell)
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
