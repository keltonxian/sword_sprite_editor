package cz.ismar.projects.IEdit.structure;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.handlers.FrameFragmentsListCommander;
import cz.ismar.projects.IEdit.io.InOut;
import cz.ismar.projects.IEdit.io.StoringException;

public class FrameHolder implements Rescalable, InOutAble
{

	public void rescaleTimes(float f)
	{
		Frame frame;
		for(Iterator iterator = frames.iterator(); iterator.hasNext(); frame.rescaleTimes(f))
		{
			frame = (Frame) iterator.next();
		}

	}

	// 输出/输入 帧组合块 的翻转信息
	// =============================================================================//
//	public Element outputTransform(Document document) throws StoringException
//	{
//		Element element = document.createElement("Transforms_set");
//		Element element1;
//		for(Iterator iterator = frames.iterator(); iterator.hasNext();)
//		{
//			Frame frame = (Frame) iterator.next();
//			frame.outputTransform(document, element);
//		}
//
//		return element;
//	}
//
//	public static void inputTransform(Element element, FrameHolder frameHolder)
//	{
//		if(InOut.verbose)
//			System.out.println("inputTransform.input");
//		NodeList nodelist = element.getElementsByTagName("Transform");
//		if(nodelist.getLength() <= 0)
//			return;
//		for(int i = 0; i < nodelist.getLength(); i++)
//		{
//			Element element1 = (Element) nodelist.item(i);
//			int frameID = Integer.parseInt(element1.getAttribute("frameID"));
//			int fragmentID = Integer.parseInt(element1.getAttribute("fragmentID"));
//			int transformType = Integer.parseInt(element1.getAttribute("transformType"));
//			// System.out.println("frameID: "+frameID);
//			Frame frame = frameHolder.get(frameID);
//			if(frame != null)
//			{
//				FrameFragment frameFragment = frame.get(fragmentID);
//				if(frameFragment != null)
//				{
//					frameFragment.setTransfromType(transformType);
//					// frameFragment.updateImage();
//					// frameFragment.updateName();
//				}
//				// frame.updateImage();
//				// SpriteEditor.mainFramingPanel.frameFragmentListModel.update();
//				// SpriteEditor.mainFramingPanel.frameListModel.update();
//				// SpriteEditor.mainFramingPanel.repaint();
//			}
//		}
//	}

	// =============================================================================//

	public Element output(Document document) throws StoringException
	{
		Element element = document.createElement("frames_arr");
		Element element1;
		
		for(Frame frame : frames)
		{
			element1 = frame.output(document);
			element.appendChild(element1);
		}

		return element;
	}

	public void input(Element element)
	{
		NodeList nodelist = element.getElementsByTagName(InOut.compatibilityInput ? "frame" : "frame_arr");
		
		if(InOut.verbose)
			System.out.println("FrameHolder.input GOT FRAMES:" + nodelist.getLength());
		
		for(int i = 0; i < nodelist.getLength(); i++)
		{
			Element element1 = (Element) nodelist.item(i);
			Frame frame = new Frame(sprite);
			frame.input(element1);
			frames.add(frame);
			if(InOut.verbose)
				System.out.println("FragmentHolder.input ADDING FRAME:" + frame);
		}

	}

	public FrameHolder(Sprite project1)
	{
		index = -1;
		frames = new ArrayList<Frame>();
		sprite = project1;
	}

	public void defragment()
	{
		int id = 0;
		for(Frame frame : frames)
		{
			frame.defragmentID(id);
			id ++;
		}
	}

	public Frame get(int i)
	{
		if(i > -1 && i < frames.size())
			return (Frame) frames.get(i);
		else
			return null;
	}

	public Frame getByID(int i)
	{
		for(Iterator iterator = frames.iterator(); iterator.hasNext();)
		{
			Frame frame = (Frame) iterator.next();
			if(frame.getID() == i)
				return frame;
		}

		throw new IllegalArgumentException("unknown frame id=" + i);
	}

	public Frame getCurrentFrame()
	{
		if(frames == null || frames.size() == 0)
		{
			return null;
		}
		
		if(index < 0)
		{
			index = 0;
		}else if(index >= frames.size())
		{
			index = frames.size() - 1;
		}
		
		return (Frame) frames.get(index);
	}

	public Frame remove(int index)
	{
		Frame frame = (Frame) frames.remove(index);
		SpriteEditor.frameFragmentMasks.removeHiden(frame);
		// daben:add
		if(SpriteEditor.animationsSaved)
			SpriteEditor.animationsSaved = false;
		// ~daben
		return frame;
	}

	public Frame remove()
	{
		// daben:add
		if(SpriteEditor.animationsSaved)
			SpriteEditor.animationsSaved = false;
		// ~daben
		return remove(index);
	}

	public void add(Frame frame)
	{
		if(index == -1)
			index = 0;
		frames.add(frame);
		// daben:add
		if(SpriteEditor.animationsSaved)
			SpriteEditor.animationsSaved = false;
		// ~daben
	}

	public void insert(Frame frame)
	{
		if(index == -1)
		{
			index = 0;
			frames.add(frame);
		}else
		{
			List list = frames;
			frames = new ArrayList(frames.size() + 1);
			frames.addAll(list.subList(0, index + 1));
			frames.add(frame);
			if(index + 1 < list.size())
				frames.addAll(list.subList(index + 1, list.size()));
			index++;
		}
		// daben:add
		if(SpriteEditor.animationsSaved)
			SpriteEditor.animationsSaved = false;
		// ~daben
	}

	public int size()
	{
		return frames.size();
	}

	public ArrayList<Frame> getFrames()
	{
		return frames;
	}

	public void setIndex(int i)
	{
		index = i;
	}

	public int getIndex()
	{
		return index;
	}

	public boolean moveNext()
	{
		if(index < frames.size() - 1)
		{
			index++;
			return true;
		}else
		{
			return false;
		}
	}

	public boolean moveTop()
	{
		if(index > 0)
		{
			frames.add(0, frames.remove(index));
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
			frames.add(index - 1, frames.remove(index));
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
		if(index < frames.size() - 1)
		{
			frames.add(index + 1, frames.remove(index));
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
		if(index < frames.size() - 1)
		{
			frames.add(frames.remove(index));
			index = frames.size() - 1;
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
		return index == frames.size() - 1;
	}

	public boolean remove(Frame frame)
	{
		boolean result = frames.remove(frame);

		if(result)
		{
			SpriteEditor.frameFragmentMasks.removeHiden(frame);
		}

		SpriteEditor.animationsSaved = false;

		return result;
	}
	
	/**
	 * 重刷新所有frames
	 */
	public void refresh()
	{
		for(Frame frame : frames)
		{
			frame.refresh();
			frame.updateImage();
		}
	}

	private ArrayList<Frame> frames;
	private int index;
	private Sprite sprite;
}
