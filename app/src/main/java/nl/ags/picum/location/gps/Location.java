package nl.ags.picum.location.gps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import nl.ags.picum.dataStorage.dataUtil.Point;
import nl.ags.picum.location.geofence.NearLocationManager;
import nl.ags.picum.location.geofence.NextNearLocation;

public class Location {

    private BroadcastReceiver geofenceBroadcastReceiver;
    private final Context context;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationObserver observer;

    public Location(Context context) {
        this.context = context;
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        //PermissionManager permissionManager = new PermissionManager
        //permissionManager.getLocationPermissions()

    }

    public void start(LocationObserver observer) {
        NearLocationManager nearLocationManager = new NearLocationManager(context);
        this.geofenceBroadcastReceiver = new GeofenceBroadcastReceiver(observer);
        this.observer = observer;
        startLocationUpdates();
        //TODO start sending locationupdates to observer
    }

    private Point getCurrentLocation() {
        //TODO get current location code
        return new Point(0f, 0f);
    }

    @SuppressLint("MissingPermission")
    private Point getLastLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        fusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<android.location.Location>() {
            @Override
            public void onComplete(@NonNull Task<android.location.Location> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    var lastLocation = task.getResult();
                    observer.onLocationUpdate(new Point((float) lastLocation.getLatitude(), (float) lastLocation.getLongitude()));
                } else {
                    Log.w("debug", "getLastLocation:exception" + task.getException());
                }
            }
        });
        return new Point(0f, 0f);
    }

    private NextNearLocation getNextNearLocation() {
        return new NextNearLocation() {
            @Override
            public void setNextNearLocation(Point point, Double radiusInMeters) {

            }
        };
    }

    private LocationRequest getLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    private LocationCallback getLocationCallback() {
        return new LocationCallback() {
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (android.location.Location location : locationResult.getLocations()) {
                    //Update location to interface
                    observer.onLocationUpdate(new Point((float) location.getLatitude(), (float) location.getLongitude()));
                }
            }
        };
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.d("location", "no permissinos");
            return;
        }
        fusedLocationClient.requestLocationUpdates(getLocationRequest(),
                getLocationCallback(),
                Looper.getMainLooper());
        Log.d("location", "Starting locationupdates");
    }

}
