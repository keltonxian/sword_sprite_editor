package cz.ismar.projects.IEdit.handlers;

import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.structure.Animation;
import cz.ismar.projects.IEdit.structure.Sprite;
import cz.ismar.projects.IEdit.ui.FramePanel;
import cz.ismar.projects.IEdit.ui.AnimationPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/**
 * 
 * “新建动画” － 操作【在动画编辑区】
 * 
 * @author Lori
 *
 */
public class AnimationCreateActionListener
    implements ActionListener
{

    public AnimationCreateActionListener()
    {
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        Animation animation = new Animation("New animation", SpriteEditor.sprite);
        String s = (String)JOptionPane.showInputDialog(SpriteEditor.mainFrame, "新动画名称:", SpriteEditor.TITLE, -1, null, null, animation.getName());
        if(s != null && s.length() > 0)
            animation.setName(s);
        SpriteEditor.sprite.add(animation);
        SpriteEditor.animationPanel.initAnimations();
        SpriteEditor.framePanel.repaint();
    }
}
