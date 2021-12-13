package datastructures;

import api.GeoLocation;

import java.util.Objects;

/**
 * This class represent a coordinates as location in 3D space
 * It has 3 fields: x,y,z which represent component in coordinate vector (x,y,z)
 */
public class Point3D implements GeoLocation {
    
    public static final GeoLocation ORIGIN = new Point3D(0.0, 0.0, 0.0);
    public static final GeoLocation X = new Point3D(1.0, 0.0, 0.0);
    public static final GeoLocation Y = new Point3D(0.0, 1.0, 0.0);
    public static final GeoLocation Z = new Point3D(0.0, 1.0, 1.0);
    
    private double x, y, z;
    
    //region Constructors
    
    /**
     * @param x X component of the point
     * @param y Y component of the point
     * @param z Z component of the point
     */
    public Point3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    /**
     * @param x X component of the point
     * @param y Y component of the point
     */
    public Point3D(double x, double y) {
        this(x, y, 0);
    }
    
    
    /**
     * @param x X component of the point
     */
    public Point3D(double x) {
        this(x, 0, 0);
    }
    
    /**
     * Class constructor where the x, y and z coordinates are set to 0. (Origin point)
     */
    public Point3D() {
        this(0, 0, 0);
    }
    
    /**
     * Copy constructor
     *
     * @param other GeoLocation object to copy
     */
    public Point3D(GeoLocation other) {
        this(other.x(), other.y(), other.z());
    }
    
    //endregion
    
    
    //region Getters and Setters
    
    /**
     * @return X component of the point
     */
    public double getX() {
        return this.x;
    }
    
    /**
     * @param x X component of the point
     * @return The modified Point3D instance. (allows chaining)
     */
    public Point3D setX(double x) {
        this.x = x;
        return this;
    }
    
    /**
     * @return Y component of the point
     */
    public double getY() {
        return this.y;
    }
    
    /**
     * @param y Y component of the point
     * @return The modified Point3D instance. (allows chaining)
     */
    public Point3D setY(double y) {
        this.y = y;
        return this;
    }
    
    /**
     * @return Z component of the point
     */
    public double getZ() {
        return this.z;
    }
    
    /**
     * @param z Z component of the point
     * @return The modified Point3D instance. (allows chaining)
     */
    public Point3D setZ(double z) {
        this.z = z;
        return this;
    }
    
    /**
     * @return X component of the point
     */
    @Override
    public double x() {
        return this.x;
    }
    
    /**
     * @return Y component of the point
     */
    @Override
    public double y() {
        return this.y;
    }
    
    /**
     * @return Z component of the point
     */
    @Override
    public double z() {
        return this.z;
    }
    
    //endregion
    
    
    /**
     * @param g GeoLocation object to measure the distance to.
     * @return The distance between this point and the given point.
     */
    @Override
    public double distance(GeoLocation g) {
        return Math.sqrt(Math.pow(this.x - g.x(), 2) + Math.pow(this.y - g.y(), 2) + Math.pow(this.z - g.z(), 2));
    }

    /**
     * This function return a string which represent this 3D point
     * @return String that represent this 3D point
     */
    @Override
    public String toString() {
        return this.x + ", " + this.y + ", " + this.z;
    }

    /**
     * This function get another Point3D Object and check if their properties are equal.
     * @param o the object we compare this object to
     * @return True if they are equal, otherwise false
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        GeoLocation p = (GeoLocation)o;
        return Double.compare(this.x, p.x()) == 0 && Double.compare(this.y, p.y()) == 0 && Double.compare(this.z, p.z()) == 0;
    }
    
    /**
     * Since we have overridden equals we also are required to override hashCode
     * @return the hashcode of the object
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y, this.z);
    }
}
