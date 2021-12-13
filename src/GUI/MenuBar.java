package GUI;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MenuBar extends JMenuBar implements ActionListener {
    private JMenu file;
    private JMenu edit;
    private JMenu help;
    
    private JMenuItem load;
    private JMenuItem save;
    private JMenuItem exit;
    
    private JFileChooser fileChooser;
    private Canvas canvas;
    private JFrame frame;
    
    public MenuBar() {
        this.file = new JMenu("File");
        this.edit = new JMenu("Edit");
        this.help = new JMenu("Help");
        
        this.load = new JMenuItem("Load");
        this.save = new JMenuItem("Save");
        this.exit = new JMenuItem("Exit");
        
        this.file.add(this.load);
        this.file.add(this.save);
        this.file.add(this.exit);
        
        this.add(this.file);
        this.add(this.edit);
        this.add(this.help);
        
        this.load.addActionListener(this);
        this.save.addActionListener(this);
        this.exit.addActionListener(this);
        
        this.setVisible(true);
    }
    
    
    public MenuBar setCanvas(Canvas canvas) {
        this.canvas = canvas;
        return this;
    }
    
    public MenuBar setFrame(JFrame frame) {
        this.frame = frame;
        return this;
    }
    
    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (this.load.equals(source)) {
            this.fileChooser = new JFileChooser();
            this.fileChooser.setCurrentDirectory(new java.io.File("."));
            this.fileChooser.setDialogTitle("Load");
            this.fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            this.fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.setFileFilter(new FileFilter() {
                public String getDescription() {
                    return "JSON Files (*.json)";
                }
        
                public boolean accept(File f) {
                    if (f.isDirectory()) {
                        return true;
                    } else {
                        String filename = f.getName().toLowerCase();
                        return filename.endsWith(".json");
                    }
                }
            });
            if (this.fileChooser.showOpenDialog(this.frame) == JFileChooser.APPROVE_OPTION) {
                this.canvas.init(this.fileChooser.getSelectedFile().getAbsolutePath());
                this.canvas.repaint();
            } else {
                System.out.println("No Selection ");
            }
        } else if (this.save.equals(source)) {
            System.out.println("Save");
        } else if (this.exit.equals(source)) {
            System.out.println("Exit");
            this.frame.dispose();
            System.exit(0);
        }
    }
}
