package cz.ismar.projects.IEdit.handlers;

import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.structure.FrameHolder;
import cz.ismar.projects.IEdit.structure.Sprite;
import cz.ismar.projects.IEdit.ui.FramePanel;
import cz.ismar.projects.IEdit.ui.FrameEditComponent;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * 【帧编辑区】的 “帧列表”
 * 里 选择事件处理
 * 
 * - 更新帧切片列表
 * - 更新“当前帧”信息面板
 * @author Lori
 *
 */
public class FrameListSelectionListener
    implements ListSelectionListener
{

    public FrameListSelectionListener()
    {
    }

    public void valueChanged(ListSelectionEvent event)
    {
    	System.out.println("index (" + event.getFirstIndex() + " : " + event.getLastIndex() + ")");
    	
        if(SpriteEditor.sprite.frameHolder.size() > 0 
        		&& SpriteEditor.framePanel.frameJList.getSelectedIndex() >= 0 
        		&& SpriteEditor.framePanel.frameJList.getSelectedIndex() < SpriteEditor.sprite.frameHolder.size())
        {
            SpriteEditor.sprite.frameHolder.setIndex(SpriteEditor.framePanel.frameJList.getSelectedIndex());
            SpriteEditor.framePanel.initFrameFragments();
            SpriteEditor.framePanel.frameEditComponent.repaint();
        }
    }
}
