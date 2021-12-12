package gui;

import api.NodeData;
import api.DirectedWeightedGraphAlgorithms;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * This class represent the menu bar in the main panel.
 * It contains JMenus and JMenuItems which are the options the user have.
 * It controls the events that will happen if the user will click on the JMenuItem
 */
public class GMenuBar extends JMenuBar implements ActionListener {
    JMenu file;
    JMenu edit;
    JMenu algo;
    JMenuItem load;
    JMenuItem ng;
    JMenuItem save;
    JMenuItem addnode;
    JMenuItem addedge;
    JMenuItem removenode;
    JMenuItem removeedge;
    JMenuItem iscon;
    JMenuItem shortdist;
    JMenuItem shortpath;
    JMenuItem center;
    JMenuItem tsp;
    JMenuItem zoomin;
    JMenuItem zoomout;
    AddNode anp;
    AddEdge aep;
    RemoveEdge rep;
    RemoveNode rnp;
    JFileChooser fileChooser;
    DirectedWeightedGraphAlgorithms graph;
    GraphDisplay gd;
    Graph gr;
    JMenuItem resetalgo;
    JMenu help;
    JMenuItem gethelp;
    JMenu modes;
    JMenuItem lightmode;
    JMenuItem colormode;
    JMenuItem colorfrenzy;

    GMenuBar(DirectedWeightedGraphAlgorithms graph, GraphDisplay gd, Graph gp) {
        this.gr = gp;
        this.gd = gd;
        fileChooser = new JFileChooser(new File(System.getProperty("user.dir")));
        file = new JMenu("File");
        edit = new JMenu("Edit");
        algo = new JMenu("Algorithms");
        modes=new JMenu("Modes");
        help=new JMenu("Help");

        load = new JMenuItem("Load");
        load.addActionListener(this);
        ng = new JMenuItem("New GGraph");
        ng.addActionListener(this);
        save = new JMenuItem("Save");
        save.addActionListener(this);

        addnode = new JMenuItem("Add Node");
        addnode.addActionListener(this);
        addedge = new JMenuItem("Add Edge");
        addedge.addActionListener(this);
        removenode = new JMenuItem("Remove Node");
        removenode.addActionListener(this);
        removeedge = new JMenuItem("Remove Edge");
        removeedge.addActionListener(this);
        zoomin =new JMenuItem("Zoom In");
        zoomin.addActionListener(this);
        zoomout=new JMenuItem("Zoom Out");
        zoomout.addActionListener(this);

        iscon = new JMenuItem("is Connected");
        iscon.addActionListener(this);
        shortdist = new JMenuItem("Shortest path distance");
        shortdist.addActionListener(this);
        shortpath = new JMenuItem("Shortest path");
        shortpath.addActionListener(this);
        center = new JMenuItem("Center");
        center.addActionListener(this);
        tsp = new JMenuItem("TSP");
        tsp.addActionListener(this);
        resetalgo=new JMenuItem("Reset Algorithm");
        resetalgo.addActionListener(this);
        if(this.gd.drawflags[0]){
            resetalgo.setEnabled(false);
        }
        else{
            resetalgo.setEnabled(true);
        }

        lightmode=new JMenuItem("Light Mode");
        lightmode.addActionListener(this);
        lightmode.setBackground(Color.white);

        colorfrenzy=new JMenuItem("Color Frenzy!");
        colorfrenzy.addActionListener(this);

        colormode=new JMenuItem("Color Mode");
        colormode.addActionListener(this);

        gethelp=new JMenuItem("Help!");
        gethelp.addActionListener(this);

        file.add(load);
        file.add(ng);
        file.add(save);

        edit.add(addnode);
        edit.add(addedge);
        edit.add(removeedge);
        edit.add(removenode);
        edit.add(zoomin);
        edit.add(zoomout);

        algo.add(iscon);
        algo.add(shortdist);
        algo.add(shortpath);
        algo.add(center);
        algo.add(tsp);
        algo.add(resetalgo);

        modes.add(lightmode);
        modes.add(colormode);
        modes.add(colorfrenzy);

        help.add(gethelp);

        this.graph = graph;
        this.add(file);
        this.add(edit);
        this.add(algo);
        this.add(modes);
        this.add(help);
        this.setVisible(true);
    }

    /**
     * This fucntion controls the events in this class
     * every condition represent an event that a JMenuItem was clicked, each JMenuItem trigger a different event
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        /**
         * If the New Graph menu item was clicked, we will create a new empty graph
         */
        if (e.getSource() == ng) {
            this.gd.resetGraphDisplay();
            this.gd.repaint();
        }
        /**
         * If the Load Graph menu item was clicked, we will load a graph from json file, using JFileChooser
         */
        else if (e.getSource() == load) {
//            lp=new GuiPanels.Load();

            this.fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Json files", "json"));
            this.fileChooser.showOpenDialog(null);
            if (this.fileChooser.getSelectedFile() == null) {
                return;
            } else {
                this.gd.resetGraphDisplay();
                this.graph.load(this.fileChooser.getSelectedFile().getPath());
                this.gd.Update(this.graph);
//                this.gd.paint(this.gr.getGraphics());
                this.gd.repaint();
            }
        }
        /**
         * If the Save Graph menu item was clicked, we will save the current graph to json file
         * If we didn't load any file and escaped the file chooser panel, the event will end and nothing
         * will happen, but if we will load a valid json file, the GraphDisplay will draw it
         */
        else if (e.getSource() == save) {
            this.fileChooser.showSaveDialog(null);
            if (this.fileChooser.getSelectedFile() == null) {
                return;
            } else {
                this.graph.save(this.fileChooser.getSelectedFile().getPath());
            }
            /**
             * if the Add Node menu item was clicked, a panel which the user can add nodes will open.
             */
        } else if (e.getSource() == addnode) {
            anp = new AddNode(this);
            /**
             * if the Add Edge menu item was clicked, a panel which the user can add edges will open.
             */
        } else if (e.getSource() == addedge) {
            aep = new AddEdge(this);
            /**
             * if the Remove Node menu item was clicked, a panel which the user can remove nodes will open.
             */
        } else if (e.getSource() == removenode) {
            rnp = new RemoveNode(this);
            /**
             * if the Remove Edge menu item was clicked, a panel which the user can remove edges will open.
             */
        } else if (e.getSource() == removeedge) {
            rep = new RemoveEdge(this);
        } else if (e.getSource() == center) {
            NodeData n=this.graph.center();
            JOptionPane.showMessageDialog(this, "The center node id is: "+n.getKey());
            this.gd.setCenterid(n.getKey());
            this.gd.setAlgoMode();
            this.gd.repaint();
        } else if (e.getSource() == shortdist) {
            int src = -1, dest = -1;
            String srcop = JOptionPane.showInputDialog(this, " Enter an source");
            String destop = JOptionPane.showInputDialog(this, "Enter a destenation");
            try {
                src = Integer.parseInt(srcop);
                dest = Integer.parseInt(destop);
                if (this.graph.getGraph().getNode(src) == null || this.graph.getGraph().getNode(dest) == null) {
                    throw new IllegalArgumentException();
                }
                JOptionPane.showMessageDialog(this, "The shortest distance between "
                +src+" and "+dest+" is: "+this.graph.shortestPathDist(src,dest));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "You entered an invalid node id.");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, "One or both of the inputs doesn't exist in the graph");
            }
        } else if (e.getSource() == shortpath) {
            int src = -1, dest = -1;
            String srcop = JOptionPane.showInputDialog(this, " Enter an source");
            String destop = JOptionPane.showInputDialog(this, "Enter a destenation");
            try {
                src = Integer.parseInt(srcop);
                dest = Integer.parseInt(destop);
                if (this.graph.getGraph().getNode(src) == null || this.graph.getGraph().getNode(dest) == null) {
                    throw new IllegalArgumentException();
                }
                ArrayList<NodeData> SPath= (ArrayList<NodeData>) this.graph.shortestPath(src,dest);
                this.gd.CreateMarkedEdgesSP(SPath);
                this.gd.setAlgoMode();
                this.gd.repaint();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "You entered an invalid node id.");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, "One or both of the inputs doesn't exist in the graph");
            }
        } else if (e.getSource() == iscon) {
            if(this.graph.isConnected()){
                JOptionPane.showMessageDialog(this, "The graph is connected");
            }
            else{
                JOptionPane.showMessageDialog(this, "The graph is not connected");
            }
        } else if (e.getSource() == tsp) {
            LinkedList<NodeData> sendtoTSP = tspInputTraslator();
            LinkedList<NodeData> outtsplist= (LinkedList<NodeData>) this.graph.tsp(sendtoTSP);
            this.gd.CreateMarkedEdgesTSP((outtsplist));
            this.gd.setAlgoMode();
            this.gd.repaint();
            if (sendtoTSP.size() == 0) {
                JOptionPane.showMessageDialog(this, "You didn't enter any valid input, therefore nothing will happen.");
            } else {
            }
        }
        else if(e.getSource()==zoomin){
            try{
                String input=JOptionPane.showInputDialog("Enter how much you want to zoom in.");
                int count=Integer.parseInt(input);
                if(count==0){
                    JOptionPane.showMessageDialog(this, "You entered 0, therefore nothing will happen");
                    return;
                }
                else if(count<0){
                    JOptionPane.showMessageDialog(this, "You entered negative integer, therefore nothing will happen");
                    return;
                }
                this.gd.Scale+=0.002*count;
//                this.gd.paint(this.gr.getGraphics());
                this.gd.repaint();
            }
            catch (NumberFormatException ex){
                JOptionPane.showMessageDialog(this, "The input you enterd is not a valid Integer");
            }
        }
        else if(e.getSource()==zoomout){
            try{
                String input=JOptionPane.showInputDialog("Enter how much you want to zoom in.");
                int count=Integer.parseInt(input);
                if(count==0){
                    JOptionPane.showMessageDialog(this, "You entered 0, therefore nothing will happen");
                    return;
                }
                else if(count<0){
                    JOptionPane.showMessageDialog(this, "You entered negative integer, therefore nothing will happen");
                    return;
                }
                this.gd.Scale-=0.002*count;
                this.gd.repaint();
            }
            catch (NumberFormatException ex){
                JOptionPane.showMessageDialog(this, "The input you enterd is not a valid Integer");
            }
        }
        else if(e.getSource()==gethelp){
            Help h=new Help();
        }
//        else if(e.getSource()==resetalgo){
//            this.gd.setRegularFlag();
//        }
        else if(e.getSource()==lightmode){
            this.gd.setLightMode();
            this.gd.repaint();
        }
        else if(e.getSource()==colormode){
            Color c=JColorChooser.showDialog(this, "Select color for the node and edges",Color.black);
            this.gd.setColorMode(c);
            this.gd.repaint();
        }
        else if(e.getSource()==colorfrenzy){
            this.gd.setColorFrenzyMode();
            this.gd.repaint();
        }
    }

    /**
     * This fucntion get inputs from the user in order to create a list of NodeData objects for the tsp function.
     * The function check if the input is valid as an integer and if it's a real id of a node.
     * We use HashSet of integers that we call selector in order to prevent the option that the user will enter
     * the same id few time.
     * @return A LinkedList that contain the NodeData for the tsp, if the list is empty, we will alert the user
     * and we won't use the tsp function.
     */
    public LinkedList<NodeData> tspInputTraslator() {
        HashSet<Integer> selector = new HashSet<>();
        LinkedList<NodeData> valid = new LinkedList<>();
        String data = "";
        while (true) {
            try {
                data = JOptionPane.showInputDialog(this, "Enter an id of node for the tsp.\n"
                        + "in order to leave enter q. invalid iputs won't enter the list");
                if (data==null||data == "") {
                    break;
                }
                if (data.equals("q") || data.equals("Q")) {
                    break;
                }
                int id = Integer.parseInt(data);
                if (this.graph.getGraph().getNode(id) == null) {
                    JOptionPane.showMessageDialog(this, "The node is not exist");
                } else if (selector.contains(id) == false) {
                    valid.add(this.graph.getGraph().getNode(id));
                    selector.add(id);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "The input is not valid, enter a valid\n"
                        + "input or enter q or Q if you want to finish");
            }
        }
        return valid;
    }
}
