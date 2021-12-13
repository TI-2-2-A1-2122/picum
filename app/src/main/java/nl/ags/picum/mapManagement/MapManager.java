package nl.ags.picum.mapManagement;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.location.Geofence;

import java.util.ArrayList;
import java.util.List;

import nl.ags.picum.UI.viewmodels.MapViewModel;
import nl.ags.picum.UI.viewmodels.SightViewModel;
import nl.ags.picum.dataStorage.dataUtil.Point;
import nl.ags.picum.dataStorage.managing.AppDatabaseManager;
import nl.ags.picum.dataStorage.managing.DataStorage;
import nl.ags.picum.dataStorage.roomData.Route;
import nl.ags.picum.dataStorage.roomData.Sight;
import nl.ags.picum.dataStorage.roomData.Waypoint;
import nl.ags.picum.location.geofence.NearLocationManager;
import nl.ags.picum.location.gps.Location;
import nl.ags.picum.location.gps.LocationObserver;
import nl.ags.picum.mapManagement.routeCalculation.RouteCalculator;

/**
 * MapManager handles the communication from the submodules to the ViewModel.
 * The class uses the singleton pattern
 * The GUI can access this class to request data
 */
public class MapManager implements LocationObserver {
    public static String LOGTAG = MapManager.class.getName();

    // Object //
    private Context context;

    private MapViewModel mapViewModel;
    private SightViewModel sightViewModel;

    private Location locationService;

    /**
     * Main constructor for the MapManager.
     * Private constructor since MapManager uses the singleton pattern
     */
    private MapManager(Context context) {
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
        // Getting all the waypoints bases on the route
        DataStorage dataStorage = AppDatabaseManager.getInstance(context);
        // TODO: 13-12-2021 not implemented yet in AppDatabaseManager
        List<Waypoint> waypoints = dataStorage.getHistory(route);

        // Creating a RouteCalculator to calculate a route, implementing the callback function
        // to update the view model
        RouteCalculator calculator = new RouteCalculator((points) -> {
            if (this.mapViewModel != null)
                this.mapViewModel.setCalculatedRoute(points);
        });

        // Call the calculate function
        calculator.calculate(waypoints);
    }

    /**
     * Given a route the method with load all the routes from that route.
     * The sights are put in the ViewModel
     *
     * @param route The route to load the sights of
     */
    public void loadSightsPerRoute(Route route) {
        // Starting a new thread to run async
        new Thread(() -> {
            // Getting a database
            DataStorage dataStorage = AppDatabaseManager.getInstance(context);

            // TODO: 13-12-2021 Get all the sights from the database
            List<Sight> sights = new ArrayList<>();

            // Setting the sights in the viewModel
            if (this.sightViewModel != null)
                this.sightViewModel.setSights(sights);

            // Setting up Location manager
            setupLocationService();

            // Lastly setting next GeoFence to the first Sight
            // TODO: 13-12-2021 Get the First next Sight from the list
            //this.locationService.nearLocationManager.setNextNearLocation();

        }).start();
    }

    private void setupLocationService() {
        this.locationService = new Location(context);
    }

    /**
     * This method wil check if any route is still marked as active.
     * If this is the case, the route that is active is loaded into the ViewModel
     *
     * @param routes The list of routes to check
     */
    public void checkActiveRoute(List<Route> routes) {
        // Starting a new thread to run async
        Route activeRoute = null;

        // Going over the routes to check active status
        for (Route route : routes)
            if (route.isInProgress()) activeRoute = route;


        // Set the active route in the ViewModel
        if (this.mapViewModel != null)
            this.mapViewModel.setCurrentRoute(activeRoute);
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

    @Override
    public void onLocationError() {
        // On Error, log the error
        Log.e(LOGTAG, "Error in retrieving location from user");

        // TODO: 13-12-2021 As extra notify user of location lost
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
        // TODO: 13-12-2021 Handle splitting waypoints that are received

        // First updating the location of the user
        this.mapViewModel.setCurrentlocation(point);

        // Starting a new thread to run async
        new Thread(() -> {
            // Getting a database
            DataStorage dataStorage = AppDatabaseManager.getInstance(context);

            // Get the list of waypoints of the current route
            List<Waypoint> waypointList = dataStorage.getHistory(this.mapViewModel.getcurrentRoute());

            // Loop over the list of waypoints
            sortPointByVisited(point, waypointList);

        }).start();

        // Then calling the method to sort the list for checks

    }

    /**
     * This algorithm sorts a given list and a current location to the correct visited and
     * not visited points. The algorithm:
     * - If the point is visited, continue
     * - If the point in further away than 5m from current position, continue
     * - If the point is within 5m, mark the point as visited and
     * go back to mark all other previous points visited
     *
     * @param currentLocation  The current location of the user
     * @param waypointList  The points to be sorted
     */
    private void sortPointByVisited(Point currentLocation, List<Waypoint> waypointList) {

        for(Waypoint waypoint : waypointList) {
            if (waypoint.isVisited()) continue;
            

        }

    }


    @Override
    public void onNearLocationEntered(Geofence geofence) {
        // Get the sight that was marked as next in the list
        if (sightViewModel != null)
            this.sightViewModel.getCurrentSight();

        // Put the next Sight to the ViewModel

        // Setting the GoeFence to the next Sight
    }

}
