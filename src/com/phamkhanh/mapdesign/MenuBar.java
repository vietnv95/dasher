package com.phamkhanh.mapdesign;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;

public class MenuBar extends JMenuBar {
	
	private JPopupMenu popupMenu;
	private JMenu fileMenu;
	private JMenu editMenu;
	private JMenu historyMenu;
	private JMenu toolMenu;
	private JMenu helpMenu;
	
	public MenuBar(){
		/** File menu New-Open File-Close-Close All-Save-Save As-Exit.*/
		fileMenu = new JMenu("File"); add(fileMenu); 
		fileMenu.setMnemonic(KeyEvent.VK_F);
			JMenuItem newItem = new JMenuItem("New"); 
				newItem.setMnemonic(KeyEvent.VK_N);
				newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_MASK));
				fileMenu.add(newItem);
			JMenuItem openFileItem = new JMenuItem("Open File"); 
				openFileItem.setMnemonic(KeyEvent.VK_O);
				openFileItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_MASK));
				fileMenu.add(openFileItem);
			fileMenu.addSeparator();
			JMenuItem closeItem = new JMenuItem("Close"); 
				closeItem.setMnemonic(KeyEvent.VK_L);
				closeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.CTRL_MASK));
				fileMenu.add(closeItem);
			JMenuItem closeAllItem = new JMenuItem("Close All"); 
				closeAllItem.setMnemonic(KeyEvent.VK_C); 
				closeAllItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.CTRL_MASK | KeyEvent.SHIFT_MASK));
				fileMenu.add(closeAllItem);
			fileMenu.addSeparator();
			JMenuItem saveItem = new JMenuItem("Save"); 
				saveItem.setMnemonic(KeyEvent.VK_S); 
				saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK));
				fileMenu.add(saveItem);
			JMenuItem saveAsItem = new JMenuItem("Save As"); 
				saveAsItem.setMnemonic(KeyEvent.VK_A); 
				fileMenu.add(saveAsItem);
			fileMenu.addSeparator();
			JMenuItem exitItem = new JMenuItem("Exit"); 
				exitItem.setMnemonic(KeyEvent.VK_E); 
				exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.ALT_MASK));
				exitItem.addActionListener(new ActionListener() {	
					@Override
					public void actionPerformed(ActionEvent arg0) {
						System.exit(0);	
					}
				});
				fileMenu.add(exitItem);
			
		/** Edit menu Cut-Copy-Paste-Indent-Unindent. */
		editMenu = new JMenu("Edit"); add(editMenu); 
		editMenu.setMnemonic(KeyEvent.VK_E);
			editMenu.addSeparator();
			JMenuItem cutItem = new JMenuItem("Cut");
				cutItem.setMnemonic(KeyEvent.VK_C);
				cutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_MASK));
				editMenu.add(cutItem);
			JMenuItem copyItem = new JMenuItem("Copy"); 
				copyItem.setMnemonic(KeyEvent.VK_O);
				copyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_MASK));
				editMenu.add(copyItem);
			JMenuItem pasteItem = new JMenuItem("Paste"); 
				pasteItem.setMnemonic(KeyEvent.VK_P);
				pasteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_MASK));
				editMenu.add(pasteItem);
			editMenu.addSeparator();
			JMenuItem indentItem = new JMenuItem("Indent"); editMenu.add(indentItem);
			JMenuItem unindentItem = new JMenuItem("UnIndent"); editMenu.add(unindentItem);
			
		
		/** History menu Undo-Redo. */
		historyMenu = new JMenu("History");add(historyMenu); 
		historyMenu.setMnemonic(KeyEvent.VK_H);
			JMenuItem undoItem = new JMenuItem("Undo"); 
				undoItem.setMnemonic(KeyEvent.VK_U);
				undoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_MASK));
				editMenu.add(undoItem);
			JMenuItem redoItem = new JMenuItem("Redo");
				redoItem.setMnemonic(KeyEvent.VK_R);
				redoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_MASK));
				editMenu.add(redoItem);
				
		/** Tool menu. */
		toolMenu = new JMenu("Tools"); add(toolMenu);
		toolMenu.setMnemonic(KeyEvent.VK_O);
			JMenuItem optionItem = new JMenuItem("Option"); toolMenu.add(optionItem);
			JMenuItem configItem = new JMenuItem("Configure");toolMenu.add(configItem);	
			
		/** Help menu. */
		helpMenu = new JMenu("Help"); add(helpMenu);
		helpMenu.setMnemonic(KeyEvent.VK_A);
			JMenuItem helpItem = new JMenuItem("Help"); helpMenu.add(helpItem);
			JMenuItem updateItem = new JMenuItem("Update"); toolMenu.add(updateItem);
			JMenuItem aboutItem = new JMenuItem("About"); helpMenu.add(aboutItem);
			aboutItem.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					JOptionPane.showMessageDialog(null, "Copyright-Nhom 3,Lop KSTN-CNTT-K55");
				}
			});
			
		/**Create Popup Menu.*/
		popupMenu = new JPopupMenu();
			popupMenu.add(newItem);
			popupMenu.add(openFileItem);
			popupMenu.addSeparator();
			popupMenu.add(undoItem);
			popupMenu.add(redoItem);
			popupMenu.addSeparator();
			popupMenu.add(optionItem);
			popupMenu.addSeparator();
			popupMenu.add(exitItem);
		
	}

	public JPopupMenu getPopupMenu() {
		return popupMenu;
	}
}
