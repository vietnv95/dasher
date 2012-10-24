package com.phamkhanh.mapdesign.command;

import java.awt.Point;
import com.phamkhanh.mapdesign.DesignPanel;
import com.phamkhanh.mapengine.MapEngine;
import com.phamkhanh.object.Cell;
import com.phamkhanh.object.Consumer;
import com.phamkhanh.object.Map;

public class AddConsumerCommand implements Command {
	private Map map;
	private Cell beforeCon;
	private Cell afterCon;
	
	public AddConsumerCommand(DesignPanel pnlDesign){
		map = pnlDesign.getMap();
		Point ptMap = MapEngine.mouseMap(pnlDesign.ptMouse);
		this.afterCon = new Consumer(ptMap, null);	
		this.beforeCon = map.getCell(afterCon.getPtMap().x, afterCon.getPtMap().y);	
	}
	
	@Override
	public void execute() {	
		map.setCell(afterCon.getPtMap().x, afterCon.getPtMap().y, afterCon);
		System.out.println("Before:"+beforeCon);
		System.out.println("After:"+afterCon);
	}

	@Override
	public void undo() {
		map.setCell(afterCon.getPtMap().x, afterCon.getPtMap().y, this.beforeCon);
	}

	@Override
	public void redo() {
		map.setCell(afterCon.getPtMap().x, afterCon.getPtMap().y, this.afterCon);
	}
}
