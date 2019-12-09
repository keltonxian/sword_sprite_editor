package cz.ismar.projects.IEdit.handlers;

import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.TableModel;
import cz.ismar.projects.IEdit.structure.*;
import cz.ismar.projects.IEdit.ui.FragmentPanel;
import cz.ismar.projects.IEdit.ui.FramePanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 移动到SpriteEditor.deleteFragment()处理
 * @author Lori
 * @deprecated
 *
 */
public class FragmentDeleteActionListener
    implements ActionListener
{

    public FragmentDeleteActionListener()
    {
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        cz.ismar.projects.IEdit.structure.Fragment fragment = SpriteEditor.sprite.fragmentHolder.remove();
        Frame aframe[] = SpriteEditor.sprite.removeFromAllFrames(fragment,true);
        if(aframe != null && aframe.length > 0)
        {
            for(int i = 0; i < aframe.length; i++)
            {
                Frame frame = aframe[i];
                frame.updateImage();
            }

        }
        SpriteEditor.fragmentPanel.fragmentTableModel.fireTableRowsDeleted(SpriteEditor.sprite.fragmentHolder.getIndex(), SpriteEditor.sprite.fragmentHolder.getIndex());
        SpriteEditor.framePanel.repaint();
    }
}
