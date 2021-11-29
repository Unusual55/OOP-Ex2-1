package api;

import java.util.Objects;

/**
 * This class represent a coordinates as location in 3D space
 * It has 3 fields: x,y,z which represent component in coordinate vector (x,y,z)
 */
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

    /**
     * This function returns the value of the x property
     * @return the value of the x property
     */
    @Override
    public double x() {
        return this.x;
    }

    /**
     * This function returns the value of the y property
     * @return the value of the y property
     */
    @Override
    public double y() {
        return this.y;
    }

    /**
     * This function returns the value of the z property
     * @return the value of the z property
     */
    @Override
    public double z() {
        return this.z;
    }

    /**
     * This function calculates the distance between two 3D points
     * @param other the 3D point we would like to calculate the distance from.
     * @return The distance between the two points
     */
    @Override
    public double distance(GeoLocation other) {
        return Math.sqrt(Math.pow(this.x - other.x(), 2) + Math.pow(this.y - other.y(), 2) + Math.pow(this.z - other.z(), 2));
    }

    /**
     * This function return a string which represent this 3D point
     * @return String that represent this 3D point
     */
    @Override
    public String toString() {
        return "" + this.x + ", " + this.y + ", " + this.z;
    }

    /**
     * This function get another Point3D Object and check if their properties are equal.
     * @param other the object we compare this object to
     * @return True if they are equal, otherwise false
     */
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

    /**
     * This function set the value of the x property of this point object
     * @param par the new value of x
     */
    public void setX(double par){
        this.x=par;
    }

    /**
     * This function set the value of y property of this object
     * @param par the new value of y
     */
    public void setY(double par){
        this.y=par;
    }

    /**
     * This function set the value of z property of this object
     * @param par thr new value of z
     */
    public void setZ(double par){
        this.z=par;
    }
}
