/**
 * 2007-9-14 daben
 * - add
 * --> SpriteEditor.transformFrameFragmentMenu.setEnabled(true);
 */

package cz.ismar.projects.IEdit.handlers;

import cz.ismar.projects.IEdit.ClipBoard;
import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.structure.Frame;
import cz.ismar.projects.IEdit.structure.FrameFragmentMasks;
import cz.ismar.projects.IEdit.structure.FrameHolder;
import cz.ismar.projects.IEdit.structure.Sprite;
import cz.ismar.projects.IEdit.ui.FramePanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;


/**
 * 【帧切片列表】里，
 * 
 * 鼠标操作,
 * 
 * 筛选出弹出菜单哪些可以显示，然后显示“弹出菜单”
 * @author Lori
 *
 */
public class FrameFragmentsListMouseListener extends MouseAdapter
{

    public FrameFragmentsListMouseListener(FrameFragmentsListCommander framefragmentslistcommander)
    {
        commander = framefragmentslistcommander;
    }

    public void mouseReleased(MouseEvent mouseevent)
    {
        if(mouseevent.isPopupTrigger() && SpriteEditor.sprite.frameHolder.size() != 0 && SpriteEditor.sprite.frameHolder.getCurrentFrame().size() != 0)
        {
            int i = SpriteEditor.framePanel.frameFragmentJList.locationToIndex(mouseevent.getPoint());
            if(i > -1)
            {
                SpriteEditor.sprite.frameHolder.getCurrentFrame().setIndex(i);
                if(SpriteEditor.sprite.frameHolder.getCurrentFrame().isFirst())
                {
                    SpriteEditor.moveTopFrameMenuItem.setEnabled(false);
                    SpriteEditor.moveUpFromFrameMenuItem.setEnabled(false);
                } else
                {
                    SpriteEditor.moveTopFrameMenuItem.setEnabled(true);
                    SpriteEditor.moveUpFromFrameMenuItem.setEnabled(true);
                }
                if(SpriteEditor.sprite.frameHolder.getCurrentFrame().isLast())
                {
                    SpriteEditor.moveBottomFromFrameMenuItem.setEnabled(false);
                    SpriteEditor.moveDownFromFrameMenuItem.setEnabled(false);
                } else
                {
                    SpriteEditor.moveBottomFromFrameMenuItem.setEnabled(true);
                    SpriteEditor.moveDownFromFrameMenuItem.setEnabled(true);
                }
                SpriteEditor.showHideFrameMenuItem.setEnabled(SpriteEditor.frameFragmentMasks.isEnabled());
                SpriteEditor.pasteFragmentToFrameMenuItem.setEnabled(SpriteEditor.fragmentsClipBoard.getItems().size() > 0);
                //daben:add
                SpriteEditor.transformFrameFragmentMenu.setEnabled(true);
                //~daben
                SpriteEditor.frameFragmentPopup.show(mouseevent.getComponent(), mouseevent.getX(), mouseevent.getY());
            }
        }
    }

    public void mouseClicked(MouseEvent mouseevent)
    {
        if(mouseevent.getClickCount() == 2)
        {
            int frameFragmentIndex = SpriteEditor.framePanel.frameFragmentJList.locationToIndex(mouseevent.getPoint());
            if(frameFragmentIndex > -1)
            {
                SpriteEditor.showHideFrameMenuItem.setEnabled(SpriteEditor.frameFragmentMasks.isEnabled());
                commander.doMask();
            }
        }
    }

    FrameFragmentsListCommander commander;
}
