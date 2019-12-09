/**
 * 2007-9-12 daben
 * - add transformation action #see daben:add
 */

package cz.ismar.projects.IEdit.handlers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import cz.ismar.projects.IEdit.SpriteEditor;

/**
 * 【帧切片列表】里，弹出菜单操作
 * @author Lori
 *
 */
public class FrameFragmentPopUpMenuActionListener
    implements ActionListener
{

    public FrameFragmentPopUpMenuActionListener(FrameFragmentsListCommander framefragmentslistcommander)
    {
        commander = framefragmentslistcommander;
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        if(actionevent.getActionCommand().equalsIgnoreCase("置顶"))
            commander.doMoveTop();
        else
        if(actionevent.getActionCommand().equalsIgnoreCase("上移"))
            commander.doMoveUp();
        else
        if(actionevent.getActionCommand().equalsIgnoreCase("下移"))
            commander.doMoveDown();
        else
        if(actionevent.getActionCommand().equalsIgnoreCase("置底"))
            commander.doMoveBottom();
        else
        if(actionevent.getActionCommand().equalsIgnoreCase("删除"))
            commander.doDelete();
        else
        if(actionevent.getActionCommand().equalsIgnoreCase("隐藏"))
            commander.doMask();
        //daben:add
//变换类型：0为没有变换，1为水平翻转，2为垂直翻转，3为旋转90度，4为旋转180度，5为旋转270度，6为关于Y=X轴翻转，7为关于Y=-X轴翻转
        //TODO,以常量表示【翻转】
        else
        if(actionevent.getActionCommand().equalsIgnoreCase("水平翻转"))
            commander.doTransform(1);
        else
        if(actionevent.getActionCommand().equalsIgnoreCase("垂直翻转"))
        	commander.doTransform(2);
        else
        if(actionevent.getActionCommand().equalsIgnoreCase("旋转90度"))
        	commander.doTransform(3);
        else
        if(actionevent.getActionCommand().equalsIgnoreCase("旋转180度"))
        	commander.doTransform(4);
        else
        if(actionevent.getActionCommand().equalsIgnoreCase("旋转270度"))
        	commander.doTransform(5);
        else
        if(actionevent.getActionCommand().equalsIgnoreCase("关于Y=X翻转"))
        	commander.doTransform(6);
        else
        if(actionevent.getActionCommand().equalsIgnoreCase("关于Y=-X翻转"))
        	commander.doTransform(7);
        else
        if(actionevent.getActionCommand().equalsIgnoreCase("还原"))
        	commander.doTransform(0);
        //~daben
        else
        if(actionevent.getActionCommand().equalsIgnoreCase("复制"))
            commander.doCopy();
        else
        if(actionevent.getActionCommand().equalsIgnoreCase("粘贴"))
            commander.doPaste();
        // daben:add
        else if (actionevent.getActionCommand().equalsIgnoreCase("撤消"))
        {
            SpriteEditor.sprite.frameHolder.getCurrentFrame().undo();
        }
        // ~daben
    }

    FrameFragmentsListCommander commander;
}
