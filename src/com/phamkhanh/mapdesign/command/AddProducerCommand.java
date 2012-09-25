package com.phamkhanh.mapdesign.command;

import java.awt.Point;

import com.phamkhanh.image.ImageLoader;
import com.phamkhanh.mapdesign.DesignPanel;
import com.phamkhanh.mapengine.Direction;
import com.phamkhanh.mapengine.MapEngine;
import com.phamkhanh.object.Cell;
import com.phamkhanh.object.Map;
import com.phamkhanh.object.Producer;

public class AddProducerCommand implements Command {
	
	private Map map;
	private Cell beforeProducer;
	private Cell afterProducer;
	
	public AddProducerCommand(DesignPanel designPanel){
		map = designPanel.getMap();
		Point ptMap = MapEngine.mouseMap(designPanel.ptMouse);
		this.afterProducer = new Producer(ptMap, ImageLoader.getImage("producer.png"), Direction.SOUTHEAST, null);	
		this.beforeProducer = map.getTileMap()[afterProducer.getPtMap().x][afterProducer.getPtMap().y];
	}
	
	@Override
	public void execute() {	
		map.getTileMap()[afterProducer.getPtMap().x][afterProducer.getPtMap().y] = afterProducer;
		System.out.println("Before:"+beforeProducer);
		System.out.println("After:"+afterProducer);
	}

	@Override
	public void undo() {
		map.getTileMap()[afterProducer.getPtMap().x][afterProducer.getPtMap().y] = this.beforeProducer;
	}

	@Override
	public void redo() {
		map.getTileMap()[afterProducer.getPtMap().x][afterProducer.getPtMap().y] = this.afterProducer;
	}

}
