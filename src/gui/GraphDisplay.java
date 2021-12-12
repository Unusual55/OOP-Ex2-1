package gui;

import api.*;
import datastructures.DWGraph;
import datastructures.Point3D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.*;
import java.awt.Image;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class is the most important class in out GUI.
 * From this class we control the whole visual graph.
 * The GraphDisplay recive it's commands from the GMenuBar, which check if the inputs are valid and add or remove
 * the nodes or edges from the DirectedWeightedGraph we have behind the scenes.
 * The GraphDisplay use paint and paintcomponent methods to control the draw and redraw actions in the graph,
 * anytime a change in the group will occur, this class will create a bufferimage- a special image created behind
 * the scenes which contain the graph: nodes and edges, and when it's ready it will be revealed to the user.
 * Special cases of nodes and edges will be handled differently from the casual graph:
 * 1) Center: The center node will be marked by red color.
 * 2) Shortest Path: The nodes in the path will be marked by green color, and the edges by red color
 * 3) TSP: The nodes in the selected path will be marked by magenta color, and the edges by orange color
 * Special Thanks to our friend, Student from the class Amir Sabag, who helped us alot throughout the GUI.
 */
public class GraphDisplay extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener {
    HashSet<Integer> markednodesSP;
    HashMap<Integer, Integer> markededgesSP;
    HashSet<Integer> markednodesTSP;
    HashMap<Integer, Integer> markededgesTSP;
    DirectedWeightedGraphAlgorithms graphAlgorithms;
    DirectedWeightedGraph graph;
    double width;
    double height;
    double Wedge;
    double Hedge;
    double WNode;
    double HNode;
    double[] BoundingBox;
    double Scale;
    Graphics g;
    int centerid;
    Stroke Estroke;
    Stroke Nstroke;
    final Color Ecolor = Color.black;
    Color Ncolor;
    Font Dfont;
    Point2D mousep;
    Point2D mouseprev;
    Point2D mousenext;
    Graph gr;
    boolean[] drawflags;
    Color selectedColor;
    boolean[] algoflags;

    GraphDisplay(DirectedWeightedGraphAlgorithms g, Graph gr) {
        algoflags=new boolean[]{false,false,false};
        this.selectedColor=Color.BLACK;
        drawflags= new boolean[]{ true, false,false,false};
        /**
         * Boolean array which represent 2 combination of vector: false=0, true-1
         * (1,0): regular edges draw- the colors will be random and will be drawn using threads
         * (0,1): special edges draw, we will use it for Algorithms.
         */
        this.gr = gr;
        this.graphAlgorithms = g;
        this.graph = g.getGraph();
        this.setBounds(0,0,gr.getWidth(),gr.getHeight());
        markednodesSP = new HashSet<>();
        markededgesSP = new HashMap<>();
        markednodesTSP = new HashSet<>();
        markededgesTSP = new HashMap<>();
        this.centerid = -1;
        this.width=800;
        this.height=600;
        Dfont = new Font(Font.SANS_SERIF, Font.BOLD, 10);
        Estroke = new BasicStroke((float) 1.5);
        Nstroke = new BasicStroke((float) 5);
        this.Scale=1;
        Ncolor = Color.BLUE;
        this.Scale = 1;
        this.Wedge = 5;
        this.Hedge = 5;
        this.HNode = 8;
        this.WNode = 8;
        this.BoundingBox = new double[4];
        this.mousep = new Point(0, 0);
        this.mousenext = new Point(0, 0);
        this.mouseprev = new Point(0, 0);
        if(this.graphAlgorithms.getGraph().nodeSize()>0) {
            SetBoundingBox();
        }
//        else {
//            SingleNodeboundingBox();
//        }
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);
    }

    /**
     * Credit for the arrow creating function to https://gist.github.com/raydac/df97493f58b0521fb20a
     *
     * @param gfx
     * @param start
     * @param end
     * @param lineStroke
     * @param arrowStroke
     * @param arrowSize
     */
    public void drawArrow(final Graphics2D gfx, final Point2D start, final Point2D end, final Stroke lineStroke, final Stroke arrowStroke, final float arrowSize) {

        final double startx = start.getX();
        final double starty = start.getY();

        gfx.setStroke(arrowStroke);
        final double deltax = startx - end.getX();
        final double result;
        if (deltax == 0.0d) {
            result = Math.PI / 2;
        } else {
            result = Math.atan((starty - end.getY()) / deltax) + (startx < end.getX() ? Math.PI : 0);
        }

        final double angle = result;

        final double arrowAngle = Math.PI / 12.0d;

        final double x1 = arrowSize * Math.cos(angle - arrowAngle);
        final double y1 = arrowSize * Math.sin(angle - arrowAngle);
        final double x2 = arrowSize * Math.cos(angle + arrowAngle);
        final double y2 = arrowSize * Math.sin(angle + arrowAngle);

        final double cx = (arrowSize / 2.0f) * Math.cos(angle);
        final double cy = (arrowSize / 2.0f) * Math.sin(angle);

//        gfx.setColor(Color.RED);
        final GeneralPath polygon = new GeneralPath();
        polygon.moveTo(end.getX(), end.getY());
        polygon.lineTo(end.getX() + x1, end.getY() + y1);
        polygon.lineTo(end.getX() + x2, end.getY() + y2);
        polygon.closePath();
        gfx.fill(polygon);

//        gfx.setColor(Color.BLACK);
        gfx.setStroke(lineStroke);
        gfx.drawLine((int) startx, (int) starty, (int) (end.getX() + cx), (int) (end.getY() + cy));
    }

    /**
     * Credit to Amir Sabag, student from the class, for the formula
     *This function recieve the GeoLocation of p, and match it so it will suit to the panel size using
     * Linear Transformation.
     * @param p Position
     * @return The new coordinates.
     */
    public double[] CoordinatesTransformation(GeoLocation p) {
        double dpx = this.BoundingBox[2] - this.BoundingBox[0];
        double dpy = this.BoundingBox[3] - this.BoundingBox[1];
        double dcx = this.BoundingBox[2] - p.x();
        double dcy = this.BoundingBox[3] - p.y();
        double xfixed=(dcx/dpx*this.width*0.8+this.height*0.1+this.mousep.getX())*this.Scale;
        double yfixed=(dcy/dpy*this.width*0.8+this.height*0.1+this.mousep.getY())*this.Scale;
        return new double[]{xfixed, yfixed};
    }

    public double[] CoordinatesTransformation(Point2D p) {
        double dpx = this.BoundingBox[2] - this.BoundingBox[0];
        double dpy = this.BoundingBox[3] - this.BoundingBox[1];
        double dcx = this.BoundingBox[2] - p.getX();
        double dcy = this.BoundingBox[3] - p.getY();
        double xfixed=(dcx/dpx*this.width+this.height+mousep.getX())*this.Scale;
        double yfixed=(dcy/dpy*this.width+this.height+mousep.getY())*this.Scale;
        System.out.println(xfixed+ ", " + yfixed);
        return new double[]{xfixed, yfixed};
    }

    /**
     * This function create a bounding box using the iterators.
     * The function check which values from all of the nodes positions are the minimal and maximal X and Y values
     * At the end, it sets those values to an array that keep track of them.
     */
    public void SetBoundingBox() {
        Iterator<NodeData> nodeiter = graph.nodeIter();
        NodeData curr = null;
        double Xmin = 0, Xmax = 0, Ymin = 0, Ymax = 0;
        if (nodeiter.hasNext()) {
            curr = nodeiter.next();
            Xmin = curr.getLocation().x();
            Xmax = curr.getLocation().x();
            Ymin = curr.getLocation().y();
            Ymax = curr.getLocation().y();
        }
        while (nodeiter.hasNext()) {
            curr = nodeiter.next();
            Xmin = Math.min(Xmin, curr.getLocation().x());
            Xmax = Math.max(Xmax, curr.getLocation().x());
            Ymin = Math.min(Ymin, curr.getLocation().y());
            Ymax = Math.max(Ymax, curr.getLocation().y());
        }
        BoundingBox[0] = Xmin;
        BoundingBox[1] = Ymin;
        BoundingBox[2] = Xmax;
        BoundingBox[3] = Ymax;
        if(this.graph.nodeSize()<=3){
            BoundingBox[0]-=10;
            BoundingBox[1]-=10;
            BoundingBox[2]+=10;
            BoundingBox[3]+=10;
        }
    }

    /**
     * This function reset the bounding box of the GraphDisplay. We will use this function if and only if the graph
     * contain no nodes or only onre node, otherwise we will use the regular setBoundingBox
     */
    public void SingleNodeboundingBox(){
        BoundingBox[0] = 0;
        BoundingBox[1] = 0;
        BoundingBox[2] = 800;
        BoundingBox[3] = 600;
    }

    /**
     * This function draw the nodes. As we mentioned earlier, we consider special cases of nodes that we want
     * to mark if we used specific algorithm.
     * @param graphics
     */
    public void DrawNodesColorSelection(Graphics graphics) {
        Graphics2D g2d = (Graphics2D) graphics;
        g2d.setColor(this.Ncolor);
        Iterator<NodeData> nodeiter = this.graph.nodeIter();
        HashMap<Integer, ColorChooseModeDrawEdges> drawedges=new HashMap<>();
        NodeData curr = null;
        Dfont = new Font(Font.SANS_SERIF, Font.BOLD, (int) (10+this.Scale));
        g2d.setFont(Dfont);
        this.HNode=3;
        this.WNode=3;
        this.Hedge=5;
        this.Wedge=5;
        ExecutorService pool= Executors.newFixedThreadPool(100);
        while (nodeiter.hasNext()) {
            curr = nodeiter.next();
            int id=curr.getKey();
            drawedges.put(id, new ColorChooseModeDrawEdges(this.graph, id, graphics, this,this.selectedColor));
            pool.execute(drawedges.get(id));
        }
        pool.shutdown();
        if(pool.isShutdown()) {
            Stroke ns=new BasicStroke((float)(3+this.Scale/100));
        this.Nstroke=ns;
        g2d.setStroke(this.Nstroke);
            nodeiter = this.graph.nodeIter();
            while (nodeiter.hasNext()) {
                curr = nodeiter.next();
                int id = curr.getKey();
                double[] coordinates = CoordinatesTransformation(curr.getLocation());
                g2d.fillOval((int) (coordinates[0] - this.WNode * this.Scale / 2), (int) (coordinates[1] - HNode * this.Scale / 2),
                        (int) (this.HNode * this.Scale/2), (int) (this.WNode * this.Scale/2));
                g2d.drawString("" + curr.getKey() + "", (int) coordinates[0], (int) coordinates[1]);
            }
        }
    }
    /**
     * This function draw the nodes. As we mentioned earlier, we consider special cases of nodes that we want
     * to mark if we used specific algorithm.
     * @param graphics
     */
    public void DrawNodesRegular(Graphics graphics) {
        Graphics2D g2d = (Graphics2D) graphics;
        g2d.setColor(this.Ncolor);
        Iterator<NodeData> nodeiter = this.graph.nodeIter();
        HashMap<Integer, DrawMyEdgesThread> drawedges=new HashMap<>();
        NodeData curr = null;
        Dfont = new Font(Font.SANS_SERIF, Font.BOLD, (int) (10+this.Scale));
        g2d.setFont(Dfont);
        this.HNode=3;
        this.WNode=3;
        this.Hedge=5;
        this.Wedge=5;
        Stroke es=new BasicStroke((float)(3+this.Scale/100));
        g2d.setStroke(this.Estroke);
        ExecutorService pool= Executors.newFixedThreadPool(100);
        while (nodeiter.hasNext()) {
            curr = nodeiter.next();
            int id=curr.getKey();
            drawedges.put(id, new DrawMyEdgesThread(this.graph, id, graphics, this));
            pool.execute(drawedges.get(id));
        }
        pool.shutdown();
        nodeiter=this.graph.nodeIter();
        Stroke ns=new BasicStroke((float) (3+this.Scale/100));
        this.Nstroke=ns;
        g2d.setStroke(this.Nstroke);
        while(nodeiter.hasNext()&&pool.isShutdown()){
            curr = nodeiter.next();
            int id=curr.getKey();
            double[] coordinates = CoordinatesTransformation(curr.getLocation());
            g2d.fillOval((int)(coordinates[0] - this.WNode * this.Scale / 2), (int)(coordinates[1] - HNode * this.Scale / 2),
                    (int)(this.HNode * this.Scale/2 ), (int)(this.WNode * this.Scale/2));
            g2d.drawString("" + curr.getKey() + "", (int) coordinates[0], (int) coordinates[1]);
        }
    }

    public void DrawNodesSpecial(Graphics graphics) {
        Graphics2D g2d = (Graphics2D) graphics;
        g2d.setStroke(this.Nstroke);
        g2d.setColor(this.Ncolor);
        Iterator<NodeData> nodeiter = this.graph.nodeIter();
        HashMap<Integer, DrawMyEdgesThread> drawedges=new HashMap<>();
        NodeData curr = null;
        Dfont = new Font(Font.SANS_SERIF, Font.BOLD, (int) (10+this.Scale));
        g2d.setFont(Dfont);
        this.WNode=3;
        this.HNode=3;
        Stroke ns=new BasicStroke((float) (3+this.Scale/100));
        this.Nstroke=ns;
        g2d.setStroke(this.Nstroke);
        while (nodeiter.hasNext()) {
            curr = nodeiter.next();
            int id=curr.getKey();
            double[] coordinates = CoordinatesTransformation(curr.getLocation());
            if (markednodesSP.contains(curr.getKey())) {
                g2d.setColor(Color.GREEN);
                g2d.fillOval((int)(coordinates[0] - this.WNode * this.Scale / 2), (int)(coordinates[1] - HNode * this.Scale / 2),
                        (int)(this.HNode * this.Scale/2), (int)(this.WNode * this.Scale/2));
                g2d.drawString("" + curr.getKey() + "", (int) coordinates[0], (int) coordinates[1]);
                g2d.setColor(Color.BLUE);
            } else if (curr.getKey() == centerid) {
                g2d.setColor(Color.RED);
                g2d.fillOval((int)(coordinates[0] - this.WNode * this.Scale / 2), (int)(coordinates[1] - HNode * this.Scale / 2),
                        (int)(this.HNode * this.Scale / 2), (int)(this.WNode * this.Scale / 2));
                g2d.drawString("" + curr.getKey() + " Center", (int) coordinates[0], (int) coordinates[1]);
                g2d.setColor(Color.BLUE);
            } else if (markededgesTSP.containsKey(curr.getKey())) {
                g2d.setColor(Color.MAGENTA);
                g2d.fillOval((int)(coordinates[0] - this.WNode * this.Scale / 2), (int)(coordinates[1] - HNode * this.Scale / 2),
                        (int)(this.HNode * this.Scale / 2), (int)(this.WNode * this.Scale / 2));
                g2d.drawString("" + curr.getKey(), (int) coordinates[0], (int) coordinates[1]);
                g2d.setColor(Color.BLUE);
            }
//            else{
//                g2d.setColor(Color.BLUE);
//                g2d.fillOval((int)(coordinates[0] - this.WNode * this.Scale / 2), (int)(coordinates[1] - HNode * this.Scale / 2),
//                        (int)(this.HNode * this.Scale / 2), (int)(this.WNode * this.Scale / 2));
//                g2d.drawString("" + curr.getKey(), (int) coordinates[0], (int) coordinates[1]);
//            }
        }
    }


    /**
     * This function draw the nodes. As we mentioned earlier, we consider special cases of edges that we want
     * to mark if we used specific algorithm.
     * @param graphics
     */
    public void drawEdges(Graphics graphics) {
        Graphics2D g2d = (Graphics2D) graphics;
        g2d.setFont(this.Dfont);
        g2d.setStroke(this.Estroke);
        g2d.setColor(this.Ecolor);
        Iterator<EdgeData> edgeiter = this.graph.edgeIter();
        EdgeData e = null;
        Dfont = new Font(Font.SANS_SERIF, Font.BOLD, (int) (10+this.Scale));
        g2d.setFont(Dfont);
        this.Wedge=3;
        this.Hedge=3;
        Stroke es=new BasicStroke((float) (0.5+this.Scale));
//        this.Estroke=es;
        g2d.setStroke(es);
        while (edgeiter!=null&&edgeiter.hasNext()) {
            e = edgeiter.next();
            NodeData Nsrc = this.graph.getNode(e.getSrc());
            NodeData Ndest = this.graph.getNode(e.getDest());
            int src = Nsrc.getKey(), dest = Ndest.getKey();
            if(algoflags[1]&&this.markededgesSP.get(src)!=null&&this.markededgesSP.get(src)!=dest){
                continue;
            }
            if(algoflags[2]&&this.markededgesTSP.get(src)!=null&&this.markededgesTSP.get(src)!=dest){
                continue;
            }
            double[] Csrc = CoordinatesTransformation(Nsrc.getLocation());
            double[] Cdest = CoordinatesTransformation(Ndest.getLocation());
            if (MarkCheckSP(src, dest)) {
                g2d.setColor(Color.RED);
                drawArrow(g2d, new Point2D.Double(Csrc[0], Csrc[1]), new Point2D.Double(Cdest[0], Cdest[1]), this.Estroke, this.Estroke, (float) (10 + this.Scale));
                g2d.setColor(Color.BLACK);
            } else if (MarkCheckTSP(src, dest)) {
                g2d.setColor(Color.ORANGE);
                drawArrow(g2d, new Point2D.Double(Csrc[0], Csrc[1]), new Point2D.Double(Cdest[0], Cdest[1]), this.Estroke, this.Estroke, (float) (10 + this.Scale));
                g2d.setColor(Color.BLACK);
            }
        }
    }

    /**
     * This function call the draw nodes and edges functions
     * @param g
     */
    public void paintComponent(Graphics g) {
        if(!drawflags[1]&&!drawflags[3]){//not algorithms, not color frenzy
            this.DrawNodesColorSelection(g);
        }
        else if(drawflags[3]&&!drawflags[1]) {
            this.DrawNodesRegular(g);
        }
        else if(drawflags[0]&&drawflags[1])
            this.selectedColor=Color.black;
            this.DrawNodesColorSelection(g);
            this.DrawNodesSpecial(g);
            this.drawEdges(g);
    }

    /**
     * This function create the bufferimage, and when it's ready, replace it with the current graph
     * @param g
     */
    public void paint(Graphics g) {
        Dimension dimension=Toolkit.getDefaultToolkit().getScreenSize();
        int w= (int) dimension.getWidth();
        int h= (int) dimension.getHeight();
        Image mygraph = createImage(800, 600);
        Graphics gr = mygraph.getGraphics();
        paintComponent(gr);
        g.drawImage(mygraph, 0,0 , this);
    }

    /**
     * This function update the current DirectedWeightedGraphAlgorithms object so it will match the curren graph
     * @param nextalgo
     */
    public void Update(DirectedWeightedGraphAlgorithms nextalgo) {
        this.drawflags=new boolean[]{true, false, false, false};
        this.algoflags=new boolean[]{false, false, false};
        this.width=800;
        this.height=600;
        this.graphAlgorithms = nextalgo;
        this.graph = nextalgo.getGraph();
        Estroke = new BasicStroke((float) 1.5);
        Nstroke = new BasicStroke((float) 5);
        Ncolor = Color.BLUE;
        this.Wedge = 5;
        this.Hedge = 5;
        this.HNode = 5;
        this.WNode = 5;
        this.mousep = new Point(0, 0);
        this.mousenext = new Point(0, 0);
        this.mouseprev = new Point(0, 0);
        if (this.graphAlgorithms != null && this.graphAlgorithms.getGraph().nodeSize() > 0) {
            if(this.graphAlgorithms.getGraph().nodeSize()>0){
                SetBoundingBox();
            }
//            else{
//                SingleNodeboundingBox();
//            }
        }
    }

    /**
     * Credit for our friend. student from the class Bar Goldenberg, who helped us with the formula for the scaling
     */
    public double[] getCurrentScale(){
        double X=Math.abs(BoundingBox[2]-BoundingBox[0]);
        double Y=Math.abs(BoundingBox[3]-BoundingBox[1]);
        double Xscale=this.width/X;
        double Yscale=this.height/Y;
        return new double[]{Xscale, Yscale};
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        mousenext = e.getPoint();
        System.out.println(e.getPoint().toString());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseprev = (Point2D) mousep.clone();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    /**
     * This event will br triggered when we will try to drag the graph.
     * Special Thank to Amir Sabag who helped us with the formula
     * @param e
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        double[] mp = {mouseprev.getX(), mouseprev.getY()};
        double[] mn = {mousenext.getX(), mousenext.getY()};

        double[] mz = {mp[0] + (e.getX() - mn[0]) / this.Scale, mp[1] + (e.getY() - mn[1]) / this.Scale};
        mousep.setLocation(mz[0], mz[1]);
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    /**
     * This event will br triggered when we will use the mousewheel, if we will roll it upwards, we will zoom in
     * Special Thank to Amir Sabag who helped us with with the idea, we wanted to take into account the number of
     * times the user rolled the mousewheel and increase the Scale of the screen by constant each time.
     * That way we can use this event to create a Zoom In and Zoom Out effects.
     * @param e
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int c = e.getScrollAmount();
        if (e.getWheelRotation() > 0) {
            this.Scale += c * 0.02;
        } else if(this.Scale>0){
            this.Scale -= c * 0.02;
        }
        this.repaint();
    }

    /**
     * This function will be called after we run the center algorithm, it will set a new center node and redraw
     * the graph.
     * @param id
     */
    public void setCenterid(int id) {
        this.centerid = id;
        algoflags[0]=true;algoflags[1]=false;algoflags[2]=false;
        this.repaint();
    }

    /**
     * This fucntion will check if an edge which start from src and end in dest is an edge which is part of the
     * Shortest path.
     * @param src
     * @param dest
     * @return True if it is, otherwise false
     */
    public boolean MarkCheckSP(int src, int dest) {
        if (this.markededgesSP.containsKey(src) && this.markededgesSP.get(src) == dest) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * This Function create a new map of marked edges that we want to mark as they are part of the shortest path
     * @param path
     */
    public void CreateMarkedEdgesSP(ArrayList<NodeData> path) {
        if (path == null || path.size() == 0) {
            return;
        }
        algoflags[0]=false;algoflags[1]=true;algoflags[2]=false;
        this.markededgesSP = new HashMap<>();
        NodeData curr = path.remove(0);
        while (path.size() > 0) {
            NodeData next = path.remove(0);
            this.markededgesSP.put(curr.getKey(), next.getKey());
            curr = next;
        }
    }
    /**
     * This fucntion will check if an edge which start from src and end in dest is an edge which is part of the
     * selected path in the TSP.
     * @param src
     * @param dest
     * @return True if it is, otherwise false
     */
    public boolean MarkCheckTSP(int src, int dest) {
        if (this.markededgesTSP.containsKey(src) && this.markededgesTSP.get(src) == dest) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * This Function create a new map of marked edges that we want to mark as they are part of the selected path
     * in the TSP
     * @param path
     */
    public void CreateMarkedEdgesTSP(LinkedList<NodeData> path) {
        if (path == null || path.size() == 0) {
            return;
        }
        algoflags[0]=false;algoflags[1]=false;algoflags[2]=true;
        this.markededgesTSP = new HashMap<>();
        NodeData curr = path.removeFirst();
        while (path.size() > 0) {
            NodeData next = path.removeFirst();
            this.markededgesTSP.put(curr.getKey(), next.getKey());
            curr = next;
        }
    }

    public Point2D GeoLocationToPoint2D(GeoLocation p){
        double x2d=this.getWidth()/2 +p.y();
        double y2d=this.getHeight()/2+p.z();
        return new Point2D.Double(x2d, y2d);
    }
//    public void setAlgorithmFlag(){
//        drawflags[0]=false;
//        drawflags[1]=true;
//    }
//    public void setRegularFlag(){
//        drawflags[0]=true;
//        drawflags[1]=false;
//    }
    public void resetGraphDisplay(){
        this.selectedColor=Color.BLACK;
        drawflags= new boolean[]{ true, false,false, false};
        /**
         * Boolean array which represent 2 combination of vector: false=0, true-1
         * (1,0): regular edges draw- the colors will be random and will be drawn using threads
         * (0,1): special edges draw, we will use it for Algorithms.
         */
        this.gr = gr;
        this.graphAlgorithms.init(new DWGraph());
        this.graph=this.graphAlgorithms.getGraph();
        this.setBounds(0,0,gr.getWidth(),gr.getHeight());
        markednodesSP = new HashSet<>();
        markededgesSP = new HashMap<>();
        markednodesTSP = new HashSet<>();
        markededgesTSP = new HashMap<>();
        this.centerid = -1;
        this.width=800;
        this.height=600;
        Dfont = new Font(Font.SANS_SERIF, Font.BOLD, 10);
        Estroke = new BasicStroke((float) 1.5);
        Nstroke = new BasicStroke((float) 5);
        this.Scale=1;
        Ncolor = Color.BLUE;
        this.Scale = 1;
        this.Wedge = 5;
        this.Hedge = 5;
        this.HNode = 8;
        this.WNode = 8;
        this.BoundingBox = new double[4];
        this.mousep = new Point(0, 0);
        this.mousenext = new Point(0, 0);
        this.mouseprev = new Point(0, 0);
        if(this.graphAlgorithms.getGraph().nodeSize()>0) {
            SetBoundingBox();
        }
    }
    public void setLightMode(){
        if(drawflags[2]||drawflags[3]){
            drawflags[2]=false;drawflags[3]=false;
            drawflags[1]=false;
        }
        drawflags[0]=true;
        this.selectedColor=Color.black;
    }
    public void setColorMode(Color c){
        if(drawflags[1]||drawflags[3]){
            drawflags[0]=false;drawflags[1]=false;
            drawflags[3]=false;
        }
        drawflags[2]=true;
        this.selectedColor=c;
    }
    public void setColorFrenzyMode(){
        drawflags[0]=false;drawflags[1]=false;
        drawflags[2]=false;drawflags[3]=true;
        this.selectedColor=Color.black;
    }
    public void setAlgoMode(){
        drawflags[0]=true;drawflags[1]=true;
        drawflags[2]=false;drawflags[3]=false;
    }
    public void resetAlgo(){
        this.centerid=-1;
        this.markededgesTSP=new HashMap<>();
        this.markededgesSP=new HashMap<>();
        this.markednodesSP=new HashSet<>();
        this.markednodesTSP=new HashSet<>();
    }
}
