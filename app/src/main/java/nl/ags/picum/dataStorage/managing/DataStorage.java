package nl.ags.picum.dataStorage.managing;

import java.util.List;

import nl.ags.picum.dataStorage.roomData.Route;
import nl.ags.picum.dataStorage.roomData.Sight;
import nl.ags.picum.dataStorage.roomData.Waypoint;

public interface DataStorage {
    List<Route> getRoutes();
    Route getCurrentRoute();
    void setCurrentRoute(Route route);

    List<Waypoint> getHistory(Route route);
    void clearHistory(Route route);

    void setWaypointProgress(int waypoint, boolean state);

    List<Waypoint> getWaypointsPerRoute(Route r);

    List<Sight> getSightsPerRoute(Route route);
}
