package cz.ismar.projects.IEdit.structure;

import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.io.InOut;
import cz.ismar.projects.IEdit.io.StoringException;
import java.io.File;
import java.util.*;

import org.w3c.dom.*;

/**
 * 将工程数据用xml生成
 * 
 * @author Lori
 * 
 */
public class Sprite implements Rescalable, InOutAble
{

	public void setExportFile(File file)
	{
		xmlFile = file;
	}

	/**
	 * 整理 动画文件用到的 切片xml文件...
	 * 
	 * 自动删除 没有用到过的切片xml文件
	 */
	public void tidyFragmentHoldersList()
	{
		ArrayList arraylist = new ArrayList();
		int size = fragmentHoldersList.getSize();
		for(int i = 0; i < size; i++)
		{
			FragmentHolder fragmentholder = (FragmentHolder) fragmentHoldersList.getElementAt(i);
			Frame aframe[] = countFromAllFrames(fragmentholder);

			// 自动删除 没有用到过的切片xml文件
			if(aframe == null || aframe.length <= 0)
			{
				arraylist.add(fragmentholder);
			}

		}
		fragmentHoldersList.remove(arraylist);
		SpriteEditor.avatarPanel.initFragmentFiles();

		SpriteEditor.framePanel.initFragments();
		SpriteEditor.framePanel.repaint();
	}

	// ----------------------------------------------------------------------------------------//
	// 打包生成xml的总出口 //
	// ----------------------------------------------------------------------------------------//
	public Element output(Document document) throws StoringException
	{
		Element element = document.createElement("sprite");
		StringBuffer stringbuffer = new StringBuffer();
		stringbuffer.append("\n");
		stringbuffer.append("writed by iEdit ");
		stringbuffer.append(SpriteEditor.majorVersion);
		stringbuffer.append(".");
		stringbuffer.append(SpriteEditor.minorVersion);
		stringbuffer.append(".");
		stringbuffer.append(SpriteEditor.buildNumber);
		stringbuffer.append("\n");
		stringbuffer.append("sprite format version ");
		stringbuffer.append(SpriteEditor.formatMajorVersion);
		stringbuffer.append(".");
		stringbuffer.append(SpriteEditor.formatMinorVersion);
		stringbuffer.append("\n");
		stringbuffer.append("used features:\n");
		long maskType = 0L;
		if(SpriteEditor.frameFragmentMasks.isEnabled())
		{
			maskType |= 1L;
			stringbuffer.append(" SPRITE_FEATURE_USE_FRAME_MASKS = ");
			stringbuffer.append(1L);
			stringbuffer.append("\n");
		}

		// 加入子选项
		element.appendChild(document.createComment(stringbuffer.toString()));

		// 精灵信息
		Element element1 = document.createElement("sprite_info");
		element1.setAttribute("majorVersion_short_r0", Integer.toString(SpriteEditor.formatMajorVersion));
		element1.setAttribute("minorVersion_short_r1", Integer.toString(SpriteEditor.formatMinorVersion));
		// 隐藏类型
		element1.setAttribute("features_long_r2", Long.toString(maskType));
		element.appendChild(element1);

		// 切片
		Element element2 = fragmentHoldersList.output(document);
		element.appendChild(element2);

		// 帧
		Element element3 = frameHolder.output(document);
		element.appendChild(element3);

		// 动画
		Element element4 = document.createElement("animations_arr");
		element.appendChild(element4);
		Element element5;
		for(Iterator<Animation> iterator = animations.iterator(); iterator.hasNext(); element4.appendChild(element5))
		{
			Animation animation = (Animation) iterator.next();
			element5 = animation.output(document);
		}

		// 隐藏属性输出 设置【屏蔽】
		// element.appendChild(SpriteEditor.frameFragmentMasks.output(document));

		// 翻转属性输出 设置
		// element.appendChild(frameHolder.outputTransform(document));

		return element;
	}

	public void input(Element element)
	{
		boolean flag = false;
		NodeList nodelist = element.getElementsByTagName("sprite_info");
		if(nodelist.getLength() == 1)
		{
			Element element1 = (Element) nodelist.item(0);
			element1.getAttribute("majorVersion_short_r0");
			element1.getAttribute("minorVersion_short_r1");
			long l = Long.decode(element1.getAttribute("features_long_r2")).longValue();
			if((l & 1L) != 0L)
			{
				flag = true;
			}
		}

		// 切片列表
		SpriteEditor.frameFragmentMasks.setEnabled(flag);
		if(!InOut.compatibilityInput)
		{
			fragmentHoldersList.input(element);
		}else
		{
			fragmentHoldersList.add(fragmentHolder);
			fragmentHoldersList.defragment();
		}

		// 帧切片
		NodeList nodelist1 = element.getElementsByTagName(InOut.compatibilityInput ? "frames" : "frames_arr");
		if(nodelist1.getLength() == 1)
		{
			Element element2 = (Element) nodelist1.item(0);
			frameHolder.input(element2);
		}else if(InOut.verbose)
		{
			System.out.println("Project.input NO FRAMES");
		}

		// 动画
		NodeList nodelist2 = element.getElementsByTagName(InOut.compatibilityInput ? "animations" : "animations_arr");
		if(InOut.verbose)
			System.out.println("Project.input GOT ANIMATIONS:" + nodelist2.getLength());
		if(nodelist2.getLength() == 1)
		{
			Element element3 = (Element) nodelist2.item(0);
			NodeList nodelist3 = element3.getElementsByTagName(InOut.compatibilityInput ? "animation" : "animation_arr");
			for(int i = 0; i < nodelist3.getLength(); i++)
			{
				Element element4 = (Element) nodelist3.item(i);
				Animation animation = new Animation(this);
				animation.input(element4);
				add(animation);
			}

		}else if(InOut.verbose)
			System.out.println("Project.input GOT NO ANIMATIONS");

		// [屏蔽隐藏属性的读入]
		// SpriteEditor.frameFragmentMasks.input(element);

		// FrameHolder.inputTransform(element, frameHolder);
	}

	public Sprite(String s)
	{
		index = -1;
		fragmentHoldersList = new FragmentHoldersList();
		fragmentHolder = new FragmentHolder();
		frameHolder = new FrameHolder(this);
		xmlFile = null;
		animations = new ArrayList<Animation>();
	}

	public void rescaleTimes(float f)
	{
		fragmentHolder.rescaleTimes(f);
		fragmentHoldersList.getAllFragments().rescaleTimes(f);
		frameHolder.rescaleTimes(f);
		Animation animation;
		for(Iterator iterator = animations.iterator(); iterator.hasNext(); animation.rescaleTimes(f))
		{
			animation = (Animation) iterator.next();
		}
	}

	public Animation remove(int i)
	{
		SpriteEditor.animationsSaved = false;
		return (Animation) animations.remove(i);
	}

	public void add(Animation animation)
	{
		animations.add(animation);
		index = animations.size() - 1;

		SpriteEditor.animationsSaved = false;
	}

	public void insert(Animation animation)
	{
		if(index == -1)
		{
			index = 0;
			animations.add(animation);
		}else
		{
			List list = animations;
			animations = new ArrayList(animations.size() + 1);
			animations.addAll(list.subList(0, index + 1));
			animations.add(animation);
			if(index + 1 < list.size())
				animations.addAll(list.subList(index + 1, list.size()));
			index++;
		}

		SpriteEditor.animationsSaved = false;
	}

	public int size()
	{
		return animations.size();
	}

	public boolean movePrev()
	{
		if(index > 0)
		{
			index--;
			return true;
		}else
		{
			return false;
		}
	}

	public boolean moveNext()
	{
		if(index < animations.size() - 1)
		{
			index++;
			return true;
		}else
		{
			return false;
		}
	}

	public boolean remove()
	{
		if(animations.size() > 0)
		{
			animations.remove(index);
			if(index > 0)
				index--;
			// daben:add
			if(SpriteEditor.animationsSaved)
				SpriteEditor.animationsSaved = false;
			// ~daben
			return true;
		}else
		{
			return false;
		}
	}

	public boolean isFirst()
	{
		return index == 0;
	}

	public boolean isLast()
	{
		return index == animations.size() - 1;
	}

	public Animation getCurrentAnimation()
	{
		if(index == -1 || animations.size() == 0)
			return null;
		else
			return (Animation) animations.get(index);
	}

	public ArrayList<Animation> getAnimations()
	{
		return animations;
	}

	public Animation[] getAnimationsContainingFrame(Frame frame)
	{
		ArrayList<Animation> arraylist = new ArrayList<Animation>();
		Iterator<Animation> iterator = animations.iterator();
		do
		{
			if(!iterator.hasNext())
			{
				break;
			}
			Animation animation = (Animation) iterator.next();
			if(animation.contains(frame))
			{
				arraylist.add(animation);
			}
		}while(true);
		return (Animation[]) arraylist.toArray(new Animation[0]);
	}

	public Animation[] getAnimationsContainingGesture(Gesture gesture)
	{
		ArrayList arraylist = new ArrayList();
		Iterator iterator = animations.iterator();
		do
		{
			if(!iterator.hasNext())
				break;
			Animation animation = (Animation) iterator.next();
			if(animation.contains(gesture))
				arraylist.add(animation);
		}while(true);
		return (Animation[]) arraylist.toArray(new Animation[0]);
	}

	public Frame[] getFramesContainingFragment(Fragment fragment)
	{
		ArrayList arraylist = new ArrayList();
		Iterator iterator = frameHolder.getFrames().iterator();
		do
		{
			if(!iterator.hasNext())
				break;
			Frame frame = (Frame) iterator.next();
			if(frame.contains(fragment))
				arraylist.add(frame);
		}while(true);
		return (Frame[]) arraylist.toArray(new Frame[0]);
	}

	public Animation[] removeFromAllAnimations(Frame frame)
	{
		ArrayList arraylist = new ArrayList();
		Iterator iterator = animations.iterator();
		do
		{
			if(!iterator.hasNext())
				break;
			Animation animation = (Animation) iterator.next();
			if(animation.contains(frame))
			{
				animation.remove(frame);
				arraylist.add(animation);
			}
		}while(true);
		return (Animation[]) arraylist.toArray(new Animation[0]);
	}

	public Animation[] removeFromAllAnimations(Gesture gesture)
	{
		ArrayList arraylist = new ArrayList();
		Iterator iterator = animations.iterator();
		do
		{
			if(!iterator.hasNext())
				break;
			Animation animation = (Animation) iterator.next();
			if(animation.contains(gesture))
			{
				animation.remove(gesture);
				arraylist.add(animation);
			}
		}while(true);
		return (Animation[]) arraylist.toArray(new Animation[0]);
	}

	public Frame[] removeFromAllFrames(FragmentHolder fragmentHolder, boolean isRemove)
	{
		ArrayList<Frame> arraylist = new ArrayList<Frame>();
		Iterator<Fragment> iterator = fragmentHolder.getFragments().iterator();
		do
		{
			if(!iterator.hasNext())
				break;

			Frame aframe[] = removeFromAllFrames(iterator.next(), isRemove);
			if(aframe != null && aframe.length > 0)
			{
				arraylist.addAll(Arrays.asList(aframe));
			}
		}while(true);

		return arraylist.toArray(new Frame[0]);
	}

	public Frame[] removeFromAllFrames(FragmentHolder fragmentholder)
	{
		return removeFromAllFrames(fragmentholder, true);
	}

	public Frame[] countFromAllFrames(FragmentHolder fragmentholder)
	{
		return removeFromAllFrames(fragmentholder, false);
	}

	public Frame[] removeFromAllFrames(Fragment fragment, boolean isRemove)
	{
		ArrayList arraylist = new ArrayList();
		Iterator iterator = frameHolder.getFrames().iterator();
		do
		{
			if(!iterator.hasNext())
				break;
			Frame frame = (Frame) iterator.next();
			if(frame.contains(fragment))
			{
				if(isRemove)
					frame.remove(fragment);

				arraylist.add(frame);
			}
		}while(true);
		return (Frame[]) arraylist.toArray(new Frame[0]);
	}

	public void setIndex(int i)
	{
		index = i;
	}

	public String getSpriteImgPath()
	{
		return spriteImgPath;
	}

	public void setSpriteImgPath(String s)
	{
		spriteImgPath = s;
	}

	public boolean moveTop()
	{
		if(index > 0)
		{
			animations.add(0, animations.remove(index));
			index = 0;
			// daben:add
			if(SpriteEditor.animationsSaved)
				SpriteEditor.animationsSaved = false;
			// ~daben
			return true;
		}else
		{
			return false;
		}
	}

	public boolean moveUp()
	{
		if(index > 0)
		{
			animations.add(index - 1, animations.remove(index));
			index--;
			// daben:add
			if(SpriteEditor.animationsSaved)
				SpriteEditor.animationsSaved = false;
			// ~daben
			return true;
		}else
		{
			return false;
		}
	}

	public boolean moveDown()
	{
		if(index < animations.size() - 1)
		{
			animations.add(index + 1, animations.remove(index));
			index++;
			// daben:add
			if(SpriteEditor.animationsSaved)
				SpriteEditor.animationsSaved = false;
			// ~daben
			return true;
		}else
		{
			return false;
		}
	}

	public boolean moveBottom()
	{
		if(index < animations.size() - 1)
		{
			animations.add(animations.remove(index));
			index = animations.size() - 1;
			// daben:add
			if(SpriteEditor.animationsSaved)
				SpriteEditor.animationsSaved = false;
			// ~daben
			return true;
		}else
		{
			return false;
		}
	}

	public int getIndex()
	{
		return index;
	}

	/**
	 * 初始化all fragment 的图
	 */
	public void initFragmentIcons()
	{
		ArrayList<Fragment> list = fragmentHoldersList.getAllFragments().getFragments();
		// 初始化所有切片图
		for(Fragment fragment : list)
		{
			fragment.updateImage();
		}
	}

	/**
	 * 初始化Frame Fragment
	 */
	public void initFrameFragmentIcons()
	{
		ArrayList<Frame> list = frameHolder.getFrames();
		for(Frame frame : list)
		{
			for(FrameFragment frameFragment : frame.getFrameFragments())
			{
				frameFragment.updateImage();
			}
		}

		// for(Iterator iterator = list.iterator(); iterator.hasNext();)
		// {
		// List list2 = ((Frame) iterator.next()).getFrameFragments();
		// for(Iterator iterator2 = list2.iterator(); iterator2.hasNext();
		// ((FrameFragment) iterator2.next()).updateImage())
		// ;
		// }
	}

	/**
	 * 初始化Frames的图
	 */
	public void initFrameIcons()
	{
		ArrayList<Frame> frames = frameHolder.getFrames();
		for(Frame frame : frames)
		{
			frame.updateImage();
		}
	}

	/**
	 * 根据当前的FragmentHolder, 刷新所有Frame
	 */
	public void refresh()
	{
		// 更新 all Fragment
		fragmentHoldersList.refreshAllFramgment();
		// 初始化 all Fragment 的image
		initFragmentIcons();

		// 更新至FrameFragment
		frameHolder.refresh();

		// 帧(Frame)
		// initFragmentIcons();
		// initFrameFragmentIcons();
		// initFrameIcons();
	}
	
	public String getName()
	{
		return xmlFile.getName();
	}
	
	/**
	 * 得到显示文字
	 * @return
	 */
	public String getCaculateMsg()
	{
		String str = "内存估算:\n";
		
		str += "切片文件数:" + fragmentHoldersList.getSize() + "\n";
		
		str += "帧数:" + frameHolder.size() + "\n";
		
		str += "动画数:" + animations.size() + "\n";
		
		int imageSize = calculateImageMemorySize();
		str += "图片占用内存:" + imageSize + " byte (" + imageSize/1024 + " k)\n" ;
		
		int memorySize = calculateMemorySize();
		str += "总占用内存:" + memorySize + " byte (" + memorySize/1024 + " k)\n";
		
		return str;
	}
	
	/**
	 * 计算占用的内存大小
	 * @return
	 */
	public int calculateMemorySize()
	{
		int size = 0;
		
		// name
		String name = getName();
		size += name.length() * 2;
		
		// fragmentTypes(byte[])
		size += fragmentHoldersList.getSize();
		
		// fragments(Fragment[])
		for(int i = 0 ; i < fragmentHoldersList.getSize() ; i ++)
		{
			FragmentHolder holder = fragmentHoldersList.getElementAt(i);
			size += holder.calculateMemorySize();
		}
		
		// frames(Frame[])
		for(int i = 0 ; i < frameHolder.size() ; i ++)
		{
			Frame frame = frameHolder.get(i);
			size += frame.calculateMemorySize();
		}
		
		// animations(Animation[])
		for(Animation ani : animations)
		{
			size += ani.calculateMemorySize();
		}
		
		return size;
	}
	
	/**
	 * 得到图片占用的内存
	 * @return
	 */
	public int calculateImageMemorySize()
	{
		int size = 0;
		
		// fragments(Fragment[])
		for(int i = 0 ; i < fragmentHoldersList.getSize() ; i ++)
		{
			FragmentHolder holder = fragmentHoldersList.getElementAt(i);
			size += holder.caculateImageMemorySize();
		}
		
		return size;
	}

	public static final boolean verbose = false;
	public File xmlFile;
	private String spriteImgPath;
	private int index;

	/** 动画 */
	private ArrayList<Animation> animations;
	/** 帧的管理者 */
	public FrameHolder frameHolder;
	/** 切片的管理者 */
	public FragmentHoldersList fragmentHoldersList;

	/** 当前正在编辑的切片, fay: 不应该放这里 */
	public FragmentHolder fragmentHolder;
}
