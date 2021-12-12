package gui;

import gui.ColorRandomizer;
import gui.GraphDisplay;
import api.DirectedWeightedGraph;
import api.EdgeData;
import api.GeoLocation;
import api.NodeData;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.Iterator;

public class LightModeDrawEdges extends Thread{
    DirectedWeightedGraph graph;
    int id;
    Graphics g;
    Stroke Estroke;
    GraphDisplay graphDisplay;
    public LightModeDrawEdges(DirectedWeightedGraph gr, int nodeid, Graphics g, GraphDisplay gd){
        this.graph=gr;
        this.id=nodeid;
        this.graphDisplay=gd;
        this.g=g;
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



        gfx.setColor(Color.black);
        final GeneralPath polygon = new GeneralPath();
        polygon.moveTo(end.getX(), end.getY());
        polygon.lineTo(end.getX() + x1, end.getY() + y1);
        polygon.lineTo(end.getX() + x2, end.getY() + y2);
        polygon.closePath();
        gfx.fill(polygon);

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
        double dpx = graphDisplay.BoundingBox[2] - graphDisplay.BoundingBox[0];
        double dpy = graphDisplay.BoundingBox[3] - graphDisplay.BoundingBox[1];
        double dcx = graphDisplay.BoundingBox[2] - p.x();
        double dcy = graphDisplay.BoundingBox[3] - p.y();
        double xfixed=(dcx/dpx*graphDisplay.width*0.8+graphDisplay.height*0.1+graphDisplay.mousep.getX())*graphDisplay.Scale;
        double yfixed=(dcy/dpy*graphDisplay.width*0.8+graphDisplay.height*0.1+graphDisplay.mousep.getY())*graphDisplay.Scale;
        return new double[]{xfixed, yfixed};
    }
    /**
     * This function draw the nodes. As we mentioned earlier, we consider special cases of edges that we want
     * to mark if we used specific algorithm.
     * @param graphics
     */
    public void drawMyEdges(Graphics graphics) {
        Graphics2D g2d = (Graphics2D) graphics;
        g2d.setFont(graphDisplay.Dfont);
        g2d.setStroke(graphDisplay.Estroke);
        g2d.setColor(graphDisplay.Ecolor);
        Iterator<EdgeData> edgeiter = this.graph.edgeIter(this.id);
        EdgeData e = null;
        NodeData Nsrc=this.graph.getNode(id);
        Font Dfont = new Font(Font.SANS_SERIF, Font.BOLD, (int) (10+graphDisplay.Scale));
        g2d.setFont(Dfont);
        graphDisplay.Wedge+=graphDisplay.Scale;
        graphDisplay.Hedge+=graphDisplay.Scale;
        Stroke stroke=new BasicStroke((float) (4+graphDisplay.Scale));
        g2d.setStroke(stroke);
        double[] Csrc = CoordinatesTransformation(Nsrc.getLocation());
        while (edgeiter!=null&&edgeiter.hasNext()) {
            e = edgeiter.next();
            NodeData Ndest = this.graph.getNode(e.getDest());
            double[] Cdest = CoordinatesTransformation(Ndest.getLocation());
            int src = Nsrc.getKey(), dest = Ndest.getKey();
            drawArrow(g2d, new Point2D.Double(Csrc[0], Csrc[1]), new Point2D.Double(Cdest[0], Cdest[1]), graphDisplay.Estroke, graphDisplay.Estroke,(float)(10+graphDisplay.Scale));
        }
    }
    @Override
    public void run(){
        drawMyEdges(g);
    }
}
