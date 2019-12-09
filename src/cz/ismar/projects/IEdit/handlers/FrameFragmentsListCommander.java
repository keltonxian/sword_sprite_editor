/**
 * 2007-9-12 daben
 * - add function
 * --> doTransform(int type)
 * 
 * 2007-9-14 daben
 * - add
 * --> SpriteEditor.mainFramingPanel.frameListModel.update();
 */

package cz.ismar.projects.IEdit.handlers;

import java.awt.Rectangle;
import java.util.List;

import javax.swing.ListSelectionModel;

import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.structure.Frame;
import cz.ismar.projects.IEdit.structure.FrameFragment;

/**
 * 【帧切片列表】操作功能类
 * 
 * @author Lori
 * 
 */
public class FrameFragmentsListCommander
{

	public FrameFragmentsListCommander()
	{
	}

	public boolean doPaste()
	{
		boolean flag = false;
		List list = SpriteEditor.fragmentsClipBoard.getItems();
		Frame frame = SpriteEditor.sprite.frameHolder.getCurrentFrame();
		if(list.size() > 0)
		{
			// daben:add
			frame.saveUndo();
			// ~daben
			int i = frame.getIndex();
			if(i < 0)
				i = 0;
			for(int j = 0; j < list.size(); j++)
			{
				FrameFragment framefragment = (FrameFragment) list.get(j);
				frame.add(i + j, new FrameFragment(framefragment));
				// daben:add
				if(SpriteEditor.animationsSaved)
					SpriteEditor.animationsSaved = false;
				// ~daben
			}

			update();
			SpriteEditor.framePanel.frameFragmentJList.getSelectionModel().setSelectionInterval(i, i);
			flag = true;
		}
		if(SpriteEditor.collisionClipboard != null)
		{
			frame.setColisionArea((Rectangle) SpriteEditor.collisionClipboard.clone());
			if(!flag)
				update();
			flag = true;
		}
		return flag;
	}

	public boolean doCopy()
	{
		SpriteEditor.fragmentsClipBoard.insertItems(SpriteEditor.framePanel.frameFragmentJList);
		Frame frame = SpriteEditor.sprite.frameHolder.getCurrentFrame();
		if(frame.isColisionAreaSelected())
		{
			SpriteEditor.collisionClipboard = frame.getColisionArea();
			return true;
		}else
			SpriteEditor.collisionClipboard = null;
		return false;
	}

	public boolean doDelete()
	{
		Frame frame = SpriteEditor.sprite.frameHolder.getCurrentFrame();
		if(frame.remove())
		{
			// daben:add
			if(SpriteEditor.animationsSaved)
				SpriteEditor.animationsSaved = false;
			// ~daben
			int i = frame.getIndex();
			update();
			SpriteEditor.framePanel.frameFragmentJList.getSelectionModel().setSelectionInterval(i, i);
			return true;
		}else
		{
			return false;
		}
	}

	public boolean doMoveBottom()
	{
		Frame frame = SpriteEditor.sprite.frameHolder.getCurrentFrame();
		if(frame.moveBottom())
		{
			// daben:add
			if(SpriteEditor.animationsSaved)
				SpriteEditor.animationsSaved = false;
			// ~daben
			int i = frame.getFrameFragments().size() - 1;
			update();
			SpriteEditor.framePanel.frameFragmentJList.getSelectionModel().setSelectionInterval(i, i);
			return true;
		}else
		{
			return false;
		}
	}

	public boolean doMoveDown()
	{
		Frame frame = SpriteEditor.sprite.frameHolder.getCurrentFrame();
		if(frame.moveDown())
		{
			// daben:add
			if(SpriteEditor.animationsSaved)
				SpriteEditor.animationsSaved = false;
			// ~daben
			int i = frame.getIndex();
			update();
			SpriteEditor.framePanel.frameFragmentJList.getSelectionModel().setSelectionInterval(i, i);
			return true;
		}else
		{
			return false;
		}
	}

	public boolean doMoveUp()
	{
		Frame frame = SpriteEditor.sprite.frameHolder.getCurrentFrame();
		if(frame.moveUp())
		{
			// daben:add
			if(SpriteEditor.animationsSaved)
				SpriteEditor.animationsSaved = false;
			// ~daben
			int i = frame.getIndex();
			update();
			SpriteEditor.framePanel.frameFragmentJList.getSelectionModel().setSelectionInterval(i, i);
			return true;
		}else
		{
			return false;
		}
	}

	public boolean doMoveTop()
	{
		Frame frame = SpriteEditor.sprite.frameHolder.getCurrentFrame();
		if(frame.moveTop())
		{
			// daben:add
			if(SpriteEditor.animationsSaved)
				SpriteEditor.animationsSaved = false;
			// ~daben
			update();
			SpriteEditor.framePanel.frameFragmentJList.getSelectionModel().setSelectionInterval(0, 0);
			return true;
		}else
		{
			return false;
		}
	}

	/**
	 * 隐藏帧
	 * @return
	 */
	public boolean doMask()
	{
		if(!SpriteEditor.frameFragmentMasks.isEnabled())
		{
			return false;
		}
		
		ListSelectionModel listselectionmodel = SpriteEditor.framePanel.frameFragmentJList.getSelectionModel();
		if(!listselectionmodel.isSelectionEmpty())
		{
			Frame frame = SpriteEditor.sprite.frameHolder.getCurrentFrame();
			if(frame != null)
			{
				int i = SpriteEditor.framePanel.frameFragmentJList.getModel().getSize();
				for(int j = 0; j < i; j++)
				{
					if(listselectionmodel.isSelectedIndex(j))
					{
						FrameFragment framefragment = frame.get(j);
						SpriteEditor.frameFragmentMasks.changeHidenState(framefragment);
					}
				}

				SpriteEditor.animationsSaved = false;
				frame.updateImage();
				SpriteEditor.framePanel.repaint();
				return true;
			}
		}
		return false;
	}

	// daben:add
	public void doTransform(int type)
	{
		Frame frame = SpriteEditor.sprite.frameHolder.getCurrentFrame();
		if(frame == null)
			return;
		frame.saveUndo();
		FrameFragment frameFragment = frame.get();
		frameFragment.setTransfromType(type);
		frameFragment.updateImage();
		frameFragment.updateName();
		int i = frame.getIndex();
		update();
		SpriteEditor.framePanel.frameFragmentJList.getSelectionModel().setSelectionInterval(i, i);
		SpriteEditor.animationsSaved = false;
	}

	// ~daben

	public void update()
	{
		SpriteEditor.sprite.frameHolder.getCurrentFrame().updateImage();
		SpriteEditor.framePanel.frameFragmentListModel.update();
		// daben:add
		SpriteEditor.framePanel.frameListModel.update();
		// ~daben
		SpriteEditor.framePanel.repaint();
	}
}
