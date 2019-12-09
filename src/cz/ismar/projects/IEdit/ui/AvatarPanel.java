package cz.ismar.projects.IEdit.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JWindow;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import cz.ismar.projects.IEdit.IconLabelListModel;
import cz.ismar.projects.IEdit.IconLabelListRenderer;
import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.handlers.FragmentListMouseListener;
import cz.ismar.projects.IEdit.handlers.FragmentListMouseMotionListener;
import cz.ismar.projects.IEdit.handlers.FrameInFramesListSelectionListener;
import cz.ismar.projects.IEdit.handlers.FrameKeyListener;
import cz.ismar.projects.IEdit.handlers.FrameListSelectionListener;
import cz.ismar.projects.IEdit.handlers.ListPopUpMenuMouseListener;
import cz.ismar.projects.IEdit.io.InOut;
import cz.ismar.projects.IEdit.io.PrefixSuffixFileFilter;
import cz.ismar.projects.IEdit.io.WrongFileFormatException;
import cz.ismar.projects.IEdit.structure.FragmentHolder;
import cz.ismar.projects.IEdit.structure.Frame;
import cz.ismar.projects.IEdit.structure.FrameFragment;
import cz.ismar.projects.IEdit.structure.Sprite;

public class AvatarPanel extends JPanel implements ActionListener
{
	/**
	 * 处理切片文件隐藏 
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == cb_fragmentFileIsVisible)
		{
			int index = fragmentFilesList.getSelectedIndex();
			FragmentHolder holder = sprite.fragmentHoldersList.getFragmentHolderByIndex(index);
			if(holder == null)
			{
				return;
			}
			
			// 设置是否隐藏
			holder.setVisible(!cb_fragmentFileIsVisible.isSelected());
			holder.setNameExtends(holder.isVisible()?null:"[隐藏]");
			frameEditComponent.repaint();
			fragmentFileListPopupMenu.setVisible(false);
		}
	}
	
	// public void initFragmentFiles()
	// {
	// fragmentFilesList.setModel(sprite.fragmentHoldersList);
	// fragmentFilesList.repaint();
	// }

	public void initFragmentFiles()
	{
		// allFragmentListModel = new
		// IconLabelListModel(SpriteEditor.project.fragmentHoldersList.getAllFragmentsHolder().getFragments());

		fragmentFileListModel = new IconLabelListModel(sprite.fragmentHoldersList.getFragmentHolders());
		fragmentFilesList.setModel(fragmentFileListModel);
		fragmentFilesList.repaint();
	}

	public void initAllFragments()
	{
		allFragmentListModel = new IconLabelListModel(sprite.fragmentHoldersList.getAllFragments().getFragments());
		allFragmentJList.setModel(allFragmentListModel);
	}

	/**
	 * 初始化 帧列表
	 */
	public void initFrames()
	{
		frameListModel = new IconLabelListModel(sprite.frameHolder.getFrames());
		frameList.setModel(frameListModel);
		frameList.setSelectedIndex(0);
		frameList.repaint();
		frameListModel.addListDataListener(new ListDataListener() {

			public void intervalAdded(ListDataEvent listdataevent)
			{
			}

			public void intervalRemoved(ListDataEvent listdataevent)
			{
				// daben:modity
				// 删除帧时，重新检查帮助帧
				// setHelpFrame(null);
				setHelpFrame(frameEditComponent.getHelpFrame());
				// ~daben
			}

			public void contentsChanged(ListDataEvent listdataevent)
			{
				setHelpFrame(frameEditComponent.getHelpFrame());
			}
		});
	}

	/**
	 * 切换帧时改变用来显示帧切片的JList("Frame frags栏")的ListModel，显示另一帧的切片数据
	 */
	public void initFrameFragments()
	{
		if(sprite.frameHolder == null || sprite.frameHolder.getCurrentFrame() == null)
		{
			frameFragmentListModel = new IconLabelListModel(new ArrayList());
		}else
		{
			frameFragmentListModel = new IconLabelListModel(sprite.frameHolder.getCurrentFrame().getFrameFragments());
			// orientationCombo.setSelectedIndex(sprite.frameHolder.getCurrentFrame().getOrientationIndex());
		}
		frameFragmentListModel.update();
		frameFragmentList.setModel(frameFragmentListModel);
		frameEditComponent.repaint();
	}

	public AvatarPanel()
	{
		sprite = new Sprite("DEFPROJECT");

		icondetailMainPanel = new DetailComponent();
		icondetailMainPanel.setBorder(BorderFactory.createTitledBorder("切片细节"));
		icondetailMainPanel.setPreferredSize(new Dimension(140, 140));

		// 编辑面板
		frameEditComponent = new AvatarFrameEditComponent();
		frameEditComponent.addMouseMotionListener(frameEditComponent);

		// frame列表
		frameList = new JList();
		frameList.setCellRenderer(new IconLabelListRenderer());
		
		// frame fragment列表
		frameFragmentList = new JList();
		frameFragmentList.setCellRenderer(new IconLabelListRenderer());

		// 切片文件列表
		fragmentFilesList = new JList();
		fragmentFilesList.setCellRenderer(new IconLabelListRenderer());
		fragmentFileListModel = new IconLabelListModel(sprite.fragmentHoldersList.getAllFragments().getFragments());
		fragmentFilesList.setModel(fragmentFileListModel);
		fragmentFileListModel.update();
		fragmentFilesList.setSelectionMode(0);
		
		// 切片文件的右键菜单(是否隐藏)
		fragmentFileListPopupMenu = new JPopupMenu();
		cb_fragmentFileIsVisible = new JCheckBox("是否隐藏");
		cb_fragmentFileIsVisible.addActionListener(this);
		fragmentFileListPopupMenu.add(cb_fragmentFileIsVisible);
		
		/**
		 * 弹出替换菜单
		 */
		fragmentFilesList.addMouseListener(new MouseAdapter() {
			 // 更换切片
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if(e.getClickCount() == 2)
				{
					processSwitchFragment();
				}
			}
			
			@Override
			public void mouseReleased(MouseEvent e)
			{
				// 切片文件隐藏
				processHideFragmentFile(e);
			}
		});
			
		
//		frameFragmentList.add(fragmentFileListPopupMenu);
		
		// 移动Frame Fragment上时, 画出切片细节
		frameFragmentList.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseMoved(MouseEvent mouseevent)
			{
				int index = frameFragmentList.locationToIndex(mouseevent.getPoint());
				if(!(frameFragmentList.getModel().getElementAt(index) instanceof String))
				{
					java.awt.image.BufferedImage bufferedimage = ((FrameFragment) frameFragmentList.getModel().getElementAt(index)).getImage();
					icondetailMainPanel.setImg(bufferedimage);
				}
				icondetailMainPanel.repaint();
			}
		});
		frameFragmentList.setFocusable(true);
		// 处理frame fragment list被选中
		frameFragmentList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event)
			{
				processFrameFragmentSelect(event);
			}
		});
		JPanel westPanel = new JPanel();

		JScrollPane fragmentList_scrollPane = new JScrollPane(fragmentFilesList);
		fragmentList_scrollPane.setPreferredSize(new Dimension(140, 25));
		JPanel allFragmentList_panel = new JPanel();
		allFragmentList_panel.setBorder(BorderFactory.createTitledBorder("全部切片列表"));
		allFragmentList_panel.setLayout(new BorderLayout());
		allFragmentList_panel.add(fragmentList_scrollPane, "Center");

		// 操作按钮
		JPanel cmd_panel = new JPanel();
		cmd_panel.setBorder(BorderFactory.createTitledBorder("操作"));
		cmd_panel.setLayout(new BoxLayout(cmd_panel, BoxLayout.Y_AXIS));
		cmd_load = createButton("加载模板");
		cmd_save = createButton("保存切片");
		cmd_panel.add(cmd_load);
		cmd_panel.add(cmd_save);
		
		// 加载模板
		cmd_load.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				processLoadSprite();
			}
		});
		
		// 保存切处的avatar数据
		cmd_save.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				processSaveFragment();
			}
		});

		JPanel cmd_allFragment_Panel = new JPanel();
		cmd_allFragment_Panel.setLayout(new BoxLayout(cmd_allFragment_Panel, BoxLayout.Y_AXIS));
		cmd_allFragment_Panel.add(cmd_panel);
		cmd_allFragment_Panel.add(allFragmentList_panel);

		// ==================切片 end=================================

		frameList.setSelectionMode(0);
		frameList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				sprite.frameHolder.setIndex(frameList.getSelectedIndex());
				Frame frame = sprite.frameHolder.getCurrentFrame();
				if(frame != null)
				{
					frame.setIndex(-1);
					frame.setIndices(null);
				}
				initFrameFragments();
				frameEditComponent.repaint();
			}
		});

//		frameList.addMouseListener(new FrameInFramesListSelectionListener());
//		frameList.addMouseListener(this);
		frameList.setFocusable(true);
		frameList.addKeyListener(new FrameKeyListener());
		JScrollPane frameList_scrollPane = new JScrollPane(frameList);
		frameList_scrollPane.setPreferredSize(new Dimension(140, 80));
		framesMainPanel = new JPanel();
		framesMainPanel.setBorder(BorderFactory.createTitledBorder("帧列表"));
		framesMainPanel.setLayout(new BoxLayout(framesMainPanel, 1));
		framesMainPanel.add(frameList_scrollPane);
		// framesMainPanel.add(frameCreateButton);
		JPanel eastPanel = new JPanel();
		eastPanel.setLayout(new BorderLayout());
		eastPanel.add(framesMainPanel, "Center");
		// frameCreateButton.addActionListener(frameCreateActionListener);
		westPanel.setLayout(new BorderLayout());
		westPanel.add(cmd_allFragment_Panel, "Center");
		JScrollPane frameFragmentList_scrollPane = new JScrollPane(frameFragmentList);
		frameFragmentList_scrollPane.setPreferredSize(new Dimension(140, 25));
		borderLeftRightPanel = new JPanel();
		borderLeftRightPanel.setBorder(BorderFactory.createTitledBorder("帧切片列表"));
		borderLeftRightPanel.setLayout(new BorderLayout());
		borderLeftRightPanel.add(frameFragmentList_scrollPane, "Center");

		// TODO,暂时屏蔽对齐方式-帧编辑区
		// borderLeftRightPanel.add(orientationCombo, "South");

		masksComboBox = new JComboBox(SpriteEditor.frameFragmentMasks.getComboBoxModel());
		masksComboBox.setEnabled(SpriteEditor.frameFragmentMasks.isEnabled());
		masksComboBox.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent actionevent)
			{
				repaint();
			}

		});
		JPanel hide_panel = new JPanel();
		hide_panel.setBorder(BorderFactory.createTitledBorder("隐藏"));
		hide_panel.setLayout(new BorderLayout());
		hide_panel.add(masksComboBox, "Center");
		JPanel background_panel = new JPanel();
		background_panel.setLayout(new BorderLayout());
		background_panel.setBorder(BorderFactory.createTitledBorder("背景"));
		JButton cmd_setting = new JButton("设置");
		cmd_setting.setMnemonic('t');
		cmd_setting.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent actionevent)
			{
				initBackgroundEditor();
				showBackgroundEditor();
			}

		});
		final JCheckBox cmd_backgroundhShow = new JCheckBox("显示");
		cmd_backgroundhShow.setMnemonic('s');
		cmd_backgroundhShow.setSelected(true);
		cmd_backgroundhShow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionevent)
			{
				SpriteEditor.framesBackground.setVisible(cmd_backgroundhShow.isSelected());
				repaint();
			}

		});
		helpFrameShowButton = new JCheckBox("帮助帧");
		helpFrameShowButton.setMnemonic('h');
		helpFrameShowButton.setSelected(false);
		helpFrameShowButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent actionevent)
			{
				frameEditComponent.setShowHelpFrame(helpFrameShowButton.isSelected());
				repaint();
			}

		});
		helpFrameLabelName = new JLabel();
		JPanel jpanel7 = new JPanel(new BorderLayout());
		jpanel7.add(cmd_backgroundhShow, "West");
		jpanel7.add(cmd_setting, "East");
		JPanel jpanel8 = new JPanel(new BorderLayout());
		jpanel8.add(helpFrameShowButton, "West");
		jpanel8.add(helpFrameLabelName, "East");
		background_panel.add(jpanel7, "West");
		background_panel.add(jpanel8, "South");
		JPanel jpanel9 = new JPanel();
		jpanel9.setLayout(new BorderLayout());
		jpanel9.add(icondetailMainPanel, "North");
		JButton cmd_preview = new JButton("预览");
		cmd_preview.setMnemonic('P');
		cmd_preview.setToolTipText("显示预览窗口");
		cmd_preview.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionevent)
			{
				processPreview();
			}
		});
		JPanel jpanel10 = new JPanel(new BorderLayout());
		jpanel10.add(hide_panel, "North");
		jpanel10.add(borderLeftRightPanel, "Center");
		jpanel10.add(cmd_preview, "South");
		jpanel9.add(jpanel10, "Center");
		eastPanel.add(background_panel, "South");
		westPanel.add(jpanel9, "East");

		// 移到frame list上, 会在frameEditComponent上显示frame
		frameList.addMouseMotionListener(new MouseMotionAdapter() {

			public void mouseMoved(MouseEvent mouseevent)
			{
				int i = frameList.locationToIndex(mouseevent.getPoint());
				if(sprite.frameHolder.get(i) != null)
				{
					frameEditComponent.setTmpFrame(sprite.frameHolder.get(i));
				}
				frameEditComponent.repaint();
			}

		});
		frameList.addMouseListener(new MouseAdapter() {

			public void mouseExited(MouseEvent mouseevent)
			{
				frameEditComponent.setTmpFrame(null);
				frameEditComponent.repaint();
			}

		});
		// orientationCombo.addActionListener(new
		// FrameOrientationActionListener());
		JPanel jpanel11 = new JPanel();
		jpanel11.setBackground(Color.WHITE);
		jpanel11.setLayout(new FlowLayout());
		jpanel11.setBorder(BorderFactory.createTitledBorder(""));
		titledBorder = BorderFactory.createTitledBorder("当前帧");
		borderCenterPanel = new JPanel();
		borderCenterPanel.setBorder(titledBorder);
		borderCenterPanel.setLayout(new BorderLayout());
		frameEditScrollPanel = new JScrollPane(frameEditComponent);
		borderCenterPanel.add(frameEditScrollPanel, "Center");
		setLayout(new BorderLayout());
		add(westPanel, "West");
		add(borderCenterPanel, "Center");
		add(eastPanel, "East");
	}

	public void showBackgroundEditor()
	{
		if(!backgroundEditor.isShowing())
			backgroundEditor.showGui();
		else
			backgroundEditor.requestFocus();
	}

	public void initBackgroundEditor()
	{
		if(backgroundEditor == null)
			backgroundEditor = new BackgroundEditor(SpriteEditor.framesBackground, this, "帧背景");
	}

	private JButton createButton(String s)
	{
		JButton jbutton = new JButton(s) {

			public Dimension getMaximumSize()
			{
				char c = '\u7FFF';
				int i = super.getMaximumSize().height;
				return new Dimension(c, i);
			}

		};

		// X中间对齐
		jbutton.setAlignmentX(0.5F);
		return jbutton;
	}

	public void setHelpFrame(Frame frame)
	{
		if(frame == null)
		{
			frameEditComponent.setHelpFrame(null);
			frameEditComponent.setShowHelpFrame(false);
			frameEditComponent.repaint();
			helpFrameLabelName.setText("");
			helpFrameShowButton.setSelected(false);
		}else
		{
			frameEditComponent.setHelpFrame(frame);
			frameEditComponent.setShowHelpFrame(true);
			helpFrameLabelName.setText(frame.getName());
			helpFrameShowButton.setSelected(true);
		}
	}

	public Sprite getSprite()
	{
		return sprite;
	}
	
	/**
	 * 更换切片
	 */
	private void processSwitchFragment()
	{
		JFileChooser chooser = SpriteEditor.getFileChooser();
		
		// 当前指向的fragmentFileList的index
		int index = fragmentFilesList.getSelectedIndex();
		
		FragmentHolder fragmentHolder = sprite.fragmentHoldersList.getFragmentHolderById(index);
		PrefixSuffixFileFilter filter = new PrefixSuffixFileFilter(fragmentHolder.getPartName(), ".png");
		chooser.setFileFilter(filter);
		
		int option = chooser.showOpenDialog(SpriteEditor.mainFrame);
		if(option == JFileChooser.APPROVE_OPTION)
		{
			File file = chooser.getSelectedFile();
			
			FragmentHolder newFragmentHolder = new FragmentHolder();
			
			// 加载Fragment图片
			newFragmentHolder.setImageFile(file);
//			System.out.println(newFragmentHolder.getName());
			
			// 加载Fragment xml文件
			File xmlFile = new File(file.getParentFile(), newFragmentHolder.getName() + ".xml");
			InOut.inFragmentXML(newFragmentHolder, xmlFile);
			
			// 更换切片
			sprite.fragmentHoldersList.replaceFragmentHolder(index, newFragmentHolder);
			
			// 更新精灵
			sprite.refresh();

			// 更新显示
			initFragmentFiles();
			initFrames();
			initFrameFragments();
			
			// 清空之前的选择
			sprite.frameHolder.getCurrentFrame().setIndices(null);
			sprite.frameHolder.getCurrentFrame().setIndex(-1);
		}
	
	}
	

	/**
	 * 保存切片的avatar数据
	 */
	private void processSaveFragment()
	{
        try
        {
        	for(FragmentHolder fragmentholder : sprite.fragmentHoldersList.getFragmentHolders())
        	{
        		if(fragmentholder.getImageFile() == null)
        		{
        			System.out.println("fragment[" + fragmentholder.getID() + "]'s image file == null!");
        			continue;
//        			throw new NullPointerException("getFragmentImage is null");
        		}
        		
        		File file = new File(fragmentholder.getImageFile().getParentFile(), fragmentholder.getName() + ".xml");
        		if(!file.getAbsolutePath().endsWith(".xml"))
        		{
        			file = new File(file.getAbsolutePath() + ".xml");
        		}
        		
        		if(!file.exists())
        		{
        			file.createNewFile();
        		}

        		// 保存切片至文件
        		InOut.outFragmentFileXML(fragmentholder, file);
        		
//        		SpriteEditor.setStatus(file + " saved");
//        		SpriteEditor.fragmentsSaved = true;
        	}
        	JOptionPane.showMessageDialog(SpriteEditor.mainFrame, "切片文件已保存!", "Save Flags Succeed", JOptionPane.INFORMATION_MESSAGE);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
            String s = "保存时出错: " + e.getLocalizedMessage();
            JOptionPane.showMessageDialog(SpriteEditor.mainFrame, s, SpriteEditor.TITLE, 0);
            SpriteEditor.setStatus(s);
        }
    
	}
	
	/**
	 * 加载模板
	 */
	private void processLoadSprite()
	{
		JFileChooser fileChooser = SpriteEditor.getFileChooser();
		
		PrefixSuffixFileFilter filter = new PrefixSuffixFileFilter("a_", ".xml");
		fileChooser.setFileFilter(filter);
		
//		fileChooser.setFileFilter(SpriteEditor.xmlFileFilter);
		int option = fileChooser.showOpenDialog(SpriteEditor.mainFrame);
		if(option == JFileChooser.APPROVE_OPTION)
		{
			try
			{
				sprite = InOut.inXML(fileChooser.getSelectedFile().getAbsolutePath());
				SpriteEditor.xmlSavePath = null;

				sprite.initFragmentIcons();
				sprite.initFrameFragmentIcons();
				sprite.initFrameIcons();

				// initFragmentFiles();
				initFragmentFiles();
				initFrames();
				initFrameFragments();
			}catch(WrongFileFormatException e1)
			{
				e1.printStackTrace();
				sprite = null;
				JOptionPane.showMessageDialog(SpriteEditor.mainFrame, "此文件可能不是动画文件!");
			}
		}	
	}
	
	/**
	 * 预览
	 */
	private void processPreview()
	{
		if(previewWindow == null)
		{
			previewWindow = new JWindow(SpriteEditor.mainFrame);
			if(previewWindowLocation == null)
			{
				previewWindowLocation = SpriteEditor.mainFrame.getLocation();
				previewWindowLocation.translate(200, 200);
			}
			previewWindow.setLocation(previewWindowLocation);
			previewWindow.getContentPane().add(frameEditComponent.getPreviewPanel());
			previewWindow.setSize(300, 300);
			previewWindow.show();
			previewWindow.addMouseMotionListener(new MouseMotionListener() {

				public void mouseMoved(MouseEvent mouseevent)
				{
					offsetX = mouseevent.getX();
					offsetY = mouseevent.getY();
				}

				public void mouseDragged(MouseEvent mouseevent)
				{
					previewWindowLocation.translate(-offsetX + mouseevent.getX(), -offsetY + mouseevent.getY());
					previewWindow.setLocation(previewWindowLocation);
				}

				int offsetX;
				int offsetY;

			});
			previewWindow.addMouseListener(new MouseAdapter() {

				public void mouseClicked(MouseEvent mouseevent)
				{
					if(mouseevent.getClickCount() > 1)
					{
						previewWindow.dispose();
						previewWindow = null;
					}
				}

			});
		}else
		{
			previewWindow.dispose();
			previewWindow = null;
		}
	}
	
	/**
	 * 选中某上frame fragment
	 * @param event
	 */
	private void processFrameFragmentSelect(ListSelectionEvent event)
	{
		// 响应keyrelease的事件
		if(event.getValueIsAdjusting())
		{
			return;
		}
		
//		System.out.println("(" + event.getFirstIndex() + " -> " + event.getLastIndex() + ")");
//		System.out.println("select -> " + frameFragmentList.getSelectedIndex());
		
		Frame frame = sprite.frameHolder.getCurrentFrame();
		int index = frameFragmentList.getSelectedIndex();
		if(frame == null || index < 0 || index >= frame.getFrameFragments().size())
		{
			return;
		}
		
		// 选中相同的块
		// 哪些是相同的切片, 选中之
		ArrayList<Integer> theSameFragmentIndexList = frame.getSameFrameFragmentIndex(frame.get(index));
		if(theSameFragmentIndexList != null)
		{
			for(Integer inte : theSameFragmentIndexList)
			{
				// 没选中的, 选上
				if(!frameFragmentList.isSelectedIndex(inte))
				{
					frameFragmentList.addSelectionInterval(inte, inte);
				}
			}
		}
		
		frame.setIndex(index);
		frame.setIndices(frameFragmentList.getSelectedIndices());
		frameEditComponent.repaint();
		
//		frameFragmentList.setSelectedIndex(index);
	}
	
	/**
	 * 处理隐藏切片
	 * @param e
	 */
	private void processHideFragmentFile(MouseEvent e)
	{
		// 右键菜单 -> 显示(隐藏菜单)
		if(e.isPopupTrigger())
		{
			// 选中
			int fragmentFileSelectedIndex = fragmentFilesList.locationToIndex(new Point(e.getX(), e.getY()));
			fragmentFilesList.setSelectedIndex(fragmentFileSelectedIndex);
			// 是否可见
			cb_fragmentFileIsVisible.setSelected(!sprite.fragmentHoldersList.getFragmentHolderByIndex(fragmentFileSelectedIndex).isVisible());
			// 显示右键菜单
			fragmentFileListPopupMenu.show(fragmentFilesList, e.getX(), e.getY());
		}
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public IconLabelListModel frameListModel;
	public DetailComponent icondetailMainPanel;

	/**
	 * 全部切片文件列表
	 */
	public JList fragmentFilesList;
	public IconLabelListModel fragmentFileListModel;

	/**
	 * 所有切出来的切片图
	 */
	public IconLabelListModel allFragmentListModel;
	public JList allFragmentJList;

	/**
	 * 帧切片列表
	 */
	public JList frameFragmentList;
	public IconLabelListModel frameFragmentListModel;
	/**
	 * 【帧编辑区】帧列表
	 */
	public JList frameList;

	public JPanel framesMainPanel;
	public JPanel borderLeftRightPanel;
	// public JComboBox orientationCombo;
	// public JButton frameCreateButton;

	// private MouseListener fragmentListMouseListener;
	// private FragmentListMouseMotionListener fragmentListMouseMotionListener;
	// private FrameListSelectionListener frameListSelectionListener;

	/** 帧编辑区 */
	public JPanel borderCenterPanel;
	public AvatarFrameEditComponent frameEditComponent;
	public JScrollPane frameEditScrollPanel;
	public TitledBorder titledBorder;

	// public JList fragmentFilesList;

	public BackgroundEditor backgroundEditor;
	public JLabel helpFrameLabelName;
	public JCheckBox helpFrameShowButton;
	public JComboBox masksComboBox;
	private JWindow previewWindow;
	private Point previewWindowLocation;

	private JButton cmd_load = null;
	private JButton cmd_save = null;
	
	/** 是否隐藏切片 */
	private JPopupMenu fragmentFileListPopupMenu = null;
	private JCheckBox cb_fragmentFileIsVisible = null;

	/** 当前编辑的sprite */
	private Sprite sprite = null;
}
