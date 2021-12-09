package nl.ags.picum.dataStorage.linkingTables;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

import nl.ags.picum.dataStorage.roomData.Route;
import nl.ags.picum.dataStorage.roomData.Waypoint;

public class RouteWithWaypoints {
    @Embedded
    public Route route;

    @Relation(
            parentColumn = "routeName",
            entityColumn = "waypointID",
            associateBy = @Junction(RouteWaypointCrossRef.class)
    )

    public List<Waypoint> waypoints;
}
