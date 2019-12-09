package cz.ismar.projects.IEdit;

import java.util.List;
import javax.swing.DefaultListModel;

public class IconLabelListModel extends DefaultListModel
{

    public IconLabelListModel(List list)
    {
        items = list;
    }

    public int getSize()
    {
        if(items == null || items.size() == 0)
            return 1;
        else
            return items.size();
    }

    public Object getElementAt(int i)
    {
        if(items == null)
            return "[no list]";
        if(items.size() == 0)
            return "[empty list]";
        else
            return items.get(i);
    }

    public void update()
    {
        super.fireIntervalRemoved(this, 0, items.size());
        super.fireIntervalAdded(this, 0, items.size());
        super.fireContentsChanged(this, 0, items.size());
    }

    public void updateAdd()
    {
        super.fireIntervalAdded(this, items.size(), items.size());
    }

    public void updateDelete()
    {
    	//System.out.println("size: "+items.size());
        super.fireIntervalRemoved(this, items.size(), items.size());
        super.fireContentsChanged(this, 0, items.size());
    }

    public void updateChange()
    {
        super.fireContentsChanged(this, 0, items.size());
    }

    private List items;
}
