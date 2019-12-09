package cz.ismar.projects.IEdit.handlers;

import cz.ismar.projects.IEdit.IconLabelListModel;
import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.structure.Animation;
import cz.ismar.projects.IEdit.structure.Sprite;
import cz.ismar.projects.IEdit.ui.FramePanel;
import cz.ismar.projects.IEdit.ui.AnimationPanel;

import java.awt.Event;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;


/**
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
public class AnimationsKeyListener extends KeyAdapter
{

    public AnimationsKeyListener()
    {
    }

    public void keyReleased(KeyEvent keyevent)
    {
    	//“置顶”
        if(keyevent.getKeyCode() == KeyEvent.VK_HOME && keyevent.getModifiers() == Event.ALT_MASK){
        	
        	SpriteEditor.sprite.moveTop();
        }
        else
        if(keyevent.getKeyCode() == KeyEvent.VK_UP && keyevent.getModifiers() == Event.ALT_MASK)
            SpriteEditor.sprite.moveUp();
        else
        if(keyevent.getKeyCode() == KeyEvent.VK_DOWN && keyevent.getModifiers() == Event.ALT_MASK)
            SpriteEditor.sprite.moveDown();
        else
        if(keyevent.getKeyCode() == KeyEvent.VK_END && keyevent.getModifiers() == Event.ALT_MASK)
            SpriteEditor.sprite.moveBottom();
        else
        //“重命名”
        if(keyevent.getKeyCode() == KeyEvent.VK_F2)
        {
            String s = (String)JOptionPane.showInputDialog(SpriteEditor.mainFrame, "Rename frameset to:", SpriteEditor.TITLE, -1, null, null, SpriteEditor.sprite.getCurrentAnimation().getName());
            if(s != null && s.length() > 0)
                SpriteEditor.sprite.getCurrentAnimation().setName(s);
            SpriteEditor.animationPanel.animationstListModel.updateChange();
        } else
        	//删除
        if(keyevent.getKeyCode() == KeyEvent.VK_DELETE)
        {
            if(0 == JOptionPane.showConfirmDialog(SpriteEditor.mainFrame, "确定删除动画?", SpriteEditor.TITLE, 2, 3))
            {
            	
                SpriteEditor.sprite.remove();
                //System.out.println("  ==== "+SpriteEditor.project.getAnimations().size());
                SpriteEditor.animationPanel.animationstListModel.updateDelete();
            }
        } else
        if(keyevent.getKeyCode() == KeyEvent.VK_INSERT && keyevent.getModifiers() == Event.SHIFT_MASK)
            SpriteEditor.sprite.insert(SpriteEditor.sprite.getCurrentAnimation().copy());
        else
        if(keyevent.getKeyCode() == KeyEvent.VK_INSERT)
        {
            Animation animation = new Animation("New animation", SpriteEditor.sprite);
            String s1 = (String)JOptionPane.showInputDialog(SpriteEditor.mainFrame, "新动画名称:", SpriteEditor.TITLE, -1, null, null, animation.getName());
            if(s1 != null && s1.length() > 0)
                animation.setName(s1);
            SpriteEditor.sprite.add(animation);
            SpriteEditor.animationPanel.initAnimations();
            SpriteEditor.framePanel.repaint();
        }
        SpriteEditor.animationPanel.initAnimations();
        SpriteEditor.animationPanel.initGestures();
    }
}
