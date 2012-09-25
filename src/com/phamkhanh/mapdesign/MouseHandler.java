package com.phamkhanh.mapdesign;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.phamkhanh.mapdesign.command.AddConsumerCommand;
import com.phamkhanh.mapdesign.command.AddConveyersCommand;
import com.phamkhanh.mapdesign.command.AddProducerCommand;
import com.phamkhanh.mapdesign.command.Command;
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
		designPanel.ptMouse.setLocation(e.getX(), e.getY());
	}

	@Override
	public void mousePressed(MouseEvent e) {
		designPanel.isPressed = true;
		designPanel.ptHeadPixel.setLocation(e.getX(), e.getY());
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		designPanel.isDragged = true;
		designPanel.ptTailPixel.setLocation(e.getX(), e.getY());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		designPanel.ptTailPixel.setLocation(e.getX(), e.getY());
		if(designPanel.getTileSelected() != null){
			// Act Commands to Change Map Element (Through Create Commands Object).
			if(designPanel.isDragged && designPanel.getTileSelected().getClass() == Conveyer.class){
				// AddConveyersCommand (After User Drag A Conveyer Segment by Certain Direction)
				Command add = new AddConveyersCommand(designPanel);
				add.execute();
				designPanel.history.add(add);
				System.out.println(designPanel.history);
				
			}else{
				// AddProducerCommand OR AddConsumerCommand (User Clicked mouse NOT Drag)
				if(designPanel.getTileSelected().getClass() == Producer.class){
					Command add = new AddProducerCommand(designPanel);
					add.execute();
					designPanel.history.add(add);
					System.out.println(designPanel.history);
				}else if(designPanel.getTileSelected().getClass() == Consumer.class){
					Command add = new AddConsumerCommand(designPanel);
					add.execute();
					designPanel.history.add(add);
					System.out.println(designPanel.history);
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
