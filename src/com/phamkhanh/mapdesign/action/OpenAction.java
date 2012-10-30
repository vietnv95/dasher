package com.phamkhanh.mapdesign.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import com.phamkhanh.image.ImageLoader;
import com.phamkhanh.mapdesign.TabbedPane;

public class OpenAction extends AbstractAction {
	private TabbedPane tabbedPane;
	
	private static OpenAction instance;
	
	private OpenAction(TabbedPane tabbedPane){
		this.tabbedPane = tabbedPane;
		putValue(LARGE_ICON_KEY, new ImageIcon(ImageLoader.getImage("open.png")) );
	}
	
	public static OpenAction getInstance(TabbedPane tabbedPane){
		if(instance == null){
			instance = new OpenAction(tabbedPane);
		}
		return instance;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		tabbedPane.openTab();
	}
}
