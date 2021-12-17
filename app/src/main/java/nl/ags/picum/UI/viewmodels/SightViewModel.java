package nl.ags.picum.UI.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Map;

import nl.ags.picum.dataStorage.dataUtil.Point;
import nl.ags.picum.dataStorage.roomData.Sight;

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

    private final MutableLiveData<Map<Sight, Point>> sightsLiveData = new MutableLiveData<>();


    public LiveData<Map<Sight, Point>> getSights() {
        return sightsLiveData;
    }

    public void setSights(Map<Sight, Point> sights)
    {
        this.sightsLiveData.postValue(sights);
    }


}
