package com.phamkhanh.mapdesign;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicButtonUI;

import com.phamkhanh.exception.SaveNotSuccessException;
import com.phamkhanh.object.Map;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

public class TabbedPane extends JTabbedPane{
	private int previousTabIndex = -1;
	private int currentTabIndex = -1;
	
	public TabbedPane(){
		setPreferredSize(new Dimension(976, 528));
		
		addChangeListener(new ChangeListener() {	
			@Override
			public void stateChanged(ChangeEvent e) {
				previousTabIndex = currentTabIndex;
				currentTabIndex = getSelectedIndex();
				if(previousTabIndex != -1){
					DesignPanel priviousTab = (DesignPanel)getComponentAt(previousTabIndex);
					priviousTab.pauseDesign();
				}
				getPanel().resumeDesign();
			}
		});
	}
	
	/**
	 * Trả về đối tượng DesignPanel trong tab đang được mở<br>
	 * Trả về <b>null</b> nếu không tab nào được chọn
	 * @author Khanh
	 */
	public DesignPanel getPanel(){
		return (DesignPanel)getSelectedComponent();
	}
	
	
	
	/**
	 * Tạo một tab designmap mới với title mặc định ban đầu *untitled.pvk <br>
	 * Tab mới được tạo ra ở cuối cùng <br>
	 * Dừng thiết kế tab đang hiển thị, sau đó chuyển TabSelected sang tab vừa tạo mới
	 * @param title title của map, sau khi lưu sẽ được đổi thành fileName của map
	 * @author Khanh
	 */
	public void newTab(){
		if(getPanel() != null) getPanel().pauseDesign();
		addTab("*untitled", new DesignPanel(this));
		try{
			setTabComponentAt(getTabCount() - 1, new ButtonTabComponent(this)); // IndexOutOfBoundsException | IllegalArgumentsException
			setSelectedIndex(getTabCount() - 1);
		}catch(IndexOutOfBoundsException | IllegalArgumentException e){
			JOptionPane.showMessageDialog(null, "Không tạo được tab mới");
		}
	}
	
	
	
	/**
	 * Save map ở tab thứ index <br>
	 * Nếu lưu map lần đầu, hiển thị hộp thoại chọn thư mục lưu map, đặt tên file, đồng thời chỉnh lại title tab <br>
	 * Nếu lưu map không phải lần đầu, chỉ update dữ liệu map lên file đã có <br>
	 * Nếu không tồn tại tab ở vị trí index thì bỏ qua <br>
	 * @param index chỉ số tab được lưu
	 * @author Khanh
	 */
	public void saveTab(int index){
		DesignPanel pnlDesign;
		try{
			pnlDesign = (DesignPanel) this.getComponentAt(index);  // ArrayOutOfBoundsException
		}catch(IndexOutOfBoundsException e){
			return;
		}
		pnlDesign.pauseDesign();
		Map map = pnlDesign.getMap();
		File file = null;
		File pictureFile = null;
		// map chưa được lưu thì bắt đầu lưu map
		if(!map.isSaved()){
			// Bản đồ chưa được lưu lần đầu tiên, hiển thị hộp thoại chọn thư mục lưu file
			if(map.getFile() == null){
				JFileChooser chooser = new JFileChooser();
				int ret = chooser.showSaveDialog(null);
				if(ret == JFileChooser.APPROVE_OPTION){
					file = chooser.getSelectedFile();
					map.setFile(file);  // SecurityException
					this.setTitleAt(index, "*"+file.getName());
				}else{
					return;
				}
			}

			// Lưu map vào file
			// Đồng thời update lại title của tab và thuộc tính saved của map
			try {
				map.save();
				map.setSaved(true); // file đã được save
				this.setTitleAt(index, map.getFile().getName() );  // Update lại title, tức bỏ dấu "*map1.pvk"
				// Lưu ảnh bản đồ cùng thư mục với file dữ liệu
				pictureFile = new File(map.getFile().getParentFile(), map.getFile().getName()+".png");
				try {
					ImageIO.write((RenderedImage) pnlDesign.getDbImage(), "png", pictureFile);
				} catch (IOException | IllegalArgumentException e) {
					e.printStackTrace();
				}
				return ;
			} catch (SaveNotSuccessException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, e.getMessage());
			}
		}//end save
		pnlDesign.resumeDesign();
	}
	
	
	
	/**
	 * Đóng tab ở vị trí index <br>
	 * Trước tiên stop design ở panel đó
	 * Nếu tab ở vị trí đó chưa được lưu, tiến hành lưu map trước, lưu không thành công thì cũng đóng tab <br>
	 * Nếu không tồn tại tab có chỉ số index thì bỏ qua
	 * @param index Vị trí tab muốn đóng
	 * @author Khanh
	 */
	public void closeTab(int index){
		saveTab(index);
		try{
			DesignPanel pnlDesign = (DesignPanel)getComponentAt(index);
			pnlDesign.stopDesign();
			removeTabAt(index); // IndexOutOfBoundsException
		}catch(IndexOutOfBoundsException e){
			e.printStackTrace();
		}
	}	
}

 
/**
 * Component to be used as tabComponent;
 * Contains a JLabel to show the text and
 * a JButton to close the tab it belongs to
 */
class ButtonTabComponent extends JPanel {
    private final TabbedPane pane;
 
    public ButtonTabComponent(final TabbedPane pane) {
        //unset default FlowLayout' gaps
        super(new FlowLayout(FlowLayout.LEFT, 0, 0));
        if (pane == null) {
            throw new NullPointerException("TabbedPane is null");
        }
        this.pane = pane;
        setOpaque(false);
         
        //make JLabel read titles from JTabbedPane
        JLabel label = new JLabel() {
            public String getText() {
                int i = pane.indexOfTabComponent(ButtonTabComponent.this);
                if (i != -1) {
                    return pane.getTitleAt(i);
                }
                return null;
            }
        };
         
        add(label);
        //add more space between the label and the button
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        //tab button
        JButton button = new TabButton();
        add(button);
        //add more space to the top of the component
        setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
    }
 
    private class TabButton extends JButton implements ActionListener {
        public TabButton() {
            int size = 17;
            setPreferredSize(new Dimension(size, size));
            setToolTipText("close this tab");
            //Make the button looks the same for all Laf's
            setUI(new BasicButtonUI());
            //Make it transparent
            setContentAreaFilled(false);
            //No need to be focusable
            setFocusable(false);
            setBorder(BorderFactory.createEtchedBorder());
            setBorderPainted(false);
            //Making nice rollover effect
            //we use the same listener for all buttons
            addMouseListener(buttonMouseListener);
            setRolloverEnabled(true);
            //Close the proper tab by clicking the button
            addActionListener(this);
        }
 
        public void actionPerformed(ActionEvent e) {
            int i = pane.indexOfTabComponent(ButtonTabComponent.this);
            if (i != -1) {
                pane.closeTab(i);
            }
        }
 
        //we don't want to update UI for this button
        public void updateUI() {
        }
 
        //paint the cross
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            //shift the image for pressed buttons
            if (getModel().isPressed()) {
                g2.translate(1, 1);
            }
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.BLACK);
            if (getModel().isRollover()) {
                g2.setColor(Color.MAGENTA);
            }
            int delta = 6;
            g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);
            g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta - 1);
            g2.dispose();
        }
    }
 
    private final static MouseListener buttonMouseListener = new MouseAdapter() {
        public void mouseEntered(MouseEvent e) {
            Component component = e.getComponent();
            if (component instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) component;
                button.setBorderPainted(true);
            }
        }
 
        public void mouseExited(MouseEvent e) {
            Component component = e.getComponent();
            if (component instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) component;
                button.setBorderPainted(false);
            }
        }
    };
}