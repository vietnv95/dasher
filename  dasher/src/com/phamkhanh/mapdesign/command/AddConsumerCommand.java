package com.phamkhanh.mapdesign.command;

import java.awt.Point;

import com.phamkhanh.image.ImageLoader;
import com.phamkhanh.mapdesign.DesignPanel;
import com.phamkhanh.mapengine.MapEngine;
import com.phamkhanh.object.Cell;
import com.phamkhanh.object.Consumer;
import com.phamkhanh.object.Map;

public class AddConsumerCommand implements Command {
	private Map map;
	private Cell beforeConsumer;
	private Cell afterConsumer;
	
	public AddConsumerCommand(DesignPanel designPanel){
		map = designPanel.getMap();
		Point ptMap = MapEngine.mouseMap(designPanel.ptMouse);
		this.afterConsumer = new Consumer(ptMap, ImageLoader.getImage("consumer.png"), null, null);	
		this.beforeConsumer = map.getTileMap()[afterConsumer.getPtMap().x][afterConsumer.getPtMap().y];
	}
	
	@Override
	public void execute() {	
		map.getTileMap()[afterConsumer.getPtMap().x][afterConsumer.getPtMap().y] = afterConsumer;
		System.out.println("Before:"+beforeConsumer);
		System.out.println("After:"+afterConsumer);
	}

	@Override
	public void undo() {
		map.getTileMap()[afterConsumer.getPtMap().x][afterConsumer.getPtMap().y] = this.beforeConsumer;
	}

	@Override
	public void redo() {
		map.getTileMap()[afterConsumer.getPtMap().x][afterConsumer.getPtMap().y] = this.afterConsumer;
	}
}
