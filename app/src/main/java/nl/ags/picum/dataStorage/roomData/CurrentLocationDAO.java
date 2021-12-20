package nl.ags.picum.dataStorage.roomData;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import nl.ags.picum.dataStorage.linkingTables.RouteWithCurrentLocations;
import nl.ags.picum.dataStorage.linkingTables.RouteWithWaypoints;

@Dao
public interface CurrentLocationDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertLocation(CurrentLocation location);

    @Transaction
    @Query("SELECT * FROM Route WHERE routeName =:routeName")
    List<RouteWithCurrentLocations> getCurrentLocationsPerRoute(String routeName);
}
