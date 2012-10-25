package com.harry.login;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class PanelBegin extends JPanel {
	
	private final static int WIDTH = 976;
	private final static int HEIGHT = 388;
	
	Font normalFont = new Font("Times New Roman", Font.TYPE1_FONT, 20);
	Font note = new Font("Times New Roman", Font.ITALIC, 20);
	JButton userLogin = new JButton("Login");
	JButton accountCreation = new JButton("Create Account");
	JButton frameClose = new JButton("Close Game");
	static PanelLogin partLogin = new PanelLogin(0, 100, new JFrame());
	static PanelAccount partAccount = new PanelAccount();
	JFrame frame = new JFrame();
	
	private JPanel getPanel() {
		return this;
	}
	
	public static void main(String[] args) {
		JFrame f = new JFrame("Demo");
		PanelAccount partAccount = new PanelAccount();
		PanelLogin partLogin = new PanelLogin(0, 100, f);
		partLogin.setVisible(false);
		partAccount.setVisible(false);
		f.add(partAccount);
		f.add(partLogin);
		f.setSize(new Dimension(976, 488));
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(new PanelTitle(0, 100), BorderLayout.NORTH);
		f.add(new PanelBegin(0, 100, f, partLogin, partAccount), BorderLayout.CENTER);
		f.setVisible(true);
	}

	public PanelBegin(int x, int y, JFrame frame, PanelLogin partLogin, PanelAccount partAccount) {
		this.frame = frame;
		this.partAccount = partAccount;
		this.partLogin = partLogin;
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setLocation(x, y);
		this.setLayout(null);
		this.setBackground(Color.white);
		this.setVisible(true);

		makeGUI();
		handleEvent();
	}
	
	private void makeGUI() {
		
		JPanel containerPanel = new JPanel();
		userLogin.setFont(normalFont);
		userLogin.setBounds(364, 80, 200, 50);
		userLogin.setToolTipText("Login");
		accountCreation.setFont(normalFont);
		accountCreation.setBounds(364, 160, 200, 50);
		accountCreation.setToolTipText("Create Account");
		frameClose.setFont(normalFont);
		frameClose.setBounds(364, 240, 200, 50);
		frameClose.setToolTipText("Close Game");
		containerPanel.add(userLogin);
		containerPanel.add(accountCreation);
		containerPanel.add(frameClose);
		this.add(userLogin);
		this.add(accountCreation);
		this.add(frameClose);
	}
	
	public void setPartLogin(PanelLogin partLogin) {
		this.partLogin = partLogin;
	}
	
	public void setPartAccount(PanelAccount partAccount) {
		this.partAccount = partAccount;
	}
	
	private void handleEvent() {
		userLogin.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
			    getPanel().setVisible(false);
				partLogin.setVisible(true);
				PanelTitle.updateTitle(1);
			}
		});
		
		accountCreation.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				getPanel().setVisible(false);
				partAccount.setVisible(true);
				PanelTitle.updateTitle(2);
			}
		});
		
		frameClose.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				System.exit(0);
			}
		});
	}
}
