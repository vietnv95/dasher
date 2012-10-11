package com.phamkhanh.mapdesign;

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import javax.swing.JFrame;
import javax.swing.JMenuBar;

import com.phamkhanh.image.ImageLoader;


public class DesignFrame extends JFrame implements WindowListener, WindowFocusListener{
	private JMenuBar menuBar;
	private TilePanel tilePanel;
	private DesignPanel designPanel;
	public DesignFrame(){
		setTitle("Design Map");
		
		designPanel = new DesignPanel();
		tilePanel = new TilePanel(designPanel);

		menuBar = new MenuBar();
		

		getContentPane().add(tilePanel, BorderLayout.WEST);
		getContentPane().add(designPanel, BorderLayout.CENTER);
		setJMenuBar(menuBar);
		
		addWindowListener(this);
		addWindowFocusListener(this);
		pack();
		
		setResizable(false);
		setVisible(true);
	}
	
	public static void main(String[] args){
		ImageLoader.loadImage();
		new DesignFrame();
	}

	@Override
	public void windowActivated(WindowEvent arg0) {designPanel.resumeDesign();}

	@Override
	public void windowClosed(WindowEvent arg0) {System.exit(0);}

	@Override
	public void windowClosing(WindowEvent arg0) {designPanel.stopDesign(); };

	@Override
	public void windowDeactivated(WindowEvent arg0) {designPanel.pauseDesign();}

	@Override
	public void windowDeiconified(WindowEvent arg0) {designPanel.resumeDesign();}

	@Override
	public void windowIconified(WindowEvent arg0) {designPanel.pauseDesign();}

	@Override
	public void windowOpened(WindowEvent arg0) {}

	@Override
	public void windowGainedFocus(WindowEvent e) {
		designPanel.requestFocus();
	}

	@Override
	public void windowLostFocus(WindowEvent e) {	
	}
}