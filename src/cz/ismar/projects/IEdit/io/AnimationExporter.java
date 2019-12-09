/**
 * 2007-9-14 daben
 * - modify
 * --> in method drawFrame
 */

package cz.ismar.projects.IEdit.io;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.structure.Animation;
import cz.ismar.projects.IEdit.structure.Frame;
import cz.ismar.projects.IEdit.structure.Gesture;
import cz.ismar.projects.IEdit.structure.Sprite;

public class AnimationExporter
{

	public AnimationExporter()
	{
		currentX = 0;
		currentY = 0;
		scale = 1;
		setExportImgSize(300, 300);
	}

	public void setExportImgSize(int _bufferW, int _bufferH)
	{
		bufferW = _bufferW;
		bufferH = _bufferH;
		bufferImage = new BufferedImage(bufferW, bufferH, 2);
		drawFirstAnimFrame();
	}

	private void drawFirstAnimFrame()
	{
		try
		{
			Animation animation = (Animation) SpriteEditor.sprite.getAnimations().get(0);
			setPosition(0, 0);
			drawAnimationFrame(animation);
		}catch(Exception exception)
		{
		}
	}

	public int getExportScale()
	{
		return scale;
	}

	public void setExportScale(int i)
	{
		scale = i;
		drawFirstAnimFrame();
	}

	/**
	 * 通知export listener
	 * 
	 * @param eventId
	 * @param percent
	 */
	private void notifyExportListener(int eventId, int percent)
	{
		if(exporterListener == null)
		{
			return;
		}

		exporterListener.exportUpdated(eventId, percent);
	}

	public void export(File file) throws IOException
	{
		stoppExport = false;
		notifyExportListener(ExporterListener.EVENT_STARTED, 0);
		if(file == null)
		{
			file = new File("./");
		}

		Iterator<ImageWriter> imageWriters = ImageIO.getImageWritersByFormatName("png");
		ImageWriter imageWriter = imageWriters.next();

		ArrayList<Animation> animationList = SpriteEditor.sprite.getAnimations();
		int totalFrames = 0;
		int currentTotalFrameIndex = 0;

		// 计算总的帧数
		for(int i = 0; i < animationList.size(); i++)
		{
			Animation animation = (Animation) animationList.get(i);
			File file1 = new File(file, i + "_" + animation.getName());
			if(file1.exists())
			{
				File afile[] = file1.listFiles();
				for(int i1 = 0; i1 < afile.length; i1++)
				{
					File file3 = afile[i1];
					file3.delete();
				}

				file1.delete();
			}
			animation = animation.copy();
			animation.reset();

			for(; !animation.isLastTick(); animation.nextTick(false))
			{
				totalFrames++;
			}
		}

		// 导出动画
		label0: for(int i = 0; i < animationList.size(); i++)
		{
			Animation animation1 = animationList.get(i);
			StringBuffer sb = new StringBuffer();
			if(i < 10)
			{
				sb.append("0");
			}
			sb.append(i);
			sb.append("_");
			sb.append(animation1.getName());
			Animation animation2 = animation1.copy();

			// 建立文件
			File file2 = new File(file, sb.toString());
			file2.mkdir();
			if(animation2 == null || animation2.size() <= 0)
			{
				continue;
			}

			setPosition(0, 0);
			animation2.reset();
			do
			{
				if(stoppExport)
				{
					notifyExportListener(ExporterListener.EVENT_STOPED, 0);
					return;
				}

				// 画动画
				drawAnimationFrame(animation2);
				notifyExportListener(ExporterListener.EVENT_UPDATE, (100 * currentTotalFrameIndex) / totalFrames);

				// 确定文件名
				StringBuffer stringbuffer1 = new StringBuffer();
				int frameIndex = animation2.getIndex();
				if(frameIndex < 10)
				{
					stringbuffer1.append("00");
				}
				if(frameIndex < 100)
				{
					stringbuffer1.append("0");
				}
				stringbuffer1.append(frameIndex);
				stringbuffer1.append(".png");

				// 导出动画图片
				File file4 = new File(file2, stringbuffer1.toString());
				ImageOutputStream iops = ImageIO.createImageOutputStream(file4);
				imageWriter.setOutput(iops);
				imageWriter.write(bufferImage);
				currentTotalFrameIndex++;
				if(animation2.isLastTick())
				{
					continue label0;
				}
				animation2.nextTick(false);
				movePosition(animation2.get().getSpeedX(), animation2.get().getSpeedY());
			}while(true);
		}

		notifyExportListener(ExporterListener.EVENT_ENDED, 100);
	}

	public static final int GIF_DELAY = 50;

	/**
	 * 导出gif
	 * 
	 * @param dir
	 * @throws IOException
	 */
	public static void exportGIF(Sprite sprite, File dir)
	{
		if(sprite == null)
		{	
			return;
		}
		
		if(dir == null)
		{
			dir = new File("./");
		}

		Animation ani = null;
		File outputFile = null;
		FileOutputStream fos = null;

		AnimatedGifEncoder e = new AnimatedGifEncoder();
		try
		{
			ArrayList<Animation> animationList = sprite.getAnimations();
			for(int i = 0 ; i < animationList.size() ; i ++)
//			for(int i = 0; i < 1; i++)
			{
				// 得到要导出的ani
				ani = sprite.getAnimations().get(i);

				// 生成文件
				outputFile = new File(dir, "1_" + i + ".gif");
				System.out.println(outputFile.getAbsolutePath());
				if(outputFile.exists())
				{
					outputFile.delete();
				}
				outputFile.createNewFile();
				fos = new FileOutputStream(outputFile);

				e = new AnimatedGifEncoder();
				e.start(fos); // 开始处理
				e.setQuality(0);
//				e.setTransparent(Color.YELLOW);
				ani.reset();
				for(Gesture gesture : ani.getGestures())
				{
//					e.setSize(gesture.getFrame().getImage().getWidth(), gesture.getFrame().getImage().getHeight());
					e.setDelay(gesture.getLength() * GIF_DELAY);
					e.addFrame(drawFrame(gesture.getFrame()));
				}
				e.finish();
				fos.close();
			}
		}catch(Exception e2)
		{
			e2.printStackTrace();
		}

		// e.setQuality(256);
		// e.setDelay(250); // 设置延迟时间
		// for(int i = 0; i < imgFileName.length; i++)
		// {
		// e.addFrame(ImageIO.read(new FileInputStream(imgFileName[i])));//
		// 加入Frame
		// }
		// e.finish();
		// fos.close();

		//
		// Iterator<ImageWriter> imageWriters =
		// ImageIO.getImageWritersByFormatName("png");
		// ImageWriter imageWriter = imageWriters.next();
		//
		// ArrayList<Animation> animationList =
		// SpriteEditor.sprite.getAnimations();
		// int totalFrames = 0;
		// int currentTotalFrameIndex = 0;
		//
		// // 计算总的帧数
		// for(int i = 0; i < animationList.size(); i++)
		// {
		// Animation animation = (Animation) animationList.get(i);
		// File file1 = new File(file, i + "_" + animation.getName());
		// if(file1.exists())
		// {
		// File afile[] = file1.listFiles();
		// for(int i1 = 0; i1 < afile.length; i1++)
		// {
		// File file3 = afile[i1];
		// file3.delete();
		// }
		//
		// file1.delete();
		// }
		// animation = animation.copy();
		// animation.reset();
		//
		// for(; !animation.isLastTick(); animation.nextTick(false))
		// {
		// totalFrames++;
		// }
		// }
		//
		// // 导出动画
		// label0: for(int i = 0; i < animationList.size(); i++)
		// {
		// Animation animation1 = animationList.get(i);
		// StringBuffer sb = new StringBuffer();
		// if(i < 10)
		// {
		// sb.append("0");
		// }
		// sb.append(i);
		// sb.append("_");
		// sb.append(animation1.getName());
		// Animation animation2 = animation1.copy();
		//
		// // 建立文件
		// File file2 = new File(file, sb.toString());
		// file2.mkdir();
		// if(animation2 == null || animation2.size() <= 0)
		// {
		// continue;
		// }
		//
		// setPosition(0, 0);
		// animation2.reset();
		// do
		// {
		// if(stoppExport)
		// {
		// notifyExportListener(ExporterListener.EVENT_STOPED, 0);
		// return;
		// }
		//
		// // 画动画
		// drawAnimationFrame(animation2);
		// notifyExportListener(ExporterListener.EVENT_UPDATE, (100 *
		// currentTotalFrameIndex) / totalFrames);
		//
		// // 确定文件名
		// StringBuffer stringbuffer1 = new StringBuffer();
		// int frameIndex = animation2.getIndex();
		// if(frameIndex < 10)
		// {
		// stringbuffer1.append("00");
		// }
		// if(frameIndex < 100)
		// {
		// stringbuffer1.append("0");
		// }
		// stringbuffer1.append(frameIndex);
		// stringbuffer1.append(".png");
		//
		// // 导出动画图片
		// File file4 = new File(file2, stringbuffer1.toString());
		// ImageOutputStream iops = ImageIO.createImageOutputStream(file4);
		// imageWriter.setOutput(iops);
		// imageWriter.write(bufferImage);
		// currentTotalFrameIndex++;
		// if(animation2.isLastTick())
		// {
		// continue label0;
		// }
		// animation2.nextTick(false);
		// movePosition(animation2.get().getSpeedX(),
		// animation2.get().getSpeedY());
		// }while(true);
		// }
		//
		// notifyExportListener(ExporterListener.EVENT_ENDED, 100);
	}

	private void drawAnimationFrame(Animation animation)
	{
		bufferImage = new BufferedImage(bufferW, bufferH, 2);
		Graphics g = bufferImage.getGraphics();
		int offsetX = currentX * scale;
		int offsetY = currentY * scale;
		AffineTransform affinetransform = new AffineTransform();
		affinetransform.scale(scale, scale);
		int sx = bufferW / 2;
		int sy = bufferH / 2;
		g.translate(sx, sy);
		SpriteEditor.gesturesBackground.paint((Graphics2D) g, affinetransform, null);
		g.translate(-sx, -sy);
		Frame frame = animation.get().getFrame();
		if(frame != null)
		{
			drawFrame(g, frame, sx, sy, offsetX, offsetY, affinetransform, true);
		}
	}
	
	private static BufferedImage drawFrame(Frame frame)
	{
//		BufferedImage image = new BufferedImage(frame.getImage().getWidth(), frame.getImage().getHeight(), 2);
		BufferedImage image = new BufferedImage(256, 256, 2);
//		BufferedImage image = new BufferedImage(512, 512, 2);
		Graphics g = image.getGraphics();
		int offsetX = 0;
		int offsetY = 0;
		AffineTransform affinetransform = new AffineTransform();
		int sx = image.getWidth()/2;
		int sy = image.getHeight()/2;
		
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, image.getWidth(), image.getHeight());
		drawFrame(g, frame, sx, sy, offsetX, offsetY, affinetransform, true);
		return image;
	}

	private static void drawFrame(Graphics g, Frame frame, int originX, int originY, int offsetX, int offsetY, AffineTransform affinetransform, boolean flag)
	{
		frame.drawFrame(g, originX, originY, offsetX, offsetY, 1, true, false);

		// for(int index = frame.size() - 1; index > -1; index--)
		// {
		// FrameFragment framefragment = frame.get(index);
		// if(SpriteEditor.frameFragmentMasks.isEnabled() &&
		// SpriteEditor.frameFragmentMasks.isHiden(framefragment))
		// {
		// continue;
		// }
		// int sx = originX + framefragment.getX() * scale + offsetX;
		// int sy = originY + framefragment.getY() * scale + offsetY;
		// g.translate(sx, sy);
		// if(flag)
		// {
		// ((Graphics2D) g).drawImage(framefragment.getImage(), affinetransform,
		// null);
		// }else
		// {
		// ((Graphics2D) g).setComposite(AlphaComposite.getInstance(3, 0.5F));
		// ((Graphics2D) g).drawImage(framefragment.getImage(), affinetransform,
		// null);
		// ((Graphics2D) g).setComposite(AlphaComposite.SrcOver);
		// }
		// g.translate(-sx, -sy);
		// }
	}

	private void movePosition(int _spdX, int _spdY)
	{
		currentX += _spdX;
		currentY += _spdY;
	}

	private void setPosition(int i, int j)
	{
		currentX = i;
		currentY = j;
	}

	public void setAnimationExporterListener(ExporterListener exporterlistener)
	{
		exporterListener = exporterlistener;
	}

	public BufferedImage getBufferImage()
	{
		return bufferImage;
	}

	public void stopExport()
	{
		stoppExport = true;
	}

	private int currentX;
	private int currentY;
	private int scale;
	private final int DEFAULT_WIDTH = 300;
	private final int DEFAULT_HEIGHT = 300;
	private int bufferW;
	private int bufferH;
	private BufferedImage bufferImage;
	private ExporterListener exporterListener;
	private boolean stoppExport;
}
