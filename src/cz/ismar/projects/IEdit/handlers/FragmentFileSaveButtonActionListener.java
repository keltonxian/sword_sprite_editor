/**
 * 2007-9-10 daben
 * - add in method
 *      actionPerformed(ActionEvent actionevent)
 */

package cz.ismar.projects.IEdit.handlers;

import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.io.InOut;
import cz.ismar.projects.IEdit.io.OldFileFormatException;
import cz.ismar.projects.IEdit.io.WrongFileFormatException;
import cz.ismar.projects.IEdit.structure.FragmentHolder;
import cz.ismar.projects.IEdit.structure.Sprite;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import javax.swing.JOptionPane;

/**
 * 【切片区】 “保存”切片操作
 * @author Lori
 *
 */
public class FragmentFileSaveButtonActionListener
    implements ActionListener
{

    public FragmentFileSaveButtonActionListener()
    {
    }

    public void actionPerformed(ActionEvent actionevent)
    {
    	//daben:add 抹除未"Create"的切片框，否则"load"时还存在
    	SpriteEditor.fragmentPanel.fragmentEditorComponent.newFragmentRectangle = null;
    	
    	SpriteEditor.fragmentPanel.fragmentEditorComponent.repaint();
    	//~daben
        try
        {
            FragmentHolder fragmentholder = SpriteEditor.sprite.fragmentHolder;
            if(fragmentholder.getImageFile() == null)
            {
            	throw new NullPointerException("getFragmentImage is null");
            }
            
            String strError = fragmentholder.isCanSave();
            if(null != strError && !"".equals(strError))
            {
            	 JOptionPane.showMessageDialog(SpriteEditor.mainFrame, strError, "Save Flags Error", JOptionPane.INFORMATION_MESSAGE);
            	 return;
            } 
            
            File file = new File(fragmentholder.getImageFile().getParentFile(), fragmentholder.getName() + ".xml");
//            System.out.println("XMLSaveAsMenuActionListener.actionPerformed path:" + file.getAbsolutePath());
            if(!file.getAbsolutePath().endsWith(".xml"))
                file = new File(file.getAbsolutePath() + ".xml");
            if(!file.exists())
                file.createNewFile();
            InOut.outFragmentFileXML(SpriteEditor.sprite.fragmentHolder, file);
            SpriteEditor.setStatus(file + " saved");
            // daben:add
            SpriteEditor.fragmentsSaved = true;
            JOptionPane.showMessageDialog(SpriteEditor.mainFrame, "切片文件已保存: " + file, "Save Flags Succeed", JOptionPane.INFORMATION_MESSAGE);
            // ~daben
        }
        catch(Exception exception)
        {
            String s = "保存时出错: " + exception.getLocalizedMessage();
            JOptionPane.showMessageDialog(SpriteEditor.mainFrame, s, SpriteEditor.TITLE, 0);
            SpriteEditor.setStatus(s);
//            exception.printStackTrace();
            System.out.println("Save Error");
        }
    }
}
