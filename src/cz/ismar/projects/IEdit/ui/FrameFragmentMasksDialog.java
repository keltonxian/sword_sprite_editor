package cz.ismar.projects.IEdit.ui;

import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.structure.FrameFragmentMasks;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;

public class FrameFragmentMasksDialog
{

    public FrameFragmentMasksDialog()
    {
    }

    public void createGui()
    {
        dialog = new JDialog(SpriteEditor.mainFrame, "Animation Export", true);
        dialog.setFocusable(true);
        dialog.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent keyevent)
            {
                if(keyevent.getKeyCode() == 27 || keyevent.getKeyCode() == 10)
                    dispose();
            }

        });
        JButton jbutton = new JButton("OK");
        jbutton.setMnemonic('O');
        jbutton.setToolTipText("[Enter]");
        jbutton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                dispose();
            }

        });
        final ComboBoxModel comboBoxModel = SpriteEditor.frameFragmentMasks.getComboBoxModel();
        final JList masksList = new JList(comboBoxModel);
        masksList.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent keyevent)
            {
                int i = masksList.getSelectedIndex();
                if(i > 0 && keyevent.getKeyCode() == 36 && keyevent.getModifiers() == 8)
                {
                    SpriteEditor.frameFragmentMasks.moveTop(i);
                    masksList.setSelectedIndex(0);
                } else
                if(i < comboBoxModel.getSize() - 1 && keyevent.getKeyCode() == 35 && keyevent.getModifiers() == 8)
                {
                    SpriteEditor.frameFragmentMasks.moveBottom(i);
                    masksList.setSelectedIndex(comboBoxModel.getSize() - 1);
                } else
                if(i > 0 && keyevent.getKeyCode() == 38 && keyevent.getModifiers() == 8)
                {
                    SpriteEditor.frameFragmentMasks.moveUp(i);
                    masksList.setSelectedIndex(i - 1);
                } else
                if(i < comboBoxModel.getSize() - 1 && keyevent.getKeyCode() == 40 && keyevent.getModifiers() == 8)
                {
                    SpriteEditor.frameFragmentMasks.moveDown(i);
                    masksList.setSelectedIndex(i + 1);
                }
            }

        });
        masksList.addMouseListener(new MouseInputAdapter() {

            public void mouseReleased(MouseEvent mouseevent)
            {
                if(mouseevent.isPopupTrigger())
                {
                    final int index = masksList.locationToIndex(mouseevent.getPoint());
                    masksList.setSelectedIndex(index);
                    JPopupMenu jpopupmenu = new JPopupMenu();
                    JMenuItem jmenuitem = new JMenuItem("moveTop");
                    jmenuitem.setAccelerator(KeyStroke.getKeyStroke(36, 8));
                    if(index > 0)
                        jmenuitem.addActionListener(new ActionListener() {

                            public void actionPerformed(ActionEvent actionevent)
                            {
                                SpriteEditor.frameFragmentMasks.moveTop(index);
                                masksList.setSelectedIndex(0);
                            }

                        });
                    else
                        jmenuitem.setEnabled(false);
                    JMenuItem jmenuitem1 = new JMenuItem("moveBottom");
                    jmenuitem1.setAccelerator(KeyStroke.getKeyStroke(35, 8));
                    if(index < comboBoxModel.getSize() - 1)
                        jmenuitem1.addActionListener(new ActionListener() {

                            public void actionPerformed(ActionEvent actionevent)
                            {
                                SpriteEditor.frameFragmentMasks.moveBottom(index);
                                masksList.setSelectedIndex(comboBoxModel.getSize() - 1);
                            }

                        });
                    else
                        jmenuitem1.setEnabled(false);
                    JMenuItem jmenuitem2 = new JMenuItem("moveUp");
                    jmenuitem2.setAccelerator(KeyStroke.getKeyStroke(38, 8));
                    if(index > 0)
                        jmenuitem2.addActionListener(new ActionListener() {

                            public void actionPerformed(ActionEvent actionevent)
                            {
                                SpriteEditor.frameFragmentMasks.moveUp(index);
                                masksList.setSelectedIndex(index - 1);
                            }

                        });
                    else
                        jmenuitem2.setEnabled(false);
                    JMenuItem jmenuitem3 = new JMenuItem("moveDown");
                    jmenuitem3.setAccelerator(KeyStroke.getKeyStroke(40, 8));
                    if(index < comboBoxModel.getSize() - 1)
                        jmenuitem3.addActionListener(new ActionListener() {

                            public void actionPerformed(ActionEvent actionevent)
                            {
                                SpriteEditor.frameFragmentMasks.moveDown(index);
                                masksList.setSelectedIndex(index + 1);
                            }

                        });
                    else
                        jmenuitem3.setEnabled(false);
                    jpopupmenu.add(jmenuitem);
                    jpopupmenu.add(jmenuitem2);
                    jpopupmenu.add(jmenuitem3);
                    jpopupmenu.add(jmenuitem1);
                    jpopupmenu.show(mouseevent.getComponent(), mouseevent.getX(), mouseevent.getY());
                }
            }



        });
        JButton jbutton1 = new JButton("New");
        jbutton1.setMnemonic('N');
        jbutton1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                String s = (String)JOptionPane.showInputDialog(dialog, "name:", SpriteEditor.TITLE, -1, null, null, "mask" + comboBoxModel.getSize());
                SpriteEditor.frameFragmentMasks.addConfiguration(s);
                dialog.repaint();
            }

        });
        JButton jbutton2 = new JButton("Copy");
        jbutton2.setMnemonic('C');
        jbutton2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                int i = masksList.getSelectedIndex();
                if(i >= 0)
                {
                    String s = (String)JOptionPane.showInputDialog(dialog, "name:", SpriteEditor.TITLE, -1, null, null, "copy_" + SpriteEditor.frameFragmentMasks.getName(i));
                    SpriteEditor.frameFragmentMasks.copyConfiguration(i, s);
                    dialog.repaint();
                }
            }

        });
        JButton jbutton3 = new JButton("Rename");
        jbutton3.setMnemonic('R');
        jbutton3.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                int i = masksList.getSelectedIndex();
                if(i >= 0)
                {
                    String s = (String)JOptionPane.showInputDialog(dialog, "name:", SpriteEditor.TITLE, -1, null, null, SpriteEditor.frameFragmentMasks.getName(i));
                    SpriteEditor.frameFragmentMasks.renameConfiguration(i, s);
                    dialog.repaint();
                }
            }

        });
        JButton jbutton4 = new JButton("Delete");
        jbutton4.setMnemonic('D');
        jbutton4.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                SpriteEditor.frameFragmentMasks.removeConfiguration(masksList.getSelectedIndex());
            }

        });
        JPanel jpanel = new JPanel(new BorderLayout());
        jpanel.add(new JScrollPane(masksList), "Center");
        JPanel jpanel1 = new JPanel(new FlowLayout(1));
        jpanel1.add(jbutton);
        jpanel1.add(jbutton1);
        jpanel1.add(jbutton2);
        jpanel1.add(jbutton3);
        jpanel1.add(jbutton4);
        JPanel jpanel2 = new JPanel(new BorderLayout());
        jpanel2.add(jpanel, "Center");
        jpanel2.add(jpanel1, "South");
        dialog.setContentPane(jpanel2);
        dialog.pack();
        dialog.setVisible(true);
    }

    private void dispose()
    {
        dialog.dispose();
    }

    private JDialog dialog;


}
