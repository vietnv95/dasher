package com.phamkhanh.main;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

import com.harry.data.GameAccess;
import com.harry.data.HighScoreFrame;

public class BarPanel extends JPanel {

	private ChooseMapFrame parent;
	
	private int level;
	private int score;
	
	private JLabel lblUserName;
	private JButton btnDesignMap;
	private JButton btnExit;
	private JButton btnLogout;
	private JButton btnHighScore;
	private JButton btnHelp;
	private JButton btnSetting;
	private JButton btnBack;

	public BarPanel(ChooseMapFrame parent, String userName) {
		this.parent = parent;
		this.setPreferredSize(new Dimension(976, 40));
		this.setBackground(Color.gray);
		this.setLayout(new FlowLayout());
		this.lblUserName = new JLabel("User name: " + userName);
		this.btnDesignMap = new JButton("Create Map");
		this.btnBack = new JButton("Back");
		this.btnExit = new JButton("Exit");
		this.btnLogout = new JButton("Log Out");
		this.btnHighScore = new JButton("High Score");
		this.btnHelp = new JButton("Help");
		this.btnSetting = new JButton("Setting");
		
		this.add(lblUserName);
		this.add(btnBack);
		btnBack.setVisible(false);
		this.add(btnDesignMap);
		this.add(btnHighScore);
		this.add(btnSetting);
		this.add(btnSetting);
		this.add(btnLogout);
		this.add(btnExit);

		
		this.btnDesignMap.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		
		this.btnExit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int result = JOptionPane.showConfirmDialog(null,
						"Do you want exit?", "Ok",

						JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION) {
					System.exit(0);
				}
			}
		});
		
		handleEvent();
	}
	
	private void handleEvent() {
		btnLogout.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				int result = JOptionPane.showConfirmDialog(null, "Do you want logout?", "Ok", JOptionPane.OK_CANCEL_OPTION);
				
				if(result == JOptionPane.OK_OPTION) {
					parent.dispose();
					GameAccess.getContain().setVisible(true);
				}
			}
		});
		
		btnHighScore.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				new HighScoreFrame(5);
			}
		});
	}
	
	public void backLevel(boolean flag){
		if(flag){
			this.btnBack.setVisible(true);
		}else{
			this.btnBack.setVisible(false);
		}
		
		this.btnBack.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				parent.backToLevel();
			}
		});
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

}
