package Objects;

import api.GeoLocation;

public class Point3D implements GeoLocation {

    public Point3D(){

    }

    @Override
    public double x() {
        return 0;
    }

    @Override
    public double y() {
        return 0;
    }

    @Override
    public double z() {
        return 0;
    }

    @Override
    public double distance(GeoLocation g) {
        return 0;
    }
}
