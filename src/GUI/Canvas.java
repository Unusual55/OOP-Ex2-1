package GUI;

import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import api.GeoLocation;
import api.NodeData;
import datastructures.DWGraphAlgorithms;
import datastructures.Point3D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.Iterator;

public class Canvas extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {
    
    private double width = -1;
    private double height = -1;
    
    private JFrame frame;
    private Graph graph;
    
    private double fOffsetX = 0.0;
    private double fOffsetY = 0.0;
    private double fScaleX = 1.0;
    private double fScaleY = 1.0;
    
    private double fStartPanX = 0.0;
    private double fStartPanY = 0.0;
    
    private double fSelectedCellX = 0.0;
    private double fSelectedCellY = 0.0;
    private NodeData chosenNode = null;
    
    private final double nodeWidth = 15;
    private final double nodeHeight = 15;
    
    Point2D beforeZoom = null;
    Point2D afterZoom = null;
    
    private final int hMinGridNumber = 10; // Minimal number of horizontal grid lines
    private final int vMinGridNumber = 10; // Minimal number of vertical grid lines
    private final int hMaxGridNumber = 20; // Maximal number of horizontal grid lines
    private final int vMaxGridNumber = 20; // Maximal number of vertical grid lines
    private final double hGridRatio = this.hMaxGridNumber / this.hMinGridNumber;
    private final double vGridRatio = this.vMaxGridNumber / this.vMinGridNumber;
    private final int hSubGridNumber = 5; // Number of horizontal subgrid lines
    private final int vSubGridNumber = 5; // Number of vertical subgrid lines
    private double majorGridStepX = 50;
    private double majorGridStepY = 50;
    private double minorGridStepX = 10;
    private double minorGridStepY = 10;
    
    private DirectedWeightedGraphAlgorithms graphAlgorithms = new DWGraphAlgorithms();
    
    public Canvas(JFrame frame) {
        this.frame = frame;
        this.init();
        this.graph.setNodeDimensions(this.nodeWidth, this.nodeHeight);
//        this.graph = new Graph(this).setNodeDimensions(this.nodeWidth, this.nodeHeight);

//        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
//        this.setPreferredSize(dimension);
        
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);
        this.addKeyListener(this);
        this.setVisible(true);
    }
    
    public void init() {
        this.graph = new Graph(this);
    }
    
    public void init(DirectedWeightedGraph graph) {
        this.graph = new Graph(graph, this);
        this.graphAlgorithms.init(this.graph);
        this.fitToScreen();
    }
    
    public void init(String fileName) {
        this.graphAlgorithms.load(fileName);
        this.graph = new Graph(this.graphAlgorithms.getGraph(), this);
        this.fitToScreen();
    }
    
    private void fitToScreen() {
        
    }
    
    public Canvas setGraph(Graph graph) {
        this.graph = graph;
        return this;
    }
    
    public Graph getGraph() {
        return this.graph;
    }
    
    // Convert coordinates from World Space --> Screen Space
    public Point WorldToScreen(double fWorldX, double fWorldY) {
        int iScreenX = (int)((fWorldX - this.fOffsetX) * this.fScaleX);
        int iScreenY = (int)((fWorldY - this.fOffsetY) * this.fScaleY);
        return new Point(iScreenX, iScreenY);
    }
    
    // Convert coordinates from Screen Space --> World Space
    public Point2D ScreenToWorld(int iScreenX, int iScreenY) {
        double fWorldX = ((double)iScreenX / this.fScaleX) + this.fOffsetX;
        double fWorldY = ((double)iScreenY / this.fScaleY) + this.fOffsetY;
        return new Point2D.Double(fWorldX, fWorldY);
    }
    
    
    private int DrawXAxis(Graphics2D g, double fWorldLeft, double fWorldTop, double fWorldRight, double fWorldBottom) {
        int nLinesDrawn = 0;
        if (0 >= fWorldTop && 0 <= fWorldBottom) {
            double sx = fWorldLeft, sy = 0;
            double ex = fWorldRight, ey = 0;
            Point sP = this.WorldToScreen(sx, sy);
            Point eP = this.WorldToScreen(ex, ey);
            g.drawLine(sP.x, sP.y, eP.x, eP.y);
            nLinesDrawn++;
        }
        return nLinesDrawn;
    }
    
    private int DrawYAxis(Graphics2D g, double fWorldLeft, double fWorldTop, double fWorldRight, double fWorldBottom) {
        int nLinesDrawn = 0;
        if (0 >= fWorldLeft && 0 <= fWorldRight) {
            double sx = 0, sy = fWorldTop;
            double ex = 0, ey = fWorldBottom;
            Point sP = this.WorldToScreen(sx, sy);
            Point eP = this.WorldToScreen(ex, ey);
            g.drawLine(sP.x, sP.y, eP.x, eP.y);
            nLinesDrawn++;
        }
        return nLinesDrawn;
    }
    
    private int DrawXGrid(Graphics2D g, double spacing, double fWorldLeft, double fWorldTop, double fWorldRight, double fWorldBottom) {
        
        // Draw horizontal lines
        int nLinesDrawn = 0;
        double y = -(this.fOffsetY % spacing);
        for (y += fWorldTop; y <= fWorldBottom; y += spacing) {
            double sx = fWorldLeft, sy = y;
            double ex = fWorldRight, ey = y;
            Point sP = this.WorldToScreen(sx, sy);
            Point eP = this.WorldToScreen(ex, ey);
            g.drawLine(sP.x, sP.y, eP.x, eP.y);
            nLinesDrawn++;
        }
        return nLinesDrawn;
    }
    
    private int DrawYGrid(Graphics2D g, double spacing, double fWorldLeft, double fWorldTop, double fWorldRight, double fWorldBottom) {
        
        // Draw vertical lines
        int nLinesDrawn = 0;
        double x = -(this.fOffsetX % spacing);
        for (x += fWorldLeft; x <= fWorldRight; x += spacing) {
            double sx = x, sy = fWorldTop;
            double ex = x, ey = fWorldBottom;
            Point sP = this.WorldToScreen(sx, sy);
            Point eP = this.WorldToScreen(ex, ey);
            g.drawLine(sP.x, sP.y, eP.x, eP.y);
            nLinesDrawn++;
        }
        return nLinesDrawn;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        
        Graphics2D g2 = (Graphics2D)g;
        if (this.width != this.getWidth() || this.height != this.getHeight() || this.width == -1 || this.height == -1) {
            this.reset();
        }
//        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        Point2D topLeft = this.ScreenToWorld(0, 0);
        Point2D bottomRight = this.ScreenToWorld((int)this.width, (int)this.height);
        double fWorldLeft = topLeft.getX(), fWorldTop = topLeft.getY(), fWorldRight = bottomRight.getX(), fWorldBottom = bottomRight.getY();
        
        // Draw Main Axes a 10x10 Unit Grid
        // Draw 10 horizontal lines
        int nLinesDrawn = 0;
        
        Point p = MouseInfo.getPointerInfo().getLocation();
        SwingUtilities.convertPointFromScreen(p, this);
        
        BasicStroke stroke;
        stroke = new BasicStroke(1f);
        g2.setStroke(stroke);
        g2.setColor(new Color(127, 127, 127, 127));
        nLinesDrawn += this.DrawXGrid(g2, this.minorGridStepX, fWorldLeft, fWorldTop, fWorldRight, fWorldBottom);
        nLinesDrawn += this.DrawYGrid(g2, this.minorGridStepY, fWorldLeft, fWorldTop, fWorldRight, fWorldBottom);
        stroke = new BasicStroke(1.25f);
        g2.setStroke(stroke);
        g2.setColor(new Color(127, 127, 127, 255));
        nLinesDrawn += this.DrawXGrid(g2, this.majorGridStepX, fWorldLeft, fWorldTop, fWorldRight, fWorldBottom);
        nLinesDrawn += this.DrawYGrid(g2, this.majorGridStepY, fWorldLeft, fWorldTop, fWorldRight, fWorldBottom);
        stroke = new BasicStroke(2.25f);
        g2.setStroke(stroke);
        g2.setColor(new Color(0, 0, 0, 255));
        nLinesDrawn += this.DrawXAxis(g2, fWorldLeft, fWorldTop, fWorldRight, fWorldBottom);
        nLinesDrawn += this.DrawYAxis(g2, fWorldLeft, fWorldTop, fWorldRight, fWorldBottom);
//        System.out.println(nLinesDrawn);

//        double X = 0, Y = 0;
//        Point P = this.WorldToScreen(X, Y);
//        double a = 12.5;
//        double b = 10.0;
//        Ellipse2D.Double el = new Ellipse2D.Double(P.getX() - a, P.getY() - b, 2*a, 2*b);
//        g2.setColor(new Color(255, 0, 0, 255));
//        g2.fill(el);
//        g2.setColor(new Color(0, 255, 0, 255));
//        g2.fill(new Ellipse2D.Double(el.x, el.y + b, 4, 4));
        this.graph.draw(g, this.chosenNode, fWorldLeft, fWorldTop, fWorldRight, fWorldBottom);
    }
    
    private void reset() {
        this.width = this.getWidth();
        this.height = this.getHeight();
        this.fOffsetX = -this.width / 2;
        this.fOffsetY = -this.height / 2;
        this.fScaleX = 1.0;
        this.fScaleY = 1.0;
        this.fStartPanX = 0.0;
        this.fStartPanY = 0.0;
        this.graph.setNodeDimensions(this.nodeWidth, this.nodeHeight);
        this.graph.setGraphDimensions(this.width, this.height);
        this.majorGridStepX = this.width / this.hMinGridNumber;
        this.majorGridStepY = this.majorGridStepX; // this.height / this.vMinGridNumber;
        this.minorGridStepX = this.majorGridStepX / this.hSubGridNumber;
        this.minorGridStepY = this.majorGridStepY / this.vSubGridNumber;
        this.beforeZoom = new Point2D.Double(this.fOffsetX, this.fOffsetY);
        this.afterZoom = new Point2D.Double(this.fOffsetX, this.fOffsetY);
    }
    
    public void ZOOM(double sign) {
        sign = Math.copySign(1.0, sign);
        
        Point p = MouseInfo.getPointerInfo().getLocation();
        SwingUtilities.convertPointFromScreen(p, this);
        this.beforeZoom = this.ScreenToWorld(p.x, p.y);
        
        final double fZoomFactorX = 0.1;
        final double fZoomFactorY = 0.1;
        double zoomX = 1.0 - sign * fZoomFactorX;
        double zoomY = 1.0 - sign * fZoomFactorY;
        this.fScaleX *= zoomX;
        this.fScaleY *= zoomY;
        
        Point2D topLeft = this.ScreenToWorld(0, 0);
        Point2D bottomRight = this.ScreenToWorld((int)this.width, (int)this.height);
        double fWorldLeft = topLeft.getX(), fWorldTop = topLeft.getY(), fWorldRight = bottomRight.getX(), fWorldBottom = bottomRight.getY();

//        int zoomCounterX = (int)(Math.abs(Math.log(this.fScaleX) / Math.log(zoomX)));
//        int zoomCounterY = (int)(Math.abs(Math.log(this.fScaleY) / Math.log(zoomY)));
        
        if (sign < 0) {
            if ((fWorldRight - fWorldLeft) / this.majorGridStepX < this.hMinGridNumber) {
                this.majorGridStepX /= this.hGridRatio;
                this.minorGridStepX /= this.hGridRatio;
            }
            if ((fWorldRight - fWorldLeft) / this.majorGridStepY < this.vMinGridNumber) {
                this.majorGridStepY /= this.vGridRatio;
                this.minorGridStepY /= this.vGridRatio;
            }
        } else if (sign > 0) {
            if ((fWorldRight - fWorldLeft) / this.majorGridStepX > this.hMaxGridNumber) {
                this.majorGridStepX *= this.hGridRatio;
                this.minorGridStepX *= this.hGridRatio;
            }
            if ((fWorldRight - fWorldLeft) / this.majorGridStepY > this.vMaxGridNumber) {
                this.majorGridStepY *= this.vGridRatio;
                this.minorGridStepY *= this.vGridRatio;
            }
        }
        
        p = MouseInfo.getPointerInfo().getLocation();
        SwingUtilities.convertPointFromScreen(p, this);
        this.afterZoom = this.ScreenToWorld(p.x, p.y);
        this.fOffsetX += (this.beforeZoom.getX() - this.afterZoom.getX());
        this.fOffsetY += (this.beforeZoom.getY() - this.afterZoom.getY());
        
        this.graph.setNodeDimensions(this.graph.getNodeWidth() * zoomX, this.graph.getNodeHeight() * zoomY);
        
        this.repaint();
    }
    
    /**
     * Invoked when the mouse button has been clicked (pressed
     * and released) on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseClicked(MouseEvent e) {
//        if (SwingUtilities.isLeftMouseButton(e)) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            if (e.getClickCount() == 2) {
                this.reset();
            }
        } else if (SwingUtilities.isRightMouseButton(e)) {
            if (this.afterZoom != null) {
                Point p = MouseInfo.getPointerInfo().getLocation();
                SwingUtilities.convertPointFromScreen(p, this);
                Point2D P = this.ScreenToWorld((int)p.getX(), (int)p.getY());
                this.graph.addVertex(P.getX(), P.getY());
                System.out.println("Added vertex at " + P.getX() + ", " + P.getY());
            }
        }
        this.repaint();
    }
    
    /**
     * Invoked when a mouse button has been pressed on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            Iterator<NodeData> nIter = this.graph.nodeIter();
            NodeData minNode = null;
            double minDist = Double.MAX_VALUE;
            while (nIter.hasNext()) {
                NodeData n = nIter.next();
                GeoLocation loc = n.getLocation();
                Point2D p = this.ScreenToWorld(e.getX(), e.getY());
                if (Math.pow((loc.x() - p.getX()) / (this.nodeWidth / 2), 2) + Math.pow((loc.y() - p.getY()) / (this.nodeHeight / 2), 2) > 1) {
                    continue;
                }
                double dist = n.getLocation().distance(new Point3D(p.getX(), p.getY(), 0));
                if (dist < minDist) {
                    minDist = dist;
                    minNode = n;
                }
            }
            if (minNode != null) {
                this.chosenNode = minNode;
            } else {
                this.chosenNode = null;
                this.fStartPanX = e.getX();
                this.fStartPanY = e.getY();
            }
            this.repaint();
        }
    }
    
    /**
     * Invoked when a mouse button has been released on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
//            this.chosenNode = null;
            if (this.afterZoom != null) {
                this.fSelectedCellX = this.afterZoom.getX();
                this.fSelectedCellY = this.afterZoom.getY();
            }
            this.repaint();
        }
    }
    
    /**
     * Invoked when the mouse enters a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseEntered(MouseEvent e) {
    
    }
    
    /**
     * Invoked when the mouse exits a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseExited(MouseEvent e) {
    
    }
    
    /**
     * Invoked when a mouse button is pressed on a component and then
     * dragged.  {@code MOUSE_DRAGGED} events will continue to be
     * delivered to the component where the drag originated until the
     * mouse button is released (regardless of whether the mouse position
     * is within the bounds of the component).
     * <p>
     * Due to platform-dependent Drag&amp;Drop implementations,
     * {@code MOUSE_DRAGGED} events may not be delivered during a native
     * Drag&amp;Drop operation.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            if (this.chosenNode == null) {
                this.fOffsetX -= (e.getX() - this.fStartPanX) / this.fScaleX;
                this.fOffsetY -= (e.getY() - this.fStartPanY) / this.fScaleY;
                this.fStartPanX = e.getX();
                this.fStartPanY = e.getY();
            } else {
                Point2D p = this.ScreenToWorld(e.getX(), e.getY());
                this.chosenNode.setLocation(new Point3D(p.getX(), p.getY(), 0));
            }
            this.repaint();
        }
        
    }
    
    /**
     * Invoked when the mouse cursor has been moved onto a component
     * but no buttons have been pushed.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseMoved(MouseEvent e) {
    
    }
    
    /**
     * Invoked when the mouse wheel is rotated.
     *
     * @param e the event to be processed
     * @see MouseWheelEvent
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        this.ZOOM(e.getWheelRotation());
    }
    
    /**
     * Invoked when a key has been typed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key typed event.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == '+') {
            this.ZOOM(1);
        }
        if (e.getKeyChar() == '-') {
            this.ZOOM(-1);
        }
    }
    
    /**
     * Invoked when a key has been pressed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key pressed event.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyPressed(KeyEvent e) {
    
    }
    
    /**
     * Invoked when a key has been released.
     * See the class description for {@link KeyEvent} for a definition of
     * a key released event.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyReleased(KeyEvent e) {
    
    }
}
