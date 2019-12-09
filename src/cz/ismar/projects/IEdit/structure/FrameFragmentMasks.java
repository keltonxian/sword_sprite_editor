package cz.ismar.projects.IEdit.structure;

import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.io.InOut;
import cz.ismar.projects.IEdit.io.StoringException;
import java.io.PrintStream;
import java.util.*;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import org.w3c.dom.*;

/**
 * 帧里的 组合块，是否为隐藏效果；
 * 
 * @author Lori
 * 
 */
public class FrameFragmentMasks implements InOutAble
{
	private static class MaskSet implements InOutAble
	{

		public void setName(String s)
		{
			name = s;
		}

		public String toString()
		{
			return name;
		}

		public Vector getMasks()
		{
			return masks;
		}

		public Element output(Document document) throws StoringException
		{
			Element element = document.createElement("maskSet");
			element.setAttribute("name", name);
			for(int i = 0; i < masks.size(); i++)
			{
				FrameFragment framefragment = (FrameFragment) masks.elementAt(i);
				Element element1 = document.createElement("mask");
				element1.setAttribute("frameID", Integer.toString(framefragment.getParentFrame().getID()));
				element1.setAttribute("frameFragmentID", Integer.toString(framefragment.getID()));
				element.appendChild(element1);
			}

			return element;
		}

		public void input(Element element)
		{
			name = element.getAttribute("name");
			NodeList nodelist = element.getElementsByTagName("mask");
			for(int i = 0; i < nodelist.getLength(); i++)
			{
				Element element1 = (Element) nodelist.item(i);
				int frameId = Integer.parseInt(element1.getAttribute("frameID"));
				try
				{
//					Frame frame = InOut.curentlyReadedSprite.frameHolder.getByID(frameId);
					Frame frame = SpriteEditor.sprite.frameHolder.getByID(frameId);
					int frameFragmentId = Integer.parseInt(element1.getAttribute("frameFragmentID"));
					ArrayList<FrameFragment> frameFragments = frame.getFrameFragments();
					for(FrameFragment frameFragment : frameFragments)
					{
						if(frameFragment.getID() == frameFragmentId)
						{
							masks.addElement(frameFragment);
							break;
						}
					}
					
//					int l = 0;
//					do
//					{
//						if(l >= frameFragments.size())
//							continue label0;
//						FrameFragment framefragment = (FrameFragment) frameFragments.get(l);
//						if(framefragment.getID() == frameFragmentId)
//						{
//							masks.addElement(framefragment);
//							continue label0;
//						}
//						l++;
//					}while(true);
				}catch(IllegalArgumentException illegalargumentexception)
				{
					System.out.println("WARNING: skiping mask - " + illegalargumentexception.toString());
				}
			}

		}

		String name;
		Vector masks;

		public MaskSet()
		{
			masks = new Vector();
		}

		public MaskSet(MaskSet maskset, String s)
		{
			name = s;
			masks = (Vector) maskset.masks.clone();
		}

		public MaskSet(String s)
		{
			name = s;
			masks = new Vector();
		}
	}

	public void moveTop(int i)
	{
		if(i > 0 && i < comboBoxModel.getSize())
		{
			Object obj = comboBoxModel.getElementAt(i);
			comboBoxModel.removeElementAt(i);
			comboBoxModel.insertElementAt(obj, 0);
		}
	}

	public void moveBottom(int i)
	{
		int j = comboBoxModel.getSize() - 1;
		if(i >= 0 && i < j)
		{
			Object obj = comboBoxModel.getElementAt(i);
			comboBoxModel.removeElementAt(i);
			comboBoxModel.insertElementAt(obj, j);
		}
	}

	public void moveUp(int i)
	{
		if(i > 0 && i < comboBoxModel.getSize())
		{
			Object obj = comboBoxModel.getElementAt(i);
			comboBoxModel.removeElementAt(i);
			comboBoxModel.insertElementAt(obj, i - 1);
		}
	}

	public void moveDown(int i)
	{
		if(i >= 0 && i < comboBoxModel.getSize() - 1)
		{
			Object obj = comboBoxModel.getElementAt(i);
			comboBoxModel.removeElementAt(i);
			comboBoxModel.insertElementAt(obj, i + 1);
		}
	}

	public FrameFragmentMasks()
	{
		addConfiguration("default");
	}

	public boolean isEnabled()
	{
		// return isEnabled;

		// TODO,暂时 全开 【隐藏】 参数
		return true;
	}

	public void setEnabled(boolean flag)
	{
		isEnabled = flag;
	}

	public void copyConfiguration(int i, String s)
	{
		MaskSet maskset = new MaskSet((MaskSet) comboBoxModel.getElementAt(i), s);
		comboBoxModel.addElement(maskset);
	}

	public void addConfiguration(String s)
	{
		comboBoxModel.addElement(new MaskSet(s));
	}

	public void removeConfiguration(int i)
	{
		if(i >= 0 && i < comboBoxModel.getSize())
		{
			if(comboBoxModel.getSize() == 1)
				addConfiguration("default");
			comboBoxModel.removeElementAt(i);
		}
	}

	public void renameConfiguration(int i, String s)
	{
		MaskSet maskset = (MaskSet) comboBoxModel.getElementAt(i);
		maskset.setName(s);
		if(maskset == comboBoxModel.getSelectedItem())
		{
			comboBoxModel.insertElementAt(maskset, i);
			comboBoxModel.removeElementAt(i);
			comboBoxModel.setSelectedItem(maskset);
		}
	}

	public ComboBoxModel getComboBoxModel()
	{
		return comboBoxModel;
	}

	public String getName(int i)
	{
		return comboBoxModel.getElementAt(i).toString();
	}

	public boolean isHiden(FrameFragment framefragment)
	{
		if(!isEnabled() || comboBoxModel.getSize() == 0)
		{
			return false;
		}else
		{
			Vector vector = ((MaskSet) comboBoxModel.getSelectedItem()).getMasks();
			return vector != null && vector.contains(framefragment);
		}
	}

	public void changeHidenState(FrameFragment framefragment)
	{
		Vector vector = ((MaskSet) comboBoxModel.getSelectedItem()).getMasks();
		if(!vector.contains(framefragment))
		{
			vector.add(framefragment);
		}else
		{
			vector.remove(framefragment);
		}
	}

	public void removeHiden(Frame frame)
	{
		if(frame != null)
		{
			FrameFragment framefragment;
			for(Iterator iterator = frame.getFrameFragments().iterator(); iterator.hasNext(); removeHiden(framefragment))
				framefragment = (FrameFragment) iterator.next();

		}
	}

	public void removeHiden(FrameFragment framefragment)
	{
		for(int i = 0; i < comboBoxModel.getSize(); i++)
		{
			Vector vector = ((MaskSet) comboBoxModel.getElementAt(i)).getMasks();
			vector.remove(framefragment);
		}

	}

	public void addHiden(FrameFragment framefragment)
	{
		Vector vector = ((MaskSet) comboBoxModel.getSelectedItem()).getMasks();
		if(!vector.contains(framefragment))
			vector.add(framefragment);
		else
			System.err.println("WARNING: mask already contain frame fragment");
	}

	public String getMasks(Frame frame)
	{
		int ai[] = new int[comboBoxModel.getSize()];
		boolean flag = false;
		for(int i = 0; i < ai.length; i++)
		{
			Vector vector = ((MaskSet) comboBoxModel.getElementAt(i)).getMasks();
			int k = -1;
			int l = 1;
			for(int i1 = frame.size() - 1; i1 >= 0; i1--)
			{
				FrameFragment framefragment = frame.get(i1);
				if(vector.contains(framefragment))
				{
					flag = true;
					k &= ~l;
				}
				l <<= 1;
			}

			ai[i] = k;
		}

		if(flag)
		{
			StringBuffer stringbuffer = new StringBuffer();
			for(int j = 0; j < ai.length; j++)
			{
				stringbuffer.append(ai[j]);
				if(j < ai.length - 1)
					stringbuffer.append(",");
			}

			return stringbuffer.toString();
		}else
		{
			return "";
		}
	}

	public void copyMasksForFrame(Frame frame, Frame frame1)
	{
		List list = frame.getFrameFragments();
		for(int i = 0; i < list.size(); i++)
		{
			FrameFragment framefragment = (FrameFragment) list.get(i);
			if(isHiden(framefragment))
				addHiden((FrameFragment) frame1.getFrameFragments().get(i));
		}

	}

	public Element output(Document document) throws StoringException
	{
		if(InOut.verbose)
			System.out.println("FrameFragmentMasks.output");

		Element element = document.createElement("maskSets_skip");
		for(int i = 0; i < comboBoxModel.getSize(); i++)
		{

			MaskSet maskset = (MaskSet) comboBoxModel.getElementAt(i);
			element.appendChild(maskset.output(document));

		}

		return element;
	}

	public void input(Element element)
	{
		if(InOut.verbose)
			System.out.println("FrameFragmentMasks.input");
		NodeList nodelist = element.getElementsByTagName("maskSet");
		if(nodelist.getLength() > 0)
			comboBoxModel.removeAllElements();
		for(int i = 0; i < nodelist.getLength(); i++)
		{
			Element element1 = (Element) nodelist.item(i);
			MaskSet maskset = new MaskSet();
			maskset.input(element1);
			comboBoxModel.addElement(maskset);
		}

	}

	private static boolean isEnabled = false;
	private static DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel();

}
