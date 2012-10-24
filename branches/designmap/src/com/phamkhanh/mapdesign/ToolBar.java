package com.phamkhanh.mapdesign;

import javax.swing.JButton;
import javax.swing.JToolBar;

import com.phamkhanh.mapdesign.action.ActionFactory;

public class ToolBar extends JToolBar {	
	private JButton btnNew;
	private JButton btnSave;
	private JButton btnUndo;
	private JButton btnRedo;
	private JButton btnClose;

	public ToolBar(ActionFactory actionFactory) {
		super();
		
		btnNew = new JButton("New");
		btnNew.setAction(actionFactory.getNewAction());
		add(btnNew);
		
		btnSave = new JButton("Save");
		btnSave.setAction(actionFactory.getSaveAction());
		add(btnSave);
		
		btnClose = new JButton("Close");
		btnClose.setAction(actionFactory.getCloseAction());
		add(btnClose);
		
		btnUndo = new JButton("Undo");
		btnUndo.setAction(actionFactory.getUndoAction());
		add(btnUndo);
		
		btnRedo = new JButton("Redo");
		btnRedo.setAction(actionFactory.getRedoAction());
		add(btnRedo);
	}
	
	
}
