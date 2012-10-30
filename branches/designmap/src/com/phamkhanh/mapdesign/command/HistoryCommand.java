package com.phamkhanh.mapdesign.command;

import java.util.LinkedList;
import java.util.logging.Logger;

import com.phamkhanh.mapdesign.DesignPanel;
import com.phamkhanh.mapdesign.TabbedPane;
import com.phamkhanh.object.Map;

public class HistoryCommand {
	private DesignPanel parent;
	
	private Logger logger = Logger.getLogger(HistoryCommand.class.getName());
	
	// History List Commands
	private LinkedList<Command> history;
	private int index;
	
	public HistoryCommand(DesignPanel parent){
		if(parent == null) 
			throw new IllegalArgumentException("DesignPanel is null");
		this.parent = parent;
		
		history = new LinkedList<Command>();
		index = -1;
	}
	
	public void add(Command command){
		if(history.isEmpty()){
			history.addFirst(command);
			index = 0;
		}else{
			// Xoa bo command tu index den 0
			while(index > 0){
				index--;
				history.remove(index);
			}
			history.addFirst(command);
		}
		fireMapChangeEvent();
	}
	
	public void undo(){
		if(!history.isEmpty() && index < history.size()){
			Command current = history.get(index);
			current.undo();	
			fireMapChangeEvent();
			index++;
				logger.info(toString());
		}
	}
	
	public void redo(){
		if(!history.isEmpty() && index > 0){
			index--;
			Command current = history.get(index);
			current.redo();
			fireMapChangeEvent();
				logger.info(toString());
		}
	}
	
	/**
	 * Mỗi một thao tác undo,redo history làm cho map đang design trở về trạng
	 * thái chưa lưu, do vậy cần update lại một vài thuộc tính
	 * 1. saved của đối tượng map
	 * 2. title của currentTab trong TabbedPane
	 */
	public void fireMapChangeEvent(){
		Map map = parent.getMap();
		TabbedPane tabbedPane = parent.getParent();
		int i = tabbedPane.getSelectedIndex();
		if(map.isSaved()){
			tabbedPane.setTitleAt(i, "*"+tabbedPane.getTitleAt(i));
			map.setSaved(false);
		}	
	}

	@Override
	public String toString() {
		return "HistoryCommand [history=" + history + ", index=" + index + "]";
	}
}
