package cz.ismar.projects.IEdit.structure;

import cz.ismar.projects.IEdit.io.StoringException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface InOutAble
{

    public abstract Element output(Document document)
        throws StoringException;

    public abstract void input(Element element);
}
