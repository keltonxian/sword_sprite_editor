package cz.ismar.projects.IEdit.handlers;

import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.io.InOut;
import cz.ismar.projects.IEdit.structure.FragmentHolder;
import cz.ismar.projects.IEdit.structure.Sprite;
import cz.ismar.projects.IEdit.ui.FragmentPanel;
import cz.ismar.projects.IEdit.ui.FragmentEditComponent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;


/**
 * 【切片区】 "重新加载"切片资源操作
 * @author Lori
 *
 */
public class FragmentFileReloadButtonActionListener
    implements ActionListener
{

    public FragmentFileReloadButtonActionListener()
    {
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        // daben:add
        if (!SpriteEditor.isFragmentSaved())
            return;
            
        FragmentHolder fragmentholder = SpriteEditor.sprite.fragmentHolder;
        if(fragmentholder != null && fragmentholder.getImageFile() != null)
        {
            File file = fragmentholder.getImageFile();
            fragmentholder = new FragmentHolder();
            SpriteEditor.sprite.fragmentHolder = fragmentholder;
            fragmentholder.setImageFile(file);
            File file1 = new File(file.getParentFile(), fragmentholder.getName() + ".xml");
            ImageIcon imageicon = fragmentholder.getIcon();
            SpriteEditor.fragmentPanel.fragmentEditorComponent.setImageSize(imageicon.getIconWidth(), imageicon.getIconHeight());
            if(file1.exists())
                try
                {
                    InOut.inFragmentXML(fragmentholder, file1);
                    SpriteEditor.setStatus(file1 + " loaded");
                    SpriteEditor.fragmentPanel.fragmentEditorComponent.repaint();
                    SpriteEditor.initFragmentIcons();
                    SpriteEditor.fragmentPanel.initFragments();
                }
                catch(Exception exception)
                {
                    String s = "重新加载时出错: " + exception.getLocalizedMessage();
                    JOptionPane.showMessageDialog(SpriteEditor.mainFrame, s, SpriteEditor.TITLE, 0);
                    SpriteEditor.setStatus(s);
//                    exception.printStackTrace();
                    System.out.println("Error when reloading");
                }
        }
    }
}
