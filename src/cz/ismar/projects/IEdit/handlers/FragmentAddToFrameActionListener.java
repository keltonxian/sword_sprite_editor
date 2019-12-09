/**
 * 2007-9-14 daben
 * - add
 * -->  SpriteEditor.project.frameHolder.get().updateImage();
 *      SpriteEditor.mainFramingPanel.frameListModel.update(); 
 */

package cz.ismar.projects.IEdit.handlers;

import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.structure.*;
import cz.ismar.projects.IEdit.ui.FramePanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/**
 * 将“切片” 添加到 “帧切片列表”操作
 * 
 * 在【帧编辑区】
 * @author Lori
 *
 */
public class FragmentAddToFrameActionListener
    implements ActionListener
{

    public FragmentAddToFrameActionListener()
    {
    }

    /**
     * 创建帧
     */
    public void actionPerformed(ActionEvent actionevent)
    {
        if(SpriteEditor.sprite.frameHolder.getCurrentFrame() == null)
        {
            if(0 == JOptionPane.showConfirmDialog(SpriteEditor.mainFrame, "还没有创建帧，现在创建一个?", SpriteEditor.TITLE, 2, 3))
            {
                SpriteEditor.framePanel.frameCreateActionListener.actionPerformed(null);
                SpriteEditor.sprite.frameHolder.getCurrentFrame().add(0, new FrameFragment(SpriteEditor.sprite.fragmentHoldersList.getAllFragments().get()));
                SpriteEditor.framePanel.initFrameFragments();
            }
        }else
        {
            SpriteEditor.sprite.frameHolder.getCurrentFrame().saveUndo();
            
            SpriteEditor.sprite.frameHolder.getCurrentFrame().add(0, new FrameFragment(SpriteEditor.sprite.fragmentHoldersList.getAllFragments().get()));
            SpriteEditor.framePanel.initFrameFragments();
        }
        
        SpriteEditor.sprite.frameHolder.getCurrentFrame().updateImage();
        SpriteEditor.framePanel.frameListModel.update();
        SpriteEditor.animationPanel.frameListModel.update();
        
        SpriteEditor.animationsSaved = false;
    }
}
