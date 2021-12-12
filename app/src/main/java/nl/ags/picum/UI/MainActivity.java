package nl.ags.picum.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import nl.ags.picum.R;
import nl.ags.picum.UI.fragments.RouteDetailsFragment;
import nl.ags.picum.dataStorage.roomData.Route;
import nl.ags.picum.UI.Util.RouteAdapter;
import nl.ags.picum.dataStorage.roomData.Waypoint;
import nl.ags.picum.mapManagement.routeCalculation.RouteCalculator;
import nl.ags.picum.mapManagement.routeCalculation.RouteCalculatorListener;
import nl.ags.picum.permission.PermissionManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PermissionManager permissionManager = new PermissionManager(getApplicationContext(), this);
        permissionManager.requestPermissions(new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });

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

    //TODO change to nonstatic
    public void showDetailsFragment(Route selectedRoute){
        FragmentManager fragmentManager = getSupportFragmentManager();
        new RouteDetailsFragment(selectedRoute).show(fragmentManager, "Dialog-popup");
    }

}