/**
 * 2007-10-10 daben
 * - modify in stateChanged()
 */

package cz.ismar.projects.IEdit.handlers;

import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.structure.*;
import cz.ismar.projects.IEdit.ui.AnimationPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * 垂直偏移 标尺处理
 * @author Lori
 *
 */
public class GestureSpeedYChangeListener
    implements ChangeListener
{

    public GestureSpeedYChangeListener()
    {
    }

    public void stateChanged(ChangeEvent changeevent)
    {
        
        //daben:modify 原方法：播放状态调整帧垂直位置会影响其它帧
        //SpriteEditor.project.get().get().setSpeedY(-SpriteEditor.mainGesturePanel.gestureSpeedY.getValue());
        if (SpriteEditor.sprite.getCurrentAnimation() != null && SpriteEditor.sprite.getCurrentAnimation().get() != null)
            ((Gesture)SpriteEditor.animationPanel.gesturesJList.getSelectedValue()).setSpeedY(-SpriteEditor.animationPanel.gestureSpeedY.getValue());

//        if (SpriteEditor.animationsSaved)
//            SpriteEditor.animationsSaved = false;
        //~daben
    }
}
