package nl.ags.picum.dataStorage.linkingTables;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

import nl.ags.picum.dataStorage.roomData.Route;
import nl.ags.picum.dataStorage.roomData.Waypoint;

public class WaypointWithRouts {
    @Embedded
    private Waypoint waypoint;

    @Relation(
            parentColumn = "waypointID",
            entityColumn = "routeName",
            associateBy = @Junction(RouteWaypointCrossRef.class)
    )

    private List<Route> routes;
}
