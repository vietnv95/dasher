package com.phamkhanh.mapdesign.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import com.phamkhanh.image.ImageLoader;
import com.phamkhanh.mapdesign.DesignPanel;
import com.phamkhanh.mapdesign.TabbedPane;

public class UndoAction extends AbstractAction {
	private TabbedPane tabbedPane;
	
	private static UndoAction instance;

	private UndoAction(TabbedPane tabbedPane) {
		this.tabbedPane = tabbedPane;
		putValue(LARGE_ICON_KEY, new ImageIcon(ImageLoader.getImage("undo.png")) );
	}
	
	public static UndoAction getInstance(TabbedPane tabbedPane){
		if(instance == null){
			instance = new UndoAction(tabbedPane);
		}
		return instance;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		DesignPanel pnlDesign = tabbedPane.getCurrentTab();
		if(pnlDesign != null){
			pnlDesign.getHistory().undo();
		}
	}	
}
