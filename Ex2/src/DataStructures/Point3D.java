package DataStructures;

import api.GeoLocation;

import java.util.Objects;

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
    
    public Point3D(GeoLocation other) {
        this(other.x(), other.y(), other.z());
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
    
    @Override
    public double distance(GeoLocation other) {
        return Math.sqrt(Math.pow(this.x - other.x(), 2) + Math.pow(this.y - other.y(), 2) + Math.pow(this.z - other.z(), 2));
    }
    
    @Override
    public String toString() {
        return "" + this.x + ", " + this.y + ", " + this.z;
    }
    
    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null || this.getClass() != other.getClass()) return false;
        GeoLocation otherPoint = (Point3D)other;
        return Double.compare(this.x, otherPoint.x()) == 0 && Double.compare(this.y, otherPoint.y()) == 0 && Double.compare(this.z, otherPoint.z()) == 0;
    }
    
    /**
     * Since we have overridden equals we also are required to override hashCode
     *
     * @return the hashcode of the object
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y, this.z);
    }
}
