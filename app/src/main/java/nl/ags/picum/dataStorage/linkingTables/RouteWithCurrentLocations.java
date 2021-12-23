package nl.ags.picum.dataStorage.linkingTables;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import nl.ags.picum.dataStorage.roomData.CurrentLocation;
import nl.ags.picum.dataStorage.roomData.Route;

public class RouteWithCurrentLocations {
    @Embedded
    public Route route;

    @Relation(
            parentColumn = "routeName",
            entityColumn = "routeName"
    )

    public List<CurrentLocation> locations;
}
