/**
 * 2007-9-12 daben
 * - modify
 * - ImageIcon imageicon3 = new ImageIcon(framefragment.getFragment().getImage());
 * --->    ImageIcon imageicon3 = new ImageIcon(framefragment.getImage());
 * ---> for the function of transformation
 */

package cz.ismar.projects.IEdit;

import cz.ismar.projects.IEdit.structure.Fragment;
import cz.ismar.projects.IEdit.structure.FragmentHolder;
import cz.ismar.projects.IEdit.structure.Frame;
import cz.ismar.projects.IEdit.structure.FrameFragment;
import cz.ismar.projects.IEdit.structure.FrameFragmentMasks;
import cz.ismar.projects.IEdit.structure.Gesture;
import java.awt.Color;
import java.awt.Component;
import javax.swing.*;

/**
 * JList 图片查看接口
 * 
 * - 显示切片，帧等信息
 * 
 * @author Lori
 * 
 */
public class IconLabelListRenderer extends DefaultListCellRenderer
{

	public IconLabelListRenderer()
	{
	}

	// 对象获得paint焦点时调用，如切换窗口，对象变更等
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
	{
		JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

		if(value instanceof Fragment)
		{
			Fragment fragment = (Fragment) value;
			ImageIcon imageicon = new ImageIcon(fragment.getImage());
			label.setIcon(imageicon);
		}else if(value instanceof Frame)
		{
			Frame frame = (Frame) value;
			if(frame.getImage() != null)
			{
				ImageIcon imageicon1 = new ImageIcon(frame.getImage());
				label.setIcon(imageicon1);
			}
		}else if(value instanceof Gesture)
		{
			Gesture gesture = (Gesture) value;
			if(gesture.getFrame() != null && gesture.getFrame().getImage() != null)
			{
				ImageIcon imageicon2 = new ImageIcon(gesture.getFrame().getImage());
				label.setIcon(imageicon2);
			}
		}else if(value instanceof FrameFragment)
		{
			FrameFragment framefragment = (FrameFragment) value;
			// daben:modify
			// ImageIcon imageicon3 = new
			// ImageIcon(framefragment.getFragment().getImage());
			// modify:
			ImageIcon imageicon3 = new ImageIcon(framefragment.getImage());
			// ~daben
			label.setIcon(imageicon3);
			
			// 如果是切处是隐藏, 背景为dark 
			if(SpriteEditor.frameFragmentMasks.isHiden(framefragment))
			{
				label.setBackground(label.getBackground().darker());
			}
		}else if(value instanceof FragmentHolder)
		{
			label.setIcon(((FragmentHolder) value).getIcon());
		}
		else
		{
			label.setIcon(null);
		}
		return label;
	}
}
