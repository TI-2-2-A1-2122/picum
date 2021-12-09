package nl.ags.picum.dataStorage.roomData;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

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

    //TODO
    @Query("SELECT * FROM Waypoint")
    List<Waypoint> getWaypointsPerRoute();
}
