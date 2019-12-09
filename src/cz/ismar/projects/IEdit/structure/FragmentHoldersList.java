
package cz.ismar.projects.IEdit.structure;

import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.io.*;
import java.io.File;
import java.io.PrintStream;
import java.util.*;

import javax.swing.AbstractListModel;
import org.w3c.dom.*;

/**
 * 
 * 精灵所使用到的切片文件列表
 * 
 * 如test.xml精灵使用到如下切面文件
 * -1.xml
 * -2.xml
 * -3.xml
 * -4.xml
 * -5.xml
 * -6.xml
 * @author Lori
 * @version 2.12 2008/1/18
 *
 */
public class FragmentHoldersList extends AbstractListModel
    implements InOutAble
{

    public FragmentHoldersList()
    {
        fragmentHolders = new ArrayList<FragmentHolder>();
        allFragments = new FragmentHolder();
    }

    public boolean add(FragmentHolder fragmentholder)
    {
        if(!fragmentHolders.contains(fragmentholder))
        {
            fragmentHolders.add(fragmentholder);
            allFragments.addAll(fragmentholder.getFragments());
            fireIntervalAdded(this, 0, fragmentHolders.size());
            return true;
        } else
        {
            return false;
        }
    }
    
    /**
     * 替换fragment
     * @param index
     * @param newFragmentHolder
     */
    public void replaceFragmentHolder(int index, FragmentHolder newFragmentHolder)
    {
    	if(newFragmentHolder == null || index < 0 || index >= fragmentHolders.size())
    	{
    		return;
    	}
    	
    	FragmentHolder oldHolder = fragmentHolders.get(index);
    	replaceFragmentHolder(index, oldHolder.getID(), newFragmentHolder);
    }
    
    /**
     * 2009-11-05 fay 更换Fragment Holder
     * 
     * @param index
     * @param newFragmentHolder
     */
    public void replaceFragmentHolder(int index, int oldId, FragmentHolder newFragmentHolder)
    {
    	if(newFragmentHolder == null || index < 0)
    	{
    		return;
    	}

    	newFragmentHolder.setID(oldId);
    	fragmentHolders.set(index, newFragmentHolder);
    }
    
    /**
     * 2009-11-05 fay 更换Fragment Holder
     * @param fragmentHolder
     */
    public void replaceFragmentHolder(FragmentHolder newFragmentHolder, FragmentHolder oldFragmentHolder)
    {
    	if(newFragmentHolder == null || oldFragmentHolder == null)
    	{
    		return;
    	}
    	
    	int index = fragmentHolders.indexOf(oldFragmentHolder);
    	if(index < 0)
    	{
    		return;
    	}
    	
    	replaceFragmentHolder(index, oldFragmentHolder.getID(), newFragmentHolder);
    }
    

    public boolean remove(Collection collection)
    {
        boolean flag = fragmentHolders.removeAll(collection);
        FragmentHolder fragmentholder;
        for(Iterator iterator = collection.iterator(); iterator.hasNext(); allFragments.removeAll(fragmentholder.getFragments()))
            fragmentholder = (FragmentHolder)iterator.next();

        fireIntervalRemoved(this, 0, fragmentHolders.size());
        return flag;
    }

    public ArrayList<FragmentHolder> getFragmentHolders()
    {
        return fragmentHolders;
    }

    /**
     * 用到的所有切片块(切出来的小块)
     * @return
     */
    public FragmentHolder getAllFragments()
    {
        return allFragments;
    }

    /**
     * 得到所有切片
     * @return
     */
    public ArrayList<Fragment> getFragments()
    {
//        ArrayList arraylist = new ArrayList();
//        FragmentHolder fragmentholder;
//        for(Iterator<FragmentHolder> iterator = fragmentHolders.iterator(); iterator.hasNext(); arraylist.addAll(fragmentholder.getFragments()))
//            fragmentholder = (FragmentHolder)iterator.next();
        
    	ArrayList<Fragment> fragments = new ArrayList<Fragment>();
        for(FragmentHolder holder : fragmentHolders)
        {
        	fragments.addAll(holder.getFragments());
        }
        return fragments;
    }

    public int getSize()
    {
        return fragmentHolders.size();
    }

    public FragmentHolder getElementAt(int index)
    {
        return fragmentHolders.get(index);
    }
    
    public FragmentHolder getFragmentHolderByIndex(int index)
    {
    	if(index < 0 || index >= fragmentHolders.size())
    	{
    		return null;
    	}
    	
    	return fragmentHolders.get(index);
    }

    public void defragment()
    {
        int id = 0;
        for(FragmentHolder holder : fragmentHolders)
        {
        	holder.setID(id);
        	id ++;
        }
    }

    /**
     * 从xml加载
     */
    public void input(Element element)
    {
        NodeList nodelist = element.getElementsByTagName("fragmentFile");
        
        // 切片文件列表
        for(int i = 0; i < nodelist.getLength(); i++)
        {
            Element element1 = (Element)nodelist.item(i);
            
            FragmentHolder fragmentholder = new FragmentHolder(Integer.parseInt(element1.getAttribute("id_byte_n0")));
            String s = element1.getAttribute("file_byteIfragmentFiles_n1");
            
            // 图片文件
            File file = new File((new File(SpriteEditor.xmlSavePath)).getParentFile(), FragmentHolder.getName(s) + ".png");
//            System.out.println(file.getAbsolutePath());
            fragmentholder.setImageFile(file);
            
            // 加载数据
            InOut.inFragmentXML(fragmentholder, new File(file.getParentFile(), fragmentholder.getName() + ".xml"));
            add(fragmentholder);
        }

    }

    public Element output(Document document)
        throws StoringException
    {
        Element element = document.createElement("fragmentFiles");
        Element element1;
        for(Iterator<FragmentHolder> iterator = fragmentHolders.iterator(); iterator.hasNext(); element.appendChild(element1))
        {
            FragmentHolder fragmentholder = (FragmentHolder)iterator.next();
            element1 = document.createElement("fragmentFile");
            InOut.setAttribute(element1, "id_byte_n0", "" + fragmentholder.getID());
            InOut.setAttribute(element1, "file_byteIfragmentFiles_n1", FileUtils.getRelativePath(new File(SpriteEditor.xmlSavePath), new File(fragmentholder.getImageFile().getParentFile(), fragmentholder.getName() + ".xml")));
        }

        return element;
    }

    public FragmentHolder getFragmentHolderById(int id)
    {
    	for(FragmentHolder holder : fragmentHolders)
    	{
    		if(holder.getID() == id)
    		{
    			return holder;
    		}
    	}
    	
    	return null;
    }
    
    /**
     * 根据Fragment File, 刷新All Fragment
     */
    public void refreshAllFramgment()
    {
    	allFragments = new FragmentHolder();
    	for(FragmentHolder holder : fragmentHolders)
    	{
    		allFragments.addAll(holder.getFragments());
    	}
    }

    /**
     * 用到的各个切片
     */
    private ArrayList<FragmentHolder> fragmentHolders;
    
    
    /**
     * 储存 精灵编辑器中用到的所有切片块，
     * 
     * 如加载了1.xml,2.xml,3.xml;
     * 
     * 则allFragmentsHolder包括1.xml,2.xml,3.xml中所有的切片块
     */
    public FragmentHolder allFragments;
}
