package nl.ags.picum.dataStorage.linkingTables;

import androidx.room.Embedded;
import androidx.room.Relation;

import nl.ags.picum.dataStorage.roomData.Sight;
import nl.ags.picum.dataStorage.roomData.Waypoint;

public class WaypointWithSight {
    @Embedded
    public Waypoint waypoint;

    @Relation(
            parentColumn = "waypointID",
            entityColumn = "waypointID"
    )

    public Sight sight;
}
