package nl.ags.picum.dataStorage.roomData;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.osmdroid.util.GeoPoint;

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
    @Ignore
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

    public GeoPoint toGeoPoint() {
        // long, lat are reversed in database, so correcting here
        return new GeoPoint(longitude, latitude);
    }

    @Override
    public String toString() {
        return "Waypoint{" +
                "waypointID=" + waypointID +
                ", visited=" + visited +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}
