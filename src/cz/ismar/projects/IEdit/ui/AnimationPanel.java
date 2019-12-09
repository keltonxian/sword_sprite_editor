/**
 * 2007-9-18 daben
 * - remove
 * --> gesturePopup.add(gestureRenameMenuItem);
 * --> in frameJList.addKeyListener(new KeyAdapter() #see daben:remove
 * 
 * - modify
 *  //修改动画播放的默认标尺值=1;
 *      gestureTicksPerSecond = new JSlider(JSlider.HORIZONTAL, 1, 10, 5);  
 *      ---> gestureTicksPerSecond = new JSlider(JSlider.HORIZONTAL, 1, 10, 1);
 *      
 *  //动画添加帧时, 不提供重新命名，直接使用Frame的name
 *       //String s = (String)JOptionPane.showInputDialog(SpriteEditor.mainFrame, "New gesture name:", "iEdit V.2.0.29", -1, null, null, gesture.getName());
 *       //if(s != null && s.length() > 0){
 *       //	gesture.setName(s);
 *       //}
 *       
 *  //去掉Gestures的改名，直接使用Frame的名称
 *       //gesturePopup.add(gestureRenameMenuItem);
 */

package cz.ismar.projects.IEdit.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.NumberFormatter;

import cz.ismar.projects.IEdit.Animator;
import cz.ismar.projects.IEdit.IconLabelListModel;
import cz.ismar.projects.IEdit.IconLabelListRenderer;
import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.handlers.AnimationCreateActionListener;
import cz.ismar.projects.IEdit.handlers.AnimationListSelectionListener;
import cz.ismar.projects.IEdit.handlers.AnimationTypeActionListener;
import cz.ismar.projects.IEdit.handlers.AnimationsKeyListener;
import cz.ismar.projects.IEdit.handlers.AnimationsListMouseListener;
import cz.ismar.projects.IEdit.handlers.AnimationsPopUpMenuActionListener;
import cz.ismar.projects.IEdit.handlers.FrameInGestureListMouseListener;
import cz.ismar.projects.IEdit.handlers.FrameInGesturePopUpMenuActionListener;
import cz.ismar.projects.IEdit.handlers.FramesPerSecondChangeListener;
import cz.ismar.projects.IEdit.handlers.GestureDurationChangeListener;
import cz.ismar.projects.IEdit.handlers.GestureListSelectionListener;
import cz.ismar.projects.IEdit.handlers.GestureNextButtonActionListener;
import cz.ismar.projects.IEdit.handlers.GesturePrevButtonActionListener;
import cz.ismar.projects.IEdit.handlers.GestureRemoveActionListener;
import cz.ismar.projects.IEdit.handlers.GestureSpeedXChangeListener;
import cz.ismar.projects.IEdit.handlers.GestureSpeedYChangeListener;
import cz.ismar.projects.IEdit.handlers.GesturesListKeyListener;
import cz.ismar.projects.IEdit.handlers.GesturesListMouseListener;
import cz.ismar.projects.IEdit.handlers.GesturesPopUpMenuActionListener;
import cz.ismar.projects.IEdit.handlers.PlayGestureButtonItemListener;
import cz.ismar.projects.IEdit.structure.Animation;
import cz.ismar.projects.IEdit.structure.Gesture;

/**
 * 动画编辑区面板
 * 
 * @author Lori
 * @version 2.12 2008/1/18
 */
public class AnimationPanel extends JPanel
{

    public AnimationPanel()
    {
        animator = new Animator();
        wysiwygGestureComponent = new WysiwygGestureComponent();
        animationLabel = new JLabel();
        typeCombo = new JComboBox(Animation.TYPE_NAMES);
        animationCreate = new JButton("新建动画");
        gestureRemove = new JButton("删除帧");
        gesturePrev = new JButton("< 上一帧");
        gestureNext = new JButton("下一帧 >");
        animationReset = new JButton("重置");
        loopPanel = new JPanel();
        playButton = new JToggleButton("播放", false);
        gestureInAnimationNoLabel = new JLabel();
        checkAround = new JCheckBox("循环");
        //daben:modify
        //gestureTicksPerSecond = new JSlider(JSlider.HORIZONTAL, 1, 10, 5);
        //modify:
        gestureTicksPerSecond = new JSlider(JSlider.HORIZONTAL, 1, 10, 1);
        //~daben
        gestureSpeedX = new JSlider(0, -20, 20, 1);
        gestureSpeedY = new JSlider(1, -20, 20, 0);
        gestureDuration = new JSlider(0, 1, 20, 1);
        animationLoop = new JComboBox(new String[] {
            "0", "1", "2", "3", "4", "5", "6", "7", "8"
        });
        gesturePopup = new JPopupMenu();
        gestureMoveTopMenuItem = new JMenuItem("置顶");
        gestureMoveUpMenuItem = new JMenuItem("上移");
        gestureMoveDownMenuItem = new JMenuItem("下移");
        gestureMoveBottomMenuItem = new JMenuItem("置底");
        gestureRenameMenuItem = new JMenuItem("重命名");
        gestureDeleteMenuItem = new JMenuItem("删除");
        framesPopupListener = new GesturesPopUpMenuActionListener();
        animationPopup = new JPopupMenu();
        animationRenameMenuItem = new JMenuItem("重命名");
        animationCreateMenuItem = new JMenuItem("新建");
        animationDuplicateMenuItem = new JMenuItem("复制");
        animationDeleteMenuItem = new JMenuItem("删除");
        animationTopMenuItem = new JMenuItem("置顶");
        animationUpMenuItem = new JMenuItem("上移");
        animationDownMenuItem = new JMenuItem("下移");
        animationBottomMenuItem = new JMenuItem("置底");
        animationsPopUpMenuActionListener = new AnimationsPopUpMenuActionListener();
        gesturesListMouseListener = new GesturesListMouseListener();
        animationCreateActionListener = new AnimationCreateActionListener();
        gestureListSelectionListener = new GestureListSelectionListener();
        frameInGesturePopUpMenuActionListener = new FrameInGesturePopUpMenuActionListener();
        frameInGestureListMouseListener = new FrameInGestureListMouseListener();
        cumulatetLabel = new JLabel("累积偏移:");
        speedXAroundPanel = new JPanel();
        speedYAroundPanel = new JPanel();
        setLayout(new BorderLayout());
        animationsJList = new JList();
        gesturesJList = new JList();
        frameJList = new JList();
        typeCombo.addActionListener(new AnimationTypeActionListener());
        frameJList.addMouseListener(frameInGestureListMouseListener);
        framePopup.add(gestureCreateFrameMenuItem);
        gestureCreateFrameMenuItem.addActionListener(frameInGesturePopUpMenuActionListener);
        gestureCreateFrameMenuItem.setAccelerator(KeyStroke.getKeyStroke(10, 0));
        JScrollPane jscrollpane = new JScrollPane(frameJList);
        frameJList.setSelectionMode(0);
        frameJList.setCellRenderer(new IconLabelListRenderer());
        gesturesJList.setCellRenderer(new IconLabelListRenderer());
        frameJList.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent mouseevent)
            {
                if(mouseevent.isPopupTrigger())
                {
                    int i = frameJList.locationToIndex(mouseevent.getPoint());
                    if(i > -1)
                        SpriteEditor.sprite.frameHolder.setIndex(i);
                }
            }

            public void mouseReleased(MouseEvent mouseevent)
            {
                if(mouseevent.isPopupTrigger())
                {
                    int i = frameJList.locationToIndex(mouseevent.getPoint());
                    if(i > -1)
                        SpriteEditor.sprite.frameHolder.setIndex(i);
                }
            }

        });
        frameJList.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent keyevent)
            {
                if(keyevent.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    Gesture gesture = new Gesture(SpriteEditor.sprite.frameHolder.get(SpriteEditor.animationPanel.frameJList.getSelectedIndex()), SpriteEditor.sprite, SpriteEditor.sprite.getCurrentAnimation());
                    //daben:remove 不提供重新命名，直接使用Frame的name
                    //String s = (String)JOptionPane.showInputDialog(SpriteEditor.mainFrame, "New gesture name:", "iEdit V.2.0.29", -1, null, null, gesture.getName());
                    //if(s != null && s.length() > 0){
                    //	gesture.setName(s);
                    //}
                    //~daben
                    SpriteEditor.sprite.getCurrentAnimation().add(gesture);
                    SpriteEditor.animationPanel.gesturesListModel.updateAdd();
                    SpriteEditor.animationPanel.gestureInAnimationNoLabel.setText((SpriteEditor.sprite.getCurrentAnimation().getIndex() + 1) + "/" + SpriteEditor.sprite.getCurrentAnimation().size());
                    SpriteEditor.animationPanel.gesturesJList.setSelectedIndex(SpriteEditor.sprite.getCurrentAnimation().getIndex());
                    SpriteEditor.animationPanel.gesturesListModel.updateChange();
                    SpriteEditor.framePanel.frameListModel.updateChange();
                }
            }

        });
        animationReset.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                Animation animation = SpriteEditor.sprite.getCurrentAnimation();
                if(animation == null)
                {
                    return;
                } else
                {
                    animation.reset();
                    SpriteEditor.animationPanel.wysiwygGestureComponent.setPosition(0, 0);
                    SpriteEditor.animationPanel.cumulatetLabel.setText("累积偏移:" + SpriteEditor.sprite.getCurrentAnimation().getTxtSumaPoosition());
                    SpriteEditor.animationPanel.repaint();
                    return;
                }
            }

        });
        jscrollpane.setPreferredSize(new Dimension(140, 25));
        framesMainPanel = new JPanel();
        framesMainPanel.setBorder(BorderFactory.createTitledBorder("帧列表"));
        framesMainPanel.setLayout(new BorderLayout());
        framesMainPanel.add(jscrollpane, "Center");
        
        gesturesJList.addMouseListener(new GesturesListMouseListener());
        gesturesJList.setFocusable(true);
        gesturesJList.addKeyListener(new GesturesListKeyListener());
        animationstListModel = new IconLabelListModel(SpriteEditor.sprite.getAnimations());
        animationsJList.setModel(animationstListModel);
        animationsJList.addMouseListener(new AnimationsListMouseListener());
        animationsJList.addListSelectionListener(new AnimationListSelectionListener());
        animationsJList.setFocusable(true);
        animationsJList.addKeyListener(new AnimationsKeyListener());
        JScrollPane jscrollpane1 = new JScrollPane(gesturesJList);
        gesturesJList.setSelectionMode(0);
        masksComboBox = new JComboBox(SpriteEditor.frameFragmentMasks.getComboBoxModel());
        masksComboBox.setEnabled(SpriteEditor.frameFragmentMasks.isEnabled());
        masksComboBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                repaint();
            }

        });
        JPanel jpanel = new JPanel();
        jpanel.setBorder(BorderFactory.createTitledBorder("隐藏"));
        jpanel.setLayout(new BorderLayout());
        jpanel.add(masksComboBox, "Center");
        jscrollpane1.setPreferredSize(new Dimension(140, 25));
        JPanel jpanel1 = new JPanel();
        jpanel1.setBorder(BorderFactory.createTitledBorder("动画帧列表"));
        jpanel1.setLayout(new BorderLayout());
        jpanel1.add(jscrollpane1, "Center");
        gesturesMainPanel = new JPanel(new BorderLayout());
        gesturesMainPanel.add(jpanel, "North");
        gesturesMainPanel.add(jpanel1, "Center");
        JScrollPane jscrollpane2 = new JScrollPane(animationsJList);
        animationsJList.setSelectionMode(0);
        jscrollpane2.setPreferredSize(new Dimension(140, 25));
        animationsMainPanel = new JPanel();
        animationsMainPanel.setBorder(BorderFactory.createTitledBorder("动画列表"));
        animationsMainPanel.setLayout(new BorderLayout());
       animationsMainPanel.add(jscrollpane2, "Center");
        animationsMainPanel.add(animationCreate, "South");
        //gesturesJList.addMouseListener(gesturesListMouseListener);
        gesturesJList.addListSelectionListener(gestureListSelectionListener);
        gesturePrev.addActionListener(new GesturePrevButtonActionListener());
        gestureNext.addActionListener(new GestureNextButtonActionListener());
        gestureRemove.addActionListener(new GestureRemoveActionListener());
        animationCreate.addActionListener(animationCreateActionListener);
        JPanel jpanel2 = new JPanel();
        jpanel2.add(gesturePrev);
        jpanel2.add(gestureNext);
        jpanel2.add(animationReset);
        jpanel2.add(gestureRemove);
        JPanel jpanel3 = new JPanel();
        jpanel3.add(gestureInAnimationNoLabel);
        jpanel3.add(cumulatetLabel);
        endAnimationAction.setColumns(3);
        endAnimationAction.setValue(new Integer(0));
        endAnimationAction.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                if(SpriteEditor.sprite.getCurrentAnimation() != null)
                {
                    int i = ((Number)endAnimationAction.getValue()).intValue();
                    if(i > 0 && i <= 256){
                    	SpriteEditor.sprite.getCurrentAnimation().setEndAnimAction((byte)i);
                    }
                    else
                        endAnimationAction.setValue(new Integer(SpriteEditor.sprite.getCurrentAnimation().getEndAnimAction()));
                } else
                {
                    endAnimationAction.setValue(new Integer(0));
                }
            }

        });
        JPanel jpanel4 = new JPanel();
        jpanel4.setLayout(new BoxLayout(jpanel4, 0));
        jpanel4.setBorder(BorderFactory.createTitledBorder("动画属性"));
        jpanel4.add(typeCombo);
        jpanel4.add(new JLabel("endAction:"));
        jpanel4.add(endAnimationAction);
        
        jpanel4.add(new JLabel("noLoops:"));
        jpanel4.add(animationLoop);
        gestureControlPanel = new JPanel(new BorderLayout());
        gestureControlPanel.setBorder(BorderFactory.createTitledBorder("动画控制"));
        gestureControlPanel.add(jpanel2, "West");
        gestureControlPanel.add(jpanel3, "Center");
        JButton jbutton = new JButton("设置");
        jbutton.setMnemonic('t');
        jbutton.addActionListener(new ActionListener() {

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
                SpriteEditor.gesturesBackground.setVisible(backgroundShowButton.isSelected());
                repaint();
            }

        });
        final JCheckBox prevFrameShowButton = new JCheckBox("显示上一帧");
        prevFrameShowButton.setMnemonic('p');
        prevFrameShowButton.setSelected(false);
        prevFrameShowButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                wysiwygGestureComponent.setShowPrevFrame(prevFrameShowButton.isSelected());
                repaint();
            }

        });
        JPanel jpanel5 = new JPanel(new BorderLayout());
        jpanel5.add(backgroundShowButton, "West");
        jpanel5.add(jbutton, "East");
        JPanel jpanel6 = new JPanel(new BorderLayout());
        jpanel6.add(prevFrameShowButton, "West");
        JPanel jpanel7 = new JPanel();
        jpanel7.setLayout(new BorderLayout());
        jpanel7.setBorder(BorderFactory.createTitledBorder("背景"));
        jpanel7.add(jpanel5, "North");
        jpanel7.add(jpanel6, "South");
        loopPanel.setBorder(BorderFactory.createTitledBorder("播放控制"));
        playButton.addItemListener(new PlayGestureButtonItemListener());
        gestureTicksPerSecond.addChangeListener(new FramesPerSecondChangeListener());
        gestureTicksPerSecond.setMajorTickSpacing(1);
        gestureTicksPerSecond.setPaintTicks(true);
        gestureTicksPerSecond.setPaintLabels(true);
        loopPanel.add(playButton);
        loopPanel.add(gestureTicksPerSecond);
        loopPanel.add(checkAround);
        JPanel jpanel8 = new JPanel();
        jpanel8.setLayout(new BorderLayout());
        jpanel8.add(gestureControlPanel, "Center");
        jpanel8.add(jpanel4, "East");
        borderCenterPanel = new JPanel();
        JPanel jpanel10 = new JPanel();
        borderCenterPanel.setLayout(new BorderLayout());
        jpanel10.setLayout(new BorderLayout());
        speedXAroundPanel.setLayout(new BorderLayout());
        speedYAroundPanel.setLayout(new BorderLayout());
        titledBorder = BorderFactory.createTitledBorder("当前动画帧");
        borderCenterPanel.setBorder(titledBorder);
        jpanel10.setBorder(BorderFactory.createTitledBorder("帧停留"));
        speedXAroundPanel.setBorder(BorderFactory.createTitledBorder("水平偏移"));
        TitledBorder titledborder = BorderFactory.createTitledBorder("垂直偏移");
        titledborder.setTitleJustification(1);
        speedYAroundPanel.setBorder(titledborder);
        borderCenterPanel.add(wysiwygGestureComponent, "Center");
        jpanel10.add(gestureDuration, "Center");
        speedXAroundPanel.add(gestureSpeedX, "Center");
        speedYAroundPanel.add(gestureSpeedY, "Center");
        gestureDuration.setMajorTickSpacing(1);
        gestureDuration.setPaintTicks(true);
        gestureDuration.setPaintLabels(true);
        gestureDuration.addChangeListener(new GestureDurationChangeListener());
        // daben:add
        gestureDuration.addMouseListener(new MouseAdapter()
        {
            public void mousePressed(MouseEvent mouseevent)
            {
                if (SpriteEditor.animationsSaved)
                    SpriteEditor.animationsSaved = false;
            }
        });
        gestureDuration.addKeyListener(new KeyAdapter()
        {
            public void keyPressed(KeyEvent keyevent)
            {
                switch (keyevent.getKeyCode())
                {
                case KeyEvent.VK_UP:
                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_LEFT:
                case KeyEvent.VK_RIGHT:
                    if (SpriteEditor.animationsSaved)
                        SpriteEditor.animationsSaved = false;
                }
            }
        });
        // ~daben
        animationLoop.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                Animation animation = SpriteEditor.sprite.getCurrentAnimation();
                if(animation != null)
                    animation.setNoLoops((byte)animationLoop.getSelectedIndex());
            }

        });
        gestureSpeedX.setMajorTickSpacing(1);
        gestureSpeedX.setPaintTicks(true);
        gestureSpeedX.setPaintLabels(true);
        gestureSpeedX.addChangeListener(new GestureSpeedXChangeListener());
        // daben:add
        gestureSpeedX.addMouseListener(new MouseAdapter()
        {
            public void mousePressed(MouseEvent mouseevent)
            {
                if (SpriteEditor.animationsSaved)
                    SpriteEditor.animationsSaved = false;
            }
        });
        
        gestureSpeedX.addKeyListener(new KeyAdapter()
        {
            public void keyPressed(KeyEvent keyevent)
            {
                switch (keyevent.getKeyCode())
                {
                case KeyEvent.VK_UP:
                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_LEFT:
                case KeyEvent.VK_RIGHT:
                    if (SpriteEditor.animationsSaved)
                        SpriteEditor.animationsSaved = false;
                }
            }
        });
        // ~daben
        gestureSpeedY.setMajorTickSpacing(1);
        gestureSpeedY.setPaintTicks(true);
        gestureSpeedY.setPaintLabels(true);
        gestureSpeedY.addChangeListener(new GestureSpeedYChangeListener());
        // daben:add
        gestureSpeedY.addMouseListener(new MouseAdapter()
        {
            public void mousePressed(MouseEvent mouseevent)
            {
                if (SpriteEditor.animationsSaved)
                    SpriteEditor.animationsSaved = false;
            }
        });
        
        gestureSpeedY.addKeyListener(new KeyAdapter()
        {
            public void keyPressed(KeyEvent keyevent)
            {
                switch (keyevent.getKeyCode())
                {
                case KeyEvent.VK_UP:
                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_LEFT:
                case KeyEvent.VK_RIGHT:
                    if (SpriteEditor.animationsSaved)
                        SpriteEditor.animationsSaved = false;
                }
            }
        });
        // ~daben
        JPanel jpanel11 = new JPanel();
        jpanel11.setLayout(new BorderLayout());
        jpanel11.add(borderCenterPanel, "Center");
        jpanel11.add(jpanel10, "North");
        jpanel11.add(speedYAroundPanel, "East");
        jpanel11.add(speedXAroundPanel, "South");
        JPanel jpanel12 = new JPanel();
        jpanel12.setLayout(new BorderLayout());
        JPanel jpanel13 = new JPanel();
        jpanel13.add(loopPanel);
        jpanel13.add(jpanel7);
        jpanel12.add(framesMainPanel, "West");
        jpanel12.add(gesturesMainPanel, "Center");
        jpanel12.add(animationsMainPanel, "East");
        jpanel12.add(jpanel13, "South");
        add(jpanel11, "Center");
        add(jpanel12, "East");
        add(jpanel8, "South");
        gesturePopup.add(gestureMoveTopMenuItem);
        gesturePopup.add(gestureMoveUpMenuItem);
        gesturePopup.add(gestureMoveDownMenuItem);
        gesturePopup.add(gestureMoveBottomMenuItem);
        gesturePopup.addSeparator();
        //daben:remove 去掉Gestures的改名，直接使用Frame的名称
        //gesturePopup.add(gestureRenameMenuItem);
        //~daben
        gesturePopup.add(gestureDeleteMenuItem);
        gestureMoveTopMenuItem.addActionListener(framesPopupListener);
        gestureMoveTopMenuItem.setAccelerator(KeyStroke.getKeyStroke(36, 8));
        gestureMoveUpMenuItem.addActionListener(framesPopupListener);
        gestureMoveUpMenuItem.setAccelerator(KeyStroke.getKeyStroke(38, 8));
        gestureMoveDownMenuItem.addActionListener(framesPopupListener);
        gestureMoveDownMenuItem.setAccelerator(KeyStroke.getKeyStroke(40, 8));
        gestureMoveBottomMenuItem.addActionListener(framesPopupListener);
        gestureMoveBottomMenuItem.setAccelerator(KeyStroke.getKeyStroke(35, 8));
        gestureDeleteMenuItem.addActionListener(framesPopupListener);
        gestureDeleteMenuItem.setAccelerator(KeyStroke.getKeyStroke(127, 0));
        gestureRenameMenuItem.addActionListener(framesPopupListener);
        gestureRenameMenuItem.setAccelerator(KeyStroke.getKeyStroke(113, 0));
        animationPopup.add(animationTopMenuItem);
        animationPopup.add(animationUpMenuItem);
        animationPopup.add(animationDownMenuItem);
        animationPopup.add(animationBottomMenuItem);
        animationPopup.addSeparator();
        animationPopup.add(animationCreateMenuItem);
        animationPopup.add(animationDuplicateMenuItem);
        animationPopup.add(animationRenameMenuItem);
        animationPopup.add(animationDeleteMenuItem);
        animationTopMenuItem.addActionListener(animationsPopUpMenuActionListener);
        animationTopMenuItem.setAccelerator(KeyStroke.getKeyStroke(36, 8));
        animationUpMenuItem.addActionListener(animationsPopUpMenuActionListener);
        animationUpMenuItem.setAccelerator(KeyStroke.getKeyStroke(38, 8));
        animationDownMenuItem.addActionListener(animationsPopUpMenuActionListener);
        animationDownMenuItem.setAccelerator(KeyStroke.getKeyStroke(40, 8));
        animationBottomMenuItem.addActionListener(animationsPopUpMenuActionListener);
        animationBottomMenuItem.setAccelerator(KeyStroke.getKeyStroke(35, 8));
        animationCreateMenuItem.addActionListener(animationCreateActionListener);
        animationCreateMenuItem.setAccelerator(KeyStroke.getKeyStroke(155, 0));
        animationRenameMenuItem.addActionListener(animationsPopUpMenuActionListener);
        animationRenameMenuItem.setAccelerator(KeyStroke.getKeyStroke(113, 0));
        animationDeleteMenuItem.addActionListener(animationsPopUpMenuActionListener);
        animationDeleteMenuItem.setAccelerator(KeyStroke.getKeyStroke(127, 0));
        animationDuplicateMenuItem.addActionListener(animationsPopUpMenuActionListener);
        animationDuplicateMenuItem.setAccelerator(KeyStroke.getKeyStroke(155, 1));
    }
    
    public TitledBorder titledBorder;
    public JPanel borderCenterPanel;

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
            backgroundEditor = new BackgroundEditor(SpriteEditor.gesturesBackground, this, "动画背景");
    }

    public void initAnimations()
    {
        if(SpriteEditor.sprite.getCurrentAnimation() != null)
        {
            animationLabel.setText(SpriteEditor.sprite.getCurrentAnimation().getName());
            animationstListModel = new IconLabelListModel(SpriteEditor.sprite.getAnimations());
            animationsJList.setModel(animationstListModel);
            animationsJList.setSelectedIndex(SpriteEditor.sprite.getIndex());
            initGestures();
        }
    }

    public void initGestures()
    {
        if(SpriteEditor.sprite.getCurrentAnimation() != null)
        {
            typeCombo.setSelectedIndex(SpriteEditor.sprite.getCurrentAnimation().getType() != 16 ? 1 : 0);
            gestureInAnimationNoLabel.setText((SpriteEditor.sprite.getCurrentAnimation().getIndex() + 1) + "/" + SpriteEditor.sprite.getCurrentAnimation().size());
            gesturesListModel = new IconLabelListModel(SpriteEditor.sprite.getCurrentAnimation().getGestures());
            gesturesJList.setModel(gesturesListModel);
            gesturesJList.setSelectedIndex(SpriteEditor.sprite.getCurrentAnimation().getIndex());
            animationLoop.setSelectedIndex(SpriteEditor.sprite.getCurrentAnimation().getNoLoops());
            endAnimationAction.setValue(new Integer(SpriteEditor.sprite.getCurrentAnimation().getEndAnimAction()));
        }
    }

    public void initFrames()
    {
        frameListModel = new IconLabelListModel(SpriteEditor.sprite.frameHolder.getFrames());
        frameJList.setModel(frameListModel);
    }

    public void reload()
    {
    }

    public void repaint()
    {
        try
        {
            SpriteEditor.animationPanel.gestureInAnimationNoLabel.setText("G[" + (SpriteEditor.sprite.getCurrentAnimation().getIndex() + 1) + " of " + SpriteEditor.sprite.getCurrentAnimation().size() + "] T[" + (SpriteEditor.sprite.getCurrentAnimation().get().getTick() + 1) + " of " + SpriteEditor.sprite.getCurrentAnimation().get().getLength() + "]");
        }
        catch(Exception exception) { }
        super.repaint();
    }

    public Animator animator;
    public WysiwygGestureComponent wysiwygGestureComponent;
    public JLabel animationLabel;
    public JList animationsJList;
    public JList gesturesJList;
    public JList frameJList;
    public JPanel framesMainPanel;
    public JComboBox typeCombo;
    public JPanel animationsMainPanel;
    public JButton animationCreate;
    public JPanel gestureControlPanel;
    public JButton gestureRemove;
    public JButton gesturePrev;
    public JButton gestureNext;
    public JButton animationReset;
    public JPanel loopPanel;
    public JToggleButton playButton;
    public JLabel gestureInAnimationNoLabel;
    public IconLabelListModel animationstListModel;
    public IconLabelListModel gesturesListModel;
    public IconLabelListModel frameListModel;
    public JPanel gesturesMainPanel;
    /**
     * 控制动画的播放 是否循环
     */
    public JCheckBox checkAround;
    
    public JSlider gestureTicksPerSecond;
    public JSlider gestureSpeedX;
    public JSlider gestureSpeedY;
    public JSlider gestureDuration;
    public JComboBox animationLoop;
    final JFormattedTextField endAnimationAction = new JFormattedTextField(new NumberFormatter(NumberFormat.getIntegerInstance()));
    
    // 动画的右键菜单
    public JPopupMenu gesturePopup;
    public JMenuItem gestureMoveTopMenuItem;
    public JMenuItem gestureMoveUpMenuItem;
    public JMenuItem gestureMoveDownMenuItem;
    public JMenuItem gestureMoveBottomMenuItem;
    public JMenuItem gestureRenameMenuItem;
    public JMenuItem gestureDeleteMenuItem;
    
    ActionListener framesPopupListener;
    public JPopupMenu animationPopup;
    public JMenuItem animationRenameMenuItem;
    public JMenuItem animationCreateMenuItem;
    public JMenuItem animationDuplicateMenuItem;
    public JMenuItem animationDeleteMenuItem;
    public JMenuItem animationTopMenuItem;
    public JMenuItem animationUpMenuItem;
    public JMenuItem animationDownMenuItem;
    public JMenuItem animationBottomMenuItem;
    
    public static JPopupMenu framePopup = new JPopupMenu();
    public static JMenuItem gestureCreateFrameMenuItem = new JMenuItem("把当前帧加入动画");
    private ActionListener animationsPopUpMenuActionListener;
    private MouseListener gesturesListMouseListener;
    public ActionListener animationCreateActionListener;
    private ListSelectionListener gestureListSelectionListener;
    public ActionListener frameInGesturePopUpMenuActionListener;
    private MouseListener frameInGestureListMouseListener;
    public static final String CUMULATED_MOVE_LABEL = "累积偏移:";
    public JLabel cumulatetLabel;
    public JPanel speedXAroundPanel;
    public JPanel speedYAroundPanel;
    public BackgroundEditor backgroundEditor;
    public JComboBox masksComboBox;

}
