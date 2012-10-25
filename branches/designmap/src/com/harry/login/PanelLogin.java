package com.harry.login;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.harry.data.XmlProcessor;
import com.phamkhanh.main.ChooseMapFrame;

public class PanelLogin extends JPanel {
	private final static int WIDTH = 976;
	private final static int HEIGHT = 388;
	private String playerName = "";
	
	Font normalFont = new Font("Times New Roman", Font.TYPE1_FONT, 20);
	Font note = new Font("Times New Roman", Font.ITALIC, 20);
	JTextField userText = new JTextField(20);
	JPasswordField passwordText = new JPasswordField(20);
	JButton buttonLogin = new JButton("Sign In");
	JButton buttonBack = new JButton("Back");
	JLabel accountLoginError = new JLabel("Account don't correct!");
	JLabel accountLoginSuccess = new JLabel("Login success!");
	JFrame frame = new JFrame();
	
	public PanelLogin(int x, int y, JFrame frame) {
		this.frame = frame;
		
		this.setSize(new Dimension(WIDTH, HEIGHT));
		this.setBackground(Color.white);
		this.setLocation(x, y);
		this.setVisible(true);
		frame.add(this);
		makeGUI();
		handleEvent();
	}
	
	private void makeGUI() {
		accountLoginError.setFont(note);
		accountLoginError.setForeground(Color.red);
		accountLoginError.setBounds(380, 280, 200, 50);
		accountLoginError.setVisible(false);
		accountLoginSuccess.setFont(note);
		accountLoginSuccess.setForeground(Color.blue);
		accountLoginSuccess.setBounds(400, 280, 200, 50);
		accountLoginSuccess.setVisible(false);
		this.setLayout(null);
		JLabel playerName = new JLabel("Player Name:");
		playerName.setFont(normalFont);
		playerName.setBounds(304, 80, 140, 20);
		userText.setFont(normalFont);
		userText.setBounds(434, 70, 200, 40);
		JLabel playerPassword = new JLabel("Password    :");
		playerPassword.setFont(normalFont);
		playerPassword.setBounds(304, 160, 140, 20);
		passwordText.setFont(normalFont);
		passwordText.setBounds(434, 150, 200, 40);
		buttonLogin.setFont(normalFont);
		buttonLogin.setToolTipText("Sign In");
		buttonLogin.setBounds(304, 220, 150, 50);
		buttonBack.setFont(normalFont);
		buttonBack.setToolTipText("Back");
		buttonBack.setBounds(484, 220, 150, 50);
		this.add(playerName);
		this.add(userText);
		this.add(playerPassword);
		this.add(passwordText);
		this.add(buttonLogin);
		this.add(buttonBack);
		this.add(accountLoginError);
		this.add(accountLoginSuccess);
	}
	
	public JPanel getPanel() {
		return this;
	}
	
	public void hideVisible() {
		userText.setText("");
		passwordText.setText("");
		accountLoginError.setVisible(false);
		accountLoginSuccess.setVisible(false);
	}
	
	private void handleEvent() {
		buttonLogin.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				String valueName = userText.getText();
				String valuePassword = new String(passwordText.getPassword());
				
				if(XmlProcessor.checkSignIn(valueName, valuePassword)) {
					ChooseMapFrame frameCreation = new ChooseMapFrame(valueName);
					accountLoginError.setVisible(false);
					accountLoginSuccess.setVisible(true);
					frame.dispose();
					frameCreation.setLocationRelativeTo(null);
					playerName = valueName;
					//hideVisible();
				}
				else {
					accountLoginSuccess.setVisible(false);
					accountLoginError.setVisible(true);
				}
			}
		});
		
		buttonBack.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				PanelTitle.updateTitle(0);
				PanelBegin partBegin = new PanelBegin(0, 100, frame, new PanelLogin(0, 100, frame), new PanelAccount());

				getPanel().setVisible(false);
				frame.add(partBegin);
				partBegin.setVisible(true);
				hideVisible();
			}
		});
	}
}
