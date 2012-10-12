package com.phamkhanh.mapdesign;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.phamkhanh.mapdesign.command.AddConsumerCommand;
import com.phamkhanh.mapdesign.command.AddConveyersCommand;
import com.phamkhanh.mapdesign.command.AddProducerCommand;
import com.phamkhanh.mapdesign.command.Command;
import com.phamkhanh.mapengine.Direction;
import com.phamkhanh.mapengine.MapEngine;
import com.phamkhanh.object.Consumer;
import com.phamkhanh.object.Conveyer;
import com.phamkhanh.object.Producer;

public class MouseHandler implements MouseListener, MouseMotionListener {
	private DesignPanel designPanel;

	public MouseHandler(DesignPanel designPanel) {
		this.designPanel = designPanel;
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {
		designPanel.ptMouse.setLocation(MapEngine.mouseMap(e.getPoint()));
	}

	@Override
	public void mousePressed(MouseEvent e) {
		designPanel.isPressed = true;
		designPanel.ptHeadPixel.setLocation(e.getPoint());
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		designPanel.isDragged = true;
		designPanel.ptTailPixel.setLocation(e.getPoint());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		designPanel.ptTailPixel.setLocation(e.getPoint());
		if(designPanel.getTileSelected() != null){
			// Act Commands to Change Map Element (Through Create Commands Object).
			if(designPanel.isDragged && designPanel.getTileSelected().getClass() == Conveyer.class){
				Point ptHead = MapEngine.mouseMap(designPanel.ptHeadPixel);
				Point ptTail = MapEngine.mouseMap(designPanel.ptTailPixel);
				Direction direction = MapEngine.tileDirecter(ptHead, ptTail);
				if( (direction == Direction.SOUTHEAST || 
					direction == Direction.SOUTHWEST ||
					direction == Direction.NORTHEAST ||
					direction == Direction.NORTHWEST) 
					&&
					(designPanel.getMap().getCell(ptHead) != null && 
					designPanel.getMap().getCell(ptTail) != null) ){
					// AddConveyersCommand (After User Drag A Conveyer Segment by Certain Direction)
					Command add = new AddConveyersCommand(designPanel);
					add.execute();
					designPanel.getHistory().add(add);
				}
			}else{
				// AddProducerCommand OR AddConsumerCommand (User Clicked mouse NOT Drag)
				if(designPanel.getTileSelected().getClass() == Producer.class){
					Command add = new AddProducerCommand(designPanel);
					add.execute();
					designPanel.getHistory().add(add);
					System.out.println(designPanel.getHistory());
				}else if(designPanel.getTileSelected().getClass() == Consumer.class){
					Command add = new AddConsumerCommand(designPanel);
					add.execute();
					designPanel.getHistory().add(add);
				}
			}
		}
		designPanel.isPressed = false;
		designPanel.isDragged = false;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		designPanel.ptMouse.setLocation(e.getX(), e.getY());
	}

}
