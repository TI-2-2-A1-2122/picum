package nl.ags.picum.dataStorage.roomData;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import nl.ags.picum.dataStorage.linkingTables.RouteWaypointCrossRef;

@Dao
public interface RouteDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertRoute(Route route);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertRouteWaypointCrossRef(RouteWaypointCrossRef crossRef);

    @Query("SELECT * FROM Route WHERE inProgress = 1")
    Route getCurrentRoute();

    @Query("UPDATE Route SET inProgress = 1 WHERE routeName = :routeName")
    void setRoute(String routeName);

    @Query("SELECT * FROM Route")
    List<Route> getAllRoutes();

}
