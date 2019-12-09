/**
 * 2007-10-30 daben
 * - modify in stateChanged()
 */

package cz.ismar.projects.IEdit.handlers;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.structure.Gesture;

/**
 * 【动画编辑区】
 * “帧停留”标尺操作处理
 * @author Lori
 *
 */
public class GestureDurationChangeListener
    implements ChangeListener
{

    public GestureDurationChangeListener()
    {
    }

    public void stateChanged(ChangeEvent changeevent)
    {
        //daben:modify 原方法：播放状态时调整帧持续时间会影响其它帧
        //SpriteEditor.project.get().get().setLength(SpriteEditor.mainGesturePanel.gestureDuration.getValue());
        if (SpriteEditor.sprite.getCurrentAnimation() != null && SpriteEditor.sprite.getCurrentAnimation().get() != null)
            ((Gesture)SpriteEditor.animationPanel.gesturesJList.getSelectedValue()).setLength(SpriteEditor.animationPanel.gestureDuration.getValue());

//        if (SpriteEditor.animationsSaved)
//            SpriteEditor.animationsSaved = false;
        //~daben
    }
}
