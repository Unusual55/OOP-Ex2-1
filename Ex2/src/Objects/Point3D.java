package Objects;

import api.GeoLocation;

public class Point3D implements GeoLocation {
    private double x, y, z;
    
    public Point3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Point3D(double x, double y) {
        this(x, y, 0);
    }
    
    public Point3D(double x) {
        this(x, 0, 0);
    }
    
    public Point3D() {
        this(0, 0, 0);
    }
    
    public Point3D(GeoLocation geo) {
        this(geo.x(), geo.y(), geo.z());
    }
    
    @Override
    public double x() {
        return this.x;
    }
    
    @Override
    public double y() {
        return this.y;
    }
    
    @Override
    public double z() {
        return this.z;
    }
    
    private Point3D add(GeoLocation p) {
        return new Point3D(this.x + p.x(), this.y + p.y(), this.z + p.z());
    }
    
    private Point3D sub(GeoLocation p) {
        return new Point3D(this.x - p.x(), this.y - p.y(), this.z - p.z());
    }
    
    private Point3D mul(double d) {
        return new Point3D(this.x * d, this.y * d, this.z * d);
    }
    
    private Point3D mul(GeoLocation p) {
        return new Point3D(this.x * p.x(), this.y * p.y(), this.z * p.z());
    }
    
    private Point3D div(double d) {
        return new Point3D(this.x / d, this.y / d, this.z / d);
    }
    
    private Point3D div(GeoLocation p) {
        return new Point3D(this.x / p.x(), this.y / p.y(), this.z / p.z());
    }
    
    @Override
    public double distance(GeoLocation g) {
        return Math.sqrt(Math.pow(this.x - g.x(), 2) + Math.pow(this.y - g.y(), 2) + Math.pow(this.z - g.z(), 2));
    }
}
