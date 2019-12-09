package cz.ismar.projects.IEdit.ui;

import cz.ismar.projects.IEdit.structure.Background;
import cz.ismar.projects.IEdit.structure.BackgroundItem;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

class BackgroundTableModel
    implements TableModel
{

    public void addRow(BackgroundItem backgrounditem, int i)
    {
        if(i >= 0)
            background.getItems().insertElementAt(backgrounditem, i);
        else
            background.getItems().add(backgrounditem);
        updateTableModelListeners();
    }

    public boolean removeRow(int i)
    {
        if(i >= 0)
        {
            background.getItems().remove(i);
            updateTableModelListeners();
            return true;
        } else
        {
            return false;
        }
    }

    public boolean moveUp(int i)
    {
        if(i > 0 && getRowCount() > 1)
        {
            Object obj = background.getItems().remove(i);
            background.getItems().insertElementAt(obj, --i);
            updateTableModelListeners();
            return true;
        } else
        {
            return false;
        }
    }

    public boolean moveDown(int i)
    {
        int j = getRowCount();
        if(i < j - 1 && j > 1)
        {
            Object obj = background.getItems().remove(i);
            background.getItems().insertElementAt(obj, ++i);
            updateTableModelListeners();
            return true;
        } else
        {
            return false;
        }
    }

    public BackgroundTableModel(Background background1)
    {
        tableModelListeners = new Vector();
        background = background1;
    }

    public int getRowCount()
    {
        return background.getItems().size();
    }

    public int getColumnCount()
    {
        return 4;
    }

    public String getColumnName(int i)
    {
        switch(i)
        {
        case 0: // '\0'
            return "visible";

        case 1: // '\001'
            return "posX";

        case 2: // '\002'
            return "posY";

        case 3: // '\003'
            return "image";
        }
        return null;
    }

    public Class getColumnClass(int i)
    {
        switch(i)
        {
        case 0: // '\0'
            return java.lang.Boolean.class;

        case 1: // '\001'
            return java.lang.Integer.class;

        case 2: // '\002'
            return java.lang.Integer.class;

        case 3: // '\003'
            return javax.swing.ImageIcon.class;
        }
        return null;
    }

    public boolean isCellEditable(int i, int j)
    {
        return i < 3;
    }

    public Object getValueAt(int i, int j)
    {
        BackgroundItem backgrounditem = (BackgroundItem)background.getItems().elementAt(i);
        switch(j)
        {
        case 0: // '\0'
            return backgrounditem.isVisible() ? Boolean.TRUE : Boolean.FALSE;

        case 1: // '\001'
            return new Integer(backgrounditem.getPosX());

        case 2: // '\002'
            return new Integer(backgrounditem.getPosY());

        case 3: // '\003'
            return new ImageIcon(backgrounditem.getImage());
        }
        return null;
    }

    public void setValueAt(Object obj, int i, int j)
    {
        BackgroundItem backgrounditem = (BackgroundItem)background.getItems().elementAt(i);
        switch(j)
        {
        case 0: // '\0'
            backgrounditem.setVisible(((Boolean)obj).booleanValue());
            break;

        case 1: // '\001'
            backgrounditem.setPosX(((Integer)obj).intValue());
            break;

        case 2: // '\002'
            backgrounditem.setPosY(((Integer)obj).intValue());
            break;
        }
        updateTableModelListeners();
    }

    private void updateTableModelListeners()
    {
        for(int i = 0; i < tableModelListeners.size(); i++)
        {
            TableModelListener tablemodellistener = (TableModelListener)tableModelListeners.elementAt(i);
            tablemodellistener.tableChanged(new TableModelEvent(this));
        }

    }

    public void addTableModelListener(TableModelListener tablemodellistener)
    {
        tableModelListeners.add(tablemodellistener);
    }

    public void removeTableModelListener(TableModelListener tablemodellistener)
    {
        tableModelListeners.remove(tablemodellistener);
    }

    Vector tableModelListeners;
    private Background background;
}
