package cz.ismar.projects.IEdit.handlers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import cz.ismar.projects.IEdit.SpriteEditor;

/**
 * 退出编辑器操作
 * @author Lori
 *
 */
public class ExitMenuActionListener
    implements ActionListener
{

    public ExitMenuActionListener()
    {
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        // daben:add
        if (!SpriteEditor.isAllSaved())
            return;
        // ~daben

      SpriteEditor.exit();
    }
}
