/**
 * 2007-10-10 daben
 * - add Fragment.setCurrentID(-1);
 */

package cz.ismar.projects.IEdit.handlers;

import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.io.InOut;
import cz.ismar.projects.IEdit.structure.FragmentHolder;
import cz.ismar.projects.IEdit.structure.Fragment;
import cz.ismar.projects.IEdit.structure.Sprite;
import cz.ismar.projects.IEdit.ui.FragmentPanel;
import cz.ismar.projects.IEdit.ui.FragmentEditComponent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.PrintStream;
import javax.swing.*;

/**
 * 【切片区】 "加载"切片资源操作
 * @author Lori
 *
 */
public class FragmentFileLoadButtonActionListener
    implements ActionListener
{

    public FragmentFileLoadButtonActionListener()
    {
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        JFileChooser jfilechooser = SpriteEditor.getFileChooser();
        jfilechooser.setFileFilter(SpriteEditor.pngFileFilter);
        int i = jfilechooser.showOpenDialog(SpriteEditor.mainFrame);
        if(i == 0)
        {
            FragmentHolder fragmentholder = new FragmentHolder();
            SpriteEditor.sprite.fragmentHolder = fragmentholder;
            File file = jfilechooser.getSelectedFile();
            fragmentholder.setImageFile(file);
            File file1 = new File(file.getParentFile(), fragmentholder.getName() + ".xml");
            ImageIcon imageicon = fragmentholder.getIcon();
            SpriteEditor.fragmentPanel.fragmentEditorComponent.setImageSize(imageicon.getIconWidth(), imageicon.getIconHeight());
            if(file1.exists())
                try
                {
                    //daben:add 读XML前给currentID赋初值(注意Fragment.currentID是static)
//                    Fragment.setCurrentID(-1);
                    //~daben
                    InOut.inFragmentXML(fragmentholder, file1);
                    SpriteEditor.setStatus(file1 + " loaded");
                }
                catch(Exception exception)
                {
                    String s = "重新加载时出错: " + exception.getLocalizedMessage();
                    JOptionPane.showMessageDialog(SpriteEditor.mainFrame, s, SpriteEditor.TITLE, 0);
                    SpriteEditor.setStatus(s);
//                    exception.printStackTrace();
                    System.out.println("Error when reloading");
                }
            SpriteEditor.initFragmentIcons();
            SpriteEditor.fragmentPanel.initFragments();
            SpriteEditor.fragmentPanel.fragmentEditorComponent.repaint();
        }
    }
}
