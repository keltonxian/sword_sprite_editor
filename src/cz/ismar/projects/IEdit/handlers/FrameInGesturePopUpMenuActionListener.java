/**
 * 2007-9-18 daben
 * - remove
 * --> in actionPerformed(ActionEvent actionevent) #see daben:remove
 * 
 * 2007-10-31 daben 
 * - add
 *      SpriteEditor.mainFramingPanel.frameJList.setSelectedIndex(SpriteEditor.mainGesturePanel.frameJList.getSelectedIndex());
 */

package cz.ismar.projects.IEdit.handlers;

import cz.ismar.projects.IEdit.IconLabelListModel;
import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.structure.*;
import cz.ismar.projects.IEdit.ui.FramePanel;
import cz.ismar.projects.IEdit.ui.AnimationPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * 在【动画编辑区】的“帧列表”里，弹出菜单“把当前帧加入动画”
 * 
 * 操作
 * @author Lori
 *
 */
public class FrameInGesturePopUpMenuActionListener
    implements ActionListener
{

    public FrameInGesturePopUpMenuActionListener()
    {
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        if (SpriteEditor.sprite.frameHolder.size() == 0)
            return;
        if(SpriteEditor.sprite.getCurrentAnimation() == null)
        {
            if(0 == JOptionPane.showConfirmDialog(SpriteEditor.mainFrame, "没有创建动画，现在创建一个吗?", SpriteEditor.TITLE, 2, 3))
            {
                SpriteEditor.animationPanel.animationCreateActionListener.actionPerformed(null);
            } else
            {
                JOptionPane.showConfirmDialog(SpriteEditor.mainFrame, "没创建动画，不能放置帧..", SpriteEditor.TITLE, 0, 0);
                return;
            }
        }
        Gesture gesture = new Gesture(SpriteEditor.sprite.frameHolder.get(SpriteEditor.animationPanel.frameJList.getSelectedIndex()), SpriteEditor.sprite, SpriteEditor.sprite.getCurrentAnimation());
        //daben:remove  不提供重新命名，直接使用Frame的name
        //String s = (String)JOptionPane.showInputDialog(SpriteEditor.mainFrame, "New gesture name:", "iEdit V.2.0.29", -1, null, null, gesture.getName());
        //if(s != null && s.length() > 0)
        //    gesture.setName(s);
        //~daben
        SpriteEditor.sprite.getCurrentAnimation().add(gesture);
        SpriteEditor.animationPanel.gesturesListModel.updateAdd();
        SpriteEditor.animationPanel.gestureInAnimationNoLabel.setText((SpriteEditor.sprite.getCurrentAnimation().getIndex() + 1) + "/" + SpriteEditor.sprite.getCurrentAnimation().size());
        SpriteEditor.animationPanel.gesturesJList.setSelectedIndex(SpriteEditor.sprite.getCurrentAnimation().getIndex());
        SpriteEditor.animationPanel.gesturesListModel.updateChange();
        SpriteEditor.framePanel.frameListModel.updateChange();
        
        // daben:add
        SpriteEditor.framePanel.frameJList.setSelectedIndex(SpriteEditor.animationPanel.frameJList.getSelectedIndex());
        // ~daben
    }
}
