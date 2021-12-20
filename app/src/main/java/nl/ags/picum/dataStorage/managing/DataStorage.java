package nl.ags.picum.dataStorage.managing;

import java.util.List;

import nl.ags.picum.dataStorage.dataUtil.Point;
import nl.ags.picum.dataStorage.roomData.CalculatedWaypoint;
import nl.ags.picum.dataStorage.roomData.Route;
import nl.ags.picum.dataStorage.roomData.Sight;
import nl.ags.picum.dataStorage.roomData.Waypoint;
import nl.ags.picum.mapManagement.routeCalculation.PointWithInstructions;

public interface DataStorage {
    List<Route> getRoutes();
    Route getCurrentRoute();
    void setCurrentRoute(Route route);

    void stopRoute(Route route);

    List<Waypoint> getHistory(Route route);
    void clearHistory(Route route);

    void setWaypointProgress(int waypoint, boolean state);

    List<Waypoint> getWaypointsPerRoute(Route r);

    List<Sight> getSightsPerRoute(Route route);

    List<Waypoint> getWaypointsWithSight(Route route);

    Point getPointFromWaypoint(Waypoint waypoint);

    Waypoint getWaypointFromSight(Sight s);

    Point getPointFromSight(String sightName);

    void setCalculatedWaypoints(List<PointWithInstructions> points, Route route);

    List<CalculatedWaypoint> getCalculatedWaypointsFromRoute(Route route);
}
