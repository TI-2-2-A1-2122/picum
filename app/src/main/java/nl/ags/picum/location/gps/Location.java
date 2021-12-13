package nl.ags.picum.location.gps;

import android.content.BroadcastReceiver;
import android.content.Context;

import nl.ags.picum.dataStorage.dataUtil.Point;
import nl.ags.picum.location.geofence.NextNearLocation;

public class Location {

    private BroadcastReceiver geofenceBroadcastReceiver;
    private Context context;

    public Location(Context context) {
        this.context = context;
        //PermissionManager permissionManager = new PermissionManager
        //permissionManager.getLocationPermissions()

    }

    public void start(LocationObserver observer) {
        this.geofenceBroadcastReceiver = new GeofenceBroadcastReceiver(observer);

        //TODO start sending locationupdates to observer
    }

    private Point getCurrentLocation() {
        //TODO get current location code
        return new Point(0f, 0f);
    }

    private Point getLastLocatoin() {
        //TODO get last location code
        return new Point(0f, 0f);
    }

    private NextNearLocation getNextNearLocation() {
        //TODO get last location code
        return new NextNearLocation() {
            @Override
            public void setNextNearLocation(Point point, Double radiusInMeters) {

            }
        };
    }

    private void getLocationRequest() {

    }

    private void startLocationUpdates() {

    }

}
