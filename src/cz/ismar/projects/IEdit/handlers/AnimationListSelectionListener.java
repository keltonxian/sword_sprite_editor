package cz.ismar.projects.IEdit.handlers;

import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.structure.Sprite;
import cz.ismar.projects.IEdit.ui.AnimationPanel;
import java.util.List;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * 在“动画列表”中切换选择时，更新“动画帧列表”
 * @author Lori
 *
 */
public class AnimationListSelectionListener
    implements ListSelectionListener
{

    public AnimationListSelectionListener()
    {
    }

    public void valueChanged(ListSelectionEvent listselectionevent)
    {
        if(!SpriteEditor.sprite.getAnimations().isEmpty() && SpriteEditor.animationPanel.animationsJList.getSelectedIndex() > -1 && SpriteEditor.animationPanel.animationsJList.getSelectedIndex() < SpriteEditor.sprite.size() + 1)
            SpriteEditor.sprite.setIndex(SpriteEditor.animationPanel.animationsJList.getSelectedIndex());
        SpriteEditor.animationPanel.initGestures();
    }
}
