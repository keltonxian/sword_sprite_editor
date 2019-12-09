/**
 * 2007-10-31 daben
 * - add in frameJList.addMouseListener()
 *             add a frame to animation when double clicked on the frame     
 */

package cz.ismar.projects.IEdit.handlers;

import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.structure.Gesture;
import cz.ismar.projects.IEdit.ui.AnimationPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

/**
 * 【动画编辑区】“帧列表”鼠标操作
 * @author Lori
 *
 */
public class FrameInGestureListMouseListener extends MouseAdapter
{

    public FrameInGestureListMouseListener()
    {
    }

    public void mousePressed(MouseEvent mouseevent)
    {
        if(mouseevent.isPopupTrigger())
        {
            AnimationPanel _tmp = SpriteEditor.animationPanel;
            AnimationPanel.framePopup.show(mouseevent.getComponent(), mouseevent.getX(), mouseevent.getY());
        }

        // daben:add
        if (mouseevent.getClickCount() == 2)
        {
            int i = SpriteEditor.animationPanel.frameJList.locationToIndex(mouseevent.getPoint());
            if(i > -1)
                SpriteEditor.sprite.frameHolder.setIndex(i);

            SpriteEditor.animationPanel.frameInGesturePopUpMenuActionListener.actionPerformed(null);
        }
        // ~daben
    }

    public void mouseReleased(MouseEvent mouseevent)
    {
        if(mouseevent.isPopupTrigger())
        {
            AnimationPanel _tmp = SpriteEditor.animationPanel;
            AnimationPanel.framePopup.show(mouseevent.getComponent(), mouseevent.getX(), mouseevent.getY());
        }
    }
}
