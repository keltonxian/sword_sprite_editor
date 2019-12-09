package cz.ismar.projects.IEdit.handlers;

import cz.ismar.projects.IEdit.IconLabelListModel;
import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.structure.Animation;
import cz.ismar.projects.IEdit.structure.Sprite;
import cz.ismar.projects.IEdit.ui.AnimationPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * "删除帧" 动作处理
 * @author Lori
 *
 */
public class GestureRemoveActionListener
    implements ActionListener
{

    public GestureRemoveActionListener()
    {
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        if(SpriteEditor.sprite.getCurrentAnimation() !=null && SpriteEditor.sprite.getCurrentAnimation().remove())
        {
            SpriteEditor.animationPanel.gesturesListModel.updateDelete();
            SpriteEditor.animationPanel.gestureInAnimationNoLabel.setText((SpriteEditor.sprite.getCurrentAnimation().getIndex() + 1) + "/" + SpriteEditor.sprite.getCurrentAnimation().size());
            SpriteEditor.animationPanel.gesturesJList.setSelectedIndex(SpriteEditor.sprite.getCurrentAnimation().getIndex());
            SpriteEditor.animationPanel.gesturesJList.getSelectionModel().setSelectionInterval(SpriteEditor.sprite.getCurrentAnimation().getIndex(), SpriteEditor.sprite.getCurrentAnimation().getIndex());
        }
    }
}
