package cz.ismar.projects.IEdit.io;


public class OldFileFormatException extends WrongFileFormatException
{

    public OldFileFormatException(String s)
    {
        imagePath = s;
    }

    public OldFileFormatException(String s, String s1)
    {
        super(s);
        imagePath = s1;
    }

    public OldFileFormatException(Throwable throwable, String s)
    {
        super(throwable);
        imagePath = s;
    }

    public OldFileFormatException(String s, Throwable throwable, String s1)
    {
        super(s, throwable);
        imagePath = s1;
    }

    public String getImagePath()
    {
        return imagePath;
    }

    private String imagePath;
}
