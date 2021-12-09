package nl.ags.picum.dataStorage.roomData;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SightDAO {

    @Query("SELECT * FROM Sight WHERE sightName = :sightName")
    Sight getSight(String sightName);

    @Query("SELECT * FROM Sight")
    List<Sight> getAllSights();

    //TODO
    @Query("SELECT * FROM Sight WHERE sightName = :routeName")
    List<Sight> getSightsPerRoute(String routeName);
}
