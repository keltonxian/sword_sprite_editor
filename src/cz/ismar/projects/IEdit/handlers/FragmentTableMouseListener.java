package cz.ismar.projects.IEdit.handlers;

import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.structure.FragmentHolder;
import cz.ismar.projects.IEdit.structure.Sprite;
import cz.ismar.projects.IEdit.ui.FragmentPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.PrintStream;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

/**
 * 在【切片区】的 “切片列表”里，弹出菜单操作
 * 
 * @author Lori
 * 
 */
public class FragmentTableMouseListener extends MouseAdapter
{

	public FragmentTableMouseListener()
	{
	}

	public void mousePressed(MouseEvent mouseevent)
	{
		// 基本没用到，该逻辑已经移动到mouseReleased 处理
		if(mouseevent.isPopupTrigger())
		{
			int i = SpriteEditor.fragmentPanel.fragmentTable.rowAtPoint(mouseevent.getPoint());
			if(i > -1)
			{
				SpriteEditor.sprite.fragmentHolder.setIndex(i);
				SpriteEditor.fragmentPanel.fragmentTablePopupMenu.show(mouseevent.getComponent(), mouseevent.getX(), mouseevent.getY());
			}
		}
		// 编辑名字
		if(mouseevent.getClickCount() == 2)
		{
			SpriteEditor.fragmentsSaved = false;
		}
	}

	public void mouseReleased(MouseEvent mouseevent)
	{
		// 鼠标弹出菜单触发器
		if(mouseevent.isPopupTrigger())
		{
			int i = SpriteEditor.fragmentPanel.fragmentTable.rowAtPoint(mouseevent.getPoint());
			if(i > -1)
			{
				System.out.println("FragmentTableMouseListener.mouseReleased rowAt:" + i);

				SpriteEditor.sprite.fragmentHolder.setIndex(i);
				SpriteEditor.fragmentPanel.fragmentTablePopupMenu.show(mouseevent.getComponent(), mouseevent.getX(), mouseevent.getY());
			}
		}
	}
}
