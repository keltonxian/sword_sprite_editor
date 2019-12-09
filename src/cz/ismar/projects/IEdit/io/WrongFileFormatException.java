package cz.ismar.projects.IEdit.io;


public class WrongFileFormatException extends Exception
{

    public WrongFileFormatException()
    {
    }

    public WrongFileFormatException(String s)
    {
        super(s);
    }

    public WrongFileFormatException(Throwable throwable)
    {
        super(throwable);
    }

    public WrongFileFormatException(String s, Throwable throwable)
    {
        super(s, throwable);
    }
}
