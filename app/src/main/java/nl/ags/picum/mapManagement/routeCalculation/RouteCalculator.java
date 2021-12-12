package nl.ags.picum.mapManagement.routeCalculation;

import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import nl.ags.picum.dataStorage.roomData.Waypoint;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RouteCalculator {
    public static String LOG_TAG = RouteCalculator.class.getName();

    private RouteCalculatorListener listener;

    public RouteCalculator(RouteCalculatorListener listener) {
        this.listener = listener;
    }

    public void calculate(List<Waypoint> waypointList) {


    }


}
