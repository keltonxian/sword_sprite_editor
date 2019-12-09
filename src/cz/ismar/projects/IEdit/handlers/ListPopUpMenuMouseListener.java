package cz.ismar.projects.IEdit.handlers;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

/**
 * 
 * 【帧编辑区】
 * 切片文件列表里，
 * 
 * 弹出菜单处理
 * 
 * － 鼠标右键弹出“刷新，移除”菜单
 * @author Lori
 *
 */
public class ListPopUpMenuMouseListener extends MouseAdapter
{

    public ListPopUpMenuMouseListener(JList jlist, JPopupMenu jpopupmenu)
    {
        list = jlist;
        menu = jpopupmenu;
    }

    public void mouseReleased(MouseEvent mouseevent)
    {
        if(mouseevent.isPopupTrigger() && list.getModel().getSize() > 0)
        {
            int index = list.locationToIndex(mouseevent.getPoint());
            list.setSelectedIndex(index);
            if(index > -1){
            	menu.show(mouseevent.getComponent(), mouseevent.getX(), mouseevent.getY());
            }
        }
    }

    private JList list;
    private JPopupMenu menu;
}
