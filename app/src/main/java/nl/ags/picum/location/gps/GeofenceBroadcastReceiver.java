package nl.ags.picum.location.gps;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {

    private LocationObserver observer;

    public GeofenceBroadcastReceiver(LocationObserver observer) {
        this.observer = observer;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //TODO filter incoming message on geofence point
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if(geofencingEvent.hasError()) {
            Log.d("geofence", "Geofence error:" + geofencingEvent.getErrorCode());
        }
        for (Geofence geofence : geofencingEvent.getTriggeringGeofences()) {
            observer.onNearLocationEntered(geofence);
        }
    }
}
