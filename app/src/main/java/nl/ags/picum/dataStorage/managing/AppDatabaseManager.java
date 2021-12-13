package nl.ags.picum.dataStorage.managing;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import nl.ags.picum.dataStorage.linkingTables.RouteWaypointCrossRef;
import nl.ags.picum.dataStorage.linkingTables.RouteWithWaypoints;
import nl.ags.picum.dataStorage.linkingTables.WaypointWithSight;
import nl.ags.picum.dataStorage.roomData.AppDatabase;
import nl.ags.picum.dataStorage.roomData.Route;
import nl.ags.picum.dataStorage.roomData.Sight;
import nl.ags.picum.dataStorage.roomData.Waypoint;

public class AppDatabaseManager implements DataStorage {
    private static AppDatabaseManager databaseManager;
    private AppDatabase database;
    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

        }
    };

    public AppDatabaseManager(Context context) {
        database = Room.databaseBuilder(context, AppDatabase.class, "Picum_DB").addMigrations(MIGRATION_1_2).createFromAsset("database/picumdb.db").build();
    }

    public static AppDatabaseManager getInstance(Context context) {
        if (databaseManager == null) {
            databaseManager = new AppDatabaseManager(context);
        }

        return databaseManager;
    }

    @Override
    public List<Route> getRoutes() {
        return this.database.routeDAO().getAllRoutes();
    }

    @Override
    public Route getCurrentRoute() {
        return this.database.routeDAO().getCurrentRoute();
    }

    @Override
    public void setCurrentRoute(Route route) {
        this.database.routeDAO().setRoute(route.getRouteName());
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

    public void setRoute(Route route) {
        this.database.routeDAO().insertRoute(route);
    }

    public void setWaypoint(Waypoint waypoint) {
        this.database.waypointDAO().addWaypoint(waypoint);
    }

    public void setSight(Sight sight) {
        this.database.sightDAO().insertSight(sight);
    }

    public void setWaypointProgress(int waypoint) {
        this.database.waypointDAO().setProgress(true, waypoint);
    }

    public void setRouteWaypoint(RouteWaypointCrossRef crossRef) {
        this.database.routeDAO().insertRouteWaypointCrossRef(crossRef);
    }

    public List<Waypoint> getWaypointsPerRoute(Route r) {
        List<RouteWithWaypoints> routeWithWaypoints = this.database.waypointDAO().getWaypointsPerRoute(r.getRouteName());
        List<Waypoint> waypoints = new ArrayList<>();

        for (RouteWithWaypoints i : routeWithWaypoints) {
            waypoints.addAll(i.waypoints);
        }

        return waypoints;
    }

    public List<Sight> getSightsPerRoute(Route route) {
        List<Sight> sights = new ArrayList<>();
        List<RouteWithWaypoints> waypointsPerRoute = database.waypointDAO().getWaypointsPerRoute(route.getRouteName());

        for (RouteWithWaypoints r : waypointsPerRoute) {
            List<Waypoint> waypoints = r.waypoints;

            for (Waypoint w : waypoints) {
                List<WaypointWithSight> waypointsight = database.sightDAO().getSightWithWaypoint(w.getWaypointID());

                for (WaypointWithSight s : waypointsight) {
                    if (s.sight != null)
                        sights.add(s.sight);
                }
            }
        }

        return sights;
    }
}
