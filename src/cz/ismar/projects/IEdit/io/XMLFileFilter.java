package cz.ismar.projects.IEdit.io;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * xml文件过滤器，
 * 
 * 用于对话框打开文件时，筛选出 匹配的文件
 * @author Lori
 *
 */
public class XMLFileFilter extends FileFilter
{

    public XMLFileFilter()
    {
    }

    public boolean accept(File file)
    {
    	//如果是文件夹，或者xml文件，返回true
        return file.isDirectory() || file.getAbsolutePath().endsWith(".xml");
    }

    public String getDescription()
    {
        return "project metadata *.xml";
    }
}
