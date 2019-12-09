package cz.ismar.projects.IEdit.io;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * 限定前缀与后缀的filter
 * @author Fay
 * @time 2009-11-18
 *
 */
public class PrefixSuffixFileFilter extends FileFilter
{
	private String prefix = null;
	private String suffix = null;
	
	public PrefixSuffixFileFilter(String prefix, String suffix)
	{
		this.prefix = prefix;
		this.suffix = suffix;
	}

	@Override
	public boolean accept(File f)
	{
		if(f.isDirectory() || 
				((prefix == null || f.getName().startsWith(prefix))
				&& (suffix == null || f.getName().endsWith(suffix))))
		{
			return true;
		}
		return false;
	}

	@Override
	public String getDescription()
	{
		return null;
	}

}
