package cz.ismar.projects.IEdit.handlers;

import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.structure.Sprite;
import cz.ismar.projects.IEdit.ui.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import javax.swing.JOptionPane;

/**
 * 膨胀－ % 操作
 * @author Lori
 *
 */
public class PercentSizeActionListener
    implements ActionListener
{

    public PercentSizeActionListener()
    {
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        String s = (String)JOptionPane.showInputDialog(SpriteEditor.mainFrame, ":to resize:", SpriteEditor.TITLE, -1, null, null, "100");
        if(s != null && s.length() > 0)
        {
            float f = 100F;
            try
            {
                f = Float.parseFloat(s);
            }
            catch(Exception exception) { }
            SpriteEditor.sprite.rescaleTimes(f / 100F);
        }
        SpriteEditor.fragmentPanel.initFragments();
        SpriteEditor.framePanel.initFrameFragments();
        SpriteEditor.framePanel.initFrames();
        SpriteEditor.animationPanel.initFrames();
        SpriteEditor.animationPanel.initGestures();
        SpriteEditor.animationPanel.initAnimations();
        SpriteEditor.framePanel.frameEditComponent.repaint();
        SpriteEditor.fragmentPanel.repaint();
    }
}
