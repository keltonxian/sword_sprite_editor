package cz.ismar.projects.IEdit.io;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class BinFileFilter extends FileFilter
{

    public BinFileFilter()
    {
    }

    public boolean accept(File file)
    {
        return file.isDirectory() || file.getAbsolutePath().endsWith(".bin");
    }

    public String getDescription()
    {
        return "binar data *.bin";
    }
}
