package nl.ags.picum.dataStorage.linkingTables;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

import nl.ags.picum.dataStorage.roomData.Route;
import nl.ags.picum.dataStorage.roomData.Waypoint;

public class RouteWithWaypoints {
    @Embedded
    private Route route;

    @Relation(
            parentColumn = "routeName",
            entityColumn = "waypointID",
            associateBy = @Junction(RouteWaypointCrossRef.class)
    )

    private List<Waypoint> waypoints;
}
