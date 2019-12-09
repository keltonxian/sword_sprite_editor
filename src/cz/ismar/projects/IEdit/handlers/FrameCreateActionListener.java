/**
 * 2007-9-18 daben
 * - modify
 * --> actionPerformed(ActionEvent actionevent) #see daben:modify
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
 * 在【帧编辑区】的帧列表里，弹出菜单“新建”以及 按钮 “新建帧”
 * 
 * 操作
 * - 建立一个新帧
 * @author Lori
 *
 */
public class FrameCreateActionListener
    implements ActionListener
{

    public FrameCreateActionListener()
    {
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        Frame frame = new Frame(SpriteEditor.sprite);
        String s = (String)JOptionPane.showInputDialog(SpriteEditor.mainFrame, "新帧名字:", SpriteEditor.TITLE, -1, null, null, frame.getName());
        //daben:modify 按撤消按扭不应该新建帧
/*        if(s != null && s.length() > 0)
        	frame.setName(s);
        SpriteEditor.mainFramingPanel.initFrames();
        SpriteEditor.mainGesturePanel.initFrames();
        SpriteEditor.mainGesturePanel.initGestures();
        SpriteEditor.project.frameHolder.add(frame);
        SpriteEditor.mainFramingPanel.frameListModel.updateAdd();
        SpriteEditor.mainFramingPanel.frameFragmentListModel.updateAdd();
        SpriteEditor.mainFramingPanel.frameJList.getSelectionModel().setSelectionInterval(SpriteEditor.mainFramingPanel.frameListModel.getSize() - 1, SpriteEditor.mainFramingPanel.frameListModel.getSize() - 1);
*/        
        //modify:
        if(s != null && s.length() > 0)
        {
            frame.setName(s);
            SpriteEditor.framePanel.initFrames();
            SpriteEditor.animationPanel.initFrames();
            SpriteEditor.animationPanel.initGestures();
            SpriteEditor.sprite.frameHolder.add(frame);
            SpriteEditor.framePanel.frameListModel.updateAdd();
            SpriteEditor.framePanel.frameFragmentListModel.updateAdd();
            SpriteEditor.framePanel.frameJList.getSelectionModel().setSelectionInterval(SpriteEditor.framePanel.frameListModel.getSize() - 1, SpriteEditor.framePanel.frameListModel.getSize() - 1);
            // daben:add
            if (SpriteEditor.animationsSaved)
                SpriteEditor.animationsSaved = false;
            // ~daben
        }
        //~daben
    }
}
