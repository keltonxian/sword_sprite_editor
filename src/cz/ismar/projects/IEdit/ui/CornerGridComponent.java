/**
 * 2007-9-4 daben #see daben:add
 *  // 在切片区，当打开了一个切片源图后，在切片源图的可编辑返回画上框
 * - add method 
        drawEditRangeRect(g);
      
 * --> used in paintComponent(Graphics g);
 * 
 * */
package cz.ismar.projects.IEdit.ui;

import cz.ismar.projects.IEdit.SpriteEditor;
import java.awt.*;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.event.MouseInputListener;


/**
 * 面板容器类, 
 * 
 * 子类：
 * － 切片区
 * － 帧编辑区
 * － 动画编辑区
 * 
 * @author Lori(赤浪)
 * @version 2.12 2008/1/18
 *
 */
public class CornerGridComponent extends JComponent
    implements MouseInputListener
{

    public CornerGridComponent()
    {
        preferredSize = new Dimension(1, 1);
        gridColor = new Color(200, 200, 200);
        crossColor = new Color(250, 50, 50);
        backgroundColor = new Color(150, 200, 200);
        addMouseListener(this);
        addMouseMotionListener(this);
        setBackground(Color.WHITE);
        setOpaque(true);
    }

    public Dimension getPreferredSize()
    {
        return preferredSize;
    }

    protected void paintComponent(Graphics g)
    {
        if(SpriteEditor.scale > 2 && SpriteEditor.gridOn)
            drawGrid(g);
        //daben:add
        drawEditRangeRect(g);
        //~daben
    }

    /**
     * 画切片区的，网格
     * @param g
     */
    private void drawGrid(Graphics g)
    {
        Insets insets = getInsets();
        int i = insets.left;
        int j = insets.top;
        int k = getWidth() - insets.right;
        int l = getHeight() - insets.bottom;
        g.setColor(gridColor);
        for(int i1 = 0; i1 < k; i1 += SpriteEditor.scale)
            g.drawLine(i1, j, i1, l);

        for(int j1 = 0; j1 < l; j1 += SpriteEditor.scale)
            g.drawLine(i, j1, k, j1);

    }
    
    //daben:add
    // 在切片区，当打开了一个切片源图后，在切片源图的可编辑返回画上框
    private void drawEditRangeRect(Graphics g)
    {
    	
    	g.setColor(Color.GREEN);
    	if (SpriteEditor.sprite.fragmentHolder.getImage() != null)
    	{
    		g.drawRect(0, 0, SpriteEditor.sprite.fragmentHolder.getImage().getWidth(null) * SpriteEditor.scale, 
    				        	SpriteEditor.sprite.fragmentHolder.getImage().getHeight(null) * SpriteEditor.scale);
    	}
    	
    }
    //~daben
    
    public void mousePressed(MouseEvent mouseevent)
    {
    }

    public void mouseDragged(MouseEvent mouseevent)
    {
    }

    public void mouseReleased(MouseEvent mouseevent)
    {
    }

    private void updateDrawableRect(int i, int j)
    {
    }

    public void mouseClicked(MouseEvent mouseevent)
    {
    }

    public void mouseMoved(MouseEvent mouseevent)
    {
    }

    public void mouseExited(MouseEvent mouseevent)
    {
    }

    public void mouseEntered(MouseEvent mouseevent)
    {
    }

    public Color getGridColor()
    {
        return gridColor;
    }

    public void setGridColor(Color color)
    {
        gridColor = color;
    }

    Dimension preferredSize;
    Color gridColor;
    Color crossColor;
    Color backgroundColor;
}
