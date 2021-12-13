package nl.ags.picum.location.gps;

import com.google.android.gms.location.Geofence;

import nl.ags.picum.dataStorage.dataUtil.Point;

public interface LocationObserver {


    //VOOR JESSE
    void onLocationError();
    void onLocationUpdate(Point point);
    void onNearLocationEntered(Geofence geofence);

}
