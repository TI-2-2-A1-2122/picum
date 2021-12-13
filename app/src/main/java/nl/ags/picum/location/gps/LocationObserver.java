package nl.ags.picum.location.gps;

public interface LocationObserver {


    //VOOR JESSE
    void onLocationError();
    void onLocationUpdate();
    void onNearLocationEntered();

}
