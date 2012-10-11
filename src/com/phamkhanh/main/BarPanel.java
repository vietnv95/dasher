package com.phamkhanh.main;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class BarPanel extends JPanel {

	private String userName = "khoa";
	private int level;
	private int point;
	private JLabel userLabel;
	private JButton createMap;
	private JButton exit;
	private JButton logOut;
	private JButton highScore;
	private JButton help;
	private JButton setting;
	private MainFrame parent;
	private JButton back;

	public BarPanel(MainFrame parent) {
		
		this.parent = parent;
		this.setPreferredSize(new Dimension(976, 40));
		this.setBackground(Color.gray);
		this.setLayout(new FlowLayout());
		this.userLabel = new JLabel("User name:" + this.userName);
		this.createMap = new JButton("Create Map");
		this.back = new JButton("Back");
		this.exit = new JButton("Exit");
		this.logOut = new JButton("Log Out");
		this.highScore = new JButton("High Score");
		this.help = new JButton("Help");
		this.setting = new JButton("Setting");
		
		this.add(userLabel);
		this.add(back);
		back.setVisible(false);
		this.add(createMap);
		this.add(highScore);
		this.add(setting);
		this.add(setting);
		this.add(logOut);
		this.add(exit);

		
		this.createMap.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				// chuyen
			}
		});
		this.exit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int result = JOptionPane.showConfirmDialog(null,
						"Bạn có muốn thoát chương trình?", "Xác nhận",

						JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION) {
					System.exit(0);
				}
			}
		});
		this.logOut.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int result = JOptionPane.showConfirmDialog(null, 
						"Bạn có muốn logout?","Xác nhận", JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION) {
					// do something
				}

			}
		});

	}
	
	public void backLevel(boolean flag){
		if(flag){
			this.back.setVisible(true);
		}else{
			this.back.setVisible(false);
		}
		
		this.back.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				parent.backToLevel();
			}
		});
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

}
