package nl.ags.picum.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.Manifest;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import nl.ags.picum.R;
import nl.ags.picum.UI.Util.RouteAdapter;
import nl.ags.picum.UI.dialog.PermissionDeniedDialog;
import nl.ags.picum.UI.fragments.RouteDetailsFragment;
import nl.ags.picum.UI.fragments.SettingsFragment;
import nl.ags.picum.UI.viewmodels.MapViewModel;
import nl.ags.picum.dataStorage.managing.AppDatabaseManager;
import nl.ags.picum.dataStorage.roomData.Route;
import nl.ags.picum.dataStorage.roomData.Waypoint;
import nl.ags.picum.mapManagement.MapManager;
import nl.ags.picum.mapManagement.routeCalculation.RouteCalculator;
import nl.ags.picum.mapManagement.routeCalculation.RouteCalculatorListener;
import nl.ags.picum.permission.PermissionManager;

public class MainActivity extends AppCompatActivity {
    private final List<Route> routes = new ArrayList<>();
    private int timeRequested = 0;
    private PermissionDeniedDialog dialogPermission;

    FragmentManager fragmentManager = getSupportFragmentManager();

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

            runOnUiThread(() -> Objects.requireNonNull(recyclerView.getAdapter()).notifyItemRangeChanged(0,tempList.size()));
        }).start();



    }

    public void requestPermission(String[] permissions){
        PermissionManager.requestPermissions(permissions, this, getApplicationContext());
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<String> permissionstorequest = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] == -1)
                if (timeRequested < 2) {
                    permissionstorequest.add(permissions[i]);
                } else {
                    showPermissionDialog();
                }

        }
        timeRequested++;
        if (permissionstorequest.size() < 1)
            requestPermission(permissionstorequest.toArray(new String[0]));

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
    public void showDetailsFragment(Route selectedRoute) {
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