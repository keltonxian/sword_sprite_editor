package cz.ismar.projects.IEdit.handlers;

import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.structure.*;
import cz.ismar.projects.IEdit.ui.DetailComponent;
import cz.ismar.projects.IEdit.ui.FramePanel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JList;

/**
 * 在“全部切片列表”里，鼠标移动操作事件
 * 
 * - 会更新“切片细节”里的预览情况
 * @author Lori
 *
 */
public class FragmentListMouseMotionListener extends MouseMotionAdapter
{

    public FragmentListMouseMotionListener()
    {
    }

    public void mouseMoved(MouseEvent mouseevent)
    {
    	// 当前鼠标停在list的项哪个上面
        int index = SpriteEditor.framePanel.allFragmentList.locationToIndex(mouseevent.getPoint());
        
        // 在<切片细节>上画出切片
        if(SpriteEditor.sprite.fragmentHoldersList.getAllFragments().get(index) != null)
        {
            java.awt.image.BufferedImage bufferedimage = SpriteEditor.sprite.fragmentHoldersList.getAllFragments().get(index).getImage();
            SpriteEditor.framePanel.fragmentDetailPanel.setImg(bufferedimage);
        }
        SpriteEditor.framePanel.fragmentDetailPanel.repaint();
    }
}
