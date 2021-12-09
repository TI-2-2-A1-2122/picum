package nl.ags.picum.dataStorage.linkingTables;

import androidx.room.Entity;

@Entity(primaryKeys = {"routeName", "waypointID"})
public class RouteWaypointCrossRef {
    private String routeName;
    private int waypointID;
}
