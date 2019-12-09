package cz.ismar.projects.IEdit.handlers;

import cz.ismar.projects.IEdit.IconLabelListModel;
import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.structure.*;
import cz.ismar.projects.IEdit.ui.FramePanel;
import cz.ismar.projects.IEdit.ui.AnimationPanel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.PrintStream;
import javax.swing.*;

/**
 * “动画帧列表”里 按键操作
 * @author Lori
 *
 */
public class GesturesListKeyListener extends KeyAdapter
{

    public GesturesListKeyListener()
    {
    }

    public void keyReleased(KeyEvent keyevent)
    {
        if(keyevent.getKeyCode() == KeyEvent.VK_HOME && keyevent.getModifiers() == KeyEvent.ALT_MASK)
        {
            if(SpriteEditor.sprite.getCurrentAnimation().moveTop())
                SpriteEditor.animationPanel.gesturesJList.getSelectionModel().setSelectionInterval(SpriteEditor.sprite.getCurrentAnimation().getIndex(), SpriteEditor.sprite.getCurrentAnimation().getIndex());
        } else
        if(keyevent.getKeyCode() == KeyEvent.VK_UP && keyevent.getModifiers() == KeyEvent.ALT_MASK)
        {
            if(SpriteEditor.sprite.getCurrentAnimation().moveUp())
                SpriteEditor.animationPanel.gesturesJList.getSelectionModel().setSelectionInterval(SpriteEditor.sprite.getCurrentAnimation().getIndex(), SpriteEditor.sprite.getCurrentAnimation().getIndex());
        } else
        if(keyevent.getKeyCode() == KeyEvent.VK_DOWN && keyevent.getModifiers() == KeyEvent.ALT_MASK)
        {
            if(SpriteEditor.sprite.getCurrentAnimation().moveDown())
                SpriteEditor.animationPanel.gesturesJList.getSelectionModel().setSelectionInterval(SpriteEditor.sprite.getCurrentAnimation().getIndex(), SpriteEditor.sprite.getCurrentAnimation().getIndex());
        } else
        if(keyevent.getKeyCode() == KeyEvent.VK_END && keyevent.getModifiers() == KeyEvent.ALT_MASK)
        {
            if(SpriteEditor.sprite.getCurrentAnimation().moveBottom())
                SpriteEditor.animationPanel.gesturesJList.getSelectionModel().setSelectionInterval(SpriteEditor.sprite.getCurrentAnimation().getIndex(), SpriteEditor.sprite.getCurrentAnimation().getIndex());
        } else
        if(keyevent.getKeyCode() == KeyEvent.VK_DELETE)
        {
            if(SpriteEditor.sprite.getCurrentAnimation().remove())
            {
                SpriteEditor.animationPanel.initGestures();
                SpriteEditor.animationPanel.gesturesJList.getSelectionModel().setSelectionInterval(SpriteEditor.sprite.getCurrentAnimation().getIndex(), SpriteEditor.sprite.getCurrentAnimation().getIndex());
            }
        } else
        if(keyevent.getKeyCode() == KeyEvent.VK_F2)
        {
            String s = (String)JOptionPane.showInputDialog(SpriteEditor.mainFrame, "命名为:", SpriteEditor.TITLE, -1, null, null, SpriteEditor.sprite.getCurrentAnimation().get().getName());
            if(s != null && s.length() > 0)
                SpriteEditor.sprite.getCurrentAnimation().get().setName(s);
            SpriteEditor.animationPanel.gesturesListModel.updateChange();
            SpriteEditor.animationPanel.gesturesJList.getSelectionModel().setSelectionInterval(SpriteEditor.sprite.getCurrentAnimation().getIndex(), SpriteEditor.sprite.getCurrentAnimation().getIndex());
        }
        SpriteEditor.framePanel.frameFragmentListModel.update();
        SpriteEditor.framePanel.repaint();
    }
}
