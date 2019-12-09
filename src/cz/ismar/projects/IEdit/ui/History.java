/**
 * 2007-10-16 daben
 * - add SpriteEditor.initFrameFragmentIcons();
 * - in createRecentMenu()
 */

package cz.ismar.projects.IEdit.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.io.InOut;
import cz.ismar.projects.IEdit.structure.FragmentHolder;
import cz.ismar.projects.IEdit.structure.Sprite;

public class History
{

	public History(JMenu jmenu)
	{
		historyMenu = jmenu;
	}

	public void createRecentMenu()
	{
		Preferences preferences;
		preferences = SpriteEditor.preferences.node("history");
		historyMenu.removeAll();

		try
		{
			int i = preferences.keys().length;

			// System.out.println("preferences.keys().length: "+i);
			int j = 0;

			while(true)
			{
				final String path = preferences.get(j + "", "");

				if(j >= i)
					break;

				if(path.length() != 0)
				{

					try
					{
						if(SpriteEditor.xmlSavePath == null || !path.equals((new File(SpriteEditor.xmlSavePath)).getCanonicalPath()))
						{
							JMenuItem jmenuitem = new JMenuItem(path);
							jmenuitem.addActionListener(new ActionListener() {

								public void actionPerformed(ActionEvent actionevent)
								{
									// 询问是否需要保存
									if(!SpriteEditor.isAnimationSaved())
										return;

									Sprite oldProject = SpriteEditor.sprite;
									try
									{
										// daben:add
										FragmentHolder oldFragmentHolder = SpriteEditor.sprite.fragmentHolder;
										// ~daben

										SpriteEditor.sprite = InOut.inXML(path);
										// daben:add
										SpriteEditor.sprite.fragmentHolder = oldFragmentHolder;

										// ~daben
										SpriteEditor.setStatus(SpriteEditor.xmlSavePath + " loaded");
										if(SpriteEditor.frameFragmentMasks.isEnabled() != SpriteEditor.useMasksMenuItem.isSelected())
											SpriteEditor.useMasksMenuItem.doClick(0);
										SpriteEditor.initFragmentIcons();
										// daben:add
										SpriteEditor.initFrameFragmentIcons();
										// ~daben
										SpriteEditor.initFrameIcons();
										// SpriteEditor.mainFragmentPanel.initFragments();
										SpriteEditor.framePanel.initFragmentFiles();
										SpriteEditor.framePanel.initFragments();
										SpriteEditor.framePanel.initFrames();
										SpriteEditor.framePanel.initFrameFragments();
										
										SpriteEditor.animationPanel.initGestures();
										SpriteEditor.animationPanel.initFrames();
										SpriteEditor.animationPanel.initAnimations();
										
//										SpriteEditor.avatarPanel.initFragmentFiles();
//										SpriteEditor.avatarPanel.initFragments();
//										SpriteEditor.avatarPanel.initFrames();
//										SpriteEditor.avatarPanel.initFrameFragments();
										
									}catch(Exception exception)
									{
										SpriteEditor.sprite = oldProject;
										String s = "Error when reloading.. MESSAGE: " + exception.getLocalizedMessage();
										JOptionPane.showMessageDialog(SpriteEditor.mainFrame, s, SpriteEditor.TITLE, 0);
										SpriteEditor.setStatus(s);
										// exception.printStackTrace();
										System.out.println("createRecentMenu Error");
									}

									// daben:add 重置标志
									SpriteEditor.animationsSaved = true;
									// ~daben
								}

							});
							historyMenu.add(jmenuitem);

							j++;
						}else
							j++;

					}catch(IOException ioexception)
					{
						// ioexception.printStackTrace();
						System.out.println("IO Error: no xml file!!!");
					}

				}else
					j++;

			}// end while

		}catch(BackingStoreException backingstoreexception)
		{
			// backingstoreexception.printStackTrace();
			System.out.println("Backing Store Error");
		}
	}

	public void updatePrefsHistory(String s)
	{
		Preferences preferences = SpriteEditor.preferences.node("history");
		String as[] = new String[5];
		for(int i = 0; i < as.length; i++)
			as[i] = preferences.get(Integer.toString(i), "");

		int j = 0;
		preferences.put(Integer.toString(j++), s);
		for(int k = 0; k < as.length; k++)
		{
			String s1 = as[k];
			if(s1.equals(s))
				continue;
			preferences.put(Integer.toString(j++), s1);
			if(j >= 5)
				break;
		}

		createRecentMenu();
	}

	private final int HISTORY_ITEMS_SIZE = 5;
	public JMenu historyMenu;
}
