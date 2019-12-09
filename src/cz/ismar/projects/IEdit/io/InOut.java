package cz.ismar.projects.IEdit.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.io.property.PropertyIO;
import cz.ismar.projects.IEdit.structure.Animation;
import cz.ismar.projects.IEdit.structure.FragmentHolder;
import cz.ismar.projects.IEdit.structure.Gesture;
import cz.ismar.projects.IEdit.structure.Sprite;

/**
 * 2007-9-4 daben #see daben - in method setAttribute(); - Byte.decode(s1) -->
 * Integer.decode(s1); - [inore] if(i < 0 || i > 255) throw new
 * NumberFormatException("Value out of range. Value:\"" + i);
 * 
 * 
 * 
 * 
 * 
 * */
public class InOut
{

	public InOut()
	{
	}

	/**
	 * 将切片保存到文件中
	 * @param fragmentholder
	 * @param file
	 * @throws Exception
	 */
	public static void outFragmentFileXML(FragmentHolder fragmentholder, File file) throws Exception
	{
		// 没用的
		fragmentholder.defragment();
		
		DocumentBuilderFactory documentbuilderfactory = DocumentBuilderFactory.newInstance();
		documentbuilderfactory.setValidating(false);
		documentbuilderfactory.setNamespaceAware(true);
		DocumentBuilder documentbuilder = documentbuilderfactory.newDocumentBuilder();
		Document document = documentbuilder.newDocument();
		
		// fragment解析成xml
		Element element = fragmentholder.output(document);
		document.appendChild(element);
		
		// 写入文件
		DOMSource domsource = new DOMSource(document);
		StreamResult streamresult = new StreamResult(new FileOutputStream(file));
		TransformerFactory transformerfactory = TransformerFactory.newInstance();
		Transformer transformer = transformerfactory.newTransformer();
		transformer.setOutputProperty("indent", "yes");
		transformer.transform(domsource, streamresult);
	}

	/**
	 * 生成xml操作
	 * 
	 * @param sprite
	 *            xml工程信息
	 * 
	 * @param file
	 *            保存xml信息的文件
	 * 
	 * @throws Exception
	 */
	public static void outXML(Sprite sprite, File file) throws Exception
	{
		sprite.tidyFragmentHoldersList();

		sprite.fragmentHoldersList.defragment();
		sprite.fragmentHolder.defragment();
		sprite.frameHolder.defragment();
		DocumentBuilderFactory documentbuilderfactory = DocumentBuilderFactory.newInstance();
		documentbuilderfactory.setValidating(false);
		documentbuilderfactory.setNamespaceAware(true);
		DocumentBuilder documentbuilder = documentbuilderfactory.newDocumentBuilder();
		Document document = documentbuilder.newDocument();

		sprite.setExportFile(file);

		// 输出sprite
		Element element = sprite.output(document);

		document.appendChild(element);
		DOMSource domsource = new DOMSource(document);
		
		// 写入文件
		StreamResult streamresult = new StreamResult(new FileOutputStream(file));
		TransformerFactory transformerfactory = TransformerFactory.newInstance();
		Transformer transformer = transformerfactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.transform(domsource, streamresult);
	}

	public static void inFragmentXML(FragmentHolder fragmentholder, File file)
	{
		long l = System.currentTimeMillis();
		try
		{
			DocumentBuilderFactory documentbuilderfactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentbuilder = documentbuilderfactory.newDocumentBuilder();
			Document document = documentbuilder.parse(file);
			Element element = document.getDocumentElement();

			fragmentholder.input(element);
		}catch(Exception exception)
		{
			// exception.printStackTrace();
			System.out.println("inFragmentXML Error");
		}
	}

	/**
	 * 打开xml操作
	 * 
	 * @param fileName
	 * @return
	 * @throws WrongFileFormatException
	 */
	public static Sprite inXML(String fileName) throws WrongFileFormatException
	{
//		long l = System.currentTimeMillis();
		
		Sprite curentlyReadedSprite = null;

		try
		{
			File file = new File(fileName);
//			File file1 = file.getParentFile();

			DocumentBuilderFactory documentbuilderfactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentbuilder = documentbuilderfactory.newDocumentBuilder();
			Document document = documentbuilder.parse(file);
			Element element = document.getDocumentElement();
			if(element.getNodeName().equals("project") && !compatibilityInput)
			{
				throw new OldFileFormatException(element.getAttribute("image-path"));
			}

			if(!element.getNodeName().equals("sprite"))
			{
				throw new WrongFileFormatException();
			}

			curentlyReadedSprite = new Sprite(fileName);
			curentlyReadedSprite.setExportFile(file);
			SpriteEditor.xmlSavePath = fileName;

			// 加载sprite
			curentlyReadedSprite.input(element);
		}catch(WrongFileFormatException wrongfileformatexception)
		{
			SpriteEditor.xmlSavePath = null;
			throw wrongfileformatexception;
		}catch(Exception exception)
		{
			exception.printStackTrace();
			JOptionPane.showMessageDialog(SpriteEditor.mainFrame, "Open file Error, XML may be damaged", "Warning", JOptionPane.WARNING_MESSAGE);
			
			return null;
		}

		// 加载相应的config文件
		try
		{
			SpriteEditor.spriteConf.loadFromFile(new File(SpriteEditor.xmlSavePath + PropertyIO.defExpandPropertyName));
		}catch(IOException ioexception)
		{
			System.err.println("Error load config file : " + ioexception.getLocalizedMessage());
		}
		return curentlyReadedSprite;
	}

	public static boolean isByte(double d)
	{
		return d >= -128D && d <= 127D;
	}

	public static boolean isUnsignedByte(double d)
	{
		return d >= 0.0D && d < 256D;
	}

	public static void exportToAndrleXML(Sprite project, File file)
	{
		try
		{
			OutputStreamWriter outputstreamwriter = new OutputStreamWriter(new FileOutputStream(file));
			outputstreamwriter.write("<?xml version=\"1.0\"?>\n");
			outputstreamwriter.write("<!DOCTYPE projectArr SYSTEM \"spriteAnim.dtd\" >\n");
			outputstreamwriter.write("<projectArr>\n");
			for(Iterator iterator = project.getAnimations().iterator(); iterator.hasNext(); outputstreamwriter.write("    />\n"))
			{
				Animation animation = (Animation) iterator.next();
				outputstreamwriter.write("    <!-- " + animation.getName() + " -->\n");
				outputstreamwriter.write("    <Gesture sequence-waitAfter-x-y-variant_shortArr-n:m.A:m.A:n_r3=\"\n");
				for(Iterator iterator1 = animation.getGestures().iterator(); iterator1.hasNext();)
				{
					Gesture gesture = (Gesture) iterator1.next();
					if(iterator1.hasNext())
						outputstreamwriter.write("        " + gesture.getLength() + "," + gesture.getSpeedX() + "," + gesture.getSpeedY() + "," + (gesture.getFrame().getID() - 1) + ",\n");
					else
						outputstreamwriter.write("        " + gesture.getLength() + "," + gesture.getSpeedX() + "," + gesture.getSpeedY() + "," + (gesture.getFrame().getID() - 1) + "\"\n");
				}

			}

			outputstreamwriter.write("</projectArr>\n");
			outputstreamwriter.flush();
			outputstreamwriter.close();
		}catch(Exception exception)
		{
			// exception.printStackTrace();
			System.out.println("exportToAndrleXML Error");
		}
	}

	public static void main(String args[])
	{
		String s = args[0];
		try
		{
			DocumentBuilderFactory documentbuilderfactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentbuilder = documentbuilderfactory.newDocumentBuilder();
			File file = new File(s);
			if(verbose)
				System.out.println("InOut.main file[" + file.getAbsolutePath() + "] exists:" + file.exists());
			Document document = documentbuilder.parse(file);
			Element element = document.getDocumentElement();
			NodeList nodelist = element.getElementsByTagName("Level");
			if(nodelist != null && nodelist.getLength() > 0)
			{
				for(int i = 0; i < nodelist.getLength(); i++)
				{
					Element element1 = (Element) nodelist.item(i);
					File file1 = new File("matrix" + i + ".csv");
					if(!file1.exists())
						file1.createNewFile();
					FileOutputStream fileoutputstream = new FileOutputStream(file1);
					OutputStreamWriter outputstreamwriter = new OutputStreamWriter(fileoutputstream);
					if(verbose)
						System.out.println("InOut.main - - - - - - - - - - - - - - - - - " + element1.getAttribute("Id"));

					int j = Integer.parseInt(element1.getAttribute("Width"));
					NodeList nodelist1 = element1.getElementsByTagName("L");
					if(nodelist1 != null && nodelist1.getLength() > 0)
					{
						for(int k = 0; k < nodelist1.getLength(); k++)
						{
							String s1 = "";
							Element element2 = (Element) nodelist1.item(k);
							String s2 = "" + element2;
							s2 = s2.substring(s2.indexOf("<") + 3);
							s2 = s2.substring(0, s2.indexOf("<"));
							char ac[] = s2.toCharArray();
							boolean flag = false;
							boolean flag1 = false;
							for(int l = 0; l < ac.length; l++)
							{
								switch(ac[l])
								{
								default:
									break;

								case 32: // ' '
									s1 = s1 + "1";
									break;

								case 35: // '#'
									if(!flag1)
									{
										flag1 = true;
										flag = !flag;
									}
									s1 = s1 + "3";
									break;

								case 36: // '$'
									s1 = s1 + "4";
									break;

								case 46: // '.'
									s1 = s1 + "5";
									break;

								case 64: // '@'
									s1 = s1 + "6";
									break;
								}
								s1 = s1 + ";";
							}

							for(int i1 = ac.length; i1 < j; i1++)
								s1 = s1 + "1;";

							if(verbose)
								System.out.println("InOut.main: " + s1);
							outputstreamwriter.write(s1 + "\n");
						}

						outputstreamwriter.close();
						fileoutputstream.close();
					}
				}

			}else if(verbose)
				System.out.println("TileCountryHolder.input zadny tileCountry k dispozici");
		}catch(Exception exception)
		{
			exception.printStackTrace();
		}
	}

	public static void convertOldFormat(File file, File file1, int i)
	{
		FragmentHolder fragmentholder = new FragmentHolder();
		File file2 = file1.getParentFile();
		Vector vector = new Vector();
		String s = file1.getName();
		s = s.substring(0, s.length() - 4);
		vector.add(s);
		vector.add(s + "R");
		for(int j = 1; (new File(file2, s + "__" + j + ".png")).exists(); j++)
		{
			vector.add(s + "__" + j);
			vector.add(s + "R__" + j);
		}

		for(int k = 0; k < vector.size(); k++)
		{
			String s1 = (String) vector.elementAt(k);
			File file4 = new File(file2, s1 + ".png");
			if(!file4.exists())
				continue;
			File file5 = new File(file2, "fr_" + s1 + ".png");
			FileUtils.copyFile(file4, file5);
			if((i & 1) != 0)
				file4.delete();
			if(k == 0)
			{
				file1 = file5;
				fragmentholder.setImageFile(file1);
			}
		}

		File file3 = new File(file1.getParentFile(), fragmentholder.getName() + ".xml");
		ImageIcon imageicon = fragmentholder.getIcon();
		if(SpriteEditor.fragmentPanel != null)
			SpriteEditor.fragmentPanel.fragmentEditorComponent.setImageSize(imageicon.getIconWidth(), imageicon.getIconHeight());
		inFragmentXML(fragmentholder, file);
		try
		{
			outFragmentFileXML(fragmentholder, file3);
		}catch(Exception exception)
		{
			// exception.printStackTrace();
			System.out.println("convertOldFormat Error");
		}
		SpriteEditor.sprite.fragmentHolder = fragmentholder;
		compatibilityInput = true;
		Sprite project = null;
		try
		{
			project = inXML(file.getAbsolutePath());
			if(SpriteEditor.useMasksMenuItem != null && SpriteEditor.frameFragmentMasks.isEnabled() != SpriteEditor.useMasksMenuItem.isSelected())
				SpriteEditor.useMasksMenuItem.doClick(0);
		}catch(WrongFileFormatException wrongfileformatexception)
		{
			// wrongfileformatexception.printStackTrace();
			System.out.println("wrong file format");
		}
		if(project != null)
			SpriteEditor.sprite = project;
		File file6;
		if((i & 2) != 0)
		{
			file.delete();
			file6 = file;
		}else
		{
			file6 = new File(file.getParentFile(), "new_" + file.getName());
		}
		compatibilityInput = false;
		try
		{
			outXML(SpriteEditor.sprite, file6);
		}catch(Exception exception1)
		{
			// exception1.printStackTrace();
			System.out.println("convertOldFormat Error2");
		}
		if((i & 1) != 0)
			file.delete();
	}

	/**
	 * 
	 * xml数据写入前的数据效验
	 * 
	 * 主要是进行 byte, int, long 的越界检查
	 * 
	 * @param element
	 * @param s
	 * @param s1
	 * @throws StoringNumberException
	 */
	public static void setAttribute(Element element, String s, String s1) throws StoringNumberException
	{
		try
		{
			if(s.indexOf("_byte_") > 0)
			{
				// daben:modify 以下1行代码限制切片宽高不能大于等于128，因为Byte.decode只接受-127~128的值
				// Byte.decode(s1);
				// modify:
				Integer.decode(s1);
				// ~daben
			}else if(s.indexOf("_uByte_") > 0)
			{
				int i = Integer.decode(s1).intValue();
				// daben:modify 以下2行代码限制切片左上角坐标不能大于255，改为不能大于图象的宽高
				// if(i < 0 || i > 255)
				// throw new
				// NumberFormatException("Value out of range. Value:\"" + i);
				// modify:
//				if(s.indexOf("x_uByte_") >= 0 && i > SpriteEditor.sprite.fragmentHolder.getImage().getWidth(null))
//					throw new NumberFormatException("Value out of range. Value:\"" + i);
//				if(s.indexOf("y_uByte_") >= 0 && i > SpriteEditor.sprite.fragmentHolder.getImage().getHeight(null))
//					throw new NumberFormatException("Value out of range. Value:\"" + i);
				// ~daben
			}else if(s.indexOf("_short_") > 0)
			{
				Short.decode(s1);
			}else if(s.indexOf("_int_") > 0)
			{
				Integer.decode(s1);
			}
		}catch(NumberFormatException numberformatexception)
		{
			String s2;
			if(s.startsWith("x_"))
				s2 = "posX";
			else if(s.startsWith("y_"))
				s2 = "posY";
			else if(s.startsWith("w_"))
				s2 = "width";
			else if(s.startsWith("h_"))
				s2 = "height";
			else
				s2 = s;
			throw new StoringNumberException(s2 + "='" + s1 + "' out of range.");
		}
		element.setAttribute(s, s1);
	}

	/**
	 * 检查element中的att属性, 转成int, 如果转化失败 -> return deaflutValue
	 * 
	 * @param element
	 * @param att
	 * @param defaultValue
	 * @return
	 */
	public static int parseInt(Element element, String att, int defaultValue)
	{
		// System.out.println("无此属性 -> " + att + ", 设为默认值:" + defaultValue);
		if(element == null || att == null || element.hasAttribute(att) == false)
		{
			return defaultValue;
		}

		String value = element.getAttribute(att);
		try
		{
			return Integer.parseInt(value);
		}catch(Exception e)
		{
			return defaultValue;
		}
	}

	/**
	 * 检查element中的att属性, 转成int, 如果转化失败 -> return deaflutValue
	 * 
	 * @param element
	 * @param att
	 * @param defaultValue
	 * @return
	 */
	public static short parseShort(Element element, String att, short defaultValue)
	{
		// System.out.println("无此属性 -> " + att + ", 设为默认值:" + defaultValue);
		if(element == null || att == null || element.hasAttribute(att) == false)
		{
			return defaultValue;
		}

		String value = element.getAttribute(att);
		try
		{
			return Short.parseShort(value);
		}catch(Exception e)
		{
			return defaultValue;
		}
	}

	/**
	 * 检查element中的att属性, 转成int, 如果转化失败 -> return deaflutValue
	 * 
	 * @param element
	 * @param att
	 * @param defaultValue
	 * @return
	 */
	public static byte parseByte(Element element, String att, byte defaultValue)
	{
		// System.out.println("无此属性 -> " + att + ", 设为默认值:" + defaultValue);
		if(element == null || att == null || element.hasAttribute(att) == false)
		{
			return defaultValue;
		}

		String value = element.getAttribute(att);
		try
		{
			return Byte.parseByte(value);
		}catch(Exception e)
		{
			return defaultValue;
		}
	}

	public static String parseString(Element element, String att, String defaultValue)
	{
		// System.out.println("无此属性 -> " + att + ", 设为默认值:" + defaultValue);
		if(element == null || att == null || element.hasAttribute(att) == false)
		{
			return defaultValue;
		}

		String value = element.getAttribute(att);
		return value;
	}

	public static boolean parseBoolean(Element element, String att, boolean defaultValue)
	{
		// System.out.println("无此属性 -> " + att + ", 设为默认值:" + defaultValue);
		if(element == null || att == null || element.hasAttribute(att) == false)
		{
			return defaultValue;
		}

		String value = element.getAttribute(att);
		try
		{
			return Boolean.parseBoolean(value);
		}catch(Exception e)
		{
			e.printStackTrace();
			return defaultValue;
		}
	}

	public static boolean verbose = false;
	public static boolean compatibilityInput = false;
//	public static Sprite curentlyReadedSprite;
	public static final int CONVERSION_TYPE_NO_DELETE = 0;
	public static final int CONVERSION_TYPE_DELETE_OLD_FILES = 1;
	public static final int CONVERSION_TYPE_RENAME_XML = 2;

}
