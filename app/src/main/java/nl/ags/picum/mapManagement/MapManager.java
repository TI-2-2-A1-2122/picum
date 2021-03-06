package nl.ags.picum.mapManagement;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.google.android.gms.location.Geofence;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import nl.ags.picum.UI.viewmodels.MapViewModel;
import nl.ags.picum.UI.viewmodels.SightViewModel;
import nl.ags.picum.dataStorage.dataUtil.Point;
import nl.ags.picum.dataStorage.managing.AppDatabaseManager;
import nl.ags.picum.dataStorage.managing.DataStorage;
import nl.ags.picum.dataStorage.roomData.CalculatedWaypoint;
import nl.ags.picum.dataStorage.roomData.Route;
import nl.ags.picum.dataStorage.roomData.Sight;
import nl.ags.picum.dataStorage.roomData.Waypoint;
import nl.ags.picum.location.gps.Location;
import nl.ags.picum.location.gps.LocationObserver;
import nl.ags.picum.mapManagement.routeCalculation.PointWithInstructions;
import nl.ags.picum.mapManagement.routeCalculation.RouteCalculator;
import nl.ags.picum.mapManagement.routeCalculation.RouteCalculatorListener;

/**
 * MapManager handles the communication from the submodules to the ViewModel.
 * The class uses the singleton pattern
 * The GUI can access this class to request data
 */
public class MapManager implements LocationObserver {
    public static final String LOGTAG = MapManager.class.getName();
    private static final double DISTANCE_METER_VISITED = 25;
    private static final double DISTANCE_METER_GEOFENCE = 50;

    // Object //
    private final Context context;

    private MapViewModel mapViewModel;
    private SightViewModel sightViewModel;

    private Location locationService;

    private List<Waypoint> sights;
    private Sight setSight;

    /**
     * Main constructor for the MapManager.
     * Private constructor since MapManager uses the singleton pattern
     */
    public MapManager(Context context) {
        this.context = context;
    }

    public void setMapViewModel(MapViewModel mapViewModel) {
        this.mapViewModel = mapViewModel;
    }

    public void setSightViewModel(SightViewModel sightViewModel) {
        this.sightViewModel = sightViewModel;
    }

    /**
     * Given a route the method will use the
     * open route API to get all the route points
     * for that route.
     * The calculated points are not returned but are put in the ViewModel
     *
     * @param route The route to calculate the points to walk of
     */
    public void calculateRoutePoints(Route route) {
        // Creating a new thread to run async
        new Thread(() -> {
            // Getting all the waypoints bases on the route
            DataStorage dataStorage = AppDatabaseManager.getInstance(context);

            this.sights = dataStorage.getHistory(route);

            //CalculateOSMRoute();

            if (this.mapViewModel == null) return;
            //this.mapViewModel.setOSMRoute(this.sights);

            // Creating a RouteCalculator to calculate a route, implementing the callback function
            // to update the view model
            RouteCalculator calculator = new RouteCalculator(new RouteCalculatorListener() {
                @Override
                public void onRoutePointsCalculated(List<PointWithInstructions> pointsWithInfo) {
                    onRouteCalculated(pointsWithInfo);
                    DataStorage instance = AppDatabaseManager.getInstance(context);
                    instance.setCalculatedWaypoints(pointsWithInfo, route);
                }

                @Override
                public void onRouteCalculationError() {
                    DataStorage instance = AppDatabaseManager.getInstance(context);
                    List<CalculatedWaypoint> calculatedWaypointsFromRoute = instance.getCalculatedWaypointsFromRoute(route);
                    ArrayList<PointWithInstructions> pointWithInstructions = new ArrayList<>();
                    for (CalculatedWaypoint calculatedWaypoint : calculatedWaypointsFromRoute) {
                        PointWithInstructions pointWithInstruction = new PointWithInstructions(calculatedWaypoint.getLongitude(), calculatedWaypoint.getLatitude(), calculatedWaypoint.getInstructions(), calculatedWaypoint.getManeuverType(), calculatedWaypoint.getStreetName());
                        pointWithInstructions.add(pointWithInstruction);
                    }
                    onRoutePointsCalculated(pointWithInstructions);
                }
            });

            // Call the calculate function
            calculator.calculate(this.sights);
        }).start();
    }

    public void onRouteCalculated(List<PointWithInstructions> pointsWithInfo) {
        MapManager.this.mapViewModel.setOSMRoute(pointsWithInfo);
        List<Point> points = new ArrayList<>(pointsWithInfo);
        if (MapManager.this.mapViewModel != null) {
            HashMap<Boolean, List<Point>> markedPoints = new HashMap<>();
            markedPoints.put(false, points);
            ArrayList<Point> visitedPoints = new ArrayList<>();
            visitedPoints.add(points.get(0));
            points.remove(0);
            markedPoints.put(true, visitedPoints);
            MapManager.this.mapViewModel.setCalculatedRoute(markedPoints);
        }
    }

    /**
     * Given a route the method with load all the routes from that route.
     * The sights are put in the ViewModel.
     * After the first Sight from the route is marked as a GeoFence
     *
     * @param route The route to load the sights of
     */
    public void loadSightsPerRoute(Route route) {
        // Starting a new thread to run async
        new Thread(() -> {
            // Getting a database
            DataStorage dataStorage = AppDatabaseManager.getInstance(context);

            List<Sight> sights = dataStorage.getSightsPerRoute(route);
            Map<Sight, Waypoint> sightsMap = new HashMap<>();

            for (Sight sight : sights) {
                sightsMap.put(sight, dataStorage.getWaypointFromSight(sight));
            }

            // Setting the sights in the viewModel
            if (this.sightViewModel != null)
                this.sightViewModel.setSights(sightsMap);

            // Setting up Location manager
            setupLocationService();

            // Find the first sight that has not been visited
            Sight firstSight = findFirstSight(sightsMap, sights.get(0));

            // Lastly setting next GeoFence to the first Sight
            // Getting the location of the first sight
            Waypoint sightWaypoint = sightsMap.get(firstSight);

            // Calling the Geofence service to set the next location
            this.locationService.nearLocationManager.setNextNearLocation(new Point(sightWaypoint.getLongitude(), sightWaypoint.getLatitude()), DISTANCE_METER_GEOFENCE);
            this.setSight = firstSight;
        }).start();
    }

    private Sight findFirstSight(Map<Sight, Waypoint> sightsMap, Sight defaultSight) {
        List<Sight> sights = new ArrayList<>(sightsMap.keySet());
        sights.sort(Comparator.comparingInt(Sight::getWaypointID));

        Sight firstSight = defaultSight;
        for(int i = sightsMap.size() - 1; i >= 0; i--) {
            Sight sight = sights.get(i);
            if(sightsMap.get(sight).isVisited()) firstSight = sight;
        }

        return firstSight;
    }

    private void setupLocationService() {
        this.locationService = new Location(context);
    }

    /**
     * This method triggers the start method for the Location subsystem
     * It wil start the live updates of users location put in the ViewModel
     */
    public void startGPSUpdates() {
        if (this.locationService == null)
            setupLocationService();

        // Starting the location service
        locationService.start(this);
    }

    public void stopGPSUpdates(){
        locationService.stop();
    }

    @Override
    public void onLocationError() {
        // On Error, log the error
        Log.e(LOGTAG, "Error in retrieving location from user");

        // TODO: 13-12-2021 As extra notify user of location lost with notification / popup
    }

    /**
     * Called when a location has updated, this method handles the update.
     * When a new location is entered:
     * - Handle the change in walked part, based on checked waypoints
     * - Sent the new location to the ViewModel
     *
     * @param point The point of the new location of the user.
     */
    @Override
    public void onLocationUpdate(Point point) {
        Log.i(LOGTAG, "Received new location update: " + point);

        new Thread(() -> AppDatabaseManager.getInstance(context).setCurrentLocation(point, mapViewModel.getCurrentRoute())).start();

        // First checking if the MapViewModel is set
        if (this.mapViewModel == null) return;

        // Then updating the location of the user
        this.mapViewModel.setCurrentlocation(point);

        // Sorting the list of waypoints to correct visited
        // Starting a new thread to run async
        new Thread(() -> {
            // Checking if an active route has been set in this.mapViewModel
            if (this.mapViewModel.getCurrentRoute() == null) return;

            // Loop over the list of waypoints
            sortPointByVisited(point);
            mapViewModel.setArrowBearing(checkForDeviation(point));
        }).start();
    }

    private Double checkForDeviation(Point currentLocation) {
        // Checking if MapViewModel is set and the calculated route is not null
        if (this.mapViewModel == null ||
                this.mapViewModel.getCalculatedRoute() == null ||
                this.mapViewModel.getCalculatedRoute().getValue() == null
        ) return -1.0;

        // Getting the list of not yet visited points
        HashMap<Boolean, List<Point>> routeList = this.mapViewModel.getCalculatedRoute().getValue();
        List<Point> notVisitedPoints = routeList.get(false);
        List<Point> visitedPoints = routeList.get(true);

        //Null check
        if (notVisitedPoints == null || visitedPoints == null || notVisitedPoints.size() == 0)
            return -1.0;

        //Getting points and distances we're interested in
        Point nextWayPoint = notVisitedPoints.get(1);
        Point lastWayPoint = visitedPoints.get(visitedPoints.size() - 1);
        double distanceBetweenFirstAndLast = lastWayPoint.toGeoPoint().distanceToAsDouble(nextWayPoint.toGeoPoint());
        double distanceToNextWayPoint = currentLocation.toGeoPoint().distanceToAsDouble(nextWayPoint.toGeoPoint());
        double distanceToLastWayPoint = currentLocation.toGeoPoint().distanceToAsDouble(lastWayPoint.toGeoPoint());
        Log.d(LOGTAG, "Distance to next waypoint : " + distanceToNextWayPoint);
        Log.d(LOGTAG, "Distance to last waypoint : " + distanceToLastWayPoint);
        Log.d(LOGTAG, "Distance between waypoints: " + distanceBetweenFirstAndLast);
        //Deviation is detected when currLocation is further away from both last and next waypoint.
        boolean deviated = distanceBetweenFirstAndLast < distanceToNextWayPoint && distanceBetweenFirstAndLast < distanceToLastWayPoint;
        //Prints deviation distance
        //if(deviated) Log.d(LOGTAG, "Deviated with a distance of" + (distanceBetweenFirstAndLast - (distanceToNextWayPoint + distanceToLastWayPoint / 2)));
        Log.d(LOGTAG, "Current heading: " + currentLocation.getBearing());
        //if(deviated) return (currentLocation.toGeoPoint().bearingTo(nextWayPoint.toGeoPoint())) % 360;


        if(deviated) return currentLocation.toGeoPoint().bearingTo(nextWayPoint.toGeoPoint());

        return -1.0;
    }

    @Override
    public void onNearLocationEntered(Geofence geofence) {
        new Thread(() -> {
            Log.i(LOGTAG, "onNearLocationEntered triggered with geofence " + geofence);

            // Return if there is no SightViewModel
            if (sightViewModel == null ||
                    mapViewModel == null ||
                    sightViewModel.getSights().getValue() == null ||
                    this.setSight == null
            ) return;

            // Updating the viewModel with the set Sight (this.setSight)
            this.sightViewModel.setCurrentSight(this.setSight);

            // Getting a database
            DataStorage dataStorage = AppDatabaseManager.getInstance(context);

            // Getting all the sights
            Map<Sight, Waypoint> sightsMap = this.sightViewModel.getSights().getValue();
            List<Sight> sights = new ArrayList<>(sightsMap.keySet()).stream().sorted(Comparator.comparingInt(Sight::getWaypointID)).collect(Collectors.toList());

            // Default nextSight to the last value
            Sight nextSight = sights.get(sights.size() - 1);

            // Go over each value of sights to find the next one up
            for (int i = 0; i < sights.size() - 1; i++) {
                if (sights.get(i).equals(this.setSight)) {
                    nextSight = sights.get(i + 1);
                    break;
                }
            }

            Log.d(LOGTAG, "Now set the geofence to: " + nextSight + " name: " + nextSight.getSightDescription() + " locatie: " + sightsMap.get(nextSight).toGeoPoint().toDoubleString());


            // Updating the sights in the ViewModel from the database
            dataStorage.setWaypointProgress(this.setSight.getWaypointID(), true);

            //Marking the site as visited
            sightsMap.get(this.setSight).setVisited(true);
            this.sightViewModel.setSights(sightsMap);
            // Calculating to show visited sight to the user.
            markRouteOfSight(sightsMap.get(this.setSight));


            // Getting the Waypoint of the nextSight
            Waypoint sightWaypoint = sightsMap.get(nextSight);

            this.locationService.nearLocationManager.setNextNearLocation(new Point(sightWaypoint.getLongitude(), sightWaypoint.getLatitude()), DISTANCE_METER_VISITED);

            // Updating setSight
            this.setSight = nextSight;
        }).start();
    }

    /**
     * This algorithm sorts a given list and a current location to the correct visited and
     * not visited points. The algorithm:
     * - If the point is visited, continue
     * - If the point in further away than 5m from current position, continue
     * - If the point is within 5m: mark the point as visited, tell the database the change and
     * * go back to mark all other previous points visited
     *
     * @param currentLocation The current location of the user
     */
    private void sortPointByVisited(Point currentLocation) {

        // Checking if MapViewModel is set and the calculated route is not null
        if (this.mapViewModel == null ||
                this.mapViewModel.getCalculatedRoute() == null ||
                this.mapViewModel.getCalculatedRoute().getValue() == null
        ) return;

        // Getting the list of not yet visited points
        HashMap<Boolean, List<Point>> routeList = this.mapViewModel.getCalculatedRoute().getValue();
        List<Point> notVisitedPoints = routeList.get(false);
        List<Point> visitedPoints = routeList.get(true);

        if (notVisitedPoints == null || visitedPoints == null || notVisitedPoints.size() == 0)
            return;

        // Get distance to next next point
        int i = 0;
        double distanceToWaypoint = notVisitedPoints.get(i).toGeoPoint().distanceToAsDouble(currentLocation.toGeoPoint());
        Log.d("TAG", "Distance to next point is: " + distanceToWaypoint + "m");

        while (distanceToWaypoint < DISTANCE_METER_VISITED) {
            Log.d("TAG", "Distance to next point is: " + distanceToWaypoint + "m");

            // Adding the next first (second) item from the not visited list
            visitedPoints.add(notVisitedPoints.get(0));

            // Removing the first item from not visited list
            notVisitedPoints.remove(0);

            i++;
            distanceToWaypoint = notVisitedPoints.get(i).toGeoPoint().distanceToAsDouble(currentLocation.toGeoPoint());
        }

        if (i != 0) this.mapViewModel.setCalculatedRoute(routeList);
    }

    public void stopRoute(Route route) {
        DataStorage dataStorage = AppDatabaseManager.getInstance(context);
        dataStorage.stopRoute(route);
        stopGPSUpdates();
    }

    private void markRouteOfSight(Waypoint waypoint) {
        HashMap<Boolean, List<Point>> pointsMap = this.mapViewModel.getCalculatedRoute().getValue();
        if (pointsMap == null) return;

        List<Point> vPoints = pointsMap.get(true);
        List<Point> nvPoints = pointsMap.get(false);
        if (vPoints == null || nvPoints == null) return;

        double closedDistance = Integer.MAX_VALUE;
        int closestPoint = -1;
        // get closest point to the waypoint
        for (int i = 0; i < nvPoints.size(); i++) {
            Point point = nvPoints.get(i);
            double distanceTo = point.toGeoPoint().distanceToAsDouble(new GeoPoint(waypoint.getLatitude(), waypoint.getLongitude()));
            if (distanceTo >= closedDistance) continue;

            closedDistance = distanceTo;
            closestPoint = i;
        }

        // Move all other points
        for (int i = closestPoint; i > 0; i--) {
            Point point = nvPoints.remove(0);
            vPoints.add(point);
        }

        this.mapViewModel.setCalculatedRoute(pointsMap);
    }
}
