package com.phamkhanh.main;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;

import java.awt.event.*;
import java.awt.image.BufferedImage;

import com.phamkhanh.image.ImageLoader;

public class OptionPanel extends JPanel {

	private JButton[] btnArray;
	private int level;
	private int type;
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	private MainFrame parent;

	public OptionPanel(MainFrame parent) {
		this.parent = parent;
		level = -1;
		type = 0;
		this.setBackground(Color.blue);
		this.setPreferredSize(new Dimension(976, 388));
		GridLayout grid = new GridLayout(4, 4);
		this.setLayout(grid);
		btnArray = new JButton[16];
		drawButton();
		drawLevelButton();
	}

	public void drawButton() {

		for (int i = 0; i < 16; i++) {

			btnArray[i] = new JButton();
			this.add(btnArray[i]);

		}
		for (int i = 0; i < 16; i++) {

			btnArray[i].addMouseListener(new mouseEvent());

		}

	}
	
	public void drawLevelButton(){
		
		for (int i = 0; i < 16; i++) {
			btnArray[i].setIcon(null);
		   btnArray[i].setText("Level:"+(i+1));
		}
	}

	public void drawMapButton() {

		BufferedImage buffImage = ImageLoader.loadImage("bando.png");
		ImageIcon ico = new ImageIcon(buffImage);

		for (int i = 0; i < 16; i++) {
			
			btnArray[i].setText("Map:" + (i + 1));
			btnArray[i].setIcon(ico);

			Border border = BorderFactory.createLineBorder(Color.blue, 2);
			btnArray[i].setBorder(border);
			btnArray[i].setToolTipText("Map:" + (i + 1));
		}
	}

	public void choiceMap() {

	}

	private class mouseEvent implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			String temp = ((JButton)e.getSource()).getActionCommand();
			
			if (type ==0){
				level = Integer.parseInt(temp.substring(temp.indexOf(':')+1,temp.length()));
				
			    parent.showBack();
				
				drawMapButton();
				type = 1;
			}else{
				
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			String temp = ((JButton) e.getSource()).getActionCommand();
			int map = Integer.parseInt(temp.substring(temp.indexOf(':') + 1,
					temp.length()));
			Border border = BorderFactory.createLineBorder(Color.BLACK, 4);
			btnArray[map - 1].setBorder(border);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			String temp = ((JButton) e.getSource()).getActionCommand();
			int map = Integer.parseInt(temp.substring(temp.indexOf(':') + 1,
					temp.length()));
			Border border = BorderFactory.createLineBorder(Color.blue, 2);
			btnArray[map - 1].setBorder(border);
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

	}

	public void paintComponent(Graphics g) {
		g.drawString("Hihi", 10, 10);
	}
}
