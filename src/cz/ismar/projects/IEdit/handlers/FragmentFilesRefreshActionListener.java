/**
 * 2007-11-5 daben
 * 
 */
package cz.ismar.projects.IEdit.handlers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Iterator;

import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.io.InOut;
import cz.ismar.projects.IEdit.structure.FragmentHolder;

/**
 * 【帧编辑区】“刷新”切片信息操作
 * @author Lori
 *
 */
public class FragmentFilesRefreshActionListener implements ActionListener
{

    public void actionPerformed(ActionEvent e)
    {
        int index = SpriteEditor.framePanel.fragmentFilesList.getSelectedIndex();
        FragmentHolder fragmentholder = (FragmentHolder)SpriteEditor.sprite.fragmentHoldersList.getFragmentHolders().remove(index);
        fragmentholder.getFragments().clear();
        File file1 = fragmentholder.getImageFile();
        File file = new File(file1.getAbsolutePath().replace("png", "xml"));
        InOut.inFragmentXML(fragmentholder, file);
        SpriteEditor.sprite.fragmentHoldersList.getFragmentHolders().add(index, fragmentholder);
        
        SpriteEditor.sprite.fragmentHoldersList.allFragments = new FragmentHolder();
        for (Iterator iterator = SpriteEditor.sprite.fragmentHoldersList.getFragmentHolders().iterator(); iterator.hasNext(); )
        {
            FragmentHolder fragmentholder1 = (FragmentHolder)iterator.next();
            SpriteEditor.sprite.fragmentHoldersList.allFragments.addAll(fragmentholder1.getFragments());
        }
        
        SpriteEditor.initFragmentIcons();
        SpriteEditor.initFrameIcons();
        SpriteEditor.framePanel.initFragmentFiles();
        SpriteEditor.framePanel.initFragments();
        SpriteEditor.framePanel.initFrames();
        SpriteEditor.framePanel.initFrameFragments();
        SpriteEditor.animationPanel.initGestures();
        SpriteEditor.animationPanel.initFrames();
        SpriteEditor.animationPanel.initAnimations();
        SpriteEditor.framePanel.repaint();
    }

}
