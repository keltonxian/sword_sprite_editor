package cz.ismar.projects.IEdit.handlers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import cz.ismar.projects.IEdit.SpriteEditor;

/**脚本编辑器 关于操作
 * @author Lori
 * @version 1.01 2008/1/17
 */
public class AboutMenuActionListener implements ActionListener{

	 
	 public AboutMenuActionListener(){
		 
	 }

	 public void actionPerformed(ActionEvent actionevent){
		 
		 JOptionPane.showMessageDialog(SpriteEditor.mainFrame, getAboutMessage(), 
				 "关于", JOptionPane.INFORMATION_MESSAGE);
	 }
	 
	 private String getAboutMessage(){
		 StringBuffer buffer = new StringBuffer();
		 
		 buffer.append("切片图请用数字表示: 如1.png(注意不能写01.png), 16.png等等\n");
		 buffer.append("动画命名请使用前缀r_, 如r_1.xml, r_55.xml; 后缀ID跟游戏特定动画的编号相关联(如职业ID)等等..\n");
		 return buffer.toString();
	 }
}
