package GUI;

import datastructures.Node;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;

public class Canvas extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {
    
    private double width = -1;
    private double height = -1;
    
    private JFrame frame;
    private Graph graph;
    
    double fOffsetX = 0.0;
    double fOffsetY = 0.0;
    double fScaleX = 1.0;
    double fScaleY = 1.0;
    
    double fStartPanX = 0.0;
    double fStartPanY = 0.0;
    
    double fSelectedCellX = 0.0;
    double fSelectedCellY = 0.0;
    
    Point2D beforeZoom = null;
    Point2D afterZoom = null;
    
    
    public Canvas(JFrame frame) {
        this.frame = frame;
        
        this.graph = new Graph(this).setNodeDimensions(10, 10);
        
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        this.setPreferredSize(dimension);
        
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);
        this.addKeyListener(this);
        this.setVisible(true);
        
    }
    
    // Convert coordinates from World Space --> Screen Space
    public Point WorldToScreen(double fWorldX, double fWorldY) {
        int iScreenX = (int)((fWorldX - this.fOffsetX) * this.fScaleX);
        int iScreenY = (int)((fWorldY - this.fOffsetY) * this.fScaleY);
        return new Point(iScreenX, iScreenY);
    }
    
    // Convert coordinates from Screen Space --> World Space
    public Point2D ScreenToWorld(int iScreenX, int iScreenY) {
        double fWorldX = ((double)iScreenX / fScaleX) + fOffsetX;
        double fWorldY = ((double)iScreenY / fScaleY) + fOffsetY;
        return new Point2D.Double(fWorldX, fWorldY);
    }
    
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        
        Graphics2D g2 = (Graphics2D)g;
        if (this.width == -1 || this.height == -1) {
            this.fOffsetX = -((double)this.getWidth()) / 2;
            this.fOffsetY = -((double)this.getHeight()) / 2;
//            System.out.println(this.getWidth() + " " + this.getHeight());
            System.out.println(this.frame.getWidth() + " " + this.frame.getHeight());
        }
        if (this.width != this.getWidth() || this.height != this.getHeight()) {
            this.width = this.getWidth();
            this.height = this.getHeight();
            System.out.println(this.getWidth() + " " + this.getHeight() + "---- 2");
            this.graph.setGraphDimensions(this.width, this.height);
            this.beforeZoom = new Point2D.Double(this.fOffsetX, this.fOffsetY);
            this.afterZoom = new Point2D.Double(this.fOffsetX, this.fOffsetY);
        }
//        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        Point2D topLeft = this.ScreenToWorld(0, 0);
        Point2D bottomRight = this.ScreenToWorld((int)this.width, (int)this.height);
        double fWorldLeft = topLeft.getX(), fWorldTop = topLeft.getY(), fWorldRight = bottomRight.getX(), fWorldBottom = bottomRight.getY();
        
        // Draw Main Axes a 10x10 Unit Grid
        // Draw 10 horizontal lines
        int nLinesDrawn = 0;
        for (double y = 0.0; y <= 10.0; y++) {
            if (y >= fWorldTop && y <= fWorldBottom) {
                double sx = 0.0, sy = y;
                double ex = 10.0, ey = y;
                
                Point ps = this.WorldToScreen(sx, sy);
                Point pe = this.WorldToScreen(ex, ey);
                
                g2.drawLine(ps.x, ps.y, pe.x, pe.y);
                nLinesDrawn++;
            }
        }
        
        // Draw 10 vertical lines
        for (double x = 0.0; x <= 10.0; x++) {
            if (x >= fWorldLeft && x <= fWorldRight) {
                double sx = x, sy = 0.0;
                double ex = x, ey = 10.0;
                
                Point ps = this.WorldToScreen(sx, sy);
                Point pe = this.WorldToScreen(ex, ey);
                
                g2.drawLine(ps.x, ps.y, pe.x, pe.y);
                nLinesDrawn++;
            }
        }
        
        this.graph.draw(g);


//        new Vertex(1, 2, 2, 10).draw(g2);
//        Edge.drawArrowHead(g2, this.width/2, this.height/2, this.width/2, this.height/2 + 100, 10, 15);


//        // Draw selected cell by filling with red circle. Convert cell coords
//        // into screen space, also scale the radius
//        int cx, cy, cr;
//        Point cell = this.WorldToScreen(this.fSelectedCellX + 0.5f, this.fSelectedCellY + 0.5f);
//        int cr = (int)(this.fScaleX * 0.3f);
//        g2.setColor(Color.RED);
//        g2.fillOval(cell.x - cr, cell.y - cr, cr * 2, cr * 2);
//        WorldToScreen(fSelectedCellX + 0.5f, fSelectedCellY + 0.5f, cx, cy);
//        cr = 0.3f * fScaleX;
//        FillCircle(cx, cy, cr, PIXEL_SOLID, FG_RED);
//        DrawString(2, 2, L"Lines Drawn: " + to_wstring(nLinesDrawn));
    
    }
    
    
    public void ZOOM(double sign) {
//        cr = 0.3f * fScaleX;
        sign = Math.copySign(1.0, sign);
        
        Point p = MouseInfo.getPointerInfo().getLocation();
        SwingUtilities.convertPointFromScreen(p, this);
        this.beforeZoom = this.ScreenToWorld(p.x, p.y);
        
        double zoom = 1.0 - sign * 0.1;
        this.fScaleX *= zoom;
        this.fScaleY *= zoom;
        
        p = MouseInfo.getPointerInfo().getLocation();
        SwingUtilities.convertPointFromScreen(p, this);
        this.afterZoom = this.ScreenToWorld(p.x, p.y);
        this.fOffsetX += (this.beforeZoom.getX() - this.afterZoom.getX());
        this.fOffsetY += (this.beforeZoom.getY() - this.afterZoom.getY());
        
        this.graph.setNodeDimensions(this.graph.getNodeWidth() * zoom, this.graph.getNodeHeight() * zoom);
        
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
                this.fOffsetX = -((double)this.getWidth()) / 2;
                this.fOffsetY = -((double)this.getHeight()) / 2;
            }
        } else if (SwingUtilities.isRightMouseButton(e)) {
            if (this.afterZoom != null) {
                Point p = MouseInfo.getPointerInfo().getLocation();
                SwingUtilities.convertPointFromScreen(p, this);
                Point2D P = this.ScreenToWorld((int)p.getX(), (int)p.getY());
                this.graph.addVertex(P.getX(), P.getY());
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
            this.fStartPanX = e.getX();
            this.fStartPanY = e.getY();
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
        if (SwingUtilities.isLeftMouseButton(e) && this.afterZoom != null) {
            this.fSelectedCellX = this.afterZoom.getX();
            this.fSelectedCellY = this.afterZoom.getY();
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
            this.fOffsetX -= (e.getX() - this.fStartPanX) / this.fScaleX;
            this.fOffsetY -= (e.getY() - this.fStartPanY) / this.fScaleY;
            this.fStartPanX = e.getX();
            this.fStartPanY = e.getY();
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
