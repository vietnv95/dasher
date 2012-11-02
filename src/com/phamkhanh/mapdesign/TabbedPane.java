package com.phamkhanh.mapdesign;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicButtonUI;

import com.phamkhanh.exception.MapErrorException;
import com.phamkhanh.exception.SaveNotSuccessException;
import com.phamkhanh.object.Map;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

public class TabbedPane extends JTabbedPane {
	/**
	 * Chỉ số index của tab được active lúc trước, được set khi fireStateChange event
	 * nhằm mục đích pause những tab không active
	 */
	private static final int TABBEDPANE_WIDTH = 976;
	private static final int TABBEDPANE_HEIGHT = 528;
	
	private int previousTabIndex = -1;
	
	/**
	 * Chỉ số index của tab hiện tại active, được set khi fireStateChange event
	 * nhằm mục đích active design panel tab hiện tại (selectedIndex)
	 */
	private int currentTabIndex = -1;

	/**
	 * Constructor
	 * addChangeListener, nhằm mục đích khi người dùng thay đổi sang tab khác thì pauseDesign tab vừa xong
	 * và resumeDesign tab vừa mới chọn 
	 */
	public TabbedPane() {
		setPreferredSize(new Dimension(TABBEDPANE_WIDTH, TABBEDPANE_HEIGHT));

		addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				previousTabIndex = currentTabIndex;
				currentTabIndex = getSelectedIndex();
				if (previousTabIndex > -1 && previousTabIndex < getTabCount()) {
					DesignPanel priviousTab = (DesignPanel) getComponentAt(previousTabIndex);
					priviousTab.pauseDesign();
				}
				if (currentTabIndex > -1 && currentTabIndex < getTabCount()) {
					getCurrentTab().resumeDesign();
					getCurrentTab().requestFocusInWindow();
				}
			}
		});
	}

	public DesignPanel getCurrentTab() {
		return (DesignPanel) getSelectedComponent();
	}

	/**
	 * Tạo một tab designmap mới với title mặc định ban đầu *untitled.pvk <br>
	 * Tab mới được tạo ra ở cuối cùng <br>
	 * Dừng thiết kế tab đang hiển thị, sau đó chuyển TabSelected sang tab vừa
	 * tạo mới
	 * 
	 * @param title
	 *            title của map, sau khi lưu sẽ được đổi thành fileName của map
	 * @author Khanh
	 */
	public void newTab() {
		if (getCurrentTab() != null)
			getCurrentTab().pauseDesign();
		// thêm tab mới với title là "*untitled"
		addTab("*untitled", new DesignPanel(this));
		try {
			setTabComponentAt(getTabCount() - 1, new ButtonTabComponent(this)); // IndexOutOfBoundsException
																				// IllegalArgumentsException
			setSelectedIndex(getTabCount() - 1); // fireStateChange listener
		} catch (IndexOutOfBoundsException | IllegalArgumentException e) {
			JOptionPane.showMessageDialog(null, "Không tạo được tab mới");
		}
	}
	
	
	/**
	 * Hiển thị Dialog mở file chứa thông tin bản đồ, <br>
	 * Nếu người dùng CANCEL thì không làm gì cả<br>
	 * Load thông tin bản đồ từ file và khởi tạo đối tượng DesignPanel tương ứng<br>
	 * Tạo tab mới, thêm DesignPanel vừa tạo, active tab vừa tạo<br>
	 */
	public void openTab(){
		DesignPanel pnlDesign = new DesignPanel(this);
		Map map = pnlDesign.getMap();
		File file = null;
		// Hiển thị Dialog mở file
		JFileChooser chooser = new JFileChooser();
		int ret = chooser.showOpenDialog(null);
		if (ret == JFileChooser.APPROVE_OPTION) {
			file = chooser.getSelectedFile();	
		} else return;
		
		try {
			// Sau khi chọn file, tiến hành load thông tin bản đồ 
			pnlDesign.unmarshalXML(file.getAbsolutePath());
			//map.load(file.getAbsolutePath());
			//pnlDesign.loadXML(file.getAbsolutePath());
			map.setSaved(true);
			// Mở tab mới với title và pnlDesign tương ứng
			addTab(file.getName(), pnlDesign);
			setTabComponentAt(getTabCount() - 1, new ButtonTabComponent(this));    // IndexOutOfBoundsException
																				   // IllegalArgumentsException
			setSelectedIndex(getTabCount() - 1);       // fireStateChange listener
		} catch (IndexOutOfBoundsException | IllegalArgumentException | MapErrorException e) {
			JOptionPane.showMessageDialog(null, "Không tạo được tab mới");
			e.printStackTrace();
			return;
		}
	}
	

	/**
	 * Save map ở tab thứ index <br>
	 * Nếu lưu map lần đầu, hiển thị hộp thoại chọn thư mục lưu map, đặt tên
	 * file, đồng thời chỉnh lại title tab <br>
	 * Nếu lưu map không phải lần đầu, chỉ update dữ liệu map lên file đã có <br>
	 * Nếu không tồn tại tab ở vị trí index thì bỏ qua <br>
	 * 
	 * @param index
	 *            chỉ số tab được lưu
	 * @author Khanh
	 */
	public void saveTab(int index) {
		DesignPanel pnlDesign;
		try {
			pnlDesign = (DesignPanel) this.getComponentAt(index); // ArrayOutOfBoundsException
		} catch (IndexOutOfBoundsException e) {
			return;
		}
		Map map = pnlDesign.getMap();
		File file = null;
		File pictureFile = null;

		if (!map.isSaved()) {
			// Bản đồ chưa được lưu lần đầu tiên, hiển thị hộp thoại chọn thư
			// mục lưu file
			if (map.getFile() == null) {
				JFileChooser chooser = new JFileChooser();
				int ret = chooser.showSaveDialog(null);
				if (ret == JFileChooser.APPROVE_OPTION) {
					file = chooser.getSelectedFile();
					map.setFile(file); // SecurityException
					this.setTitleAt(index, "*" + file.getName());
				} else {
					return;
				}
			}

			try {
				pnlDesign.marshalXML();
				//map.save();  // SaveNotSuccessException
				//pnlDesign.saveXML();
				map.setSaved(true); 
				this.setTitleAt(index, map.getFile().getName() );  

				map.setSaved(true); // file đã được save
				this.setTitleAt(index, map.getFile().getName()); // Update lại
																	// title,
																	// tức bỏ
																	// dấu
																	// "*map1.pvk"
				// Lưu ảnh bản đồ cùng thư mục với file dữ liệu
				pictureFile = new File(map.getFile().getParentFile(), map
						.getFile().getName() + ".png");
				try {
					ImageIO.write((RenderedImage) pnlDesign.getDbImage(),
							"png", pictureFile);
				} catch (IOException | IllegalArgumentException e) {
					System.out.println("Lỗi lưu file ảnh");
					e.printStackTrace();
				}
				
				
			} catch (SaveNotSuccessException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, e.getMessage());
			}
		}// end save

	}
	
	// Lưu thông tin metadata vào file xml
	protected void saveMetaDataMap(String userName, int level, String mapPath, String picturePath){
		
	}

	/**
	 * Đóng tab ở vị trí index <br>
	 * Trước tiên stop design ở panel đó Nếu tab ở vị trí đó chưa được lưu, tiến
	 * hành lưu map trước, lưu không thành công thì cũng đóng tab <br>
	 * Nếu không tồn tại tab có chỉ số index thì bỏ qua
	 * 
	 * @param index
	 *            Vị trí tab muốn đóng
	 * @author Khanh
	 */
	public void closeTab(int index) {
		try {
			DesignPanel pnlDesign = (DesignPanel) getComponentAt(index);  //IndexOutOfBoundsException
			Map map = pnlDesign.getMap();
			// Nếu map chưa lưu,hỏi người dùng xem có muốn lưu map trước không
			if (!map.isSaved()) {
				int ret = JOptionPane.showConfirmDialog(null,
						"Bạn có muốn lưu bản đồ không?", "Hộp thoại xác nhận",
						JOptionPane.YES_NO_CANCEL_OPTION);
				if (ret == JOptionPane.YES_OPTION) {
					saveTab(index);
				} else if (ret == JOptionPane.CANCEL_OPTION) {
					return;
				}
			}
			pnlDesign.stopDesign();
			removeTabAt(index); // IndexOutOfBoundsException
			
			// Nếu đóng tab hiện tại, thì chuyển focus về tab trước nó (nếu
			// không còn tab, currentTabIndex = -1
			if (index == currentTabIndex) {
				setSelectedIndex(currentTabIndex - 1);   // fireStateChange event
			}else 
				setSelectedIndex(currentTabIndex);  // fireStateChange event
			
		} catch (IndexOutOfBoundsException e) {
			System.out.println("ArrayOutOfBoundsException in closeTab, index = " + index);
			e.printStackTrace();
		}
	}
}

/**
 * Component to be used as tabComponent; Contains a JLabel to show the text and
 * a JButton to close the tab it belongs to
 */
class ButtonTabComponent extends JPanel {
	private final TabbedPane pane;

	public ButtonTabComponent(final TabbedPane pane) {
		// unset default FlowLayout' gaps
		super(new FlowLayout(FlowLayout.LEFT, 0, 0));
		if (pane == null) {
			throw new NullPointerException("TabbedPane is null");
		}
		this.pane = pane;
		setOpaque(false);

		// make JLabel read titles from JTabbedPane
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
		// add more space between the label and the button
		label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
		// tab button
		JButton button = new TabButton();
		add(button);
		// add more space to the top of the component
		setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
	}

	private class TabButton extends JButton implements ActionListener {
		public TabButton() {
			int size = 17;
			setPreferredSize(new Dimension(size, size));
			setToolTipText("close this tab");
			// Make the button looks the same for all Laf's
			setUI(new BasicButtonUI());
			// Make it transparent
			setContentAreaFilled(false);
			// No need to be focusable
			setFocusable(false);
			setBorder(BorderFactory.createEtchedBorder());
			setBorderPainted(false);
			// Making nice rollover effect
			// we use the same listener for all buttons
			addMouseListener(buttonMouseListener);
			setRolloverEnabled(true);
			// Close the proper tab by clicking the button
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent e) {
			int i = pane.indexOfTabComponent(ButtonTabComponent.this);
			if (i != -1) {
				pane.closeTab(i);
			}
		}

		// we don't want to update UI for this button
		public void updateUI() {
		}

		// paint the cross
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g.create();
			// shift the image for pressed buttons
			if (getModel().isPressed()) {
				g2.translate(1, 1);
			}
			g2.setStroke(new BasicStroke(2));
			g2.setColor(Color.BLACK);
			if (getModel().isRollover()) {
				g2.setColor(Color.MAGENTA);
			}
			int delta = 6;
			g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight()
					- delta - 1);
			g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight()
					- delta - 1);
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