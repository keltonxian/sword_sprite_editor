package cz.ismar.projects.IEdit.structure;

import cz.ismar.projects.IEdit.io.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.PrintStream;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 2007-9-4 daben #see daben
 *  //切片宽超出图象边界不让它抛出异常
 * - in method output()
   - modify 
     throw new StoringNumberException("posX+width='" + (getX() + getWidth()) + "' > imageWidth='" + fragmentHolder.getImage().getWidth(null) + "'.");
     InOut.setAttribute(element, "w_byte_r3", "" + getWidth());
	-->
	 InOut.setAttribute(element, "w_byte_r3", "" + (fragmentHolder.getImage().getWidth(null) - getX()));
    else
	 InOut.setAttribute(element, "w_byte_r3", "" + getWidth());
    
     throw new StoringNumberException("posY+height='" + (getY() + getHeight()) + "' > imageHeight='" + fragmentHolder.getImage().getHeight(null) + "'.");
     InOut.setAttribute(element, "h_byte_r4", "" + getHeight());
	-->
	 InOut.setAttribute(element, "h_byte_r4", "" + (fragmentHolder.getImage().getHeight(null) - getY()));
    else
	 InOut.setAttribute(element, "h_byte_r4", "" + getHeight());
	
 * 
 * 2007-10-10 daben
 * - add in input()
 * --> if (ID > currentID)
 *          currentID = ID;
 * 
 * 
 * 
 * */
public class Fragment implements Rescalable, InOutAble
{
    public Element output(Document document)
        throws StoringException
    {
        Element element = document.createElement("fragment");
        try
        {
            InOut.setAttribute(element, "id_byte_a0", "" + getID());
            InOut.setAttribute(element, "name_utf_a1", getName());
            InOut.setAttribute(element, "index_shortIDfragments_n0", fragmentHolder.getName() + "_" + getID());
            InOut.setAttribute(element, "x_uByte_r1", "" + getX());
            InOut.setAttribute(element, "y_uByte_r2", "" + getY());
            if(getX() + getWidth() > fragmentHolder.getImage().getWidth(null))
            {
            	//daben:modify 切片宽超出图象边界不让它抛出异常
            	InOut.setAttribute(element, "w_byte_r3", "" + (fragmentHolder.getImage().getWidth(null) - getX()));
            }else
            {
            	InOut.setAttribute(element, "w_byte_r3", "" + getWidth());
            }
            
            if(getY() + getHeight() > fragmentHolder.getImage().getHeight(null))
            {
            	//daben:modify 切片高超出图象边界不让它抛出异常
            	InOut.setAttribute(element, "h_byte_r4", "" + (fragmentHolder.getImage().getHeight(null) - getY()));
            }else
            {
            	InOut.setAttribute(element, "h_byte_r4", "" + getHeight());
            }
            
            // 2009-11-05 fay 输出(avatarX, avatarY)
            element.setAttribute("avatarX", "" + avatarX);
            element.setAttribute("avatarY", "" + avatarY);
//            InOut.setAttribute(element, "avatarX", "" + avatarX);
//            InOut.setAttribute(element, "avatarY", "" + avatarY);
        }
        catch(StoringNumberException storingnumberexception)
        {
            String s = storingnumberexception.getMessage() + " Fragment '" + getName() + "' id='" + getID() + "'.";
            throw new StoringException(s);
        }
        return element;
    }

    /**
     * 从element加载数据
     */
    public boolean isOutOfImage()
    {
    	 if(getX() + getWidth() > fragmentHolder.getImage().getWidth(null) ||
    			 getY() + getHeight() > fragmentHolder.getImage().getHeight(null) )
         {
         	return true;
         }
    	 
    	 return false;
    }
    /**
     * 从element加载数据
     */
    public void input(Element element)
    {
        boolean flag = element.getAttribute("id").length() > 0;
        
        id = Integer.parseInt(element.getAttribute(flag ? "id" : "id_byte_a0"));
        x = Integer.parseInt(element.getAttribute(flag ? "x" : "x_uByte_r1"));
        y = Integer.parseInt(element.getAttribute(flag ? "y" : "y_uByte_r2"));
        width = Integer.parseInt(element.getAttribute(flag ? "width" : "w_byte_r3"));
        height = Integer.parseInt(element.getAttribute(flag ? "height" : "h_byte_r4"));
        name = element.getAttribute(flag ? "name" : "name_utf_a1");
        
        // 2009-11-06 fay 读出avatarX, avatarY
        avatarX = InOut.parseByte(element, "avatarX", (byte) 0);
        avatarY = InOut.parseByte(element, "avatarY", (byte) 0);
    }

    public BufferedImage getImage()
    {
        return image;
    }

    /**
     * 根据"整切片图" 与 当前的x, y, width, height 生成切片图
     */
    public void updateImage()
    {
        image = new BufferedImage(getWidth(), getHeight(), 2);
        image.getGraphics().drawImage(getFragmentHolder().getImage(), -getX(), -getY(), null);
    }

    public int getX()
    {
        return x;
    }

    public void setX(int i)
    {
        x = i;
    }

    public int getY()
    {
        return y;
    }

    public void setY(int i)
    {
        y = i;
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int i)
    {
        width = i;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int i)
    {
        height = i;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String s)
    {
        name = s;
    }

    public int getID()
    {
        return id;
    }

    public void setID(int i)
    {
        id = i;
    }

    public Fragment(FragmentHolder fragmentholder)
    {
        fragmentHolder = fragmentholder;
    }

    public Fragment(FragmentHolder fragmentholder, int _x, int _y, int _width, int _height, String _name)
    {
        fragmentHolder = fragmentholder;
        id = fragmentholder.getNextFragmentId();
        x = _x;
        y = _y;
        width = _width;
        height = _height;
        name = _name;
    }

    public Fragment(FragmentHolder fragmentholder, int _id, int _x, int _y, int _width, int _height, String name)
    {
        fragmentHolder = fragmentholder;
        id = _id;
        x = _x;
        y = _y;
        width = _width;
        height = _height;
        this.name = name;
    }

    public Fragment(int _id, int _x, int _y, int _width, int _height, String _name)
    {
        id = _id;
        x = _x;
        y = _y;
        width = _width;
        height = _height;
        name = _name;
    }

    public Rectangle getRectangle()
    {
        return new Rectangle(x, y, width, height);
    }

    public void rescaleTimes(float f)
    {
        if(f > 0.0F)
        {
            x *= f;
            y *= f;
            width *= f;
            height *= f;
        } else
        {
            x /= -f;
            y /= -f;
            width /= -f;
            height /= -f;
        }
//        rectangle = new Rectangle(x, y, width, height);
    }
    
    /**
     * 2009-11-11 fay 移动avatar点
     * @param offsetX
     * @param offsetY
     */
    public void moveAvatarPoint(int offsetX, int offsetY)
    {
    	avatarX += offsetX;
    	avatarY += offsetY;
    }
    public int getAvatarX()
	{
		return avatarX;
	}
    public int getAvatarY()
	{
		return avatarY;
	}
    
    public String toString()
    {
        return name + (nameExtend==null?"":nameExtend);
    }

    public FragmentHolder getFragmentHolder()
    {
        return fragmentHolder;
    }
    
    public boolean isVisible()
	{
		return isVisible;
	}
    public void setVisible(boolean isVisible)
	{
		this.isVisible = isVisible;
	}
    
    public void setNameExtend(String nameExtend)
	{
		this.nameExtend = nameExtend;
	}

    private FragmentHolder fragmentHolder;
    private BufferedImage image;
    
    private int id;
    private int x;
    private int y;
    private int width;
    private int height;
    private String name;
    
    /** 2009-11-03 fay 变装的钉点 */
    private int avatarX = 0;
    private int avatarY = 0;
    
    /** 2009-11-30 是否可见 */
    private boolean isVisible = true;
    private String nameExtend = null;
}
