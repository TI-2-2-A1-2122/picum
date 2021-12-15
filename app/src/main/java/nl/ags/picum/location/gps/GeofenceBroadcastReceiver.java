package nl.ags.picum.location.gps;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {
    public static String LOGTAG = GeofenceBroadcastReceiver.class.getName();

    public GeofenceBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOGTAG, "Received a trigger with intent: " + intent);

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if(geofencingEvent.hasError()) {
            Log.d(LOGTAG, "Geofence error:" + geofencingEvent.getErrorCode());
        }
        for (Geofence geofence : geofencingEvent.getTriggeringGeofences()) {
            Log.d(LOGTAG, "Geofence entered: " + geofence.getRequestId());

            if (Location.geofenceBroadcastReceiver != null)
                Location.geofenceBroadcastReceiver.onReceive(context, intent);

        }

    }
}
