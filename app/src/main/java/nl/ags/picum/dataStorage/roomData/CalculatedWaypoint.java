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

    @ColumnInfo(name = "Instructions")
    private String instructions ;

    @ColumnInfo(name = "ManeuverType")
    private int maneuverType;

    @ColumnInfo(name = "StreetName")
    private String streetName;

    private String routeName;

    public CalculatedWaypoint() {

    }
    @Ignore
    public CalculatedWaypoint(float latitude, float longitude, String routeName, String instructions, int maneuverType, String name) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.routeName = routeName;
        this.instructions = instructions;
        this.maneuverType = maneuverType;
        this.streetName = name;
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

    public String getInstructions() {
        return instructions;
    }

    public int getManeuverType() {
        return maneuverType;
    }


    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public void setManeuverType(int maneuverType) {
        this.maneuverType = maneuverType;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String steetName) {
        this.streetName = steetName;
    }
}
