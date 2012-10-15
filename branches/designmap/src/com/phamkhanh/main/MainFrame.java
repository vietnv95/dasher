package com.phamkhanh.main;

import java.awt.*;

import javax.swing.*;

public class MainFrame extends JFrame{
	/**
	 * @param args
	 */
	
	private BarPanel barPanel;
	private OptionPanel optionPanel;
	
	public MainFrame(String userName){
		this.setSize(976,488);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.barPanel = new  BarPanel(this, userName);
		this.optionPanel = new OptionPanel(this);
		this.setTitle("Game");
		this.setLayout(new BorderLayout());
		this.add(this.barPanel,BorderLayout.NORTH);
		this.add(this.optionPanel,BorderLayout.CENTER);
		this.setVisible(true);
		this.setResizable(false);
	}
	
	public void showBack(){
		this.barPanel.backLevel(true);
	}

	public void backToLevel(){
		this.optionPanel.drawLevelButton();
		this.barPanel.backLevel(false);
		this.optionPanel.setType(0);
	
	}
	
	public BarPanel getBarPanel() {
		return barPanel;
	}
	
	public OptionPanel getOptionPanel() {
		return optionPanel;
	}
}