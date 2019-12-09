package cz.ismar.projects.IEdit.io;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtils
{

    public FileUtils()
    {
    }

    private static List getPathList(File file)
    {
        ArrayList arraylist = new ArrayList();
        if(file.exists())
        {
            String s = file.getAbsolutePath();
            for(File file1 = new File(s); file1 != null; file1 = file1.getParentFile())
                arraylist.add(file1.getName());

        }
        return arraylist;
    }

    private static String matchPathLists(List list, List list1)
    {
        String s = "";
        int i = list.size() - 1;
        int j;
        for(j = list1.size() - 1; i >= 0 && j >= 0 && list.get(i).equals(list1.get(j)); j--)
            i--;

        for(; i >= 0; i--)
            s = s + ".." + File.separator;

        for(; j >= 1; j--)
            s = s + list1.get(j) + File.separator;

        s = s + list1.get(j);
        return s;
    }

    public static String getRelativePath(File file, File file1)
    {
        if(verbose)
            System.out.println("FileUtils.getRelativePath home: " + file);
        if(verbose)
            System.out.println("FileUtils.getRelativePath file: " + file1);
        if(!file.isDirectory())
        {
            file = file.getParentFile();
            if(verbose)
                System.out.println("FileUtils.getRelativePath home is file, so home is parent: " + file);
        }
        String s = matchPathLists(getPathList(file), getPathList(file1));
        if(verbose)
            System.out.println("FileUtils.getRelativePath relativ is: " + s);
        return s;
    }

    public static void copyFile(File file, File file1)
    {
        FileInputStream fileinputstream = null;
        FileOutputStream fileoutputstream = null;
        try
        {
            fileinputstream = new FileInputStream(file);
            fileoutputstream = new FileOutputStream(file1);
            byte abyte0[] = new byte[1000];
            do
            {
                int i = fileinputstream.read(abyte0);
                if(i == -1)
                    break;
                fileoutputstream.write(abyte0, 0, i);
            } while(true);
        }
        catch(IOException ioexception)
        {
            System.err.println(ioexception.toString());
        }
        finally
        {
            if(fileinputstream != null)
                try
                {
                    fileinputstream.close();
                }
                catch(IOException ioexception1) { }
            if(fileoutputstream != null)
                try
                {
                    fileoutputstream.close();
                }
                catch(IOException ioexception2) { }
        }
    }

    public static boolean verbose = false;

}
