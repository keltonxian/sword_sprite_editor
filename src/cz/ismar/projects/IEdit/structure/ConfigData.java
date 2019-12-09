package cz.ismar.projects.IEdit.structure;

import java.io.File;
import java.util.Properties;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import cz.ismar.projects.IEdit.SpriteEditor;

public class ConfigData
{

	public static Properties prop = new Properties();

	public final static String configName = "editor.config";

	public final static String prexName = "Type_";

	public static int aniType;

	public static void setAniType(int _type)
	{
		aniType = _type;
		if(aniType >= aniTypeList.length)
			aniType = 0;
	}

	public static int getAniType()
	{
		return aniType;
	}

	public static String[] aniTypeList = { "人物动作", };
	public static String[][] aniMessagesList = { { "站立", "攻击" }, };

	public static String getAniMessage(int aniIndex)
	{
		try
		{
			return aniMessagesList[getAniType()][aniIndex];
		}catch(Exception ex)
		{

			return "-Bug-";
		}
	}

	public static String[] getAniNames()
	{
		try
		{
			return aniMessagesList[getAniType()];
		}catch(Exception ex)
		{
			return new String[] { "-未定义-" };
		}
	}

	public static void init()
	{

		String filename = System.getProperty("user.dir") + "/res/" + "/" + configName;
		inEventFactoryXML(filename);
	}

	/**
	 * 读入事件工厂配置
	 * 
	 * @param s
	 * @return
	 */
	public static void inEventFactoryXML(String s)
	{

		long l = System.currentTimeMillis();
		try
		{
			File file = new File(s);
			File file1 = file.getParentFile();
			DocumentBuilderFactory documentbuilderfactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentbuilder = documentbuilderfactory.newDocumentBuilder();
			Document document = documentbuilder.parse(file);
			Element element = document.getDocumentElement();
			input(element);
		}catch(Exception exception)
		{

			// SpriteEditor.showError("错误", "res.editor.config 错误:" +
			// exception);
		}
	}

	public static void input(Element element) throws Exception
	{

		NodeList nodelist = element.getElementsByTagName("anis");

		int nodeLength = nodelist.getLength();
		if(nodeLength <= 0)
			return;

		aniTypeList = new String[nodeLength];

		aniMessagesList = new String[nodeLength][];

		for(int i = 0; i < nodeLength; i++)
		{
			Element element1 = (Element) nodelist.item(i);
			String name = element1.getAttribute("name");

			aniTypeList[i] = name;

			NodeList nodelist3 = element1.getElementsByTagName("ani");

			if(nodelist3.getLength() <= 0)
			{
				aniMessagesList[i] = new String[] { "未定义", };
				continue;
			}
			aniMessagesList[i] = new String[nodelist3.getLength()];
			for(int j = 0; j < nodelist3.getLength(); j++)
			{
				Element element4 = (Element) nodelist3.item(j);
				String msg = element4.getAttribute("msg");

				aniMessagesList[i][j] = msg;
			}
		}

//		System.out.println("# ==========动画种类====================");
//		for(int i = 0; i < aniTypeList.length; i++)
//		{
//			System.out.println("" + aniTypeList[i]);
//		}
//		System.out.println("");
//		for(int i = 0; i < aniMessagesList.length; i++)
//		{
//
//			System.out.println("#######");
//			for(int j = 0; j < aniMessagesList[i].length; j++)
//			{
//				System.out.println("" + aniMessagesList[i][j]);
//
//			}
//		}

	}
}
