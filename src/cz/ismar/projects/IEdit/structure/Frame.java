/**
 * 2007-9-12 daben
 * - modify in
 * --> updateImage()
 * --> for the function of transformation
 * 
 * 2007-11-5 daben
 * - add saveUndo()
 *       undo()
 */

package cz.ismar.projects.IEdit.structure;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.io.InOut;
import cz.ismar.projects.IEdit.io.StoringException;

public class Frame implements Rescalable, InOutAble
{

	public static int getIndexFromValue(int i)
	{
		switch(i)
		{
		case 1: // '\001'
			return 0;

		case 2: // '\002'
			return 1;

		case 3: // '\003'
			return 2;

		case 4: // '\004'
			return 3;

		case 8: // '\b'
			return 4;

		case 5: // '\005'
		case 6: // '\006'
		case 7: // '\007'
		default:
			return 0;
		}
	}

	public void rescaleTimes(float f)
	{
		if(f > 0.0F)
		{
			colisionArea = new Rectangle((int) (colisionArea.getX() * (double) f), (int) (colisionArea.getY() * (double) f), (int) (colisionArea.getWidth() * (double) f), (int) (colisionArea.getHeight() * (double) f));
		}else
		{
			colisionArea = new Rectangle((int) (colisionArea.getX() / (double) (-f)), (int) (colisionArea.getY() / (double) (-f)), (int) (colisionArea.getWidth() / (double) (-f)), (int) (colisionArea.getHeight() / (double) (-f)));
		}
		FrameFragment framefragment;
		for(Iterator iterator = frameFragments.iterator(); iterator.hasNext(); framefragment.rescaleTimes(f))
		{
			framefragment = (FrameFragment) iterator.next();
		}

	}

	/**
	 * 输出翻转信息
	 * 
	 * @param document
	 * @return
	 * @throws StoringException
	 */
	public void outputTransform(Document document, Element addToElement) throws StoringException
	{
		if(InOut.verbose)
			System.out.println("Frame.outputTransform");
		try
		{

			Element element1;
			for(ListIterator listiterator = frameFragments.listIterator(frameFragments.size()); listiterator.hasPrevious();)
			{
				FrameFragment framefragment = (FrameFragment) listiterator.previous();
				element1 = framefragment.outputTransform(document, getID());
				if(element1 != null)
					addToElement.appendChild(element1);
			}

		}catch(StoringException storingexception)
		{

		}
	}

	public Element output(Document document) throws StoringException
	{
		Element element = document.createElement("frame_arr");
		try
		{
			InOut.setAttribute(element, "id_byte_a0", "" + getID());
			InOut.setAttribute(element, "name_utf_a1", getName() != null ? getName() : "name");
			InOut.setAttribute(element, "colisionX_byte_r1", "" + (int) colisionArea.getX());
			InOut.setAttribute(element, "colisionY_byte_r2", "" + (int) colisionArea.getY());
			InOut.setAttribute(element, "colisionX2_byte_r3", "" + (int) (colisionArea.getX() + colisionArea.getWidth()));
			InOut.setAttribute(element, "colisionY2_byte_r4", "" + (int) (colisionArea.getY() + colisionArea.getHeight()));

			int orientationIndex = getOrientationIndex();
			if((orientationIndex < 0) || (orientationIndex >= ORIENTATION_VALUES.length))
				orientationIndex = 0;

			InOut.setAttribute(element, "orientation_byte_r0", "" + ORIENTATION_VALUES[orientationIndex]);

			// 暂时屏蔽 隐藏 参数的 output
			/*
			 * if(SpriteEditor.frameFragmentMasks.isEnabled()) { if(size() > 32)
			 * throw new
			 * NumberFormatException("More frame fragments than allowed for masks: "
			 * + size() + " > 32 in frame ID: " + ID); String s =
			 * SpriteEditor.frameFragmentMasks.getMasks(this);
			 * InOut.setAttribute(element, "masks_intArr_r5", s); }
			 */

			Element element1;
			for(ListIterator listiterator = frameFragments.listIterator(frameFragments.size()); listiterator.hasPrevious(); element.appendChild(element1))
			{
				FrameFragment framefragment = (FrameFragment) listiterator.previous();
				element1 = framefragment.output(document);
			}

		}catch(StoringException storingexception)
		{
			String s1 = storingexception.getMessage() + " Frame '" + getName() + "' id='" + getID() + "'.";
			throw new StoringException(s1);
		}
		return element;
	}

	public void input(Element element)
	{
		ID = Integer.parseInt(element.getAttribute(InOut.compatibilityInput ? "id" : "id_byte_a0"));
		name = element.getAttribute(InOut.compatibilityInput ? "name" : "name_utf_a1");
		int cx = 0;
		int cy = 0;
		int colisionX = 10;
		int colisionY = 10;
		try
		{
			cx = Integer.parseInt(element.getAttribute(InOut.compatibilityInput ? "colision-x" : "colisionX_byte_r1"));
			cy = Integer.parseInt(element.getAttribute(InOut.compatibilityInput ? "colision-y" : "colisionY_byte_r2"));
			if(InOut.compatibilityInput)
			{
				colisionX = Integer.parseInt(element.getAttribute("colision-width"));
				colisionY = Integer.parseInt(element.getAttribute("colision-height"));
				orientationIndex = Integer.parseInt(element.getAttribute("orientation-index"));
			}else
			{
				colisionX = Integer.parseInt(element.getAttribute("colisionX2_byte_r3")) - cx;
				colisionY = Integer.parseInt(element.getAttribute("colisionY2_byte_r4")) - cy;
				int orientation = Integer.parseInt(element.getAttribute("orientation_byte_r0"));
				int index = 0;
				do
				{
					if(index >= ORIENTATION_VALUES.length)
					{
						break;
					}

					if(orientation == ORIENTATION_VALUES[index])
					{
						orientationIndex = index;
						break;
					}
					index++;
				}while(true);
			}
		}catch(Exception exception)
		{
			// exception.printStackTrace();
			System.out.println("Frame input Error");
		}

		colisionArea = new Rectangle(cx, cy, colisionX, colisionY);
		// 切片列表
		NodeList nodelist = element.getElementsByTagName(InOut.compatibilityInput ? "frame-fragment" : "frameFragment");

		int length = nodelist.getLength();
		for(int i = 0; i < length; i++)
		{
			Element element1;
			if(!InOut.compatibilityInput)
			{
				element1 = (Element) nodelist.item(i);
			}else
			{
				element1 = (Element) nodelist.item(length - i - 1);
			}

			// 解析FrameFragment
			FrameFragment framefragment = new FrameFragment(sprite);
			framefragment.input(element1);
			add(0, framefragment);
		}
	}

	public Frame(Sprite project1)
	{
		this("");
		sprite = project1;
		name = "FRM_" + ID;
		frameFragments = new ArrayList<FrameFragment>();
	}

	public Frame(String s)
	{
		index = -1;
		colisionArea = new Rectangle(-10, -7, 27, 20);
		colisionAreaSelected = false;
		name = s;
		ID = ++currentID;
		frameFragments = new ArrayList<FrameFragment>();
	}

	public Frame(int i, String s)
	{
		index = -1;
		colisionArea = new Rectangle(-10, -7, 27, 20);
		colisionAreaSelected = false;
		ID = i;
		if(currentID < i)
			currentID = i;
		name = s;
		frameFragments = new ArrayList<FrameFragment>();
	}

	public Frame copy()
	{
		Frame frame = new Frame(sprite);
		frame.setOrientationIndex(orientationIndex);
		frame.setName(getName());
		frame.setColisionArea(new Rectangle(getColisionArea()));
		FrameFragment framefragment;
		for(Iterator<FrameFragment> iterator = getFrameFragments().iterator(); iterator.hasNext(); frame.add(new FrameFragment(framefragment)))
		{
			framefragment = (FrameFragment) iterator.next();
		}

		return frame;
	}

	public FrameFragment get(int i)
	{
		if(i > -1 && i < frameFragments.size())
			return (FrameFragment) frameFragments.get(i);
		else
			return null;
	}

	public FrameFragment get()
	{
		if(index < 0 || index >= frameFragments.size())
		{
			return null;
		}
		
		return (FrameFragment) frameFragments.get(index);
	}

	public FrameFragment remove(int i)
	{
		FrameFragment framefragment = (FrameFragment) frameFragments.remove(i);
		framefragment.setParentFrame(null);
		SpriteEditor.frameFragmentMasks.removeHiden(framefragment);
		return framefragment;
	}

	public boolean remove()
	{
		if(frameFragments.size() > 0)
		{
			// daben:add
			this.saveUndo();
			// ~daben
			FrameFragment framefragment = (FrameFragment) frameFragments.remove(index);
			SpriteEditor.frameFragmentMasks.removeHiden(framefragment);
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

	/**
	 * 增加frameFragment至末尾
	 * 
	 * @param framefragment
	 */
	public void add(FrameFragment framefragment)
	{
		frameFragments.add(framefragment);
		framefragment.setParentFrame(this);
	}

	/**
	 * 增加frameFragment至指定的位置
	 * 
	 * @param index
	 * @param framefragment
	 */
	public void add(int index, FrameFragment framefragment)
	{
		frameFragments.add(index, framefragment);
		framefragment.setParentFrame(this);
	}

	public boolean moveNext()
	{
		if(index < frameFragments.size() - 1)
		{
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

	public boolean moveTop()
	{
		if(index > 0)
		{
			this.saveUndo();
			frameFragments.add(0, frameFragments.remove(index));
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
			this.saveUndo();
			frameFragments.add(index - 1, frameFragments.remove(index));
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
		if(index < frameFragments.size() - 1)
		{
			this.saveUndo();
			frameFragments.add(index + 1, frameFragments.remove(index));
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
		if(index < frameFragments.size() - 1)
		{
			this.saveUndo();
			frameFragments.add(frameFragments.remove(index));
			index = frameFragments.size() - 1;
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
		return index == frameFragments.size() - 1;
	}

	public int size()
	{
		return frameFragments.size();
	}

	public ArrayList<FrameFragment> getFrameFragments()
	{
		return frameFragments;
	}

	/**
	 * 当前选中的FrameFragment的index
	 * 
	 * @return
	 */
	public int getIndex()
	{
		return index;
	}

	public void setIndex(int i)
	{
		index = i;
		indices = null;
	}

	public void setIndices(int ai[])
	{
		indices = ai;
	}

	public int[] getIndices()
	{
		return indices;
	}

	public boolean isIndexSelected(int i)
	{
		if(indices != null)
		{
			for(int j = 0; j < indices.length; j++)
			{
				if(indices[j] == i)
				{
					return true;
				}
			}

		}
		return false;
	}

	public int getID()
	{
		return ID;
	}

	public void defragmentID(int i)
	{
		ID = i;
		currentID = i;
	}

	public void defragment()
	{
		Iterator<FrameFragment> iterator = frameFragments.iterator();
		for(int i = 0; iterator.hasNext(); i++)
		{
			FrameFragment framefragment = (FrameFragment) iterator.next();
			framefragment.defragmentID(i);
		}

	}

	public String getName()
	{
		return name;
	}

	public void setName(String s)
	{
		// daben:add
		if(SpriteEditor.animationsSaved)
			SpriteEditor.animationsSaved = false;
		// ~daben
		name = s;
	}

	public String toString()
	{
		return name + "[" + ID + "]";
	}

	public Rectangle getColisionArea()
	{
		return colisionArea;
	}

	public int getOrientationIndex()
	{
		return orientationIndex;
	}

	public void setOrientationIndex(int i)
	{
		orientationIndex = i;
	}

	public void setColisionArea(Rectangle rectangle)
	{
		colisionArea = rectangle;
	}

	public boolean isColisionAreaSelected()
	{
		return colisionAreaSelected;
	}

	public void setColisionAreaSelected(boolean flag)
	{
		colisionAreaSelected = flag;
	}

	public BufferedImage getImage()
	{
		return image;
	}

	public boolean contains(Fragment fragment)
	{
		for(Iterator<FrameFragment> iterator = frameFragments.iterator(); iterator.hasNext();)
		{
			FrameFragment framefragment = (FrameFragment) iterator.next();
			if(framefragment.getFragment() == fragment)
				return true;
		}

		return false;
	}

	public boolean remove(Fragment fragment)
	{
		Iterator<FrameFragment> iterator = frameFragments.iterator();
		FrameFragment framefragment = null;
		do
		{
			if(!iterator.hasNext())
				break;
			FrameFragment framefragment1 = (FrameFragment) iterator.next();
			if(framefragment1.getFragment() != fragment)
				continue;
			framefragment = framefragment1;
			// daben:add
			iterator.remove();
			framefragment.setParentFrame(null);
			SpriteEditor.frameFragmentMasks.removeHiden(framefragment);
			// ~daben
			// break;
		}while(true);
		// daben:remove
		// frameFragments.remove(framefragment);
		// framefragment.setParentFrame(null);
		// SpriteEditor.frameFragmentMasks.removeHiden(framefragment);
		// ~daben
		return false;
	}

	public void updateImage()
	{
		int tempBeginX = 0;
		int tempBeginY = 0;
		int tempEndX = 0;
		int tempEndY = 0;
		
		// 计算图片大小
		for(FrameFragment frameFragment : frameFragments)
		{
			if(frameFragment.getTopLeftX(1) < tempBeginX)
			{
				tempBeginX = frameFragment.getTopLeftX(1);
			}
			if(frameFragment.getTopLeftY(1) < tempBeginY)
			{
				tempBeginY = frameFragment.getTopLeftY(1);
			}
			
			if(frameFragment.getTopLeftX(1) + frameFragment.getImage().getWidth() > tempEndX)
			{
				tempEndX = frameFragment.getTopLeftX(1) + frameFragment.getImage().getWidth();
			}
			if(frameFragment.getTopLeftY(1) + frameFragment.getImage().getHeight() > tempEndY)
			{
				tempEndY = frameFragment.getTopLeftY(1) + frameFragment.getImage().getHeight();
			}
		}
		
		int imageWidth = tempEndX - tempBeginX;
		int imageHeight = tempEndY - tempBeginY;
		if(imageWidth * imageWidth == 0)
		{
			image = null;
			return;
		}
		
		BufferedImage bufferedImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
		
		FrameFragment framefragment1 = null;
		for(int i = frameFragments.size() - 1 ; i >= 0 ; i --)
		{
			framefragment1 = frameFragments.get(i);
			
			// 隐藏的切片不画
			if(!SpriteEditor.frameFragmentMasks.isHiden(framefragment1))
			{
				bufferedImage.getGraphics().drawImage(framefragment1.getImage(), framefragment1.getTopLeftX(1) - tempBeginX, framefragment1.getTopLeftY(1) - tempBeginY, null);
			}
		}

		float f = (float) bufferedImage.getHeight() / 48F;
		BufferedImage bufferedimage1 = new BufferedImage((int) ((float) bufferedImage.getWidth() / f), (int) ((float) bufferedImage.getHeight() / f), 2);
		bufferedimage1.getGraphics().drawImage(bufferedImage.getScaledInstance((int) ((float) bufferedImage.getWidth() / f), (int) ((float) bufferedImage.getHeight() / f), 2), 0, 0, null);
		image = bufferedimage1;
	}

	// daben:add
	public void saveUndo()
	{
		if(undoList.size() >= UNDOTIMES)
		{
			undoList.remove(0);
		}

		//  clone一份当前帧的FrameFragment进undoList
		ArrayList<FrameFragment> fragments = new ArrayList<FrameFragment>();
		for(FrameFragment frameFragment : frameFragments)
		{
			fragments.add(frameFragment.clone());
		}
			
		undoList.add(fragments);
	}

	/**
	 * undo
	 * 从undo list中, 拿上一份的framefragment
	 * 
	 */
	public void undo()
	{
		if(getUndoLisSize() < 1)
		{
			System.out.println("No undo operating remain~");
			return;
		}
		
		ArrayList<FrameFragment> fragments = undoList.remove(getUndoLisSize() - 1);
		if(fragments == null)
		{
			System.out.println("get undo null");
			return;
		}
		
		this.frameFragments = fragments;
		SpriteEditor.initFrameFragmentIcons();
		SpriteEditor.framePanel.initFrameFragments();
		this.updateImage();
		SpriteEditor.framePanel.frameListModel.update();
		SpriteEditor.framePanel.frameJList.getSelectionModel().setSelectionInterval(SpriteEditor.sprite.frameHolder.getIndex(), SpriteEditor.sprite.frameHolder.getIndex());
	}

	// ~daben

	/**
	 * 刷新FrameFragment 重新从sprite得到fragment
	 */
	public void refresh()
	{
		for(FrameFragment frameFragment : frameFragments)
		{
			frameFragment.refresh();
		}
	}

	/**
	 * 得到与frameFragment相同fragment的一组FrameFragment
	 * 
	 * @param ff
	 * @return 包括传入的framefragment
	 */
	public ArrayList<FrameFragment> getSameFrameFragment(FrameFragment ff)
	{
		if(ff == null)
		{
			return null;
		}

		ArrayList<FrameFragment> result = new ArrayList<FrameFragment>();

		// 查找与ff相同fragment的
		for(FrameFragment frameFragment : frameFragments)
		{
			if(frameFragment.getFragment() == ff.getFragment())
			{
				result.add(ff);
			}
		}

		return result;
	}

	/**
	 * 得到与frameFragment相同fragment的一组FrameFragment
	 * 
	 * @param ff
	 * @return 包括传入的framefragment
	 */
	public ArrayList<Integer> getSameFrameFragmentIndex(FrameFragment ff)
	{
		if(ff == null)
		{
			return null;
		}

		ArrayList<Integer> result = new ArrayList<Integer>();

		// 查找与ff相同fragment的
		for(int i = 0; i < frameFragments.size(); i++)
		{
			FrameFragment frameFragment = frameFragments.get(i);
			if(frameFragment.getFragment() == ff.getFragment())
			{
				result.add(i);
			}
		}

		return result;
	}

	/**
	 * 得到指定的FrameFragment的index
	 * 
	 * @param ff
	 * @return
	 */
	public int getIndexByFrameFragment(FrameFragment ff)
	{
		if(ff == null)
		{
			return -1;
		}

		for(int i = 0; i < frameFragments.size(); i++)
		{
			FrameFragment tempFF = frameFragments.get(i);
			if(tempFF == ff)
			{
				return i;
			}
		}

		return -1;
	}
	
	public int getUndoLisSize()
	{
		return undoList.size();
//		return newUndoList.size();
	}
	
	/**
	 * 
	 * @param g
	 * @param frame
	 * @param originX
	 * @param originY
	 * @param affinetransform
	 * @param isNormalFrame
	 */
	public void drawFrame(Graphics g, int originX, int originY, int offsetX, int offsetY, int scale, 
			boolean isNormalFrame, boolean isDrawSelectRect)
	{
		// 缩放
		AffineTransform affineTransform = new AffineTransform();
		affineTransform.scale(scale, scale);
		
		for(int index = size() - 1; index > -1; index--)
		{
			FrameFragment framefragment = get(index);
			
			// 隐藏的不画
			if(SpriteEditor.frameFragmentMasks.isEnabled() && SpriteEditor.frameFragmentMasks.isHiden(framefragment))
			{
				continue;
			}
			
			if(!framefragment.isVisible())
			{
				continue;
			}
			
			Fragment fragment = framefragment.getFragment();
			if(!fragment.isVisible())
			{
				continue;
			}
			
			int sx = originX + framefragment.getTopLeftX(scale) + offsetX;
			int sy = originY + framefragment.getTopLeftY(scale) + offsetY;
			g.translate(sx, sy);
			if(isNormalFrame) 	// this is normal frame
			{
				((Graphics2D) g).drawImage(framefragment.getImage(), affineTransform, null);
			}else				// this is help frame
			{
				((Graphics2D) g).setComposite(AlphaComposite.getInstance(3, 0.5F));
				((Graphics2D) g).drawImage(framefragment.getImage(), affineTransform, null);
				((Graphics2D) g).setComposite(AlphaComposite.SrcOver);
			}
			
			
			// 被选中切片, 要画上红色的矩形框
			if(isDrawSelectRect && isIndexSelected(index))
			{
				g.setColor(Color.RED);
//				g.drawRect(0, 0, framefragment.getRectangle().width * scale, framefragment.getRectangle().height * scale);
				g.drawRect(0, 0, framefragment.getWidth(scale), framefragment.getHeight(scale));
				g.fillRect(-3, -3, 7, 7);
//				g.fillRect(framefragment.getRectangle().width * scale - 3, framefragment.getRectangle().height * scale - 3, 7, 7);
//				g.fillRect(-3, framefragment.getRectangle().height * scale - 3, 7, 7);
//				g.fillRect(framefragment.getRectangle().width * scale - 3, -3, 7, 7);
				g.fillRect(framefragment.getWidth(scale) - 3, framefragment.getHeight(scale) - 3, 7, 7);
				g.fillRect(-3, framefragment.getHeight(scale) - 3, 7, 7);
				g.fillRect(framefragment.getWidth(scale) - 3, -3, 7, 7);
			}
			g.translate(-sx, -sy);
		}
	}
	
	
	/**
	 * 克隆一个frame
	 */
	public Frame clone()
	{
		Frame frame = new Frame(this.name);
		frame.ID = this.ID;
		frame.frameFragments = new ArrayList<FrameFragment>();
		
		for(FrameFragment frameFragment : this.frameFragments)
		{
			frame.frameFragments.add(frameFragment.clone());
		}
		
		frame.index = this.index;
		
		frame.indices = new int[indices.length];
		System.arraycopy(indices, 0, frame.indices, 0, indices.length);
		
		frame.colisionArea = new Rectangle(colisionArea);
		frame.orientationIndex = orientationIndex;
		frame.colisionAreaSelected = colisionAreaSelected;
		
		frame.image = image;
		frame.sprite = sprite;
		
		return frame;
	}
	
	/**
	 * 计算内存占用大小(要看客户端的实现)
	 * 
	 * fragmentFileIndexs	byte[]	N bytes
	 * fragments	Fragment[]	N * 4 bytes
	 * fragmentSubIndexs	byte[]	N bytes
	 * centerXs	byte[]	N bytes
	 * centerYs	byte[]	N bytes
	 * transforms	byte[]	N bytes
	 * 
	 * @return
	 */
	public int calculateMemorySize()
	{
		int size = 0;
		
		// fragmentFileIndexs byte[] N bytes
		size += frameFragments.size();
		
		// fragments	Fragment[]	N * 4 bytes
		size += frameFragments.size() * 4;
		
		// fragmentSubIndexs	byte[]	N bytes
		size += frameFragments.size();
		
		// centerXs	byte[]	N bytes
		size += frameFragments.size();
		
		// centerYs	byte[]	N bytes
		size += frameFragments.size();
		
		// transforms	byte[]	N bytes
		size += frameFragments.size();
		
		return size;
	}

	public static final String DEFAULT_NAME_PREFIX = "FRM_";
	public static final String ORIENTATION_NAMES[] = { "1-ORIENTATION", "HORIZONTAL", "VERTICAL", "4-ORIENTATION", "8-ORIENTATION" };
	public static final int ORIENTATION_VALUES[] = { 1, 2, 3, 4, 8 };
	
	private static int currentID;
	private int ID; // 帧ID，唯一
	private String name;
	private ArrayList<FrameFragment> frameFragments;
	private int index; // 帧中当前选择的一个切片块在frameFragments中的索引
	private int indices[]; // 帧中当前选择的一组切片块在frameFragments中的索引
	private Rectangle colisionArea; // 标识碰撞区域
	private int orientationIndex;
	private boolean colisionAreaSelected;
	private BufferedImage image; // 帧的缩略图
	private Sprite sprite;
	
	// daben:add
	private final int UNDOTIMES = 10;
	private ArrayList<ArrayList<FrameFragment>> undoList = new ArrayList<ArrayList<FrameFragment>>();
	// ~daben
	
	/** 2009-11-19 fay 新的undo list*/
//	private ArrayList<Frame> newUndoList = new ArrayList<Frame>();

}
