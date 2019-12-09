/**
 * 2007-9-14 daben
 * - add
 * --> Fragment.setCurrentID(nodelist.getLength());
 * 
 * 2007-10-10
 * - remove
 * --> fragment.defragmentID(i) int defragment()
 */

package cz.ismar.projects.IEdit.structure;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.ImageIcon;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sixlegs.png.PngImage;

import cz.ismar.projects.IEdit.io.InOut;
import cz.ismar.projects.IEdit.io.StoringException;

public class FragmentHolder implements Rescalable, InOutAble
{

	public void rescaleTimes(float f)
	{
		Fragment fragment;
		for(Iterator<Fragment> iterator = fragments.iterator(); iterator.hasNext(); fragment.rescaleTimes(f))
		{
			fragment = iterator.next();
		}

	}

	public Element output(Document document) throws StoringException
	{
		Element element = document.createElement("fragment_arr");
		InOut.setAttribute(element, "name_byteIDfragmentImgs_r0", getName());

		Element element1;
		for(Fragment fragment : fragments)
		{
			element1 = fragment.output(document);
			element.appendChild(element1);
		}

		// for(Iterator<Fragment> iterator = fragments.iterator();
		// iterator.hasNext(); element.appendChild(element1))
		// {
		// Fragment fragment = (Fragment)iterator.next();
		// element1 = fragment.output(document);
		// }

		return element;
	}

	public void input(Element element)
	{
		NodeList nodelist = element.getElementsByTagName("fragment");

		for(int i = 0; i < nodelist.getLength(); i++)
		{
			Element element1 = (Element) nodelist.item(i);
			Fragment fragment = new Fragment(this);
			fragment.input(element1);
			fragments.add(fragment);
		}

		// daben:add
		// 让Load或Reload后，新切片的名称以当前切片个数为基准命名，见FragmentPanel.createFragment()
		// 2007-10-11，去掉，使用新方法赋切片ID，见Fragment.input()
		// Fragment.setCurrentID(nodelist.getLength());
		// ~daben
	}

	public FragmentHolder()
	{
		index = -1;
		// ID = ++currentID;
		fragments = new ArrayList<Fragment>();
	}

	public FragmentHolder(int i)
	{
		index = -1;
		ID = i;
		// if(i > currentID)
		// currentID = i;
		fragments = new ArrayList<Fragment>();
	}

	// "整理碎片" - 按"All frags"里的列表顺序重新设置fragment的ID(第一个设为0)
	public void defragment()
	{
		Iterator<Fragment> iterator = fragments.iterator();
		for(int i = 0; iterator.hasNext(); i++)
		{
			Fragment fragment = iterator.next();
			// daben:remove 不使用原来的生成ID的方法
			// fragment.defragmentID(i);
			// ~daben
		}
	}

	// 检查是否可以保存切片
	//切片id是否按顺序赋值，必须为从0开始
	public String isCanSave()
	{
		String strReturn = "";
		Iterator<Fragment> iterator = fragments.iterator();
		for(int i = 0; iterator.hasNext(); i++)
		{
			Fragment fragment = iterator.next();
			if(i != fragment.getID())
			{
				strReturn = "切片的id没有按顺序从0开始排序,请重新调整。";
				break;
			}
			if(fragment.isOutOfImage())
			{
				strReturn = fragment.getName()+"切片的尺寸超过了原图的大小，请重新编辑该切片。";
				break;
			}
		}
		return strReturn;
	}
	
	/**
	 * 通过id得到fragment
	 * 
	 * @param i
	 * @return
	 */
	public Fragment getFragmentById(int i)
	{
		for(Fragment fragment : fragments)
		{
			if(fragment.getID() == i)
			{
				return fragment;
			}
		}

		return null;
	}

	public Fragment get()
	{
		if(index > -1 && index < fragments.size())
			return (Fragment) fragments.get(index);
		else
			return null;
	}

	public Fragment get(int i)
	{
		if(i > -1 && i < fragments.size())
			return (Fragment) fragments.get(i);
		else
			return null;
	}

	public Fragment remove(int i)
	{
		return (Fragment) fragments.remove(i);
	}

	public boolean removeAll(Collection collection)
	{
		return fragments.removeAll(collection);
	}

	public Fragment remove()
	{
		return (Fragment) fragments.remove(index);
	}

	public void addAll(Collection collection)
	{
		if(index == -1)
			index = 0;
		fragments.addAll(collection);
	}

	public void add(Fragment fragment)
	{
		if(index == -1)
			index = 0;
		fragments.add(fragment);
	}

	public int size()
	{
		return fragments.size();
	}

	public ArrayList<Fragment> getFragments()
	{
		return fragments;
	}

	public void setIndex(int i)
	{
		index = i;
	}

	public int getIndex()
	{
		return index;
	}

	public static String getName(String s)
	{
		return s.substring(0, s.length() - 4);
	}

	public static String getName(File file)
	{
		return getName(file.getName());
	}
	
	/**
	 * 得到切片的类型
	 * 文件类格式: 类型_xxx.png
	 * @return
	 */
	public String getPartName()
	{
		String name = imageFile.getName();
		int index = name.indexOf("_");
		if(index < 0)
		{
			return null;
		}else
		{
			return name.substring(0, index);
		}
	}

	public String getName()
	{
		return getName(imageFile);
	}

	public File getImageFile()
	{
		return imageFile;
	}

	public void setImageFile(File file)
	{
		imageFile = file;
		try
		{
			image = (new PngImage()).read(file);
			icon = new ImageIcon(image);
		}catch(IOException e)
		{
			e.printStackTrace();
			// System.out.println("Load Image File Error!");
		}
	}

	public java.awt.Image getImage()
	{
		return image;
	}

	public ImageIcon getIcon()
	{
		return icon;
	}

	public String toString()
	{
		if(imageFile != null)
			return getName() + (nameExtends==null?"":nameExtends);
		else
			return "[empty]";
	}

	public boolean equals(Object obj)
	{
		try
		{
			return (obj instanceof FragmentHolder) && ((FragmentHolder) obj).imageFile.getCanonicalPath().equals(getImageFile().getCanonicalPath());
		}catch(Exception ex)
		{
			return false;
		}
		// Exception exception;
		// exception;
		// return false;
	}

	public int getID()
	{
		return ID;
	}

	public void setID(int id)
	{
		ID = id;
		// currentID = id;
	}

	public boolean moveTopFragment()
	{
		if(index > 0)
		{
			fragments.add(0, fragments.remove(index));
			index = 0;
			return true;
		}else
		{
			return false;
		}
	}

	public boolean moveUpFragment()
	{
		if(index > 0)
		{
			fragments.add(index - 1, fragments.remove(index));
			index--;
			return true;
		}else
		{
			return false;
		}
	}

	public boolean moveDownFragment()
	{
		if(index < fragments.size() - 1)
		{
			fragments.add(index + 1, fragments.remove(index));
			index++;
			return true;
		}else
		{
			return false;
		}
	}

	public boolean moveBottomFragment()
	{
		if(index < fragments.size() - 1)
		{
			fragments.add(fragments.remove(index));
			index = fragments.size() - 1;
			return true;
		}else
		{
			return false;
		}
	}

	/**
	 * 查找fragments, 得到最大的ID, 然后+1, return
	 * 
	 * @return
	 */
	public int getNextFragmentId()
	{
		int maxId = -1;
		for(Fragment fragment : fragments)
		{
			if(fragment.getID() > maxId)
			{
				maxId = fragment.getID();
			}
		}

		return maxId + 1;
	}
	
	public boolean isVisible()
	{
		return isVisible;
	}

	public void setVisible(boolean isVisible)
	{
		this.isVisible = isVisible;
	}
	


	public String getNameExtends()
	{
		return nameExtends;
	}

	public void setNameExtends(String nameExtends)
	{
		this.nameExtends = nameExtends;
	}

	/**
	 * 计算占用内存大小
	 * 
	 * ids	byte[]	N bytes
	 * xs	byte[]	N bytes
	 * ys	byte[]	N bytes
	 * widths	byte[]	N bytes
	 * heights	byte[]	N bytes
	 * avatarXs	byte[]	N bytes
	 * avatarYs	byte[]	N bytes
	 * name	String	UTF
	 * images	Image	width * height * 4
	 * 
	 * @return
	 */
	public int calculateMemorySize()
	{
		int size = 0;
		
		// ids
		size += fragments.size();
		
		// xs
		size += fragments.size();
		
		// ys
		size += fragments.size();
		
		// widths
		size += fragments.size();
		
		// heights
		size += fragments.size();
		
		// avatar xs
		size += fragments.size();
		
		// avatar ys
		size += fragments.size();
		
		// name
		size += getName().length() * 2;
		
		// image
		if(image != null)
		{
			size += image.getWidth(null) * image.getHeight(null) * 4;
		}
		
		return size;	
	}
	
	/**
	 * 得到图片占用资源
	 * @return
	 */
	public int caculateImageMemorySize()
	{
		int size = 0;
		
		// image
		if(image != null)
		{
			size += image.getWidth(null) * image.getHeight(null) * 4;
		}
		
		return size;	
	}


	private ArrayList<Fragment> fragments;
	private int index;
	// private static int currentID;
	private int ID;
	private File imageFile;
	private ImageIcon icon;
	private java.awt.Image image;
	
	/** 2009-11-18 是否可见 */
	private boolean isVisible = true;
	/** 名字的扩展, for: 隐藏*/
	private String nameExtends = null;
}
