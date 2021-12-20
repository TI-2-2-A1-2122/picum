package nl.ags.picum.dataStorage.roomData;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import nl.ags.picum.dataStorage.linkingTables.RouteWithWaypoints;
import nl.ags.picum.dataStorage.linkingTables.WaypointWithSight;

@Dao
public interface WaypointDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addWaypoint(Waypoint waypoint);

    @Query("SELECT * FROM Waypoint WHERE waypointID = :waypointID")
    Waypoint getWaypoint(int waypointID);

    @Query("SELECT * FROM Waypoint")
    List<Waypoint> getAllWaypoints();

    @Query("UPDATE Waypoint SET visited = :state WHERE waypointID = :waypointID")
    void setProgress(boolean state, int waypointID);

    @Transaction
    @Query("SELECT * FROM Route WHERE routeName =:routeName")
    List<RouteWithWaypoints> getWaypointsPerRoute(String routeName);

    @Transaction
    @Query("SELECT * FROM Sight WHERE sightName =:sightName")
    List<WaypointWithSight> getWaypointPerSight(String sightName);


}
