package gui;

import datastructures.Node;
import datastructures.Point3D;
import api.NodeData;
import java.awt.Component;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * This panel is the panel that opens when we want to add a new node to the graph.
 * It control an event that check if the inputs are valid, and if trey are, we will add it to the graph
 * and of course send a command to the GraphDisplay to draw the new node
 */
public class AddNode extends JPanel implements ActionListener {
    GMenuBar gmb;
    private JFrame addnodeframe =new JFrame("Add Node");
    JTextField idtext, xtext, ytext, ztext;
    JLabel idlabel, xlabel, ylabel, zlabel;
    JButton submit;
    boolean flag=false;
    AddNode(GMenuBar g){
        this.gmb=g;
        addnodeframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addnodeframe.setBounds(200,100,400,400);

        Container c= addnodeframe.getContentPane();
        c.setLayout(null);

        idlabel=new JLabel("id:");
        idlabel.setBounds(20,30,250,30);

        idtext=new JTextField();
        idtext.setBounds(80,30,250,30);

        xlabel=new JLabel("x  :");
        xlabel.setBounds(20,90,250,30);

        xtext=new JTextField();
        xtext.setBounds(80,90,250,30);

        ylabel=new JLabel("y  :");
        ylabel.setBounds(20,150,250,30);

        ytext=new JTextField();
        ytext.setBounds(80,150,250,30);

        zlabel=new JLabel("z  :");
        zlabel.setBounds(20,210,250,30);

        ztext=new JTextField();
        ztext.setBounds(80,210,250,30);

        submit=new JButton("Submit");
        submit.addActionListener(this);
        submit.setBounds(150,320,100,30);

        c.add(submit);c.add(idlabel);c.add(idtext);c.add(xlabel);c.add(xtext);c.add(ylabel);c.add(ytext);
        c.add(zlabel);c.add(ztext);
        addnodeframe.setVisible(true);
    }
    /**
     * This event trigger will check if the inputs are valid, and if they are, it will add it to the graph and
     * draw the new node, otherwise it will show an error dialogue.
     * @param e Event that happen when the user click on the button
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==submit){
            try{
                int id=Integer.parseInt(idtext.getText());
                if(id<0){
                    throw new NumberFormatException();
                }
                double x=Double.parseDouble(xtext.getText());
                double y=Double.parseDouble(ytext.getText());
                double z=Double.parseDouble(ztext.getText());
                if(gmb.graph.getGraph().getNode(id)!=null){
                    throw new IllegalArgumentException();
                }
                NodeData n=new Node(id,0,new Point3D(x,y,z));
                gmb.graph.getGraph().addNode(n);
                gmb.graph.init(gmb.graph.getGraph());
                gmb.gd.Update(gmb.graph);
//                gmb.gd.paint(gmb.gr.getGraphics());
                this.gmb.gd.repaint();
                this.addnodeframe.dispose();
            }
            catch (NumberFormatException ex){
                JOptionPane.showMessageDialog(this," The One or more of the inputs are invalid");
            }
            catch (IllegalArgumentException ex){
                JOptionPane.showMessageDialog(this,"The id you entered already exist");
            }
        }
    }
}
