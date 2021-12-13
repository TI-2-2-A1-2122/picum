package nl.ags.picum.UI.viewmodels;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import nl.ags.picum.dataStorage.dataUtil.Point;
import nl.ags.picum.dataStorage.roomData.Route;

public class MapViewModel extends ViewModel {

    private MutableLiveData<Point> currentlocation;


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


    public Route getcurrentRoute() {
        return currentRoute.getValue();
    }

    public void setCurrentRoute(Route route) {
        this.currentRoute.setValue(route);
    }

    private MutableLiveData<List<Route>> routes;

    public MutableLiveData<List<Route>> getRoutes() {
        if (routes == null) {
            routes = new MutableLiveData<List<Route>>();
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
            calculatedRoute = new MutableLiveData<List<Point>>();
        }
        return calculatedRoute;
    }

    public void setCalculatedRoute(List<Point> points)
    {
        getCalculatedRoute().postValue(points);
    }





}
