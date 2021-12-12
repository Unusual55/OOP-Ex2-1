package gui;

import api.DirectedWeightedGraph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.InvalidParameterException;
/**
 * This panel is the panel that opens when we want to remove an  edge from the graph.
 * It control an event that check if the inputs are valid, and if trey are, we will remove it from the graph
 * and of course send a command to the GraphDisplay to remove the edge
 */
public class RemoveEdge extends JPanel implements ActionListener {
    GMenuBar gmb;
    private JFrame removeEdgeframe = new JFrame("Remove Edge");
    JTextField srctext, desttext;
    JLabel srclabel, destlabel;
    JButton submit;

    RemoveEdge(GMenuBar g) {
        this.gmb = g;
        removeEdgeframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        removeEdgeframe.setBounds(200, 100, 400, 200);

        Container c = removeEdgeframe.getContentPane();
        c.setLayout(null);

        srclabel = new JLabel("src   :");
        srclabel.setBounds(20, 30, 250, 30);

        srctext = new JTextField();
        srctext.setBounds(80, 30, 250, 30);

        destlabel = new JLabel("dest  :");
        destlabel.setBounds(20, 80, 250, 30);

        desttext = new JTextField();
        desttext.setBounds(80, 80, 250, 30);

        submit = new JButton("Submit");
        submit.addActionListener(this);
        submit.setBounds(150, 120, 100, 30);

        c.add(submit);
        c.add(srclabel);
        c.add(srctext);
        c.add(destlabel);
        c.add(desttext);
        removeEdgeframe.setVisible(true);
    }
    /**
     * This event trigger will check if the inputs are valid, and if they are, it will remove it from the graph and
     * remove it from the visual graph, otherwise it will show an error dialogue.
     * @param e Event that happen when the user click on the button
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submit) {
            try {
                DirectedWeightedGraph g = gmb.graph.getGraph();
                int src = Integer.parseInt(srctext.getText());
                int dest = Integer.parseInt(desttext.getText());
                if (g.getNode(src) == null || g.getNode(dest) == null || src == dest) {
                    throw new IllegalArgumentException();
                }
                if (g.getEdge(src, dest) == null) {
                    throw new NullPointerException();
                }
                g.removeEdge(src, dest);
                gmb.graph.init(gmb.graph.getGraph());
                gmb.gd.Update(gmb.graph);
//                gmb.gd.paint(gmb.gr.getGraphics());
                this.gmb.gd.repaint();
                removeEdgeframe.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, " The One or more of the inputs are invalid");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, " The source and destenation can't be equal\n" +
                        "or maybe one or both of them doesn't exist");
            } catch (NullPointerException ex) {
                JOptionPane.showMessageDialog(this, "The edge you wanted to delete doesn't exist");
            }
        }
    }
}
