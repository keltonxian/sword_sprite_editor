package cz.ismar.projects.IEdit.handlers;

import cz.ismar.projects.IEdit.*;
import cz.ismar.projects.IEdit.structure.*;
import cz.ismar.projects.IEdit.ui.*;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

/**
 * 移动到SpriteEditor.createFragment()处理
 * @author Lori
 * @deprecated
 *
 */
public class FragmentCreateButtonActionListener
    implements ActionListener
{

    public FragmentCreateButtonActionListener()
    {
    }

    /**
     * 生成新的切片
     */
    public void actionPerformed(ActionEvent actionevent)
    {
        Fragment fragment = new Fragment(SpriteEditor.sprite.fragmentHolder, 
        		(int)SpriteEditor.fragmentPanel.fragmentEditorComponent.newFragmentRectangle.getX(), 
        		(int)SpriteEditor.fragmentPanel.fragmentEditorComponent.newFragmentRectangle.getY(), 
        		(int)SpriteEditor.fragmentPanel.fragmentEditorComponent.newFragmentRectangle.getWidth(), 
        		(int)SpriteEditor.fragmentPanel.fragmentEditorComponent.newFragmentRectangle.getHeight(), 
        		"Name" + SpriteEditor.sprite.fragmentHolder.getNextFragmentId());
        
        fragment.updateImage();
        SpriteEditor.sprite.fragmentHolder.add(fragment);
        SpriteEditor.fragmentPanel.fragmentTableModel.fireTableRowsInserted(SpriteEditor.framePanel.frameFragmentListModel.size(), SpriteEditor.framePanel.frameFragmentListModel.size());
        SpriteEditor.fragmentPanel.fragmentTable.getSelectionModel().setSelectionInterval(SpriteEditor.sprite.fragmentHolder.size() - 1, SpriteEditor.sprite.fragmentHolder.size() - 1);
        SpriteEditor.framePanel.allFragmentListModel.update();
    }
}
