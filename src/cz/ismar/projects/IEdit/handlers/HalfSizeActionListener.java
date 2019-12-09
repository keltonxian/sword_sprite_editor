package cz.ismar.projects.IEdit.handlers;

import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.structure.Sprite;
import cz.ismar.projects.IEdit.ui.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * "膨胀"- /2 
 * 操作
 * @author Lori
 *
 */
public class HalfSizeActionListener
    implements ActionListener
{

    public HalfSizeActionListener()
    {
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        SpriteEditor.sprite.rescaleTimes(-2F);
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
