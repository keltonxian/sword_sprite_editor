package cz.ismar.projects.IEdit.ui;

import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.structure.Background;
import cz.ismar.projects.IEdit.structure.BackgroundItem;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class BackgroundEditor extends JDialog
    implements TableModelListener
{

    public BackgroundEditor(Background background1, Component component, String s)
    {
        super(SpriteEditor.mainFrame, s, false);
        background = background1;
        parentComponent = component;
        createGui();
    }

    private void createGui()
    {
        setDefaultCloseOperation(0);
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent windowevent)
            {
                quit();
            }

        });
        setFocusable(true);
        addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent keyevent)
            {
                if(keyevent.getKeyCode() == 27)
                    quit();
            }

        });
        backgroundTableModel = new BackgroundTableModel(background);
        backgroundTableModel.addTableModelListener(this);
        table = new JTable(backgroundTableModel);
        table.setColumnSelectionAllowed(false);
        table.setSelectionMode(0);
        table.setRowHeight(100);
        table.getColumnModel().getColumn(0).setMaxWidth(40);
        table.getColumnModel().getColumn(1).setMaxWidth(50);
        table.getColumnModel().getColumn(2).setMaxWidth(50);
        table.addKeyListener(new KeyAdapter() {

            public void keyReleased(KeyEvent keyevent)
            {
                if(keyevent.getKeyCode() == 38 && keyevent.getModifiers() == 8)
                    moveUp();
                else
                if(keyevent.getKeyCode() == 40 && keyevent.getModifiers() == 8)
                    moveDown();
                else
                if(keyevent.getKeyCode() == 127)
                    delete();
                else
                if(keyevent.getKeyCode() == 155)
                    create();
                else
                if(keyevent.getKeyCode() == 27)
                    quit();
            }

        });
        getContentPane().add(new JScrollPane(table), "Center");
        JButton jbutton = new JButton("确定");
        jbutton.setMnemonic('O');
        jbutton.setToolTipText("[确定]");
        jbutton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                quit();
            }

        });
        JButton jbutton1 = new JButton("新建");
        jbutton1.setMnemonic('C');
        jbutton1.setToolTipText("[新建背景]");
        jbutton1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                create();
            }

        });
        JButton jbutton2 = new JButton("删除");
        jbutton2.setMnemonic('D');
        jbutton2.setToolTipText("[删除背景]");
        jbutton2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                delete();
            }

        });
        JButton jbutton3 = new JButton("上移");
        jbutton3.setToolTipText("[上移]");
        jbutton3.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                moveUp();
            }

        });
        JButton jbutton4 = new JButton("下移");
        jbutton4.setToolTipText("[下移]");
        jbutton4.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                moveDown();
            }

        });
        JButton jbutton5 = new JButton("背景色");
        jbutton5.setMnemonic('B');
        jbutton5.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                java.awt.Color color = JColorChooser.showDialog(BackgroundEditor.this, "选择背景色", background.getBgColor());
                if(color != null)
                {
                    background.setBgColor(color);
                    parentComponent.repaint();
                }
            }

        });
        JPanel jpanel = new JPanel(new FlowLayout(1));
        jpanel.add(jbutton);
        JPanel jpanel1 = new JPanel(new FlowLayout(1));
        jpanel1.add(jbutton1);
        jpanel1.add(jbutton2);
        jpanel1.add(jbutton3);
        jpanel1.add(jbutton4);
        JPanel jpanel2 = new JPanel(new FlowLayout(1));
        jpanel2.add(jbutton5);
        JPanel jpanel3 = new JPanel(new BorderLayout());
        jpanel3.add(jpanel, "West");
        jpanel3.add(jpanel1, "Center");
        jpanel3.add(jpanel2, "East");
        getContentPane().add(jpanel3, "South");
        pack();
    }

    private void moveDown()
    {
        int i = table.getSelectedRow();
        if(backgroundTableModel.moveDown(i))
        {
            i++;
            table.getSelectionModel().setSelectionInterval(i, i);
        }
    }

    private void moveUp()
    {
        int i = table.getSelectedRow();
        if(backgroundTableModel.moveUp(i))
        {
            i--;
            table.getSelectionModel().setSelectionInterval(i, i);
        }
    }

    private void delete()
    {
        int i = table.getSelectedRow();
        if(backgroundTableModel.removeRow(i))
        {
            if(i == table.getRowCount())
                i--;
            table.getSelectionModel().setSelectionInterval(i, i);
        }
    }

    private void create()
    {
        JFileChooser jfilechooser = SpriteEditor.getFileChooser();
        jfilechooser.setFileFilter(SpriteEditor.pngFileFilter);
        int i = jfilechooser.showOpenDialog(SpriteEditor.mainFrame);
        if(i == 0)
        {
            File file = jfilechooser.getSelectedFile();
            try
            {
                BackgroundItem backgrounditem = new BackgroundItem();
                backgrounditem.setImagePath(file.getPath());
                backgroundTableModel.addRow(backgrounditem, table.getSelectedRow());
            }
            catch(Exception exception)
            {
//                exception.printStackTrace();
                System.out.println("create Error");
                JOptionPane.showMessageDialog(SpriteEditor.mainFrame, "图片加载失败!", SpriteEditor.TITLE, 0);
            }
        }
        table.requestFocus();
    }

    public void showGui()
    {
        setVisible(true);
    }

    public void tableChanged(TableModelEvent tablemodelevent)
    {
        parentComponent.repaint();
    }

    public void quit()
    {
        dispose();
    }

    private Background background;
    private Component parentComponent;
    private JTable table;
    private BackgroundTableModel backgroundTableModel;






}
