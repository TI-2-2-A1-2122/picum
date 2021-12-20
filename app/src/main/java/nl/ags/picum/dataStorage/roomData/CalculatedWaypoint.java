package nl.ags.picum.dataStorage.roomData;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class CalculatedWaypoint {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int CalculatedWaypointID;

    @ColumnInfo(name = "Latitude")
    @NonNull
    private float latitude;

    @ColumnInfo(name = "Longitude")
    @NonNull
    private float longitude;

    private String routeName;

    public CalculatedWaypoint() {

    }
    @Ignore
    public CalculatedWaypoint(float latitude, float longitude, String routeName) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.routeName = routeName;
    }

    public int getCalculatedWaypointID() {
        return CalculatedWaypointID;
    }

    public void setCalculatedWaypointID(int calculatedWaypointID) {
        CalculatedWaypointID = calculatedWaypointID;
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

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }
}
