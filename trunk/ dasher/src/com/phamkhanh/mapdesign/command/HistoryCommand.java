package com.phamkhanh.mapdesign.command;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class HistoryCommand {
	// History List Commands
	private LinkedList<Command> history;
	private int index;
	
	public HistoryCommand(){
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
	}
	
	public void undo(){
		if(!history.isEmpty() && index < history.size()){
			Command current = history.get(index);
			current.undo();
			index++;
		}
	}
	
	public void redo(){
		if(!history.isEmpty() && index > 0){
			Command current = history.get(index);
			current.redo();
			index--;
		}
	}

	@Override
	public String toString() {
		return "HistoryCommand [history=" + history + ", index=" + index + "]";
	}
	
	
}
