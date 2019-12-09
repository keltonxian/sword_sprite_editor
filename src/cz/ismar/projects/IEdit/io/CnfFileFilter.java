package cz.ismar.projects.IEdit.io;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import cz.ismar.projects.IEdit.io.property.PropertyIO;

public class CnfFileFilter extends FileFilter
{

    public CnfFileFilter()
    {
    }

    public boolean accept(File file)
    {
        return file.isDirectory() || file.getAbsolutePath().endsWith(PropertyIO.defExpandPropertyName);
    }

    public String getDescription()
    {
        return "configuration *.properties";
    }
}
