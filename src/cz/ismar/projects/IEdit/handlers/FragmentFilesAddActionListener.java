/**
 * 2007-11-1 daben
 * - remove  SpriteEditor.project.fragmentHolder = fragmentholder;
 *           SpriteEditor.mainFragmentPanel.initFragments();
 */

package cz.ismar.projects.IEdit.handlers;

import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.io.InOut;
import cz.ismar.projects.IEdit.structure.*;
import cz.ismar.projects.IEdit.ui.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.PrintStream;
import javax.swing.*;

/**
 * 在【帧编辑区】“增加”切片文件操作
 * @author Lori
 *
 */
public class FragmentFilesAddActionListener
    implements ActionListener
{

    public FragmentFilesAddActionListener()
    {
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        JFileChooser jfilechooser = SpriteEditor.getFileChooser();
        jfilechooser.setFileFilter(SpriteEditor.xmlFileFilter);
        int i = jfilechooser.showOpenDialog(SpriteEditor.mainFrame);
        if(i == 0)
        {
            FragmentHolder fragmentholder = new FragmentHolder();
            // daben:remove
//            SpriteEditor.project.fragmentHolder = fragmentholder;
            // ~daben
            File file = jfilechooser.getSelectedFile();
            File file1 = new File(file.getParentFile(), FragmentHolder.getName(file) + ".png");
            fragmentholder.setImageFile(file1);
            ImageIcon imageicon = fragmentholder.getIcon();
            // daben:remove
//            SpriteEditor.mainFragmentPanel.clipper.setImageSize(imageicon.getIconWidth(), imageicon.getIconHeight());
            // ~daben
            try
            {
                InOut.inFragmentXML(fragmentholder, file);
                SpriteEditor.setStatus(file + " loaded");
                if(SpriteEditor.sprite.fragmentHoldersList.add(fragmentholder))
                {
//                    SpriteEditor.mainFragmentPanel.clipper.repaint();
                    SpriteEditor.initFragmentIcons();
                    SpriteEditor.initFrameIcons();
                    // daben:remove
//                    SpriteEditor.mainFragmentPanel.initFragments();
                    // ~daben
                    SpriteEditor.framePanel.initFragmentFiles();
                    SpriteEditor.framePanel.initFragments();
                    SpriteEditor.framePanel.initFrames();
                    SpriteEditor.framePanel.initFrameFragments();
                    SpriteEditor.animationPanel.initGestures();
                    SpriteEditor.animationPanel.initFrames();
                    SpriteEditor.animationPanel.initAnimations();
                    SpriteEditor.framePanel.repaint();
                } else
                {
                    String s = "文件已经加载!";
                    JOptionPane.showMessageDialog(SpriteEditor.mainFrame, s, SpriteEditor.TITLE, 0);
                }
            }
            catch(Exception exception)
            {
                String s1 = "重新加载时出错: " + exception.getLocalizedMessage();
                JOptionPane.showMessageDialog(SpriteEditor.mainFrame, s1, SpriteEditor.TITLE, 0);
                SpriteEditor.setStatus(s1);
//                exception.printStackTrace();
                System.out.println("Add Error");
            }
        }
    }
}
