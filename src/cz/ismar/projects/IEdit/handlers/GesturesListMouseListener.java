package cz.ismar.projects.IEdit.handlers;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JOptionPane;

import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.structure.Animation;
import cz.ismar.projects.IEdit.structure.Gesture;
import cz.ismar.projects.IEdit.structure.Sprite;

/**
 * “动画帧列表”里，
 * 
 * 鼠标释放事件处理
 * 
 * － 筛选出弹出菜单内容，弹出菜单
 * 
 * @author Lori
 * 
 */
public class GesturesListMouseListener extends MouseAdapter
{

	public GesturesListMouseListener()
	{
	}

	public void mouseClicked(MouseEvent mouseevent)
	{
		// 双击修改事件
		if(mouseevent.getClickCount() == 2)
		{
			String[] values = { "设置为判定帧", "清空判定帧", };
			String s = (String) JOptionPane.showInputDialog(SpriteEditor.mainFrame, "判定帧设置", "请选择", JOptionPane.PLAIN_MESSAGE, null, values, values[0]);

			// If a string was returned, say so.
			if((s != null) && (s.length() > 0))
			{
				Animation animation = SpriteEditor.sprite.getCurrentAnimation();
//				animation.setCheckFrameId((byte) -1);
				
//				List list = animation.getGestures();
				// 全部设置为false
				// for(int i=0;i<list.size();i++){
				// Gesture gusture = (Gesture)list.get(i);
				// gusture.setCheck(false);
				// }

				Gesture gusture = animation.get();
				if(s.equalsIgnoreCase("设置为判定帧"))
				{
//					if(!animation.checkFrames.contains(gusture))
//					{
						gusture.isCheck = true;
//						animation.checkFrames.add(gusture);
//					}
					// animation.setCheckFrameId((byte)animation.getIndex());
					// Gesture gusture = animation.get();
					// gusture.setCheck(true);
					// SpriteEditor.mainGesturePanel.gesturesJList.updateUI();
					// SpriteEditor.mainGesturePanel.animationsJList.updateUI();
				}else if(s.equalsIgnoreCase("清空判定帧"))
				{
//					if(animation.checkFrames.contains(gusture))
//					{
						gusture.isCheck = false;
//						animation.checkFrames.remove(gusture);
//					}
				}
				SpriteEditor.animationPanel.gesturesJList.updateUI();
				SpriteEditor.animationPanel.animationsJList.updateUI();
			}

		}
	}

	public void mouseReleased(MouseEvent mouseevent)
	{
		if(mouseevent.isPopupTrigger() && SpriteEditor.sprite.size() != 0 && SpriteEditor.sprite.getCurrentAnimation().size() != 0)
		{
			int i = SpriteEditor.animationPanel.gesturesJList.locationToIndex(mouseevent.getPoint());
			System.out.println("Editor.mouseReleased tmp:" + i);
			if(i > -1)
			{
				SpriteEditor.sprite.getCurrentAnimation().setIndex(i);
				if(SpriteEditor.sprite.getCurrentAnimation().isFirst())
				{
					SpriteEditor.animationPanel.gestureMoveTopMenuItem.setEnabled(false);
					SpriteEditor.animationPanel.gestureMoveUpMenuItem.setEnabled(false);
				}else
				{
					SpriteEditor.animationPanel.gestureMoveTopMenuItem.setEnabled(true);
					SpriteEditor.animationPanel.gestureMoveUpMenuItem.setEnabled(true);
				}
				if(SpriteEditor.sprite.getCurrentAnimation().isLast())
				{
					SpriteEditor.animationPanel.gestureMoveBottomMenuItem.setEnabled(false);
					SpriteEditor.animationPanel.gestureMoveDownMenuItem.setEnabled(false);
				}else
				{
					SpriteEditor.animationPanel.gestureMoveBottomMenuItem.setEnabled(true);
					SpriteEditor.animationPanel.gestureMoveDownMenuItem.setEnabled(true);
				}
				SpriteEditor.animationPanel.gesturePopup.show(mouseevent.getComponent(), mouseevent.getX(), mouseevent.getY());
			}
			if(SpriteEditor.sprite.getCurrentAnimation().size() == 0)
			{
				SpriteEditor.animationPanel.gestureMoveTopMenuItem.setEnabled(false);
				SpriteEditor.animationPanel.gestureMoveUpMenuItem.setEnabled(false);
				SpriteEditor.animationPanel.gestureMoveBottomMenuItem.setEnabled(false);
				SpriteEditor.animationPanel.gestureMoveDownMenuItem.setEnabled(false);
				SpriteEditor.animationPanel.gestureRenameMenuItem.setEnabled(false);
				SpriteEditor.animationPanel.gestureDeleteMenuItem.setEnabled(false);
			}
		}
	}
}
