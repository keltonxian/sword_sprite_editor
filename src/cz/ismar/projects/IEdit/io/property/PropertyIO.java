package cz.ismar.projects.IEdit.io.property;

import cz.ismar.projects.IEdit.io.FileUtils;
import java.io.*;
import java.util.*;


public class PropertyIO
{

	//属性文件的默认扩展名
	public final static String defExpandPropertyName = ".properties";
    public PropertyIO()
    {
        propertyIOables = new Hashtable();
    }

    public void addPropertyIOable(String s, PropertyIOable propertyioable)
    {
        propertyIOables.put(s, propertyioable);
    }

    public void storeToFile(File file)
        throws IOException
    {
        propertyFile = file;
        props = new Properties();
        String s;
        PropertyIOable propertyioable;
        for(Enumeration enumeration = propertyIOables.keys(); enumeration.hasMoreElements(); propertyioable.storeToProps(this, s))
        {
            s = (String)enumeration.nextElement();
            propertyioable = (PropertyIOable)propertyIOables.get(s);
        }

        if(props.size() > 0){
        	props.store(new FileOutputStream(file), comments);
        }
        props = null;
    }

    public boolean loadFromFile(File file)
        throws IOException
    {
        propertyFile = file;
        boolean flag = false;
        props = new Properties();
        props.load(new FileInputStream(file));
        for(Enumeration enumeration = propertyIOables.keys(); enumeration.hasMoreElements();)
        {
            String s = (String)enumeration.nextElement();
            PropertyIOable propertyioable = (PropertyIOable)propertyIOables.get(s);
            flag |= propertyioable.loadFromProps(this, s);
        }

        props = null;
        return flag;
    }

    public File getPropertyFile()
    {
        return propertyFile;
    }

    public void setComments(String s)
    {
        comments = s;
    }

    public boolean isSet(String s)
    {
        return props.getProperty(s) != null;
    }

    public int getInt(String s, int i)
    {
        String s1 = props.getProperty(s);
        if(s1 == null)
            return i;
        else
            return Integer.decode(s1).intValue();
    }

    public String getString(String s, String s1)
    {
        String s2 = props.getProperty(s);
        if(s2 == null)
            return s1;
        else
            return s2;
    }

    public boolean getBoolean(String s, boolean flag)
    {
        String s1 = props.getProperty(s);
        if(s1 == null)
            return flag;
        else
            return Boolean.valueOf(s1).booleanValue();
    }

    public String getRelativePath(String s, String s1)
    {
        String s2 = props.getProperty(s);
        if(s2 == null)
            return s1;
        else
            return propertyFile.getParent() + File.separator + s2;
    }

    public int getInt(String s)
        throws PropertValueNotDefinedException
    {
        String s1 = props.getProperty(s);
        if(s1 == null)
            throw new PropertValueNotDefinedException("property value not defined for: " + s);
        else
            return Integer.decode(s1).intValue();
    }

    public String getString(String s)
        throws PropertValueNotDefinedException
    {
        String s1 = props.getProperty(s);
        if(s1 == null)
            throw new PropertValueNotDefinedException("property value not defined for: " + s);
        else
            return s1;
    }

    public boolean getBoolean(String s)
        throws PropertValueNotDefinedException
    {
        String s1 = props.getProperty(s);
        if(s1 == null)
            throw new PropertValueNotDefinedException("property value not defined for: " + s);
        else
            return Boolean.valueOf(s1).booleanValue();
    }

    public String getRelativePath(String s)
        throws PropertValueNotDefinedException
    {
        String s1 = props.getProperty(s);
        if(s1 == null)
            throw new PropertValueNotDefinedException("property value not defined for: " + s);
        else
            return propertyFile.getParent() + File.separator + s1;
    }

    public void putInt(String s, int i)
    {
        props.setProperty(s, Integer.toString(i));
    }

    public void putString(String s, String s1)
    {
        props.setProperty(s, s1);
    }

    public void putBoolean(String s, boolean flag)
    {
        props.setProperty(s, Boolean.toString(flag));
    }

    public void putRelativePath(String s, String s1)
    {
        String s2 = FileUtils.getRelativePath(propertyFile, new File(s1));
        props.setProperty(s, s2);
    }

    private Hashtable propertyIOables;
    private File propertyFile;
    
    //属性文件头说明
    private String comments;
    public Properties props;
}
