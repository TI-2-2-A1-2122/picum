package nl.ags.picum.UI.viewmodels;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.HashMap;
import java.util.List;

import nl.ags.picum.dataStorage.dataUtil.Point;
import nl.ags.picum.dataStorage.roomData.Route;
import nl.ags.picum.mapManagement.MapManager;
import nl.ags.picum.mapManagement.routeCalculation.PointWithInstructions;

public class MapViewModel extends AndroidViewModel {

    private final MapManager mapManager;

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

    public MapManager getMapManager()
    {
        return mapManager;
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
        this.mapManager.loadSightsPerRoute(route);
        // Return the value
        this.currentRoute.setValue(route);
    }

    private final MutableLiveData<HashMap<Boolean, List<Point>>> calculatedRoute = new MutableLiveData<>();

    public MutableLiveData<HashMap<Boolean, List<Point>>> getCalculatedRoute()
    {
        return calculatedRoute;
    }

    public void setCalculatedRoute(HashMap<Boolean, List<Point>> points)
    {
        calculatedRoute.postValue(points);
    }

    //OSMRoute LiveData
    private final MutableLiveData<List<PointWithInstructions>> OSMRoute = new MutableLiveData<>();

    public MutableLiveData<List<PointWithInstructions>> getOSMRoute()
    {
        return OSMRoute;
    }

    public void setOSMRoute(List<PointWithInstructions> points)
    {
        OSMRoute.postValue(points);
    }

    //arrowBearing LiveData
    private final MutableLiveData<Double> arrowBearing = new MutableLiveData<>();

    public MutableLiveData<Double> getArrowBearing()
    {
        return arrowBearing;
    }

    public void setArrowBearing(Double bearing)
    {
        arrowBearing.postValue(bearing);
    }


}
