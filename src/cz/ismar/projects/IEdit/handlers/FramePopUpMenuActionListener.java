package cz.ismar.projects.IEdit.handlers;

import cz.ismar.projects.IEdit.IconLabelListModel;
import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.structure.*;
import cz.ismar.projects.IEdit.ui.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Iterator;

import javax.swing.*;

/**
 * 【帧编辑区】“帧列表”里 弹出菜单操作
 * @author Lori
 *
 */
public class FramePopUpMenuActionListener
    implements ActionListener
{

    public FramePopUpMenuActionListener()
    {
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        if(!actionevent.getActionCommand().equalsIgnoreCase("新建"))
            if(actionevent.getActionCommand().equalsIgnoreCase("置顶"))
            {
                if(SpriteEditor.sprite.frameHolder.moveTop())
                {
                    SpriteEditor.framePanel.frameJList.getSelectionModel().setSelectionInterval(SpriteEditor.sprite.frameHolder.getIndex(), SpriteEditor.sprite.frameHolder.getIndex());
                    SpriteEditor.animationPanel.initFrames();
                }
            } else
            if(actionevent.getActionCommand().equalsIgnoreCase("上移"))
            {
                if(SpriteEditor.sprite.frameHolder.moveUp())
                {
                    SpriteEditor.framePanel.frameJList.getSelectionModel().setSelectionInterval(SpriteEditor.sprite.frameHolder.getIndex(), SpriteEditor.sprite.frameHolder.getIndex());
                    SpriteEditor.animationPanel.initFrames();
                }
            } else
            if(actionevent.getActionCommand().equalsIgnoreCase("下移"))
            {
                if(SpriteEditor.sprite.frameHolder.moveDown())
                {
                    SpriteEditor.framePanel.frameJList.getSelectionModel().setSelectionInterval(SpriteEditor.sprite.frameHolder.getIndex(), SpriteEditor.sprite.frameHolder.getIndex());
                    SpriteEditor.animationPanel.initFrames();
                }
            } else
            if(actionevent.getActionCommand().equalsIgnoreCase("置底"))
            {
                if(SpriteEditor.sprite.frameHolder.moveBottom())
                {
                    SpriteEditor.framePanel.frameJList.getSelectionModel().setSelectionInterval(SpriteEditor.sprite.frameHolder.getIndex(), SpriteEditor.sprite.frameHolder.getIndex());
                    SpriteEditor.animationPanel.initFrames();
                }
            } else
            if(actionevent.getActionCommand().equalsIgnoreCase("复制"))
            {
                Frame frame = SpriteEditor.sprite.frameHolder.getCurrentFrame();
                Frame frame2 = frame.copy();
                //daben:modify
/*                String s1 = (String)JOptionPane.showInputDialog(SpriteEditor.mainFrame, "New frame name:", "iEdit V.2.0.29", -1, null, null, SpriteEditor.project.frameHolder.get().getName() + "_copy");
                if(s1 != null && s1.length() > 0)
                    frame2.setName(s1);
                SpriteEditor.frameFragmentMasks.copyMasksForFrame(frame, frame2);
                SpriteEditor.project.frameHolder.insert(frame2);
                SpriteEditor.mainFramingPanel.initFrames();
                SpriteEditor.mainGesturePanel.initFrames();
                int i = SpriteEditor.project.frameHolder.getIndex();
                SpriteEditor.mainFramingPanel.frameJList.getSelectionModel().setSelectionInterval(i, i);*/
                //modify:
                String s1 = (String)JOptionPane.showInputDialog(SpriteEditor.mainFrame, "新帧名字:", SpriteEditor.TITLE, -1, null, null, SpriteEditor.sprite.frameHolder.getCurrentFrame().getName());
                if(s1 != null && s1.length() > 0)
                {
                	frame2.setName(s1);
                	SpriteEditor.frameFragmentMasks.copyMasksForFrame(frame, frame2);
                	SpriteEditor.sprite.frameHolder.insert(frame2);
                	SpriteEditor.framePanel.initFrames();
                	SpriteEditor.animationPanel.initFrames();
                	int i = SpriteEditor.sprite.frameHolder.getIndex();
                	SpriteEditor.framePanel.frameJList.getSelectionModel().setSelectionInterval(i, i);
                	//更新frame缩略图显示
                	SpriteEditor.sprite.frameHolder.getCurrentFrame().updateImage();
                	SpriteEditor.framePanel.frameListModel.update();
                	SpriteEditor.animationPanel.frameListModel.update();
                }
                //~daben
            } else
            if(actionevent.getActionCommand().equalsIgnoreCase("删除"))
            {
//                System.out.println("Editor.actionPerformed");
                cz.ismar.projects.IEdit.structure.Animation aanimation[] = SpriteEditor.sprite.getAnimationsContainingFrame(SpriteEditor.sprite.frameHolder.getCurrentFrame());
                if(aanimation.length > 0)
                {
                    if(0 == JOptionPane.showConfirmDialog(SpriteEditor.mainFrame, "帧已经使用\n" + Arrays.asList(aanimation) + ".\n确定要删除吗?", SpriteEditor.TITLE, 2, 2))
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
            if(actionevent.getActionCommand().equalsIgnoreCase("重命名"))
            {
                System.out.println("Editor.actionPerformed..............................");
                String s = (String)JOptionPane.showInputDialog(SpriteEditor.mainFrame, "新帧名字:", SpriteEditor.TITLE, -1, null, null, SpriteEditor.sprite.frameHolder.getCurrentFrame().getName());
                if(s != null && s.length() > 0)
                    SpriteEditor.sprite.frameHolder.getCurrentFrame().setName(s);
                SpriteEditor.animationPanel.initFrames();
                SpriteEditor.framePanel.frameListModel.updateChange();
            } else
            if(actionevent.getActionCommand().equalsIgnoreCase("设为帮助帧"))
            {
                Frame frame1 = SpriteEditor.sprite.frameHolder.getCurrentFrame();
                SpriteEditor.framePanel.setHelpFrame(frame1);
            }
        SpriteEditor.framePanel.frameFragmentListModel.update();
        SpriteEditor.framePanel.frameEditComponent.repaint();
        // daben:add
        if (SpriteEditor.animationsSaved)
            SpriteEditor.animationsSaved = false;
        // ~daben
    }
}
