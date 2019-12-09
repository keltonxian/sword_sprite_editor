/**
 * 2007-9-12 daben
 * - add transformable function #see daben:add
 * - modify some old methods    #see daben:modify
 * - add functions:
 *               setName(String newName)
 *               getTransformType()
 *               setTransfromType(int type)
 *               getImage()
 *               updateRectangle()
 *               updateImage()
 * - add two attributes:
 *                    private int transformType  --> the type of transformation,see the remark
 *                    private BufferedImage transformedImage  --> a copy of fragment.getImage(),which will be transformed
 *
 * 2007-9-13 daben
 * - add function:
 *               updateName()
 *               
 * 2007-9-14 daben
 * - add
 * --> InOut.setAttribute(element, "transformation_byte_r3", "" + getTransformType());
 * - adds in
 * --> input(Element element)
 * --> FrameFragment(Fragment fragment1, int i, int j)
 * --> FrameFragment(Fragment fragment1, String s, int i, int j)
 * --> FrameFragment(int i, Fragment fragment1, String s, int j, int k)
 * - modify
 * - in
 * --> setX(int i)
 * --> setY(int i)
 * --> moveDelta(int i, int j)
 * 
 * 2007-9-18 daben
 * - modify
 * --> FrameFragment(FrameFragment framefragment)
 */

package cz.ismar.projects.IEdit.structure;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import cz.ismar.projects.IEdit.io.InOut;
import cz.ismar.projects.IEdit.io.StoringException;
import cz.ismar.projects.IEdit.io.StoringNumberException;


public class FrameFragment implements Rescalable, InOutAble
{

	public void rescaleTimes(float scale)
	{
//		if(scale > 0.0F)
//		{
//			x *= scale;
//			y *= scale;
////			rectangle = new Rectangle((int) (rectangle.getX() * (double) f), (int) (rectangle.getY() * (double) f), (int) (rectangle.getWidth() * (double) f), (int) (rectangle.getHeight() * (double) f));
//		}else
//		{
//			x /= -scale;
//			y /= -scale;
////			rectangle = new Rectangle((int) (rectangle.getX() / (double) (-f)), (int) (rectangle.getY() / (double) (-f)), (int) (rectangle.getWidth() / (double) (-f)), (int) (rectangle.getHeight() / (double) (-f)));
//		}
	}

	/**
	 * 输出翻转信息
	 * 
	 * @param document
	 * @param frameID
	 * @return
	 * @throws StoringException
	 */
	public Element outputTransform(Document document, int frameID) throws StoringException
	{
		// 0为没有翻转
		if(getTransformType() == 0)
		{
			return null;
		}

		Element element = document.createElement("Transform");
		try
		{
			InOut.setAttribute(element, "frameID", "" + frameID);
			InOut.setAttribute(element, "fragmentID", "" + getID());
			InOut.setAttribute(element, "transformType", "" + getTransformType());
		}catch(StoringNumberException storingnumberexception)
		{
		}
		return element;
	}

	public Element output(Document document) throws StoringException
	{
		Element element = document.createElement("frameFragment");
		try
		{
			InOut.setAttribute(element, "id_byte_a0", "" + getID());
			InOut.setAttribute(element, "name_utf_a1", getName());
			InOut.setAttribute(element, "x_byte_r0", "" + getTopLeftX());
			InOut.setAttribute(element, "y_byte_r1", "" + getTopLeftY());
			InOut.setAttribute(element, "index_shortIRfragments_r2", getFragment().getFragmentHolder().getName() + "_" + getFragment().getID());
			InOut.setAttribute(element, "fragmentId_byte_n0", "" + getFragment().getID());
			InOut.setAttribute(element, "fragmentsFileId_byte_n1", "" + getFragment().getFragmentHolder().getID());
			
			InOut.setAttribute(element, "transformType", "" + getTransformType());
			
			InOut.setAttribute(element, "z_transformation_byte_r3", "" + getTransformType());

			// 2009-12-03 输出center point
			InOut.setAttribute(element, "center_x", "" + centerX);
			InOut.setAttribute(element, "center_y", "" + centerY);
			
		}catch(StoringNumberException storingnumberexception)
		{
			String s = storingnumberexception.getMessage() + " Fragment '" + getName() + "' id='" + getID() + "'.";
			throw new StoringException(s);
		}
		return element;
	}

	public void input(Element element)
	{
		id = Integer.parseInt(element.getAttribute(InOut.compatibilityInput ? "id" : "id_byte_a0"));
		name = element.getAttribute(InOut.compatibilityInput ? "name" : "name_utf_a1");

		// 切片
		fragmentId = Integer.parseInt(element.getAttribute(InOut.compatibilityInput ? "fragment-id" : "fragmentId_byte_n0"));
		if(!InOut.compatibilityInput)
		{
			fragmentFileIndex = Integer.parseInt(element.getAttribute("fragmentsFileId_byte_n1"));
		}
		FragmentHolder fragmentholder = sprite.fragmentHoldersList.getFragmentHolderById(fragmentFileIndex);
		fragment = fragmentholder.getFragmentById(fragmentId);
//		rectangle = new Rectangle(x, y, fragment.getWidth(), fragment.getHeight());

		// 翻转参数
		transformType = InOut.parseInt(element, "z_transformation_byte_r3", 0);
		
		int x = 0;
		int y = 0;
		// 2009-12-03 fay: 只读centerPoint, 兼容以前的TopLeftPoint 
		if(element.hasAttribute("center_x") && element.hasAttribute("center_y"))
		{
			centerX = InOut.parseInt(element, "center_x", 0);
			centerY = InOut.parseInt(element, "center_y", 0);
		}else
		{
			x = InOut.parseInt(element, InOut.compatibilityInput ? "x" : "x_byte_r0", 0);
			y = InOut.parseInt(element, InOut.compatibilityInput ? "y" : "y_byte_r1", 0);
			
			// 确定中心点
			initCenterPoint(x, y);
		}
	}
	
	/**
	 * 初始化"中心点"
	 * 
	 * @param x
	 * @param y
	 */
	private void initCenterPoint(int x, int y)
	{
		if(transformType == TRANSFORM_ROTE_90 || transformType == TRANSFORM_ROTE_270)
		{
			centerX = x + fragment.getHeight()/2;
			centerY = y + fragment.getWidth()/2;
		}else
		{
			centerX = x + fragment.getWidth()/2;
			centerY = y + fragment.getHeight()/2;
		}
	}

	public FrameFragment(Sprite _sprite)
	{
		sprite = _sprite;
	}

	// daben:modify
	/*
	 * public FrameFragment(FrameFragment framefragment) {
	 * this(framefragment.getFragment(), framefragment.getX(),
	 * framefragment.getY()); }
	 */
	// modify:
	public FrameFragment(FrameFragment framefragment)
	{
		this(framefragment.getFragment(), framefragment.getTopLeftX(1), framefragment.getTopLeftY(1));
		transformType = framefragment.getTransformType();
		this.updateImage();
		this.updateName();
	}

	// ~daben

	public FrameFragment(Fragment fragment1)
	{
		this(fragment1, 0, 0);
	}

	public FrameFragment(Fragment fragment1, int _x, int _y)
	{
		// this(fragment1, "name", i, j);
		this(fragment1, fragment1.getName(), _x, _y);
	}

	public FrameFragment(Fragment fragment1, String _name, int _x, int _y)
	{
		id = ++currentID;
		fragment = fragment1;
		name = _name;
		
//		rectangle = new Rectangle(_x, _y, fragment1.getWidth(), fragment1.getHeight());
//		x = _x;
//		y = _y;
		
		transformType = 0;
		updateImage();
		
		initCenterPoint(_x, _y);
	}
	
	public FrameFragment(int i, Fragment fragment1, String _name, int _x, int _y)
	{
		id = i;
		if(currentID < i)
			currentID = i;
		fragment = fragment1;
		name = _name;
		
//		x = _x;
//		y = _y;
//		rectangle = new Rectangle(_x, _y, fragment1.getWidth(), fragment1.getHeight());
		
		transformType = 0;
		updateImage();
		initCenterPoint(_x, _y);
	}

	public Fragment getFragment()
	{
		return fragment;
	}

	public void setFragment(Fragment fragment1)
	{
		fragment = fragment1;
	}
	
	/**
	 * 原始的 top_left_x
	 * @return
	 */
	private int getTopLeftX()
	{
		return centerX - getWidth(1)/2;
	}
	
	/**
	 * 原始的 top_left_y
	 * @return
	 */
	private int getTopLeftY()
	{
		return centerY - getHeight(1)/2;
	}

	public int getTopLeftX(int scale)
	{
		int tempX = getTopLeftX() + getAvatarPointAfterTransform().x;
		
		if(scale > 0)
		{
			return tempX * scale;
		}else
		{
			return tempX / -scale;
		}
	}
	
	public int getTopLeftY(int scale)
	{
		int tempY = getTopLeftY() + getAvatarPointAfterTransform().y;
		
		if(scale > 0)
		{
			return tempY * scale;
		}else
		{
			return tempY / -scale;
		}
	}
	
	/**
	 * 根据翻转, avatar point 也要做一些改变
	 * @return
	 */
	public Point getAvatarPointAfterTransform()
	{
		if(fragment == null)
		{
			return new Point();
		}
		
		int avatarX = fragment.getAvatarX();
		int avatarY = fragment.getAvatarY();
		
//	case TRANSFORM_ROTE_270:	// 顺时针旋转270度
//		affineTransform = new AffineTransform(0, -1, 1, 0, 0, width);
//		break;
//		
//	case TRANSFORM_ROTE_90:		// 旋转90度
//		dstImage = new BufferedImage(height, width, srcImage.getType());
//		affineTransform = new AffineTransform(0, 1, -1, 0, height, 0);
//		break;
//	case TRANSFORM_ROTE_180:	// 顺时针旋转180度
//		dstImage = new BufferedImage(width, height, srcImage.getType());
//		affineTransform = new AffineTransform(-1, 0, 0, -1, width, height);
//		break;
//	case TRANSFORM_ROTE_270:	// 顺时针旋转270度
//		dstImage = new BufferedImage(height, width, srcImage.getType());
//		affineTransform = new AffineTransform(0, -1, 1, 0, 0, width);
//		affineTransform = new AffineTransform(0, 1, -1, 0, 0, width);
		
		switch(transformType)
		{
		case TRANSFORM_FLIPX:		// 水平翻转
			return new Point(-avatarX, avatarY);
		case TRANSFORM_FLIPY:		// 垂直翻转
			return new Point(avatarX, -avatarY);
		case TRANSFORM_ROTE_90:
//			affineTransform = new AffineTransform(0, 1, -1, 0, height, 0);
			return new Point(-avatarY, avatarX);
		case TRANSFORM_ROTE_180:
			return new Point(-avatarX, -avatarY);
		case TRANSFORM_ROTE_270:
//			affineTransform = new AffineTransform(0, -1, 1, 0, 0, width);
			return new Point(avatarY, -avatarX);
		default:
			return new Point(avatarX, avatarY);
		}
		
	}
	
//	public int getAvatarX()
//	{
//		return fragment.getAvatarX();
//	}
//	public int getAvatarY()
//	{
//		return fragment.getAvatarY();
//	}

//	public void setX(int _x)
//	{
//		x = _x;
////		rectangle = new Rectangle(x, y, transformedImage.getWidth(), transformedImage.getHeight());
//	}

//	public void setY(int _y)
//	{
//		y = _y;
////		rectangle = new Rectangle(x, y, transformedImage.getWidth(), transformedImage.getHeight());
//	}

	public void moveDelta(int offsetX, int offsetY)
	{
		centerX += offsetX;
		centerY += offsetY;
//		rectangle = new Rectangle(x, y, transformedImage.getWidth(), transformedImage.getHeight());
	}
	
	/**
	 * 2009-11-11 fay 
	 * 移动avatarX 与 avatarY, 不移动 x, y
	 * @param offsetX
	 * @param offsetY
	 */
	public void moveDeltaAvatar(int offsetX, int offsetY)
	{
		fragment.moveAvatarPoint(offsetX, offsetY);
//		rectangle = new Rectangle(x + fragment.getAvatarX(), y + fragment.getAvatarY(), transformedImage.getWidth(), transformedImage.getHeight());
		
//		System.out.println("avatar -> (" + fragment.getAvatarX() + ":" + fragment.getAvatarY() + ")");
	}
	
	/**
	 * 是否包含指定的点
	 * @param scale
	 * @param pointX
	 * @param pointY
	 * @return
	 */
	public boolean contains(int scale, int pointX, int pointY)
	{
		Rectangle rect = new Rectangle(getTopLeftX(scale), getTopLeftY(scale), getWidth(scale), getHeight(scale));
		return rect.contains(pointX, pointY);
	}

	public int getWidth(float scale)
	{
		int width = 0;
		if(transformType == TRANSFORM_ROTE_90 || transformType == TRANSFORM_ROTE_270)
		{
			width = fragment.getHeight();
		}else
		{
			width = fragment.getWidth();
		}
		
		if(scale < 0)
		{
			return (int) (width / -scale);
		}else
		{
			return (int) (width * scale);
		}
//		return transformedImage.getWidth();
	}
	
	public int getHeight(float scale)
	{
		int height = 0;
		if(transformType == TRANSFORM_ROTE_90 || transformType == TRANSFORM_ROTE_270)
		{
			height = fragment.getWidth();
		}else
		{
			height = fragment.getHeight();
		}
		
		if(scale < 0)
		{
			return (int) (height / -scale);
		}else
		{
			return (int) (height * scale);
		}
//		return transformedImage.getHeight();
	}
	

//	public Rectangle getRectangle()
//	{
//		return rectangle;
//	}

	public String getName()
	{
		return name != null ? name : fragment.getName() + " " + id;
	}

	public int getID()
	{
		return id;
	}

	public String toString()
	{
		return name != null ? name : fragment.getName() + " " + id;
	}

	public void defragmentID(int i)
	{
		id = i;
		currentID = i;
	}

	public Frame getParentFrame()
	{
		return parentFrame;
	}

	public void setParentFrame(Frame frame)
	{
		if(parentFrame != null && frame != null)
		{
			throw new IllegalArgumentException("parentFrame can be included only in one frame");
		}else
		{
			parentFrame = frame;
			return;
		}
	}

	// daben:add
	public int getTransformType()
	{
		return transformType;
	}

	public void setTransfromType(int type)
	{
		transformType = type;
	}

	public BufferedImage getImage()
	{
//		if(true)
//			return fragment.getImage();
		
		if(transformedImage == null)
		{
			// System.out.println("transformedImage haven't been inited !!!!!!!!");
			return fragment.getImage();
		}
		return transformedImage;
	}

	// 若变换切片图象，则也要调整切片矩形(切片框代表的矩形)
//	public void updateRectangle()
//	{
//		int type = transformType;
//		switch(type)
//		{
//		// 旋转后放倒切片图象，宽高互换
//		case 3:
//		case 5:
//		case 6:
//		case 7:
//			rectangle.width = fragment.getRectangle().height;
//			rectangle.height = fragment.getRectangle().width;
//			break;
//		case 0:
//		case 1:
//		case 2:
//		case 4:
//		default:
//			rectangle.width = fragment.getRectangle().width;
//			rectangle.height = fragment.getRectangle().height;
//		}
//		return;
//	}

	public void updateImage()
	{
		BufferedImage srcImage = fragment.getImage();
		int width = srcImage.getWidth();
		int height = srcImage.getHeight();
		BufferedImage dstImage = null;
		AffineTransform affineTransform = null;
		try
		{
			switch(transformType)
			{
			case TRANSFORM_FLIPX:		// 水平翻转
				dstImage = new BufferedImage(width, height, srcImage.getType());
				affineTransform = new AffineTransform(-1, 0, 0, 1, width, 0);
				break;
			case TRANSFORM_FLIPY:		// 垂直翻转
				dstImage = new BufferedImage(width, height, srcImage.getType());
				affineTransform = new AffineTransform(1, 0, 0, -1, 0, height);
				break;
			case TRANSFORM_ROTE_90:		// 旋转90度
				dstImage = new BufferedImage(height, width, srcImage.getType());
				affineTransform = new AffineTransform(0, 1, -1, 0, height, 0);
				break;
			case TRANSFORM_ROTE_180:	// 顺时针旋转180度
				dstImage = new BufferedImage(width, height, srcImage.getType());
				affineTransform = new AffineTransform(-1, 0, 0, -1, width, height);
				break;
			case TRANSFORM_ROTE_270:	// 顺时针旋转270度
				dstImage = new BufferedImage(height, width, srcImage.getType());
				affineTransform = new AffineTransform(0, -1, 1, 0, 0, width);
				break;
			case 6:						// 关于Y=X轴翻转
				dstImage = new BufferedImage(height, width, srcImage.getType());
				affineTransform = new AffineTransform(0, -1, -1, 0, height, width);
				break;
			case 7:						// 关于Y=-X轴翻转
				dstImage = new BufferedImage(height, width, srcImage.getType());
				affineTransform = new AffineTransform(0, 1, 1, 0, 0, 0);
				break;
			case 0:
				dstImage = new BufferedImage(width, height, srcImage.getType());
				affineTransform = new AffineTransform();
				break;
			}
			
			AffineTransformOp affineTransformOp = new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			affineTransformOp.filter(srcImage, dstImage);
			transformedImage = dstImage;
//			updateRectangle();
		}catch(Exception e)
		{
			transformedImage = fragment.getImage();
//			rectangle = fragment.getRectangle();
			System.out.println("FrameFragment transform image failure");
		}
	}

	public void updateName()
	{
		int type = transformType;
		switch(type)
		{
		case 0:
			name = fragment.getName();
			break;
		case 1:
			name = fragment.getName() + ":Flip X";
			break;
		case 2:
			name = fragment.getName() + ":Flip Y";
			break;
		case 3:
			name = fragment.getName() + ":Rotate 90";
			break;
		case 4:
			name = fragment.getName() + ":Rotate 180";
			break;
		case 5:
			name = fragment.getName() + ":Rotate 270";
			break;
		case 6:
			name = fragment.getName() + ":Flip Y=X";
			break;
		case 7:
			name = fragment.getName() + ":Flip Y=-X";
			break;
		}
	}

	/**
	 * 重新从sprite得到fragment
	 */
	public void refresh()
	{
		// System.out.println("fragmentFileIndex == " + fragmentFileIndex);
		// System.out.println("fragmentFileIndex == " + fragmentId);

		// 重新得到fragment holder
		FragmentHolder fragmentholder = sprite.fragmentHoldersList.getFragmentHolderById(fragmentFileIndex);
		// 重新得到fragment
		fragment = fragmentholder.getFragmentById(fragmentId);
		updateImage();
	}

	public FrameFragment getFrameFragment()
	{
		return new FrameFragment(this);
	}
	
	/** 是否可见 */
	public boolean isVisible()
	{
		if(fragment == null)
		{
			return false;
		}
		
		return fragment.getFragmentHolder().isVisible();
	}
	
	/**
	 * 2009-11-19
	 * 复制一个frame fragment
	 */
	public FrameFragment clone()
	{
		FrameFragment ff = new FrameFragment(this.sprite);
		
		ff.fragmentFileIndex = fragmentFileIndex;
		ff.fragmentId = fragmentId;
		ff.id = id;
//		ff.x = x;
//		ff.y = y;
		ff.centerX = centerX;
		ff.centerY = centerY;
		ff.name = name;
//		ff.rectangle = new Rectangle(rectangle);
		ff.fragment = fragment;
		ff.parentFrame = parentFrame;
		ff.transformType = transformType;
		ff.transformedImage = transformedImage;
		
		return ff;
	}

	public static final int TRANSFORM_NONE = 0;
	public static final int TRANSFORM_FLIPX = 1;
	public static final int TRANSFORM_FLIPY = 2;
	public static final int TRANSFORM_ROTE_90 = 3;
	public static final int TRANSFORM_ROTE_180 = 4;
	public static final int TRANSFORM_ROTE_270 = 5;
	
	/** 2009-11-05 保存切片文件Index与切片id */
	private int fragmentFileIndex = -1;
	private int fragmentId = -1;

	private static int currentID;
	private int id;
//	private int x;
//	private int y;
	private String name;
//	private Rectangle rectangle;
	private Fragment fragment;
	private Sprite sprite;
	private Frame parentFrame;
	// daben:add
	// 变换类型：0为没有变换，1为水平翻转，2为垂直翻转，3为旋转90度，4为旋转180度，5为旋转270度，6为关于Y=X轴翻转，7为关于Y=-X轴翻转
	private int transformType;
	// 变换后的图象，以fragment.getImage()为基础变换后的结果
	private BufferedImage transformedImage;
	// ~daben
	
	private int centerX = 0;
	private int centerY = 0;
}
