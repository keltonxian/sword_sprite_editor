/*
 * 2007-10-31 daben
 */

package cz.ismar.projects.IEdit.handlers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.structure.FragmentHolder;
import cz.ismar.projects.IEdit.structure.Sprite;
import cz.ismar.projects.IEdit.ui.FramePanel;
import cz.ismar.projects.IEdit.ui.AnimationPanel;

public class NewProjectMenuActionListener implements ActionListener
{

    public void actionPerformed(ActionEvent event)
    {
        if (!SpriteEditor.isAnimationSaved())
            return;
        
        SpriteEditor.mainTabbedPanel.remove(SpriteEditor.framePanel);
        SpriteEditor.mainTabbedPanel.remove(SpriteEditor.animationPanel);
        // 由于切片面板不重新初始化，所以先保存原切片面板数据
        FragmentHolder oldFragmentHolder = SpriteEditor.sprite.fragmentHolder;
        SpriteEditor.zoomMenu.setText("放大: 1x");
        SpriteEditor.xmlSavePath = null;
        
        
        SpriteEditor.spriteConf.setComments("iEdit sprite conf file");
        SpriteEditor.spriteConf.addPropertyIOable("framesBackround", SpriteEditor.framesBackground);
        SpriteEditor.spriteConf.addPropertyIOable("gesturesBackground", SpriteEditor.gesturesBackground);

        SpriteEditor.sprite = new Sprite("DEFPROJECT");
        if(SpriteEditor.sprite.getCurrentAnimation() != null)
            SpriteEditor.sprite.getCurrentAnimation().reset();
        SpriteEditor.sprite.fragmentHolder = oldFragmentHolder;


        SpriteEditor.loadPrefs();
//        SpriteEditor.mainFragmentPanel = new FragmentPanel();
//        SpriteEditor.mainFragmentPanel.initFragments();
//        ImageIcon imageicon1 = SpriteEditor.project.fragmentHolder.getIcon();
//        if(imageicon1 != null)
//            SpriteEditor.mainFragmentPanel.clipper.setImageSize(imageicon1.getIconWidth(), imageicon1.getIconHeight());
        SpriteEditor.framePanel = new FramePanel();
        SpriteEditor.framePanel.initFragmentFiles();
        SpriteEditor.framePanel.initFragments();
        SpriteEditor.framePanel.initFrames();
        SpriteEditor.framePanel.initFrameFragments();
        SpriteEditor.animationPanel = new AnimationPanel();
        SpriteEditor.animationPanel.initGestures();
        SpriteEditor.animationPanel.initFrames();
        SpriteEditor.animationPanel.initAnimations();
        
        SpriteEditor.mainTabbedPanel.add("帧编辑区", SpriteEditor.framePanel);
        SpriteEditor.mainTabbedPanel.add("动画编辑区", SpriteEditor.animationPanel);
        SpriteEditor.mainTabbedPanel.setSelectedComponent(SpriteEditor.framePanel);

    }

}
