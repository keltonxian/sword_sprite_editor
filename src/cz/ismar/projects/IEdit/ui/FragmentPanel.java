/**
 * 2008-1-18 Lori(赤浪)
 * 
 * 切片编辑区是否已经保存
 *   设置SpriteEditor.fragmentsSaved为false的地方：
 *   - createFragment();
 *   - moveTopFragment()
 *   - moveUpFragment()
 *   - moveDownFragment()
 *   - moveBottomFragment()
 *   - deleteFragment()
 * 
 * 
 * 
 * */
package cz.ismar.projects.IEdit.ui;

import cz.ismar.projects.IEdit.*;
import cz.ismar.projects.IEdit.handlers.*;
import cz.ismar.projects.IEdit.structure.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ListSelectionListener;

import cz.ismar.projects.IEdit.structure.Frame;
/**
 * 切片列表面板类
 * 
 * 切片的名字，id都用JTable(表格)存储
 * 
 * @author Lori(赤浪)
 *
 */
public class FragmentPanel extends JPanel
{

    public void initFragments()
    {
        fragmentTableModel = new TableModel(SpriteEditor.sprite.fragmentHolder.getFragments());
        fragmentTable.setModel(fragmentTableModel);
        
        //通知所有侦听器，已插入范围在 [firstRow, lastRow]（包括）的行
        fragmentTableModel.fireTableRowsInserted(0, SpriteEditor.sprite.fragmentHolder.size());
        if(fragmentEditorComponent != null)
        {
            fragmentEditorComponent.repaint();
        }
    }

    public FragmentPanel()
    {
    	// 右键菜单
        fragmentTablePopupMenu = new JPopupMenu();
        moveTopFragmentMenuItem = new JMenuItem("置顶");
        moveUpFragmentMenuItem = new JMenuItem("上移");
        moveDownFragmentMenuItem = new JMenuItem("下移");
        moveBottomFragmentMenuItem = new JMenuItem("置底");
        deleteFragmentMenuItem = new JMenuItem("删除");
        fragmentListSelectionListener = new FragmentListSelectionListener();
        fragmentTableMouseListener = new FragmentTableMouseListener();
        fragmentTable = new JTable();
        fragmentEditorComponent = new FragmentEditComponent();
        fragmentTable.setSelectionMode(0);
        ListSelectionModel listselectionmodel = fragmentTable.getSelectionModel();
        listselectionmodel.addListSelectionListener(fragmentListSelectionListener);
        fragmentTable.addMouseListener(fragmentTableMouseListener);
        fragmentTable.setFocusable(true);
        fragmentTable.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent keyevent)
            {
                int row = fragmentTable.getSelectedRow();
                
                if(row >= 0)
                {
                    if(keyevent.getKeyCode() == KeyEvent.VK_DELETE)
                    {
                        SpriteEditor.sprite.fragmentHolder.setIndex(row);
                        deleteFragment();
                    } else
                    if(keyevent.getKeyCode() == KeyEvent.VK_HOME && keyevent.getModifiers() == Event.ALT_MASK)
                    {
                        SpriteEditor.sprite.fragmentHolder.setIndex(row);
                        moveTopFragment();
                    } else
                    if(keyevent.getKeyCode() == KeyEvent.VK_UP && keyevent.getModifiers() == Event.ALT_MASK)
                    {
                        SpriteEditor.sprite.fragmentHolder.setIndex(row);
                        moveUpFragment();
                    } else
                    if(keyevent.getKeyCode() == KeyEvent.VK_DOWN && keyevent.getModifiers() == Event.ALT_MASK)
                    {
                        SpriteEditor.sprite.fragmentHolder.setIndex(row);
                        moveDownFragment();
                    } else
                    if(keyevent.getKeyCode() == KeyEvent.VK_END && keyevent.getModifiers() == Event.ALT_MASK)
                    {
                        SpriteEditor.sprite.fragmentHolder.setIndex(row);
                        moveBottomFragment();
                    } else
                    	//修改名字
                    if(keyevent.getKeyCode() == KeyEvent.VK_F2)
                    {
                        SpriteEditor.fragmentsSaved = false;
                    }
                }
            }

        });
        JScrollPane jscrollpane = new JScrollPane(fragmentEditorComponent);
        jscrollpane.setPreferredSize(new Dimension(1, 1));
        JPanel jpanel = new JPanel(new BorderLayout());
        jpanel.setBorder(BorderFactory.createTitledBorder("图片"));
        jpanel.setLayout(new BorderLayout());
        jpanel.add(jscrollpane, "Center");
        setLayout(new BorderLayout());
        add(jpanel, "Center");
        JPanel westPanel = new JPanel(new BorderLayout());
        
        JPanel cmds_panel = new JPanel();
        cmds_panel.setLayout(new BoxLayout(cmds_panel, BoxLayout.Y_AXIS));
        cmds_panel.setBorder(BorderFactory.createTitledBorder("切片文件"));
        JButton cmd_load = createButton("加载");
        cmd_load.setMnemonic('L');
        cmd_load.addActionListener(new FragmentFileLoadButtonActionListener());
        JButton cmd_save = createButton("保存");
        cmd_save.setMnemonic('S');
        cmd_save.addActionListener(new FragmentFileSaveButtonActionListener());
        JButton cmd_reload = createButton("重新加载");
        cmd_reload.setMnemonic('R');
        cmd_reload.addActionListener(new FragmentFileReloadButtonActionListener());
        cmds_panel.add(cmd_load);
        cmds_panel.add(cmd_save);
        cmds_panel.add(cmd_reload);
        
        JScrollPane jscrollpane1 = new JScrollPane(fragmentTable);
        jscrollpane1.setPreferredSize(new Dimension(140, 80));
        createButton = createButton("新建切片");
        createButton.setMnemonic('C');
        createButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                createFragment();
            }

        });
        JPanel fragmentList_panel = new JPanel();
        fragmentList_panel.setBorder(BorderFactory.createTitledBorder("切片列表"));
        fragmentList_panel.setLayout(new BoxLayout(fragmentList_panel, 1));
        fragmentList_panel.add(jscrollpane1);
        fragmentList_panel.add(createButton);
        JButton cmd_backgroundColor = new JButton("背景色");
        cmd_backgroundColor.setMnemonic('B');
        cmd_backgroundColor.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                java.awt.Color color = JColorChooser.showDialog(FragmentPanel.this, "选择背景色", fragmentEditorComponent.backgroundColor);
                if(color != null)
                {
                    fragmentEditorComponent.backgroundColor = color;
                    fragmentEditorComponent.repaint();
                }
            }

        });
        JPanel background_panel = new JPanel();
        background_panel.setBorder(BorderFactory.createTitledBorder("背景"));
        background_panel.add(cmd_backgroundColor);
        westPanel.add(cmds_panel, "North");
        westPanel.add(fragmentList_panel, "Center");
        westPanel.add(background_panel, "South");
        add(westPanel, "West");
        fragmentTablePopupMenu.add(moveTopFragmentMenuItem);
        fragmentTablePopupMenu.add(moveUpFragmentMenuItem);
        fragmentTablePopupMenu.add(moveDownFragmentMenuItem);
        fragmentTablePopupMenu.add(moveBottomFragmentMenuItem);
        fragmentTablePopupMenu.addSeparator();
        fragmentTablePopupMenu.add(deleteFragmentMenuItem);
        moveTopFragmentMenuItem.setAccelerator(KeyStroke.getKeyStroke(36, 8));
        moveTopFragmentMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                moveTopFragment();
            }

        });
        moveUpFragmentMenuItem.setAccelerator(KeyStroke.getKeyStroke(38, 8));
        moveUpFragmentMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                moveUpFragment();
            }

        });
        moveDownFragmentMenuItem.setAccelerator(KeyStroke.getKeyStroke(40, 8));
        moveDownFragmentMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                moveDownFragment();
            }

        });
        moveBottomFragmentMenuItem.setAccelerator(KeyStroke.getKeyStroke(35, 8));
        moveBottomFragmentMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                moveBottomFragment();
            }

        });
        deleteFragmentMenuItem.setAccelerator(KeyStroke.getKeyStroke(127, 0));
        deleteFragmentMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                deleteFragment();
            }

        });
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

    /**
     * 新建切片
     */
    private void createFragment()
    {
        Fragment fragment = new Fragment(SpriteEditor.sprite.fragmentHolder, 
        		(int)SpriteEditor.fragmentPanel.fragmentEditorComponent.newFragmentRectangle.getX(), 
        		(int)SpriteEditor.fragmentPanel.fragmentEditorComponent.newFragmentRectangle.getY(), 
        		(int)SpriteEditor.fragmentPanel.fragmentEditorComponent.newFragmentRectangle.getWidth(), 
        		(int)SpriteEditor.fragmentPanel.fragmentEditorComponent.newFragmentRectangle.getHeight(),
        		"Name" + SpriteEditor.sprite.fragmentHolder.getNextFragmentId());
        
//        System.out.println("here -> " + SpriteEditor.project.fragmentHolder.getNextFragmentId());
//        System.out.println("" + Fragment.getNextID());
        fragment.updateImage();
        SpriteEditor.sprite.fragmentHolder.add(fragment);
        // daben:add
        if (SpriteEditor.fragmentsSaved)
            SpriteEditor.fragmentsSaved = false;
        // ~daben
        SpriteEditor.fragmentPanel.fragmentTableModel.fireTableRowsInserted(SpriteEditor.framePanel.frameFragmentListModel.size(), SpriteEditor.framePanel.frameFragmentListModel.size());
        SpriteEditor.fragmentPanel.fragmentTable.getSelectionModel().setSelectionInterval(SpriteEditor.sprite.fragmentHolder.size() - 1, SpriteEditor.sprite.fragmentHolder.size() - 1);
        SpriteEditor.framePanel.allFragmentListModel.update();
    }

    private void deleteFragment()
    {
        Fragment fragment = SpriteEditor.sprite.fragmentHolder.remove();
        // daben:add
        if (SpriteEditor.fragmentsSaved)
            SpriteEditor.fragmentsSaved = false;
        // ~daben
        Frame aframe[] = SpriteEditor.sprite.removeFromAllFrames(fragment,true);
        if(aframe != null && aframe.length > 0)
        {
            for(int i = 0; i < aframe.length; i++)
            {
                Frame frame = aframe[i];
                frame.updateImage();
            }

        }
        fragmentTableModel.fireTableRowsDeleted(SpriteEditor.sprite.fragmentHolder.getIndex(), SpriteEditor.sprite.fragmentHolder.getIndex());
        fragmentEditorComponent.repaint();
    }

    private void moveTopFragment()
    {
        if(SpriteEditor.sprite.fragmentHolder.moveTopFragment())
        {
            fragmentTableModel.fireTableDataChanged();
            int i = SpriteEditor.sprite.fragmentHolder.getIndex();
            fragmentTable.getSelectionModel().setSelectionInterval(i, i);
            // daben:add
            if (SpriteEditor.fragmentsSaved)
                SpriteEditor.fragmentsSaved = false;
            // ~daben
        }
    }

    private void moveUpFragment()
    {
        if(SpriteEditor.sprite.fragmentHolder.moveUpFragment())
        {
            fragmentTableModel.fireTableDataChanged();
            int i = SpriteEditor.sprite.fragmentHolder.getIndex();
            fragmentTable.getSelectionModel().setSelectionInterval(i, i);
            
            // daben:add
            if (SpriteEditor.fragmentsSaved)
                SpriteEditor.fragmentsSaved = false;
            // ~daben
        }
    }

    private void moveDownFragment()
    {
        if(SpriteEditor.sprite.fragmentHolder.moveDownFragment())
        {
            fragmentTableModel.fireTableDataChanged();
            int i = SpriteEditor.sprite.fragmentHolder.getIndex();
            fragmentTable.getSelectionModel().setSelectionInterval(i, i);
            // daben:add
            if (SpriteEditor.fragmentsSaved)
                SpriteEditor.fragmentsSaved = false;
            // ~daben
        }
    }

    private void moveBottomFragment()
    {
        if(SpriteEditor.sprite.fragmentHolder.moveBottomFragment())
        {
            fragmentTableModel.fireTableDataChanged();
            int i = SpriteEditor.sprite.fragmentHolder.getIndex();
            fragmentTable.getSelectionModel().setSelectionInterval(i, i);
            
            // daben:add
            if (SpriteEditor.fragmentsSaved)
                SpriteEditor.fragmentsSaved = false;
            // ~daben
        }
    }

    /**
     * 切片编辑器区
     */
    public FragmentEditComponent fragmentEditorComponent;
    
    /**
     * 切片区表格
     * [name1, name2, name3...]
     * 
     * 可以有鼠标事件，以及 按键事件
     */
    public JTable fragmentTable;
    public TableModel fragmentTableModel;
    
    /** 在切片区表格弹出的右键菜单 */
    public JPopupMenu fragmentTablePopupMenu;
    public JMenuItem moveTopFragmentMenuItem;
    public JMenuItem moveUpFragmentMenuItem;
    public JMenuItem moveDownFragmentMenuItem;
    public JMenuItem moveBottomFragmentMenuItem;
    public JMenuItem deleteFragmentMenuItem;
    
    private ListSelectionListener fragmentListSelectionListener;
    private MouseListener fragmentTableMouseListener;
    public JButton createButton;
}
