package cz.ismar.projects.IEdit.structure;

import cz.ismar.projects.IEdit.io.property.PropertyIO;
import cz.ismar.projects.IEdit.io.property.PropertyIOable;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.ImageObserver;
import java.util.Vector;

public class Background
    implements PropertyIOable
{

    public Background()
    {
        items = new Vector();
        bgColor = Color.WHITE;
        defaultBgColor = bgColor;
        visible = true;
    }

    public void addBackgroundItem(BackgroundItem backgrounditem)
    {
        items.add(backgrounditem);
    }

    public void removeAll()
    {
        items.removeAllElements();
    }

    public Vector getItems()
    {
        return items;
    }

    public void setVisible(boolean flag)
    {
        visible = flag;
    }

    public void paint(Graphics2D graphics2d, AffineTransform affinetransform, ImageObserver imageobserver)
    {
        if(visible)
        {
            for(int i = 0; i < items.size(); i++)
            {
                BackgroundItem backgrounditem = (BackgroundItem)items.elementAt(i);
                backgrounditem.paint(graphics2d, affinetransform, imageobserver);
            }

        }
    }

    public void storeToProps(PropertyIO propertyio, String s)
    {
        if(bgColor != defaultBgColor)
            propertyio.putInt(s + "-bgColor", bgColor.getRGB());
        for(int i = 0; i < items.size(); i++)
        {
            BackgroundItem backgrounditem = (BackgroundItem)items.elementAt(i);
            backgrounditem.storeToProps(propertyio, s + (i + 1));
        }

    }

    public boolean loadFromProps(PropertyIO propertyio, String s)
    {
        defaultBgColor = bgColor = new Color(propertyio.getInt(s + "-bgColor", bgColor.getRGB()));
        items.removeAllElements();
        int i = 0;
        for(BackgroundItem backgrounditem = new BackgroundItem(); backgrounditem.loadFromProps(propertyio, s + (i + 1)); i++)
        {
            items.addElement(backgrounditem);
            backgrounditem = new BackgroundItem();
        }

        return items.size() != 0;
    }

    public void setBgColor(Color color)
    {
        bgColor = color;
    }

    public Color getBgColor()
    {
        return bgColor;
    }

    private Vector items;
    private Color bgColor;
    private Color defaultBgColor;
    private boolean visible;
}
