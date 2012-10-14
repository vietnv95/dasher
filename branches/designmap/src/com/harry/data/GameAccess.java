package com.harry.data;

import java.awt.Color;
import java.awt.Container;
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
import javax.swing.border.LineBorder;

import com.phamkhanh.main.BarPanel;
import com.phamkhanh.main.MainFrame;

public class GameAccess {
	
	public final static int FRAME_WIDTH = 976;
	public final static int FRAME_HEIGHT = 488;
	public final static int COMPONENT_HEIGHT = 243;
	public final static int BORDER_SIZE = 2;
	
	private String playerName = "";
	Font titleFont = new Font("Times New Roman", Font.BOLD, 25);
	Font normalFont = new Font("Time New Roman", Font.TYPE1_FONT, 20);
	Font bigTitle = new Font("Time New Roman", Font.BOLD, 60);
	Font note = new Font("Times New Roman", Font.ITALIC, 20);
	JTextField userText = new JTextField(20);
	JPasswordField passwordText = new JPasswordField(20);
	JButton accountCreation = new JButton("Create Account");
	JButton userLogin = new JButton("Login");
	JButton frameClose = new JButton("Exit Game");
	JButton buttonLogin = new JButton("Sign In");
	JButton[] buttonBack = new JButton[2];
	JTextField newAccountText = new JTextField(20);
	JButton newAccountCreation = new JButton("Create Account");
	JPasswordField newPasswordText = new JPasswordField(20);
	JPasswordField passwordConfirmText = new JPasswordField(20);
	private static JFrame contain = new JFrame();
	JPanel[] mainPanel = new JPanel[4];
	JLabel mainTitle = new JLabel("Conveyer Game");
	JLabel mainTitleLogin = new JLabel("Login");
	JLabel mainTitleCreation = new JLabel("Create Account");
	JLabel accountPasswordError = new JLabel("Error password!");
	JLabel accountCreationSuccess = new JLabel("Account create success!");
	JLabel accountNameError = new JLabel("This name's account is invalid!");
	JLabel accountLoginError = new JLabel("Account don't correct!");
	JLabel accountLoginSuccess = new JLabel("Login success!");
	
	public static void main(String[] args) {
		GameAccess newInstance = new GameAccess(getContain());
		GameAccess.getContain().setVisible(true);
	}
	
	public GameAccess(JFrame frame) {
		frame = this.getContain();
		XmlProcessor.createFile();
		getContain().setSize(FRAME_WIDTH, FRAME_HEIGHT);
		getContain().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContain().setLocationRelativeTo(null);
		getContain().setResizable(false);
		getContain().setTitle("Dung Harry");
		
		accessGame();
	}

	public void accessGame() {
		mainTitleLogin.setFont(bigTitle);
		mainTitleCreation.setFont(bigTitle);
		accountPasswordError.setFont(note);
		accountPasswordError.setForeground(Color.red);
		accountPasswordError.setBounds(640, 285, 300, 20);
		accountCreationSuccess.setFont(note);
		accountCreationSuccess.setForeground(Color.BLUE);
		accountCreationSuccess.setBounds(350, 420, 200, 20);
		accountNameError.setFont(note);
		accountNameError.setForeground(Color.red);
		accountNameError.setBounds(640, 145, 300, 20);
		accountLoginError.setFont(note);
		accountLoginError.setForeground(Color.red);
		accountLoginError.setBounds(380, 280, 200, 50);
		accountLoginError.setVisible(false);
		accountLoginSuccess.setFont(note);
		accountLoginSuccess.setForeground(Color.blue);
		accountLoginSuccess.setBounds(400, 280, 200, 50);
		accountLoginSuccess.setVisible(false);

		mainPanel[0] = new JPanel();
		mainPanel[1] = new JPanel();
		mainPanel[2] = new JPanel();
		mainPanel[3] = new JPanel();
		
		buttonBack[0] = new JButton("Back");
		buttonBack[1] = new JButton("Back");
		
		mainPanel[0].setSize(new Dimension(978, 100));
		mainTitle.setFont(bigTitle);
		mainPanel[0].setLocation(0, 0);
		mainPanel[0].setBackground(Color.lightGray);
		mainPanel[0].add(mainTitle);
		mainPanel[0].setBorder(LineBorder.createGrayLineBorder());
		
		mainPanel[1].setSize(new Dimension(978, 388));
		mainPanel[1].setLocation(0, 100);
		mainPanel[1].setBackground(Color.white);
		mainPanel[1].setLayout(null);
		JPanel containerPanel = new JPanel();
		userLogin.setFont(normalFont);
		userLogin.setBounds(364, 100, 200, 50);
		userLogin.setToolTipText("Login");
		accountCreation.setFont(normalFont);
		accountCreation.setBounds(364, 180, 200, 50);
		accountCreation.setToolTipText("Create Account");
		frameClose.setFont(normalFont);
		frameClose.setBounds(364, 260, 200, 50);
		frameClose.setToolTipText("Close Game");
		containerPanel.add(userLogin);
		containerPanel.add(accountCreation);
		containerPanel.add(frameClose);
		mainPanel[1].add(userLogin);
		mainPanel[1].add(accountCreation);
		mainPanel[1].add(frameClose);
		
		mainPanel[2].setSize(new Dimension(978, 388));
		mainPanel[2].setLocation(0, 100);
		mainPanel[2].setBackground(Color.white);
		mainPanel[2].setLayout(null);
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
		buttonBack[0].setFont(normalFont);
		buttonBack[0].setToolTipText("Back");
		buttonBack[0].setBounds(484, 220, 150, 50);
		mainPanel[2].add(playerName);
		mainPanel[2].add(userText);
		mainPanel[2].add(playerPassword);
		mainPanel[2].add(passwordText);
		mainPanel[2].add(buttonLogin);
		mainPanel[2].add(buttonBack[0]);
		mainPanel[2].add(accountLoginError);
		mainPanel[2].add(accountLoginSuccess);
		mainPanel[2].setVisible(false);
		
		mainPanel[3].setSize(new Dimension(978, 388));
		mainPanel[3].setBackground(Color.white);
		mainPanel[3].setLocation(0, 100);
		mainPanel[3].setLayout(null);
		JLabel newAccountName = new JLabel("Player Name          :");
		newAccountName.setFont(normalFont);
	    newAccountName.setBounds(244, 150, 200, 20);
	    newAccountText.setFont(normalFont);
	    newAccountText.setBounds(434, 140, 200, 40);
	    JLabel newPassword = new JLabel("Password              :");
	    newPassword.setFont(normalFont);
	    newPassword.setBounds(244, 220, 200, 20);
	    newPasswordText.setFont(normalFont);
	    newPasswordText.setBounds(434, 210, 200, 40);
	    JLabel passwordConfirm = new JLabel("Confirm Password :");
	    passwordConfirm.setFont(normalFont);
	    passwordConfirm.setBounds(244, 290, 200, 20);
	    passwordConfirmText.setFont(normalFont);
	    passwordConfirmText.setBounds(434, 280, 200, 40);
	    newAccountCreation.setFont(normalFont);
	    newAccountCreation.setToolTipText("Create New Account");
	    newAccountCreation.setBounds(244, 350, 200, 50);
	    buttonBack[1].setFont(normalFont);
	    buttonBack[1].setBounds(450, 350, 200, 50);
	    buttonBack[1].setToolTipText("Back");
	    mainPanel[3].add(newAccountName);
	    mainPanel[3].add(newAccountText);
	    mainPanel[3].add(newPassword);
	    mainPanel[3].add(newPasswordText);
	    mainPanel[3].add(passwordConfirm);
	    mainPanel[3].add(passwordConfirmText);
	    mainPanel[3].add(newAccountCreation);
	    mainPanel[3].add(buttonBack[1]);
	    mainPanel[3].add(accountCreationSuccess);
	    mainPanel[3].add(accountNameError);
	    mainPanel[3].add(accountPasswordError);
	    accountCreationSuccess.setVisible(false);
	    accountNameError.setVisible(false);
	    accountPasswordError.setVisible(false);
	    mainPanel[3].setVisible(false);
		
		getContain().add(mainPanel[0]);
		getContain().add(mainPanel[1]);
		getContain().add(mainPanel[2]);
		getContain().add(mainPanel[3]);
		
		handleEvent();
	}
	
	private void hideVisible() {
		accountCreationSuccess.setVisible(false);
		accountNameError.setVisible(false);
		accountPasswordError.setVisible(false);
		userText.setText("");
		passwordText.setText("");
		passwordConfirmText.setText("");
		newAccountText.setText("");
		newPasswordText.setText("");
		accountLoginError.setVisible(false);
		accountLoginSuccess.setVisible(false);
	}
	
	private void handleEvent() {
		userLogin.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				mainTitle.setVisible(false);
				mainTitleLogin.setVisible(true);
				mainPanel[0].add(mainTitleLogin);
				
				mainPanel[1].setVisible(false);
				mainPanel[2].setVisible(true);
			}
		});
		
		accountCreation.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				mainTitle.setVisible(false);
				mainTitleCreation.setVisible(true);
				mainPanel[0].add(mainTitleCreation);
				
				mainPanel[1].setVisible(false);
				mainPanel[3].setVisible(true);
			}
		});
		
		buttonBack[0].addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				mainTitleLogin.setVisible(false);
				mainTitle.setVisible(true);
				mainPanel[0].add(mainTitle);
				
				mainPanel[2].setVisible(false);
				mainPanel[1].setVisible(true);
				hideVisible();
			}
		});
		
		buttonBack[1].addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				mainTitleCreation.setVisible(false);
				mainTitle.setVisible(true);
				mainPanel[0].add(mainTitle);
				
				mainPanel[3].setVisible(false);
				mainPanel[1].setVisible(true);
				hideVisible();
			}
		});
		
		buttonLogin.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				String valueName = userText.getText();
				String valuePassword = new String(passwordText.getPassword());
				
				if(XmlProcessor.checkSignIn(valueName, valuePassword)) {
					MainFrame frameCreation = new MainFrame(valueName);
					accountLoginError.setVisible(false);
					accountLoginSuccess.setVisible(true);
					getContain().dispose();
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
		
		newAccountCreation.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				String valueName = newAccountText.getText();
				String valuePassword = new String(newPasswordText.getPassword());
				String valuePasswordConfirm = new String(passwordConfirmText.getPassword());
				
				if( !valuePassword.equals(valuePasswordConfirm) || valuePassword.equals("") ) {
					accountCreationSuccess.setVisible(false);
					accountNameError.setVisible(false);
					accountPasswordError.setVisible(true);
					mainPanel[3].add(accountPasswordError);
				}
				else if(valueName.equals("") || !XmlProcessor.checkCreateAccount(valueName)) {
					accountPasswordError.setVisible(false);
					accountCreationSuccess.setVisible(false);
					accountNameError.setVisible(true);
					mainPanel[3].add(accountNameError);
				}
				else {
					XmlProcessor.createUser(valueName, valuePassword);
					accountNameError.setVisible(false);
					accountPasswordError.setVisible(false);
					accountCreationSuccess.setVisible(true);
					mainPanel[3].add(accountCreationSuccess);
				} 
			}
		});
		
		frameClose.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
			    getContain().setVisible(false);
			    getContain().dispose();
			}
		});
	}
	
	public String getPlayerName() {
		return playerName;
	}

	public static JFrame getContain() {
		return contain;
	}

	public static void setContain(JFrame contain) {
		GameAccess.contain = contain;
	}

}