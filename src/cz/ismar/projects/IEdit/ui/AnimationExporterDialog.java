package cz.ismar.projects.IEdit.ui;

import cz.ismar.projects.IEdit.SpriteEditor;
import cz.ismar.projects.IEdit.io.AnimationExporter;
import cz.ismar.projects.IEdit.io.ExporterListener;
import cz.ismar.projects.IEdit.structure.FrameFragmentMasks;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.NumberFormatter;

public class AnimationExporterDialog
{

    public AnimationExporterDialog()
    {
        exportDir = new File("./");
    }

    public void createGui()
    {
        dialog = new JDialog(SpriteEditor.mainFrame, "Animation Export", true);
        animationExporter = new AnimationExporter();
        animationExporter.setAnimationExporterListener(new ExporterListener() {

            public void exportUpdated(int eventId, int percent)
            {
                switch(eventId)
                {
                case EVENT_STOPED: 
                case EVENT_ENDED: 
                    setExporting(false);
                    break;

                case EVENT_STARTED: 
                    setExporting(true);
                    break;
                }
                progressBar.setValue(percent);
                dialog.repaint();
            }

        });
        dialog.setDefaultCloseOperation(0);
        dialog.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent windowevent)
            {
                dispose();
            }

        });
        dialog.setFocusable(true);
        dialog.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent keyevent)
            {
                if(keyevent.getKeyCode() == 27)
                {
                	dispose();
                }
            }

        });
        doneButton = new JButton("OK");
        doneButton.setMnemonic('O');
        doneButton.setToolTipText("[Enter]");
        doneButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                dispose();
            }

        });
        exportButton = new JButton("Export");
        exportButton.setMnemonic('E');
        exportButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                doExport();
            }

        });
        stopButton = new JButton("Stop");
        stopButton.setMnemonic('S');
        stopButton.setEnabled(false);
        stopButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                animationExporter.stopExport();
            }

        });
        final JLabel fileNameLabel = new JLabel(exportDir.getAbsolutePath());
        dirButton = new JButton("Change");
        dirButton.setMnemonic('C');
        dirButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                JFileChooser jfilechooser = SpriteEditor.getDirChooser();
                int i = jfilechooser.showOpenDialog(dialog);
                if(i == 0)
                {
                    exportDir = jfilechooser.getSelectedFile();
                    fileNameLabel.setText(exportDir.getAbsolutePath());
                }
            }

        });
        JPanel jpanel = new JPanel(new FlowLayout(0));
        jpanel.setBorder(BorderFactory.createTitledBorder("Export Dir:"));
        jpanel.add(dirButton);
        jpanel.add(fileNameLabel);
        NumberFormat numberformat = NumberFormat.getIntegerInstance();
        final JFormattedTextField wField = new JFormattedTextField(new NumberFormatter(numberformat));
        wField.setColumns(4);
        wField.setValue(new Integer(animationExporter.getBufferImage().getWidth()));
        final JFormattedTextField hField = new JFormattedTextField(new NumberFormatter(numberformat));
        hField.setValue(new Integer(animationExporter.getBufferImage().getHeight()));
        hField.setColumns(4);
        wField.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                int i = Integer.parseInt(wField.getValue().toString());
                if(i < 1 || i > 1000)
                {
                    wField.setValue(new Integer(animationExporter.getBufferImage().getWidth()));
                } else
                {
                    int j = Integer.parseInt(hField.getValue().toString());
                    setExportImageSize(i, j);
                    dialog.repaint();
                }
            }

        });
        hField.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                int i = Integer.parseInt(hField.getValue().toString());
                if(i < 1 || i > 1000)
                {
                    hField.setValue(new Integer(animationExporter.getBufferImage().getHeight()));
                } else
                {
                    int j = Integer.parseInt(wField.getValue().toString());
                    setExportImageSize(j, i);
                    dialog.repaint();
                }
            }

        });
        final JSpinner scaleSpinner = new JSpinner(new SpinnerNumberModel(animationExporter.getExportScale(), 1, 10, 1));
        scaleSpinner.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent changeevent)
            {
                int i = Integer.parseInt(scaleSpinner.getValue().toString());
                animationExporter.setExportScale(i);
                dialog.repaint();
            }

        });
        masksComboBox = new JComboBox(SpriteEditor.frameFragmentMasks.getComboBoxModel());
        masksComboBox.setEnabled(SpriteEditor.frameFragmentMasks.isEnabled());
        masksComboBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                dialog.repaint();
            }

        });
        JPanel jpanel1 = new JPanel();
        jpanel1.setBorder(BorderFactory.createTitledBorder("Mask"));
        jpanel1.setLayout(new BorderLayout());
        jpanel1.add(masksComboBox, "Center");
        JPanel jpanel2 = new JPanel(new FlowLayout(1));
        jpanel2.setBorder(BorderFactory.createTitledBorder("Image parameters:"));
        jpanel2.add(new JLabel("w:"));
        jpanel2.add(wField);
        jpanel2.add(new JLabel("h:"));
        jpanel2.add(hField);
        jpanel2.add(new JLabel("scale:"));
        jpanel2.add(scaleSpinner);
        JPanel jpanel3 = new JPanel();
        jpanel3.setLayout(new BoxLayout(jpanel3, 0));
        jpanel3.add(jpanel2);
        jpanel3.add(jpanel1);
        JPanel jpanel4 = new JPanel();
        jpanel4.setLayout(new BoxLayout(jpanel4, 1));
        jpanel4.add(jpanel);
        jpanel4.add(jpanel3);
        JPanel jpanel5 = new JPanel(new FlowLayout(1));
        jpanel5.add(doneButton);
        jpanel5.add(exportButton);
        jpanel5.add(stopButton);
        jpanel5.add(exportButton);
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        JPanel jpanel6 = new JPanel(new FlowLayout(1));
        jpanel6.setBorder(BorderFactory.createTitledBorder("Export progress:"));
        jpanel6.add(progressBar);
        JPanel jpanel7 = new JPanel();
        jpanel7.setLayout(new BoxLayout(jpanel7, 1));
        jpanel7.add(jpanel6);
        jpanel7.add(jpanel5);
        JPanel jpanel8 = new JPanel() {

            public void paintComponent(Graphics g)
            {
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, getWidth(), getHeight());
                int i = animationExporter.getBufferImage().getWidth();
                int j = animationExporter.getBufferImage().getHeight();
                int k = (getWidth() - i) / 2;
                int l = (getHeight() - j) / 2;
                g.setColor(Color.BLACK);
                g.drawRect(k - 1, l - 1, i + 2, j + 2);
                g.drawImage(animationExporter.getBufferImage(), k, l, null);
            }

            public Dimension getPreferredSize()
            {
                return new Dimension(animationExporter.getBufferImage().getWidth(), animationExporter.getBufferImage().getHeight());
            }

        };
        JPanel jpanel9 = new JPanel(new BorderLayout(10, 10));
        JPanel jpanel10 = new JPanel();
        jpanel10.setBorder(BorderFactory.createTitledBorder("Preview:"));
        jpanel10.add(new JScrollPane(jpanel8));
        jpanel9.add(jpanel10, "Center");
        jpanel9.add(jpanel4, "North");
        jpanel9.add(jpanel7, "South");
        dialog.getContentPane().add(jpanel9);
        dialog.pack();
        dialog.setVisible(true);
    }

    private void setExportImageSize(int i, int j)
    {
        animationExporter.setExportImgSize(i, j);
    }

    private void doExport()
    {
        (new Thread() {

            public void run()
            {
                try
                {
                    animationExporter.export(exportDir);
                }
                catch(IOException ioexception)
                {
                    ioexception.printStackTrace();
//                    System.out.println("doExport Error");
                }
            }

        }).start();
    }

    private synchronized void setExporting(boolean flag)
    {
        if(flag != isExporting)
        {
            doneButton.setEnabled(!flag);
            exportButton.setEnabled(!flag);
            dirButton.setEnabled(!flag);
            stopButton.setEnabled(flag);
        }
        isExporting = flag;
    }

    private synchronized void dispose()
    {
        if(!isExporting)
            dialog.dispose();
    }

    private AnimationExporter animationExporter;
    private boolean isExporting;
    private File exportDir;
    private JButton doneButton;
    private JButton exportButton;
    private JButton stopButton;
    private JButton dirButton;
    private JProgressBar progressBar;
    private JComboBox masksComboBox;
    private JDialog dialog;









}
