package com.phamkhanh.mapdesign.command;

import java.awt.Point;
import com.phamkhanh.mapdesign.DesignPanel;
import com.phamkhanh.mapengine.Direction;
import com.phamkhanh.mapengine.MapEngine;
import com.phamkhanh.object.Cell;
import com.phamkhanh.object.Map;
import com.phamkhanh.object.Producer;

public class AddProducerCommand implements Command {
	
	private Map map;
	private Cell beforePro;
	private Cell afterPro;
	
	public AddProducerCommand(DesignPanel designPanel){
		map = designPanel.getMap();
		Point ptMap = MapEngine.mouseMap(designPanel.ptMouse);
		this.afterPro = new Producer(ptMap, Direction.SOUTHEAST);	
		this.beforePro = map.getCell(ptMap.x, ptMap.y);
	}
	
	@Override
	public void execute() {	
		int x = afterPro.getPtMap().x;
		int y = afterPro.getPtMap().y;
		map.setCell(x, y, afterPro);
		
		System.out.println("Before:"+beforePro);
		System.out.println("After:"+afterPro);
	}

	@Override
	public void undo() {
		int x = afterPro.getPtMap().x;
		int y = afterPro.getPtMap().y;
		map.setCell(x, y, this.beforePro);
	}

	@Override
	public void redo() {
		int x = afterPro.getPtMap().x;
		int y = afterPro.getPtMap().y;
		map.setCell(x, y, this.afterPro);
	}

}
