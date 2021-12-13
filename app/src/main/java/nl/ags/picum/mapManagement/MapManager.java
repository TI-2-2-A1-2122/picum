package nl.ags.picum.mapManagement;

import android.content.Context;

import java.util.List;

import nl.ags.picum.R;
import nl.ags.picum.dataStorage.dataUtil.Point;
import nl.ags.picum.dataStorage.managing.AppDatabaseManager;
import nl.ags.picum.dataStorage.managing.DataStorage;
import nl.ags.picum.dataStorage.roomData.Route;
import nl.ags.picum.dataStorage.roomData.Waypoint;
import nl.ags.picum.mapManagement.routeCalculation.RouteCalculator;
import nl.ags.picum.mapManagement.routeCalculation.RouteCalculatorListener;

/**
 * MapManager handles the communication from the submodules to the ViewModel.
 * The class uses the singleton pattern
 * The GUI can access this class to request data
 */
public class MapManager {

    // Singleton //
    private static MapManager mapManager;

    public static MapManager getInstance() {
        if (mapManager == null) mapManager = new MapManager();

        return mapManager;
    }

    // Object //

    /**
     * Main constructor for the MapManager.
     * Private constructor since MapManager uses the singleton pattern
     */
    private MapManager() {
    }

    /**
     * Given a route the method will use the
     * open route API to get all the route points
     * for that route.
     * The calculated points are not returned but are put in the ViewModel
     *
     * @param route The route to calculate the points to walk of
     */
    public void calculateRoutePoints(Route route, Context context) {
        // Getting all the waypoints bases on the route
        DataStorage dataStorage = AppDatabaseManager.getInstance(context);
        // TODO: 13-12-2021 not implemented yet in AppDatabaseManager
        List<Waypoint> waypoints = dataStorage.getHistory(route);

        // Creating a RouteCalculator to calculate a route, implementing the callback function
        // to update the view model
        RouteCalculator calculator = new RouteCalculator(points -> {
            // Sending the route list to the ViewModel
            // TODO: 13-12-2021 Add the list to the ViewModel
        });

        // Call the calculate function
        calculator.calculate(waypoints);
    }

    /**
     * This method triggers the load to get all the routes from the database.
     * The loaded routes are put in the ViewModel as soon as they are retrieved
     */
    public void loadAllRoutes() {

    }

    /**
     * Given a route the method with load all the routes from that route.
     * The sights are put in the ViewModel
     *
     * @param route The route to load the sights of
     */
    public void loadSightsPerRoute(Route route) {

    }

    /**
     * This method triggers the start method for the Location subsystem
     * It wil start the live updates of users location put in the ViewModel
     */
    public void startGPSUpdates() {

    }

    /**
     * This method wil check if any route is still marked as active.
     * If this is the case, the route that is active is loaded into the ViewModel
     */
    public void checkActiveRoute() {

    }

}
