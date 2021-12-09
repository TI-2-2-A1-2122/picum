package nl.ags.picum.dataStorage.roomData;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Waypoint {
    @PrimaryKey
    private int waypointID;

    @ColumnInfo(name = "Visited")
    private boolean visited;

    @ColumnInfo(name = "Longitude")
    private float longitude;

    @ColumnInfo(name = "Latitude")
    private float latitude;

    public Waypoint() {

    }

    public Waypoint(int waypointID, boolean visited, float longitude, float latitude) {
        this.waypointID = waypointID;
        this.visited = visited;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public int getWaypointID() {
        return waypointID;
    }

    public void setWaypointID(int waypointID) {
        this.waypointID = waypointID;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }
}
