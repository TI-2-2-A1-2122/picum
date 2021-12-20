package nl.ags.picum.dataStorage.roomData;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import nl.ags.picum.dataStorage.linkingTables.RouteWaypointCrossRef;

@Database(entities = {Route.class, Sight.class, Waypoint.class, RouteWaypointCrossRef.class, CurrentLocation.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract RouteDAO routeDAO();
    public abstract SightDAO sightDAO();
    public abstract WaypointDAO waypointDAO();
    public abstract CurrentLocationDAO currentLocationDAO();
}
