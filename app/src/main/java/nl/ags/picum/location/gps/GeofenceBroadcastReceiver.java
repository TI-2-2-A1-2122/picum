package nl.ags.picum.location.gps;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {

    private LocationObserver observer;

    public GeofenceBroadcastReceiver(LocationObserver observer) {
        this.observer = observer;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //TODO filter incoming message on geofence point
        //observer.onNearLocationEntered();
    }
}
