/**
 * 2007-9-6 daben 
 * - add
 * --> SpriteEditor.centerScrollPane(SpriteEditor.mainFramingPanel.wysiwygFrameScrollPanel);
 */

package cz.ismar.projects.IEdit.handlers;

import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.ui.*;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;

public class ZoomMenuActionListener
    implements ActionListener
{

    public ZoomMenuActionListener()
    {
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        SpriteEditor.scale = Integer.parseInt(actionevent.getActionCommand());
        SpriteEditor.zoomMenu.setText("放大: " + SpriteEditor.scale + "x");
        SpriteEditor.fragmentPanel.fragmentEditorComponent.repaint();
        SpriteEditor.fragmentPanel.fragmentEditorComponent.getParent().invalidate();
        SpriteEditor.framePanel.frameEditComponent.updatePrefferedSize();
        //daben:add 缩放时让图象居中
        SpriteEditor.centerScrollPane(SpriteEditor.framePanel.frameEditScrollPanel);
        //~daben
        SpriteEditor.framePanel.repaint();
        SpriteEditor.framePanel.getParent().invalidate();
        SpriteEditor.animationPanel.repaint();
        SpriteEditor.avatarPanel.repaint();
    }
}
