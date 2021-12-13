package nl.ags.picum.location.geofence;

import nl.ags.picum.dataStorage.dataUtil.Point;

public interface NextNearLocation {

    //sets next geofence point
    void setNextNearLocation(Point point, Double radiusInMeters);

}
