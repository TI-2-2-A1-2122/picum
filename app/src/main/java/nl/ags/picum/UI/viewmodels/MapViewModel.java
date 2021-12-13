package nl.ags.picum.UI.viewmodels;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import nl.ags.picum.dataStorage.dataUtil.Point;
import nl.ags.picum.dataStorage.roomData.Route;

public class MapViewModel extends ViewModel {


    private MutableLiveData<Point> currentlocation;


    public LiveData<Point> getCurrentlocation() {
        if (currentlocation == null) {
            currentlocation = new MutableLiveData<>();
        }
        return currentlocation;
    }

    public void setCurrentlocation(Point currentlocation) {
        this.currentlocation.postValue(currentlocation);
    }

    private MutableLiveData<Route> currentRoute;


    public LiveData<Route> getcurrentRoute() {
        if (currentRoute == null) {
            currentRoute = new MutableLiveData<>();
        }
        return currentRoute;
    }

    public void setCurrentRoute(Route currentRoute) {
        this.currentRoute.postValue(currentRoute);
    }

    private MutableLiveData<List<Route>> routes;

    public LiveData<List<Route>> getRoutes() {
        if (routes == null) {
            routes = new MutableLiveData<List<Route>>();
            loadRoutes();
        }
        return routes;
    }

    public void setRoutes(List<Route> routes)
    {
        this.routes.postValue(routes);
    }

    private void loadRoutes() {
    }

    //private MutableLiveData<List<GeoPoint>>





}
