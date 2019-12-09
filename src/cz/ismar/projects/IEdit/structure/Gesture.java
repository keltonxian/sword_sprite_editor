package cz.ismar.projects.IEdit.structure;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.io.InOut;
import cz.ismar.projects.IEdit.io.StoringException;

public class Gesture implements Rescalable, InOutAble
{

	public Element output(Document document) throws StoringException
	{
		Element element = document.createElement("gesture");
		InOut.setAttribute(element, "name_utf_a0", "" + getName());
		InOut.setAttribute(element, "length_short_r0", "" + getLength());
		InOut.setAttribute(element, "ischeck", "" + isCheck);
		if(animation.getType() != 4)
		{
			InOut.setAttribute(element, "speedX_short_r1", "" + getSpeedX());
			InOut.setAttribute(element, "speedY_short_r2", "" + getSpeedY());
			InOut.setAttribute(element, "frameId_short_r3", "" + getFrame().getID());
		}else
		{
			InOut.setAttribute(element, "frameId_short_r1", "" + getFrame().getID());
		}
		return element;
	}

	public void input(Element element)
	{
		if(InOut.verbose)
			System.out.println("Gesture.input");
		try
		{
			int i = Integer.parseInt(element.getAttribute(InOut.compatibilityInput ? "frame-id" : animation.getType() == 4 ? "frameId_short_r1" : "frameId_short_r3"));
			frame = project.frameHolder.getByID(i);
			if(InOut.verbose)
				System.out.println("Gesture.input FRAME:" + frame);

			setName(element.getAttribute(InOut.compatibilityInput ? "name" : "name_utf_a0"));
			setLength(Integer.parseInt(element.getAttribute(InOut.compatibilityInput ? "length" : "length_short_r0")));
			if(animation.getType() != 4)
			{
				setSpeedX(Integer.parseInt(element.getAttribute(InOut.compatibilityInput ? "speed-x" : "speedX_short_r1")));
				setSpeedY(Integer.parseInt(element.getAttribute(InOut.compatibilityInput ? "speed-y" : "speedY_short_r2")));
			}

			String str = element.getAttribute("ischeck");
			isCheck = (str != null && str.equalsIgnoreCase("true"));
		}catch(NumberFormatException numberformatexception)
		{
			throw new NumberFormatException("gesture input error, animation='" + animation.getName() + "': " + numberformatexception.getMessage());
		}
	}

	public void moveFirstTick()
	{
		currentTick = 0;
	}

	public void moveLastTick()
	{
		currentTick = length - 1;
	}

	public boolean lastTick()
	{
		return currentTick + 1 >= length;
	}

	public boolean firstTick()
	{
		return currentTick == 0;
	}

	public void nextTick()
	{
		currentTick++;
	}

	public void prevTick()
	{
		currentTick--;
	}

	public int getTick()
	{
		return currentTick;
	}

	public Gesture(Sprite project1, Animation animation1)
	{
		length = 1;
		currentTick = 0;
		project = project1;
		animation = animation1;
	}

	public Gesture(Frame frame1, Sprite project1, Animation animation1)
	{
		length = 1;
		currentTick = 0;
		frame = frame1;
		project = project1;
		animation = animation1;
	}

	public Gesture copy()
	{
		Gesture gesture = new Gesture(frame, project, animation);
		gesture.speedX = speedX;
		gesture.speedY = speedY;
		gesture.length = length;
		gesture.name = name;
		return gesture;
	}

	public void rescaleTimes(float f)
	{
		if(f > 0.0F)
		{
			speedX *= f;
			speedY *= f;
		}else
		{
			speedX /= -f;
			speedY /= -f;
		}
	}

	public int getSpeedX()
	{
		return speedX;
	}

	public void setSpeedX(int i)
	{
		speedX = i;
	}

	public int getSpeedY()
	{
		return speedY;
	}

	public void setSpeedY(int i)
	{
		speedY = i;
	}

	public int getLength()
	{
		return length;
	}

	public void setLength(int i)
	{
		length = i;
	}

	public Frame getFrame()
	{
		return frame;
	}

	public void setFrame(Frame frame1)
	{
		frame = frame1;
	}

	public String getName()
	{
		if(name == null)
		{
			if(frame == null)
				return "noname";
			else
				// daben:modify
				// 动画的创建后，名字后加上ID
				// return frame.getName();
				// modify:
				return (frame.getName() + "[" + frame.getID() + "]");
			// ~daben
		}else
		{
			return name;
		}
	}

	public String toString()
	{
		return getName() + (isCheck ? "[判定帧]" : "");
	}

	public void setName(String _name)
	{
		// daben:add
		if(SpriteEditor.animationsSaved)
			SpriteEditor.animationsSaved = false;
		// ~daben
		this.name = _name;
	}

	private int speedX;
	private int speedY;
	// 帧延时时间宽度，即此帧延时显示多少帧，或者说这帧重复显示几次(见GestureDurationChangeListener.stateChanged())
	// 帧延时时间为：length * 帧间隔时间(见PlayGestureButtonItemListener)
	private int length;
	private Frame frame;
	private String name;
	private Animation animation;
	public Sprite project;
	// 标记当前为第几次显示，若此帧重复显示n次，则0<=currentTick<n (n=length)
	private int currentTick;

	public boolean isCheck = false;

	public boolean isCheck()
	{
		return isCheck;
	}

	public void setCheck(boolean isCheck)
	{
		this.isCheck = isCheck;
	}

}
