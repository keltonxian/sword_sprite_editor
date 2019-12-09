package cz.ismar.projects.IEdit.handlers;

import cz.ismar.projects.IEdit.IconLabelListModel;
import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.structure.*;
import cz.ismar.projects.IEdit.ui.FramePanel;
import cz.ismar.projects.IEdit.ui.AnimationPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import javax.swing.*;

/**
 * "动画帧列表" 里 弹出菜单选择操作
 * 
 * @author Lori
 * 
 */
public class GesturesPopUpMenuActionListener implements ActionListener
{

	public GesturesPopUpMenuActionListener()
	{
	}

	public void actionPerformed(ActionEvent actionevent)
	{
		if(actionevent.getActionCommand().equalsIgnoreCase("置顶"))
		{
			if(SpriteEditor.sprite.getCurrentAnimation().moveTop())
				SpriteEditor.animationPanel.gesturesJList.getSelectionModel().setSelectionInterval(SpriteEditor.sprite.getCurrentAnimation().getIndex(), SpriteEditor.sprite.getCurrentAnimation().getIndex());
		}else if(actionevent.getActionCommand().equalsIgnoreCase("上移"))
		{
			if(SpriteEditor.sprite.getCurrentAnimation().moveUp())
				SpriteEditor.animationPanel.gesturesJList.getSelectionModel().setSelectionInterval(SpriteEditor.sprite.getCurrentAnimation().getIndex(), SpriteEditor.sprite.getCurrentAnimation().getIndex());
		}else if(actionevent.getActionCommand().equalsIgnoreCase("下移"))
		{
			if(SpriteEditor.sprite.getCurrentAnimation().moveDown())
				SpriteEditor.animationPanel.gesturesJList.getSelectionModel().setSelectionInterval(SpriteEditor.sprite.getCurrentAnimation().getIndex(), SpriteEditor.sprite.getCurrentAnimation().getIndex());
		}else if(actionevent.getActionCommand().equalsIgnoreCase("置底"))
		{
			if(SpriteEditor.sprite.getCurrentAnimation().moveBottom())
				SpriteEditor.animationPanel.gesturesJList.getSelectionModel().setSelectionInterval(SpriteEditor.sprite.getCurrentAnimation().getIndex(), SpriteEditor.sprite.getCurrentAnimation().getIndex());
		}else if(actionevent.getActionCommand().equalsIgnoreCase("删除"))
		{
			System.out.println("GesturesPopUpMenuActionListener.actionPerformed Delete");
			if(SpriteEditor.sprite.getCurrentAnimation().remove())
			{
				SpriteEditor.animationPanel.initGestures();
				SpriteEditor.animationPanel.gesturesJList.getSelectionModel().setSelectionInterval(SpriteEditor.sprite.getCurrentAnimation().getIndex(), SpriteEditor.sprite.getCurrentAnimation().getIndex());
			}
		}else if(actionevent.getActionCommand().equalsIgnoreCase("重命名"))
		{
			String s = (String) JOptionPane.showInputDialog(SpriteEditor.mainFrame, "命名为:", SpriteEditor.TITLE, -1, null, null, SpriteEditor.sprite.getCurrentAnimation().get().getName());
			if(s != null && s.length() > 0)
				SpriteEditor.sprite.getCurrentAnimation().get().setName(s);
			
			SpriteEditor.animationPanel.gesturesListModel.updateChange();
			SpriteEditor.animationPanel.gesturesJList.getSelectionModel().setSelectionInterval(SpriteEditor.sprite.getCurrentAnimation().getIndex(), SpriteEditor.sprite.getCurrentAnimation().getIndex());
		}
		SpriteEditor.framePanel.frameFragmentListModel.update();
		SpriteEditor.framePanel.repaint();
	}

}
