package nl.ags.picum;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import nl.ags.picum.dataStorage.linkingTables.RouteWaypointCrossRef;
import nl.ags.picum.dataStorage.managing.AppDatabaseManager;
import nl.ags.picum.dataStorage.roomData.Route;
import nl.ags.picum.dataStorage.roomData.Sight;
import nl.ags.picum.dataStorage.roomData.Waypoint;

public class MainActivity extends AppCompatActivity {
    private AppDatabaseManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manager = AppDatabaseManager.getInstance(getApplicationContext());
        manager.getCurrentRoute();

        /*
        new Thread(() -> {
            fillTable();
        }).start();
         */
    }

    private void fillTable() {
        Route route = new Route("Historische kilometer", "bla bla bla", 2, true);

        Waypoint w1 = new Waypoint(1,false, -122.93464f, 78.15937f);
        Waypoint w2 = new Waypoint(2,false, 26.42477f, -20.68753f);

        Sight s = new Sight("Taj hamal", 2, "bla bla bla", "http://youtube.com", 2);

        RouteWaypointCrossRef crossRef = new RouteWaypointCrossRef(route.getRouteName(), w1.getWaypointID());
        RouteWaypointCrossRef crossRef2 = new RouteWaypointCrossRef(route.getRouteName(), w2.getWaypointID());

        this.manager.setRoute(route);

        this.manager.setWaypoint(w1);
        this.manager.setWaypoint(w2);

        this.manager.setSight(s);

        this.manager.setRouteWaypoint(crossRef);
        this.manager.setRouteWaypoint(crossRef2);
    }
}