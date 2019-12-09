/**
 * 2007-9-14 daben
 * - add
 * --> SpriteEditor.initFrameFragmentIcons();
 */

package cz.ismar.projects.IEdit.handlers;

import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.io.InOut;
import cz.ismar.projects.IEdit.structure.FragmentHolder;
import cz.ismar.projects.IEdit.structure.FrameFragmentMasks;
import cz.ismar.projects.IEdit.ui.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JOptionPane;

/**
 * "文件" － “重新加载”操作
 * @author Lori
 *
 */
public class ReloadMenuActionListener
    implements ActionListener
{

    public ReloadMenuActionListener()
    {
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        // daben:add
        if (!SpriteEditor.isAnimationSaved())
            return;
        // ~daben
        
        try
        {
            if(SpriteEditor.xmlSavePath != null)
            {
                // daben:add
                FragmentHolder oldFragmentHolder = SpriteEditor.sprite.fragmentHolder;
                // ~daben
                
                SpriteEditor.sprite = InOut.inXML(SpriteEditor.xmlSavePath);
                // daben:add
                SpriteEditor.sprite.fragmentHolder = oldFragmentHolder;
                
                // ~daben
                SpriteEditor.setStatus(SpriteEditor.xmlSavePath + " loaded");
                if(SpriteEditor.frameFragmentMasks.isEnabled() != SpriteEditor.useMasksMenuItem.isSelected())
                    SpriteEditor.useMasksMenuItem.doClick(0);
                SpriteEditor.initFragmentIcons();
                //daben:add
                SpriteEditor.initFrameFragmentIcons();
                //~daben
                SpriteEditor.initFrameIcons();
                SpriteEditor.fragmentPanel.initFragments();
                SpriteEditor.framePanel.initFragmentFiles();
                SpriteEditor.framePanel.initFragments();
                SpriteEditor.framePanel.initFrames();
                SpriteEditor.framePanel.initFrameFragments();
                SpriteEditor.animationPanel.initGestures();
                SpriteEditor.animationPanel.initFrames();
                SpriteEditor.animationPanel.initAnimations();
            }
        }
        catch(Exception exception)
        {
            String s = "重新加载失败: " + exception.getLocalizedMessage();
            JOptionPane.showMessageDialog(SpriteEditor.mainFrame, s, SpriteEditor.TITLE, 0);
            SpriteEditor.setStatus(s);
            exception.printStackTrace();
            System.out.println("Error when reloading");
        }
        
        SpriteEditor.animationsSaved = true;
    }
}
