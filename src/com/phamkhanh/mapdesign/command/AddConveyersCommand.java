package com.phamkhanh.mapdesign.command;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.logging.Logger;

import com.phamkhanh.mapdesign.DesignPanel;
import com.phamkhanh.mapengine.Direction;
import com.phamkhanh.mapengine.MapEngine;
import com.phamkhanh.object.Cell;
import com.phamkhanh.object.Controller;
import com.phamkhanh.object.Conveyer;
import com.phamkhanh.object.Map;

public class AddConveyersCommand implements Command {
	
	private Logger logger = Logger.getLogger(AddConveyersCommand.class.getName());

	private Map map;
	private List<Cell> before = new ArrayList<Cell>();
	private List<Cell> after = new ArrayList<Cell>();
	private Direction direct;
	private Point ptHead;
	private Point ptTail;

	public AddConveyersCommand(DesignPanel pnlDesign) {
		map = pnlDesign.getMap();
		ptHead = MapEngine.mouseMap(pnlDesign.ptHeadPixel);
		ptTail = MapEngine.mouseMap(pnlDesign.ptTailPixel);
		direct = MapEngine.tileDirecter(ptHead, ptTail);
		
		Point ptNext = (Point) ptHead.clone();  // Biến chạy duyệt cell từ ptHead -> ptTail
		if (direct != null ) {
			// Thêm cell trước ptHead nếu là instanceof Conveyer
			Cell cellBeforeHead = getBeforeHead();
			if(cellBeforeHead != null) before.add(cellBeforeHead);
			
			
			do {
				before.add(map.getCell(ptNext));
				if (ptNext.equals(ptTail))
					break;
				ptNext = MapEngine.tileWalker(ptNext, direct);
			} while (true);
			
			// Thêm cell sau ptTail nếu là  instanceof Conveyer
			Cell cellAfterTail = getAfterTail();
			if(cellAfterTail != null) before.add(cellAfterTail);
		}
	}
	
	/** Lấy về Cell ngay trước ptHead theo hướng ngược direction. */
	private Cell getBeforeHead(){
		Point ptBeforeHead = MapEngine.tileWalker(ptHead, MapEngine.getDirection(direct, MapEngine.BACK));
		Cell cellBeforeHead = map.getCell(ptBeforeHead);
		if(cellBeforeHead != null && cellBeforeHead instanceof Conveyer) return cellBeforeHead;
		else return null;
	}
	
	/** Lấy về cell ngay sau ptTail theo hướng direction. */
	private Cell getAfterTail(){
		Point ptAfterTail = MapEngine.tileWalker(ptTail, direct);
		Cell cellAfterTail = map.getCell(ptAfterTail);
		if(cellAfterTail != null && cellAfterTail instanceof Conveyer) return cellAfterTail;
		else return null;
	}

	@Override
	public void execute() {
		// Duyệt từng cell trong mảng before, ban đầu khởi tạo tất cả là Cell
		for (Cell cell : before) {
			int x = cell.getPtMap().x;
			int y = cell.getPtMap().y;
			map.setCell(x, y, new Conveyer(new Point(x, y), direct) );
		}

		// Tiến hành update lại các đối tượng Cell trong before khi cần thiết
		// đặc biệt là các vị trí đoạn conveyer giao nhau hoặc vị trí đầu, vị trí cuối
		for(int i = 0; i < before.size(); i++){
			if(i == 0){
				changeHead(before.get(i), direct);
			}else if(i == before.size()-1){
				changeTail(before.get(i), direct);
			}else{
				changeMiddle(before.get(i), direct);
			}	
		}

		// Lưu lại thông tin để sử dụng cho quá trình undo,redo
		for (Cell cell : before) {
			after.add(map.getCell(cell.getPtMap()));
		}
		
		// Log thông tin để test
		logger.info("\nAfter : "+ after+ "\nBefore : " + before);
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

		
		int nGates = 0; // Số cổng vào hoặc ra Cell (nếu nGates >=, Cell chắc chắn là COntroller)
		if(isFront != 0) nGates++;
		if(isLeft != 0) nGates++;
		if(isRight != 0) nGates++;
		if(isBack != 0) nGates++;
		
		int x = cell.getPtMap().x;
		int y = cell.getPtMap().y;
		if (nGates >= 3) {	
			map.setCell(x ,y, new Controller(new Point(x, y),0, new ArrayList<>(directions)) );
		}else if(nGates == 2){ // Chắc chắn có isFront = 1
			// Có 4 trường hợp xảy ra
			// TH1. isBack == -1 : Conveyer bình thường
			// TH2. isLeft == -1 : Conveyer ngã rẽ trái
			// TH3. isRight == -1 : Conveyer ngã rẽ phải
			// Th4. Còn lại,Controller
			if(isBack == -1){
				map.setCell(x, y, new Conveyer(new Point(x,y), direction) );
			}else if(isLeft == -1){
				map.setCell(x, y, new Conveyer(new Point(x,y), direction, Conveyer.TURN_LEFT) );
			}else if(isRight == -1){
				map.setCell(x, y, new Conveyer(new Point(x,y), direction, Conveyer.TURN_RIGHT) );
			}else{
				map.setCell(x, y, new Controller(new Point(x, y),0, new ArrayList<>(directions)) );
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

		
		int nGates = 0; // Số cổng vào hoặc ra Cell
		if(isLeft != 0) nGates++;
		if(isRight != 0) nGates++;
		if(isFront != 0) nGates++;
		if(isBack != 0) nGates++;
		
		int x = cell.getPtMap().x;
		int y = cell.getPtMap().y;
		if (nGates >= 3) {
			map.setCell(x, y, new Controller(new Point(x, y),0, new ArrayList<>(directions)) );
		}else if(nGates == 2){  // chac chan co isBack = -1 (con 1 trong 4 huong kia la 1 hoac -1)
			// Tương tự, có 4 trường hợp xảy ra
			if(isFront == 1){
				map.setCell(x, y, new Conveyer(new Point(x,y), direction) );
			}else if(isLeft == 1){
				map.setCell(x, y, new Conveyer(new Point(x,y), direction, Conveyer.TURN_LEFT) );
			}else if(isRight == 1){
				map.setCell(x, y, new Conveyer(new Point(x,y), direction, Conveyer.TURN_RIGHT) );
			}else{
				// trường hợp hai đoạn băng chuyền cùng đi vào, không xác định được cell đó nên
				// là loại gì
				//map.setCell(x, y, new Controller(new Point(x, y),0, new ArrayList<>(directions)) );
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
			map.setCell(x, y, new Controller(new Point(x, y),0, new ArrayList<>(directions)) );
		}
	}

	/** Kiểm tra hướng t của Cell nằm cạnh ô cell (theo hướng direction)
	 * @param cell
	 * @param direct
	 * @return 1 Nếu t đi ra khỏi cell
	 *         -1 Nếu t đi vào cell (ngược hướng direction)
	 *         0 Nếu t hướng trái hoặc phải (như vậy không ra cũng chẳng vào cell)
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
			map.setCell(x, y, cell);
		}
	}

	@Override
	public void redo() {
		for (Cell cell : after) {
			int x = cell.getPtMap().x;
			int y = cell.getPtMap().y;
			map.setCell(x, y, cell);
		}
	}

}
