package nl.ags.picum.dataStorage.managing;

import java.util.List;

import nl.ags.picum.dataStorage.roomData.AppDatabase;
import nl.ags.picum.dataStorage.roomData.Route;
import nl.ags.picum.dataStorage.roomData.Waypoint;

public class AppDatabaseManager implements DataStorage {
    private static AppDatabase database;

    public AppDatabaseManager() {
        
    }

    public static AppDatabase getInstance() {
        if (database == null) {
            database = new AppDatabase();
        }

        return database;
    }

    @Override
    public List<Route> getRoutes() {
        return null;
    }

    @Override
    public Route getCurrentRoute() {
        return null;
    }

    @Override
    public void setCurrentRoute(Route route) {

    }

    @Override
    public List<Waypoint> getHistory(Route route) {
        return null;
    }

    @Override
    public void setHistory(Route route, List<Waypoint> waypoints) {

    }

    @Override
    public void appendHistory(Route route, Waypoint waypoint) {

    }

    @Override
    public void clearHistory(Route route) {

    }
}
