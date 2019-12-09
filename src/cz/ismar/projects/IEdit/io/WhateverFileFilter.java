package cz.ismar.projects.IEdit.io;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class WhateverFileFilter extends FileFilter
{

    public WhateverFileFilter()
    {
    }

    public boolean accept(File file)
    {
        return true;
    }

    public String getDescription()
    {
        return "filename will be ignored";
    }
}
