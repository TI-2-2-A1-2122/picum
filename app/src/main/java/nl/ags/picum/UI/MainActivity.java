package nl.ags.picum.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import java.util.ArrayList;
import java.util.List;

import nl.ags.picum.R;
import nl.ags.picum.UI.fragments.RouteDetailsFragment;
import nl.ags.picum.UI.viewmodels.MapViewModel;
import nl.ags.picum.dataStorage.dataUtil.Point;
import nl.ags.picum.dataStorage.roomData.Route;
import nl.ags.picum.UI.Util.RouteAdapter;
import nl.ags.picum.location.gps.Location;
import nl.ags.picum.location.gps.LocationObserver;
import nl.ags.picum.dataStorage.roomData.Waypoint;
import nl.ags.picum.mapManagement.MapManager;
import nl.ags.picum.mapManagement.routeCalculation.RouteCalculator;
import nl.ags.picum.mapManagement.routeCalculation.RouteCalculatorListener;
import nl.ags.picum.permission.PermissionManager;

public class MainActivity extends AppCompatActivity {

    MapViewModel mapViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mapViewModel = new ViewModelProvider(this).get(MapViewModel.class);

        // TODO REMOVE
//        PermissionManager permissionManager = new PermissionManager();
//        permissionManager.requestPermissions(new String[] {
//                Manifest.permission.ACCESS_FINE_LOCATION,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//        }, this, getApplicationContext());
//
//        MapManager manager = MapManager.getInstance();
//        manager.setMapViewModel(this.mapViewModel);
//
//        manager.startGPSUpdates(this);

        mapViewModel.setCurrentlocation(new Point(100,100));

        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }

    //TODO change to nonstatic
    public void showDetailsFragment(Route selectedRoute){
        FragmentManager fragmentManager = getSupportFragmentManager();
        new RouteDetailsFragment(selectedRoute).show(fragmentManager, "Dialog-popup");
    }



}