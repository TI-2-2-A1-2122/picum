package nl.ags.picum.dataStorage.managing;

import java.util.List;

import nl.ags.picum.dataStorage.roomData.Route;
import nl.ags.picum.dataStorage.roomData.Waypoint;

public interface DataStorage {
    List<Route> getRoutes();
    Route getCurrentRoute();
    void setCurrentRoute(Route route);

    List<Waypoint> getHistory(Route route);
    void setHistory(Route route, List<Waypoint> waypoints);
    void appendHistory(Route route, Waypoint waypoint);
    void clearHistory(Route route);
}
