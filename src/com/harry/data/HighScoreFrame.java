package com.harry.data;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class HighScoreFrame extends JFrame {
	JTable tableScore;
	Font font = new Font("Times New Roman", Font.BOLD , 15);
	
	public HighScoreFrame(int size) {
		this.setSize(new Dimension(500, 300));
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setTitle("Top " + size + " high score");
		this.setVisible(true);
		
		HighScore[] highScore = XmlProcessor.getHighScore(size);
		
		String[] titleTable = {"Order", "Player Name", "Score"};
		String[][] data = new String[size][3];
			
		
		for(int i = 0; i < highScore.length; i ++) {
			data[i][0] = Integer.toString(i + 1);
			data[i][1] = highScore[i].getUserName();
			data[i][2] = Integer.toString(highScore[i].getScore());
		}
		
		tableScore = new JTable(data, titleTable){
			public boolean isCellEditable(int rows, int columns) {
				return false;
			}
			
			public Component prepareRenderer(TableCellRenderer r, int rows, int columns) {
				Component component = super.prepareRenderer(r, rows, columns);
				
				component.setBackground(Color.white);
				
				if(isCellSelected(rows, columns)) {
					component.setBackground(Color.green);
				}
				
				return component;
			}
		};
		
		tableScore.setFont(font);
		tableScore.setFillsViewportHeight(true);
		
		JScrollPane scrollPane = new JScrollPane(tableScore);

		this.add(scrollPane);
		this.setVisible(true);
	}
}
