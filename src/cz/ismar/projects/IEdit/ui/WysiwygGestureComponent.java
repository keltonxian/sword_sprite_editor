/**
 * 2007-9-12 daben
 * - modify in
 * -->  drawFrame(Graphics g, Frame frame, int i, int j, int k, int l, AffineTransform affinetransform, boolean flag)
 * --> for the function of transformation
 */

package cz.ismar.projects.IEdit.ui;

import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.structure.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;

import cz.ismar.projects.IEdit.structure.Frame;;

public class WysiwygGestureComponent extends CentralGridComponent
{

    public WysiwygGestureComponent()
    {
        currentX = 0;
        currentY = 0;
        showPrevFrame = false;
    }

    public void setShowPrevFrame(boolean flag)
    {
        showPrevFrame = flag;
    }

    public void setPosition(int i, int j)
    {
        currentX = i;
        currentY = j;
    }

    public void movePosition(int i, int j)
    {
        currentX += i;
        currentY += j;
    }

    protected void paintComponent(Graphics g)
    {
        if(SpriteEditor.sprite.getCurrentAnimation() == null || SpriteEditor.sprite.getCurrentAnimation().get() == null || SpriteEditor.sprite.getCurrentAnimation().get().getFrame() == null)
        {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
        } else
        {
            if(isOpaque())
            {
                g.setColor(SpriteEditor.gesturesBackground.getBgColor());
                g.fillRect(0, 0, getWidth(), getHeight());
            }
            int i = currentX * SpriteEditor.scale;
            int j = currentY * SpriteEditor.scale;
            AffineTransform affinetransform = new AffineTransform();
            affinetransform.scale(SpriteEditor.scale, SpriteEditor.scale);
            int k = getWidth() / 2;
            int l = getHeight() / 2;
            g.translate(k, l);
            SpriteEditor.gesturesBackground.paint((Graphics2D)g, affinetransform, this);
            g.translate(-k, -l);
            if(showPrevFrame)
            {
                Gesture gesture = SpriteEditor.sprite.getCurrentAnimation().getPrev();
                if(gesture != null)
                    drawFrame(g, gesture.getFrame(), k, l, i - gesture.getSpeedX(), j - gesture.getSpeedY(), affinetransform, false);
            }
            Frame frame = SpriteEditor.sprite.getCurrentAnimation().get().getFrame();
            if(frame != null)
                drawFrame(g, frame, k, l, i, j, affinetransform, true);
            
            if(SpriteEditor.mapCrossOn){
            	drawMapCrossData(g);
            }
        }
      
        super.paintComponent(g);
    }
    
    //画地图碰撞信息【9x10】
    private void drawMapCrossData(Graphics g)
    {
    	Animation animation = SpriteEditor.sprite.getCurrentAnimation();
    	if(animation==null)return;
    	if(animation.getMapCrossTable()==null)return;
        Insets insets = getInsets();
        int i = insets.left;
        int j = insets.top;
        int k = getWidth() - insets.right;
        int l = getHeight() - insets.bottom;
        int i1 = getWidth() / 2;
        int j1 = getHeight() / 2;
       
        //9x10
        g.setColor(crossColor);
        int startX = i1-SpriteEditor.scale*(96+12-6); 
        //int endX = i1+SpriteEditor.scale*(96+12); 
        int startY = j1-SpriteEditor.scale*(120-6); 
        //int endY = j1+SpriteEditor.scale*120; 
        
        
        int GridWidth = SpriteEditor.scale*24;
        int drawArcWidth = GridWidth/2;
        int drawArcHeight = GridWidth/2;
        
        int counter = 0;
        int row = 0;
        int column = 0;
        
        
        for(int y = startY;row<Animation.MAPCROSS_ROW;y+=GridWidth){
        	
        	int getx = startX;
        	column = 0;
        	for(int x = getx;column<Animation.MAPCROSS_COLUMN;x+=GridWidth){

        		boolean isCross = animation.getCross(column, row);
        		if(!isCross){
        			g.setColor(Color.BLACK);
        			g.fillRect(x, y, drawArcWidth, drawArcHeight);
        			
        			g.setColor(Color.RED);
        			g.drawLine(x, y, x+drawArcWidth, y+drawArcHeight);
        			g.drawLine(x, y+1, x+drawArcWidth, y+drawArcHeight+1);
        			
        			g.drawLine(x, y+drawArcHeight, x+drawArcWidth, y);
        			g.drawLine(x, y+drawArcHeight+1, x+drawArcWidth, y+1);
        			
        			counter++;
        		}
        		
        		column++;
        	}
        	row++;
        }
        //System.out.println("counter: "+counter);
    }
    
    private void drawFrame(Graphics g, Frame frame, int originX, int originY, int offsetX, int offsetY, AffineTransform affinetransform, 
            boolean flag)
    {
    	frame.drawFrame(g, originX, originY, offsetX, offsetY, SpriteEditor.scale, true, false);
//        for(int i1 = frame.size() - 1; i1 > -1; i1--)
//        {
//            FrameFragment framefragment = frame.get(i1);
//            if(SpriteEditor.frameFragmentMasks.isEnabled() && SpriteEditor.frameFragmentMasks.isHiden(framefragment))
//                continue;
//            int j1 = originX + framefragment.getX() * SpriteEditor.scale + offsetX;
//            int k1 = originY + framefragment.getY() * SpriteEditor.scale + offsetY;
//            g.translate(j1, k1);
//            if(flag)
//            {
//            	//daben:modify
//                //((Graphics2D)g).drawImage(framefragment.getFragment().getImage(), affinetransform, this);
//                //modify:
//                ((Graphics2D)g).drawImage(framefragment.getImage(), affinetransform, this);
//                //~daben
//            } else
//            {
//                ((Graphics2D)g).setComposite(AlphaComposite.getInstance(3, 0.5F));
//                //daben:modify
//                //((Graphics2D)g).drawImage(framefragment.getFragment().getImage(), affinetransform, this);
//                //modify:
//                ((Graphics2D)g).drawImage(framefragment.getImage(), affinetransform, this);
//                //~daben
//                ((Graphics2D)g).setComposite(AlphaComposite.SrcOver);
//            }
//            g.translate(-j1, -k1);
//        }
    }
    
    public void mouseMoved(MouseEvent mouseevent)
    {
        SpriteEditor.animationPanel.titledBorder.setTitle("当前动画帧 [" + (mouseevent.getX() - getWidth() / 2) / SpriteEditor.scale + "," + (mouseevent.getY() - getHeight() / 2) / SpriteEditor.scale + "]");
        SpriteEditor.animationPanel.borderCenterPanel.repaint();
    }

    public void mouseExited(MouseEvent mouseevent)
    {
        SpriteEditor.animationPanel.titledBorder.setTitle("当前动画帧");
        SpriteEditor.animationPanel.borderCenterPanel.repaint();
    }
    public void mouseClicked(MouseEvent mouseevent)
    {
    	if(!SpriteEditor.mapCrossOn)return;
    	
    	int i1 = getWidth() / 2;
    	int j1 = getHeight() / 2;
    	int mouseX = mouseevent.getX() - i1;
        int mouseY = mouseevent.getY() - j1;
        
    	Animation animation = SpriteEditor.sprite.getCurrentAnimation();
        if(animation==null)return;
        int startX = -SpriteEditor.scale*(96+12); 
        int startY = -SpriteEditor.scale*(120); 
        int GridWidth = SpriteEditor.scale*24;
        
        int gx = (mouseX - startX)/GridWidth;
        int gy = (mouseY - startY)/GridWidth;
//        
//        System.out.println("mouseX: "+mouseX);
//        System.out.println("mouseY: "+mouseY);
//        System.out.println("startX: "+startX);
//        System.out.println("startY: "+startY);
//        System.out.println("GridWidth: "+GridWidth);
//        System.out.println("gx: "+gx);
//        System.out.println("gy: "+gy);
        
        if((gx<0) || (gx>= Animation.MAPCROSS_COLUMN))return;
        if((gy<0) || (gy>= Animation.MAPCROSS_ROW))return;
        
        if(animation.getMapCrossTable()==null){
        	animation.initMapCrossTable();
        }
        animation.setCross(gx, gy, !animation.getCross(gx, gy));
        SpriteEditor.animationPanel.borderCenterPanel.repaint();
    }

    private int currentX;
    private int currentY;
    private boolean showPrevFrame;
}
