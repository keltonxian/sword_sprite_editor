package cz.ismar.projects.IEdit.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;

import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.structure.Fragment;

/**
 * 编辑切片的Component
 * 
 * @author Lori
 *
 */
public class FragmentEditComponent extends CornerGridComponent implements KeyListener
{

    public FragmentEditComponent()
    {
        resizing = 0;
        originalImageWidth = 0;
        originalImageHeight = 0;
        //daben:add add KeyListener
        setFocusable(true);
        addKeyListener(this);
        //~daben
    }

    public void setImageSize(int width, int height)
    {
        originalImageWidth = width;
        originalImageHeight = height;
    }

    public int screenToPixel(int i)
    {
        return i / SpriteEditor.scale;
    }

    public int screenToPoint(int i)
    {
        return (i + SpriteEditor.scale / 2) / SpriteEditor.scale;
    }

    private int getSelectionCornerSize()
    {
        return Math.max(SpriteEditor.scale / 2, 2);
    }

    protected void paintComponent(Graphics g)
    {
    	// 不透明 -> 画背景色
        if(isOpaque())
        {
            g.setColor(backgroundColor);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        
        // 画整张切片图
        g.setColor(Color.GRAY);
        if(SpriteEditor.sprite.fragmentHolder.getImage() != null)
        {
            AffineTransform affinetransform = new AffineTransform();
            affinetransform.scale(SpriteEditor.scale, SpriteEditor.scale);
            ((Graphics2D)g).drawImage(SpriteEditor.sprite.fragmentHolder.getImage(), affinetransform, this);
        }
        super.paintComponent(g);
        
        // 画跟随鼠标的十字架
        drawCross(g);
        
        // 画切片的框
        g.setColor(Color.GRAY);
//        g.setXORMode(Color.RED);
        for(Fragment fragment : SpriteEditor.sprite.fragmentHolder.getFragments())
        {
            // 如果是选中的框, <左上>与<右下>有标志
            drawRect(g, fragment.getRectangle(), fragment == selectedFragment, SpriteEditor.scale);
        }

        // 画当前新建的框, <左上>与<右下>有标志
         if(newFragmentRectangle != null)
         {
//            drawRect(g, newFragmentRectangle, true, SpriteEditor.scale);
        	 drawShadowRect(g, newFragmentRectangle, SpriteEditor.scale);
         }
    }

    /**
     * 设置选中的切片
     * @param index
     */
    public void selectRect(int index)
    {
//        newFragmentRectangle = null;
        newFragmentRectangle = null;
        
        if(index >= 0 && index < SpriteEditor.sprite.fragmentHolder.getFragments().size())
        {
            selectedFragment = (Fragment)SpriteEditor.sprite.fragmentHolder.getFragments().get(index);
//            selectedRectangle = selectedFragment.getRectangle();
        } else
        {
//            selectedRectangle = null;
            selectedFragment = null;
        }
        repaint();
    }

    /**
     * 画矩形
     * @param g
     * @param rectangle
     * @param flag
     * 			true -> 画<左上角> 与 <右下角> 的小边角
     * @param scale
     * 			缩放倍数
     */
    public void drawRect(Graphics g, Rectangle rectangle, boolean flag, int scale)
    {
    	g.setColor(Color.red);
        g.drawRect(rectangle.x * scale + 1, 
        		rectangle.y * scale + 1, 
        		rectangle.width * scale - 2, 
        		rectangle.height * scale - 2);
        
        // <左上角> 与 <右下角> 的小边角
        if(flag)
        {
            g.fillRect(rectangle.x * scale - getSelectionCornerSize(), 
            		rectangle.y * scale - getSelectionCornerSize(), 
            		1 + 2 * getSelectionCornerSize(), 
            		1 + 2 * getSelectionCornerSize());
            g.fillRect((rectangle.x * scale + rectangle.width * scale) - getSelectionCornerSize(), 
            		(rectangle.y * scale + rectangle.height * scale) - getSelectionCornerSize(), 
            		1 + 2 * getSelectionCornerSize(), 
            		1 + 2 * getSelectionCornerSize());
        }
        
//        g.setColor(new Color(0x33000000, true));
////        g.setColor(new Color(0x7700cc00, true));
//        g.fillRect(rectangle.x * scale + 1, 
//        		rectangle.y * scale + 1, 
//        		rectangle.width * scale - 2, 
//        		rectangle.height * scale - 2);
    }
    
    /**
     * 画矩形
     * @param g
     * @param rectangle
     * @param flag
     * 			true -> 画<左上角> 与 <右下角> 的小边角
     * @param scale
     */
    public void drawShadowRect(Graphics g, Rectangle rectangle, int scale)
    {
    	g.setColor(new Color(0x77000000, true));
    	
    	g.fillRect(rectangle.x * scale + 1, 
        		rectangle.y * scale + 1, 
        		rectangle.width * scale - 2, 
        		rectangle.height * scale - 2);
    	
//        g.drawRect(rectangle.x * scale + 1, 
//        		rectangle.y * scale + 1, 
//        		rectangle.width * scale - 2, 
//        		rectangle.height * scale - 2);
    }
    
    

    public Dimension getPreferredSize()
    {
        if(SpriteEditor.sprite.fragmentHolder.getIcon() != null)
            return new Dimension(SpriteEditor.sprite.fragmentHolder.getIcon().getIconWidth() * SpriteEditor.scale, SpriteEditor.sprite.fragmentHolder.getIcon().getIconHeight() * SpriteEditor.scale);
        else
            return super.getPreferredSize();
    }

    public void mousePressed(MouseEvent mouseevent)
	{
		int mouseX = screenToPoint(mouseevent.getX());
		int mouseY = screenToPoint(mouseevent.getY());
		resizing = getResizeType(mouseX, mouseY);
		
		// 调整选中的框的大小, early return!
		if(resizing != 0)
		{
			// 选中了调整区的 哪个角 【左上角为1 ， 右下角为2】，
			// System.out.println("WysiwygFragmentComponent.mousePressed isInSelectedCorner: "
			// + getResizeType(mouseX, mouseY));
			return;
		}
		
		// 新建切片
		int x = screenToPixel(mouseevent.getX());
		int y = screenToPixel(mouseevent.getY());

		// 查看选中了哪个切片
		boolean flag = false;
		for(int i = 0; i < SpriteEditor.sprite.fragmentHolder.getFragments().size() && !flag; i++)
		{
			Fragment fragment = (Fragment) SpriteEditor.sprite.fragmentHolder.getFragments().get(i);
			if(fragment.getRectangle().contains(x, y))
			{
				SpriteEditor.fragmentPanel.fragmentTable.getSelectionModel().setSelectionInterval(i, i);

				// 左边的切片列表设置选中
				SpriteEditor.sprite.fragmentHolder.setIndex(i);
				flag = true;
			}
		}

		// 如果没有切片被选中, 清空切片列表
		if(!flag)
		{
			SpriteEditor.fragmentPanel.fragmentTable.getSelectionModel().clearSelection();
			SpriteEditor.sprite.fragmentHolder.setIndex(-1);
		}

		// 新建新的切片框
		newFragmentRectangle = new Rectangle(x, y, 0, 0);
		repaint();
	}

    /**
     * 得到resize 的类型
     * 
     * @param x
     * @param y
     * @return
     */
    public int getResizeType(int x, int y)
    {
        if(selectedFragment != null)
        {
        	// 调整左上角
            if(selectedFragment.getX() == x && selectedFragment.getY() == y)
            {
                return RESIZE_TOP;
            }else if(((selectedFragment.getX() + selectedFragment.getWidth()) == x) 
            		&& ((selectedFragment.getY() + selectedFragment.getHeight() == y)))
            {
            	// 调整右下角
            	return RESIZE_BOTTOM;
            }else
            {
            	return 0;
            }
        } else
        {
            return 0;
        }
    }

    public void mouseDragged(MouseEvent mouseevent)
    {
    	switch(resizing)
    	{
    	case RESIZE_BOTTOM:		// resize右下角 -> setWidth & setHeight
    		int x = Math.min(Math.max(0, screenToPoint(mouseevent.getX())), originalImageWidth);
    		int y = Math.min(Math.max(0, screenToPoint(mouseevent.getY())), originalImageHeight);
    		selectedFragment.setWidth(x - selectedFragment.getX());
    		selectedFragment.setHeight(y - selectedFragment.getY());
    		// daben:add
    		if (SpriteEditor.fragmentsSaved)
    			SpriteEditor.fragmentsSaved = false;
    		// ~daben
    		repaint();
    		break;
    	case RESIZE_TOP:		// resize左上角 -> setX & setY
    		x = Math.max(0, screenToPoint(mouseevent.getX()));
    		y = Math.max(0, screenToPoint(mouseevent.getY()));
    		selectedFragment.setWidth((selectedFragment.getWidth() + selectedFragment.getX()) - x);
    		selectedFragment.setHeight((selectedFragment.getHeight() + selectedFragment.getY()) - y);
    		// daben:add
    		if (SpriteEditor.fragmentsSaved)
    			SpriteEditor.fragmentsSaved = false;
    		// ~daben
    		selectedFragment.setX(x);
    		selectedFragment.setY(y);
    		repaint();
    		break;
    	default:
    		updateNewSize(mouseevent);
    	}
    }

    public void mouseReleased(MouseEvent mouseevent)
    {
    	// 放开鼠标, 如果有resize, 则清空resize
        if(resizing != 0)
        {
            resizing = 0;
            // 更新切片的图 
            selectedFragment.updateImage();
            SpriteEditor.framePanel.allFragmentListModel.update();
            SpriteEditor.framePanel.repaint();
        }
    }

    void updateNewSize(MouseEvent mouseevent)
    {
    	// 拖动新建的切片框
        if(resizing == 0)
        {
            int px = screenToPixel(mouseevent.getX());
            int py = screenToPixel(mouseevent.getY());
            if(newFragmentRectangle != null)
            {
            	newFragmentRectangle.setSize(px - newFragmentRectangle.x, py - newFragmentRectangle.y);
                updateDrawableRect(getWidth(), getHeight());
            }
        }
        repaint();
    }

    private void updateDrawableRect(int i, int j)
    {
        int x = newFragmentRectangle.x;
        int y = newFragmentRectangle.y;
        int width = newFragmentRectangle.width;
        int height = newFragmentRectangle.height;
        if(width < 0)
        {
            width = 0 - width;
            //daben:modify 原:若从右往左画切片，画出的切片框会往右偏移一格
            //k = (k - i1) + 1;
            //modify:
            x = x - width;
            //~daben
            if(x < 0)
            {
                width += x;
                x = 0;
            }
        }
        if(height < 0)
        {
            height = 0 - height;
            //daben:modify 原:若从下往上画切片，画出的切片会往下偏移一格
            //l = (l - j1) + 1;
            //modify:
            y = y - height;
            //~daben
            if(y < 0)
            {
                height += y;
                y = 0;
            }
        }
        newFragmentRectangle.x = x;
        newFragmentRectangle.y = y;
        newFragmentRectangle.width = width;
        newFragmentRectangle.height = height;
    }
    
    public void mouseEntered(MouseEvent mouseevent)
    {
        SpriteEditor.fragmentPanel.fragmentTable.editCellAt(-1, -1);
        requestFocus();
    }
    
    //daben:add 增加上下左右键移动切片，ESC键取消选择切片功能
    public void keyPressed(KeyEvent keyevent)
    {
    	int i = keyevent.getKeyCode();
    	switch(i)
    	{
    	//if press left key, move clip rectangle left
    	case KeyEvent.VK_LEFT:		// 向左
        case KeyEvent.VK_A:
    		if (selectedFragment != null)
    		{
    			int newX = selectedFragment.getX() - 1;
    			if(newX < 0)
    			{
    				newX = 0;
    			}
    			selectedFragment.setX(newX);
    			
                if(SpriteEditor.fragmentsSaved)
                {
                    SpriteEditor.fragmentsSaved = false;
                }
                
    			selectedFragment.updateImage();
                SpriteEditor.framePanel.allFragmentListModel.update();
                SpriteEditor.framePanel.repaint();
    			repaint();
    		}else if (newFragmentRectangle != null)
    		{
    			int newX = newFragmentRectangle.x - 1;
    			if (newX < 0)
    			{
    				newX = 0;
    			}
    			newFragmentRectangle.x = newX;
    			repaint();
    		}
    		break;
    		
    	case KeyEvent.VK_RIGHT:		// 向右
        case KeyEvent.VK_D:
    		if (selectedFragment != null)
    		{
    			int newX = selectedFragment.getX() + 1;
    			if (newX + selectedFragment.getWidth() > originalImageWidth)
    				newX = selectedFragment.getX();
    			selectedFragment.setX(newX);
                // 2007-11-1
                if (SpriteEditor.fragmentsSaved)
                    SpriteEditor.fragmentsSaved = false;
                // ~
    			selectedFragment.updateImage();
                SpriteEditor.framePanel.allFragmentListModel.update();
                SpriteEditor.framePanel.repaint();
    			repaint();
    		}
    		else if (newFragmentRectangle != null)
    		{
    			int newX = newFragmentRectangle.x + 1;
    			if (newX + newFragmentRectangle.width > originalImageWidth)
    				newX = newFragmentRectangle.x;
    			newFragmentRectangle.x = newX;
    			repaint();
    		}
    		break;
    		
    	case KeyEvent.VK_UP:		// 向上
        case KeyEvent.VK_W:	
    		if (selectedFragment != null)
    		{
    			int newY = selectedFragment.getY() - 1;
    			if (newY < 0)
    				newY = 0;
    			selectedFragment.setY(newY);
                // 2007-11-1
                if (SpriteEditor.fragmentsSaved)
                    SpriteEditor.fragmentsSaved = false;
                // ~
    			selectedFragment.updateImage();
                SpriteEditor.framePanel.allFragmentListModel.update();
                SpriteEditor.framePanel.repaint();
    			repaint();
    		}
    		else if (newFragmentRectangle != null)
    		{
    			int newY = newFragmentRectangle.y - 1;
    			if (newY < 0)
    				newY = 0;
    			newFragmentRectangle.y = newY;
    			repaint();
    		}
    		break;
    		
    	case KeyEvent.VK_DOWN:			// 向下
        case KeyEvent.VK_S:
    		if (selectedFragment != null)
    		{
    			int newY = selectedFragment.getY() + 1;
    			if (newY + selectedFragment.getHeight() > originalImageHeight)
    				newY = selectedFragment.getY();
    			selectedFragment.setY(newY);
                // 2007-11-1
                if (SpriteEditor.fragmentsSaved)
                    SpriteEditor.fragmentsSaved = false;
                // ~
    			selectedFragment.updateImage();
                SpriteEditor.framePanel.allFragmentListModel.update();
                SpriteEditor.framePanel.repaint();
    			repaint();
    		}
    		else if (newFragmentRectangle != null)
    		{
    			int newY = newFragmentRectangle.y + 1;
    			if (newY + newFragmentRectangle.height > originalImageHeight)
    				newY = newFragmentRectangle.y;
    			newFragmentRectangle.y = newY;
    			repaint();
    		}
    		break;
    		
    	case KeyEvent.VK_ESCAPE:			// 取消
    		newFragmentRectangle = null;
    		if (resizing == 0)
    		{
    			SpriteEditor.fragmentPanel.fragmentTable.getSelectionModel().clearSelection();
    		}
			repaint();
            break;
            
        case KeyEvent.VK_DELETE:			// 删除
            ActionListener[]  actionListeners = SpriteEditor.fragmentPanel.deleteFragmentMenuItem.getActionListeners();
            
            for (int n=0; n<actionListeners.length; n++)
            {
                actionListeners[n].actionPerformed(null);
            }
            break;
            
        case KeyEvent.VK_ENTER:
            ActionListener[] actionListeners2 = SpriteEditor.fragmentPanel.createButton.getActionListeners();
            for (int n=0; n<actionListeners2.length; n++)
            {
                actionListeners2[n].actionPerformed(null);
            }
            break;
    	}
    }
    //~daben
    
    public void keyTyped(KeyEvent keyevent)
    {
    }
    
    public void keyReleased(KeyEvent keyevent)
    {
    }
    
    /**
     * daben:add 画跟随鼠标的十字架
     * @param g
     */
    private void drawCross(Graphics g)
    {
    	g.setColor(Color.WHITE);
    	if (cornerX <= originalImageWidth * SpriteEditor.scale && cornerY <= originalImageHeight * SpriteEditor.scale)
    	{
    		g.drawLine(0, cornerY, originalImageWidth * SpriteEditor.scale, cornerY);
    		g.drawLine(cornerX, 0, cornerX, originalImageHeight * SpriteEditor.scale);
    	}
    }
    
    public void mouseMoved(MouseEvent mouseevent)
    {
    	cornerX = screenToPixel(mouseevent.getX()) * SpriteEditor.scale;
    	cornerY = screenToPixel(mouseevent.getY()) * SpriteEditor.scale;
    	repaint();
    }
    //~daben

    /** 新建的切片的矩形 */
    public Rectangle newFragmentRectangle;
    
    /** 当前选中的切片 */
    public Fragment selectedFragment;
    
    
    public static final int RESIZE_TOP = 1;
    public static final int RESIZE_BOTTOM = 2;
    /** 当前resize的类型 */
    public int resizing;
    
    
    /** 原始图片的大小 */
    private int originalImageWidth;
    private int originalImageHeight;
    
    //daben:add
    //跟随鼠标的十字架中心坐标
    private int cornerX;
    private int cornerY;
    //~daben
}
