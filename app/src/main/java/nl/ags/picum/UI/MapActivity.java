package nl.ags.picum.UI;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;

import java.util.List;

import nl.ags.picum.R;
import nl.ags.picum.UI.viewmodels.MapViewModel;
import nl.ags.picum.UI.viewmodels.SightViewModel;
import nl.ags.picum.dataStorage.roomData.Route;
import nl.ags.picum.dataStorage.roomData.Sight;

public class MapActivity extends AppCompatActivity {

    private MapViewModel mapViewModel;
    private SightViewModel sightViewModel;

    private MapView mMap;
    private IMapController mMapController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        this.mapViewModel = new ViewModelProvider(this).get(MapViewModel.class);
        this.sightViewModel = new ViewModelProvider(this).get(SightViewModel.class);

        this.mapViewModel.getMapManager().setSightViewModel(this.sightViewModel);

        this.sightViewModel.getCurrentSight().observe(this, this::onSightChanged);
        this.sightViewModel.getSights().observe(this, this::onSightsChanged);

        this.mMap = findViewById(R.id.MainMap);
        mMapController = mMap.getController();
        initializeMap();

        Route selectedRoute = (Route)getIntent().getSerializableExtra("SelectedRoute");

        mapViewModel.setCurrentRoute(selectedRoute);

        Log.d("pizzaparty", "onCreate: " + mapViewModel.getCurrentRoute());
    }

    private void onSightsChanged(List<Sight> sights) {
        Log.d("TAG", "Sights updated: " + sights.toString());
    }

    private void onSightChanged(Sight sight) {
        Log.d("TAG", "Sight location triggered: " + sight);
    }


    public void onStartRouteButtonClick(View view){
        ((Button)view).setVisibility(View.INVISIBLE);
        //TODO add function to start route
    }

    public void initializeMap(){
        mMap.setTileSource(TileSourceFactory.MAPNIK);
        mMapController.setZoom(20.1);
        //TODO Get Location
        GeoPoint startPoint = new GeoPoint(51.585474, 4.792315);
        //TODO
        mMapController.setCenter(startPoint); //(Middelpunt route?)
        RotationGestureOverlay mRotationGestureOverlay = new RotationGestureOverlay(mMap);
        mRotationGestureOverlay.setEnabled(true);
        mMap.setMultiTouchControls(true);
        mMap.getOverlays().add(mRotationGestureOverlay);
        mMap.setMinZoomLevel(13.0);
        mMap.setMaxZoomLevel(21.0);
        mMap.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);
        mMap.setScrollableAreaLimitLatitude(51.637524, 51.525810, 5);
        mMap.setScrollableAreaLimitLongitude(4.680891, 4.844670, 5);
    }

    @Override
    public void onResume() {
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        mMap.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onPause() {
        super.onPause();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        mMap.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }
}