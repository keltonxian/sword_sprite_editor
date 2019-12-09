package cz.ismar.projects.IEdit.ui;

import cz.ismar.projects.IEdit.SpriteEditor;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;

public class DetailComponent extends JComponent
{

    public DetailComponent()
    {
    }

    protected void paintComponent(Graphics g)
    {
        if(isOpaque())
        {
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        AffineTransform affinetransform = new AffineTransform();
        affinetransform.scale(SpriteEditor.scale, SpriteEditor.scale);
        int i = getWidth() / 2;
        int j = getHeight() / 2;
        boolean flag = false;
        boolean flag1 = false;
        if(img != null)
        {
            int k = i - (img.getWidth() / 2) * SpriteEditor.scale;
            int l = j - (img.getHeight() / 2) * SpriteEditor.scale;
            g.translate(k, l);
            ((Graphics2D)g).drawImage(img, affinetransform, this);
            g.translate(-k, -l);
        }
    }

    public BufferedImage getImg()
    {
        return img;
    }

    public void setImg(BufferedImage bufferedimage)
    {
        img = bufferedimage;
    }

    private BufferedImage img;
}
