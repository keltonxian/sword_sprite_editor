package cz.ismar.projects.IEdit.handlers;

import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.structure.*;
import cz.ismar.projects.IEdit.ui.AnimationPanel;
import cz.ismar.projects.IEdit.ui.WysiwygGestureComponent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JSlider;

/**
 * "下一帧" 按钮操作
 * @author Lori
 *
 */
public class GestureNextButtonActionListener
    implements ActionListener
{

    public GestureNextButtonActionListener()
    {
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        Animation animation = SpriteEditor.sprite.getCurrentAnimation();
        if(animation == null)
            return;
        if(animation.nextTick(false))
            SpriteEditor.animationPanel.wysiwygGestureComponent.movePosition(SpriteEditor.sprite.getCurrentAnimation().get().getSpeedX(), SpriteEditor.sprite.getCurrentAnimation().get().getSpeedY());
        SpriteEditor.animationPanel.gestureSpeedX.setValue(SpriteEditor.sprite.getCurrentAnimation().get().getSpeedX());
        SpriteEditor.animationPanel.gestureSpeedY.setValue(-SpriteEditor.sprite.getCurrentAnimation().get().getSpeedY());
        SpriteEditor.animationPanel.gestureDuration.setValue(SpriteEditor.sprite.getCurrentAnimation().get().getLength());
        AnimationPanel _tmp = SpriteEditor.animationPanel;
        SpriteEditor.animationPanel.cumulatetLabel.setText("累积偏移:" + SpriteEditor.sprite.getCurrentAnimation().getTxtSumaPoosition());
        SpriteEditor.animationPanel.repaint();
    }
}
