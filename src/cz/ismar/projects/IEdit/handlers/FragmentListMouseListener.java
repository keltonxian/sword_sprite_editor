/**
 * 2007-10-31 daben
 * - add mouseClicked()
 *              add a frag to frame when double clicked on the frag
 */

package cz.ismar.projects.IEdit.handlers;

import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.structure.*;
import cz.ismar.projects.IEdit.ui.FramePanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

/**
 * 在【帧编辑区】的“全部切片列表”里的 鼠标事件
 * 
 * 1） 鼠标释放后，弹出菜单“添加到帧 Enter” 2) 鼠标双击直接添加
 * 
 * @author Lori
 * 
 */
public class FragmentListMouseListener extends MouseAdapter
{

	public FragmentListMouseListener()
	{
	}

	/**
	 * 弹出右键菜单
	 */
	public void mouseReleased(MouseEvent mouseevent)
	{
		if(mouseevent.isPopupTrigger() && SpriteEditor.sprite.fragmentHoldersList.getAllFragments().size() != 0)
		{
			int index = SpriteEditor.framePanel.allFragmentList.locationToIndex(mouseevent.getPoint());
			if(index <= -1)
			{
				return;
			}
			
			SpriteEditor.framePanel.allFragmentList.setSelectedIndex(index);
			SpriteEditor.sprite.fragmentHoldersList.getAllFragments().setIndex(index);
			
			// 2009-11-30 弹出[隐藏]
			Fragment fragment = SpriteEditor.sprite.fragmentHoldersList.getAllFragments().get();
			SpriteEditor.framePanel.cb_allFragmentListIsVisible.setSelected(!fragment.isVisible());
			SpriteEditor.framePanel.allFragmentListPopupMenu.show(mouseevent.getComponent(), mouseevent.getX(), mouseevent.getY());
			
			// 右键菜单 -> "增加到帧"
//			SpriteEditor.allFrameFragmentPopup.show(mouseevent.getComponent(), mouseevent.getX(), mouseevent.getY());
		}
	}

	// daben:add
	public void mouseClicked(MouseEvent mouseevent)
	{
		// 双击的时候
		if(mouseevent.getClickCount() == 2)
		{
			int i = SpriteEditor.framePanel.allFragmentList.locationToIndex(mouseevent.getPoint());
			if(i > -1)
				SpriteEditor.sprite.fragmentHoldersList.getAllFragments().setIndex(i);
			if(SpriteEditor.sprite.fragmentHoldersList.getAllFragments().get() == null)
				return;

			if(SpriteEditor.sprite.frameHolder.getCurrentFrame() == null)
			{
				if(0 == JOptionPane.showConfirmDialog(SpriteEditor.mainFrame, "还没有创建帧，现在创建一个吗?", SpriteEditor.TITLE, 2, 3))
				{
					SpriteEditor.framePanel.frameCreateActionListener.actionPerformed(null);
					SpriteEditor.sprite.frameHolder.getCurrentFrame().add(0, new FrameFragment(SpriteEditor.sprite.fragmentHoldersList.getAllFragments().get()));
					SpriteEditor.framePanel.initFrameFragments();
				}
			}else
			{
				SpriteEditor.sprite.frameHolder.getCurrentFrame().saveUndo();

				// 增加到帧
				SpriteEditor.sprite.frameHolder.getCurrentFrame().add(0, new FrameFragment(SpriteEditor.sprite.fragmentHoldersList.getAllFragments().get()));
				SpriteEditor.framePanel.initFrameFragments();
			}
			
			SpriteEditor.sprite.frameHolder.getCurrentFrame().updateImage();
			SpriteEditor.framePanel.frameListModel.update();
			SpriteEditor.animationPanel.frameListModel.update();
			// daben:add
			if(SpriteEditor.animationsSaved)
				SpriteEditor.animationsSaved = false;
			// ~daben
		}
	}
	// ~daben
}
