package nl.ags.picum.UI.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Map;

import nl.ags.picum.dataStorage.dataUtil.Point;
import nl.ags.picum.dataStorage.roomData.Sight;
import nl.ags.picum.dataStorage.roomData.Waypoint;

public class SightViewModel extends ViewModel {

    private final MutableLiveData<Sight> currentSight = new MutableLiveData<>();

    public LiveData<Sight> getCurrentSight()
    {
        return currentSight;
    }

    public void setCurrentSight(Sight sight)
    {
        this.currentSight.postValue(sight);
    }

    private final MutableLiveData<Map<Sight, Waypoint>> sightsLiveData = new MutableLiveData<>();


    public LiveData<Map<Sight, Waypoint>> getSights() {
        return sightsLiveData;
    }

    public void setSights(Map<Sight, Waypoint> sights)
    {
        this.sightsLiveData.postValue(sights);
    }


}
