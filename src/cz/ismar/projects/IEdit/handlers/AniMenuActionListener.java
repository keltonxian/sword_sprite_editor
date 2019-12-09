/**
 * 2007-9-6 daben 
 * - add
 * --> SpriteEditor.centerScrollPane(SpriteEditor.mainFramingPanel.wysiwygFrameScrollPanel);
 */

package cz.ismar.projects.IEdit.handlers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.structure.ConfigData;

public class AniMenuActionListener
    implements ActionListener
{

    public AniMenuActionListener()
    {
    }

    public void actionPerformed(ActionEvent actionevent)
    {
    	ConfigData.setAniType(Integer.parseInt(actionevent.getActionCommand()));
    	SpriteEditor.animationPanel.animationsMainPanel.repaint();
    }
}
