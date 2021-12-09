package nl.ags.picum.dataStorage.roomData;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Route.class, Sight.class, Waypoint.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract RouteDAO routeDAO();
    public abstract SightDAO sightDAO();
    public abstract WaypointDAO waypointDAO();
}
