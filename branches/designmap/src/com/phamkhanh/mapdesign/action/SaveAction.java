package com.phamkhanh.mapdesign.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import com.phamkhanh.image.ImageLoader;
import com.phamkhanh.mapdesign.TabbedPane;

public class SaveAction extends AbstractAction {
	private TabbedPane tabbedPane;
	private static SaveAction instance;
	
	private SaveAction(TabbedPane tabbedPane) {
		this.tabbedPane = tabbedPane;
		putValue(LARGE_ICON_KEY, new ImageIcon(ImageLoader.getImage("save.png")) );
	}

	public static SaveAction getInstance(TabbedPane tabbedPane){
		if(instance == null){
			instance = new SaveAction(tabbedPane);
		}
		return instance;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		tabbedPane.saveTab(tabbedPane.getSelectedIndex());
	}
}
