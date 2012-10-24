package com.phamkhanh.mapdesign.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import com.phamkhanh.image.ImageLoader;
import com.phamkhanh.mapdesign.TabbedPane;

public class NewAction extends AbstractAction {
	private TabbedPane tabbedPane;
	
	private static NewAction instance;
	
	private NewAction(TabbedPane tabbedPane){
		this.tabbedPane = tabbedPane;
		putValue(LARGE_ICON_KEY, new ImageIcon(ImageLoader.getImage("new.png")) );
	}
	
	public static NewAction getInstance(TabbedPane tabbedPane){
		if(instance == null){
			instance = new NewAction(tabbedPane);
		}
		return instance;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		tabbedPane.newTab();
	}
}
