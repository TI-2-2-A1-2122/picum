package nl.ags.picum.dataStorage.roomData;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import nl.ags.picum.dataStorage.linkingTables.WaypointWithSight;

@Dao
public interface SightDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertSight(Sight sight);

    @Query("SELECT * FROM Sight WHERE sightName = :sightName")
    Sight getSight(String sightName);

    @Query("SELECT * FROM Sight")
    List<Sight> getAllSights();

    @Query("SELECT * FROM Waypoint WHERE waypointID =:waypointID")
    List<WaypointWithSight> getSightWithWaypoint(int waypointID);
}
