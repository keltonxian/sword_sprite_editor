/**
 * 
 *  修改帧间隔时间的计算方式
 *       //Thread.sleep(1000 / SpriteEditor.mainGesturePanel.gestureTicksPerSecond.getValue());
 *       //modify:
 *       Thread.sleep(75 * SpriteEditor.mainGesturePanel.gestureTicksPerSecond.getValue());
 * 
 * */
package cz.ismar.projects.IEdit.handlers;

import cz.ismar.projects.IEdit.Animator;
import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.structure.*;
import cz.ismar.projects.IEdit.ui.AnimationPanel;
import cz.ismar.projects.IEdit.ui.WysiwygGestureComponent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.*;

/**
 * 动画播放 操作
 * @author Lori(赤浪)
 *
 */
public class PlayGestureButtonItemListener
    implements ItemListener
{

    public PlayGestureButtonItemListener()
    {
    }

    public void itemStateChanged(ItemEvent itemevent)
    {
    	//当“播放”按钮被选定时
        if(itemevent.getStateChange() == ItemEvent.SELECTED)
        {
            SpriteEditor.animationPanel.wysiwygGestureComponent.setPosition(0, 0);
            SpriteEditor.animationPanel.wysiwygGestureComponent.repaint();
            SpriteEditor.animationPanel.animator = new Animator() {

                public void run()
                {
                    running = true;
                    SpriteEditor.animationPanel.wysiwygGestureComponent.setPosition(0, 0);
                    if(SpriteEditor.sprite.getCurrentAnimation() != null && SpriteEditor.sprite.getCurrentAnimation().size() > 0)
                    {
                        SpriteEditor.sprite.getCurrentAnimation().reset();
                        for(; SpriteEditor.animationPanel.checkAround.isSelected() || !SpriteEditor.sprite.getCurrentAnimation().isLastTick(); SpriteEditor.animationPanel.wysiwygGestureComponent.movePosition(SpriteEditor.sprite.getCurrentAnimation().get().getSpeedX(), SpriteEditor.sprite.getCurrentAnimation().get().getSpeedY()))
                        {
                            SpriteEditor.animationPanel.repaint();
                            try
                            {
                            	//daben:modify 修改帧间隔时间的计算方式
                                //Thread.sleep(1000 / SpriteEditor.mainGesturePanel.gestureTicksPerSecond.getValue());
                            	//modify:
                                Thread.sleep(75 * SpriteEditor.animationPanel.gestureTicksPerSecond.getValue());
                                //~daben
                            }
                            catch(Exception exception) { }
                            SpriteEditor.sprite.getCurrentAnimation().nextTick(SpriteEditor.animationPanel.checkAround.isSelected());
                        }

                    }
                    SpriteEditor.animationPanel.playButton.setSelected(false);
                    SpriteEditor.sprite.getCurrentAnimation().reset();
                    SpriteEditor.animationPanel.repaint();
                }

            };
            SpriteEditor.animationPanel.animator.start();
        } else
        {
            SpriteEditor.animationPanel.animator.running = false;
            SpriteEditor.animationPanel.checkAround.setSelected(false);
            SpriteEditor.animationPanel.wysiwygGestureComponent.setPosition(0, 0);
            SpriteEditor.animationPanel.wysiwygGestureComponent.repaint();
        }
    }
}
