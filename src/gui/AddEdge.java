package gui;

import api.DirectedWeightedGraph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.InvalidParameterException;

/**
 * This panel is the panel that opens when we want to add a new edge to the graph.
 * It control an event that check if the inputs are valid, and if trey are, we will add it to the graph
 * and of course send a command to the GraphDisplay to draw the new edge
 */
public class AddEdge extends JPanel implements ActionListener {
    GMenuBar gmb;
    private JFrame addedgeframe = new JFrame("Add Edge");
    JTextField srctext, desttext, wtext;
    JLabel srclabel, destlabel, wlabel;
    JButton submit;

    AddEdge(GMenuBar gmb) {
        this.gmb = gmb;
        addedgeframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addedgeframe.setBounds(200, 100, 400, 400);

        Container c = addedgeframe.getContentPane();
        c.setLayout(null);

        srclabel = new JLabel("src   :");
        srclabel.setBounds(20, 30, 250, 30);

        srctext = new JTextField();
        srctext.setBounds(80, 30, 250, 30);

        destlabel = new JLabel("dest  :");
        destlabel.setBounds(20, 90, 250, 30);

        desttext = new JTextField();
        desttext.setBounds(80, 90, 250, 30);

        wlabel = new JLabel("weight:");
        wlabel.setBounds(20, 150, 250, 30);

        wtext = new JTextField();
        wtext.setBounds(80, 150, 250, 30);

        submit = new JButton("Submit");
        submit.addActionListener(this);
        submit.setBounds(150, 320, 100, 30);

        c.add(submit);
        c.add(srclabel);
        c.add(srctext);
        c.add(destlabel);
        c.add(desttext);
        c.add(wlabel);
        c.add(wtext);
        addedgeframe.setVisible(true);
    }

    /**
     * This event trigger will check if the inputs are valid, and if they are, it will add it to the graph and
     * draw the new edge, otherwise it will show an error dialogue.
     * @param e Event that happen when the user click on the button
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submit) {
            try {
                DirectedWeightedGraph g = gmb.graph.getGraph();
                int src = Integer.parseInt(srctext.getText());
                int dest = Integer.parseInt(desttext.getText());
                if (g.getNode(src) == null || g.getNode(dest) == null) {
                    throw new IllegalArgumentException();
                }
                if (dest == src) {
                    throw new InvalidParameterException();
                }
                double w = Double.parseDouble(wtext.getText());
                if (w < 0) {
                    throw new ArithmeticException();
                }
                g.connect(src, dest, w);
                gmb.graph.init(gmb.graph.getGraph());
                gmb.gd.Update(gmb.graph);
//                gmb.gd.paint(gmb.gr.getGraphics());
                this.gmb.gd.repaint();
                addedgeframe.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, " The One or more of the inputs are invalid");
            } catch (InvalidParameterException ex) {
                JOptionPane.showMessageDialog(this, " The source and destenation can't be equal");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, "The src or the dest do not exist");
            } catch (ArithmeticException ex) {
                JOptionPane.showMessageDialog(this, "The weight of the edge can't be negative or 0");
            }
        }
    }
}
