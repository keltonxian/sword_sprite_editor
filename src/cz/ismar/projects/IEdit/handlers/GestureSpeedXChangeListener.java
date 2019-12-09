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
 * 水平偏移 标尺处理
 * 
 * @author Lori
 *
 */
public class GestureSpeedXChangeListener
    implements ChangeListener
{

    public GestureSpeedXChangeListener()
    {
    }

    public void stateChanged(ChangeEvent changeevent)
    {
        //daben:modify 原方法：播放状态调整帧水平位置会影响其它帧
        //SpriteEditor.project.get().get().setSpeedX(SpriteEditor.mainGesturePanel.gestureSpeedX.getValue());
        if (SpriteEditor.sprite.getCurrentAnimation() != null && SpriteEditor.sprite.getCurrentAnimation().get() != null)
            ((Gesture)SpriteEditor.animationPanel.gesturesJList.getSelectedValue()).setSpeedX(SpriteEditor.animationPanel.gestureSpeedX.getValue());
        
//        if (SpriteEditor.animationsSaved)
//            SpriteEditor.animationsSaved = false;
        //~daben
    }
}
