package com.harry.login;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class PanelTitle extends JPanel {
	private final static int WIDTH = 976;
	private final static int HEIGHT = 100;
	
	static JLabel[] title = new JLabel[3];
	Font font = new Font("Times New Roman", Font.BOLD, 60);
	
	public PanelTitle(int x, int y) {
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setBackground(Color.lightGray);
		this.setBorder(LineBorder.createGrayLineBorder());
		this.setVisible(true);
		this.setLocation(x, y);
		makeGUI();
	}
	
	private void makeGUI() {
		title[0] = new JLabel("Conveyer Game");
		title[1] = new JLabel("Login");
		title[2] = new JLabel("Create Account");
		
		for(int i = 0; i < title.length; i ++) {
			this.add(title[i]);
			title[i].setFont(font);
			
			if(i == 0) {
				title[i].setVisible(true);
			}
			else {
				title[i].setVisible(false);
			}
		}
	}
	
	public static void updateTitle(int index) {
		for(int i = 0; i < title.length; i ++) {
			if(i == index) {
				title[index].setVisible(true);
			}
			else {
				title[i].setVisible(false);
			}
		}
	}
	
	public void hidePanel() {
		this.setVisible(false);
	}
	
	public void showPanel() {
		this.setVisible(true);
	}
}