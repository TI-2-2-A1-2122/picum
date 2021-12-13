package nl.ags.picum.UI.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import nl.ags.picum.dataStorage.roomData.Sight;

public class SightViewModel extends ViewModel {

    private MutableLiveData<Sight> currentSight;

    public LiveData<Sight> getCurrentSight()
    {
        if (currentSight == null) {
            currentSight = new MutableLiveData<Sight>();
        }
        return currentSight;
    }

    public void setCurrentSight(Sight sight)
    {
        this.currentSight.postValue(sight);
    }

    private MutableLiveData<List<Sight>> sightsLiveData;

    public LiveData<List<Sight>> getSights() {
        if (sightsLiveData == null) {
            sightsLiveData = new MutableLiveData<List<Sight>>();
        }
        return sightsLiveData;
    }

    public void setSights(List<Sight> sights)
    {
        this.sightsLiveData.postValue(sights);
    }


}
