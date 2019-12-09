package cz.ismar.projects.IEdit.io;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class PNGFileFilter extends FileFilter
{

    public PNGFileFilter()
    {
    }

    public boolean accept(File file)
    {
        return file.isDirectory() || file.getAbsolutePath().endsWith(".png");
    }

    public String getDescription()
    {
        return "images *.png";
    }
}
