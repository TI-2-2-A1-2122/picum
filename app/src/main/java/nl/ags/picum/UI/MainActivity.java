package nl.ags.picum.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.Manifest;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import nl.ags.picum.R;
import nl.ags.picum.UI.fragments.RouteDetailsFragment;
import nl.ags.picum.UI.fragments.SettingsFragment;
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

    FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        setContentView(R.layout.activity_main);
        PermissionManager permissionManager = new PermissionManager();
        permissionManager.requestPermissions(new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        }, this, getApplicationContext());

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

    //TODO change to nonstatic
    public void showDetailsFragment(Route selectedRoute){
        FragmentManager fragmentManager = getSupportFragmentManager();
        new RouteDetailsFragment(selectedRoute).show(fragmentManager, "Dialog-popup");
    }

    /**
     * open setting fragmen
     * @param view
     */
    public void onClickLanguageFAB(View view){

        new SettingsFragment().show(this.fragmentManager, "settings fragment");
    }

    /**
     * set app local language to english
     * @param view
     */
    public void toEnglish(View view) {
        Locale locale = new Locale("en");
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        Intent intent = getIntent();
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    /**
     * set application local language to dutch
     * @param view
     */
    public void toDutch(View view) {
        Locale locale = new Locale("nl");
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        Intent intent = getIntent();
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    /**
     * close setting fragment
     * @param view
     */
    public void backButton (View view){
        fragmentManager.beginTransaction().remove(fragmentManager.getFragments().get(0)).commit();
    }
}