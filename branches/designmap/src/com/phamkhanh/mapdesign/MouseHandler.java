package com.phamkhanh.mapdesign;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

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
	private DesignPanel pnlDesign;

	private Logger logger = Logger.getLogger(MouseHandler.class.getName());
	
	public MouseHandler(DesignPanel pnlDesign) {
		this.pnlDesign = pnlDesign;
		logger.setLevel(Level.ALL);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		pnlDesign.ptMouse.setLocation(MapEngine.mouseMap(e.getPoint()));
	}

	@Override
	public void mousePressed(MouseEvent e) {
		pnlDesign.isPressed = true;
		pnlDesign.ptHeadPixel.setLocation(e.getPoint());
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		pnlDesign.isDragged = true;
		pnlDesign.ptTailPixel.setLocation(e.getPoint());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		pnlDesign.ptTailPixel.setLocation(e.getPoint());
		if (pnlDesign.getTileSelected() != null) {
			// Act Commands to Change Map Element (Through Create Commands
			// Object).
			if (pnlDesign.isDragged
					&& pnlDesign.getTileSelected().getClass() == Conveyer.class) {
				Point ptHead = MapEngine.mouseMap(pnlDesign.ptHeadPixel);
				Point ptTail = MapEngine.mouseMap(pnlDesign.ptTailPixel);
				Direction direction = MapEngine.tileDirecter(ptHead, ptTail);
				if ((direction == Direction.SOUTHEAST
						|| direction == Direction.SOUTHWEST
						|| direction == Direction.NORTHEAST || direction == Direction.NORTHWEST)
						&& (pnlDesign.getMap().getCell(ptHead) != null && pnlDesign
								.getMap().getCell(ptTail) != null)) {
					// AddConveyersCommand (After User Drag A Conveyer Segment
					// by Certain Direction)
					Command add = new AddConveyersCommand(pnlDesign);
					add.execute();
					pnlDesign.getHistory().add(add);
					logger.info(pnlDesign.getHistory().toString());
				}
			} else {
				// AddProducerCommand OR AddConsumerCommand (User Clicked mouse
				// NOT Drag)
				if (pnlDesign.getTileSelected().getClass() == Producer.class) {
					Command add = new AddProducerCommand(pnlDesign);
					add.execute();
					pnlDesign.getHistory().add(add);
					logger.info(pnlDesign.getHistory().toString());
				} else if (pnlDesign.getTileSelected().getClass() == Consumer.class) {
					Command add = new AddConsumerCommand(pnlDesign);
					add.execute();
					pnlDesign.getHistory().add(add);
					logger.info(pnlDesign.getHistory().toString());
				}
			}
		}
		pnlDesign.isPressed = false;
		pnlDesign.isDragged = false;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		pnlDesign.ptMouse.setLocation(e.getPoint());
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

}
