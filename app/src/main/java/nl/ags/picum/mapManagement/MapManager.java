package nl.ags.picum.mapManagement;

import android.content.Context;

//Todo remove when implemented
interface Route {}

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
     * @param route  The route to calculate the points to walk of
     */
    public void calculateRoutePoints(Route route) {

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
     * @param route  The route to load the sights of
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
