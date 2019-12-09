/**
 * 2007-11-5 daben
 * - add keyPressed()
 */

package cz.ismar.projects.IEdit.handlers;

import cz.ismar.projects.IEdit.IconLabelListModel;
import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.structure.*;
import cz.ismar.projects.IEdit.ui.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import javax.swing.*;

/**
 * 【帧编辑区】里的，“帧列表” 按键操作
 * @author Lori
 *
 */
public class FrameKeyListener extends KeyAdapter
{

    public FrameKeyListener()
    {
    }
    
    // daben:add
    public void keyPressed(KeyEvent keyevent)
    {
        int i = keyevent.getKeyCode();
        switch (i)
        {
        case KeyEvent.VK_UP:
        case KeyEvent.VK_DOWN:
            SpriteEditor.sprite.frameHolder.setIndex(SpriteEditor.framePanel.frameJList.getSelectedIndex());
            SpriteEditor.framePanel.initFrameFragments();
            SpriteEditor.framePanel.frameEditComponent.setTmpFrame(null);
        }
    }
    // ~daben

    public void keyReleased(KeyEvent keyevent)
    {
        if(keyevent.getKeyCode() == KeyEvent.VK_HOME && keyevent.getModifiers() == KeyEvent.ALT_MASK)
        {
            if(SpriteEditor.sprite.frameHolder.moveTop())
            {
                SpriteEditor.framePanel.frameJList.getSelectionModel().setSelectionInterval(SpriteEditor.sprite.frameHolder.getIndex(), SpriteEditor.sprite.frameHolder.getIndex());
                SpriteEditor.animationPanel.initFrames();
            }
        } else
        if(keyevent.getKeyCode() == KeyEvent.VK_UP && keyevent.getModifiers() == KeyEvent.ALT_MASK)
        {
            if(SpriteEditor.sprite.frameHolder.moveUp())
            {
                SpriteEditor.framePanel.frameJList.getSelectionModel().setSelectionInterval(SpriteEditor.sprite.frameHolder.getIndex(), SpriteEditor.sprite.frameHolder.getIndex());
                SpriteEditor.animationPanel.initFrames();
            }
        } else
        if(keyevent.getKeyCode() == KeyEvent.VK_DOWN && keyevent.getModifiers() == KeyEvent.ALT_MASK)
        {
            if(SpriteEditor.sprite.frameHolder.moveDown())
            {
                SpriteEditor.framePanel.frameJList.getSelectionModel().setSelectionInterval(SpriteEditor.sprite.frameHolder.getIndex(), SpriteEditor.sprite.frameHolder.getIndex());
                SpriteEditor.animationPanel.initFrames();
            }
        } else
        if(keyevent.getKeyCode() == KeyEvent.VK_END && keyevent.getModifiers() == KeyEvent.ALT_MASK)
        {
            if(SpriteEditor.sprite.frameHolder.moveBottom())
            {
                SpriteEditor.framePanel.frameJList.getSelectionModel().setSelectionInterval(SpriteEditor.sprite.frameHolder.getIndex(), SpriteEditor.sprite.frameHolder.getIndex());
                SpriteEditor.animationPanel.initFrames();
            }
        } else
        if(keyevent.getKeyCode() == KeyEvent.VK_INSERT && keyevent.getModifiers() == KeyEvent.SHIFT_MASK)
        {
            Frame frame = SpriteEditor.sprite.frameHolder.getCurrentFrame();
            Frame frame3 = frame.copy();
            String s2 = (String)JOptionPane.showInputDialog(SpriteEditor.mainFrame, "新帧名字:", SpriteEditor.TITLE, -1, null, null, SpriteEditor.sprite.frameHolder.getCurrentFrame().getName() + "_copy");
            if(s2 != null && s2.length() > 0)
                frame3.setName(s2);
            SpriteEditor.frameFragmentMasks.copyMasksForFrame(frame, frame3);
            SpriteEditor.sprite.frameHolder.insert(frame3);
            SpriteEditor.framePanel.initFrames();
            SpriteEditor.animationPanel.initFrames();
            int i = SpriteEditor.sprite.frameHolder.getIndex();
            SpriteEditor.framePanel.frameJList.getSelectionModel().setSelectionInterval(i, i);
        } else
        if(keyevent.getKeyCode() == KeyEvent.VK_DELETE)
        {
            cz.ismar.projects.IEdit.structure.Animation aanimation[] = SpriteEditor.sprite.getAnimationsContainingFrame(SpriteEditor.sprite.frameHolder.getCurrentFrame());
            if(aanimation.length > 0)
            {
                if(0 == JOptionPane.showConfirmDialog(SpriteEditor.mainFrame, "帧已经使用:\n" + Arrays.asList(aanimation) + ".\n确定要删除吗?", SpriteEditor.TITLE, 2, 2))
                {
                    SpriteEditor.sprite.removeFromAllAnimations(SpriteEditor.sprite.frameHolder.getCurrentFrame());
                    SpriteEditor.sprite.frameHolder.remove();
                    SpriteEditor.animationPanel.initGestures();
                    SpriteEditor.framePanel.frameListModel.updateDelete();
                }
            } else
            if(0 == JOptionPane.showConfirmDialog(SpriteEditor.mainFrame, "帧未使用\n确定要删除吗?", SpriteEditor.TITLE, 2, 3))
            {
                SpriteEditor.sprite.frameHolder.remove();
                SpriteEditor.framePanel.frameListModel.updateDelete();
                SpriteEditor.framePanel.initFrameFragments();
                SpriteEditor.framePanel.initFrames();
                SpriteEditor.animationPanel.initFrames();
            }
        } else
        if(keyevent.getKeyCode() == KeyEvent.VK_INSERT)
        {
            Frame frame1 = new Frame(SpriteEditor.sprite);
            String s1 = (String)JOptionPane.showInputDialog(SpriteEditor.mainFrame, "新帧名字:", SpriteEditor.TITLE, -1, null, null, frame1.getName());
            if(s1 != null && s1.length() > 0)
                frame1.setName(s1);
            SpriteEditor.framePanel.initFrames();
            SpriteEditor.animationPanel.initFrames();
            SpriteEditor.animationPanel.initGestures();
            SpriteEditor.sprite.frameHolder.add(frame1);
            SpriteEditor.framePanel.frameListModel.updateAdd();
            SpriteEditor.framePanel.frameFragmentListModel.updateAdd();
            SpriteEditor.framePanel.frameJList.getSelectionModel().setSelectionInterval(SpriteEditor.framePanel.frameListModel.getSize() - 1, SpriteEditor.framePanel.frameListModel.getSize() - 1);
        } else
        if(keyevent.getKeyCode() == KeyEvent.VK_F2)
        {
            String s = (String)JOptionPane.showInputDialog(SpriteEditor.mainFrame, "新帧名字:", SpriteEditor.TITLE, -1, null, null, SpriteEditor.sprite.frameHolder.getCurrentFrame().getName());
            if(s != null && s.length() > 0)
            {
                SpriteEditor.sprite.frameHolder.getCurrentFrame().setName(s);
                // daben:add
                if (SpriteEditor.animationsSaved)
                    SpriteEditor.animationsSaved = false;
                // ~daben
            }
            SpriteEditor.animationPanel.initFrames();
            SpriteEditor.framePanel.frameListModel.updateChange();
        } else
        if(keyevent.getKeyCode() == KeyEvent.VK_ENTER)
        {
            Frame frame2 = SpriteEditor.sprite.frameHolder.getCurrentFrame();
            SpriteEditor.framePanel.setHelpFrame(frame2);
        }
        SpriteEditor.framePanel.frameFragmentListModel.update();
        SpriteEditor.framePanel.frameEditComponent.repaint();
    }
}
