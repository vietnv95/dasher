package com.phamkhanh.mapdesign.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import com.phamkhanh.image.ImageLoader;
import com.phamkhanh.mapdesign.TabbedPane;

public class CloseAction extends AbstractAction {
	private TabbedPane tabbedPane;
	private static CloseAction instance;

	private CloseAction(TabbedPane tabbedPane) {
		this.tabbedPane = tabbedPane;
		putValue(LARGE_ICON_KEY, new ImageIcon(ImageLoader.getImage("close.png")) );
	}
	
	public static CloseAction getInstance(TabbedPane tabbedPane){
		if(instance == null){
			instance = new CloseAction(tabbedPane);
		}
		return instance;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		tabbedPane.closeTab(tabbedPane.getSelectedIndex());
	}
	
}
