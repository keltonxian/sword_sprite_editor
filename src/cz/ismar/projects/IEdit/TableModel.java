package cz.ismar.projects.IEdit;

import cz.ismar.projects.IEdit.structure.Fragment;
import cz.ismar.projects.IEdit.structure.Frame;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class TableModel extends AbstractTableModel
{

    public TableModel(List list)
    {
        items = list;
    }

    public String getColumnName(int i)
    {
        return COL_NAMES[i];
    }

    public int getRowCount()
    {
        return items.size();
    }

    public int getColumnCount()
    {
        return COL_NAMES.length;
    }

    public Object getValueAt(int i, int j)
    {
        return items.get(i);
    }

    public boolean isCellEditable(int i, int j)
    {
        return true;
    }

    public void setValueAt(Object obj, int i, int j)
    {
        if(items.get(i) instanceof Fragment)
            ((Fragment)items.get(i)).setName((String)obj);
        else
        if(items.get(i) instanceof Frame)
            ((Frame)items.get(i)).setName((String)obj);
        fireTableCellUpdated(i, j);
    }

    public static final String COL_NAMES[] = {
        "name"
    };
    private List items;

}
