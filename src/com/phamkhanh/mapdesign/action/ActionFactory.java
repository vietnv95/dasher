package com.phamkhanh.mapdesign.action;

import com.phamkhanh.mapdesign.TabbedPane;

public class ActionFactory {
	private TabbedPane tabbedPane;

	public ActionFactory(TabbedPane tabbedPane) {
		super();
		this.tabbedPane = tabbedPane;
	}
	
	public NewAction getNewAction(){
		return NewAction.getInstance(tabbedPane);
	}
	
	public CloseAction getCloseAction(){
		return CloseAction.getInstance(tabbedPane);
	}
	
	public SaveAction getSaveAction(){
		return SaveAction.getInstance(tabbedPane);
	}
	
	public UndoAction getUndoAction(){
		return UndoAction.getInstance(tabbedPane);
	}
	
	public RedoAction getRedoAction(){
		return RedoAction.getInstance(tabbedPane);
	}
	
}
