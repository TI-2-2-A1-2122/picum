package nl.ags.picum.UI.viewmodels;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import nl.ags.picum.dataStorage.dataUtil.Point;
import nl.ags.picum.dataStorage.roomData.Route;

public class MapViewModel extends ViewModel {

    private MutableLiveData<Point> currentlocation;


    /**
     * this MutableLiveData of point
     * you can observe this with getCurrentlocation().observe
     * you can get the object point with getCurrentlocation().getValue
     * @return MutableLiveData<Point>
     */
    public MutableLiveData<Point> getCurrentlocation() {
        if (currentlocation == null) {
            currentlocation = new MutableLiveData<>();
        }
        return currentlocation;
    }

    public void setCurrentlocation(Point currentlocation) {
        getCurrentlocation().postValue(currentlocation);
    }

    private MutableLiveData<Route> currentRoute = new MutableLiveData<>();

    /**
     * @return returns the current route
     */
    public Route getcurrentRoute() {
        return currentRoute.getValue();
    }

    /**
     * it uses set because it's only supposed to be called from the main thread
     * @param route the new current route
     */
    public void setCurrentRoute(Route route) {
        this.currentRoute.setValue(route);
    }

    private MutableLiveData<List<Route>> routes;

    /**
     * this MutableLiveData of routes
     * you can observe this with getRoutes().observe
     * you can get the object point with getRoutes().getValue
     * @return MutableLiveData<List<Route>>
     */
    public MutableLiveData<List<Route>> getRoutes() {
        if (routes == null) {
            routes = new MutableLiveData<>();
            loadRoutes();
        }
        return routes;
    }

    public void setRoutes(List<Route> routes)
    {
        getRoutes().postValue(routes);
    }

    private void loadRoutes() {
    }

    private MutableLiveData<List<Point>> calculatedRoute;

    public MutableLiveData<List<Point>> getCalculatedRoute()
    {
        if (calculatedRoute == null) {
            calculatedRoute = new MutableLiveData<>();
        }
        return calculatedRoute;
    }

    public void setCalculatedRoute(List<Point> points)
    {
        getCalculatedRoute().postValue(points);
    }





}
