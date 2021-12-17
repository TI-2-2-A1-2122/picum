package nl.ags.picum.UI;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;


import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.kml.HotSpot;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.bonuspack.routing.RoadNode;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.ags.picum.R;
import nl.ags.picum.UI.fragments.RouteDetailsFragment;
import nl.ags.picum.UI.fragments.SightDetailsPopupFragment;
import nl.ags.picum.UI.fragments.SightsListFragment;
import nl.ags.picum.UI.viewmodels.MapViewModel;
import nl.ags.picum.dataStorage.dataUtil.Point;
import nl.ags.picum.dataStorage.managing.AppDatabaseManager;
import nl.ags.picum.dataStorage.roomData.Route;
import nl.ags.picum.dataStorage.roomData.Sight;
import nl.ags.picum.mapManagement.MapManager;
import nl.ags.picum.UI.viewmodels.SightViewModel;

public class MapActivity extends AppCompatActivity {

    private MapViewModel mapViewModel;
    private SightViewModel sightViewModel;


    private MapView mMap;
    private IMapController mMapController;
    private List<Sight> sights;

    private MyLocationNewOverlay mLocationOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_map);
        // this.items = new ArrayList<OverlayItem>();
        Configuration.getInstance().setUserAgentValue("AGSPicum/1.0");
        this.mapViewModel = new ViewModelProvider(this).get(MapViewModel.class);
        this.sightViewModel = new ViewModelProvider(this).get(SightViewModel.class);

        this.mapViewModel.getMapManager().setSightViewModel(this.sightViewModel);

        this.sightViewModel.getCurrentSight().observe(this, this::onSightChanged);
        this.sightViewModel.getSights().observe(this, this::onSightsChanged);

        this.mapViewModel.getCalculatedRoute().observe(this, (pointsMap) -> {
            List<Point> points = pointsMap.get(false);
            mMapController.setCenter(converPointToGeoPoint(points.get(0)));
            // TODO: 17-12-2021 setPointsInMap method not called, visited points line are other method
            //setPointsInMap(points);
            drawRouteList(pointsMap);
        });
        this.mMap = findViewById(R.id.MainMap);
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
        mMapController = mMap.getController();
        initializeMap();
        mMapController.setZoom(20.1);


        Route selectedRoute = (Route) getIntent().getSerializableExtra("SelectedRoute");
        mapViewModel.setCurrentRoute(selectedRoute);
        new Thread(() -> {
            getSights();
        }).start();


        Log.d("pizzaparty", "onCreate: " + mapViewModel.getCurrentRoute());
    }

    private void drawRouteList(HashMap<Boolean, List<Point>> pointsMap) {
        // Checking if the lists exist
        if (pointsMap.get(true) == null || pointsMap.get(false) == null) return;

        // Getting the two lists from the map
        List<GeoPoint> visitedPoints = converPointToGeoPoint(pointsMap.get(true));
        List<GeoPoint> notVisitedPoints = converPointToGeoPoint(pointsMap.get(false));

        //Drawing the two lines
        Polyline visitedLine = new Polyline();
        visitedLine.getOutlinePaint().setColor(getColor(R.color.visited_line_color));
        visitedLine.setPoints(visitedPoints);
        visitedLine.getOutlinePaint().setStrokeCap(Paint.Cap.ROUND);

        Polyline notVisitedLine = new Polyline();
        notVisitedLine.getOutlinePaint().setColor(getColor(R.color.not_visited_line_color));
        notVisitedLine.setPoints(notVisitedPoints);
        notVisitedLine.getOutlinePaint().setStrokeCap(Paint.Cap.ROUND);

        mMap.getOverlayManager().add(visitedLine);
        mMap.getOverlayManager().add(notVisitedLine);

        mMap.invalidate();
        Log.d("MapActivity", notVisitedPoints.toString());
        Log.d("MapActivity", "Points of the route have been drawn");
    }

    public void getSights() {
        AppDatabaseManager dbManager = new AppDatabaseManager(this);
        sights = dbManager.getSightsPerRoute(mapViewModel.getCurrentRoute());
    }

    private void onSightsChanged(Map<Sight, Point> sights) {
        setMarkersInMap(sights);
        Log.d("TAG", "Sights updated: " + sights.toString());
    }

    private void onSightChanged(Sight sight) {
        Log.d("TAG", "Sight location triggered: " + sight);
    }

    public void onStartRouteButtonClick(View view) {
        ((Button) view).setVisibility(View.INVISIBLE);
        //TODO add function to start route
    }


    public void setPointsInMap(List<Point> points) {

        RoadManager roadManager = new OSRMRoadManager(this, Configuration.getInstance().getUserAgentValue());
        ((OSRMRoadManager) roadManager).setMean(OSRMRoadManager.MEAN_BY_FOOT);
        ArrayList<GeoPoint> waypoints = new ArrayList<>(converPointToGeoPoint(points));
        Road road = roadManager.getRoad(waypoints);
        Polyline roadOverlay = RoadManager.buildRoadOverlay(road);
        mMap.getOverlays().add(roadOverlay);

        Drawable nodeIcon = getDrawable(R.drawable.osm_ic_follow_me);
//            nodeIcon.setHotspot(0.5f, 0.5f);
        for (int i = 0; i < road.mNodes.size(); i++) {
            RoadNode node = road.mNodes.get(i);
            Marker nodeMarker = new Marker(mMap);
            nodeMarker.setPosition(node.mLocation);
            nodeMarker.setIcon(nodeIcon);
            nodeMarker.setSnippet(node.mInstructions);
            nodeMarker.setSubDescription(Road.getLengthDurationText(this, node.mLength, node.mDuration));
            nodeMarker.setTitle("Step " + i);
            // Drawable icon = getDrawable(getDirectionicon(node.mManeuverType));
            //] nodeMarker.setImage(icon);
            mMap.getOverlays().add(nodeMarker);

        }
        mMap.invalidate();

    }


    public int getDirectionicon(int instruction) {
        switch (instruction) {
            case 1:
            case 11:
                return R.drawable.ic_continue;
            case 0:
            case 2:
            default:
                return R.drawable.ic_empty;
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
                return R.drawable.ic_roundabout;
            case 5:
                return R.drawable.ic_sharp_left;
            case 8:
                return R.drawable.ic_sharp_right;
            case 3:
            case 9:
            case 17:
            case 20:
                return R.drawable.ic_slight_left;
            case 6:
            case 10:
            case 18:
            case 21:
                return R.drawable.ic_slight_right;
            case 4:
                return R.drawable.ic_turn_left;
            case 7:
                return R.drawable.ic_turn_right;
            case 12:
            case 13:
            case 14:
                return R.drawable.ic_u_turn;
        }
    }

    public void setMarkersInMap(Map<Sight, Point> sights) {
        sights.forEach((k, v) -> {
            Marker m = new Marker(mMap);
            m.setPosition(converPointToGeoPoint(v));
            m.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            m.setIcon(getResources().getDrawable(R.drawable.marker_default));
            m.setTitle(k.getSightName());
            m.setSnippet(k.getSightDescription());
            mMap.getOverlays().add(m);
            mMap.invalidate();

        });
    }

    public void initializeMap() {
        mMap.setTileSource(TileSourceFactory.MAPNIK);
        mMapController.setZoom(20.1);
        mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(getApplicationContext()), mMap);
        mLocationOverlay.enableMyLocation();
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

    public void onFABClicked(View view) {
        new SightsListFragment(sights, this).show(getSupportFragmentManager(), "list");
    }


    public GeoPoint converPointToGeoPoint(Point point) {
        return new GeoPoint(point.getLatitude(), point.getLongitude());
    }

    public List<GeoPoint> converPointToGeoPoint(List<Point> points) {
        List<GeoPoint> geoPoints = new ArrayList<>();
        for (Point point : points)
            geoPoints.add(new GeoPoint(point.getLatitude(), point.getLongitude()));
        return geoPoints;
    }

}