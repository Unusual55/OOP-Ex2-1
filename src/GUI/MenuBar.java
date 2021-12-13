package GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuBar extends JMenuBar implements ActionListener {
    private JMenu file;
    private JMenu edit;
    private JMenu help;
    
    private JMenuItem load;
    private JMenuItem save;
    private JMenuItem exit;
    
    private JFileChooser fileChooser;
    private Graph graph;
    private Canvas canvas;
    private JFrame frame;
    
    public MenuBar() {
        this.file = new JMenu("File");
        this.edit = new JMenu("Edit");
        this.help = new JMenu("Help");
        
        this.load = new JMenuItem("Load");
        this.save = new JMenuItem("Save");
        this.exit = new JMenuItem("Exit");
        
        this.file.add(load);
        this.file.add(save);
        this.file.add(exit);
        
        this.add(file);
        this.add(edit);
        this.add(help);
        
        this.setVisible(true);
    }
    
    public MenuBar setGraph(Graph graph) {
        this.graph = graph;
        return this;
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
        if (e.getSource() == this.load) {
        
        }
    }
}
