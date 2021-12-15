package nl.ags.picum.UI.viewmodels;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import nl.ags.picum.dataStorage.dataUtil.Point;
import nl.ags.picum.dataStorage.roomData.Route;

public class MapViewModel extends ViewModel {

    private final MutableLiveData<Point> currentlocation = new MutableLiveData<>();


    /**
     * this MutableLiveData of point
     * you can observe this with getCurrentlocation().observe
     * you can get the object point with getCurrentlocation().getValue
     * @return MutableLiveData<Point>
     */
    public MutableLiveData<Point> getCurrentlocation() {
        return currentlocation;
    }

    public void setCurrentlocation(Point currentlocation) {
        getCurrentlocation().postValue(currentlocation);
    }

    private final MutableLiveData<Route> currentRoute = new MutableLiveData<>();

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
        getCalculatedRoute().postValue(points);
    }





}
