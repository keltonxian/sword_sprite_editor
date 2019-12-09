package cz.ismar.projects.IEdit.handlers;

import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.ui.FragmentPanel;
import cz.ismar.projects.IEdit.ui.FragmentEditComponent;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * 【切片区】里“切片列表”里的选择事件，
 * 
 * －更新切片图的切片框信息
 *  － 矩形框 框住 当前选择的切片
 * @author Lori
 *
 */
public class FragmentListSelectionListener
    implements ListSelectionListener
{

    public FragmentListSelectionListener()
    {
    }

    /**
     * 在切片列表, 选中的切片, 在编辑器框里会高亮
     */
    public void valueChanged(ListSelectionEvent listselectionevent)
    {
        if(listselectionevent.getValueIsAdjusting())
        {
            return;
        }
        
        ListSelectionModel listselectionmodel = (ListSelectionModel)listselectionevent.getSource();
        if(!listselectionmodel.isSelectionEmpty())
        {
            SpriteEditor.fragmentPanel.fragmentEditorComponent.selectRect(listselectionmodel.getLeadSelectionIndex());
        }
        else
        {
            SpriteEditor.fragmentPanel.fragmentEditorComponent.selectRect(-1);
        }
    }
}
