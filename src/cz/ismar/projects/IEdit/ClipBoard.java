package cz.ismar.projects.IEdit;

import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

/**
 * 
 * 用于 Copy @ paste 处理
 * @author Lori
 *
 */
public class ClipBoard
{

    public ClipBoard()
    {
        items = new ArrayList();
    }

    public boolean insertItems(JList jlist)
    {
        ListModel listmodel = jlist.getModel();
        ArrayList arraylist = new ArrayList();
        for(int i = 0; i < listmodel.getSize(); i++)
            if(jlist.getSelectionModel().isSelectedIndex(i))
                arraylist.add(listmodel.getElementAt(i));

        if(arraylist.size() > 0)
        {
            items = arraylist;
            return true;
        } else
        {
            return false;
        }
    }

    public List getItems()
    {
        return items;
    }

    ArrayList items;
}
