package com.phamkhanh.game;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

public class GameFrame extends JFrame implements WindowListener {
	
	private GamePanel gamePanel;
	
	public GameFrame(){
		super("Game Demo");
		
		Container con = getContentPane(); // default BorderLayout
		gamePanel = new GamePanel();
		con.add(gamePanel, BorderLayout.CENTER);
		
		addWindowListener(this);
		pack();
		
		setResizable(false);
		setVisible(true);
	}

	@Override
	public void windowActivated(WindowEvent arg0) {gamePanel.resumeGame();}

	@Override
	public void windowClosed(WindowEvent arg0) {}

	@Override
	public void windowClosing(WindowEvent arg0) {gamePanel.stopGame();}

	@Override
	public void windowDeactivated(WindowEvent arg0) {gamePanel.pauseGame();}

	@Override
	public void windowDeiconified(WindowEvent arg0) {gamePanel.resumeGame();}

	@Override
	public void windowIconified(WindowEvent arg0) {gamePanel.pauseGame();}

	@Override
	public void windowOpened(WindowEvent arg0) {}
	
	
	public static void main(String[] args){
		new GameFrame();
	}
}
