package cz.ismar.projects.IEdit.structure;

import java.util.ArrayList;
import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.io.InOut;
import cz.ismar.projects.IEdit.io.StoringException;

/**
 * 动画类
 * 
 * @author Lori
 * 
 */
public class Animation implements Rescalable, InOutAble
{

	public Element output(Document document) throws StoringException
	{
		Element element = document.createElement("animation_arr");
		InOut.setAttribute(element, "name_utf_a0", "" + getName());
		InOut.setAttribute(element, "type_byte_r0", "" + getType());
		InOut.setAttribute(element, "noLoops_byte_r1", "" + getNoLoops());
		InOut.setAttribute(element, "endAnimAction_byte_r2", "" + getEndAnimAction());

		InOut.setAttribute(element, "xanimationId", "" + getAnimationId());
		InOut.setAttribute(element, "xcheckId", "" + getCheckFrameId());
		InOut.setAttribute(element, "ymapData", "" + crossToXmlData());
		
		ArrayList<Gesture> list = getGestures();
		Element element1;
		for(Iterator<Gesture> iterator = list.iterator(); iterator.hasNext(); element.appendChild(element1))
		{
			Gesture gesture = (Gesture) iterator.next();
			element1 = gesture.output(document);
		}

		return element;
	}

	public void input(Element element)
	{
		setName(element.getAttribute(InOut.compatibilityInput ? "name" : "name_utf_a0"));
		try
		{
			setType(Byte.parseByte(element.getAttribute(InOut.compatibilityInput ? "type" : "type_byte_r0")));
			setNoLoops(Byte.parseByte(element.getAttribute(InOut.compatibilityInput ? "noLoops" : "noLoops_byte_r1")));
			setEndAnimAction(Byte.parseByte(element.getAttribute(InOut.compatibilityInput ? "endAnimAction" : "endAnimAction_byte_r2")));

			setAnimationId(Byte.parseByte(element.getAttribute("xanimationId")));
			setCheckFrameId(Byte.parseByte(element.getAttribute("xcheckId")));

			this.xmlDataToCross(element.getAttribute("ymapData"));
		}catch(Exception exception)
		{
		}
		
		NodeList nodelist = element.getElementsByTagName("gesture");
		for(int i = 0; i < nodelist.getLength(); i++)
		{
			Element element1 = (Element) nodelist.item(i);
			Gesture gesture = new Gesture(sprite, this);
			gesture.input(element1);
			add(gesture);
			// 设置判定帧
			if(this.getIndex() == this.getCheckFrameId())
			{
				gesture.setCheck(true);
			}
		}

	}

	public boolean nextTick(boolean isLoop)
	{
		// System.out.println("" + SpriteEditor.project.get().get().getTick() +
		// " " + SpriteEditor.project.get().get().getLength());
		if(get().lastTick())
		{
			if(isLast())
			{
				if(isLoop)
				{
					setIndex(0);
					get().moveFirstTick();
				}else
				{
					return false;
				}
			}else
			{
				moveNext();
				get().moveFirstTick();
			}
		}else
		{
			get().nextTick();
		}
		return true;
	}

	public boolean prevTick(boolean flag)
	{
		if(get().firstTick())
		{
			if(isFirst())
			{
				if(flag)
				{
					setIndex(size() - 1);
					get().moveLastTick();
				}else
				{
					return false;
				}
			}else
			{
				movePrev();
			}
		}else
		{
			get().prevTick();
		}
		return true;
	}

	public boolean isLastTick()
	{
		if(!isLast())
			return false;
		else
			return get().lastTick();
	}

	public Animation(Sprite project1)
	{
		type = 16;
		noLoops = 1;
		endAnimAction = 0;
		index = -1;
		sprite = project1;
		gestures = new ArrayList<Gesture>();
	}

	public Animation(String s, Sprite project1)
	{
		type = 16;
		noLoops = 1;
		endAnimAction = 0;
		index = -1;
		name = s;
		sprite = project1;
		gestures = new ArrayList<Gesture>();
	}

	public Animation copy()
	{
		Animation animation = new Animation(getName() + "_copy", sprite);
		animation.type = type;
		animation.noLoops = noLoops;
		animation.endAnimAction = endAnimAction;
		Gesture gesture;
		for(Iterator<Gesture> iterator = gestures.iterator(); iterator.hasNext(); animation.add(gesture))
		{
			gesture = ((Gesture) iterator.next()).copy();
		}

		return animation;
	}

	public Gesture remove(int i)
	{
		// daben:add
		if(SpriteEditor.animationsSaved)
			SpriteEditor.animationsSaved = false;
		// ~daben
		return (Gesture) gestures.remove(i);
	}

	public void add(Gesture gesture)
	{
		gestures.add(gesture);
		index = gestures.size() - 1;
		SpriteEditor.animationsSaved = false;
	}

	public int size()
	{
		return gestures.size();
	}

	public int getIndex()
	{
		return index;
	}

	public void setIndex(int i)
	{
		if(i == -1 && gestures.size() > 0)
			index = 0;
		else
			index = i;
	}

	public void reset()
	{
		index = 0;
		if(get() != null)
			get().moveFirstTick();
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
		if(index < gestures.size() - 1)
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
			gestures.add(0, gestures.remove(index));
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
			gestures.add(index - 1, gestures.remove(index));
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
		if(index < gestures.size() - 1)
		{
			gestures.add(index + 1, gestures.remove(index));
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
		if(index < gestures.size() - 1)
		{
			gestures.add(gestures.remove(index));
			index = gestures.size() - 1;
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
		return index == gestures.size() - 1;
	}

	public boolean remove()
	{
		if(gestures.size() > 0)
		{
			gestures.remove(index);
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

	public Gesture get()
	{
		if(gestures.size() == 0)
		{
			return null;
		}
		
		if(index < 0)
		{
			index = 0;
		}else if(index >= gestures.size())
		{
			index = gestures.size() - 1;
		}
		return (Gesture) gestures.get(index);
	}

	public Gesture getNext()
	{
		if(index < gestures.size() - 1)
			return (Gesture) gestures.get(index + 1);
		else
			return null;
	}

	public Gesture getPrev()
	{
		if(index > 0)
			return (Gesture) gestures.get(index - 1);
		else
			return null;
	}

	public ArrayList<Gesture> getGestures()
	{
		return gestures;
	}

	public boolean contains(Gesture gesture)
	{
		return gestures.contains(gesture);
	}

	public boolean contains(Frame frame)
	{
		for(Iterator<Gesture> iterator = gestures.iterator(); iterator.hasNext();)
		{
			Gesture gesture = (Gesture) iterator.next();
			if(gesture.getFrame() == frame)
			{
				return true;
			}
		}

		return false;
	}

	public boolean remove(Gesture gesture)
	{
		boolean flag = gestures.remove(gesture);
		if(flag)
			index = 0;
		while(gestures.remove(gesture))
			;
		// daben:add
		if(SpriteEditor.animationsSaved)
			SpriteEditor.animationsSaved = false;
		// ~daben
		return flag;
	}

	public boolean remove(Frame frame)
	{
		boolean flag = false;
		Iterator<Gesture> iterator = gestures.iterator();
		ArrayList<Gesture> arraylist = new ArrayList<Gesture>();
		do
		{
			if(!iterator.hasNext())
				break;
			Gesture gesture = (Gesture) iterator.next();
			if(gesture.getFrame() == frame)
			{
				arraylist.add(gesture);
				flag = true;
			}
		}while(true);
		gestures.removeAll(arraylist);
		// daben:add
		if(SpriteEditor.animationsSaved)
			SpriteEditor.animationsSaved = false;
		// ~daben
		return flag;
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
		return name;

		// String name = "[未定义]";
		// name = ConfigData.getAniMessage(this.getAnimationId());
		// return name+"("+this.getCheckFrameId()+")";

	}

	public void rescaleTimes(float f)
	{
		Gesture gesture;
		for(Iterator<Gesture> iterator = gestures.iterator(); iterator.hasNext(); gesture.rescaleTimes(f))
			gesture = (Gesture) iterator.next();

	}

	public byte getType()
	{
		return type;
	}

	public void setType(byte byte0)
	{
		type = byte0;
	}

	public byte getNoLoops()
	{
		return noLoops;
	}

	public void setNoLoops(byte byte0)
	{
		noLoops = byte0;
	}

	public byte getEndAnimAction()
	{
		return endAnimAction;
	}

	public void setEndAnimAction(byte byte0)
	{
		endAnimAction = byte0;
	}

	public int[] getSumaPosition()
	{
		// System.out.println("Animation.getSumaPosition ----------------");
		int ai[] = { 0, 0 };
		for(int i = 0; i < index + 1; i++)
		{
			Gesture gesture = (Gesture) gestures.get(i);
			Frame frame = gesture.getFrame();
			// System.out.println("Animation.getSumaPosition gesture:" +
			// gesture);
			// System.out.println("Animation.getSumaPosition frame:" + frame);
			int j = 0;
			if(i < index)
				j = gesture.getLength();
			else
				j = gesture.getTick() + 1;
			// System.out.println("Animation.getSumaPosition frame.[" + i + "]"
			// + j);
			ai[0] += j * gesture.getSpeedX();
			ai[1] += j * gesture.getSpeedY();
		}

		return ai;
	}

	public int[] getSumaPositionForGesture(int i)
	{
		int ai[] = { 0, 0 };
		for(int j = 0; j < i; j++)
		{
			Gesture gesture = gestures.get(j);
			int k = gesture.getLength();
			ai[0] += k * gesture.getSpeedX();
			ai[1] += k * gesture.getSpeedY();
		}

		return ai;
	}

	public String getTxtSumaPoosition()
	{
		int ai[] = getSumaPosition();
		return "[" + ai[0] + "," + ai[1] + "]";
	}

	public String getTxtSumaPoositionForGesture(int i)
	{
		int ai[] = getSumaPositionForGesture(i);
		return "[" + ai[0] + "," + ai[1] + "]";
	}

	
	/** 保存哪些帧是判定帧 */
//	public ArrayList<Gesture> checkFrames = new ArrayList<Gesture>();

	public byte getAnimationId()
	{
		return animationId;
	}

	public void setAnimationId(byte animationId)
	{
		this.animationId = animationId;
	}

	public byte getCheckFrameId()
	{
		return checkFrameId;
	}

	public void setCheckFrameId(byte checkFrameId)
	{
		this.checkFrameId = checkFrameId;
	}

	// 地图碰撞信息【9x10】,3个int可以保存了
	public final static int MAPCROSS_ROW = 10; // 行
	public final static int MAPCROSS_COLUMN = 9; // 列

	private boolean[][] mapCrossTable;

	public void initMapCrossTable()
	{
		if(mapCrossTable == null)
		{
			mapCrossTable = new boolean[MAPCROSS_ROW][MAPCROSS_COLUMN];
			for(int x = 0; x < MAPCROSS_COLUMN; x++)
			{
				for(int y = 0; y < MAPCROSS_ROW; y++)
				{
					mapCrossTable[y][x] = true;
				}
			}
		}
	}

	public boolean[][] getMapCrossTable()
	{
		return mapCrossTable;
	}

	public void setCross(int x, int y, boolean isCross)
	{
		if(mapCrossTable == null)
		{
			initMapCrossTable();
		}

		if((y < mapCrossTable.length) && (x < mapCrossTable[y].length))
			mapCrossTable[y][x] = isCross;
	}

	public boolean getCross(int x, int y)
	{
		if(mapCrossTable == null)
			return false;

		if((y < mapCrossTable.length) && (x < mapCrossTable[y].length))
		{
			return mapCrossTable[y][x];
		}
		return false;
	}

	// ===========================XML====================== //
	public String crossToXmlData()
	{

		StringBuffer buffer = new StringBuffer();

		if(mapCrossTable == null)
		{
			return "NULL";
		}
		for(int i = 0; i < MAPCROSS_ROW; i++)
		{
			for(int j = 0; j < MAPCROSS_COLUMN; j++)
			{

				if(mapCrossTable[i][j])
					buffer.append("1");
				else
					buffer.append("0");
			}
		}

		return buffer.toString();
	}

	public void xmlDataToCross(String xmlString)
	{

		if(xmlString == null)
		{
			return;
		}

		if(xmlString.equals(""))
			return;
		if(xmlString.equals("NULL"))
		{
			return;
		}

		if(mapCrossTable == null)
		{
			this.initMapCrossTable();
		}
		int length = xmlString.length();

		int index = 0;
		try
		{

			for(int i = 0; i < MAPCROSS_ROW; i++)
			{
				for(int j = 0; j < MAPCROSS_COLUMN; j++)
				{

					String s = "" + xmlString.charAt(index);
					if(Integer.parseInt(s) == 1)
					{
						mapCrossTable[i][j] = true;
					}else
					{
						mapCrossTable[i][j] = false;

					}
					index++;
				}
			}
		}catch(Exception ex)
		{
		}
	}
	
	/**
	 * 计算内存占用大小(要看客户端的实现)
	 * 
	 * id	int	4 bytes
	 * loopnum	int	4 bytes
	 * lastTime	byte[]	N bytes
	 * isChecks	byte[]	N bytes
	 * x		int	4 bytes
	 * y		int	4 bytes
	 * frames	Frame[]	N * 4 bytes
	 * frameIds	byte[]	N bytes
	 * frameIndex	int	4 bytes
	 * currentFrameLastTime	int	4 bytes
	 * loopIndex	int	4 bytes
	 * isFlipX	boolean	4 bytes
	 * 
	 * @return
	 */
	public int calculateMemorySize()
	{
		int size = 0;
		
		// id	int	4 bytes
		size += 4;
		
		// loopnum	int	4 bytes
		size += 4;
		
		// lastTime	byte[]	N bytes
		size += gestures.size();
		
		// isChecks	byte[]	N bytes
		size += gestures.size();
		
		// x	int	4 bytes
		size += 4;
		
		// y	int	4 bytes
		size += 4;
		
		// frames	Frame[]	N * 4 bytes
		size += 4 * gestures.size();
		
		// frameIds	byte[]	N bytes
		size += gestures.size();
		
		// frameIndex	int	4 bytes
		size += 4;
		
		// currentFrameLastTime	int	4 bytes
		size += 4;
		
		// loopIndex	int	4 bytes
		size += 4;
		
		// isFlipX	boolean	4 bytes
		size += 4;
		
		return size;
	}
	
	public static final String PREFIX = "Amination_";
	public static final int TYPE_MOVING = 16;
	public static final int TYPE_NOT_MOVING = 4;
	public static final String TYPE_NAMES[] = { "MOVING", "NOT MOVING" };
	public static final byte TYPE_VALUES[] = { 16, 4 };
	private String name = "";
	private ArrayList<Gesture> gestures;
	private byte type;
	private byte noLoops;
	private byte endAnimAction;
	private transient int index;

	private Sprite sprite;

	// 增加动画类型选择
	private byte animationId; // 如 斧攻击，杖攻击等
	private byte checkFrameId = -1;
}
