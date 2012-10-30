package com.phamkhanh.mapdesign;

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import javax.swing.JFrame;
import javax.swing.JToolBar;

import com.phamkhanh.image.ImageLoader;
import com.phamkhanh.mapdesign.action.ActionFactory;


public class DesignFrame extends JFrame implements WindowListener, WindowFocusListener{
	private String userName;
	//private JMenuBar menuBar;


	private JToolBar toolBar;
	private TileBar tileBar;
	private TabbedPane tabbedPane;
	
	public DesignFrame(String userName){
		this.userName = userName;
		setTitle("UserName : "+this.userName);
		
		tabbedPane = new TabbedPane();
		ActionFactory actionFactory = new ActionFactory(tabbedPane);
		tileBar = new TileBar(tabbedPane);
		//menuBar = new MenuBar();
		toolBar = new ToolBar(actionFactory);
		
		getContentPane().add(toolBar, BorderLayout.NORTH);
		getContentPane().add(tileBar, BorderLayout.WEST);
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
		//setJMenuBar(menuBar);
		
		addWindowListener(this);
		addWindowFocusListener(this);
		
		pack();
		setResizable(false);
		setVisible(true);
	}

	

	
	
	@Override
	public void windowActivated(WindowEvent arg0) {
		if(tabbedPane.getCurrentTab() != null)
			tabbedPane.getCurrentTab().resumeDesign();
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// Back to choose map screen
		System.exit(0);
	}

	@Override
	public void windowClosing(WindowEvent arg0) {};

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		if(tabbedPane.getCurrentTab() != null)
			tabbedPane.getCurrentTab().pauseDesign();
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		if(tabbedPane.getCurrentTab() != null)
			tabbedPane.getCurrentTab().resumeDesign();
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		if(tabbedPane.getCurrentTab() != null) 
			tabbedPane.getCurrentTab().pauseDesign();
	}

	@Override
	public void windowOpened(WindowEvent arg0) {}

	@Override
	public void windowGainedFocus(WindowEvent e) {
		if(tabbedPane.getCurrentTab() != null)
			tabbedPane.getCurrentTab().requestFocusInWindow();
	}
	@Override
	public void windowLostFocus(WindowEvent e) {}
	
	public static void main(String[] args){
		ImageLoader.loadImage();
		new DesignFrame("dung_harry");
	}
}