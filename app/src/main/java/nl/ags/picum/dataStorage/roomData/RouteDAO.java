package nl.ags.picum.dataStorage.roomData;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RouteDAO {

    @Query("SELECT * FROM Route WHERE routeName = :routeName")
    Route getRoute(String routeName);

    @Query("SELECT * FROM Route")
    List<Route> getAllRoutes();
}
