package gui;

import api.DirectedWeightedGraph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * This panel is the panel that opens when we want to remove an  node from the graph.
 * It control an event that check if the inputs are valid, and if trey are, we will remove it from the graph
 * and of course send a command to the GraphDisplay to remove the node
 */
public class RemoveNode extends JPanel implements ActionListener {
    GMenuBar gmb;
    private JFrame nrframe = new JFrame("Remove Node");
    JTextField idtext;
    JButton submit;

    RemoveNode(GMenuBar g) {
        this.gmb = g;
        nrframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        nrframe.setBounds(200, 100, 400, 200);

        Container c = nrframe.getContentPane();
        c.setLayout(null);

        JLabel file = new JLabel("Node's id:");
        file.setBounds(20, 30, 250, 30);

        idtext = new JTextField();
        idtext.setBounds(80, 30, 250, 30);

        submit = new JButton("Submit");
        submit.addActionListener(this);
        submit.setBounds(150, 90, 100, 30);

        c.add(submit);
        c.add(idtext);
        c.add(file);
        nrframe.setVisible(true);
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
                int id = Integer.parseInt(idtext.getText());
                DirectedWeightedGraph g = gmb.graph.getGraph();
                if (g.getNode(id) == null) {
                    throw new IllegalArgumentException();
                }
                g.removeNode(id);
                gmb.graph.init(gmb.graph.getGraph());
                gmb.gd.Update(gmb.graph);
//                gmb.gd.paint(gmb.gr.getGraphics());
                this.gmb.gd.repaint();
                nrframe.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, " The id you entered is invalid");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, "The node you wanted to remove doesn't exist");
            }
        }
    }
}
