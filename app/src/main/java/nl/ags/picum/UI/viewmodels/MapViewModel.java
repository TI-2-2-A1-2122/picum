package nl.ags.picum.UI.viewmodels;


import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import nl.ags.picum.dataStorage.dataUtil.Point;
import nl.ags.picum.dataStorage.roomData.Route;
import nl.ags.picum.mapManagement.MapManager;

public class MapViewModel extends AndroidViewModel {

    private MapManager mapManager;

    public MapViewModel(@NonNull Application application) {
        super(application);
        this.mapManager = new MapManager(application);
        init();
    }

    private void init() {
        // Setting the ViewModel to this ViewModel
        this.mapManager.setMapViewModel(this);

        // Tell the manager to start updates
        this.mapManager.startGPSUpdates();
    }

    private final MutableLiveData<Point> currentlocation = new MutableLiveData<>();

    /**
     * this MutableLiveData of point
     * you can observe this with getCurrentlocation().observe
     * you can get the object point with getCurrentlocation().getValue
     * @return MutableLiveData<Point>
     */
    public MutableLiveData<Point> getCurrentLocation() {
        return currentlocation;
    }

    public void setCurrentlocation(Point currentlocation) {
        getCurrentLocation().postValue(currentlocation);
    }

    private final MutableLiveData<Route> currentRoute = new MutableLiveData<>();

    /**
     * @return returns the current route
     */
    public Route getCurrentRoute() {
        return currentRoute.getValue();
    }

    /**
     * it uses set because it's only supposed to be called from the main thread
     * @param route the new current route
     */
    public void setCurrentRoute(Route route) {
        // First starting to calculate the route
        this.mapManager.calculateRoutePoints(route);

        // Return the value
        this.currentRoute.setValue(route);
    }

    private final MutableLiveData<List<Route>> routes = new MutableLiveData<>();

    /**
     * this MutableLiveData of routes
     * you can observe this with getRoutes().observe
     * you can get the object point with getRoutes().getValue
     * @return MutableLiveData<List<Route>>
     */
    public MutableLiveData<List<Route>> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes)
    {
        getRoutes().postValue(routes);
    }

    private void loadRoutes() {
    }

    private final MutableLiveData<List<Point>> calculatedRoute = new MutableLiveData<>();

    public MutableLiveData<List<Point>> getCalculatedRoute()
    {
        return calculatedRoute;
    }

    public void setCalculatedRoute(List<Point> points)
    {
        calculatedRoute.postValue(points);
    }





}
