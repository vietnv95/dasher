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
import com.phamkhanh.object.Consumer;
import com.phamkhanh.object.Conveyer;
import com.phamkhanh.object.Producer;
import com.phamkhanh.object.Tile;

public class TilePanel extends JPanel {
	private static final int PWIDTH = 32;
	private static final int PHEIGHT = 488;
	
	private int numberTile = 3;
	private int indexSelected = -1;
	private Tile[] tiles = new Tile[numberTile];
	
	
	public TilePanel(final DesignPanel designPanel){
		setDoubleBuffered(true);
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(PWIDTH, PHEIGHT));
		
		// Load tiles
		tiles[0] = new Conveyer(new Point(0,0), ImageLoader.getImage("conveyer.png"), null);
		tiles[1] = new Producer(new Point(0,4), ImageLoader.getImage("producer.png"),null, null);
		tiles[2] = new Consumer(new Point(0,6), ImageLoader.getImage("consumer.png"), null, null);
		
		// Handler mouse event
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {}
			
			@Override
			public void mouseExited(MouseEvent arg0) {}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				int index = e.getY() / MapEngine.TILEHEIGHT;
				if(index >= 0 && index < numberTile){
					indexSelected = index;
					designPanel.setTileSelected(tiles[indexSelected]);
					repaint();
				}
				
			}
		});
 	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		for(int i = 0; i < numberTile; i++){
			if(i != indexSelected){
				g.drawImage(tiles[i].getImage(), 0, i*16, null);
			}else{
				// Ve tileSelected voi background mau do de danh dau selected
				g.setColor(Color.BLACK);
				g.fillRect(0, i*16, MapEngine.TILEWIDTH, MapEngine.TILEHEIGHT);
				g.setColor(Color.WHITE);
				g.drawImage(tiles[i].getImage(), 0, i*16, null);
			}
		}
	}
	
	
	
}

