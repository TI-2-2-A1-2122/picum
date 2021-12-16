package nl.ags.picum.UI.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

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

    private final MutableLiveData<List<Sight>> sightsLiveData = new MutableLiveData<>();

    public LiveData<List<Sight>> getSights() {
        return sightsLiveData;
    }

    public void setSights(List<Sight> sights)
    {
        this.sightsLiveData.postValue(sights);
    }


}
