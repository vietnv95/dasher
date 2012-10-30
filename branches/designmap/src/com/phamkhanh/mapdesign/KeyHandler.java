package com.phamkhanh.mapdesign;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
	private DesignPanel pnlDesign;
	private TabbedPane tabbedPane;

	public KeyHandler(DesignPanel pnlDesign) {
		super();
		this.pnlDesign = pnlDesign;
		this.tabbedPane = pnlDesign.getParent();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_ESCAPE || keyCode == KeyEvent.VK_END
				|| keyCode == KeyEvent.VK_Q
				|| (keyCode == KeyEvent.VK_C && e.isControlDown())) {
			// Back to choose map screen
			System.exit(0);
		}
		if(keyCode == KeyEvent.VK_X && e.isControlDown()){
			tabbedPane.closeTab(tabbedPane.getSelectedIndex());
		}
		if (keyCode == KeyEvent.VK_Z && e.isControlDown()) {
			pnlDesign.getHistory().undo();
		}
		if (keyCode == KeyEvent.VK_R && e.isControlDown()) {
			pnlDesign.getHistory().redo();
		}
		if(keyCode == KeyEvent.VK_S && e.isControlDown()){
			tabbedPane.saveTab(tabbedPane.getSelectedIndex());
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}

}
