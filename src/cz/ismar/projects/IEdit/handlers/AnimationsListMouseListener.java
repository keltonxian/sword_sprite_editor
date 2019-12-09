package cz.ismar.projects.IEdit.handlers;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;

import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.structure.Animation;
import cz.ismar.projects.IEdit.structure.ConfigData;
import cz.ismar.projects.IEdit.structure.Sprite;


/**
 * 在“动画列表”中的操作时，设置如下菜单的可使用性；
 * 同时弹出菜单
 * 
 * - 置顶
 * - 上移
 * - 下移
 * - 置底
 * - 新建
 * - 复制
 * - F2
 * - 删除
 * @author Lori
 *
 */
public class AnimationsListMouseListener extends MouseAdapter
{

    public AnimationsListMouseListener()
    {
    }
    
    public void mouseClicked(MouseEvent mouseevent)
	 {
		 
    	
    	//双击修改事件
		 if (mouseevent.getClickCount() == 2)
		 {
			 
			 String[] getAniNames = ConfigData.getAniNames();
			 
             String s = (String)JOptionPane.showInputDialog(
            		 SpriteEditor.mainFrame,
                                 "请选择动作的类型",
                                 "动作选择",
                                 JOptionPane.PLAIN_MESSAGE,
                                 null,
                                 getAniNames,
                                 getAniNames[0]);

             //If a string was returned, say so.
             if ((s != null) && (s.length() > 0)) {
                 
            	 Animation animation = SpriteEditor.sprite.getCurrentAnimation();
            	 if(animation==null)return;
            	 for(int i=0;i<getAniNames.length;i++){
            		 if(s.equalsIgnoreCase(getAniNames[i])){
            			 animation.setAnimationId((byte)i);
            			 animation.setName("");
            			 break;
            		 }
            	 }
             }
		 }
		 
	 }

    public void mouseReleased(MouseEvent mouseevent)
    {
        if(mouseevent.isPopupTrigger() && SpriteEditor.sprite.size() != 0)
        {
            int i = SpriteEditor.animationPanel.animationsJList.locationToIndex(mouseevent.getPoint());
            if(i > -1)
            {
                SpriteEditor.sprite.setIndex(i);
                if(SpriteEditor.sprite.isFirst())
                {
                    SpriteEditor.animationPanel.animationTopMenuItem.setEnabled(false);
                    SpriteEditor.animationPanel.animationUpMenuItem.setEnabled(false);
                } else
                {
                    SpriteEditor.animationPanel.animationTopMenuItem.setEnabled(true);
                    SpriteEditor.animationPanel.animationUpMenuItem.setEnabled(true);
                }
                if(SpriteEditor.sprite.isLast())
                {
                    SpriteEditor.animationPanel.animationDownMenuItem.setEnabled(false);
                    SpriteEditor.animationPanel.animationBottomMenuItem.setEnabled(false);
                } else
                {
                    SpriteEditor.animationPanel.animationDownMenuItem.setEnabled(true);
                    SpriteEditor.animationPanel.animationBottomMenuItem.setEnabled(true);
                }
                
                //弹出菜单
                SpriteEditor.animationPanel.animationPopup.show(mouseevent.getComponent(), mouseevent.getX(), mouseevent.getY());
            }
        }
    }
}
