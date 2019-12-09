package cz.ismar.projects.IEdit.structure;

import com.sixlegs.png.PngImage;
import cz.ismar.projects.IEdit.io.property.PropertyIO;
import cz.ismar.projects.IEdit.io.property.PropertyIOable;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

public class BackgroundItem
    implements PropertyIOable
{

    public BackgroundItem()
    {
        visible = true;
    }

    public void setImagePath(String s)
        throws IOException
    {
        imagePath = s;
        image = (new PngImage()).read(new File(s));
    }

    public void setImage(Image image1)
    {
        image = image1;
    }

    public Image getImage()
    {
        return image;
    }

    public boolean isVisible()
    {
        return visible;
    }

    public void setVisible(boolean flag)
    {
        visible = flag;
    }

    public int getPosX()
    {
        return posX;
    }

    public int getPosY()
    {
        return posY;
    }

    public void setPosX(int i)
    {
        posX = i;
    }

    public void setPosY(int i)
    {
        posY = i;
    }

    public void paint(Graphics2D graphics2d, AffineTransform affinetransform, ImageObserver imageobserver)
    {
        if(visible)
        {
            AffineTransform affinetransform1 = new AffineTransform(affinetransform);
            affinetransform1.translate(posX, posY);
            graphics2d.drawImage(image, affinetransform1, imageobserver);
        }
    }

    public void storeToProps(PropertyIO propertyio, String s)
    {
        propertyio.putBoolean(s + "-visible", visible);
        propertyio.putInt(s + "-posX", posX);
        propertyio.putInt(s + "-posY", posY);
        propertyio.putRelativePath(s + "-image", imagePath);
    }

    public boolean loadFromProps(PropertyIO propertyio, String s)
    {
        try
        {
            visible = propertyio.getBoolean(s + "-visible");
            posX = propertyio.getInt(s + "-posX");
            posY = propertyio.getInt(s + "-posY");
            setImagePath(propertyio.getRelativePath(s + "-image", imagePath));
        }
        catch(Exception exception)
        {
            return false;
        }
        return true;
    }

    private String imagePath;
    private Image image;
    private boolean visible;
    private int posX;
    private int posY;
}
