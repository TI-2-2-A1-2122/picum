package nl.ags.picum.dataStorage.roomData;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import nl.ags.picum.dataStorage.linkingTables.RouteWithCalculatedWaypoints;

@Dao
public interface CalculatedWaypointDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCalculatedWaypoint(CalculatedWaypoint waypoint);

    @Transaction
    @Query("SELECT * FROM Route WHERE routeName =:routeName")
    List<RouteWithCalculatedWaypoints> getCalculatedWaypointsPerRoute(String routeName);
}
