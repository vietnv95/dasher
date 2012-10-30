package com.phamkhanh.mapdesign;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import com.phamkhanh.image.ImageLoader;
import com.phamkhanh.mapengine.MapEngine;
import com.phamkhanh.object.Cell;
import com.phamkhanh.object.Consumer;
import com.phamkhanh.object.Conveyer;
import com.phamkhanh.object.Producer;

public class TileBar extends JPanel {
	private static final int PWIDTH = 32;
	private static final int PHEIGHT = 488;
	
	private int numberTile = 3;
	private int indexSelected = -1;
	private Cell[] tiles = new Cell[numberTile];
	
	public TileBar(final TabbedPane tabbedPane){
		setDoubleBuffered(true);
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(PWIDTH, PHEIGHT));
		
		// Load tiles
		tiles[0] = new Conveyer(new Point(0,0), null);
		tiles[1] = new Producer(new Point(0,4),null);
		tiles[2] = new Consumer(new Point(0,6), null);
		
		// Handler mouse event
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				int index = e.getY() / MapEngine.TILEHEIGHT;
				if(index >= 0 && index < numberTile){
					indexSelected = index;
					DesignPanel pnlDesign = tabbedPane.getCurrentTab();
					if(pnlDesign != null){
						pnlDesign.setTileSelected(tiles[indexSelected]);
					}
					repaint();
				}	
			}
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
			}
			@Override
			public void mousePressed(MouseEvent arg0) {}
			@Override
			public void mouseExited(MouseEvent arg0) {}
			@Override
			public void mouseEntered(MouseEvent arg0) {}
			
		});	
 	 }
	

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		for(int i = 0; i < numberTile; i++){
			if(i == indexSelected){
				// Ve tileSelected voi background mau do de danh dau selected
				g.setColor(Color.BLACK);
				g.fillRect(0, i*16, MapEngine.TILEWIDTH, MapEngine.TILEHEIGHT);
				g.setColor(Color.WHITE);
			}
		}
		g.drawImage(ImageLoader.getImage("conveyer.png"), 0, 0, null);
		g.drawImage(ImageLoader.getImage("producer.png"), 0, 16, null);
		g.drawImage(ImageLoader.getImage("consumer.png"), 0, 32, null);
	}	
}

