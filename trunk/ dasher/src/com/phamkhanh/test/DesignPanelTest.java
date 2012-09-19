package com.phamkhanh.test;

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import com.phamkhanh.image.ImageLoader;
import com.phamkhanh.mapdesign.DesignPanel;
import com.phamkhanh.mapdesign.TilePanel;

public class DesignPanelTest extends JFrame implements WindowListener{
	private TilePanel tilePanel;
	private DesignPanel designPanel;
	public DesignPanelTest(){
		setTitle("TileFrame");
		setVisible(true);
		
		designPanel = new DesignPanel();
		tilePanel = new TilePanel(designPanel);
		
		
		getContentPane().add(tilePanel, BorderLayout.WEST);
		getContentPane().add(designPanel, BorderLayout.CENTER);
		
		pack();
	}
	
	public static void main(String[] args){
		ImageLoader.loadImage();
		
		new DesignPanelTest();
	}

	@Override
	public void windowActivated(WindowEvent arg0) {designPanel.resumeDesign();}

	@Override
	public void windowClosed(WindowEvent arg0) {}

	@Override
	public void windowClosing(WindowEvent arg0) {designPanel.stopDesign();}

	@Override
	public void windowDeactivated(WindowEvent arg0) {designPanel.pauseDesign();}

	@Override
	public void windowDeiconified(WindowEvent arg0) {designPanel.resumeDesign();}

	@Override
	public void windowIconified(WindowEvent arg0) {designPanel.pauseDesign();}

	@Override
	public void windowOpened(WindowEvent arg0) {}
}