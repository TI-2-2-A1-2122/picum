package nl.ags.picum.location.geofence;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

import nl.ags.picum.dataStorage.dataUtil.Point;
import nl.ags.picum.location.gps.GeofenceBroadcastReceiver;


public class NearLocationManager implements NextNearLocation {

    //Make sure you have your permissions!

    private final GeofencingClient geofencingClient;
    private final Context context;
    private Geofence activeGeofence;
    private PendingIntent geofencePendingIntent;

    private BroadcastReceiver broadcastReceiver;

    public NearLocationManager(Context context) {
        geofencingClient = LocationServices.getGeofencingClient(context);
        this.context = context;
    }

    @Override
    public void setNextNearLocation(Point nextLocation, Double radiusInMeters) {
        //remove current geofence
        if(activeGeofence != null) {
            List<String> activeFenceList = new ArrayList<>();
            activeFenceList.add(activeGeofence.getRequestId());
            geofencingClient.removeGeofences(activeFenceList);
        }
        //set new geofence
        setActiveGeofence(nextLocation, radiusInMeters);
        addGeofence();
    }


    private void setActiveGeofence(Point nextLocation, double radiusInMeters) {
        this.activeGeofence = new Geofence.Builder()
                .setRequestId(nextLocation.getId())
                .setCircularRegion(
                        nextLocation.getLatitude(),
                        nextLocation.getLongitude(),
                        (float) radiusInMeters
                )
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_DWELL)
                .setLoiteringDelay(1)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .build();
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofence(this.activeGeofence);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (geofencePendingIntent != null) {
            return geofencePendingIntent;
        }
        Intent intent = new Intent(this.context, GeofenceBroadcastReceiver.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        geofencePendingIntent = PendingIntent.getBroadcast(this.context, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
        return geofencePendingIntent;
    }

    @SuppressLint("MissingPermission")
    private void addGeofence() {
        geofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                .addOnSuccessListener(aVoid -> Log.d("Geo", "Geofence added sucessfully!"))
                .addOnFailureListener(e -> Log.d("Geo", "Adding geofence failed, " + e.getLocalizedMessage()));
    }

}

