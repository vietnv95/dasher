package com.phamkhanh.mapdesign;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
	private DesignPanel designPanel;

	public KeyHandler(DesignPanel designPanel) {
		super();
		this.designPanel = designPanel;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_ESCAPE || keyCode == KeyEvent.VK_END
				|| keyCode == KeyEvent.VK_Q
				|| (keyCode == KeyEvent.VK_C && e.isControlDown())) {
			designPanel.stopDesign();
		}
		if (keyCode == KeyEvent.VK_Z && e.isControlDown()) {
			designPanel.getHistory().undo();
		}
		if (keyCode == KeyEvent.VK_R && e.isControlDown()) {
			designPanel.getHistory().redo();
		}
		if(keyCode == KeyEvent.VK_S && e.isControlDown()){
			designPanel.saveMap();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

}
