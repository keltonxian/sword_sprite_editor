package cz.ismar.projects.IEdit.handlers;

import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.structure.Animation;
import cz.ismar.projects.IEdit.structure.Sprite;
import cz.ismar.projects.IEdit.ui.AnimationPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JPanel;

/**
 * "动画属性"设置 时的操作事件
 * - Moving
 * - Not Moving
 * @author Lori
 *
 */
public class AnimationTypeActionListener
    implements ActionListener
{

    public AnimationTypeActionListener()
    {
    }

    public void actionPerformed(ActionEvent actionevent)
    {
    	//下拉菜单选择
    	  //-Moving
    	  //-Not Moving
        if (SpriteEditor.sprite.getCurrentAnimation() == null)
            return;
        SpriteEditor.sprite.getCurrentAnimation().setType(Animation.TYPE_VALUES[SpriteEditor.animationPanel.typeCombo.getSelectedIndex()]);
        SpriteEditor.animationPanel.speedXAroundPanel.setVisible(SpriteEditor.sprite.getCurrentAnimation().getType() == 16);
        SpriteEditor.animationPanel.speedYAroundPanel.setVisible(SpriteEditor.sprite.getCurrentAnimation().getType() == 16);
    }
}
