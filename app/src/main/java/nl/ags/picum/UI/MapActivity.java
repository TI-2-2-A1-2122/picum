package nl.ags.picum.UI;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.lifecycle.ViewModelProvider;


import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.library.BuildConfig;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import nl.ags.picum.R;
import nl.ags.picum.UI.viewmodels.MapViewModel;
import nl.ags.picum.UI.viewmodels.SightViewModel;
import nl.ags.picum.dataStorage.dataUtil.Point;
import nl.ags.picum.dataStorage.managing.AppDatabaseManager;
import nl.ags.picum.dataStorage.roomData.Route;
import nl.ags.picum.dataStorage.roomData.Sight;

public class MapActivity extends AppCompatActivity {

    private MapViewModel mapViewModel;
    private SightViewModel sightViewModel;
    private AppDatabaseManager appDatabaseManager;

    private MapView mMap;
    private IMapController mMapController;
    private MyLocationNewOverlay mLocationOverlay;

    //private List<OverlayItem> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
       // this.items = new ArrayList<OverlayItem>();
        this.mapViewModel = new ViewModelProvider(this).get(MapViewModel.class);
        this.sightViewModel = new ViewModelProvider(this).get(SightViewModel.class);

        this.mapViewModel.getMapManager().setSightViewModel(this.sightViewModel);

        this.sightViewModel.getCurrentSight().observe(this, this::onSightChanged);
        this.sightViewModel.getSights().observe(this, this::onSightsChanged);

        this.mapViewModel.getCalculatedRoute().observe(this, (List<Point> points) -> {
            mMapController.setCenter(converPointToGeoPoint(points.get(0)));
            setPointsInMap(points);
        });
        this.mMap = findViewById(R.id.MainMap);
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
        mMapController = mMap.getController();
        initializeMap();
        mMapController.setZoom(19.1);


        Route selectedRoute = (Route)getIntent().getSerializableExtra("SelectedRoute");

        mapViewModel.setCurrentRoute(selectedRoute);


        Log.d("pizzaparty", "onCreate: " + mapViewModel.getCurrentRoute());
    }

    private void onSightsChanged(Map<Sight, Point> sights) {
        setMarkersInMap(sights);
        Log.d("TAG", "Sights updated: " + sights.toString());
    }

    private void onSightChanged(Sight sight) {
        Log.d("TAG", "Sight location triggered: " + sight);
    }


    public void onStartRouteButtonClick(View view){
        ((Button)view).setVisibility(View.INVISIBLE);
        //TODO add function to start route
    }


    public void setPointsInMap(List<Point> points){
        List<GeoPoint> geoPoints = converPointToGeoPoint(points);
        Polyline line = new Polyline();   //see note below!
        line.setPoints(geoPoints);
//        line.setOnClickListener(new Polyline.OnClickListener() {
//            @Override
//            public boolean onClick(Polyline polyline, MapView mapView, GeoPoint eventPos) {
//
//                return false;
//            }
//        });
        mMap.getOverlayManager().add(line);

    }

    public void setMarkersInMap(Map<Sight, Point> sights){
        sights.forEach((k,v) -> {
            Marker startMarker = new Marker(mMap);
            startMarker.setPosition(converPointToGeoPoint(v));
            startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_TOP);
            startMarker.setIcon(getResources().getDrawable(R.drawable.sight_image));
            startMarker.setTitle(k.getSightName());
            mMap.getOverlays().add(startMarker);
            mMap.invalidate();
//
//            Marker m = new Marker(mMap);
//            m.setPosition(converPointToGeoPoint(v));
//            m.setTextLabelBackgroundColor(
//                    Color.TRANSPARENT
//            );
//            m.setTextLabelForegroundColor(
//                    Color.RED
//            );
//            //m.setTextIcon(k.getSightName());
//            m.setIcon(AppCompatResources.getDrawable(getApplicationContext(),R.drawable.sight_image));
//            m.setAnchor(0.2f, 0.4f);
//            m.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
//                @Override
//                public boolean onMarkerClick(Marker marker, MapView mapView) {
//                    CharSequence text = "pressed point: "+ k.getSightName();
//                    Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
//                    return false;
//                }
//            });
//
//            mMap.getOverlays()
//                    .add(m);
        });
    }

    public void initializeMap(){
        mMap.setTileSource(TileSourceFactory.MAPNIK);
        mMapController.setZoom(20.1);
        mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(getApplicationContext()),mMap);
        mLocationOverlay.enableMyLocation();
        RotationGestureOverlay mRotationGestureOverlay = new RotationGestureOverlay(mMap);
        mRotationGestureOverlay.setEnabled(true);
        mMap.setMultiTouchControls(true);
        mMap.getOverlays().add(mRotationGestureOverlay);
//        mMap.setMinZoomLevel(13.0);
//        mMap.setMaxZoomLevel(21.0);
//        mMap.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);
//        mMap.setScrollableAreaLimitLatitude(51.637524, 51.525810, 5);
//        mMap.setScrollableAreaLimitLongitude(4.680891, 4.844670, 5);
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

    public GeoPoint converPointToGeoPoint(Point point){
        return new GeoPoint(point.getLatitude(), point.getLongitude());
    }

    public List<GeoPoint> converPointToGeoPoint(List<Point> points){
        List<GeoPoint> geoPoints = new ArrayList<>();
        for (Point point : points)
            geoPoints.add(new GeoPoint(point.getLatitude(),point.getLongitude()));
        return geoPoints;
    }


}