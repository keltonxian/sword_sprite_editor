package cz.ismar.projects.IEdit.handlers;

import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.ui.AnimationPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * 【动画编辑区】“播放控制”标尺操作处理
 * @author Lori
 *
 */
public class FramesPerSecondChangeListener
    implements ChangeListener
{

    public FramesPerSecondChangeListener()
    {
    }

    public void stateChanged(ChangeEvent changeevent)
    {
        SpriteEditor.animationPanel.gestureTicksPerSecond.getValue();
    }
}
