package nl.ags.picum.dataStorage.roomData;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RouteDAO {

    @Query("SELECT * FROM Route WHERE inProgress = 1")
    Route getCurrentRoute();

    @Query("UPDATE Route SET inProgress = 1 WHERE routeName = :routeName")
    void setRoute(String routeName);

    @Query("SELECT * FROM Route")
    List<Route> getAllRoutes();
}
