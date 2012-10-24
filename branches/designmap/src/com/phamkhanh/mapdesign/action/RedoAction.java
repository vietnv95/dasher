package com.phamkhanh.mapdesign.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import com.phamkhanh.image.ImageLoader;
import com.phamkhanh.mapdesign.DesignPanel;
import com.phamkhanh.mapdesign.TabbedPane;

public class RedoAction extends AbstractAction {
	private TabbedPane tabbedPane;
	
	private static RedoAction instance;

	private RedoAction(TabbedPane tabbedPane) {
		super();
		this.tabbedPane = tabbedPane;
		putValue(LARGE_ICON_KEY, new ImageIcon(ImageLoader.getImage("redo.png")) );
	}
	
	public static RedoAction getInstance(TabbedPane tabbedPane){
		if(instance == null){
			instance = new RedoAction(tabbedPane);
		}
		return instance;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		DesignPanel pnlDesign = tabbedPane.getPanel();
		if(pnlDesign != null){
			pnlDesign.getHistory().redo();
		}
	}
	
	
}
