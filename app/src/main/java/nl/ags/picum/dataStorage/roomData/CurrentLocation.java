package nl.ags.picum.dataStorage.roomData;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CurrentLocation {

    @PrimaryKey
    @NonNull
    private int CurrentLocationID;

    @ColumnInfo(name = "Latitude")
    @NonNull
    private float latitude;

    @ColumnInfo(name = "Longitude")
    @NonNull
    private float longitude;

    @NonNull
    private String routeName;

    public CurrentLocation() {

    }

    public CurrentLocation(int currentLocationID, float latitude, float longitude, @NonNull String routeName) {
        this.CurrentLocationID = currentLocationID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.routeName = routeName;
    }

    public int getCurrentLocationID() {
        return CurrentLocationID;
    }

    public void setCurrentLocationID(int currentLocationID) {
        CurrentLocationID = currentLocationID;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    @NonNull
    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(@NonNull String routeName) {
        this.routeName = routeName;
    }
}
