/**
 * 2007-9-14 daben
 * - modify in
 * //删除帧时，重新检查帮助帧
 * --> frameFragmentJList.addMouseMotionListener
 * 
 * 2007-10-30 daben
 * - remove 
 *    borderLeftRightPanel.add(orientationCombo, "South");
 * - //modify in frameFragmentJList.addMouseMotionListener(new MouseMotionAdapter()
 *   //原方法不能在"Img dettail"上显示正确的切片，有时显示的不是鼠标所在位置的切片图象
 *   
 * 2008-1-18 Lori
 *       // 增加切片文件的刷新功能
 *       JMenuItem refreshmenuitem = new JMenuItem("刷新");
 *       refreshmenuitem.addActionListener(new FragmentFilesRefreshActionListener());
 *       jpopupmenu.add(refreshmenuitem);
 */

package cz.ismar.projects.IEdit.ui;

import cz.ismar.projects.IEdit.*;
import cz.ismar.projects.IEdit.handlers.*;
import cz.ismar.projects.IEdit.structure.*;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;

import cz.ismar.projects.IEdit.structure.Frame;

/**
 * 
 * 帧编辑区 面板
 * @author Lori
 *
 */

/*
 * setFocusable()
 * 设置是否能获得焦点，false,则按键事件无法传递给该componet
 * 
 * JList使用说明
 * - setCellRenderer(DefaultListCellRenderer );
 * - setModel(AbstractListModel );
 * 
 * 这样设置后，可以看到列表的信息，以及可以设置图片视图
 * */
public class FramePanel extends JPanel implements ActionListener
{
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == cb_allFragmentListIsVisible)		// 隐藏切片
		{
			Fragment fragment = SpriteEditor.sprite.fragmentHoldersList.getAllFragments().get();
			fragment.setVisible(!cb_allFragmentListIsVisible.isSelected());
			frameEditComponent.repaint();
			
			fragment.setNameExtend(fragment.isVisible()?"":"[隐藏]");
			
			allFragmentListPopupMenu.setVisible(false);
		}
	}
	
	/**
	 * 切片文件的列表
	 */
    public void initFragmentFiles()
    {
        fragmentFilesList.setModel(SpriteEditor.sprite.fragmentHoldersList);
        fragmentFilesList.repaint();
    }

    /**
     * 所有切出来的切片图
     */
    public void initFragments()
    {
        allFragmentListModel = new IconLabelListModel(SpriteEditor.sprite.fragmentHoldersList.getAllFragments().getFragments());
        allFragmentList.setModel(allFragmentListModel);
        allFragmentList.repaint();
    }

    /**
     * 帧列表
     */
    public void initFrames()
    {
    	frameListModel = new IconLabelListModel(SpriteEditor.sprite.frameHolder.getFrames());
        frameJList.setModel(frameListModel);
        frameJList.repaint();
        frameListModel.addListDataListener(new ListDataListener() {

            public void intervalAdded(ListDataEvent listdataevent)
            {
            }

            public void intervalRemoved(ListDataEvent listdataevent)
            {
                // daben:modity
            	//删除帧时，重新检查帮助帧
                //setHelpFrame(null);
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
     * 切换帧时改变用来显示帧切片的JList("Frame Fragment栏")的ListModel，显示另一帧的切片数据
     */
    public void initFrameFragments()
    {
        if(SpriteEditor.sprite.frameHolder == null || SpriteEditor.sprite.frameHolder.getCurrentFrame() == null)
        {
            frameFragmentListModel = new IconLabelListModel(new ArrayList());
        } else
        {
            frameFragmentListModel = new IconLabelListModel(SpriteEditor.sprite.frameHolder.getCurrentFrame().getFrameFragments());
            orientationCombo.setSelectedIndex(SpriteEditor.sprite.frameHolder.getCurrentFrame().getOrientationIndex());
        }
        frameFragmentListModel.update();
        frameFragmentJList.setModel(frameFragmentListModel);
        frameEditComponent.repaint();
    }

    public FramePanel()
    {
        orientationCombo = new JComboBox(Frame.ORIENTATION_NAMES);
        frameCreateButton = createButton("新建帧");
        fragmentListMouseListener = new FragmentListMouseListener();
        fragmentListMouseMotionListener = new FragmentListMouseMotionListener();
//        frameListSelectionListener = new FrameListSelectionListener();
        frameCreateActionListener = new FrameCreateActionListener();
        allFramePopup = new JPopupMenu();
        frameCreateMenuItem = new JMenuItem("新建");
        frameDuplicateMenuItem = new JMenuItem("复制");
        frameRenameMenuItem = new JMenuItem("重命名");
        frameDeleteMenuItem = new JMenuItem("删除");
        frameSetAsHelpFrame = new JMenuItem("设为帮助帧");
        frameTopMenuItem = new JMenuItem("置顶");
        frameUpMenuItem = new JMenuItem("上移");
        frameDownMenuItem = new JMenuItem("下移");
        frameBottomMenuItem = new JMenuItem("置底");
        FramePopUpMenuActionListener framepopupmenuactionlistener = new FramePopUpMenuActionListener();
        frameCreateMenuItem.addActionListener(frameCreateActionListener);
        frameCreateMenuItem.setAccelerator(KeyStroke.getKeyStroke(155, 0));
        frameDuplicateMenuItem.addActionListener(framepopupmenuactionlistener);
        frameDuplicateMenuItem.setAccelerator(KeyStroke.getKeyStroke(155, 1));
        frameRenameMenuItem.addActionListener(framepopupmenuactionlistener);
        frameRenameMenuItem.setAccelerator(KeyStroke.getKeyStroke(113, 0));
        frameDeleteMenuItem.addActionListener(framepopupmenuactionlistener);
        frameDeleteMenuItem.setAccelerator(KeyStroke.getKeyStroke(127, 0));
        frameSetAsHelpFrame.addActionListener(framepopupmenuactionlistener);
        frameSetAsHelpFrame.setAccelerator(KeyStroke.getKeyStroke(10, 0));
        frameTopMenuItem.addActionListener(framepopupmenuactionlistener);
        frameTopMenuItem.setAccelerator(KeyStroke.getKeyStroke(36, 8));
        frameUpMenuItem.addActionListener(framepopupmenuactionlistener);
        frameUpMenuItem.setAccelerator(KeyStroke.getKeyStroke(38, 8));
        frameDownMenuItem.addActionListener(framepopupmenuactionlistener);
        frameDownMenuItem.setAccelerator(KeyStroke.getKeyStroke(40, 8));
        frameBottomMenuItem.addActionListener(framepopupmenuactionlistener);
        frameBottomMenuItem.setAccelerator(KeyStroke.getKeyStroke(35, 8));
        allFramePopup.add(frameTopMenuItem);
        allFramePopup.add(frameUpMenuItem);
        allFramePopup.add(frameDownMenuItem);
        allFramePopup.add(frameBottomMenuItem);
        allFramePopup.addSeparator();
        allFramePopup.add(frameCreateMenuItem);
        allFramePopup.add(frameDuplicateMenuItem);
        allFramePopup.add(frameRenameMenuItem);
        allFramePopup.add(frameDeleteMenuItem);
        allFramePopup.add(frameSetAsHelpFrame);
        fragmentDetailPanel = new DetailComponent();
        fragmentDetailPanel.setBorder(BorderFactory.createTitledBorder("切片细节"));
        fragmentDetailPanel.setPreferredSize(new Dimension(140, 140));
        frameEditComponent = new FrameEditComponent();
        frameEditComponent.addMouseMotionListener(frameEditComponent);
        frameJList = new JList();
        frameFragmentJList = new JList();
        allFragmentList = new JList();
        allFragmentList.setCellRenderer(new IconLabelListRenderer());
        frameFragmentJList.setCellRenderer(new IconLabelListRenderer());
        frameJList.setCellRenderer(new IconLabelListRenderer());
        
        // 全部的切片的右键菜单
        allFragmentListPopupMenu = new JPopupMenu();
        allFragmentListPopupMenu.add(cb_allFragmentListIsVisible);
        cb_allFragmentListIsVisible.addActionListener(this);
        
        allFragmentListModel = new IconLabelListModel(SpriteEditor.sprite.fragmentHoldersList.getAllFragments().getFragments());
        allFragmentList.setModel(allFragmentListModel);
        allFragmentListModel.update();
        allFragmentList.setSelectionMode(0);
        allFragmentList.addMouseListener(fragmentListMouseListener);
        allFragmentList.addMouseMotionListener(fragmentListMouseMotionListener);
        allFragmentList.addKeyListener(new KeyAdapter() {

            public void keyReleased(KeyEvent keyevent)
            {
                if(keyevent.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    int i = allFragmentList.getSelectedIndex();
                    if(i >= 0 && SpriteEditor.sprite.frameHolder.getCurrentFrame() != null)
                    {
                        SpriteEditor.sprite.frameHolder.getCurrentFrame().saveUndo();
                        
                        SpriteEditor.sprite.frameHolder.getCurrentFrame().add(0, new FrameFragment(SpriteEditor.sprite.fragmentHoldersList.getAllFragments().get(i)));
                        SpriteEditor.framePanel.initFrameFragments();
                        // daben:add
                        if (SpriteEditor.animationsSaved)
                            SpriteEditor.animationsSaved = false;
                        // ~daben
                    }
                }else if(keyevent.getModifiers() == InputEvent.CTRL_MASK)
                {
                    if (keyevent.getKeyCode() == KeyEvent.VK_Z)
                    {
                        SpriteEditor.sprite.frameHolder.getCurrentFrame().undo();
                    }
                }
            }
        });
        
//        cb_allFragmentListIsVisible = new JCheckBox("隐藏");
        
        
        frameFragmentJList.addMouseMotionListener(new MouseMotionAdapter() {

            public void mouseMoved(MouseEvent mouseevent)
            {
                int index = frameFragmentJList.locationToIndex(mouseevent.getPoint());
                //daben:modify 原方法不能在"Img dettail"上显示正确的切片，有时显示的不是鼠标所在位置的切片图象
//                if(SpriteEditor.project.get() != null && SpriteEditor.project.get().get() != null && SpriteEditor.project.get().get().getFrame().get(i) != null && SpriteEditor.project.get().get().getFrame().get(i).getFragment() != null)
//                {
//                    java.awt.image.BufferedImage bufferedimage = SpriteEditor.project.get().get().getFrame().get(i).getFragment().getImage();
//                	icondetailMainPanel.setImg(bufferedimage);
//                }
                //modify:
                if (!(frameFragmentJList.getModel().getElementAt(index) instanceof String))
            	{
            		java.awt.image.BufferedImage bufferedimage = ((FrameFragment)frameFragmentJList.getModel().getElementAt(index)).getImage();
            		fragmentDetailPanel.setImg(bufferedimage);
            	}
                //~daben
                fragmentDetailPanel.repaint();
            }
        });
        frameFragmentJList.addMouseListener(new FrameFragmentsListMouseListener(SpriteEditor.frameFragmentsListCommander));
        frameFragmentJList.addKeyListener(new FrameFragmentsListKeyListener(SpriteEditor.frameFragmentsListCommander));
        frameFragmentJList.setFocusable(true);
        frameFragmentJList.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent listselectionevent)
            {
                Frame frame = SpriteEditor.sprite.frameHolder.getCurrentFrame();
                if(frame != null)
                {
                    frame.setIndex(frameFragmentJList.getSelectedIndex());
                    frame.setIndices(frameFragmentJList.getSelectedIndices());
                    frameEditComponent.repaint();
                }
            }

        });
        JPanel westPanel = new JPanel();
        JScrollPane allFragment_scrollPane = new JScrollPane(allFragmentList);
        allFragment_scrollPane.setPreferredSize(new Dimension(140, 25));
        JPanel jpanel1 = new JPanel();
        jpanel1.setLayout(new BoxLayout(jpanel1, BoxLayout.Y_AXIS));
        JPanel allFragment_panel = new JPanel();
        allFragment_panel.setBorder(BorderFactory.createTitledBorder("全部切片列表"));
        allFragment_panel.setLayout(new BorderLayout());
        allFragment_panel.add(allFragment_scrollPane, "Center");
        
        JPanel fragmentFile_panel = new JPanel();
        fragmentFile_panel.setLayout(new BoxLayout(fragmentFile_panel, BoxLayout.Y_AXIS));
        fragmentFile_panel.setBorder(BorderFactory.createTitledBorder("切片文件"));
        JButton jbutton = createButton("增加");
        jbutton.addActionListener(new FragmentFilesAddActionListener());
        fragmentFile_panel.add(jbutton);
        fragmentFilesList = new JList();
        fragmentFilesList.setModel(SpriteEditor.sprite.fragmentHoldersList);
        JPopupMenu jpopupmenu = new JPopupMenu();
        JMenuItem jmenuitem = new JMenuItem("移除");
        jmenuitem.addActionListener(new FragmentFilesDeleteActionListener(fragmentFilesList));
        // daben:add
        // 增加切片文件的刷新功能
        JMenuItem refreshmenuitem = new JMenuItem("刷新");
        refreshmenuitem.addActionListener(new FragmentFilesRefreshActionListener());
        jpopupmenu.add(refreshmenuitem);
        // ~daben
        jpopupmenu.add(jmenuitem);
        fragmentFilesList.addMouseListener(new ListPopUpMenuMouseListener(fragmentFilesList, jpopupmenu));
        JScrollPane jscrollpane1 = new JScrollPane(fragmentFilesList);
        jscrollpane1.setPreferredSize(new Dimension(140, 58));
        fragmentFile_panel.add(jscrollpane1);
        jpanel1.add(fragmentFile_panel);
        jpanel1.add(allFragment_panel);
        frameJList.setSelectionMode(0);
//        frameJList.getSelectionModel().addListSelectionListener(frameListSelectionListener);
        
        frameJList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e)
			{
		        if(SpriteEditor.sprite.frameHolder.size() != 0 
		        		&& SpriteEditor.framePanel.frameJList.getSelectedIndex() >= 0 
		        		&& SpriteEditor.framePanel.frameJList.getSelectedIndex() < SpriteEditor.sprite.frameHolder.size())
		        {
		            SpriteEditor.sprite.frameHolder.setIndex(SpriteEditor.framePanel.frameJList.getSelectedIndex());
		            initFrameFragments();
		            frameEditComponent.repaint();
		        }
			}
		});

        
        
        frameJList.addMouseListener(new FrameInFramesListSelectionListener());
        frameJList.setFocusable(true);
        frameJList.addKeyListener(new FrameKeyListener());
        JScrollPane jscrollpane2 = new JScrollPane(frameJList);
        jscrollpane2.setPreferredSize(new Dimension(140, 80));
        framesMainPanel = new JPanel();
        framesMainPanel.setBorder(BorderFactory.createTitledBorder("帧列表"));
        framesMainPanel.setLayout(new BoxLayout(framesMainPanel, 1));
        framesMainPanel.add(jscrollpane2);
        framesMainPanel.add(frameCreateButton);
        JPanel eastPanel = new JPanel();
        eastPanel.setLayout(new BorderLayout());
        eastPanel.add(framesMainPanel, "Center");
        frameCreateButton.addActionListener(frameCreateActionListener);
        westPanel.setLayout(new BorderLayout());
        westPanel.add(jpanel1, "Center");
        JScrollPane jscrollpane3 = new JScrollPane(frameFragmentJList);
        jscrollpane3.setPreferredSize(new Dimension(140, 25));
        borderLeftRightPanel = new JPanel();
        borderLeftRightPanel.setBorder(BorderFactory.createTitledBorder("帧切片列表"));
        borderLeftRightPanel.setLayout(new BorderLayout());
        borderLeftRightPanel.add(jscrollpane3, "Center");
        
        //TODO,暂时屏蔽对齐方式-帧编辑区
        //borderLeftRightPanel.add(orientationCombo, "South");
        
        masksComboBox = new JComboBox(SpriteEditor.frameFragmentMasks.getComboBoxModel());
        masksComboBox.setEnabled(SpriteEditor.frameFragmentMasks.isEnabled());
        masksComboBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                repaint();
            }

        });
        JPanel jpanel5 = new JPanel();
        jpanel5.setBorder(BorderFactory.createTitledBorder("隐藏"));
        jpanel5.setLayout(new BorderLayout());
        jpanel5.add(masksComboBox, "Center");
        JPanel jpanel6 = new JPanel();
        jpanel6.setLayout(new BorderLayout());
        jpanel6.setBorder(BorderFactory.createTitledBorder("背景"));
        JButton jbutton1 = new JButton("设置");
        jbutton1.setMnemonic('t');
        jbutton1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                initBackgroundEditor();
                showBackgroundEditor();
            }

        });
        final JCheckBox backgroundShowButton = new JCheckBox("显示");
        backgroundShowButton.setMnemonic('s');
        backgroundShowButton.setSelected(true);
        backgroundShowButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                SpriteEditor.framesBackground.setVisible(backgroundShowButton.isSelected());
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
        jpanel7.add(backgroundShowButton, "West");
        jpanel7.add(jbutton1, "East");
        JPanel jpanel8 = new JPanel(new BorderLayout());
        jpanel8.add(helpFrameShowButton, "West");
        jpanel8.add(helpFrameLabelName, "East");
        jpanel6.add(jpanel7, "West");
        jpanel6.add(jpanel8, "South");
        JPanel jpanel9 = new JPanel();
        jpanel9.setLayout(new BorderLayout());
        jpanel9.add(fragmentDetailPanel, "North");
        JButton jbutton2 = new JButton("预览");
        jbutton2.setMnemonic('P');
        jbutton2.setToolTipText("显示预览窗口");
        jbutton2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
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
                } else
                {
                    previewWindow.dispose();
                    previewWindow = null;
                }
            }


        });
        JPanel jpanel10 = new JPanel(new BorderLayout());
        jpanel10.add(jpanel5, "North");
        jpanel10.add(borderLeftRightPanel, "Center");
        jpanel10.add(jbutton2, "South");
        jpanel9.add(jpanel10, "Center");
        eastPanel.add(jpanel6, "South");
        westPanel.add(jpanel9, "East");
        frameJList.addMouseMotionListener(new MouseMotionAdapter() {

            public void mouseMoved(MouseEvent mouseevent)
            {
                int i = frameJList.locationToIndex(mouseevent.getPoint());
                if(SpriteEditor.sprite.frameHolder.get(i) != null)
                    frameEditComponent.setTmpFrame(SpriteEditor.sprite.frameHolder.get(i));
                frameEditComponent.repaint();
            }

        });
        frameJList.addMouseListener(new MouseAdapter() {

            public void mouseExited(MouseEvent mouseevent)
            {
                frameEditComponent.setTmpFrame(null);
                frameEditComponent.repaint();
            }

        });
        orientationCombo.addActionListener(new FrameOrientationActionListener());
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
        } else
        {
            frameEditComponent.setHelpFrame(frame);
            frameEditComponent.setShowHelpFrame(true);
            helpFrameLabelName.setText(frame.getName());
            helpFrameShowButton.setSelected(true);
        }
    }

    public IconLabelListModel allFragmentListModel;
    public IconLabelListModel frameListModel;
    public IconLabelListModel frameFragmentListModel;
    public DetailComponent fragmentDetailPanel;
    /** 全部切片列表 */
    public JList allFragmentList;
    
    /** 全部切片的右键菜单 [隐藏] */
    public JPopupMenu allFragmentListPopupMenu = null;
    public JCheckBox cb_allFragmentListIsVisible = new JCheckBox("隐藏");
    
    
    /** 帧切片列表 */
    public JList frameFragmentJList;
    /** 【帧编辑区】帧列表 */
    public JList frameJList;
    
    public JPanel framesMainPanel;
    public JPanel borderLeftRightPanel;
    public JComboBox orientationCombo;
    public JButton frameCreateButton;
    private FragmentListMouseListener fragmentListMouseListener;
    private MouseMotionListener fragmentListMouseMotionListener;
//    private FrameListSelectionListener frameListSelectionListener;
    public ActionListener frameCreateActionListener;
    public JPopupMenu allFramePopup;
    public JMenuItem frameCreateMenuItem;
    public JMenuItem frameDuplicateMenuItem;
    public JMenuItem frameRenameMenuItem;
    public JMenuItem frameDeleteMenuItem;
    public JMenuItem frameSetAsHelpFrame;
    public JMenuItem frameTopMenuItem;
    public JMenuItem frameUpMenuItem;
    public JMenuItem frameDownMenuItem;
    public JMenuItem frameBottomMenuItem;
    
    /** 帧编辑区 */
    public FrameEditComponent frameEditComponent;
    public JScrollPane frameEditScrollPanel;
    
    public JPanel borderCenterPanel;
    public TitledBorder titledBorder;
    
    public JList fragmentFilesList;
    public BackgroundEditor backgroundEditor;
    public JLabel helpFrameLabelName;
    public JCheckBox helpFrameShowButton;
    public JComboBox masksComboBox;
    private JWindow previewWindow;
    private Point previewWindowLocation;
    
    
}

