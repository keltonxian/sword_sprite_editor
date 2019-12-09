package cz.ismar.projects.IEdit.handlers;

import java.awt.Event;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import cz.ismar.projects.IEdit.SpriteEditor;

/**
 * 【帧切片列表】里，按键操作 功能等同于FrameFragmentPopUpMenuActionListener
 * 
 * @author Lori
 * @see FrameFragmentPopUpMenuActionListener
 * 
 */
public class FrameFragmentsListKeyListener extends KeyAdapter
{

	public FrameFragmentsListKeyListener(FrameFragmentsListCommander framefragmentslistcommander)
	{
		commander = framefragmentslistcommander;
	}

	public void keyPressed(KeyEvent keyevent)
	{
		if(keyevent.getModifiers() == InputEvent.CTRL_MASK)
		{
			if(keyevent.getKeyCode() == KeyEvent.VK_Z)
			{
				SpriteEditor.sprite.frameHolder.getCurrentFrame().undo();
			}
		}
	}

	public void keyReleased(KeyEvent keyevent)
	{
		int i = SpriteEditor.framePanel.frameFragmentJList.getSelectedIndex();
		if(i > -1)
			if(keyevent.getKeyCode() == KeyEvent.VK_HOME && keyevent.getModifiers() == Event.ALT_MASK)
				commander.doMoveTop();
			else if(keyevent.getKeyCode() == KeyEvent.VK_UP && keyevent.getModifiers() == Event.ALT_MASK)
				commander.doMoveUp();
			else if(keyevent.getKeyCode() == KeyEvent.VK_DOWN && keyevent.getModifiers() == Event.ALT_MASK)
				commander.doMoveDown();
			else if(keyevent.getKeyCode() == KeyEvent.VK_END && keyevent.getModifiers() == Event.ALT_MASK)
				commander.doMoveBottom();
			else if(keyevent.getKeyCode() == KeyEvent.VK_C && keyevent.getModifiers() == Event.CTRL_MASK)
				commander.doCopy();
			else if(keyevent.getKeyCode() == KeyEvent.VK_V && keyevent.getModifiers() == Event.CTRL_MASK)
				commander.doPaste();
			else if(keyevent.getKeyCode() == KeyEvent.VK_ENTER)
				commander.doMask();
			else if(keyevent.getKeyCode() == KeyEvent.VK_DELETE)
				commander.doDelete();
	}

	FrameFragmentsListCommander commander;
}
