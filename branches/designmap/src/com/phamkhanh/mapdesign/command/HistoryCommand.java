package com.phamkhanh.mapdesign.command;

import java.util.LinkedList;

import com.phamkhanh.mapdesign.DesignPanel;
import com.phamkhanh.mapdesign.TabbedPane;
import com.phamkhanh.object.Map;

public class HistoryCommand {
	private DesignPanel parent;
	
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
		}
	}
	
	public void redo(){
		if(!history.isEmpty() && index > 0){
			Command current = history.get(index);
			current.redo();
			fireMapChangeEvent();
			index--;
		}
	}
	
	public void fireMapChangeEvent(){
		Map map = parent.getMap();
		TabbedPane tabbedPane = parent.getParent();
		int index = tabbedPane.getSelectedIndex();
		if(map.isSaved()){
			tabbedPane.setTitleAt(index, "*"+tabbedPane.getTitleAt(index));
			map.setSaved(false);
		}	
	}

	@Override
	public String toString() {
		return "HistoryCommand [history=" + history + ", index=" + index + "]";
	}
	
	
}
