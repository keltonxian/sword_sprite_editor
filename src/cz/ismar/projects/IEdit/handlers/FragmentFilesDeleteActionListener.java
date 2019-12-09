package cz.ismar.projects.IEdit.handlers;

import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.structure.FragmentHolder;
import cz.ismar.projects.IEdit.structure.FragmentHoldersList;
import cz.ismar.projects.IEdit.structure.Frame;
import cz.ismar.projects.IEdit.structure.Sprite;
import cz.ismar.projects.IEdit.ui.FramePanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JList;
import javax.swing.ListModel;

/**
 * 【帧编辑区】“移除”切片信息操作
 * @author Lori
 *
 */
public class FragmentFilesDeleteActionListener
    implements ActionListener
{

    public FragmentFilesDeleteActionListener(JList jlist)
    {
        list = jlist;
    }

    public void actionPerformed(ActionEvent event)
    {
        ListModel listmodel = list.getModel();
        ArrayList<FragmentHolder> arraylist = new ArrayList<FragmentHolder>();
        for(int i = 0; i < listmodel.getSize(); i++)
        {
            if(!list.isSelectedIndex(i))
                continue;
            
            FragmentHolder fragmentholder = (FragmentHolder)SpriteEditor.sprite.fragmentHoldersList.getElementAt(i);
            arraylist.add(fragmentholder);
            
            Frame aframe[] = SpriteEditor.sprite.removeFromAllFrames(fragmentholder);
            if(aframe == null || aframe.length <= 0)
                continue;
            for(int j = 0; j < aframe.length; j++)
            {
                Frame frame = aframe[j];
                frame.updateImage();
            }

        }

        SpriteEditor.sprite.fragmentHoldersList.remove(arraylist);
        SpriteEditor.framePanel.initFragments();
        SpriteEditor.framePanel.repaint();
    }

    JList list;
}
