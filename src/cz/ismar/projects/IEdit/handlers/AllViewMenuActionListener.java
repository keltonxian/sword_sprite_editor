package cz.ismar.projects.IEdit.handlers;

import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.ui.*;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

/**
 * “显示”菜单设置
 * 
 * - 碰撞框
 * - 网格
 * - 坐标架
 * - 动画帧列表
 * - 帧列表
 * - 动画列表
 * 
 * @author Lori
 *
 */
public class AllViewMenuActionListener
    implements ActionListener
{

    public AllViewMenuActionListener()
    {
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        if(actionevent.getActionCommand().equalsIgnoreCase("碰撞框"))
            SpriteEditor.collisionOn = !SpriteEditor.collisionOn;
        else
        if(actionevent.getActionCommand().equalsIgnoreCase("网格"))
            SpriteEditor.gridOn = !SpriteEditor.gridOn;
        else
        if(actionevent.getActionCommand().equalsIgnoreCase("坐标架"))
            SpriteEditor.crossOn = !SpriteEditor.crossOn;
        else
        	if(actionevent.getActionCommand().equalsIgnoreCase("地图碰撞"))
        		SpriteEditor.mapCrossOn = !SpriteEditor.mapCrossOn;
        else
        if(actionevent.getActionCommand().equalsIgnoreCase("动画帧列表"))
        {
            SpriteEditor.framesetOn = !SpriteEditor.framesetOn;
            SpriteEditor.animationPanel.animationsMainPanel.setVisible(SpriteEditor.framesetOn);
        } else
        if(actionevent.getActionCommand().equalsIgnoreCase("帧列表"))
        {
            SpriteEditor.allFrameOn = !SpriteEditor.allFrameOn;
            SpriteEditor.framePanel.framesMainPanel.setVisible(SpriteEditor.allFrameOn);
        } else
        if(actionevent.getActionCommand().equalsIgnoreCase("动画列表"))
        {
            SpriteEditor.framesetFrameOn = !SpriteEditor.framesetFrameOn;
            SpriteEditor.animationPanel.gesturesMainPanel.setVisible(SpriteEditor.framesetFrameOn);
        }
        SpriteEditor.fragmentPanel.fragmentEditorComponent.repaint();
        
        //父窗口无效
        SpriteEditor.fragmentPanel.fragmentEditorComponent.getParent().invalidate();
        SpriteEditor.framePanel.repaint();
        SpriteEditor.framePanel.getParent().invalidate();
        SpriteEditor.animationPanel.repaint();
        SpriteEditor.animationPanel.getParent().invalidate();
    }
}
