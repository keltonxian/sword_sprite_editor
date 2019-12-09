/**
 * 2007-11-1 daben
 * - add  JOptionPane.showMessageDialog(...
 * 
 */

package cz.ismar.projects.IEdit.handlers;

import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.io.InOut;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.PrintStream;
import javax.swing.JOptionPane;

public class XMLSaveMenuActionListener
    implements ActionListener
{

    public XMLSaveMenuActionListener()
    {
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        try
        {
            if(SpriteEditor.xmlSavePath != null)
            {
                System.out.println("XMLSaveAsMenuActionListener.actionPerformed path:" + SpriteEditor.xmlSavePath);
                File file = new File(SpriteEditor.xmlSavePath);
                
                //容错，
                //如果保存的精灵文件不是以.xml结束，则自动加上
                if(!file.getAbsolutePath().endsWith(".xml"))
                {
                	file = new File(file.getAbsolutePath() + ".xml");
                }
                
                //如果不存在该文件，则创建该文件
                if(!file.exists())
                    file.createNewFile();
                
                InOut.outXML(SpriteEditor.sprite, file);
                
                SpriteEditor.setStatus(file + " saved");
                
                SpriteEditor.animationsSaved = true;
            } else
            {
            	//转化为另存为 操作
                (new XMLSaveAsMenuActionListener()).actionPerformed(actionevent);
            }
        }
        catch(Exception exception)
        {
            String s = "保存时出错: " + exception.getLocalizedMessage();
            JOptionPane.showMessageDialog(SpriteEditor.mainFrame, s, SpriteEditor.TITLE, 0);
            SpriteEditor.setStatus(s);
        }
    }
}
