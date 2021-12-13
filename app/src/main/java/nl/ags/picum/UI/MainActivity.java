package nl.ags.picum.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nl.ags.picum.R;
import nl.ags.picum.UI.dialog.PermissionDeniedDialog;
import nl.ags.picum.UI.fragments.RouteDetailsFragment;
import nl.ags.picum.dataStorage.managing.AppDatabaseManager;
import nl.ags.picum.dataStorage.roomData.Route;
import nl.ags.picum.UI.Util.RouteAdapter;
import nl.ags.picum.location.gps.Location;
import nl.ags.picum.location.gps.LocationObserver;
import nl.ags.picum.dataStorage.roomData.Waypoint;
import nl.ags.picum.mapManagement.routeCalculation.RouteCalculator;
import nl.ags.picum.mapManagement.routeCalculation.RouteCalculatorListener;
import nl.ags.picum.permission.PermissionManager;

public class MainActivity extends AppCompatActivity {
    private List<Route> routes = new ArrayList<>();
    private int timeRequested = 0;
    private PermissionDeniedDialog dialogPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dialogPermission = new PermissionDeniedDialog();
        requestPermission(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        } );

        RecyclerView recyclerView = findViewById(R.id.main_routes_recyclerview);
        recyclerView.setAdapter(new RouteAdapter(routes, this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        new Thread(() -> {
            AppDatabaseManager manager = AppDatabaseManager.getInstance(getApplicationContext());
            this.routes.clear();

            List<Route> tempList = manager.getRoutes();
            this.routes.addAll(tempList);

            recyclerView.getAdapter().notifyDataSetChanged();
        }).start();

        /*
        //TODO change code for implementation
        List<Route> routes = new ArrayList<>();
        routes.add(new Route("History", "Dit is een kilomter om te lopen", 0, false));
        routes.add(new Route("Geen history", "Dit is geen historische kilometer", 0, false));

         */
//
//        RecyclerView recyclerView = findViewById(R.id.main_routes_recyclerview);
//        recyclerView.setAdapter(new RouteAdapter(routes, this));
//        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
//        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        

        Waypoint w1 = new Waypoint(1,false, 51.740484f, 4.544803f);
        Waypoint w2 = new Waypoint(2,false, 51.771082f, 4.614198f);

        List<Waypoint> waypointList = new ArrayList<>();
        waypointList.add(w1);
        waypointList.add(w2);

        RouteCalculator calculator = new RouteCalculator(new RouteCalculatorListener() {
            @Override
            public void onRoutePointsCalculated(List<Point> points) {
                Log.d("TESTSSSSSSSSSSSS", points.toString());
            }
        });

        calculator.calculate(waypointList);
    }

    public void requestPermission(String[] permissions){
        PermissionManager.requestPermissions(permissions, this, getApplicationContext());
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] == -1)
                if (timeRequested < 2) {
                    requestPermission(new String[]{permissions[i]});
                } else {
                    showPermissionDialog();
                }

        }
        timeRequested++;

    }

    public void showPermissionDialog(){
        if (!dialogPermission.isAdded())
        dialogPermission.show(getSupportFragmentManager(), "gps");
    }

    @Override
    public void onResume() {
        if (timeRequested >= 2) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                showPermissionDialog();
            }
        }

        super.onResume();

    }

    //TODO change to nonstatic
    public void showDetailsFragment(Route selectedRoute){
        FragmentManager fragmentManager = getSupportFragmentManager();
        new RouteDetailsFragment(selectedRoute).show(fragmentManager, "Dialog-popup");
    }



}