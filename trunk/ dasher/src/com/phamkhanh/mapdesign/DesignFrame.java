package com.phamkhanh.mapdesign;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

public class DesignFrame extends JFrame implements WindowListener, WindowFocusListener {
	
	private DesignPanel designPanel;
	
	public DesignFrame(){
		super("Game Demo");
		
		Container con = getContentPane(); // default BorderLayout
		designPanel = new DesignPanel();
		con.add(designPanel, BorderLayout.CENTER);
		
		addWindowListener(this);
		addWindowFocusListener(this);
		pack();
		
		setResizable(false);
		setVisible(true);
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
	
	@Override
	public void windowGainedFocus(WindowEvent e) {
		designPanel.requestFocusInWindow();	
	}

	@Override
	public void windowLostFocus(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
	public static void main(String[] args){
		new DesignFrame();
	}
	
	
}

	
