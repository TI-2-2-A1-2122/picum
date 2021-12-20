package nl.ags.picum.UI;


import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.bonuspack.routing.RoadNode;
import org.osmdroid.config.Configuration;
import org.osmdroid.library.BuildConfig;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.CustomZoomButtonsController;

import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.ags.picum.R;
import nl.ags.picum.UI.fragments.CompleteRouteFragment;
import nl.ags.picum.UI.fragments.SightsListFragment;
import nl.ags.picum.UI.viewmodels.MapViewModel;
import nl.ags.picum.dataStorage.dataUtil.Point;
import nl.ags.picum.dataStorage.managing.AppDatabaseManager;
import nl.ags.picum.dataStorage.roomData.CurrentLocation;
import nl.ags.picum.dataStorage.roomData.Route;
import nl.ags.picum.dataStorage.roomData.Sight;
import nl.ags.picum.UI.viewmodels.SightViewModel;
import nl.ags.picum.dataStorage.roomData.Waypoint;

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
        setContentView(R.layout.activity_map);
        
        // this.items = new ArrayList<OverlayItem>();
        Configuration.getInstance().setUserAgentValue("AGSPicum/1.0");
        this.mapViewModel = new ViewModelProvider(this).get(MapViewModel.class);
        this.sightViewModel = new ViewModelProvider(this).get(SightViewModel.class);

        this.mapViewModel.getMapManager().setSightViewModel(this.sightViewModel);

        this.sightViewModel.getCurrentSight().observe(this, this::onSightChanged);
        this.sightViewModel.getSights().observe(this, this::onSightsChanged);


        /*
        // Observe CalculatedRoute points
        this.mapViewModel.getCalculatedRoute().observe(this, (pointsMap) -> {
            List<Point> points = pointsMap.get(false);
            mMapController.setCenter(convertPointToGeoPoint(points.get(0)));
            // TODO: 17-12-2021 setPointsInMap method not called, visited points line are other method
            //setPointsInMap(points);
            drawRouteList(pointsMap);
        });

        // observer the raw-route
        this.mapViewModel.getOSMRoute().observe(this, (nodes) ->{
            setPointsInMap(nodes);
        });
        */

        this.mMap = findViewById(R.id.MainMap);
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
        mMapController = mMap.getController();
        initializeMap();
        mMapController.setZoom(20.1);


        Route selectedRoute = (Route) getIntent().getSerializableExtra("SelectedRoute");
        mapViewModel.setCurrentRoute(selectedRoute);
        new Thread(this::getSights).start();

        int progress = getIntent().getIntExtra("CurrentProgress", 0);
        checkProgress(progress);

        Log.d("pizzaparty", "onCreate: " + mapViewModel.getCurrentRoute());
    }

    private void checkProgress(int progress) {
        if (progress == 100) {
            drawWalkedRoute();
        } else {
            drawYetToWalkRoute();
        }

    }

    private void drawYetToWalkRoute() {
        // Observe CalculatedRoute points
        this.mapViewModel.getCalculatedRoute().observe(this, (pointsMap) -> {
            List<Point> points = pointsMap.get(false);
            mMapController.setCenter(convertPointToGeoPoint(points.get(0)));
            // TODO: 17-12-2021 setPointsInMap method not called, visited points line are other method
            //setPointsInMap(points);
            drawRouteList(pointsMap);
        });

        // observer the raw-route
        this.mapViewModel.getOSMRoute().observe(this, (nodes) ->{
            setPointsInMap(nodes);
        });
    }

    private void drawWalkedRoute() {
        List<CurrentLocation> visitedLocations = AppDatabaseManager.getInstance(getApplicationContext()).getCurrentLocationsFromRoute(this.mapViewModel.getCurrentRoute());
        List<GeoPoint> visitedPoints = new ArrayList<>();

        for (CurrentLocation c :visitedLocations) {
            visitedPoints.add(new GeoPoint(c.getLatitude(), c.getLongitude()));
        }

        Polyline visitedLine = new Polyline();
        visitedLine.getOutlinePaint().setColor(getColor(R.color.visited_line_color));
        visitedLine.getOutlinePaint().setStrokeCap(Paint.Cap.ROUND);
        mMap.getOverlayManager().add(visitedLine);

        FragmentManager fragmentManager = getSupportFragmentManager();
        CompleteRouteFragment dialog = CompleteRouteFragment.newInstance();
        dialog.show(fragmentManager, "JOE");
    }

    private Polyline visitedLine;
    private Polyline notVisitedLine;

    private void drawRouteList(HashMap<Boolean, List<Point>> pointsMap) {
        // Checking if the lists exist
        if (pointsMap.get(true) == null || pointsMap.get(false) == null) return;

        // Getting the two lists from the map
        List<GeoPoint> visitedPoints = convertPointToGeoPoint(pointsMap.get(true));
        List<GeoPoint> notVisitedPoints = convertPointToGeoPoint(pointsMap.get(false));

        // Checking if the lines have been made
        if(visitedLine == null || notVisitedLine == null) {
            this.visitedLine = new Polyline();
            this.visitedLine.getOutlinePaint().setColor(getColor(R.color.visited_line_color));
            this.visitedLine.getOutlinePaint().setStrokeCap(Paint.Cap.ROUND);
            mMap.getOverlayManager().add(this.visitedLine);

            this.notVisitedLine = new Polyline();
            this.notVisitedLine.getOutlinePaint().setColor(getColor(R.color.not_visited_line_color));
            this.notVisitedLine.getOutlinePaint().setStrokeCap(Paint.Cap.ROUND);
            mMap.getOverlayManager().add(this.notVisitedLine);
        }

        //Drawing the two lines
        notVisitedLine.setPoints(notVisitedPoints);
        visitedLine.setPoints(visitedPoints);

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


    public void setPointsInMap(List<RoadNode> points) {

        Drawable nodeIcon = getDrawable(R.drawable.osm_ic_follow_me);
//            nodeIcon.setHotspot(0.5f, 0.5f);spo
        int actualSteps = 1;
        String lastInstruction = "";
        for (int i = 0; i < points.size(); i++) {
            RoadNode node = points.get(i);
            Log.d("MarkerNodes", "NODE: " + node.mInstructions);
            //
            if (node.mManeuverType != 24 && node.mInstructions != null) {
                if (node.mInstructions.equals(lastInstruction) && node.mLength < 0.01) {

                } else {

                    Marker nodeMarker = new Marker(mMap);
                    nodeMarker.setPosition(node.mLocation);
                    nodeMarker.setIcon(nodeIcon);
                    nodeMarker.setSnippet(node.mInstructions);
                    nodeMarker.setSubDescription(Road.getLengthDurationText(this, node.mLength, node.mDuration));
                    nodeMarker.setTitle("Step " + actualSteps);
                    Drawable icon = getDrawable(getDirectionIcon(node.mManeuverType));
                    nodeMarker.setImage(icon);
                    actualSteps++;
                    lastInstruction = node.mInstructions;
                    mMap.getOverlays().add(nodeMarker);
                }
            }

        }
        mMap.invalidate();

    }


    public int getDirectionIcon(int instruction) {
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
            m.setPosition(convertPointToGeoPoint(v));
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


    public void onFABClicked (View view){

        new SightsListFragment(sights, this).show(getSupportFragmentManager(), "list");
    }


    public GeoPoint convertPointToGeoPoint(Point point) {
        return new GeoPoint(point.getLatitude(), point.getLongitude());
    }

    public List<GeoPoint> convertPointToGeoPoint(List<Point> points) {
        List<GeoPoint> geoPoints = new ArrayList<>();
        for (Point point : points)
            geoPoints.add(new GeoPoint(point.getLatitude(), point.getLongitude()));
        return geoPoints;
    }

    public List<GeoPoint> convertWayPointToGeoPoint(List<Waypoint> points) {
        List<GeoPoint> geoPoints = new ArrayList<>();
        for (Waypoint point : points)
            geoPoints.add(new GeoPoint(point.getLatitude(), point.getLongitude()));
        return geoPoints;
    }

}