/**
 * 2007-9-14 daben
 * - add
 * --> SpriteEditor.initFrameFragmentIcons();
 * 
 * 2007-11-1 daben
 * - add   FragmentHolder oldFragmentHolder = SpriteEditor.project.fragmentHolder;
 *         SpriteEditor.project.fragmentHolder = oldFragmentHolder;
 * 
 */

package cz.ismar.projects.IEdit.handlers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.io.InOut;
import cz.ismar.projects.IEdit.io.OldFileFormatException;
import cz.ismar.projects.IEdit.io.WrongFileFormatException;
import cz.ismar.projects.IEdit.structure.FragmentHolder;
import cz.ismar.projects.IEdit.structure.Sprite;
import cz.ismar.projects.IEdit.ui.History;

public class XMLOpenMenuActionListener implements ActionListener
{

	public XMLOpenMenuActionListener(History history1)
	{
		history = history1;
	}

	public void actionPerformed(ActionEvent actionevent)
	{
		// daben:add
		if(!SpriteEditor.isAnimationSaved())
			return;
		// ~daben

		JFileChooser jfilechooser = SpriteEditor.getFileChooser();
		jfilechooser.setFileFilter(SpriteEditor.xmlFileFilter);
		int i = jfilechooser.showOpenDialog(SpriteEditor.mainFrame);
		if(i == JFileChooser.APPROVE_OPTION)
		{
			File file = jfilechooser.getSelectedFile();
			Sprite oldProject = SpriteEditor.sprite;
			try
			{
				// daben:add
				FragmentHolder oldFragmentHolder = SpriteEditor.sprite.fragmentHolder;
				// ~daben

				SpriteEditor.sprite = InOut.inXML(file.getAbsolutePath());
				// daben:add
				SpriteEditor.sprite.fragmentHolder = oldFragmentHolder;

				// ~daben
				SpriteEditor.setStatus(SpriteEditor.xmlSavePath + " 已加载");
				if(SpriteEditor.frameFragmentMasks.isEnabled() != SpriteEditor.useMasksMenuItem.isSelected())
					SpriteEditor.useMasksMenuItem.doClick(0);
				try
				{
					history.updatePrefsHistory(file.getCanonicalPath());
				}catch(IOException ioexception)
				{
				}
			}catch(OldFileFormatException oldfileformatexception)
			{
				int option = JOptionPane.showConfirmDialog(SpriteEditor.mainFrame, "Older sprite format. Make conversion?", SpriteEditor.TITLE, 0);
				if(option == 0)
				{
					int k = 0;
					int l = JOptionPane.showConfirmDialog(SpriteEditor.mainFrame, "Delete Older sprite format files?", SpriteEditor.TITLE, 0);
					if(l == 0)
						k = 1;
					File file1 = new File(file.getParent(), oldfileformatexception.getImagePath());
					InOut.convertOldFormat(file, file1, k);
				}
			}catch(WrongFileFormatException wrongfileformatexception)
			{
				String s = "加载出错 " + wrongfileformatexception.getLocalizedMessage();
				JOptionPane.showMessageDialog(SpriteEditor.mainFrame, s, SpriteEditor.TITLE, 0);
				SpriteEditor.setStatus(s);
				// wrongfileformatexception.printStackTrace();
				System.out.println("打开XML文件时出错");
				SpriteEditor.sprite = oldProject;
			}
			// InOut.inXML操作根据XML里的信息构造各个类对象，并用XML里的数据初始化，但此时各个类对象还没初始化完！
			// 比如FrameFragment.fragment.getImage为null。程序提供initXXX为加载的类完成剩下的初始化操作

			// 注意初始化顺序！原FrameIcon建立在FragmentIcons上，加入变换切片功能后，FrameFragment增加一个属性transformedImage，
			// 即FrameFragment维护着一个FrameFragmentsIcon，此时FrameIcon重新建立在FrameFragmentsIcon上，所以要先初始化FrameFragmentsIcon

			SpriteEditor.initFragmentIcons();
			// daben:add
			SpriteEditor.initFrameFragmentIcons();
			// ~daben
			SpriteEditor.initFrameIcons();
			// daben:remove
			// SpriteEditor.mainFragmentPanel.initFragments();
			SpriteEditor.fragmentPanel.revalidate();
			// ~daben
			
			// 帧编辑区
			SpriteEditor.framePanel.initFragmentFiles();
			SpriteEditor.framePanel.initFragments();
			SpriteEditor.framePanel.initFrames();
			SpriteEditor.framePanel.initFrameFragments();
			
			// 动画编辑区
			SpriteEditor.animationPanel.initGestures();
			SpriteEditor.animationPanel.initFrames();
			SpriteEditor.animationPanel.initAnimations();
			
			// 变装编辑区
//			SpriteEditor.avatarPanel.initFragmentFiles();
//			SpriteEditor.avatarPanel.initFragments();
//			SpriteEditor.avatarPanel.initFrames();
//			SpriteEditor.avatarPanel.initFrameFragments();
		}

		// daben:add 重置标志
		SpriteEditor.animationsSaved = true;
		// ~daben
	}

	private History history;
}
