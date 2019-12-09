/**
 * 2007-9-4 daben #see daben:add
 * - in method moveSelectedFragments
 * 
 * 2007-9-7 daben
 * - modify in
 * -->  keyPressed(KeyEvent keyevent)  remove SHIFT
 * 
 * 2007-9-12 daben
 * - modify in
 * -->  drawFrame(Graphics g, Frame frame, int i, int j, AffineTransform affinetransform, boolean flag)
 * --> for the function of transformation
 * 
 * 2007-9-14 daben
 * - remove
 * --> frame.updateImage() in moveSelectedFragments(int i, int j)
 * - add in
 * --> mousePressed(MouseEvent mouseevent)
 * --> mouseReleased(MouseEvent mouseevent)
 * - modify in
 * --> mouseReleased(MouseEvent mouseevent)
 * 
 * 2007-10-9 daben
 * //使得鼠标落在右下角红色框内都可以调整碰撞区域。原方法若放大倍数太小，判断是否调整的区域就太小，鼠标很难点到
 * - modify in
 * --> mousePressed()
 * -->   if(tmpX == (int)(rectangle.getX() + rectangle.getWidth()) && tmpY == (int)(rectangle.getY() + rectangle.getHeight()))
 */

package cz.ismar.projects.IEdit.ui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.sql.Savepoint;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.ListSelectionModel;

import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.structure.Frame;
import cz.ismar.projects.IEdit.structure.FrameFragment;
import cz.ismar.projects.IEdit.structure.Sprite;

public class AvatarFrameEditComponent extends CentralGridComponent implements KeyListener
{
	private JPopupMenu popupMenu = new JPopupMenu();
	
	public AvatarFrameEditComponent()
	{
		tmpX = 0;
		tmpY = 0;
		colisionResizing = false;
		tmpFrame = null;
		setFocusable(true);
		addKeyListener(this);
		
//		popupMenu.add(SpriteEditor.flipXTransformMenuItem);
//		popupMenu.add(SpriteEditor.flipYTransformMenuItem);
//		popupMenu.add(SpriteEditor.rotate90TransformMenuItem);
//		popupMenu.add(SpriteEditor.rotate180TransformMenuItem);
//		popupMenu.add(SpriteEditor.rotate270TransformMenuItem);
	}

	public void setShowHelpFrame(boolean flag)
	{
		showHelpFrame = flag;
	}

	public Frame getHelpFrame()
	{
		return helpFrame;
	}

	public void setHelpFrame(Frame frame)
	{
		helpFrame = frame;
	}

	public void setTmpFrame(Frame frame)
	{
		tmpFrame = frame;
	}

	protected void paintComponent(Graphics g)
	{
		Sprite sprite = SpriteEditor.avatarPanel.getSprite();

		// 背景色
		if(sprite.frameHolder.getCurrentFrame() == null)
		{
			g.setColor(getBackground());
			g.fillRect(0, 0, getWidth(), getHeight());
			return;
		}

		Frame frame;
		if(tmpFrame == null)
		{
			frame = sprite.frameHolder.getCurrentFrame();
		}else
		{
			frame = tmpFrame;
		}

		if(isOpaque())
		{
			g.setColor(SpriteEditor.framesBackground.getBgColor());
			g.fillRect(0, 0, getWidth(), getHeight());
		}

		AffineTransform affinetransform = new AffineTransform();
		affinetransform.scale(SpriteEditor.scale, SpriteEditor.scale);
		int centerX = getWidth() / 2;
		int centerY = getHeight() / 2;
		g.translate(centerX, centerY);
		SpriteEditor.framesBackground.paint((Graphics2D) g, affinetransform, this);
		g.translate(-centerX, -centerY);
		if(frame == null)
		{
			return;
		}

		// 是否画帮助帧
		if(showHelpFrame && helpFrame != null)
		{
			drawFrame(g, helpFrame, centerX, centerY, SpriteEditor.scale, false);
		}
		
		// 画普通帧
		drawFrame(g, frame, centerX, centerY, SpriteEditor.scale, true);
		super.paintComponent(g);

		// 画碰撞框
		Rectangle rectangle = frame.getColisionArea();
		if(rectangle != null && SpriteEditor.collisionOn)
		{
			g.setColor(Color.YELLOW);
			int k = centerX + (int) rectangle.getX() * SpriteEditor.scale;
			int l = centerY + (int) rectangle.getY() * SpriteEditor.scale;
			g.drawRect(k - 1, l - 1, (int) rectangle.getWidth() * SpriteEditor.scale + 2, (int) rectangle.getHeight() * SpriteEditor.scale + 2);
			g.setColor(Color.BLACK);
			g.drawRect(k - 2, l - 2, (int) rectangle.getWidth() * SpriteEditor.scale + 4, (int) rectangle.getHeight() * SpriteEditor.scale + 4);
			if(frame.isColisionAreaSelected())
			{
				g.fillRect((k + (int) rectangle.getWidth() * SpriteEditor.scale) - 3, (l + (int) rectangle.getHeight() * SpriteEditor.scale) - 3, 10, 10);
				g.setColor(Color.RED);
				g.fillRect((k + (int) rectangle.getWidth() * SpriteEditor.scale) - 1, (l + (int) rectangle.getHeight() * SpriteEditor.scale) - 1, 6, 6);
			}
		}
	}

	/**
	 * 在指定的位置现指定的Frame
	 * 
	 * @param g
	 * @param frame
	 * @param screenX
	 * @param screenY
	 * @param affinetransform
	 * @param isNormalFrame
	 *            是否普通帧 (还有一种是帮助帧,要画半透明)
	 */
	private void drawFrame(Graphics g, Frame frame, int screenX, int screenY, int scale, boolean isNormalFrame)
	{
		frame.drawFrame(g, screenX, screenY, 0, 0, scale, isNormalFrame, true);
		
//		for(int i = frame.size() - 1; i >= 0; i--)
//		{
//			FrameFragment framefragment = frame.get(i);
//			if(SpriteEditor.frameFragmentMasks.isEnabled() && SpriteEditor.frameFragmentMasks.isHiden(framefragment))
//			{
//				continue;
//			}
//			
//			if(framefragment.isVisible() == false)
//			{
//				continue;
//			}
//
//			int tempX = screenX + framefragment.getX() * SpriteEditor.scale;
//			int tempY = screenY + framefragment.getY() * SpriteEditor.scale;
//			g.translate(tempX, tempY);
//			if(isNormalFrame) // this is normal frame
//			{
//				((Graphics2D) g).drawImage(framefragment.getImage(), affinetransform, this);
//			}else
//			{
//				// 半黎明, 画帮助帧
//				((Graphics2D) g).setComposite(AlphaComposite.getInstance(3, 0.5F));
//				((Graphics2D) g).drawImage(framefragment.getImage(), affinetransform, this);
//				((Graphics2D) g).setComposite(AlphaComposite.SrcOver);
//			}
//
//			// 选中的, 画框
//			if(frame.isIndexSelected(i))
//			{
//				g.setColor(Color.RED);
//				g.drawRect(0, 0, framefragment.getRectangle().width * SpriteEditor.scale, framefragment.getRectangle().height * SpriteEditor.scale);
//				g.fillRect(-3, -3, 7, 7);
//				g.fillRect(framefragment.getRectangle().width * SpriteEditor.scale - 3, framefragment.getRectangle().height * SpriteEditor.scale - 3, 7, 7);
//				g.fillRect(-3, framefragment.getRectangle().height * SpriteEditor.scale - 3, 7, 7);
//				g.fillRect(framefragment.getRectangle().width * SpriteEditor.scale - 3, -3, 7, 7);
//			}
//			g.translate(-tempX, -tempY);
//		}

	}

	/**
	 * 画预览帧
	 * 
	 * @param g
	 * @param frame
	 * @param x
	 * @param y
	 */
	private void drawPreviewFrame(Graphics g, Frame frame, int x, int y)
	{
		frame.drawFrame(g, x, y, 0, 0, 1, true, false);
//		for(int i = frame.size() - 1; i > -1; i--)
//		{
//			FrameFragment framefragment = frame.get(i);
//			if(!SpriteEditor.frameFragmentMasks.isEnabled() || !SpriteEditor.frameFragmentMasks.isHiden(framefragment))
//			{
//				int centerX = x + framefragment.getX();
//				int centerY = y + framefragment.getY();
//				g.translate(centerX, centerY);
//				((Graphics2D) g).drawImage(framefragment.getFragment().getImage(), null, this);
//				g.translate(-centerX, -centerY);
//			}
//		}
	}

	public void mouseMoved(MouseEvent mouseevent)
	{
		SpriteEditor.avatarPanel.titledBorder.setTitle("鼠标位置 [" + (mouseevent.getX() - getWidth() / 2) / SpriteEditor.scale + "," + (mouseevent.getY() - getHeight() / 2) / SpriteEditor.scale + "]");
		SpriteEditor.avatarPanel.borderCenterPanel.repaint();
	}

	public void mouseExited(MouseEvent mouseevent)
	{
		SpriteEditor.avatarPanel.titledBorder.setTitle("当前帧");
		SpriteEditor.avatarPanel.borderCenterPanel.repaint();
	}

	public void mouseClicked(MouseEvent mouse)
	{
		Sprite sprite = SpriteEditor.avatarPanel.getSprite();

		Frame frame = sprite.frameHolder.getCurrentFrame();
		if(frame == null)
		{
			return;
		}

		// SpriteEditor.avatarPanel.frameList.getSelectionModel().setSelectionInterval(sprite.frameHolder.getIndex(),
		// sprite.frameHolder.getIndex());

		int tempX = mouse.getX() - getWidth() / 2;
		int tempY = mouse.getY() - getHeight() / 2;
		tempX = (tempX + (tempX <= 0 ? -SpriteEditor.scale / 2 : SpriteEditor.scale / 2)) / SpriteEditor.scale;
		tempY = (tempY + (tempY <= 0 ? -SpriteEditor.scale / 2 : SpriteEditor.scale / 2)) / SpriteEditor.scale;
		frame.setColisionAreaSelected(false);
		Rectangle rectangle = new Rectangle(frame.getColisionArea());
		Rectangle rectangle1 = new Rectangle(frame.getColisionArea());
		rectangle1.grow(2, 2);
		rectangle1.setLocation(rectangle.x - 1, rectangle.y - 1);
		rectangle.grow(-2, -2);
		rectangle.setLocation(rectangle.x, rectangle.y);
		if(SpriteEditor.collisionOn && rectangle1.contains(tempX, tempY) && !rectangle.contains(tempX, tempY))
		{
			frame.setColisionAreaSelected(true);
		}
		// ListSelectionModel listselectionmodel =
		// SpriteEditor.avatarPanel.frameFragmentList.getSelectionModel();
		// else
		// {
		// if((mouse.getModifiers() & 3) == 0)
		// {
		// listselectionmodel.clearSelection();
		// }
		//
		// for(int k = 0; k < frame.size(); k++)
		// {
		// FrameFragment framefragment = frame.get(k);
		// if(!framefragment.getRectangle().contains(tempX, tempY) ||
		// SpriteEditor.frameFragmentMasks.isEnabled() &&
		// SpriteEditor.frameFragmentMasks.isHiden(framefragment))
		// {
		// continue;
		// }
		// if((mouse.getModifiers() & 3) == 0)
		// {
		// listselectionmodel.setSelectionInterval(k, k);
		// break;
		// }
		// if((mouse.getModifiers() & 1) != 0)
		// {
		// if(listselectionmodel.isSelectedIndex(k))
		// continue;
		// listselectionmodel.addSelectionInterval(k, k);
		// break;
		// }
		// if((mouse.getModifiers() & 2) == 0)
		// continue;
		// if(!listselectionmodel.isSelectedIndex(k))
		// {
		// listselectionmodel.addSelectionInterval(k, k);
		// frame.setIndex(k);
		// frame.setIndices(SpriteEditor.avatarPanel.frameFragmentList.getSelectedIndices());
		// }else
		// {
		// listselectionmodel.removeSelectionInterval(k, k);
		// }
		// break;
		// }
		//
		// int ai[] =
		// SpriteEditor.avatarPanel.frameFragmentList.getSelectedIndices();
		// if(ai == null || ai.length == 0)
		// {
		// frame.setIndex(-1);
		// }else
		// {
		// frame.setIndex(ai[0]);
		// frame.setIndices(ai);
		// }
		// }
		repaint();
	}

	public void mousePressed(MouseEvent mouse)
	{
		Sprite sprite = SpriteEditor.avatarPanel.getSprite();
		Frame frame = sprite.frameHolder.getCurrentFrame();
		int tempX = mouse.getX() - getWidth() / 2;
		int tempY = mouse.getY() - getHeight() / 2;
		tmpX = (tempX + (tempX <= 0 ? -SpriteEditor.scale / 2 : SpriteEditor.scale / 2)) / SpriteEditor.scale;
		tmpY = (tempY + (tempY <= 0 ? -SpriteEditor.scale / 2 : SpriteEditor.scale / 2)) / SpriteEditor.scale;
		if(frame != null && frame.isColisionAreaSelected())
		{
			Rectangle rectangle = frame.getColisionArea();
			// daben:modify
			// 使得鼠标落在右下角红色框内都可以调整碰撞区域。原方法若放大倍数太小，判断是否调整的区域就太小，鼠标很难点到
			// if(tmpX == (int)(rectangle.getX() + rectangle.getWidth()) && tmpY
			// == (int)(rectangle.getY() + rectangle.getHeight()))
			if((tempX < ((int) (rectangle.getX() + rectangle.getWidth())) * SpriteEditor.scale + 8 && tempX > ((int) (rectangle.getX() + rectangle.getWidth())) * SpriteEditor.scale - 8)
					&& (tempY < ((int) (rectangle.getY() + rectangle.getHeight())) * SpriteEditor.scale + 8 && tempY > ((int) (rectangle.getY() + rectangle.getHeight())) * SpriteEditor.scale - 8))
				// ~daben
				colisionResizing = true;
		}

		// daben:add 鼠标按下也能选中切片
		if(frame == null)
		{
			return;
		}
		tempX = (tempX + (tempX <= 0 ? -SpriteEditor.scale / 2 : SpriteEditor.scale / 2)) / SpriteEditor.scale;
		tempY = (tempY + (tempY <= 0 ? -SpriteEditor.scale / 2 : SpriteEditor.scale / 2)) / SpriteEditor.scale;
		ListSelectionModel frameFragmentListModel = SpriteEditor.avatarPanel.frameFragmentList.getSelectionModel();
		
//		if((mouse.getModifiers() & 3) == 0)
//		{
		// 清空原来的选反地
		frameFragmentListModel.clearSelection();
		frame.setIndex(-1);
		frame.setIndices(null);
//		}

		int size = frame.getFrameFragments().size();
		for(int k = 0; k < size; k++)
		{
			FrameFragment framefragment = frame.get(k);

			// 不在区域内, continue;
			if(!framefragment.contains(1, tempX, tempY) 
					|| (SpriteEditor.frameFragmentMasks.isEnabled() && SpriteEditor.frameFragmentMasks.isHiden(framefragment)))
			{
				continue;
			}

			// 选中最前面的切片, 其它选中交由 ListSelectionListener 去做 
			// @see processFrameFragmentSelect 
			frameFragmentListModel.setSelectionInterval(k, k);
			break;
		}
		repaint();
	}

	/**
	 * 移动选中的frame fragment
	 */
	public void mouseDragged(MouseEvent event)
	{
		updateSize(event);
		repaint();
	}

	/**
	 * 更新当前编辑的frame
	 */
	public void mouseReleased(MouseEvent mouse)
	{
		Sprite sprite = SpriteEditor.avatarPanel.getSprite();
		
		if(mouse.isPopupTrigger())		// 右键菜单->　翻转
		{
//			System.out.println("here");
//			System.out.println("x == " + mouse.getX());
//			System.out.println("y == " + mouse.getY());
			
			popupMenu.show(mouse.getComponent(), 0, 0);
		}else								// 确定更新的位置
		{
			// 2009-11-11 因为avatar point一变, 所有frame都要变, 更新所有frame
			for(Frame frame : sprite.frameHolder.getFrames())
			{
				frame.updateImage();
			}
			SpriteEditor.avatarPanel.frameList.updateUI();
			
			// SpriteEditor.avatarPanel.frameListModel.update();
			colisionResizing = false;
			firstStep = true;
		}
		
		repaint();
	}

	/**
	 * 更新所选中的fragment的位置
	 * 
	 * @param mouse
	 */
	void updateSize(MouseEvent mouse)
	{
		Sprite sprite = SpriteEditor.avatarPanel.getSprite();

		Frame frame = sprite.frameHolder.getCurrentFrame();
		if(frame == null)
		{
			return;
		}

		int currentX = mouse.getX() - getWidth() / 2;
		int currentY = mouse.getY() - getHeight() / 2;
		currentX = (currentX + (currentX <= 0 ? -SpriteEditor.scale / 2 : SpriteEditor.scale / 2)) / SpriteEditor.scale;
		currentY = (currentY + (currentY <= 0 ? -SpriteEditor.scale / 2 : SpriteEditor.scale / 2)) / SpriteEditor.scale;

		// 编辑碰撞框
		if(frame.isColisionAreaSelected())
		{
			if(colisionResizing)
			{
				int k = (int) ((double) currentX - frame.getColisionArea().getX());
				int l = (int) ((double) currentY - frame.getColisionArea().getY());
				frame.getColisionArea().setSize(k, l);
			}else
			{
				frame.getColisionArea().setLocation(((int) frame.getColisionArea().getX() + currentX) - tmpX, ((int) frame.getColisionArea().getY() + currentY) - tmpY);
			}

			if(SpriteEditor.animationsSaved)
				SpriteEditor.animationsSaved = false;
		}else
		// 编辑位置
		{
			// daben:add
			if(firstStep)
			{
				sprite.frameHolder.getCurrentFrame().saveUndo();
				firstStep = false;
			}
			// ~daben

			moveSelectedFragments(currentX - tmpX, currentY - tmpY);
		}
		tmpX = currentX;
		tmpY = currentY;
	}

	/**
	 * 移动所选的Fragment
	 * 
	 * @param _x
	 * @param _y
	 */
	private void moveSelectedFragments(int _x, int _y)
	{
		Sprite sprite = SpriteEditor.avatarPanel.getSprite();

		// 当前帧
		Frame frame = sprite.frameHolder.getCurrentFrame();
		if(frame == null)
		{
			return;
		}

		// fay: 同一个切片的话,一个移动,另一个也要移动
		FrameFragment ff = frame.get();
		if(ff == null)
		{
			return;
		}

		// 选中的切片一起移动
		int ai[] = frame.getIndices();
		if(ai != null)
		{
			for(int k = 0; k < ai.length; k++)
			{
				FrameFragment framefragment = frame.get(ai[k]);
				if(framefragment != null)
				{
//					framefragment.moveDelta(_x, _y);
					framefragment.moveDeltaAvatar(_x, _y);
					
					// daben:remove 移动中没必要更新帧缩略图，太占用资源，会使得移动不连贯，改到mouseReleased中
					// frame.updateImage();
					// ~daben
				}
			}

			// 更新"坐标信息"
			int posX = frame.get(ai[0]).getTopLeftX(1);
			int posY = frame.get(ai[0]).getTopLeftY(1);
			SpriteEditor.avatarPanel.titledBorder.setTitle("Current frame composition [" + posX + "," + posY + "]");
			SpriteEditor.avatarPanel.borderCenterPanel.repaint();

			SpriteEditor.animationsSaved = false;
		}
	}

	public void keyPressed(KeyEvent keyevent)
	{
		Sprite sprite = SpriteEditor.avatarPanel.getSprite();

		if(keyevent.getModifiers() == InputEvent.ALT_MASK)
		{
			int keyCode = keyevent.getKeyCode();
			switch(keyCode)
			{
			case KeyEvent.VK_UP: // '&'
				if(sprite.frameHolder.getCurrentFrame().moveUp())
				{
					SpriteEditor.avatarPanel.frameFragmentList.getSelectionModel().setSelectionInterval(sprite.frameHolder.getCurrentFrame().getIndex(), sprite.frameHolder.getCurrentFrame().getIndex());
				}
				break;

			case KeyEvent.VK_DOWN: // '('
				if(sprite.frameHolder.getCurrentFrame().moveDown())
				{
					SpriteEditor.avatarPanel.frameFragmentList.getSelectionModel().setSelectionInterval(sprite.frameHolder.getCurrentFrame().getIndex(), sprite.frameHolder.getCurrentFrame().getIndex());
				}
				break;

			case KeyEvent.VK_HOME: // '$'
				if(sprite.frameHolder.getCurrentFrame().moveTop())
				{
					SpriteEditor.avatarPanel.frameFragmentList.getSelectionModel().setSelectionInterval(0, 0);
				}
				break;

			case KeyEvent.VK_END: // '#'
				if(sprite.frameHolder.getCurrentFrame().moveBottom())
				{
					SpriteEditor.avatarPanel.frameFragmentList.getSelectionModel().setSelectionInterval(sprite.frameHolder.getCurrentFrame().getIndex(), sprite.frameHolder.getCurrentFrame().getIndex());
				}
				break;
			}
		}
		// daben:add
		 else if(keyevent.getModifiers() == InputEvent.CTRL_MASK)
		 {
			 if (keyevent.getKeyCode() == KeyEvent.VK_Z)
			 {
				 sprite.frameHolder.getCurrentFrame().undo();
			 }
		 }
		// else if (keyevent.getKeyCode() == KeyEvent.VK_C)
		// {
		// SpriteEditor.fragmentsClipBoard.insertItems(SpriteEditor.framePanel.frameFragmentJList);
		// Frame frame = SpriteEditor.project.frameHolder.getCurrentFrame();
		// if(frame.isColisionAreaSelected())
		// SpriteEditor.collisionClipboard = frame.getColisionArea();
		// else
		// SpriteEditor.collisionClipboard = null;
		// }
		// else if (keyevent.getKeyCode() == KeyEvent.VK_V)
		// {
		// java.util.List list = SpriteEditor.fragmentsClipBoard.getItems();
		// Frame frame = SpriteEditor.project.frameHolder.getCurrentFrame();
		// if(list.size() > 0)
		// {
		// frame.saveUndo();
		// int i = frame.getIndex();
		// if (i<0)
		// i = 0;
		// for(int j = 0; j < list.size(); j++)
		// {
		// FrameFragment framefragment = (FrameFragment)list.get(j);
		// frame.add(i + j, new FrameFragment(framefragment));
		// if (SpriteEditor.animationsSaved)
		// SpriteEditor.animationsSaved = false;
		// }
		//
		// SpriteEditor.project.frameHolder.getCurrentFrame().updateImage();
		// SpriteEditor.framePanel.frameFragmentListModel.update();
		// SpriteEditor.framePanel.frameListModel.update();
		// SpriteEditor.framePanel.frameJList.getSelectionModel().setSelectionInterval(SpriteEditor.project.frameHolder.getIndex(),
		// SpriteEditor.project.frameHolder.getIndex());
		// SpriteEditor.framePanel.frameFragmentJList.getSelectionModel().setSelectionInterval(i,
		// i);
		// }
		// if(SpriteEditor.collisionClipboard != null)
		// {
		// frame.setColisionArea((Rectangle)SpriteEditor.collisionClipboard.clone());
		// }
		// }
		// }
		// ~daben
		// else if(keyevent.getKeyCode() == KeyEvent.VK_DELETE &&
		// SpriteEditor.project.frameHolder.getCurrentFrame().remove())
		// {
		// // daben:add 更新小视图
		// SpriteEditor.project.frameHolder.getCurrentFrame().updateImage();
		// SpriteEditor.framePanel.frameListModel.update();
		// // ~daben
		// SpriteEditor.framePanel.frameFragmentListModel.update();
		// SpriteEditor.framePanel.frameJList.getSelectionModel().setSelectionInterval(SpriteEditor.project.frameHolder.getIndex(),
		// SpriteEditor.project.frameHolder.getIndex());
		// SpriteEditor.framePanel.frameFragmentJList.getSelectionModel().setSelectionInterval(SpriteEditor.project.frameHolder.getCurrentFrame().getIndex(),
		// SpriteEditor.project.frameHolder.getCurrentFrame().getIndex());
		// }
		// daben:modify
		// if(keyevent.getModifiers() == InputEvent.SHIFT_MASK)
		// else
		// ~daben
		// {
		// int i = keyevent.getKeyCode();
		// int v =
		// SpriteEditor.mainFramingPanel.wysiwygFrameScrollPanel.getVerticalScrollBar().getValue();
		// int h =
		// SpriteEditor.mainFramingPanel.wysiwygFrameScrollPanel.getHorizontalScrollBar().getValue();
		// switch(i)
		// {
		// case KeyEvent.VK_LEFT: // '%'
		// case KeyEvent.VK_A:
		// if (firstStep)
		// {
		// SpriteEditor.project.frameHolder.get().saveUndo();
		// firstStep = false;
		// }
		// moveSelectedFragments(-1, 0);
		// //SpriteEditor.mainFramingPanel.wysiwygFrameScrollPanel.getHorizontalScrollBar().setValue(h
		// + 10);
		// break;
		// case KeyEvent.VK_RIGHT: // '\''
		// case KeyEvent.VK_D:
		// if (firstStep)
		// {
		// SpriteEditor.project.frameHolder.get().saveUndo();
		// firstStep = false;
		// }
		// moveSelectedFragments(1, 0);
		// //SpriteEditor.mainFramingPanel.wysiwygFrameScrollPanel.getHorizontalScrollBar().setValue(h
		// - 10);
		// break;
		// case KeyEvent.VK_UP: // '&'
		// case KeyEvent.VK_W:
		// if (firstStep)
		// {
		// SpriteEditor.project.frameHolder.get().saveUndo();
		// firstStep = false;
		// }
		// moveSelectedFragments(0, -1);
		// //SpriteEditor.mainFramingPanel.wysiwygFrameScrollPanel.getVerticalScrollBar().setValue(v
		// + 10);
		// break;
		// case KeyEvent.VK_DOWN: // '('
		// case KeyEvent.VK_S:
		// if (firstStep)
		// {
		// SpriteEditor.project.frameHolder.get().saveUndo();
		// firstStep = false;
		// }
		// moveSelectedFragments(0, 1);
		// //SpriteEditor.mainFramingPanel.wysiwygFrameScrollPanel.getVerticalScrollBar().setValue(v
		// - 10);
		// break;
		// }
		// }
		// daben:add 毁掉键盘事件，不让JScrollPane响应键盘事件
		// keyevent.consume();
		// ~daben
	}

	protected void processKeyEvent(KeyEvent keyevent)
	{
		Sprite sprite = SpriteEditor.avatarPanel.getSprite();

		if(keyevent.getModifiers() == InputEvent.ALT_MASK || keyevent.getModifiers() == InputEvent.CTRL_MASK)
		{
			super.processKeyEvent(keyevent);
			return;
		}
		// 若方向键处于按下状态，只让自身响应此事件，不向外传递，其它情况把键盘事件向其它地方传递
		if(keyevent.getID() == KeyEvent.KEY_PRESSED)
		{
			int i = keyevent.getKeyCode();
			switch(i)
			{
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_A:
				// 响应此事件，或调用keyPressed(keyevent)，效果一样
				if(firstStep)
				{
					sprite.frameHolder.getCurrentFrame().saveUndo();
					firstStep = false;
				}
				moveSelectedFragments(-1, 0);
				return;
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_D:
				if(firstStep)
				{
					sprite.frameHolder.getCurrentFrame().saveUndo();
					firstStep = false;
				}
				moveSelectedFragments(1, 0);
				return;
			case KeyEvent.VK_UP:
			case KeyEvent.VK_W:
				if(firstStep)
				{
					sprite.frameHolder.getCurrentFrame().saveUndo();
					firstStep = false;
				}
				moveSelectedFragments(0, -1);
				return;
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_S:
				if(firstStep)
				{
					sprite.frameHolder.getCurrentFrame().saveUndo();
					firstStep = false;
				}
				moveSelectedFragments(0, 1);
				return;
			}
		}

		super.processKeyEvent(keyevent);
	}

	public void keyReleased(KeyEvent keyevent)
	{
		firstStep = true;
	}

	public void keyTyped(KeyEvent keyevent)
	{
	}

	public void mouseEntered(MouseEvent mouseevent)
	{
		requestFocus();
	}

	public JPanel getPreviewPanel()
	{
		if(previewPanel == null)
		{
			previewPanel = new JPanel() {

				protected void paintComponent(Graphics g)
				{
					g.setColor(Color.BLACK);
					g.fillRect(0, 0, getWidth(), getHeight());
					g.setColor(SpriteEditor.framesBackground.getBgColor());
					g.fillRect(2, 2, getWidth() - 4, getHeight() - 4);
					Frame frame = SpriteEditor.avatarPanel.getSprite().frameHolder.getCurrentFrame();
					if(frame != null)
					{
						drawPreviewFrame(g, frame, getWidth() / 2, getHeight() / 2);
					}
				}

			};
		}
		return previewPanel;
	}

	public void repaint(long l, int i, int j, int k, int i1)
	{
		super.repaint(l, i, j, k, i1);
		if(previewPanel != null)
			previewPanel.repaint();
	}

	private int tmpX;
	private int tmpY;
	private boolean colisionResizing;
	private Frame tmpFrame;
	private boolean showHelpFrame;
	private Frame helpFrame;
	JPanel previewPanel;
	// daben:add
	private boolean firstStep = true;
	// ~daben
}
