package cz.ismar.projects.IEdit.handlers;

import cz.ismar.projects.IEdit.IconLabelListModel;
import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.structure.Animation;
import cz.ismar.projects.IEdit.structure.Sprite;
import cz.ismar.projects.IEdit.ui.AnimationPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/**
 * 
 * 功能等同于AnimationKeyListener
 * @see AnimationKeyListener
 * 在“动画列表”中的操作
 * 
 * - 置顶
 * - 上移
 * - 下移
 * - 置底
 * - 新建
 * - 复制
 * - F2
 * - 删除
 * @author Lori
 *
 */
public class AnimationsPopUpMenuActionListener
    implements ActionListener
{

    public AnimationsPopUpMenuActionListener()
    {
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        if(actionevent.getActionCommand().equalsIgnoreCase("置顶"))
            SpriteEditor.sprite.moveTop();
        else
        if(actionevent.getActionCommand().equalsIgnoreCase("上移"))
            SpriteEditor.sprite.moveUp();
        else
        if(actionevent.getActionCommand().equalsIgnoreCase("下移"))
            SpriteEditor.sprite.moveDown();
        else
        if(actionevent.getActionCommand().equalsIgnoreCase("置底"))
            SpriteEditor.sprite.moveBottom();
        else
        if(actionevent.getActionCommand().equalsIgnoreCase("重命名"))
        {
            String s = (String)JOptionPane.showInputDialog(SpriteEditor.mainFrame, "Rename frameset to:", SpriteEditor.TITLE, -1, null, null, SpriteEditor.sprite.getCurrentAnimation().getName());
            if(s != null && s.length() > 0)
                SpriteEditor.sprite.getCurrentAnimation().setName(s);
            SpriteEditor.animationPanel.animationstListModel.updateChange();
        } else
        if(actionevent.getActionCommand().equalsIgnoreCase("删除"))
        {
            if(0 == JOptionPane.showConfirmDialog(SpriteEditor.mainFrame, "确定删除动画?", SpriteEditor.TITLE, 2, 3))
            {
                SpriteEditor.sprite.remove();
                SpriteEditor.animationPanel.animationstListModel.updateDelete();
            }
        } else
        if(actionevent.getActionCommand().equalsIgnoreCase("复制"))
            SpriteEditor.sprite.insert(SpriteEditor.sprite.getCurrentAnimation().copy());
        SpriteEditor.animationPanel.initAnimations();
        SpriteEditor.animationPanel.initGestures();
    }
}
