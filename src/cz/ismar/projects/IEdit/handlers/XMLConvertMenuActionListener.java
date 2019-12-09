package cz.ismar.projects.IEdit.handlers;

import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.io.*;
import cz.ismar.projects.IEdit.structure.FrameFragmentMasks;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.*;

/**
 * xml目录导出操作
 * @author Lori
 *
 */
public class XMLConvertMenuActionListener
    implements ActionListener
{

    public XMLConvertMenuActionListener()
    {
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        JFileChooser jfilechooser = SpriteEditor.getDirChooser();
        int i = jfilechooser.showOpenDialog(SpriteEditor.mainFrame);
        int j = 0;
        try
        {
            if(i == 0)
            {
                File file = jfilechooser.getSelectedFile();
                int k = JOptionPane.showConfirmDialog(SpriteEditor.mainFrame, "Convert all sprites in directory: " + file + " ?", SpriteEditor.TITLE, 0);
                if(k == 0)
                {
                    File afile[] = file.listFiles(new FilenameFilter() {

                        public boolean accept(File file3, String s1)
                        {
                            return s1.endsWith(".xml");
                        }

                    });
                    for(int l = 0; l < afile.length; l++)
                    {
                        File file1 = afile[l];
                        System.out.println("converting " + file1);
                        try
                        {
                            SpriteEditor.sprite = InOut.inXML(file1.getAbsolutePath());
                            SpriteEditor.setStatus(SpriteEditor.xmlSavePath + " loaded");
                            if(SpriteEditor.frameFragmentMasks.isEnabled() != SpriteEditor.useMasksMenuItem.isSelected())
                                SpriteEditor.useMasksMenuItem.doClick(0);
                        }
                        catch(OldFileFormatException oldfileformatexception)
                        {
                            File file2 = new File(file1.getParent(), oldfileformatexception.getImagePath());
                            InOut.convertOldFormat(file1, file2, 3);
                            j++;
                        }
                        catch(WrongFileFormatException wrongfileformatexception)
                        {
                            String s = "Error when reloading.. MESSAGE: " + wrongfileformatexception.getLocalizedMessage();
                            JOptionPane.showMessageDialog(SpriteEditor.mainFrame, s, SpriteEditor.TITLE, 0);
                            SpriteEditor.setStatus(s);
//                            wrongfileformatexception.printStackTrace();
                            System.out.println("Convert Error");
                        }
                    }

                }
            }
        }
        catch(Exception exception) { }
        JOptionPane.showMessageDialog(SpriteEditor.mainFrame, j + " files converted");
    }
}
