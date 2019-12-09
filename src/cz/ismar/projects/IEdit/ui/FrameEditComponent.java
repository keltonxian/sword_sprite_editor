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

import cz.ismar.projects.IEdit.ClipBoard;
import cz.ismar.projects.IEdit.IconLabelListModel;
import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.structure.Background;
import cz.ismar.projects.IEdit.structure.Fragment;
import cz.ismar.projects.IEdit.structure.Frame;
import cz.ismar.projects.IEdit.structure.FrameFragment;
import cz.ismar.projects.IEdit.structure.FrameFragmentMasks;
import cz.ismar.projects.IEdit.structure.FrameHolder;
import cz.ismar.projects.IEdit.structure.Sprite;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.MenuKeyEvent;

public class FrameEditComponent extends CentralGridComponent implements KeyListener
{
	public FrameEditComponent()
	{
		tmpX = 0;
		tmpY = 0;
		colisionResizing = false;
		tmpFrame = null;
		setFocusable(true);
		addKeyListener(this);
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
		if(SpriteEditor.sprite.frameHolder.getCurrentFrame() == null)
		{
			g.setColor(getBackground());
			g.fillRect(0, 0, getWidth(), getHeight());
			return;
		}
		Frame frame;
		if(tmpFrame == null)
		{	frame = SpriteEditor.sprite.frameHolder.getCurrentFrame();
		
		}else
		{
			frame = tmpFrame;
		}
		
		if(isOpaque())
		{
			g.setColor(SpriteEditor.framesBackground.getBgColor());
			g.fillRect(0, 0, getWidth(), getHeight());
		}
		
		// 放大或缩小
		AffineTransform affinetransform = new AffineTransform();
		affinetransform.scale(SpriteEditor.scale, SpriteEditor.scale);
		
		int sx = getWidth() / 2;
		int sy = getHeight() / 2;
		
		g.translate(sx, sy);
		SpriteEditor.framesBackground.paint((Graphics2D) g, affinetransform, this);
		g.translate(-sx, -sy);
		
		if(frame != null)
		{
			// 画帮助帧
			if(showHelpFrame && helpFrame != null)
			{
				drawFrame(g, helpFrame, sx, sy, SpriteEditor.scale, false);
			}
			
			// 画原来的帧
			drawFrame(g, frame, sx, sy, SpriteEditor.scale, true);
			
			super.paintComponent(g);
			
			// 画碰撞框
			Rectangle rectangle = frame.getColisionArea();
			if(rectangle != null && SpriteEditor.collisionOn)
			{
				g.setColor(Color.YELLOW);
				int k = sx + (int) rectangle.getX() * SpriteEditor.scale;
				int l = sy + (int) rectangle.getY() * SpriteEditor.scale;
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
	}

	/**
	 * 画帧
	 * 
	 * @param g
	 * @param frame
	 * @param originX
	 * @param originY
	 * @param affinetransform
	 * 				放大缩小,翻转等参数
	 * @param isNormalFrame
	 */
	private void drawFrame(Graphics g, Frame frame, int originX, int originY, int scale, boolean isNormalFrame)
	{
		frame.drawFrame(g, originX, originY, 0, 0, scale, isNormalFrame, true);
		
//		for(int index = frame.size() - 1; index > -1; index--)
//		{
//			FrameFragment framefragment = frame.get(index);
//			
//			// 隐藏的不画
//			if(SpriteEditor.frameFragmentMasks.isEnabled() && SpriteEditor.frameFragmentMasks.isHiden(framefragment))
//			{
//				continue;
//			}
//			
//			Fragment fragment = framefragment.getFragment();
//			if(!fragment.isVisible())
//			{
//				continue;
//			}
//			
//			int sx = screenZeroX + framefragment.getX() * SpriteEditor.scale;
//			int sy = screenZeroY + framefragment.getY() * SpriteEditor.scale;
//			g.translate(sx, sy);
//			if(isNormalFrame) 	// this is normal frame
//			{
//				// daben:modify
//				// ((Graphics2D)g).drawImage(framefragment.getFragment().getImage(),
//				// affinetransform, this);
//				// modify:
//				((Graphics2D) g).drawImage(framefragment.getImage(), affinetransform, this);
//				// ~daben
//			}else		// this is help frame
//			{
//				((Graphics2D) g).setComposite(AlphaComposite.getInstance(3, 0.5F));
//				// daben:modify
//				// ((Graphics2D)g).drawImage(framefragment.getFragment().getImage(),
//				// affinetransform, this);
//				// modify:
//				((Graphics2D) g).drawImage(framefragment.getImage(), affinetransform, this);
//				// ~daben
//				((Graphics2D) g).setComposite(AlphaComposite.SrcOver);
//			}
//			
//			// 被选中切片, 要画上红色的矩形框
//			if(frame.isIndexSelected(index))
//			{
//				g.setColor(Color.RED);
//				// daben:modify
//				// g.drawRect(0, 0, fragment.getWidth() * SpriteEditor.scale,
//				// fragment.getHeight() * SpriteEditor.scale);
//				// g.fillRect(-3, -3, 7, 7);
//				// g.fillRect(fragment.getWidth() * SpriteEditor.scale - 3,
//				// fragment.getHeight() * SpriteEditor.scale - 3, 7, 7);
//				// g.fillRect(-3, fragment.getHeight() * SpriteEditor.scale - 3,
//				// 7, 7);
//				// g.fillRect(fragment.getWidth() * SpriteEditor.scale - 3, -3,
//				// 7, 7);
//				// modify:
//				g.drawRect(0, 0, framefragment.getRectangle().width * SpriteEditor.scale, framefragment.getRectangle().height * SpriteEditor.scale);
//				g.fillRect(-3, -3, 7, 7);
//				g.fillRect(framefragment.getRectangle().width * SpriteEditor.scale - 3, framefragment.getRectangle().height * SpriteEditor.scale - 3, 7, 7);
//				g.fillRect(-3, framefragment.getRectangle().height * SpriteEditor.scale - 3, 7, 7);
//				g.fillRect(framefragment.getRectangle().width * SpriteEditor.scale - 3, -3, 7, 7);
//				// ~daben
//
//			}
//			g.translate(-sx, -sy);
//		}
	}

	private void drawPreviewFrame(Graphics g, Frame frame, int originX, int originY)
	{
		frame.drawFrame(g, originX, originY, 0, 0, 1, true, false);
//		for(int i1 = frame.size() - 1; i1 > -1; i1--)
//		{
//			FrameFragment framefragment = frame.get(i1);
//			if(!SpriteEditor.frameFragmentMasks.isEnabled() || !SpriteEditor.frameFragmentMasks.isHiden(framefragment))
//			{
//				int k = i + framefragment.getX();
//				int l = j + framefragment.getY();
//				g.translate(k, l);
//				((Graphics2D) g).drawImage(framefragment.getFragment().getImage(), null, this);
//				g.translate(-k, -l);
//			}
//		}
	}

	public void mouseMoved(MouseEvent mouseevent)
	{
		SpriteEditor.framePanel.titledBorder.setTitle("鼠标位置 [" + (mouseevent.getX() - getWidth() / 2) / SpriteEditor.scale + "," + (mouseevent.getY() - getHeight() / 2) / SpriteEditor.scale + "]");
		SpriteEditor.framePanel.borderCenterPanel.repaint();
	}

	public void mouseExited(MouseEvent mouseevent)
	{
		SpriteEditor.framePanel.titledBorder.setTitle("当前帧");
		SpriteEditor.framePanel.borderCenterPanel.repaint();
	}

	public void mouseClicked(MouseEvent mouseevent)
	{
		Frame frame = SpriteEditor.sprite.frameHolder.getCurrentFrame();
		if(frame == null)
		{
			return;
		}

		// SpriteEditor.framePanel.frameJList.getSelectionModel().setSelectionInterval(SpriteEditor.sprite.frameHolder.getIndex(),
		// SpriteEditor.sprite.frameHolder.getIndex());

		int i = mouseevent.getX() - getWidth() / 2;
		int j = mouseevent.getY() - getHeight() / 2;
		i = (i + (i <= 0 ? -SpriteEditor.scale / 2 : SpriteEditor.scale / 2)) / SpriteEditor.scale;
		j = (j + (j <= 0 ? -SpriteEditor.scale / 2 : SpriteEditor.scale / 2)) / SpriteEditor.scale;
		frame.setColisionAreaSelected(false);
		Rectangle rectangle = new Rectangle(frame.getColisionArea());
		Rectangle rectangle1 = new Rectangle(frame.getColisionArea());
		rectangle1.grow(2, 2);
		rectangle1.setLocation(rectangle.x - 1, rectangle.y - 1);
		rectangle.grow(-2, -2);
		rectangle.setLocation(rectangle.x, rectangle.y);
		ListSelectionModel listselectionmodel = SpriteEditor.framePanel.frameFragmentJList.getSelectionModel();

		// 碰撞框
		if(SpriteEditor.collisionOn && rectangle1.contains(i, j) && !rectangle.contains(i, j))
		{
			frame.setColisionAreaSelected(true);
		}
		// else
		// {
		// // 2009-11-09 fay fix:选多个切片
		// if((mouseevent.getModifiers() & InputEvent.CTRL_MASK) == 0)
		// {
		// listselectionmodel.clearSelection();
		// }
		//			
		// int size = frame.getFrameFragments().size();
		// for(int k = 0; k < size; k++)
		// {
		// FrameFragment framefragment = frame.get(k);
		// if(!framefragment.getRectangle().contains(i, j) ||
		// SpriteEditor.frameFragmentMasks.isEnabled() &&
		// SpriteEditor.frameFragmentMasks.isHiden(framefragment))
		// continue;
		// if((mouseevent.getModifiers() & 3) == 0)
		// {
		// listselectionmodel.setSelectionInterval(k, k);
		// break;
		// }
		// if((mouseevent.getModifiers() & 1) != 0)
		// {
		// if(listselectionmodel.isSelectedIndex(k))
		// continue;
		// listselectionmodel.addSelectionInterval(k, k);
		// break;
		// }
		// if((mouseevent.getModifiers() & 2) == 0)
		// continue;
		// if(!listselectionmodel.isSelectedIndex(k))
		// {
		// listselectionmodel.addSelectionInterval(k, k);
		// frame.setIndex(k);
		// frame.setIndices(SpriteEditor.framePanel.frameFragmentJList.getSelectedIndices());
		// }else
		// {
		// listselectionmodel.removeSelectionInterval(k, k);
		// }
		// break;
		// }
		//
		// int ai[] =
		// SpriteEditor.framePanel.frameFragmentJList.getSelectedIndices();
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

	public void mousePressed(MouseEvent mouseevent)
	{
		Frame frame = SpriteEditor.sprite.frameHolder.getCurrentFrame();
		int tempX = mouseevent.getX() - getWidth() / 2;
		int tempY = mouseevent.getY() - getHeight() / 2;
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
		ListSelectionModel listselectionmodel = SpriteEditor.framePanel.frameFragmentJList.getSelectionModel();

		// 2009-11-09 fay: fix选多个frame fragment

		// 没按下ctrl, 清空原来选中的
		if((mouseevent.getModifiers() & InputEvent.CTRL_MASK) == 0)
		{
			listselectionmodel.clearSelection();
			frame.setIndex(-1);
			frame.setIndices(null);
		}

		int size = frame.getFrameFragments().size();
		for(int k = 0; k < size; k++)
		{
			FrameFragment framefragment = frame.get(k);

			// 不在范围内 -> continue
			if(!framefragment.contains(1, tempX, tempY) || (SpriteEditor.frameFragmentMasks.isEnabled() && SpriteEditor.frameFragmentMasks.isHiden(framefragment)))
			{
				continue;
			}

			// 没有按下ctrl-> break
			if((mouseevent.getModifiers() & InputEvent.CTRL_MASK) == 0)
			{
				listselectionmodel.setSelectionInterval(k, k);
				break;
			}

			// if((mouseevent.getModifiers() & InputEvent.SHIFT_MASK) != 0)
			// {
			// if(listselectionmodel.isSelectedIndex(k))
			// {
			// continue;
			// }
			// listselectionmodel.addSelectionInterval(k, k);
			// break;
			// }

			// if((mouseevent.getModifiers() & InputEvent.CTRL_MASK) == 0)
			// {
			// continue;
			// }

			if(!listselectionmodel.isSelectedIndex(k)) // 原来没选中, 追加
			{
				listselectionmodel.addSelectionInterval(k, k);
				frame.setIndex(k);
				frame.setIndices(SpriteEditor.framePanel.frameFragmentJList.getSelectedIndices());
			}else
			// 原来选中, 反选
			{
				listselectionmodel.removeSelectionInterval(k, k);
			}
		}

		// int ai[] =
		// SpriteEditor.framePanel.frameFragmentJList.getSelectedIndices();
		// if(ai == null || ai.length == 0)
		// {
		// frame.setIndex(-1);
		// }else
		// {
		// frame.setIndex(ai[0]);
		// frame.setIndices(ai);
		// }
		repaint();
	}

	public void mouseDragged(MouseEvent mouseevent)
	{
		updateSize(mouseevent);
		repaint();
	}

	public void mouseReleased(MouseEvent mouseevent)
	{
		Frame frame = SpriteEditor.sprite.frameHolder.getCurrentFrame();
		if(frame == null)
		{
			return;
		}

		// 右键菜单
		if(mouseevent.isPopupTrigger())
		{
			if(frame != null)
			{
				// daben:add
				// 检查撤销动作是否有效
				if(frame.getUndoLisSize() < 1)
				{
					SpriteEditor.undoFrameMenuItem.setEnabled(false);
				}else
				{
					SpriteEditor.undoFrameMenuItem.setEnabled(true);
				}
				// ~daben

				SpriteEditor.pasteFragmentToFrameMenuItem.setEnabled(SpriteEditor.fragmentsClipBoard.getItems().size() > 0 || SpriteEditor.collisionClipboard != null);
				if(frame.getIndex() > -1)
				{
					if(frame.isFirst())
					{
						SpriteEditor.moveTopFrameMenuItem.setEnabled(false);
						SpriteEditor.moveUpFromFrameMenuItem.setEnabled(false);
					}else
					{
						SpriteEditor.moveTopFrameMenuItem.setEnabled(true);
						SpriteEditor.moveUpFromFrameMenuItem.setEnabled(true);
					}
					if(frame.isLast())
					{
						SpriteEditor.moveBottomFromFrameMenuItem.setEnabled(false);
						SpriteEditor.moveDownFromFrameMenuItem.setEnabled(false);
					}else
					{
						SpriteEditor.moveBottomFromFrameMenuItem.setEnabled(true);
						SpriteEditor.moveDownFromFrameMenuItem.setEnabled(true);
					}
					SpriteEditor.deleteFromFrameMenuItem.setEnabled(true);
					// daben:modify
					// SpriteEditor.showHideFrameMenuItem.setEnabled(true);
					// modify:
					SpriteEditor.showHideFrameMenuItem.setEnabled(SpriteEditor.frameFragmentMasks.isEnabled());
					// ~daben
					SpriteEditor.copyFragmentFromFrameMenuItem.setEnabled(true);
					// daben:add
					SpriteEditor.transformFrameFragmentMenu.setEnabled(true);
					// ~daben
				}else
				{
					SpriteEditor.moveTopFrameMenuItem.setEnabled(false);
					SpriteEditor.moveUpFromFrameMenuItem.setEnabled(false);
					SpriteEditor.moveBottomFromFrameMenuItem.setEnabled(false);
					SpriteEditor.moveDownFromFrameMenuItem.setEnabled(false);
					SpriteEditor.deleteFromFrameMenuItem.setEnabled(false);
					SpriteEditor.showHideFrameMenuItem.setEnabled(false);
					SpriteEditor.copyFragmentFromFrameMenuItem.setEnabled(frame.isColisionAreaSelected());
					// daben:add
					SpriteEditor.transformFrameFragmentMenu.setEnabled(false);
					// ~daben
				}

				SpriteEditor.frameFragmentPopup.show(mouseevent.getComponent(), mouseevent.getX(), mouseevent.getY());
			}
		}else
		{
			colisionResizing = false;
			// daben:add
			frame.updateImage();
			// TODO: fay, 这一句有点问题, 待解决
			// SpriteEditor.framePanel.frameListModel.update();
			// SpriteEditor.mainFramingPanel.frameJList.getSelectionModel().setSelectionInterval(SpriteEditor.project.frameHolder.getIndex(),
			// SpriteEditor.project.frameHolder.getIndex());
			firstStep = true;
			// ~daben
			repaint();
		}
	}

	void updateSize(MouseEvent mouseevent)
	{
		Frame frame = SpriteEditor.sprite.frameHolder.getCurrentFrame();
		if(frame == null)
			return;
		int i = mouseevent.getX() - getWidth() / 2;
		int j = mouseevent.getY() - getHeight() / 2;
		i = (i + (i <= 0 ? -SpriteEditor.scale / 2 : SpriteEditor.scale / 2)) / SpriteEditor.scale;
		j = (j + (j <= 0 ? -SpriteEditor.scale / 2 : SpriteEditor.scale / 2)) / SpriteEditor.scale;
		if(frame.isColisionAreaSelected())
		{
			if(colisionResizing)
			{
				int k = (int) ((double) i - frame.getColisionArea().getX());
				int l = (int) ((double) j - frame.getColisionArea().getY());
				frame.getColisionArea().setSize(k, l);
			}else
			{
				frame.getColisionArea().setLocation(((int) frame.getColisionArea().getX() + i) - tmpX, ((int) frame.getColisionArea().getY() + j) - tmpY);
			}
			// daben:add
			if(SpriteEditor.animationsSaved)
				SpriteEditor.animationsSaved = false;
			// ~daben
		}else
		{
			// daben:add
			if(firstStep)
			{
				SpriteEditor.sprite.frameHolder.getCurrentFrame().saveUndo();
				firstStep = false;
			}
			// ~daben
			moveSelectedFragments(i - tmpX, j - tmpY);
		}
		tmpX = i;
		tmpY = j;
	}

	private void moveSelectedFragments(int offsetX, int offsetY)
	{
		Frame frame = SpriteEditor.sprite.frameHolder.getCurrentFrame();
		if(frame == null)
		{
			return;
		}

		int ai[] = frame.getIndices();
		if(ai != null)
		{
			for(int k = 0; k < ai.length; k++)
			{
				FrameFragment framefragment = frame.get(ai[k]);
				if(framefragment != null)
				{
					framefragment.moveDelta(offsetX, offsetY);

					// daben:remove 移动中没必要更新帧缩略图，太占用资源，会使得移动不连贯，改到mouseReleased中
					// frame.updateImage();
					// ~daben
				}
			}
			// daben:add add information of position to titleBorder
			int posX = frame.get(ai[0]).getTopLeftX(1);
			int posY = frame.get(ai[0]).getTopLeftY(1);
			SpriteEditor.framePanel.titledBorder.setTitle("Current frame composition [" + posX + "," + posY + "]");
			SpriteEditor.framePanel.borderCenterPanel.repaint();
			// ~daben

			SpriteEditor.animationsSaved = false;
		}
	}

	public void keyPressed(KeyEvent keyevent)
	{
		if(keyevent.getModifiers() == InputEvent.ALT_MASK)
		{
			int j = keyevent.getKeyCode();
			switch(j)
			{
			case KeyEvent.VK_UP: // '&'
				if(SpriteEditor.sprite.frameHolder.getCurrentFrame().moveUp())
				{
					SpriteEditor.framePanel.frameFragmentJList.getSelectionModel().setSelectionInterval(SpriteEditor.sprite.frameHolder.getCurrentFrame().getIndex(), SpriteEditor.sprite.frameHolder.getCurrentFrame().getIndex());
				}
				break;

			case KeyEvent.VK_DOWN: // '('
				if(SpriteEditor.sprite.frameHolder.getCurrentFrame().moveDown())
				{
					SpriteEditor.framePanel.frameFragmentJList.getSelectionModel().setSelectionInterval(SpriteEditor.sprite.frameHolder.getCurrentFrame().getIndex(), SpriteEditor.sprite.frameHolder.getCurrentFrame().getIndex());
				}
				break;

			case KeyEvent.VK_HOME: // '$'
				if(SpriteEditor.sprite.frameHolder.getCurrentFrame().moveTop())
				{
					SpriteEditor.framePanel.frameFragmentJList.getSelectionModel().setSelectionInterval(0, 0);
				}
				break;

			case KeyEvent.VK_END: // '#'
				if(SpriteEditor.sprite.frameHolder.getCurrentFrame().moveBottom())
				{
					SpriteEditor.framePanel.frameFragmentJList.getSelectionModel().setSelectionInterval(SpriteEditor.sprite.frameHolder.getCurrentFrame().getIndex(), SpriteEditor.sprite.frameHolder.getCurrentFrame().getIndex());
				}
				break;
			}
		}
		// daben:add
		else if(keyevent.getModifiers() == InputEvent.CTRL_MASK)
		{
			if(keyevent.getKeyCode() == KeyEvent.VK_Z)
			{
				SpriteEditor.sprite.frameHolder.getCurrentFrame().undo();
			}else if(keyevent.getKeyCode() == KeyEvent.VK_C)
			{
				SpriteEditor.fragmentsClipBoard.insertItems(SpriteEditor.framePanel.frameFragmentJList);
				Frame frame = SpriteEditor.sprite.frameHolder.getCurrentFrame();
				if(frame.isColisionAreaSelected())
					SpriteEditor.collisionClipboard = frame.getColisionArea();
				else
					SpriteEditor.collisionClipboard = null;
			}else if(keyevent.getKeyCode() == KeyEvent.VK_V)
			{
				java.util.List list = SpriteEditor.fragmentsClipBoard.getItems();
				Frame frame = SpriteEditor.sprite.frameHolder.getCurrentFrame();
				if(list.size() > 0)
				{
					frame.saveUndo();
					int i = frame.getIndex();
					if(i < 0)
						i = 0;
					for(int j = 0; j < list.size(); j++)
					{
						FrameFragment framefragment = (FrameFragment) list.get(j);
						frame.add(i + j, new FrameFragment(framefragment));
						if(SpriteEditor.animationsSaved)
							SpriteEditor.animationsSaved = false;
					}

					SpriteEditor.sprite.frameHolder.getCurrentFrame().updateImage();
					SpriteEditor.framePanel.frameFragmentListModel.update();
					SpriteEditor.framePanel.frameListModel.update();
					SpriteEditor.framePanel.frameJList.getSelectionModel().setSelectionInterval(SpriteEditor.sprite.frameHolder.getIndex(), SpriteEditor.sprite.frameHolder.getIndex());
					SpriteEditor.framePanel.frameFragmentJList.getSelectionModel().setSelectionInterval(i, i);
				}
				if(SpriteEditor.collisionClipboard != null)
				{
					frame.setColisionArea((Rectangle) SpriteEditor.collisionClipboard.clone());
				}
			}
		}
		// ~daben
		else if(keyevent.getKeyCode() == KeyEvent.VK_DELETE && SpriteEditor.sprite.frameHolder.getCurrentFrame().remove())
		{
			// daben:add 更新小视图
			SpriteEditor.sprite.frameHolder.getCurrentFrame().updateImage();
			SpriteEditor.framePanel.frameListModel.update();
			// ~daben
			SpriteEditor.framePanel.frameFragmentListModel.update();
			SpriteEditor.framePanel.frameJList.getSelectionModel().setSelectionInterval(SpriteEditor.sprite.frameHolder.getIndex(), SpriteEditor.sprite.frameHolder.getIndex());
			SpriteEditor.framePanel.frameFragmentJList.getSelectionModel().setSelectionInterval(SpriteEditor.sprite.frameHolder.getCurrentFrame().getIndex(), SpriteEditor.sprite.frameHolder.getCurrentFrame().getIndex());
		}else if(keyevent.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			Frame frame = SpriteEditor.sprite.frameHolder.getCurrentFrame();

			SpriteEditor.framePanel.frameFragmentJList.getSelectionModel().clearSelection();
			frame.setIndex(-1);
			frame.setIndices(null);
			repaint();
		}
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
					SpriteEditor.sprite.frameHolder.getCurrentFrame().saveUndo();
					firstStep = false;
				}
				moveSelectedFragments(-1, 0);
				return;
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_D:
				if(firstStep)
				{
					SpriteEditor.sprite.frameHolder.getCurrentFrame().saveUndo();
					firstStep = false;
				}
				moveSelectedFragments(1, 0);
				return;
			case KeyEvent.VK_UP:
			case KeyEvent.VK_W:
				if(firstStep)
				{
					SpriteEditor.sprite.frameHolder.getCurrentFrame().saveUndo();
					firstStep = false;
				}
				moveSelectedFragments(0, -1);
				return;
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_S:
				if(firstStep)
				{
					SpriteEditor.sprite.frameHolder.getCurrentFrame().saveUndo();
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
			previewPanel = new JPanel() {

				protected void paintComponent(Graphics g)
				{
					g.setColor(Color.BLACK);
					g.fillRect(0, 0, getWidth(), getHeight());
					g.setColor(SpriteEditor.framesBackground.getBgColor());
					g.fillRect(2, 2, getWidth() - 4, getHeight() - 4);
					Frame frame = SpriteEditor.sprite.frameHolder.getCurrentFrame();
					if(frame != null)
						drawPreviewFrame(g, frame, getWidth() / 2, getHeight() / 2);
				}

			};
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
