package com.phamkhanh.mapdesign.command;

import java.awt.Point;
import java.util.logging.Logger;

import com.phamkhanh.mapdesign.DesignPanel;
import com.phamkhanh.mapengine.MapEngine;
import com.phamkhanh.object.Cell;
import com.phamkhanh.object.Consumer;
import com.phamkhanh.object.Map;

public class AddConsumerCommand implements Command {
	
	private Logger logger = Logger.getLogger(AddConsumerCommand.class.getName());
	
	private Map map;
	private Cell beforeCon;
	private Cell afterCon;
	
	public AddConsumerCommand(DesignPanel pnlDesign){
		map = pnlDesign.getMap();
		Point ptMap = MapEngine.mouseMap(pnlDesign.ptMouse);
		this.afterCon = new Consumer(ptMap, null);	
		this.beforeCon = map.getCell(afterCon.getPtMap());	
	}
	
	@Override
	public void execute() {	
		map.setCell(afterCon.getPtMap().x, afterCon.getPtMap().y, afterCon);
			logger.info("\nAfter : "+afterCon + "\nBefore : "+beforeCon);
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
