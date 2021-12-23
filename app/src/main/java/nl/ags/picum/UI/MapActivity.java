package nl.ags.picum.UI;



import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.library.BuildConfig;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import nl.ags.picum.R;
import nl.ags.picum.UI.Util.InstructionConverter;
import nl.ags.picum.UI.fragments.CompleteRouteFragment;
import nl.ags.picum.UI.fragments.SightDetailsPopupFragment;
import nl.ags.picum.UI.fragments.SightsListFragment;
import nl.ags.picum.UI.viewmodels.MapViewModel;
import nl.ags.picum.UI.viewmodels.SightViewModel;
import nl.ags.picum.dataStorage.dataUtil.Point;
import nl.ags.picum.dataStorage.managing.AppDatabaseManager;
import nl.ags.picum.dataStorage.roomData.CurrentLocation;
import nl.ags.picum.dataStorage.roomData.Route;
import nl.ags.picum.dataStorage.roomData.Sight;
import nl.ags.picum.dataStorage.roomData.Waypoint;
import nl.ags.picum.mapManagement.routeCalculation.PointWithInstructions;

public class MapActivity extends AppCompatActivity {

    private static final float STROKE_WIDTH = 10;
    private MapViewModel mapViewModel;
    private SightViewModel sightViewModel;


    private MapView mMap;
    private IMapController mMapController;

    private MyLocationNewOverlay mLocationOverlay;
    private CompassOverlay mCompassOverlay;
    private RotationGestureOverlay mRotationGestureOverlay;
    private ImageView devArrow;
    private float devArrowRotation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // this.items = new ArrayList<OverlayItem>();
        Configuration.getInstance().setUserAgentValue("AGSPicum/1.0");
        this.mapViewModel = new ViewModelProvider(this).get(MapViewModel.class);
        this.sightViewModel = new ViewModelProvider(this).get(SightViewModel.class);

        this.mapViewModel.getMapManager().setSightViewModel(sightViewModel);

        this.sightViewModel.getCurrentSight().observe(this, this::onSightChanged);
        this.sightViewModel.getSights().observe(this, this::onSightsChanged);

        this.mapViewModel.getArrowBearing().observe(this, this::drawArrow);

        this.sightViewModel.getCurrentSight().observe(this, (sight) -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            SightDetailsPopupFragment dialog = new SightDetailsPopupFragment(sight, this);
            dialog.show(fragmentManager, "JOE");

            calcProgress();

            Log.d("ENTERLOCATION", sight.getSightName());
        });

        this.mMap = findViewById(R.id.MainMap);
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
        mMapController = mMap.getController();
        initializeMap();
        mMapController.setZoom(17.0);


        Route selectedRoute = (Route) getIntent().getSerializableExtra("SelectedRoute");
        mapViewModel.setCurrentRoute(selectedRoute);

        int progress = getIntent().getIntExtra("CurrentProgress", 0);
        checkProgress(progress);

        Log.d("pizzaparty", "onCreate: " + mapViewModel.getCurrentRoute());
    }

    private void calcProgress() {
        new Thread(() -> {
            double amountOfVisitedSights = 0;
            List<Waypoint> waypoints = AppDatabaseManager.getInstance(getApplicationContext()).getWaypointsWithSight(mapViewModel.getCurrentRoute());

            for (Waypoint w : waypoints) {

                if (w.isVisited())
                    amountOfVisitedSights++;
            }

            double divide = amountOfVisitedSights / waypoints.size();
            int progress = (int) (divide * 100);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    checkProgress(progress);
                }
            });
        }).start();
    }

    private void checkProgress(int progress) {
        if (progress == 100) {
            drawWalkedRoute();

            new Thread(() -> {
                if (this.mapViewModel == null) return;
                mapViewModel.getMapManager().stopRoute(mapViewModel.getCurrentRoute());
            }).start();

        } else {
            drawYetToWalkRoute();
        }
    }

    private void drawYetToWalkRoute() {
        // Observe CalculatedRoute points
        this.mapViewModel.getCalculatedRoute().observe(this, (pointsMap) -> {
            List<Point> points = pointsMap.get(false);
            mMapController.setCenter(points.get(0).toGeoPoint());
            // TODO: 17-12-2021 setPointsInMap method not called, visited points line are other method
            //setPointsInMap(points);
            drawRouteList(pointsMap);
        });

        // observer the raw-route
        this.mapViewModel.getOSMRoute().observe(this, (nodes) -> {
            setPointsInMap(nodes);
        });
    }

    private void drawWalkedRoute() {
        new Thread(() -> {
            List<CurrentLocation> visitedLocations = AppDatabaseManager.getInstance(getApplicationContext()).getCurrentLocationsFromRoute(this.mapViewModel.getCurrentRoute());
            List<GeoPoint> visitedPoints = new ArrayList<>();

            for (CurrentLocation c : visitedLocations) {
                visitedPoints.add(new GeoPoint(c.getLatitude(), c.getLongitude()));
            }

            Polyline visitedLine = new Polyline();
            visitedLine.getOutlinePaint().setColor(getColor(R.color.visited_line_color));
            visitedLine.getOutlinePaint().setStrokeCap(Paint.Cap.ROUND);
            mMap.getOverlayManager().add(visitedLine);

            visitedLine.setPoints(visitedPoints);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mMapController.setCenter(visitedPoints.get(0));
                }
            });

            findViewById(R.id.floatingStopButton).setVisibility(View.INVISIBLE);
            findViewById(R.id.floatingFollowButton).setVisibility(View.INVISIBLE);
            findViewById(R.id.floatingActionButton).setVisibility(View.INVISIBLE);

            FragmentManager fragmentManager = getSupportFragmentManager();
            CompleteRouteFragment dialog = CompleteRouteFragment.newInstance();
            dialog.show(fragmentManager, "JOE");
        }).start();
    }

    private Polyline visitedLine;
    private Polyline notVisitedLine;
    private Polyline nextLine;

    private void drawRouteList(HashMap<Boolean, List<Point>> pointsMap) {
        // Checking if the lists exist
        if (pointsMap.get(true) == null || pointsMap.get(false) == null) return;

        // Getting the two lists from the map
        List<GeoPoint> visitedPoints = convertPointToGeoPoint(Objects.requireNonNull(pointsMap.get(true)));
        List<GeoPoint> notVisitedPoints = convertPointToGeoPoint(Objects.requireNonNull(pointsMap.get(false)));
        List<GeoPoint> nextPoints = new ArrayList<>();
        nextPoints.add(notVisitedPoints.get(0));
        nextPoints.add(visitedPoints.get(visitedPoints.size()-1));

        // Checking if the lines have been made
        if (visitedLine == null || notVisitedLine == null) {
            this.visitedLine = new Polyline();
            this.visitedLine.getOutlinePaint().setColor(getColor(R.color.visited_line_color));
            this.visitedLine.getOutlinePaint().setStrokeCap(Paint.Cap.ROUND);
            this.visitedLine.getOutlinePaint().setStrokeWidth(STROKE_WIDTH);
            mMap.getOverlayManager().add(this.visitedLine);

            this.notVisitedLine = new Polyline();
            this.notVisitedLine.getOutlinePaint().setColor(getColor(R.color.not_visited_line_color));
            this.notVisitedLine.getOutlinePaint().setStrokeWidth(STROKE_WIDTH);
            this.notVisitedLine.getOutlinePaint().setStrokeCap(Paint.Cap.ROUND);
            mMap.getOverlayManager().add(this.notVisitedLine);

            this.nextLine = new Polyline();
            this.nextLine.getOutlinePaint().setColor(getColor(R.color.next_segment_line_color));
            this.nextLine.getOutlinePaint().setStrokeWidth(STROKE_WIDTH);
            this.nextLine.getOutlinePaint().setStrokeCap(Paint.Cap.ROUND);
            mMap.getOverlayManager().add(this.nextLine);
        }

        //Drawing the two lines
        notVisitedLine.setPoints(notVisitedPoints);
        visitedLine.setPoints(visitedPoints);
        nextLine.setPoints(nextPoints);

        mMap.invalidate();
        Log.d("MapActivity", "Points of the route have been drawn");
    }

    private void onSightsChanged(Map<Sight, Waypoint> sights) {
        setMarkersInMap(sights);
        Log.d("TAG", "Sights updated: " + sights.toString());
    }

    private void onSightChanged(Sight sight) {
        Log.d("TAG", "Sight location triggered: " + sight);
    }

    public void onStartRouteButtonClick(View view) {

        ((Button) view).setVisibility(View.INVISIBLE);
        findViewById(R.id.floatingFollowButton).setVisibility(View.VISIBLE);
        findViewById(R.id.floatingStopButton).setVisibility(View.VISIBLE);
        mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(getApplicationContext()), mMap);
        mLocationOverlay.enableMyLocation();
        mLocationOverlay.enableFollowLocation();
        mMap.getOverlays().add(this.mLocationOverlay);
        mCompassOverlay = new CompassOverlay(getApplicationContext(), new InternalCompassOrientationProvider(getApplicationContext()), mMap);
        mCompassOverlay.enableCompass();
        mMap.getOverlays().add(this.mCompassOverlay);
        mMapController.setZoom(20.1);
        mMap.invalidate();

    }


    public void setPointsInMap(List<PointWithInstructions> points) {
        Drawable drawable = AppCompatResources.getDrawable(this, R.drawable.osm_ic_follow_me);
        Drawable nodeIcon = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(((BitmapDrawable) drawable).getBitmap(), (int) (36.0f * getResources().getDisplayMetrics().density), (int) (36.0f * getResources().getDisplayMetrics().density), true));
//            nodeIcon.setHotspot(0.5f, 0.5f);spo
        int actualSteps = 1;
        String lastInstruction = "";
        for (int i = 0; i < points.size(); i++) {
            PointWithInstructions node = points.get(i);
            Log.d("MarkerNodes", "NODE: " + node.getInstructions());
            //
            if (node.getManeuverType() != 24 && node.getInstructions() != null) {
                if (!(node.getInstructions().equals(lastInstruction) /*&& node.mLength < 0.01*/)) {
                    Marker nodeMarker = new Marker(mMap);
                    nodeMarker.setPosition(node.toGeoPoint());
                    nodeMarker.setIcon(nodeIcon);
                    nodeMarker.setSnippet(InstructionConverter.getInstruction(this, node.getManeuverType(), node.getStreetName(), node.getInstructions()));
                    //nodeMarker.setSubDescription(Road.getLengthDurationText(this, node.mLength, node.mDuration));
                    nodeMarker.setTitle(getApplicationContext().getString(R.string.step) + " " + actualSteps);
                    Drawable icon =  AppCompatResources.getDrawable(this,getDirectionIcon(node.getManeuverType()));
                    nodeMarker.setImage(icon);
                    actualSteps++;
                    lastInstruction = node.getInstructions();
                    mMap.getOverlays().add(0, nodeMarker);
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

    public GeoPoint getCenterOfRoute(HashMap<Boolean, List<Point>> pointsMap) {
        float longPoints = 0.0f;
        float latPoints = 0.0f;
        List<Point> points = Stream.concat(pointsMap.get(false).stream(), pointsMap.get(true).stream())
                .collect(Collectors.toList());

        for (Point point : points) {
            longPoints += point.getLongitude();
            latPoints += point.getLatitude();
        }
        return new GeoPoint((latPoints / points.size()), (longPoints / points.size()));
    }

            
    private HashMap<Sight, Marker> sightMarkers;

    public void setMarkersInMap(Map<Sight, Waypoint> sights) {
        if (this.sightMarkers == null) {
            sightMarkers = new HashMap<>();
            sights.forEach((k, v) -> {
                Marker m = new Marker(mMap);
                m.setPosition(convertPointToGeoPoint(v));
                m.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
                m.setTitle(k.getSightName());
                m.setSnippet(k.getSightDescription());

                mMap.getOverlays().add(1, m);
                mMap.invalidate();

                sightMarkers.put(k, m);
            });
        }

        sights.forEach((k,v) ->{
            // Depending on the state of the sight, choose the correct icon
            Drawable drawable = AppCompatResources.getDrawable(this, getCorrectSightImage(v.isVisited()));
            Drawable dr = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(((BitmapDrawable) drawable).getBitmap(), (int) (48.0f * getResources().getDisplayMetrics().density), (int) (48.0f * getResources().getDisplayMetrics().density), true));
            sightMarkers.get(k).setIcon(dr);
        });
    }

    private int getCorrectSightImage(boolean visited) {
        return visited ? R.mipmap.sight_image : R.mipmap.sight_image_empty;
    }

    public void initializeMap() {
        mMap.setTileSource(TileSourceFactory.MAPNIK);
        this.mRotationGestureOverlay = new RotationGestureOverlay(mMap);
        mRotationGestureOverlay.setEnabled(true);
        mMap.setMultiTouchControls(true);
        mMap.getOverlays().add(mRotationGestureOverlay);
        mMap.setMinZoomLevel(13.0);
        mMap.setMaxZoomLevel(21.0);
        mMap.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);
        mMap.setScrollableAreaLimitLatitude(51.637524, 51.525810, 5);
        mMap.setScrollableAreaLimitLongitude(4.680891, 4.844670, 5);
        this.devArrow = findViewById(R.id.devArrow);
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
        new SightsListFragment(this.sightViewModel.getSights().getValue(),  this).show(getSupportFragmentManager(), "list");
    }

    public void onFFBClicked(View view) {
        mLocationOverlay.enableFollowLocation();
    }



    public GeoPoint convertPointToGeoPoint(Waypoint point) {
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

    public void onFSBClicked(View view) {
        if (this.mapViewModel == null) return;
          new Thread(() ->{ mapViewModel.getMapManager().stopRoute(mapViewModel.getCurrentRoute());}).start();
        finish();

    }


    public void drawArrow(Double bearing) {
        float convertedBearing = (bearing.floatValue() + this.mMap.getMapOrientation()) % 360;
        Log.d("arrow", "Bearing is: " + bearing);


        if(bearing == -1) {
            runOnUiThread(() -> {
                if(this.devArrow.getVisibility() != View.INVISIBLE) this.devArrow.setVisibility(View.INVISIBLE);
            });
            return;
        }

        Log.d("arrow", "Converted bearing is: " + convertedBearing);
        runOnUiThread(() -> {
            if(this.devArrow.getVisibility() != View.VISIBLE) this.devArrow.setVisibility(View.VISIBLE);
            this.devArrow.setRotation(convertedBearing);
        });
    }

//    private void draw() {
//        Projection mProjection= mMap.getProjection();
//        Bitmap mBitmap = Bitmap.createBitmap(mProjection.getWidth(), mProjection.getHeight(), Bitmap.Config.ARGB_8888);
//        final Canvas canvas = new Canvas(mBitmap);
//        mProjection.save(canvas, true, false);
//        mTilesOverlay.drawTiles(canvas, mProjection, mProjection.getZoomLevel(), mViewPort);
//        if (mOverlays != null) {
//            for (final Overlay overlay : mOverlays) {
//                if (overlay != null && overlay.isEnabled()) {
//                    overlay.draw(canvas, mProjection);
//                }
//            }
//        }
//        mProjection.restore(canvas, false);
//    }
}