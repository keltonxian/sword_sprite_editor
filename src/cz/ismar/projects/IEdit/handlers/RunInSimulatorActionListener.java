/**
 * 2007-11-6 daben
 */

package cz.ismar.projects.IEdit.handlers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

import javax.swing.JOptionPane;

import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.structure.FragmentHolder;

public class RunInSimulatorActionListener implements ActionListener
{

	public RunInSimulatorActionListener(){
		
	}
    public void actionPerformed(ActionEvent arg0)
    {
        if (!SpriteEditor.animationsSaved)
        {
            JOptionPane.showMessageDialog(SpriteEditor.mainFrame, 
                    "测试之前先保存XML", 
                    "警告", 
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        Object obj = arg0.getSource();
        //打包
        if(obj == SpriteEditor.convertMenuItem){
        	
        	try{
        		
        		convertXML2Bin();
        	}
        	 catch (Exception e)
             {
                 System.out.println("-- Run in simulator failure");
                 JOptionPane.showMessageDialog(SpriteEditor.mainFrame,
                         "打包失败"+e,
                         "错误:",
                         JOptionPane.INFORMATION_MESSAGE);
             }
        	return;
        }
        
        //运行
        projectName = SpriteEditor.sprite.xmlFile.getName().replace(".xml", "");
        theNthAnimation = SpriteEditor.animationPanel.animationsJList.getSelectedIndex();
        
        try
        {
        	
            //设为demo精灵
            if( obj == SpriteEditor.setDemoSpriteMenuItem){
            	
            	runCovert(true);
            	//prepareDataFile(true);
            }
            else{
            	runCovert(false);
            	prepareDataFile(false);
            	packageJar();
            	runJar();
            	
            }
            
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(SpriteEditor.mainFrame,
                    "运行失败"+e,
                    "错误:",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    // 把XML文件转换为游戏中用的二进制文件
    void convertXML2Bin() throws Exception
    {
        String userDir = System.getProperty("user.dir");
        String res_fragment = userDir + "/res/fragment/";
        String res_sprite = userDir + "/res/sprite/";
        String output_fragment = userDir + "/output/fragment/";
        String output_sprite = userDir + "/output/sprite/";
        
        // 清空原文件夹
        delDir(res_fragment);
        delDir(res_sprite);
        delDir(output_fragment);
        delDir(output_sprite);
        
        
        //-----------------------------------------------------------------------------------------
        /*
        // 拷贝动画中用到的xml文件到res目录
        //List gesturesList = ((Animation)SpriteEditor.mainGesturePanel.animationsJList.getModel().getElementAt(theNthAnimation)).getGestures();
        List holderList = SpriteEditor.project.fragmentHoldersList.getFragmentHolders();
        fragmentsXMLDir = new String[holderList.size()];
        fragmentsImageDir = new String[holderList.size()];
        FragmentHolder fragmentHolder = null;
        for (int i=0; i<holderList.size(); i++)
        {
            fragmentHolder = (FragmentHolder)holderList.get(i);
            fragmentsImageDir[i] = fragmentHolder.getImageFile().getAbsolutePath();
            fragmentsXMLDir[i] = fragmentsImageDir[i].replace(".png", ".xml");
            
            try
            {
                copyFile(fragmentsXMLDir[i], res_fragment + new File(fragmentsXMLDir[i]).getName());
            }
            catch (IOException ioException)
            {
                System.out.println("-- copy fragments xml file error before converting");
                throw ioException;
            }
        }
        
        try
        {
            copyFile(SpriteEditor.project.xmlFile.getAbsolutePath(), res_sprite + projectName + ".xml");
        }
        catch (IOException ioException)
        {
            System.out.println("-- copy sprite xml file error before converting");
            throw ioException;
        }*/
        
        copyAllSpriteFile();
        //-------------------------------------------------------------------------------------------
//        
//        
//        
////        String runString = "java -jar " + System.getProperty("user.dir") + "/iEditorXML2Bin.jar";
//        String runString = "java -jar iEditorXML2Bin.jar";
//        Process p = null;
//        try
//        {
//            p = Runtime.getRuntime().exec(runString, null, null);
////            p = Runtime.getRuntime().exec("cmd /c start" + System.getProperty("user.dir") + "/run.bat");
//            
//            DataInputStream in = new DataInputStream(p.getInputStream());
//            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//
//            String line = null;
//            while ((line = reader.readLine()) != null)
//            {   
//                System.out.println(line);
//            }   
//            reader.close();
//            in.close();
//
//            p.destroy();
//         }
//        catch (Exception e)
//        {
//            System.out.println("-- run convertor error:"+e);
//            throw e;
//        }
    }
    
    void runCovert(boolean isSetDemo) throws Exception
    {

    	String userDir = System.getProperty("user.dir");
         String res_sprite = userDir + "/res/sprite/";
         String output_sprite = userDir + "/output/sprite/";
         
        String runString = "java -jar iEditorXML2Bin.jar";
        Process p = null;
        try
        {
        	
        	if(SpriteEditor.xmlSavePath!=null){
        		copyFile(SpriteEditor.xmlSavePath, res_sprite + projectName + ".xml");
        		/*if(isSetDemo){
        			copyFile(SpriteEditor.xmlSavePath, res_sprite + "demo.xml");
        			
        		}*/
        	}
        	 
            p = Runtime.getRuntime().exec(runString, null, null);
            DataInputStream in = new DataInputStream(p.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line = null;
            while ((line = reader.readLine()) != null)
            {   
                System.out.println(line);
            }   
            reader.close();
            in.close();

            p.destroy();
            
            if(isSetDemo){
            	
            	copyFile(output_sprite + projectName + ".spr", userDir + "/j2me/TestSprite/"  + "demo.spr");
            	copyFile(output_sprite + "99.spr", userDir + "/j2me/TestSprite/"  + "99.spr");
            }
            
         }
        catch (Exception e)
        {
            System.out.println("-- run convertor error:"+e);
            throw e;
        }
    }
    
    public void createDir(String path)
    {
        File dir = new File(path);
        if (!dir.exists())
            dir.mkdir();
    }
    
    public void delDir(String path)
    {
        File dir = new File(path);
        if (dir.exists())
        {
            File[] tmp = dir.listFiles();
            for (int i = 0; i < tmp.length; i++)
            {
                if (tmp[i].isDirectory())
                {
                    delDir(path + "/" + tmp[i].getName());
                } else
                {
                    tmp[i].delete();
                }
            }
            
//            dir.delete();
        }
    }
    
    public void copyFile(String src, String dest) throws IOException
    {
        FileInputStream in = new FileInputStream(src);
        File file = new File(dest);
        if (!file.exists())
            file.createNewFile();
        FileOutputStream out = new FileOutputStream(file);
        int c;
        byte buffer[] = new byte[1024];
        while ((c = in.read(buffer)) != -1)
        {
            for (int i = 0; i < c; i++)
                out.write(buffer[i]);
        }
        in.close();
        out.close();
    }
    
    // 把图片文件及二进制文件拷贝到TestSprite目录
    void prepareDataFile(boolean isSetDemo) throws Exception
    {
        String userDir = System.getProperty("user.dir");
        String output_fragment = userDir + "/output/fragment/";
        String output_sprite = userDir + "/output/sprite/";
        
        StringBuffer manifest = new StringBuffer();
        manifest.append("Manifest-Version: 1.0\n");
        manifest.append("MicroEdition-Configuration: CLDC-1.1\n");
        manifest.append("MIDlet-Name: Sprite Midlet Suite\n");
        manifest.append("spid: " + theNthAnimation + "\n");
        manifest.append("MIDlet-Vendor: Midlet Suite Vendor\n");
        manifest.append("MIDlet-1: GameMIDlet,,main.GameMIDlet\n");
        manifest.append("MIDlet-Version: 1.0.0\n");
        manifest.append("MicroEdition-Profile: MIDP-2.0\n");
        manifest.append("spriteName: " + projectName + "\n");
        
        File manifestFile = new File(userDir + "/j2me/TestSprite/META-INF/MANIFEST.MF");
        try
        {
            if(manifestFile.exists())
            	manifestFile.delete();
            
                manifestFile.createNewFile();
            
            FileOutputStream out=new FileOutputStream(manifestFile,true);
            out.write(manifest.toString().getBytes());
        }
        catch (Exception e)
        {
            System.out.println("-- prepare data file error");
            throw e;
        }
        
        try
        {
            /*
        	for (int i=0; i<fragmentsImageDir.length; i++)
            {
                copyFile(fragmentsImageDir[i], userDir + "/j2me/TestSprite/" + new File(fragmentsImageDir[i]).getName());
            }
            */
        	
        	//TODO
        	
        	if(SpriteEditor.xmlSavePath!=null){
        		
        		File f = new File(SpriteEditor.xmlSavePath);
        		String dir = f.getParent() + File.separator;
        		copyTempFiles(dir,true);
        	}
        	copyTempFiles(output_fragment,false);
        	copyTempFiles(output_sprite,false);
        	
            //copyFile(output_fragment + "all.fr", userDir + "/j2me/TestSprite/ani/all.fr");
            //copyFile(output_sprite + projectName + ".spr", userDir + "/j2me/TestSprite/ani/" + projectName + ".spr");
            
//            //设置demo精灵
//            if(isSetDemo){
//            	copyFile(output_sprite + projectName + ".spr", userDir + "/j2me/TestSprite/"  + "demo.spr");
//            }
        }
        catch (Exception ioException)
        {
            System.out.println("-- copy file error after converting");
//            ioException.printStackTrace();
            throw ioException;
        }
    }
    
    // 把TestSprite目录打包成jar文件
    void packageJar() throws Exception
    {
        String folder = System.getProperty("user.dir") + "/j2me/";
        String jarStr = System.getProperty("user.dir") + "/j2me/" + jarFileName;
        String jarFileFolder = folder + "TestSprite/";

        File jar = new File(jarStr);
        File files[] = getDirFiles(jarFileFolder);
        
        try
        {
            ZipFiles(files, jar);
        }
        catch (Exception e)
        {
            System.out.println("package jar error");
            throw e;
        }
    }
    
    File[] getDirFiles(String path)
    {
        File dir = new File(path);
        
        File[] files = dir.listFiles();
        if(files == null)
        {
            return null;
        }
        
        Vector list = new Vector(); 
        for(int i=0; i<files.length; i++)
        {
            list.add(files[i]);     
        }
        
        File[] result = new File[list.size()];
        
        Enumeration e = list.elements();
        for(int i=0;e.hasMoreElements() && i<result.length;i++)
        {
            result[i] = (File)e.nextElement();
            // System.out.println("--  "+files[i].toString());
        }
        
        return result;
    }
    
    void ZipFiles(File[] srcfile, File zipfile) throws Exception
    {
        byte[] buf = new byte[1024];
        try
        {
            // Create the JAR file
            JarOutputStream out = new JarOutputStream(new FileOutputStream(zipfile));
            
            // Compress the files
            for (int i = 0; i < srcfile.length; i++)
            {
                if(srcfile[i].isDirectory())
                {
                    // System.out.println(srcfile[i].getPath()+"-- is dir");
                    
                    File files[] = getDirFiles(srcfile[i].getAbsolutePath());
                    for(int j=0;j<files.length;j++)
                    {
                        
                        FileInputStream in = new FileInputStream(files[j]);
                        // Add JAR entry to output stream.
                        out.putNextEntry(new ZipEntry(srcfile[i].getName() + "/" + files[j].getName()));
                        // Transfer bytes from the file to the JAR file
                        int len;
                        while ((len = in.read(buf)) > 0) 
                        {
                            out.write(buf, 0, len);
                        }
                        // Complete the entry
                        out.closeEntry();
                        in.close();
                    }
                }
                else
                {
                    FileInputStream in = new FileInputStream(srcfile[i]);
                    // Add JAR entry to output stream.
                    out.putNextEntry(new ZipEntry(srcfile[i].getName()));
                    // Transfer bytes from the file to the JAR file
                    int len;
                    while ((len = in.read(buf)) > 0)
                    {
                        out.write(buf, 0, len);
                    }
                    // Complete the entry
                    out.closeEntry();
                    in.close();
                }
            }
            // Complete the JAR file
            out.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("zip files error");
            throw e;
        }
    }
    
    // 运行打包后的jar
    void runJar() throws Exception
    {
        String folder = System.getProperty("user.dir");
        String batFile = folder + "\\j2me\\" + "run.bat";

        try
        {
            FileWriter fw = new FileWriter(batFile);
            BufferedWriter out = new BufferedWriter(fw);

            String temp1 = "start " + folder + "\\j2me\\" + jarFileName;
            out.write(temp1, 0, temp1.length());
            out.flush();
            fw.close();
            out.close();
        }
        catch (IOException e)
        {
            System.out.println("-- run jar:write bat file error");
            throw e;
        }
        
        Runtime ce = Runtime.getRuntime();
        try
        {
            ce.exec(batFile);
        }
        catch (IOException e1)
        {
            System.out.println("-- run jar error");
            throw e1;
        }   

    }
    
    public void copyAllSpriteFile(){
    	
    	if(SpriteEditor.xmlSavePath!=null){
    		
    		File f = new File(SpriteEditor.xmlSavePath);
    		
    		String dir = f.getParent() + File.separator;
    		copyFragments(dir);
    		copySprites(dir);
    		
    	}
    }
    
    public static String[] fragmentFilenames;
    public static String[] spriteFilenames;
    public void copyFragments(String dir){
    	
    	String userDir = System.getProperty("user.dir");
        String res_fragment = userDir + "/res/fragment/";
        
    	File fragmentFiles=new File(dir);
		File[] proc_list = fragmentFiles.listFiles();
		fragmentFilenames =new String[proc_list.length];
		
		Vector fragmentVector = new Vector();
		 for(int i=0;i<fragmentFilenames.length;i++)
        {
			 String name = proc_list[i].getName();
			 
			 if(name.endsWith(".png")){
				 fragmentVector.add(name.subSequence(0, name.length()-4)+".xml");
			 }
       }
		 fragmentFilenames = new String[fragmentVector.size()];
		 fragmentVector.copyInto(fragmentFilenames);
		 
		 for(int i=0;i<fragmentFilenames.length;i++){
			 
			 String fileDir = dir+fragmentFilenames[i];
			 String pngFileDir = fileDir.subSequence(0, fileDir.length()-4)+".png";
			 
			 System.out.println("[fragment]: "+fileDir);
			 System.out.println("[Png]: "+pngFileDir);
			 
			 try
			 {
				 copyFile(fileDir, res_fragment + new File(fileDir).getName());
				 
				 
				 //放到运行末路
				 //copyFile(pngFileDir, userDir + "/j2me/TestSprite" +File.separator +  new File(pngFileDir).getName());
				 
			 }
			 catch (IOException ioException)
			 {
			 }
		 }
		 
    }
    public void copySprites(String dir){
    	 
        String userDir = System.getProperty("user.dir");
        String res_sprite = userDir + "/res/sprite/";
    	File spriteFiles=new File(dir);
		File[] sprite_list = spriteFiles.listFiles();
		spriteFilenames =new String[sprite_list.length];
		
		 Vector spriteVector = new Vector();
		 for(int i=0;i<spriteFilenames.length;i++)
        {
			 String name = sprite_list[i].getName();
			 if(name.endsWith(".xml")){
				 
				 if(!isFragmentFile(name))
				 spriteVector.add(name);
			 }
       }
		 
		 spriteFilenames = new String[spriteVector.size()];
		 spriteVector.copyInto(spriteFilenames);
		 for(int i=0;i<spriteFilenames.length;i++){
			 
			 String fileDir = dir+spriteFilenames[i];
			 System.out.println("[sprite]: "+fileDir);
			 try
			 {
				 copyFile(fileDir, res_sprite + new File(fileDir).getName());
			 }
			 catch (IOException ioException)
			 {
			 }
		 }
    }
    
//TODO
    public void copyTempFiles(String dir,boolean isPngCopy){
    	 
        String userDir = System.getProperty("user.dir");
        String res_sprite = userDir + "/j2me/TestSprite/ani/";
        
        if(SpriteEditor.mapCrossOn)
         res_sprite = userDir + "/j2me/TestSprite/building/";
    	File spriteFiles=new File(dir);
		File[] sprite_list = spriteFiles.listFiles();
		
		 for(int i=0;i<sprite_list.length;i++)
        {
			 String name = sprite_list[i].getName();
			 String fileDir = dir+name;
			 
			 
			 if(isPngCopy){
				 if(!name.endsWith(".png"))continue;
			 }
			 System.out.println("[copyTempFiles]: "+fileDir);
			 try
			 {
				 copyFile(fileDir, res_sprite + new File(fileDir).getName());
			 }
			 catch (IOException ioException)
			 {
			 }
       }
		 
		 
    }
    
    public boolean isFragmentFile(String name){
    	
    	if(fragmentFilenames==null)return false;
    	for(int i=0;i<fragmentFilenames.length;i++){
    		if(fragmentFilenames[i].equalsIgnoreCase(name))return true;
    	}
    	return false;
    }
    
    private final String jarFileName = "TestSprite.jar";
    private String projectName;
    private int theNthAnimation = -1;
    private String[] fragmentsXMLDir;
    private String[] fragmentsImageDir;
}
