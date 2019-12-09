package cz.ismar.projects.IEdit.handlers;

import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.io.InOut;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.PrintStream;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class XMLSaveAsMenuActionListener
    implements ActionListener
{

    public XMLSaveAsMenuActionListener()
    {
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        try
        {
            JFileChooser jfilechooser = SpriteEditor.getFileChooser();
            jfilechooser.setFileFilter(SpriteEditor.xmlFileFilter);
            if(SpriteEditor.xmlSavePath != null)
            {
                File file = new File(SpriteEditor.xmlSavePath);
                if(file.isFile())
                    file = file.getParentFile();
                else
                if(!file.isDirectory())
                    file = null;
                if(file != null && file.exists())
                    jfilechooser.setCurrentDirectory(file);
            }
            jfilechooser.setFileFilter(SpriteEditor.xmlFileFilter);
//            System.out.println("XMLSaveAsMenuActionListener.actionPerformed d:" + jfilechooser.getCurrentDirectory());
//            System.out.println("XMLSaveAsMenuActionListener.actionPerformed f:" + jfilechooser.getSelectedFile());
            int i = jfilechooser.showSaveDialog(SpriteEditor.mainFrame);
            if(i == 0)
            {
                File file1 = jfilechooser.getSelectedFile();
                SpriteEditor.xmlSavePath = file1.getAbsolutePath();
                if(!SpriteEditor.xmlSavePath.endsWith(".xml"))
                    SpriteEditor.xmlSavePath = SpriteEditor.xmlSavePath + ".xml";
//                System.out.println("XMLSaveAsMenuActionListener.actionPerformed newPath" + SpriteEditor.xmlSavePath);
            }
            if(SpriteEditor.xmlSavePath != null)
            {
//                System.out.println("XMLSaveAsMenuActionListener.actionPerformed path:" + SpriteEditor.xmlSavePath);
                File file2 = new File(SpriteEditor.xmlSavePath);
                if(!file2.exists())
                    file2.createNewFile();
                InOut.outXML(SpriteEditor.sprite, file2);
                SpriteEditor.setStatus(file2 + " saved");
                // daben:add
                SpriteEditor.animationsSaved = true;
                // ~daben
            }
        }
        catch(Exception exception)
        {
            String s = "保存时出错: " + exception.getLocalizedMessage();
            JOptionPane.showMessageDialog(SpriteEditor.mainFrame, s, SpriteEditor.TITLE, 0);
            SpriteEditor.setStatus(s);
//            exception.printStackTrace();
            System.out.println("Save As XML Error");
        }
    }
}
