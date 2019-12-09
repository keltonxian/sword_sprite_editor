package cz.ismar.projects.IEdit.handlers;

import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.structure.Sprite;
import cz.ismar.projects.IEdit.ui.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * "膨胀"
 * - 2X 
 * 
 * 操作处理(将精灵膨胀2倍)
 * @author Lori
 *
 */
public class DoubleSizeActionListener
    implements ActionListener
{

    public DoubleSizeActionListener()
    {
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        SpriteEditor.sprite.rescaleTimes(2.0F);
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
