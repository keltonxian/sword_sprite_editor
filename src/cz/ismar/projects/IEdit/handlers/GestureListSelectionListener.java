package cz.ismar.projects.IEdit.handlers;

import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.structure.*;
import cz.ismar.projects.IEdit.ui.AnimationPanel;
import cz.ismar.projects.IEdit.ui.WysiwygGestureComponent;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * “动画帧列表”里选择事件处理
 * 
 * － 更新“当前动画帧”信息
 * － 更新Label x/x 累积偏移 xx
 * @author Lori
 *
 */
public class GestureListSelectionListener
    implements ListSelectionListener
{

    public GestureListSelectionListener()
    {
    }

    public void valueChanged(ListSelectionEvent listselectionevent)
    {
        if(SpriteEditor.sprite.size() > 0 && SpriteEditor.sprite.getCurrentAnimation().size() > 0 && SpriteEditor.animationPanel.gesturesJList.getSelectedIndex() > -1 && SpriteEditor.animationPanel.gesturesJList.getSelectedIndex() < SpriteEditor.sprite.getCurrentAnimation().size() + 1)
        {
            Animation animation = SpriteEditor.sprite.getCurrentAnimation();
            animation.setIndex(SpriteEditor.animationPanel.gesturesJList.getSelectedIndex());
            SpriteEditor.animationPanel.gestureInAnimationNoLabel.setText((animation.getIndex() + 1) + "/" + animation.size());
            if(animation.getType() == 4)
            {
                SpriteEditor.animationPanel.gestureSpeedX.setValue(0);
                SpriteEditor.animationPanel.gestureSpeedY.setValue(0);
                SpriteEditor.animationPanel.speedXAroundPanel.setVisible(false);
                SpriteEditor.animationPanel.speedYAroundPanel.setVisible(false);
            } else
            {
                SpriteEditor.animationPanel.gestureSpeedX.setValue(SpriteEditor.sprite.getCurrentAnimation().get().getSpeedX());
                SpriteEditor.animationPanel.gestureSpeedY.setValue(-SpriteEditor.sprite.getCurrentAnimation().get().getSpeedY());
                AnimationPanel _tmp = SpriteEditor.animationPanel;
                SpriteEditor.animationPanel.cumulatetLabel.setText("累积偏移:" + SpriteEditor.sprite.getCurrentAnimation().getTxtSumaPoositionForGesture(SpriteEditor.animationPanel.gesturesJList.getSelectedIndex() + 1));
                SpriteEditor.animationPanel.speedXAroundPanel.setVisible(true);
                SpriteEditor.animationPanel.speedYAroundPanel.setVisible(true);
            }
            SpriteEditor.animationPanel.gestureDuration.setValue(SpriteEditor.sprite.getCurrentAnimation().get().getLength());
        }
        SpriteEditor.animationPanel.wysiwygGestureComponent.repaint();
    }
}
