package cz.ismar.projects.IEdit.handlers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.structure.Animation;
import cz.ismar.projects.IEdit.structure.Frame;
import cz.ismar.projects.IEdit.structure.FrameHolder;

public class DeleteUnuseFrameHandler implements ActionListener
{

	@Override
	public void actionPerformed(ActionEvent e)
	{
		List frames = SpriteEditor.sprite.frameHolder.getFrames();
		if(frames == null || frames.size() == 0)
		{
			return;
		}
		
		ArrayList<Frame> removeList = new ArrayList<Frame>();
		
		Frame frame = null;
		Animation[] ani = null;
		for(int i = 0 ; i < frames.size(); i ++)
		{
			frame = (Frame) frames.get(i);
			ani = SpriteEditor.sprite.getAnimationsContainingFrame(frame);
			if(ani == null || ani.length == 0)
			{
				removeList.add(frame);
				System.out.println("ready remove -> [" + i + "]=" + frame);
			}
		}
		
		if(removeList.size() == 0)
		{
			JOptionPane.showMessageDialog(SpriteEditor.mainFrame, "没有无用的帧");
			return;
		}
		
		String str = "删除了:\n";
		 
		for(int i = 0 ; i < removeList.size() ; i ++)
		{
			frame = removeList.get(i);
			str += frame + "\n";
			SpriteEditor.sprite.frameHolder.remove(frame);
			SpriteEditor.framePanel.frameListModel.updateDelete();
			SpriteEditor.framePanel.initFrameFragments();
			SpriteEditor.framePanel.initFrames();
			SpriteEditor.animationPanel.initFrames();
		}
		str += "共" + removeList.size() + "帧";
		JOptionPane.showMessageDialog(SpriteEditor.mainFrame, str);
	}

}
