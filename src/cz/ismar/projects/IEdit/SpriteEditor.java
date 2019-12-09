/**
 * 2007-9-12 daben
 * - add transformation popup menu item to frameFragmentPopup #see daben:add
 *
 * 2007-9-14 daben
 * //初始化切片图象-FrameFragment.transformedImage
 * - add a method
 * --> initFrameFragmentIcons()
 * 
 * 2007-10-30 daben
 * - 屏蔽菜单 "LoadConf" and "Convert directory" and "Export animations"
 * 
 *                SpriteEditor.fileMenu.add(SpriteEditor.itemLoadConf);
 *                SpriteEditor.fileMenu.addSeparator();
 *                SpriteEditor.fileMenu.add(SpriteEditor.itemConvertXMLs);
 *                SpriteEditor.fileMenu.add(SpriteEditor.itemExportAnims);
 *                
 * 2007-11-1 daben
 * // 打开xml文件时，清空上次打开的记录
 * - add in getFileChooser()
 * -       fileChooser.setSelectedFile(new File(""));
 * 
 * //退出或者打开其它xml时，发出“是否需要保存”的询问！
 * - add a method
 *         isAllSaved()  
 *         isFragmentSaved()
 *         isAnimationSaved()
 *         
 * //添加菜单“测试”-> "在模拟器中运行"
 *     SpriteEditor.runInSimulatorMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0));
 *     SpriteEditor.runInSimulatorMenuItem.addActionListener(new RunInSimulatorActionListener());
 *     SpriteEditor.testMenu.add(SpriteEditor.runInSimulatorMenuItem);
 *     SpriteEditor.menuBar.add(SpriteEditor.testMenu);
 *         
 * // 在 帧切片 列表 里 增加 “撤消”功能
 *     SpriteEditor.frameFragmentPopup.addSeparator();
 *     SpriteEditor.frameFragmentPopup.add(SpriteEditor.undoFrameMenuItem);
 * // 增加了切片块翻转功能，不过暂时屏蔽该功能
 *     //SpriteEditor.frameFragmentPopup.add(SpriteEditor.transformFrameFragmentMenu);
 *     
 * //
 *  
 *  
 * 2008-1-18 Lori
 * - 开始整合动画编辑器，整合前版本号为2.12
 * 
 * v2.13 2008/3/7 Lori
 * -增加动画判定帧
 * -增加动画类型选择
 * -Animation.java
 *   private byte animationId; //如 斧攻击，杖攻击等
 *   private byte checkFrameId = -1;//判定帧
 *         
 * - 模拟器运行，加入判定帧的观看效果
 * 
 * v2.14 2008/3/14 Lori
 * -增加动画类型 （魔法类的选择）
 */

/*  //相当于J2me的Rms
 * public static Preferences preferences = Preferences.userRoot().node("iedit2");
 * 
 * - 弹出菜单使用规范(- 鼠标事件与按键事件同时应用) 
 *  -addMouseListener() -在mouseRelease里 做出选项的筛选后，弹出菜单A;
 *  -addKeyListener() -  
 * 
 *  - 弹出菜单A早已经配置好每个选项的事件；
 *  选项1.addActionListener(framefragmentpopupmenuactionlistener);
 *  选项2.addActionListener(framefragmentpopupmenuactionlistener);
 *  选项3.addActionListener(framefragmentpopupmenuactionlistener);
 *  选项4.addActionListener(framefragmentpopupmenuactionlistener);
 *  选项5.addActionListener(framefragmentpopupmenuactionlistener);
 *
 *  
 * */
package cz.ismar.projects.IEdit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.prefs.Preferences;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

import cz.ismar.projects.IEdit.handlers.AboutMenuActionListener;
import cz.ismar.projects.IEdit.handlers.AllViewMenuActionListener;
import cz.ismar.projects.IEdit.handlers.AniMenuActionListener;
import cz.ismar.projects.IEdit.handlers.DeleteUnuseFrameHandler;
import cz.ismar.projects.IEdit.handlers.DoubleSizeActionListener;
import cz.ismar.projects.IEdit.handlers.ExitMenuActionListener;
import cz.ismar.projects.IEdit.handlers.FragmentAddToFrameActionListener;
import cz.ismar.projects.IEdit.handlers.FrameFragmentPopUpMenuActionListener;
import cz.ismar.projects.IEdit.handlers.FrameFragmentsListCommander;
import cz.ismar.projects.IEdit.handlers.HalfSizeActionListener;
import cz.ismar.projects.IEdit.handlers.NewProjectMenuActionListener;
import cz.ismar.projects.IEdit.handlers.PercentSizeActionListener;
import cz.ismar.projects.IEdit.handlers.ReloadMenuActionListener;
import cz.ismar.projects.IEdit.handlers.RunInSimulatorActionListener;
import cz.ismar.projects.IEdit.handlers.XMLConvertMenuActionListener;
import cz.ismar.projects.IEdit.handlers.XMLOpenMenuActionListener;
import cz.ismar.projects.IEdit.handlers.XMLSaveAsMenuActionListener;
import cz.ismar.projects.IEdit.handlers.XMLSaveMenuActionListener;
import cz.ismar.projects.IEdit.handlers.ZoomMenuActionListener;
import cz.ismar.projects.IEdit.io.AnimationExporter;
import cz.ismar.projects.IEdit.io.BinFileFilter;
import cz.ismar.projects.IEdit.io.CnfFileFilter;
import cz.ismar.projects.IEdit.io.FileUtils;
import cz.ismar.projects.IEdit.io.InOut;
import cz.ismar.projects.IEdit.io.OldFileFormatException;
import cz.ismar.projects.IEdit.io.PNGFileFilter;
import cz.ismar.projects.IEdit.io.WhateverFileFilter;
import cz.ismar.projects.IEdit.io.WrongFileFormatException;
import cz.ismar.projects.IEdit.io.XMLFileFilter;
import cz.ismar.projects.IEdit.io.property.PropertyIO;
import cz.ismar.projects.IEdit.structure.Background;
import cz.ismar.projects.IEdit.structure.BackgroundItem;
import cz.ismar.projects.IEdit.structure.ConfigData;
import cz.ismar.projects.IEdit.structure.Fragment;
import cz.ismar.projects.IEdit.structure.Frame;
import cz.ismar.projects.IEdit.structure.FrameFragment;
import cz.ismar.projects.IEdit.structure.FrameFragmentMasks;
import cz.ismar.projects.IEdit.structure.Sprite;
import cz.ismar.projects.IEdit.ui.AnimationExporterDialog;
import cz.ismar.projects.IEdit.ui.AvatarPanel;
import cz.ismar.projects.IEdit.ui.BoundsSaver;
import cz.ismar.projects.IEdit.ui.FragmentPanel;
import cz.ismar.projects.IEdit.ui.FrameFragmentMasksDialog;
import cz.ismar.projects.IEdit.ui.FramePanel;
import cz.ismar.projects.IEdit.ui.AnimationPanel;
import cz.ismar.projects.IEdit.ui.History;

/**
 * @author Lori
 * @version 2.12 2008/1/18
 * 
 */
public class SpriteEditor
{

	public SpriteEditor()
	{
	}

	public static JFileChooser getDirChooser()
	{
		if(dirChooser == null)
		{
			dirChooser = new JFileChooser();
			dirChooser.setFileSelectionMode(1);
			File file = null;
			if(dirChooser.getCurrentDirectory() == dirChooser.getFileSystemView().getDefaultDirectory())
				if(sprite.xmlFile != null)
					file = sprite.xmlFile.getParentFile();
				else
					file = new File("./");
			if(file != null)
				dirChooser.setCurrentDirectory(file);
			dirChooser.addChoosableFileFilter(new FileFilter() {

				public boolean accept(File file1)
				{
					return file1.isDirectory();
				}

				public String getDescription()
				{
					return "select directory";
				}

			});
		}
		return dirChooser;
	}

	public static JFileChooser getFileChooser()
	{
		if(fileChooser == null)
		{
			fileChooser = new JFileChooser();
			File file = null;
			if(fileChooser.getCurrentDirectory() == fileChooser.getFileSystemView().getDefaultDirectory())
			{

				System.out.println(": " + fileChooser.getCurrentDirectory().getAbsolutePath());
				if(sprite.xmlFile != null)
				{
					file = sprite.xmlFile.getParentFile();
				}else
				{
					// 当前工程的父目录
					file = new File("./");
				}
			}
			if(file != null)
				fileChooser.setCurrentDirectory(file);
		}else
		{
			fileChooser.removeChoosableFileFilter(xmlFileFilter);
			fileChooser.removeChoosableFileFilter(pngFileFilter);
			fileChooser.removeChoosableFileFilter(binFileFilter);
			fileChooser.removeChoosableFileFilter(whateverFileFilter);
		}
		// daben:add
		// 打开xml文件时，清空上次打开的记录
		fileChooser.setSelectedFile(new File(""));

		return fileChooser;
	}

	public static void initFragmentIcons()
	{
		if(sprite == null)
		{
			System.out.println("initFragmentIcons project is null!");
		}else
		{
			ImageIcon imageicon = sprite.fragmentHolder.getIcon();
			if(imageicon != null && fragmentPanel != null)
			{
				fragmentPanel.repaint();
				fragmentPanel.fragmentEditorComponent.setImageSize(imageicon.getIconWidth(), imageicon.getIconHeight());
			}
		}

		if(backgroundImagePath != null)
		{
			try
			{
				BackgroundItem backgrounditem = new BackgroundItem();
				backgrounditem.setImagePath(backgroundImagePath);
				backgrounditem.setPosX(bgndX);
				backgrounditem.setPosY(bgndY);
				gesturesBackground.addBackgroundItem(backgrounditem);
			}catch(IOException ioexception)
			{
				// ioexception.printStackTrace();
				System.out.println("initFragmentIcons IO Error");
			}
		}

		if(backgroundFramesImagePath != null && framesBackground.getItems().size() == 0)
		{
			try
			{
				BackgroundItem backgrounditem1 = new BackgroundItem();
				backgrounditem1.setImagePath(backgroundFramesImagePath);
				backgrounditem1.setPosX(bgndFramesX);
				backgrounditem1.setPosY(bgndFramesY);
				framesBackground.addBackgroundItem(backgrounditem1);
			}catch(IOException ioexception1)
			{
				// ioexception1.printStackTrace();
				System.out.println("initFragmentIcons IO Error2");
			}
		}
		
		ArrayList<Fragment> list = sprite.fragmentHoldersList.getAllFragments().getFragments();
//		Fragment fragment;
		for(Fragment fragment : list)
		{
			fragment.updateImage();
		}
//		for(Iterator iterator = list.iterator(); iterator.hasNext(); fragment.updateImage())
//			fragment = (Fragment) iterator.next();

		list = sprite.fragmentHolder.getFragments();
		for(Fragment fragment : list)
		{
			fragment.updateImage();
		}
//		Fragment fragment1;
//		for(Iterator iterator1 = list.iterator(); iterator1.hasNext(); fragment1.updateImage())
//			fragment1 = (Fragment) iterator1.next();
	}

	// daben:add 初始化切片图象-FrameFragment.transformedImage
	public static void initFrameFragmentIcons()
	{
		ArrayList<Frame> list = SpriteEditor.sprite.frameHolder.getFrames();
		
		for(Frame frame : list)
		{
			for(FrameFragment frameFragment : frame.getFrameFragments())
			{
				frameFragment.updateImage();
			}
		}
		
//		for(Iterator iterator = list.iterator(); iterator.hasNext();)
//		{
//			ArrayList<FrameFragment> list2 = ((Frame) iterator.next()).getFrameFragments();
//			for(Iterator iterator2 = list2.iterator(); iterator2.hasNext(); ((FrameFragment) iterator2.next()).updateImage())
//				;
//		}
	}

	// ~daben

	public static void initFrameIcons()
	{
		ArrayList<Frame> list = sprite.frameHolder.getFrames();
		for(Frame frame : list)
		{
			frame.updateImage();
		}
//		for(Iterator iterator = list.iterator(); iterator.hasNext(); ((Frame) iterator.next()).updateImage())
//			;
	}

	public static void main(String args[])
	{
		String s = null;
		String s1 = null;
		System.out.println(TITLE);
		// System.out.println("- - - - - -");
		spriteConf.setComments("iEdit sprite conf file");

		spriteConf.addPropertyIOable("framesBackround", framesBackground);

		spriteConf.addPropertyIOable("gesturesBackground", gesturesBackground);

		if(args != null)
		{

			String s2 = null;
			for(int j = 0; j < args.length; j++)
			{
				if(args[j].startsWith("-h"))
				{
					System.out.println("options:");
					for(int k = 0; k < OPTIONS.length; k++)
						System.out.println(" " + OPTIONS[k]);

					System.out.println("----------------------------------");
				}
				if(args[j].startsWith("-i"))
				{
					File file = new File(args[j].substring(2));
					System.out.println("iEdit PARAM image:" + file + " exists:" + file.exists());
					if(file.exists())
						s = file.getAbsolutePath();
				}
				if(args[j].startsWith("-bx"))
				{
					bgndX = Integer.parseInt(args[j].substring(3));
					System.out.println("iEdit PARAM background image X pos: " + bgndX);
				}else if(args[j].startsWith("-by"))
				{
					bgndY = Integer.parseInt(args[j].substring(3));
					System.out.println("iEdit PARAM background image Y pos: " + bgndY);
				}else if(args[j].startsWith("-b"))
				{
					File file1 = new File(args[j].substring(2));
					System.out.println("iEdit PARAM background image:" + file1 + " exists:" + file1.exists());
					if(file1.exists())
						backgroundImagePath = file1.getAbsolutePath();
				}
				if(args[j].startsWith("-bfx"))
				{
					bgndFramesX = Integer.parseInt(args[j].substring(4));
					System.out.println("iEdit PARAM background image X pos: " + bgndFramesX);
				}else if(args[j].startsWith("-bfy"))
				{
					bgndFramesY = Integer.parseInt(args[j].substring(4));
					System.out.println("iEdit PARAM background image Y pos: " + bgndFramesY);
				}else if(args[j].startsWith("-bf"))
				{
					File file2 = new File(args[j].substring(3));
					System.out.println("iEdit PARAM background image:" + file2 + " exists:" + file2.exists());
					if(file2.exists())
						backgroundFramesImagePath = file2.getAbsolutePath();
				}
				if(args[j].startsWith("-s"))
				{
					File file3 = new File(args[j].substring(2));
					System.out.println("iEdit PARAM source xml: " + file3 + " exists:" + file3.exists());
					if(file3.exists())
					{
						s1 = file3.getAbsolutePath();
						xmlSavePath = file3.getAbsolutePath();
					}
				}
				if(args[j].startsWith("-tspec"))
				{
					File file4 = new File(args[j].substring(6));
					System.out.println("iEdit PARAM target spec xml: " + file4 + " exists:" + file4.exists());
					xmlSpecSavePath = file4.getAbsolutePath();
				}else if(args[j].startsWith("-t"))
				{
					File file5 = new File(args[j].substring(2));
					System.out.println("iEdit PARAM target xml: " + file5 + " exists:" + file5.exists());
					xmlSavePath = file5.getAbsolutePath();
				}
				if(args[j].startsWith("-v"))
				{
					System.out.print("iEdit PARAM verbose: ");
					if(args[j].indexOf("x") != -1)
					{
						InOut.verbose = true;
						System.out.print("xml in out, ");
					}
					if(args[j].indexOf("f") != -1)
					{
						FileUtils.verbose = true;
						System.out.print("file utilst, ");
					}
					System.out.println("");
				}
				if(args[j].startsWith("-a"))
				{
					s2 = args[j].substring(2);
					System.out.println("iEdit PARAM actions: " + s2);
				}
			}

			// System.out.println("- - -");
			try
			{
				if(s1 != null)
				{
					try
					{
						sprite = InOut.inXML(s1);
						history.updatePrefsHistory((new File(s1)).getCanonicalPath());
					}catch(OldFileFormatException oldfileformatexception)
					{
						sprite = new Sprite("DEFPROJECT");
						File file6 = new File(s1);
						int option = JOptionPane.showConfirmDialog(mainFrame, "Older sprite format. Make conversion?", TITLE, 0);
						if(option == 0)
						{
							int j1 = 0;
							int option2 = JOptionPane.showConfirmDialog(mainFrame, "Delete Older sprite format files?", TITLE, 0);
							if(option2 == 0)
								j1 = 1;
							File file7 = new File(file6.getParent(), oldfileformatexception.getImagePath());
							InOut.convertOldFormat(file6, file7, j1);
						}
					}catch(WrongFileFormatException wrongfileformatexception)
					{
						// wrongfileformatexception.printStackTrace();
						System.out.println("wrongfileformat");
					}
					if(s2 == null)
					{
						initFragmentIcons();
						initFrameIcons();
					}
				}
			}catch(Exception exception)
			{
				System.out.println("iEdit error importing, creating default");
				// exception.printStackTrace();
			}

			if(sprite == null)
			{
				sprite = new Sprite("DEFPROJECT");
			}
			if(s != null)
			{
				sprite.setSpriteImgPath(s);
				ImageIcon imageicon = new ImageIcon(sprite.getSpriteImgPath());
				if(imageicon != null)
				{
					if(fragmentPanel != null)
						fragmentPanel.repaint();
					if(s2 == null)
					{
						initFragmentIcons();
						initFrameIcons();
					}
					System.out.println("iEdit setting new  image");
				}
			}
			if(s2 != null)
			{
				long l = System.currentTimeMillis();
				if(s2.indexOf("x") != -1)
				{
					System.out.println("iEdit saving in native XML to:" + xmlSavePath);
					try
					{
						InOut.outXML(sprite, new File(xmlSavePath));
					}catch(Exception exception1)
					{
						System.out.println("Editor.main ERROR when PROJECT XML SAVE:");
						// exception1.printStackTrace();
						System.exit(-1);
					}
				}
				if(s2.indexOf("s") != -1)
				{
					System.out.println("iEdit saving in spec XML to:" + xmlSpecSavePath);
					InOut.exportToAndrleXML(sprite, new File(xmlSpecSavePath));
				}
				System.out.println("iEdit no UI actions done in:" + (System.currentTimeMillis() - l) + "ms");
				return;
			}
		}else
		{
			System.out.println("options:");
			for(int i = 0; i < OPTIONS.length; i++)
				System.out.println(" " + OPTIONS[i]);

			System.out.println("----------------------------------");
		}
		
		if(sprite.getCurrentAnimation() != null)
		{	
			sprite.getCurrentAnimation().reset();
		}

		SwingUtilities.invokeLater(new Runnable() {

			public void run()
			{

				// System.out.println("run!!!!!!!!!!!!!!!");

				ConfigData.init();

				SpriteEditor.loadPrefs();

				// 切片编辑区
				SpriteEditor.fragmentPanel = new FragmentPanel();
				SpriteEditor.fragmentPanel.initFragments();
				ImageIcon imageicon1 = SpriteEditor.sprite.fragmentHolder.getIcon();
				if(imageicon1 != null)
				{
					SpriteEditor.fragmentPanel.fragmentEditorComponent.setImageSize(imageicon1.getIconWidth(), imageicon1.getIconHeight());
				}

				// 帧编辑区
				SpriteEditor.framePanel = new FramePanel();
				SpriteEditor.framePanel.initFragmentFiles();
				SpriteEditor.framePanel.initFragments();
				SpriteEditor.framePanel.initFrames();
				SpriteEditor.framePanel.initFrameFragments();

				// 动画编辑区
				SpriteEditor.animationPanel = new AnimationPanel();
				SpriteEditor.animationPanel.initGestures();
				SpriteEditor.animationPanel.initFrames();
				SpriteEditor.animationPanel.initAnimations();

				// 变装编辑区
				SpriteEditor.avatarPanel = new AvatarPanel();

				SpriteEditor.fileMenu.setMnemonic('F');
				// daben:add
				// 增加新建工程功能
				SpriteEditor.fileMenu.add(SpriteEditor.itemNewProject);
				SpriteEditor.itemNewProject.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, 2));
				SpriteEditor.itemNewProject.addActionListener(new NewProjectMenuActionListener());
				// ~daben
				SpriteEditor.fileMenu.add(SpriteEditor.itemOpenXML);
				SpriteEditor.fileMenu.add(SpriteEditor.itemSaveAsXML);
				SpriteEditor.fileMenu.add(SpriteEditor.itemSaveXML);
				SpriteEditor.fileMenu.add(SpriteEditor.itemReload);
				SpriteEditor.fileMenu.add(SpriteEditor.itemExportAnims);
				SpriteEditor.fileMenu.addSeparator();
				SpriteEditor.fileMenu.add(SpriteEditor.historyMenu);

				/*
				 * SpriteEditor.fileMenu.addSeparator();
				 * SpriteEditor.fileMenu.add(SpriteEditor.itemLoadConf);
				 * SpriteEditor.fileMenu.addSeparator();
				 * SpriteEditor.fileMenu.add(SpriteEditor.itemConvertXMLs);
				 * SpriteEditor.fileMenu.add(SpriteEditor.itemExportAnims);
				 */
				SpriteEditor.fileMenu.addSeparator();
				SpriteEditor.fileMenu.add(SpriteEditor.itemExit);
				SpriteEditor.itemOpenXML.setAccelerator(KeyStroke.getKeyStroke(79, 2));
				SpriteEditor.itemSaveXML.setAccelerator(KeyStroke.getKeyStroke(83, 2));
				SpriteEditor.itemSaveAsXML.setAccelerator(KeyStroke.getKeyStroke(83, 3));
				SpriteEditor.itemReload.setAccelerator(KeyStroke.getKeyStroke(82, 2));
				SpriteEditor.itemSaveXML.addActionListener(new XMLSaveMenuActionListener());
				SpriteEditor.itemSaveAsXML.addActionListener(new XMLSaveAsMenuActionListener());
				SpriteEditor.itemOpenXML.addActionListener(new XMLOpenMenuActionListener(SpriteEditor.history));
				SpriteEditor.itemConvertXMLs.addActionListener(new XMLConvertMenuActionListener());
				SpriteEditor.itemExportAnims.addActionListener(new java.awt.event.ActionListener() {

					public void actionPerformed(java.awt.event.ActionEvent actionevent)
					{
						if(sprite == null)
						{
							JOptionPane.showMessageDialog(SpriteEditor.mainFrame, "请先打开动画!");
						}
						
//						(new AnimationExporterDialog()).createGui();
						JFileChooser fileChooser = getDirChooser();
						int option = fileChooser.showSaveDialog(SpriteEditor.mainFrame);
						if(option == JFileChooser.APPROVE_OPTION)
						{
							File file = fileChooser.getSelectedFile();
							AnimationExporter.exportGIF(sprite, file);
						}
					}

				});
				SpriteEditor.itemReload.addActionListener(new ReloadMenuActionListener());
				SpriteEditor.itemLoadConf.addActionListener(new java.awt.event.ActionListener() {

					public void actionPerformed(java.awt.event.ActionEvent actionevent)
					{
						JFileChooser jfilechooser = SpriteEditor.getFileChooser();
						jfilechooser.setFileFilter(SpriteEditor.cnfFileFilter);
						int i2 = jfilechooser.showOpenDialog(SpriteEditor.mainFrame);
						if(i2 == 0)
						{
							File file8 = jfilechooser.getSelectedFile();
							try
							{
								if(SpriteEditor.spriteConf.loadFromFile(file8))
								{
									if(SpriteEditor.framePanel != null)
										SpriteEditor.framePanel.backgroundEditor.dispose();
									SpriteEditor.framePanel.initBackgroundEditor();
									if(SpriteEditor.animationPanel != null)
										SpriteEditor.animationPanel.backgroundEditor.dispose();
									SpriteEditor.animationPanel.initBackgroundEditor();
								}
							}catch(Exception exception2)
							{
								// exception2.printStackTrace();
								JOptionPane.showMessageDialog(SpriteEditor.mainFrame, "Error configuration loading!", TITLE, 0);
							}
						}
					}

				});
				SpriteEditor.itemExit.addActionListener(new ExitMenuActionListener());
				SpriteEditor.viewMenu.setMnemonic('V');
				SpriteEditor.viewMenu.addActionListener(SpriteEditor.zoomMenuActionListener);
				JCheckBoxMenuItem jcheckboxmenuitem = new JCheckBoxMenuItem("网格");
				jcheckboxmenuitem.setAccelerator(KeyStroke.getKeyStroke(71, 8));
				jcheckboxmenuitem.addActionListener(SpriteEditor.allViewMenuActionListener);
				jcheckboxmenuitem.setState(SpriteEditor.gridOn);
				JCheckBoxMenuItem jcheckboxmenuitem1 = new JCheckBoxMenuItem("坐标架");
				jcheckboxmenuitem1.setAccelerator(KeyStroke.getKeyStroke(67, 8));
				jcheckboxmenuitem1.addActionListener(SpriteEditor.allViewMenuActionListener);
				jcheckboxmenuitem1.setState(SpriteEditor.crossOn);

				JCheckBoxMenuItem jcheckboxmenuitem2 = new JCheckBoxMenuItem("碰撞框");
				jcheckboxmenuitem2.setAccelerator(KeyStroke.getKeyStroke(65, 8));
				jcheckboxmenuitem2.addActionListener(SpriteEditor.allViewMenuActionListener);
				jcheckboxmenuitem2.setState(SpriteEditor.collisionOn);
				JCheckBoxMenuItem jcheckboxmenuitem3 = new JCheckBoxMenuItem("动画列表");
				jcheckboxmenuitem3.addActionListener(SpriteEditor.allViewMenuActionListener);
				jcheckboxmenuitem3.setAccelerator(KeyStroke.getKeyStroke(90, 8));
				jcheckboxmenuitem3.setState(true);
				JCheckBoxMenuItem jcheckboxmenuitem4 = new JCheckBoxMenuItem("帧列表");
				jcheckboxmenuitem4.addActionListener(SpriteEditor.allViewMenuActionListener);
				jcheckboxmenuitem4.setAccelerator(KeyStroke.getKeyStroke(88, 8));
				jcheckboxmenuitem4.setState(true);
				JCheckBoxMenuItem jcheckboxmenuitem5 = new JCheckBoxMenuItem("动画帧列表");
				jcheckboxmenuitem5.addActionListener(SpriteEditor.allViewMenuActionListener);
				jcheckboxmenuitem5.setAccelerator(KeyStroke.getKeyStroke(67, 8));
				jcheckboxmenuitem5.setState(true);
				JCheckBoxMenuItem jcheckboxmenuitem6 = new JCheckBoxMenuItem("Trace mouse");
				jcheckboxmenuitem6.addActionListener(SpriteEditor.allViewMenuActionListener);
				jcheckboxmenuitem6.setState(true);
				JCheckBoxMenuItem jcheckboxmenuitem7 = new JCheckBoxMenuItem("TileGroups(composite,anim)");
				jcheckboxmenuitem7.addActionListener(SpriteEditor.allViewMenuActionListener);
				jcheckboxmenuitem7.setState(true);
				JCheckBoxMenuItem jcheckboxmenuitem8 = new JCheckBoxMenuItem("Background tiles");
				jcheckboxmenuitem8.addActionListener(SpriteEditor.allViewMenuActionListener);
				jcheckboxmenuitem8.setState(true);
				JCheckBoxMenuItem jcheckboxmenuitem9 = new JCheckBoxMenuItem("Scripts in map");
				jcheckboxmenuitem9.addActionListener(SpriteEditor.allViewMenuActionListener);
				jcheckboxmenuitem9.setState(true);
				JCheckBoxMenuItem jcheckboxmenuitem10 = new JCheckBoxMenuItem("Routers in map");
				JCheckBoxMenuItem jcheckboxmenuitem11 = new JCheckBoxMenuItem("RouterIDs in map");
				jcheckboxmenuitem10.addActionListener(SpriteEditor.allViewMenuActionListener);
				jcheckboxmenuitem10.setState(true);
				jcheckboxmenuitem11.addActionListener(SpriteEditor.allViewMenuActionListener);
				jcheckboxmenuitem11.setState(true);

				JCheckBoxMenuItem jcheckboxmenuitem12 = new JCheckBoxMenuItem("地图碰撞");
				jcheckboxmenuitem12.addActionListener(SpriteEditor.allViewMenuActionListener);
				jcheckboxmenuitem12.setState(SpriteEditor.mapCrossOn);
				SpriteEditor.viewMenu.add(jcheckboxmenuitem);
				SpriteEditor.viewMenu.add(jcheckboxmenuitem1);
				SpriteEditor.viewMenu.add(jcheckboxmenuitem3);
				SpriteEditor.viewMenu.add(jcheckboxmenuitem4);
				SpriteEditor.viewMenu.add(jcheckboxmenuitem5);
				SpriteEditor.viewMenu.add(jcheckboxmenuitem2);
				SpriteEditor.viewMenu.add(jcheckboxmenuitem12);

				JMenuItem aboutMenuItem = new JMenuItem("查看...");
				aboutMenuItem.addActionListener(new AboutMenuActionListener());
				SpriteEditor.helpMenu.add(aboutMenuItem);

				for(int l1 = 0; l1 < SpriteEditor.zoomOptions.length; l1++)
				{
					JRadioButtonMenuItem jradiobuttonmenuitem = new JRadioButtonMenuItem("放大: " + SpriteEditor.zoomOptions[l1] + "x");
					if(SpriteEditor.zoomAlts[l1] > -1)
						jradiobuttonmenuitem.setAccelerator(KeyStroke.getKeyStroke(48 + SpriteEditor.zoomOptions[l1], 8));
					SpriteEditor.zoomGroup.add(jradiobuttonmenuitem);
					SpriteEditor.zoomMenu.setMnemonic('o');
					SpriteEditor.zoomMenu.add(jradiobuttonmenuitem);
					if(SpriteEditor.scale == SpriteEditor.zoomOptions[l1])
					{
						jradiobuttonmenuitem.setSelected(true);
						SpriteEditor.zoomMenu.setText("放大: " + SpriteEditor.zoomOptions[l1] + "x");
					}
					jradiobuttonmenuitem.setActionCommand("" + SpriteEditor.zoomOptions[l1]);
					jradiobuttonmenuitem.addActionListener(SpriteEditor.zoomMenuActionListener);
				}

				// 动画设置
				for(int l1 = 0; l1 < ConfigData.aniTypeList.length; l1++)
				{
					JRadioButtonMenuItem jradiobuttonmenuitem = new JRadioButtonMenuItem(ConfigData.aniTypeList[l1]);
					SpriteEditor.aniGroup.add(jradiobuttonmenuitem);
					SpriteEditor.aniMenu.add(jradiobuttonmenuitem);

					if(ConfigData.getAniType() == l1)
					{
						jradiobuttonmenuitem.setSelected(true);
					}
					jradiobuttonmenuitem.setActionCommand("" + l1);
					jradiobuttonmenuitem.addActionListener(SpriteEditor.AniMenuActionListener);
				}

				JMenu jmenu = new JMenu("膨胀");
				jmenu.setMnemonic('R');
				JMenuItem jmenuitem = new JMenuItem("/2");
				JMenuItem jmenuitem1 = new JMenuItem("2x");
				JMenuItem jmenuitem2 = new JMenuItem("%");
				jmenu.add(jmenuitem);
				jmenu.add(jmenuitem1);
				jmenu.add(jmenuitem2);
				jmenuitem.addActionListener(new HalfSizeActionListener());
				jmenuitem1.addActionListener(new DoubleSizeActionListener());
				jmenuitem2.addActionListener(new PercentSizeActionListener());
				final JMenuItem editMasksMenuItem = new JMenuItem("编辑");
				editMasksMenuItem.setMnemonic('E');
				editMasksMenuItem.setEnabled(SpriteEditor.frameFragmentMasks.isEnabled());
				editMasksMenuItem.addActionListener(new java.awt.event.ActionListener() {

					public void actionPerformed(java.awt.event.ActionEvent actionevent)
					{
						(new FrameFragmentMasksDialog()).createGui();
					}

				});
				SpriteEditor.useMasksMenuItem = new JCheckBoxMenuItem("使用");
				SpriteEditor.useMasksMenuItem.setMnemonic('U');
				SpriteEditor.useMasksMenuItem.setSelected(SpriteEditor.frameFragmentMasks.isEnabled());
				SpriteEditor.useMasksMenuItem.addActionListener(new java.awt.event.ActionListener() {

					public void actionPerformed(java.awt.event.ActionEvent actionevent)
					{
						boolean flag = SpriteEditor.useMasksMenuItem.isSelected();
						SpriteEditor.frameFragmentMasks.setEnabled(flag);
						editMasksMenuItem.setEnabled(flag);
						SpriteEditor.framePanel.masksComboBox.setEnabled(flag);
						SpriteEditor.framePanel.repaint();
						SpriteEditor.animationPanel.masksComboBox.setEnabled(flag);
						SpriteEditor.animationPanel.repaint();
					}

				});
				JMenu jmenu1 = new JMenu("隐藏");
				jmenu1.setMnemonic('M');

				jmenu1.add(SpriteEditor.useMasksMenuItem);

				jmenu1.add(editMasksMenuItem);
				SpriteEditor.menuBar.add(SpriteEditor.fileMenu);
				SpriteEditor.menuBar.add(SpriteEditor.zoomMenu);
				SpriteEditor.menuBar.add(SpriteEditor.viewMenu);
				SpriteEditor.menuBar.add(jmenu);
				SpriteEditor.menuBar.add(aniMenu);

				// TODO,暂时不使用隐藏.................
				// SpriteEditor.menuBar.add(jmenu1);

				// daben:add
				// 添加菜单“测试”-> "在模拟器中运行"
				SpriteEditor.runInSimulatorMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0));

				RunInSimulatorActionListener runInSimulatorActionListener = new RunInSimulatorActionListener();
				SpriteEditor.runInSimulatorMenuItem.addActionListener(runInSimulatorActionListener);
				SpriteEditor.convertMenuItem.addActionListener(runInSimulatorActionListener);
				SpriteEditor.setDemoSpriteMenuItem.addActionListener(runInSimulatorActionListener);
				SpriteEditor.testMenu.add(SpriteEditor.convertMenuItem);
				// SpriteEditor.testMenu.add(SpriteEditor.setDemoSpriteMenuItem);
				SpriteEditor.testMenu.add(SpriteEditor.runInSimulatorMenuItem);
				SpriteEditor.menuBar.add(SpriteEditor.testMenu);

				// fay: 工具(T) 2008-10-08
				JMenu toolMenu = new JMenu("工具(T)");
				toolMenu.setMnemonic('T');
				JMenuItem deleteUnuseFrameItem = new JMenuItem("删除无用的帧(D)");
				deleteUnuseFrameItem.setMnemonic('D');
				deleteUnuseFrameItem.addActionListener(new DeleteUnuseFrameHandler());
				toolMenu.add(deleteUnuseFrameItem);
				
				// 2009-12-17 计算内存占用
				JMenuItem calculateMenoryItem = new JMenuItem("计算内存占用");
				calculateMenoryItem.setMnemonic('C');
				calculateMenoryItem.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e)
					{
						if(sprite != null)
						{
							JOptionPane.showMessageDialog(mainFrame, sprite.getCaculateMsg());
						}else
						{
							JOptionPane.showMessageDialog(mainFrame, "未加载精灵!");
						}
					}
				});
				toolMenu.add(calculateMenoryItem);
				
				SpriteEditor.menuBar.add(toolMenu);

				SpriteEditor.menuBar.add(helpMenu);

				// ~daben
				SpriteEditor.mainFrame.setJMenuBar(SpriteEditor.menuBar);
				SpriteEditor.mainFrame.addWindowListener(new java.awt.event.WindowAdapter() {

					public void windowClosing(java.awt.event.WindowEvent windowevent)
					{
						// daben:add
						// 退出时保存xml
						if(!SpriteEditor.isAllSaved())
							return;
						// ~daben

						SpriteEditor.exit();
					}
				});
				SpriteEditor.allFragmentPopup = new JPopupMenu();
				SpriteEditor.allFragmentPopup.add(SpriteEditor.addToFrameMenuItem);
				SpriteEditor.addToFrameMenuItem.addActionListener(new FragmentAddToFrameActionListener());
				SpriteEditor.addToFrameMenuItem.setAccelerator(KeyStroke.getKeyStroke(10, 0));
				FrameFragmentPopUpMenuActionListener framefragmentpopupmenuactionlistener = new FrameFragmentPopUpMenuActionListener(SpriteEditor.frameFragmentsListCommander);
				SpriteEditor.moveTopFrameMenuItem.addActionListener(framefragmentpopupmenuactionlistener);
				SpriteEditor.moveTopFrameMenuItem.setAccelerator(KeyStroke.getKeyStroke(36, 8));
				SpriteEditor.moveUpFromFrameMenuItem.addActionListener(framefragmentpopupmenuactionlistener);
				SpriteEditor.moveUpFromFrameMenuItem.setAccelerator(KeyStroke.getKeyStroke(38, 8));
				SpriteEditor.moveDownFromFrameMenuItem.addActionListener(framefragmentpopupmenuactionlistener);
				SpriteEditor.moveDownFromFrameMenuItem.setAccelerator(KeyStroke.getKeyStroke(40, 8));
				SpriteEditor.moveBottomFromFrameMenuItem.addActionListener(framefragmentpopupmenuactionlistener);
				SpriteEditor.moveBottomFromFrameMenuItem.setAccelerator(KeyStroke.getKeyStroke(35, 8));
				SpriteEditor.deleteFromFrameMenuItem.addActionListener(framefragmentpopupmenuactionlistener);
				SpriteEditor.deleteFromFrameMenuItem.setAccelerator(KeyStroke.getKeyStroke(127, 0));
				SpriteEditor.showHideFrameMenuItem.addActionListener(framefragmentpopupmenuactionlistener);
				SpriteEditor.showHideFrameMenuItem.setAccelerator(KeyStroke.getKeyStroke(10, 0));
				// daben:add
				// 增加 切片 翻转功能菜单
				SpriteEditor.flipXTransformMenuItem.addActionListener(framefragmentpopupmenuactionlistener);
				SpriteEditor.flipXTransformMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, 0));
				SpriteEditor.flipYTransformMenuItem.addActionListener(framefragmentpopupmenuactionlistener);
				SpriteEditor.flipYTransformMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, 0));
				SpriteEditor.rotate90TransformMenuItem.addActionListener(framefragmentpopupmenuactionlistener);
				SpriteEditor.rotate90TransformMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, 0));
				SpriteEditor.rotate180TransformMenuItem.addActionListener(framefragmentpopupmenuactionlistener);
				SpriteEditor.rotate180TransformMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4, 0));
				SpriteEditor.rotate270TransformMenuItem.addActionListener(framefragmentpopupmenuactionlistener);
				SpriteEditor.rotate270TransformMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_5, 0));
				SpriteEditor.flipXYTransformMenuItem.addActionListener(framefragmentpopupmenuactionlistener);
				SpriteEditor.flipXYTransformMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_6, 0));
				SpriteEditor.flipMXYTransformMenuItem.addActionListener(framefragmentpopupmenuactionlistener);
				SpriteEditor.flipMXYTransformMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_7, 0));
				SpriteEditor.noTransformMenuItem.addActionListener(framefragmentpopupmenuactionlistener);
				SpriteEditor.noTransformMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_0, 0));
				// ~daben
				SpriteEditor.copyFragmentFromFrameMenuItem.addActionListener(framefragmentpopupmenuactionlistener);
				SpriteEditor.copyFragmentFromFrameMenuItem.setAccelerator(KeyStroke.getKeyStroke(67, 2));
				SpriteEditor.pasteFragmentToFrameMenuItem.addActionListener(framefragmentpopupmenuactionlistener);
				SpriteEditor.pasteFragmentToFrameMenuItem.setAccelerator(KeyStroke.getKeyStroke(86, 2));
				// daben:add
				SpriteEditor.undoFrameMenuItem.addActionListener(framefragmentpopupmenuactionlistener);
				SpriteEditor.undoFrameMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, 2));
				// ~daben
				SpriteEditor.frameFragmentPopup.add(SpriteEditor.moveTopFrameMenuItem);
				SpriteEditor.frameFragmentPopup.add(SpriteEditor.moveUpFromFrameMenuItem);
				SpriteEditor.frameFragmentPopup.add(SpriteEditor.moveDownFromFrameMenuItem);
				SpriteEditor.frameFragmentPopup.add(SpriteEditor.moveBottomFromFrameMenuItem);
				SpriteEditor.frameFragmentPopup.addSeparator();
				SpriteEditor.frameFragmentPopup.add(SpriteEditor.deleteFromFrameMenuItem);
				SpriteEditor.frameFragmentPopup.addSeparator();
				SpriteEditor.frameFragmentPopup.add(SpriteEditor.showHideFrameMenuItem);
				// daben:add
				// 切片块翻转
				SpriteEditor.frameFragmentPopup.add(SpriteEditor.transformFrameFragmentMenu);
				SpriteEditor.transformFrameFragmentMenu.add(SpriteEditor.flipXTransformMenuItem);
				SpriteEditor.transformFrameFragmentMenu.add(SpriteEditor.flipYTransformMenuItem);
				SpriteEditor.transformFrameFragmentMenu.add(SpriteEditor.rotate90TransformMenuItem);
				SpriteEditor.transformFrameFragmentMenu.add(SpriteEditor.rotate180TransformMenuItem);
				SpriteEditor.transformFrameFragmentMenu.add(SpriteEditor.rotate270TransformMenuItem);

				// SpriteEditor.transformFrameFragmentMenu.add(SpriteEditor.flipXYTransformMenuItem);
				// SpriteEditor.transformFrameFragmentMenu.add(SpriteEditor.flipMXYTransformMenuItem);

				SpriteEditor.transformFrameFragmentMenu.addSeparator();
				SpriteEditor.transformFrameFragmentMenu.add(SpriteEditor.noTransformMenuItem);
				// ~daben
				SpriteEditor.frameFragmentPopup.addSeparator();
				SpriteEditor.frameFragmentPopup.add(SpriteEditor.copyFragmentFromFrameMenuItem);
				SpriteEditor.frameFragmentPopup.add(SpriteEditor.pasteFragmentToFrameMenuItem);
				// daben:add
				// 在 帧切片 列表 里 增加 “撤消”功能
				SpriteEditor.frameFragmentPopup.addSeparator();
				SpriteEditor.frameFragmentPopup.add(SpriteEditor.undoFrameMenuItem);
				// ~daben
				SpriteEditor.mainTabbedPanel = new JTabbedPane(1);
				SpriteEditor.mainTabbedPanel.add("切片区", SpriteEditor.fragmentPanel);
				SpriteEditor.mainTabbedPanel.add("帧编辑区", SpriteEditor.framePanel);
				SpriteEditor.mainTabbedPanel.add("动画编辑区", SpriteEditor.animationPanel);
				SpriteEditor.mainTabbedPanel.add("变装编辑区", SpriteEditor.avatarPanel);
				SpriteEditor.mainTabbedPanel.addChangeListener(new ChangeListener() {

					public void stateChanged(ChangeEvent changeevent)
					{
						SpriteEditor.centerScrollPane(SpriteEditor.framePanel.frameEditScrollPanel);
						SpriteEditor.centerScrollPane(SpriteEditor.avatarPanel.frameEditScrollPanel);
					}

				});
				SpriteEditor.mainFrame.getContentPane().add(mainTabbedPanel, "Center");
				SpriteEditor.statusBar = new JLabel(TITLE);
				SpriteEditor.statusBar.setBorder(BorderFactory.createEtchedBorder());
				SpriteEditor.mainFrame.getContentPane().add(SpriteEditor.statusBar, "South");
				SpriteEditor.mainFrame.pack();
				SpriteEditor.mainFrame.setVisible(true);
				SpriteEditor.mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				BoundsSaver boundssaver = new BoundsSaver(SpriteEditor.mainFrame, SpriteEditor.preferences.node("main_window"));
				boundssaver.initiateComponentBounds(0, 0, SpriteEditor.initWidth, SpriteEditor.initHeight);

				SpriteEditor.history.createRecentMenu();
				SpriteEditor.mainFrame.setVisible(true);
				SpriteEditor.zoomMenuActionListener.actionPerformed(new java.awt.event.ActionEvent(this, 0, "" + SpriteEditor.scale));
			}

		});
	}

	// daben:add
	public static boolean isAllSaved()
	{
		if(!isFragmentSaved())
		{
			return false;
		}
		return isAnimationSaved();
	}

	public static boolean isFragmentSaved()
	{
		if(!SpriteEditor.fragmentsSaved)
		{
			int result = JOptionPane.showConfirmDialog(SpriteEditor.mainFrame, "切片编辑完未保存 ! \n确定不保存吗 ?", "警告", JOptionPane.OK_CANCEL_OPTION);
			if(result != JOptionPane.OK_OPTION)
			{
				return false;
			}else
			{
				SpriteEditor.fragmentsSaved = true;
			}
		}

		return true;
	}

	public static boolean isAnimationSaved()
	{
		if(!SpriteEditor.animationsSaved)
		{
			int result = JOptionPane.showConfirmDialog(SpriteEditor.mainFrame, "帧或者动画编辑完未保存 ! \n确定不保存吗 ?", "警告", JOptionPane.OK_CANCEL_OPTION);
			if(result != JOptionPane.OK_OPTION)
				return false;
			else
				SpriteEditor.animationsSaved = true;

		}

		return true;
	}

	// ~daben

	public static void exit()
	{
		savePrefs();
		try
		{
			if(xmlSavePath != null)
				spriteConf.storeToFile(new File(xmlSavePath + PropertyIO.defExpandPropertyName));
		}catch(IOException ioexception)
		{
			System.err.println("Error saving configuration " + ioexception.getLocalizedMessage());
		}
		System.exit(-1);
	}

	public static void loadPrefs()
	{
		ConfigData.setAniType(preferences.getInt("aniType", 0));
		scale = preferences.getInt("zoom", zoomOptions[zoomOptions.length / 2]);
		gridOn = preferences.getBoolean("gridOn", gridOn);
		crossOn = preferences.getBoolean("crossOn", crossOn);
		framesetOn = preferences.getBoolean("framesetOn", framesetOn);
		allFrameOn = preferences.getBoolean("allFrameOn", allFrameOn);
		framesetFrameOn = preferences.getBoolean("framesetFrameOn", framesetFrameOn);
		collisionOn = preferences.getBoolean("collisionOn", collisionOn);
		mapCrossOn = preferences.getBoolean("mapCrossOn", mapCrossOn);

	}

	private static void savePrefs()
	{
		preferences.putInt("aniType", ConfigData.getAniType());
		preferences.putInt("zoom", scale);
		preferences.putBoolean("gridOn", gridOn);
		preferences.putBoolean("crossOn", crossOn);
		preferences.putBoolean("framesetOn", framesetOn);
		preferences.putBoolean("allFrameOn", allFrameOn);
		preferences.putBoolean("framesetFrameOn", framesetFrameOn);
		preferences.putBoolean("collisionOn", collisionOn);
		preferences.putBoolean("mapCrossOn", mapCrossOn);
	}

	public static String addNull(int i, int j)
	{
		String s = "";
		int k = (int) Math.pow(10D, j - 1);
		for(int l = i != 0 ? i : 1; l < k; k /= 10)
			s = s + "0";

		s = s + i;
		return s;
	}

	public static void centerScrollBar(JScrollBar jscrollbar)
	{

		int i = (jscrollbar.getMaximum() - jscrollbar.getVisibleAmount()) / 2;
		jscrollbar.setValue(i);
	}

	public static void centerScrollPane(JScrollPane scrollPane)
	{
		centerScrollBar(scrollPane.getHorizontalScrollBar());
		centerScrollBar(scrollPane.getVerticalScrollBar());
	}

	public static void setStatus(String s)
	{
		statusBar.setText(s);
	}

	/**
	 * 显示错误对话框信息
	 * 
	 * @param title
	 * @param msg
	 */
	public static void showError(String title, String msg)
	{
		JOptionPane.showMessageDialog(mainFrame, msg, title, JOptionPane.ERROR_MESSAGE);
	}

	// 版本号
	public static final int majorVersion = 3;
	public static final int minorVersion = 0;

	// TODO,版本号
	public static final int buildNumber = 1;

	public static String getVersion()
	{
		return majorVersion + "." + minorVersion + "." + buildNumber;
	}

	// 版本号只要在这里修改即可
	public static final String TITLE = "SpriteEditor(iEdit) - v" + majorVersion + "." + minorVersion + "." + buildNumber;

	// xml格式版本
	public static final int formatMajorVersion = 1;
	public static final int formatMinorVersion = 0;

	public static final long SPRITE_FEATURE_USE_FRAME_MASKS = 1L;
	public static final boolean SWAP_IN_XML = false;
	public static final int FRAME_ICON_HEIGHT = 48;
	public static String xmlSavePath = null;
	public static String xmlSpecSavePath = null;
	public static int initWidth = 1200;
	public static final int DEFAULT_LIST_WIDTH = 140;
	public static int initHeight = 800;
	public static String backgroundImagePath;
	public static int bgndX = 0;
	public static int bgndY = 0;
	public static String backgroundFramesImagePath;
	public static int bgndFramesX = 0;
	public static int bgndFramesY = 0;
	public static Background framesBackground = new Background();
	public static Background gesturesBackground = new Background();
	public static int scale = 1;
	public static boolean gridOn = false;
	public static boolean crossOn = false;
	public static boolean framesetOn = true;
	public static boolean allFrameOn = true;
	public static boolean framesetFrameOn = true;
	public static boolean collisionOn = false;
	public static boolean mapCrossOn = false; // 可通行性
	public static int zoomOptions[] = { 1, 2, 3, 4, 5, 6, 7, 8, 10, 15, 20, 30 };
	public static int zoomAlts[] = { 1, 2, 3, 4, 5, 6, 7, 8, -1, -1, -1, -1 };
	public static final int FPS_MIN = 1;
	public static final int FPS_MAX = 10;
	public static final int FPS_INIT = 5;
	public static final int LOOP_MIN = 0;
	public static final int LOOP_MAX = 10;
	public static final int LOOP_INIT = 0;
	public static final int DUR_MIN = 1;
	public static final int DUR_MAX = 20;
	public static final int DUR_INIT = 1;
	public static final int SPD_MIN = -20;
	public static final int SPD_MAX = 20;
	public static final int SPD_INIT_X = 1;
	public static final int SPD_INIT_Y = 0;

	public static Sprite sprite;
	/** 动画编辑区 */
	public static AnimationPanel animationPanel;
	/** 切片编辑区 */
	public static FragmentPanel fragmentPanel;
	/** 帧编辑区 */
	public static FramePanel framePanel;

	/** 2009-11-03 变装编辑区 */
	public static AvatarPanel avatarPanel;

	public static JFrame mainFrame = new JFrame(TITLE);
	private static JFileChooser fileChooser;
	private static JFileChooser dirChooser;
	public static Preferences preferences = Preferences.userRoot().node("iedit2");
	public static PropertyIO spriteConf = new PropertyIO();
	public static ClipBoard fragmentsClipBoard = new ClipBoard();
	public static java.awt.Rectangle collisionClipboard;
	public static FrameFragmentsListCommander frameFragmentsListCommander = new FrameFragmentsListCommander();
	public static FrameFragmentMasks frameFragmentMasks = new FrameFragmentMasks();
	public static JCheckBoxMenuItem useMasksMenuItem;

	private static JMenu helpMenu = new JMenu("帮助(H)");

	/**
	 * 显示当前操作精灵文件信息
	 */
	private static JLabel statusBar;

	public static FileFilter xmlFileFilter = new XMLFileFilter();
	public static FileFilter pngFileFilter = new PNGFileFilter();
	public static FileFilter binFileFilter = new BinFileFilter();
	public static FileFilter whateverFileFilter = new WhateverFileFilter();
	public static FileFilter cnfFileFilter = new CnfFileFilter();
	public static JMenuBar menuBar = new JMenuBar();
	public static JMenu fileMenu = new JMenu("文件");
	public static JMenu zoomMenu = new JMenu("缩放");
	public static JMenu viewMenu = new JMenu("显示");
	// daben:add
	public static JMenu testMenu = new JMenu("测试");

	public static JMenu aniMenu = new JMenu("动画设置");

	public static JMenuItem runInSimulatorMenuItem = new JMenuItem("在模拟器中运行");
	public static JMenuItem setDemoSpriteMenuItem = new JMenuItem("设为demo精灵");
	public static JMenuItem convertMenuItem = new JMenuItem("精灵打包");
	// ~daben
	public static ButtonGroup zoomGroup = new ButtonGroup();
	public static ButtonGroup aniGroup = new ButtonGroup();
	public static JMenuItem addToFrameMenuItem = new JMenuItem("添加到帧");

	/** 增加到帧 */
	public static JPopupMenu allFragmentPopup;
	public static JPopupMenu frameFragmentPopup = new JPopupMenu();
	public static JMenuItem moveTopFrameMenuItem = new JMenuItem("置顶");
	public static JMenuItem moveUpFromFrameMenuItem = new JMenuItem("上移");
	public static JMenuItem moveDownFromFrameMenuItem = new JMenuItem("下移");
	public static JMenuItem moveBottomFromFrameMenuItem = new JMenuItem("置底");
	public static JMenuItem deleteFromFrameMenuItem = new JMenuItem("删除");
	public static JMenuItem showHideFrameMenuItem = new JMenuItem("隐藏");
	// daben:add
	public static JMenu transformFrameFragmentMenu = new JMenu("翻转");
	public static JMenuItem flipXTransformMenuItem = new JMenuItem("水平翻转");
	public static JMenuItem flipYTransformMenuItem = new JMenuItem("垂直翻转");
	public static JMenuItem rotate90TransformMenuItem = new JMenuItem("旋转90度");
	public static JMenuItem rotate180TransformMenuItem = new JMenuItem("旋转180度");
	public static JMenuItem rotate270TransformMenuItem = new JMenuItem("旋转270度");
	public static JMenuItem flipXYTransformMenuItem = new JMenuItem("关于Y=X翻转");
	public static JMenuItem flipMXYTransformMenuItem = new JMenuItem("关于Y=-X翻转");
	public static JMenuItem noTransformMenuItem = new JMenuItem("还原");
	// ~daben
	public static JMenuItem copyFragmentFromFrameMenuItem = new JMenuItem("复制");
	public static JMenuItem pasteFragmentToFrameMenuItem = new JMenuItem("粘贴");
	// daben:add
	public static JMenuItem undoFrameMenuItem = new JMenuItem("撤消");
	public static JMenuItem itemNewProject = new JMenuItem("新建工程");
	// ~daben
	public static JMenuItem itemOpenXML = new JMenuItem("打开XML");
	public static JMenuItem itemSaveAsXML = new JMenuItem("另存为");
	public static JMenuItem itemReload = new JMenuItem("重新加载");
	public static JMenuItem itemLoadConf = new JMenuItem("加载配置文件");
	public static JMenu historyMenu;
	public static JMenuItem itemExportAnims = new JMenuItem("导出动画");
	public static JMenuItem itemConvertXMLs = new JMenuItem("Convert directory");
	private static History history;
	public static JMenuItem itemSaveXML = new JMenuItem("保存XML");
	public static JMenuItem itemExit = new JMenuItem("退出");
	private static java.awt.event.ActionListener zoomMenuActionListener = new ZoomMenuActionListener();
	private static java.awt.event.ActionListener AniMenuActionListener = new AniMenuActionListener();
	private static java.awt.event.ActionListener allViewMenuActionListener = new AllViewMenuActionListener();
	public static final String OPTIONS[] = { "-h help", "-v[x][f] verbose x - xml in/out, f file utils relative path generating", "-i imagepath (-ic://img/image.png)", "-b background imagepath (-bc://img/backimage.png)", "-bx background image x-position", "-by background image y-position",
			"-bf background frames imagepath (-bfc://img/backimage.png)", "-bfx background frames image x-position", "-bfy background frames image y-position", "-s source native XML", "-t target native XML", "-tspec target spec XML", "-tbin target binary", "-ttbin target tile country binary",
			"-a[x][s][b] no UI actionr XML,SPEC XML, bin}" };

	// daben:add
	public static JTabbedPane mainTabbedPanel;
	// 切片区是否已经保存
	public static boolean fragmentsSaved = true;
	// 帧编辑区和动画编辑区是否已经保存
	public static boolean animationsSaved = true;
	// ~daben

	static
	{
		historyMenu = new JMenu("历史记录");
		history = new History(historyMenu);
	}

}
