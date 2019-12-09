package cz.ismar.projects.IEdit.handlers;

import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.structure.FrameHolder;
import cz.ismar.projects.IEdit.structure.Sprite;
import cz.ismar.projects.IEdit.ui.FramePanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

/**
 * 
 * 【帧编辑区】“帧列表”里，鼠标释放处理
 * 
 * － 筛选出弹出菜单选项，然后弹出菜单
 * @author Lori
 *
 */
public class FrameInFramesListSelectionListener extends MouseAdapter
{

    public FrameInFramesListSelectionListener()
    {
    }

    public void mouseReleased(MouseEvent mouseevent)
    {
        if(mouseevent.isPopupTrigger() && SpriteEditor.sprite.frameHolder.size() != 0)
        {
            int i = SpriteEditor.framePanel.frameJList.locationToIndex(mouseevent.getPoint());
            if(i > -1)
            {
                SpriteEditor.sprite.frameHolder.setIndex(i);
                if(SpriteEditor.sprite.frameHolder.isFirst())
                {
                    SpriteEditor.framePanel.frameTopMenuItem.setEnabled(false);
                    SpriteEditor.framePanel.frameUpMenuItem.setEnabled(false);
                } else
                {
                    SpriteEditor.framePanel.frameTopMenuItem.setEnabled(true);
                    SpriteEditor.framePanel.frameUpMenuItem.setEnabled(true);
                }
                if(SpriteEditor.sprite.frameHolder.isLast())
                {
                    SpriteEditor.framePanel.frameDownMenuItem.setEnabled(false);
                    SpriteEditor.framePanel.frameBottomMenuItem.setEnabled(false);
                } else
                {
                    SpriteEditor.framePanel.frameDownMenuItem.setEnabled(true);
                    SpriteEditor.framePanel.frameBottomMenuItem.setEnabled(true);
                }
                SpriteEditor.framePanel.allFramePopup.show(mouseevent.getComponent(), mouseevent.getX(), mouseevent.getY());
            }
        }
    }
}
