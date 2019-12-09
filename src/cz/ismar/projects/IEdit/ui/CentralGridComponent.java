package cz.ismar.projects.IEdit.ui;

import cz.ismar.projects.IEdit.SpriteEditor;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

/**
 * 2007-9-4 daben #see daben:modify
 * //放大/缩小 时，让编辑范围变大
 * - Dimension dimension = new Dimension(200 * i, 200 * i);
 * --> Dimension dimension = new Dimension(400 * i, 400 * i);
 *   
 * 2007-9-6 daben #see daben:add
 * - 放大/缩小 时，更新滚动条
 * -->  if (SpriteEditor.mainFramingPanel != null)
        {
        	SpriteEditor.mainFramingPanel.wysiwygFrameScrollPanel.getHorizontalScrollBar().setMaximum(dimension.width);
        	SpriteEditor.mainFramingPanel.wysiwygFrameScrollPanel.getVerticalScrollBar().setMaximum(dimension.height);
        }
 * 
 * 
 * */
public class CentralGridComponent extends JPanel
    implements MouseInputListener, MouseMotionListener
{

    public CentralGridComponent()
    {
        preferredSize = new Dimension(1000, 1000);
        gridColor = new Color(200, 200, 200);
        crossColor = new Color(250, 50, 50);
        backgroundColor = new Color(150, 200, 200);
        addMouseListener(this);
        addMouseMotionListener(this);
        setBackground(Color.WHITE);
        setOpaque(true);
        updatePrefferedSize();
    }

    public void updatePrefferedSize()
    {
        int i = SpriteEditor.scale;
        //daben:modify 让编辑范围变大
        //Dimension dimension = new Dimension(200 * i, 200 * i);
        //modify:
        Dimension dimension = new Dimension(400 * i, 400 * i);
        //~daben
        setPreferredSize(dimension);
        setSize(dimension);
        //daben:add
        // 更新滚动条
        if (SpriteEditor.framePanel != null)
        {
        	SpriteEditor.framePanel.frameEditScrollPanel.getHorizontalScrollBar().setMaximum(dimension.width);
        	SpriteEditor.framePanel.frameEditScrollPanel.getVerticalScrollBar().setMaximum(dimension.height);
        }
        //~daben
    }
    
    protected void paintComponent(Graphics g)
    {
    	if(SpriteEditor.mapCrossOn){
    		drawMapCross(g);
    		return;
    	}
    	
        //if(SpriteEditor.scale > 2)
        //{
            g.setColor(Color.GRAY);
            drawGrid(g);
        //}
            
            
    }

    private void drawGrid(Graphics g)
    {
        Insets insets = getInsets();
        int i = insets.left;
        int j = insets.top;
        int k = getWidth() - insets.right;
        int l = getHeight() - insets.bottom;
        int i1 = getWidth() / 2;
        int j1 = getHeight() / 2;
        if(SpriteEditor.gridOn && (SpriteEditor.scale > 2))
        {
            g.setColor(gridColor);
            for(int k1 = SpriteEditor.scale; k1 < k / 2; k1 += (SpriteEditor.scale))
            {
                g.drawLine(i1 + k1, j, i1 + k1, l);
                g.drawLine(i1 - k1, j, i1 - k1, l);
            }
            for(int l1 = SpriteEditor.scale; l1 < l / 2; l1 += (SpriteEditor.scale))
            {
                g.drawLine(i, j1 - l1, k, j1 - l1);
                g.drawLine(i, j1 + l1, k, j1 + l1);
            }

            if(!SpriteEditor.crossOn)
            {
                g.drawLine(i1, j, i1, l);
                g.drawLine(i, j1, k, j1);
            }
        }
        if(SpriteEditor.crossOn)
        {
            g.setColor(crossColor);
            g.drawLine(i1, j, i1, l);
            g.drawLine(i, j1, k, j1);
        }
    }
    

    public final static int GIRD_SIZE = 24;
    public final static int GIRD_HALF_SIZE = GIRD_SIZE/2;
    //画地图碰撞
    private void drawMapCross(Graphics g)
    {
        Insets insets = getInsets();
        int i = insets.left;
        int j = insets.top;
        int k = getWidth() - insets.right;
        int l = getHeight() - insets.bottom;
        int i1 = getWidth() / 2;
        int j1 = getHeight() / 2;
       
        g.setColor(gridColor);
        for(int k1 = (SpriteEditor.scale*GIRD_HALF_SIZE); k1 < k / 2; k1 += (SpriteEditor.scale*GIRD_SIZE))
        {
        	g.drawLine(i1 + k1, j, i1 + k1, l);
        	g.drawLine(i1 - k1, j, i1 - k1, l);
        }
        for(int l1 = 0; l1 < l / 2; l1 += (SpriteEditor.scale*GIRD_SIZE))
        {
        	g.drawLine(i, j1 - l1, k, j1 - l1);
        	g.drawLine(i, j1 + l1, k, j1 + l1);
        }

        g.setColor(crossColor);
        g.drawLine(i1, j, i1, l);
        g.drawLine(i, j1, k, j1);
        
        //9x10
        g.setColor(crossColor);
        g.drawRect(i1-SpriteEditor.scale*(96+12), j1-SpriteEditor.scale*120, SpriteEditor.scale*(96*2+24), SpriteEditor.scale*240);
       
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

    public void mouseReleased(MouseEvent mouseevent)
    {
    }

    public void mouseEntered(MouseEvent mouseevent)
    {
    }

    public void mousePressed(MouseEvent mouseevent)
    {
    }

    public void mouseDragged(MouseEvent mouseevent)
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
