package com.harry.login;

import javax.swing.JFrame;
import javax.swing.JPanel;
import com.harry.data.GameAccess;

public class PanelAccount extends JPanel {
	public PanelAccount() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		GameAccess gameAccess = new GameAccess(new JFrame());
		gameAccess.getContain().setVisible(true);
	}
}
