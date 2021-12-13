package gui;

import javax.swing.*;
import java.awt.*;

/**
 * This Panel introduce out GUI to the user in order to inform him about the functionality and the special
 * features of the GUI, and of course to let him know that he can open it whenever he want by click on 'h'.
 */
public class Help extends JPanel {
    JFrame helpframe=new JFrame("Help");
    Help(){
        helpframe.setLocationRelativeTo(null);
        helpframe.setSize(400, 500);
        helpframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Font Dfont = new Font(Font.SANS_SERIF, Font.BOLD, 20);
        JLabel title=new JLabel("Welcome to our GUI!");
        title.setBounds(90, 20, 250, 30);
        title.setFont(Dfont);

        Dfont=new Font(Font.MONOSPACED, Font.LAYOUT_LEFT_TO_RIGHT, 14);
        JTextArea copyright=new JTextArea("This GUI created for the assignment of "
        +"Ofri Tavor and Nir Sasson.");
        copyright.setBounds(5,90, 400, 20);
        copyright.setLineWrap(true);
        copyright.setWrapStyleWord(true);
        copyright.setOpaque(false);
        copyright.setEditable(false);

        JTextArea info=new JTextArea("There are few things we want you to know before diving into the graph visualisation."
        +" The File menu contains the option to create a new, empty graph, load a graph or save the current graph to json "
        +"file so you can load it later. The basic graph edit options are availble in the Edit menu, you can add or remove nodes "
        +"and edges from the graph, and you can even zoom in and out the screen in order to see the graph better. "
        +"In the Algorithms menu you can find every algorithm you can run on the graph, just click on the algorithm you want. "
        +"Before you go, we want you to know that you can zoom in and out using the up and down arrows as well. "
        +"There are 3 modes in this Gui: Light Mode which is the classic mode with black nodes and edges, "
                +"Color Selection Mode, which allow you to choose the color of the Graph, and the most special "
                +"one, which is the Color Frenzy, which paint the nodes and edges with random colors. Have Fun");
        info.setBounds(5,110, 350, 200);
        info.setLineWrap(true);
        info.setWrapStyleWord(true);
        info.setOpaque(false);
        info.setEditable(false);

        Dfont=new Font(Font.MONOSPACED, Font.LAYOUT_LEFT_TO_RIGHT, 18);

        JTextArea again=new JTextArea("In order to open this panel again, just click on 'h' button");
        again.setBounds(5,400, 380, 50);
        again.setLineWrap(true);
        again.setWrapStyleWord(true);
        again.setOpaque(false);
        again.setEditable(false);
        again.setFont(Dfont);
        again.setForeground(Color.RED);

        Container c=helpframe.getContentPane();
        c.setLayout(null);

        c.add(title);
        c.add(copyright);
        c.add(info);
        c.add(again);
        helpframe.setVisible(true);
    }
}
