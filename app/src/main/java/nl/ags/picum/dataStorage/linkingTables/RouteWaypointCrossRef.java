package nl.ags.picum.dataStorage.linkingTables;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(primaryKeys = {"routeName", "waypointID"})
public class RouteWaypointCrossRef {
    @NonNull
    public String routeName;
    public int waypointID;

    public RouteWaypointCrossRef(@NonNull String routeName, int waypointID) {
        this.routeName = routeName;
        this.waypointID = waypointID;
    }
}
